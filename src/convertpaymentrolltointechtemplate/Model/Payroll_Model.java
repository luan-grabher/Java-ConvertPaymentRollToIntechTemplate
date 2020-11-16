package convertpaymentrolltointechtemplate.Model;

import Dates.Dates;
import fileManager.Args;
import fileManager.FileManager;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import sql.Database;

public class Payroll_Model {

    private final File file;
    private final Map<String, String> values;
    private final List<Map<String, String>> salaries = new ArrayList<>();
    private Integer year;
    private Integer month;
    
    private final String sqlGetCPF = FileManager.getText(FileManager.getFile("sql/getEmployeeCPF.sql"));

    /**
     * Cria a classe com o arquivo csv da folha de pagamento
     *
     * @param file Arquivo fornecido pelo usuário
     */
    public Payroll_Model(File file) {
        this.values = new HashMap<>();
        this.file = file;
    }

    /**
     * Extrai os dados para um para mapa <String,String> que guarda o nome do
     * valor e o valor em String e uma Lista de Mapa<String,String> que guarda
     * os salarios    
     */
    public void extractDataOfFile() {
        //Pega linhas arquivo
        String text = FileManager.getText(file);
        String[] lines = text.split("\r\n");
        
        //Extrai data
        Pattern p = Pattern.compile("[A-Z]+\\/[0-9]+");
        String firstCol = lines[0].split(";")[0];
        Matcher m = p.matcher(firstCol);
        if(m.find()){
            String[] dateString = m.group().split("/");
            year = Integer.valueOf(dateString[1]);
            String monthName = dateString[0];
            
            //Pega os meses em lista e pega o index onde está aquele mes pra descobrir o mes
            month = Args.indexOf((String[]) Dates.getBrazilianMonths().toArray(), Args.INDEX_OF_SEARCH_TYPE_EQUALS, monthName) + 1;
            values.put("Mês", month.toString());
            values.put("Ano", year.toString());
            
            Map<String,String> lastSalary = new HashMap<>();
            
            //Percorre linhas
            for (String line : lines) {
                //Divide colunas em uma lista
                List<String> cols = new ArrayList<>(Arrays.asList(line.split(";")));
                //Remove colunas em branco
                cols.removeAll(Arrays.asList(""));

                //Se tiver escrito "Admissao em", grava o nome e codigo
                if(line.contains("Admissão em")){
                    lastSalary.put("Codigo", cols.get(0));
                    lastSalary.put("Nome", cols.get(1));
                }else if(line.contains("Líquido - >")){
                    //Se tiver 'Liquido', tem o salario
                    lastSalary.put("Salario", cols.get(1));   
                    //lastSalary.put("CPF", getEmployeeCPF(lastSalary.get("Codigo")));   
                    //coloca na lista de salarios
                    salaries.add(new HashMap<>(lastSalary));
                    //Limpa o salario atual
                    lastSalary.clear();
                }else if(cols.get(0).contains("FGTS GRF 8%")){
                    values.put("FGTS", cols.get(1).replaceAll("[^0-9,]", ""));
                }else if(cols.get(0).contains("GPS - >")){
                    values.put("INSS", cols.get(1).replaceAll("[^0-9,]", ""));
                }else if(line.contains("Empresa: ")){
                    //Pega número da empresa e CNPJ
                    values.put("empresa", cols.get(0).split(":")[1].split("-")[0].trim());
                    values.put("CNPJ", cols.get(1).split(":")[1].trim());
                }
            }
        }else{
            throw new Error("Data não encontrada na primeira linha do arquivo!");
        }
    }
    
    public String getEmployeeCPF(String employeeCode){
         Map<String,String> swaps = new HashMap<>();
         swaps.put("enterprise", values.get("empresa"));
         swaps.put("employee", employeeCode);
         
         List<String[]> select = Database.getDatabase().select(sqlGetCPF, swaps);
         
         if(!select.isEmpty() && !"".equals(select.get(0)[0]) ){
             return select.get(0)[0];
         }else{
             throw new Error("Não foi possivel encontrar o CPF do funcionário de código " + employeeCode);
         }
    }

    public Map<String, String> getValues() {
        return values;
    }

    public List<Map<String, String>> getSalaries() {
        return salaries;
    }    
}
