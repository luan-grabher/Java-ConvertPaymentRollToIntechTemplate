package convertpaymentrolltointechtemplate.Model;

import Dates.Dates;
import convertpaymentrolltointechtemplate.ConvertPaymentRollToIntechTemplate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Entry_Model {

    private List<Map<String, String>> entries = new ArrayList<>();
    private final Map<String, String> payrollValues;
    private final List<Map<String, String>> salaries;
    private Map<String, String> defaultCols = new HashMap<>();

    private String ultimoDiaMes = "";
    private String penultimoDiaMes = "";
    private String dia20ProximoMes = "";
    private String penultimoDiaProximoMes = "";

    public Entry_Model(Map<String, String> payrollValues, List<Map<String, String>> salaries) {
        this.payrollValues = payrollValues;
        this.salaries = salaries;

        //Descrição de mes e ano
        this.payrollValues.put("Mês e Ano", Dates.getMonthAbbr_PtBr(Integer.valueOf(payrollValues.get("Mês")) - 1).toUpperCase() + " " + payrollValues.get("Ano"));
        createDates();

        //Adiciona os valores das colunas padrões
        addDefaultCol("Cod Fundacao");
        addDefaultCol("Tipo da Previsao");
        addDefaultCol("Cod Ccusto Emissor");
        addDefaultCol("CPFCNPJ");
        addDefaultCol("Cod Banco");
        addDefaultCol("Cod Agencia");
        addDefaultCol("DV Agencia");
        addDefaultCol("Conta Corrente");
        addDefaultCol("DV Conta Corrente");
        //addDefaultCol("Cod Natureza");
        addDefaultCol("Cod Empresa");
        addDefaultCol("Cod Plano");
        addDefaultCol("Cod Perfil");
        addDefaultCol("Cod Fundo");
        addDefaultCol("Cod Custeio");
        addDefaultCol("Cod Ccusto");
    }

    /**
     * Adiciona o valor no arquivo ini informado no mapa de colunas padrao
     *
     * @param name Nome que irá ficar na key do mapa e nome que está no arquivo
     * ini
     */
    private void addDefaultCol(String name) {
        defaultCols.put(name, getIniVal(name));
    }

    /**
     * Retorna o valor da variavel name na seção
     * ConvertPaymentRollToIntechTemplate do arquivo ini
     *
     * @param name Nome do valor procurado na seçao
     *
     * @return o valor da variavel name na seção
     * ConvertPaymentRollToIntechTemplate do arquivo ini
     */
    private String getIniVal(String name) {
        return ConvertPaymentRollToIntechTemplate.ini.get("ConvertPaymentRollToIntechTemplate", name);
    }

    /**
     * Cria Datas
     */
    private void createDates() {
        Integer month = Integer.valueOf(payrollValues.get("Mês"));
        Integer year = Integer.valueOf(payrollValues.get("Ano"));

        Calendar firstDay = Dates.getCalendarFromFormat("01/" + month + "/" + year, "dd/MM/yy");
        ultimoDiaMes = firstDay.getActualMaximum(Calendar.DAY_OF_MONTH) + "/" + month + "/" + year;
        
        Calendar penultimoDiaCalendar = Dates.getCalendarFromFormat(ultimoDiaMes, "dd/MM/yy");
        penultimoDiaCalendar.add(Calendar.DAY_OF_MONTH, -1);
        penultimoDiaMes = Dates.getCalendarInThisStringFormat(penultimoDiaCalendar, "dd/MM/yyyy");
        
        Calendar dia20ProximoMesCalendar = Dates.getCalendarFromFormat(ultimoDiaMes, "dd/MM/yy");
        dia20ProximoMesCalendar.set(Calendar.DAY_OF_MONTH, 20);
        dia20ProximoMesCalendar.add(Calendar.MONTH, 1);
        dia20ProximoMes = Dates.getCalendarInThisStringFormat(dia20ProximoMesCalendar, "dd/MM/yyyy");
        
        Calendar penultimoDiaProximoMesCalendar = Dates.getCalendarFromFormat(dia20ProximoMes, "dd/MM/yy");
        penultimoDiaProximoMesCalendar.set(Calendar.DAY_OF_MONTH, dia20ProximoMesCalendar.getActualMaximum(Calendar.DAY_OF_MONTH) -1);        
        penultimoDiaProximoMes = Dates.getCalendarInThisStringFormat(penultimoDiaProximoMesCalendar, "dd/MM/yyyy");
    }

    /**
     * Adiciona lançamentos de salario no mapa de lançamentos
     */
    public void addSalariesToEntries() {
        //Percorre todos salarios
        for (Map<String, String> salary : salaries) {
            try{
                Map<String, String> cols = new HashMap<>();
                cols.put("Descricao", "FOLHA SALARIAL");
                cols.put("Obs Geral", salary.get("Nome").toUpperCase());
                cols.put("Data Emissao", penultimoDiaMes);
                cols.put("Data Vencimento", penultimoDiaProximoMes);
                cols.put("Data Competencia Contabil", ultimoDiaMes);
                //cols.put("CPFCNPJ", salary.get("CPF"));
                cols.put("Hist Contabil", "PG SALARIO " + payrollValues.get("Mês e Ano"));
                cols.put("Cod Natureza", "FF01");
                cols.put("Valor Bruto", salary.get("Salario"));
                cols.put("Nosso Numero", "");
                cols.put("Tipo Conta Bancaria", "COR");

                entries.add(cols);
            }catch(Exception e){                
            }
        }
    }

    /**
     * Adiciona lançamento de FGTS no mapa de lançamentos
     */
    public void addFGTSToEntry() {
        Map<String, String> cols = new HashMap<>();
        cols.put("Descricao", "FGTS");
        cols.put("Obs Geral", "FGTS FL SAL " + payrollValues.get("Mês e Ano"));
        cols.put("Data Emissao", ultimoDiaMes);
        cols.put("Data Vencimento", dia20ProximoMes);
        cols.put("Data Competencia Contabil", ultimoDiaMes);
        //cols.put("CPFCNPJ", payrollValues.get("CNPJ"));
        cols.put("Hist Contabil", "PG FGTS FOLHA SALARIAL " + payrollValues.get("Mês e Ano"));
        cols.put("Cod Natureza", "FF03");
        cols.put("Valor Bruto", payrollValues.get("FGTS"));
        cols.put("Nosso Numero", "");
        cols.put("Tipo Conta Bancaria", "COR");

        entries.add(cols);
    }

    /**
     * Adiciona lançamento de INSS no mapa de lançamentos
     */
    public void addINSSToEntry() {
        Map<String, String> cols = new HashMap<>();
        cols.put("Descricao", "INSS");
        cols.put("Obs Geral", "INSS FL SAL " + payrollValues.get("Mês e Ano"));
        cols.put("Data Emissao", ultimoDiaMes);
        cols.put("Data Vencimento", dia20ProximoMes);
        cols.put("Data Competencia Contabil", ultimoDiaMes);
        //cols.put("CPFCNPJ", payrollValues.get("CNPJ"));
        cols.put("Hist Contabil", "PG GPS INSS FOLHA SALARIAL " + payrollValues.get("Mês e Ano"));
        cols.put("Cod Natureza", "FF02");
        cols.put("Valor Bruto", payrollValues.get("INSS"));
        cols.put("Nosso Numero", "");
        cols.put("Tipo Conta Bancaria", "COR");

        entries.add(cols);
    }

    /**
     * Cria uma string com o mapa de lançamentos no formato do template de
     * provisão CSV
     *
     * @return Retorna uma string com o mapa de lançamentos no formato do
     * template de provisão CSV
     */
    public String getImportText() {
        StringBuilder text = new StringBuilder();

        //Percorre lançamentos
        for (Map<String, String> entry : entries) {
            text.append(defaultCols.get("Cod Fundacao")).append(";");
            text.append(defaultCols.get("Tipo da Previsao")).append(";");
            text.append(entry.get("Descricao")).append(";");
            text.append(entry.get("Obs Geral")).append(";");
            text.append(defaultCols.get("Cod Ccusto Emissor")).append(";");
            text.append(entry.get("Data Emissao")).append(";");
            text.append(entry.get("Data Vencimento")).append(";");
            text.append(entry.get("Data Competencia Contabil")).append(";");
            text.append(defaultCols.get("CPFCNPJ")).append(";");
            text.append(defaultCols.get("Cod Banco")).append(";");
            text.append(defaultCols.get("Cod Agencia")).append(";");
            text.append(defaultCols.get("DV Agencia")).append(";");
            text.append(defaultCols.get("Conta Corrente")).append(";");
            text.append(defaultCols.get("DV Conta Corrente")).append(";");
            text.append(entry.get("Hist Contabil")).append(";");
            text.append(entry.get("Cod Natureza")).append(";");
            text.append(defaultCols.get("Cod Empresa")).append(";");
            text.append(defaultCols.get("Cod Plano")).append(";");
            text.append(defaultCols.get("Cod Perfil")).append(";");
            text.append(defaultCols.get("Cod Fundo")).append(";");
            text.append(defaultCols.get("Cod Custeio")).append(";");
            text.append(defaultCols.get("Cod Ccusto")).append(";");
            text.append(entry.get("Valor Bruto")).append(";");
            text.append(entry.get("Nosso Numero")).append(";");
            text.append(entry.get("Tipo Conta Bancaria")).append(";");
            text.append("\r\n");
        }

        return text.toString();
    }
}
