package convertpaymentrolltointechtemplate.Model;

import fileManager.FileManager;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Payroll_Model {

    private final File file;
    private final Map<String, String> values;
    private final List<Map<String, String>> salaries = new ArrayList<>();

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
            String dateString = m.group();

            //Percorre linhas
            for (String line : lines) {

            }
        }else{
            throw new Error("Data não encontrada na primeira linha do arquivo!");
        }
    }

    public Map<String, String> getValues() {
        return values;
    }

    public List<Map<String, String>> getSalaries() {
        return salaries;
    }    
}
