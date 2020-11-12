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
        addDefaultCol("Cod Custo");
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
            cols.put("Data Emissao", dataEmissao);
            cols.put("Data Vencimento", dataVencimento);
            cols.put("Data Competencia Contabil", dataCompetenciaContabil);
            cols.put("CPFCNPJ", salary.get("CPF"));
            cols.put("Hist Contabil", "Salário pago ao colaborador " + salary.get("Nome"));
            cols.put("Valor Bruto", salary.get("Salario"));
            cols.put("Nosso Numero", salary.get("Salario"));
            cols.put("Tipo Conta Bancaria", "SAL");

            entries.add(cols);
        }
    }

    public void addFGTSToEntries() {
        Map<String, String> cols = new HashMap<>();
        cols.put("Descricao", "FGTS");
        cols.put("Data Emissao", dataEmissao);
        cols.put("Data Vencimento", dataVencimento);//Trocar para data do proximo mês e pá
        cols.put("Data Competencia Contabil", dataCompetenciaContabil);
        cols.put("CPFCNPJ", payrollValues.get("CNPJ"));
        cols.put("Hist Contabil", "Pagamento FGTS mensal");
        cols.put("Valor Bruto", payrollValues.get("FGTS"));
        cols.put("Nosso Numero", payrollValues.get("FGTS"));
        cols.put("Tipo Conta Bancaria", "COR");

        entries.add(cols);
    }
    
    public void addINSSToEntries() {
        Map<String, String> cols = new HashMap<>();
        cols.put("Descricao", "INSS");
        cols.put("Data Emissao", dataEmissao);
        cols.put("Data Vencimento", dataVencimento);//Trocar para data do proximo mês e pá
        cols.put("Data Competencia Contabil", dataCompetenciaContabil);
        cols.put("CPFCNPJ", payrollValues.get("CNPJ"));
        cols.put("Hist Contabil", "Pagamento FGTS mensal");
        cols.put("Valor Bruto", payrollValues.get("INSS"));
        cols.put("Nosso Numero", payrollValues.get("INSS"));
        cols.put("Tipo Conta Bancaria", "COR");

        entries.add(cols);
    }
}
