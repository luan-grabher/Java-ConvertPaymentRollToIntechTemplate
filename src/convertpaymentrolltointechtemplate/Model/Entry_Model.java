package convertpaymentrolltointechtemplate.Model;

import convertpaymentrolltointechtemplate.ConvertPaymentRollToIntechTemplate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Entry_Model {

    private List<Map<String, String>> entries = new ArrayList<>();
    private final Map<String, String> payrollValues;
    private final List<Map<String, String>> salaries;
    private Map<String, String> defaultCols = new HashMap<>();

    private String dataEmissao = "30/10/2020";
    private String dataVencimento = "30/10/2020";
    private String dataCompetenciaContabil = "30/10/2020";

    public Entry_Model(Map<String, String> payrollValues, List<Map<String, String>> salaries) {
        this.payrollValues = payrollValues;
        this.salaries = salaries;

        //Adiciona os valores das colunas padrões
        addDefaultCol("Cod Fundacao");
        addDefaultCol("Tipo da Previsao");
        addDefaultCol("Obs Geral");
        addDefaultCol("Cod Ccusto Emissor");
        addDefaultCol("Cod Banco");
        addDefaultCol("Cod Agencia");
        addDefaultCol("DV Agencia");
        addDefaultCol("Conta Corrente");
        addDefaultCol("DV Conta Corrente");
        addDefaultCol("Cod Natureza");
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

    public void addSalariesToEntries() {
        //Percorre todos salarios
        for (Map<String, String> salary : salaries) {
            Map<String, String> cols = new HashMap<>();
            cols.put("Descricao", "Salário pago ao colaborador " + salary.get("Nome"));
            cols.put("Obs Geral", "Salário");
            cols.put("Data Emissao", dataEmissao);
            cols.put("Data Vencimento", dataVencimento);
            cols.put("Data Competencia Contabil", dataCompetenciaContabil);
            //cols.put("CPFCNPJ", salary.get("CPF"));
            cols.put("Hist Contabil", "Salário pago ao colaborador " + salary.get("Nome"));
            cols.put("Valor Bruto", salary.get("Salario"));
            cols.put("Nosso Numero", salary.get("Salario"));
            cols.put("Tipo Conta Bancaria", "SAL");

            entries.add(cols);
        }
    }

    public void addFGTSToEntry() {
        Map<String, String> cols = new HashMap<>();
        cols.put("Descricao", "FGTS");
        cols.put("Obs Geral", "FGTS");
        cols.put("Data Emissao", dataEmissao);
        cols.put("Data Vencimento", dataVencimento);//Trocar para data do proximo mês e pá
        cols.put("Data Competencia Contabil", dataCompetenciaContabil);
        //cols.put("CPFCNPJ", payrollValues.get("CNPJ"));
        cols.put("Hist Contabil", "Pagamento FGTS mensal");
        cols.put("Valor Bruto", payrollValues.get("FGTS"));
        cols.put("Nosso Numero", payrollValues.get("FGTS"));
        cols.put("Tipo Conta Bancaria", "COR");

        entries.add(cols);
    }

    public void addINSSToEntry() {
        Map<String, String> cols = new HashMap<>();
        cols.put("Descricao", "INSS");
        cols.put("Obs Geral", "INSS");
        cols.put("Data Emissao", dataEmissao);
        cols.put("Data Vencimento", dataVencimento);//Trocar para data do proximo mês e pá
        cols.put("Data Competencia Contabil", dataCompetenciaContabil);
        //cols.put("CPFCNPJ", payrollValues.get("CNPJ"));
        cols.put("Hist Contabil", "Pagamento FGTS mensal");
        cols.put("Valor Bruto", payrollValues.get("INSS"));
        cols.put("Nosso Numero", payrollValues.get("INSS"));
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
            text.append(defaultCols.get("Cod Natureza")).append(";");
            text.append(defaultCols.get("Cod Empresa")).append(";");
            text.append(defaultCols.get("Cod Plano")).append(";");
            text.append(defaultCols.get("Cod Perfil")).append(";");
            text.append(defaultCols.get("Cod Fundo")).append(";");
            text.append(defaultCols.get("Cod Custeio")).append(";");
            text.append(defaultCols.get("Cod Custo")).append(";");
            text.append(entry.get("Valor Bruto")).append(";");
            text.append(entry.get("Nosso Numero")).append(";");
            text.append(entry.get("Tipo Conta Bancaria")).append(";");
            text.append("\r\n");
        }
        
        return text.toString();
    }
}
