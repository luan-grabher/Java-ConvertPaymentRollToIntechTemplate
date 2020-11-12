package convertpaymentrolltointechtemplate.Model;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    }

    public Map<String, String> getValues() {
        return values;
    }

    public List<Map<String, String>> getSalaries() {
        return salaries;
    }    
}
