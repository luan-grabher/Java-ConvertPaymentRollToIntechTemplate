package convertpaymentrolltointechtemplate;

import convertpaymentrolltointechtemplate.Model.Entry_Model;
import convertpaymentrolltointechtemplate.Model.Payroll_Model;
import fileManager.Args;
import fileManager.FileManager;
import fileManager.Selector;
import java.io.File;
import javax.swing.JOptionPane;
import org.ini4j.Ini;
import sql.Database;

public class ConvertPaymentRollToIntechTemplate {

    public static Ini ini;

    public static void main(String[] args) {
        //Define ini
        try {
            String inipath = args != null ? Args.get(args, "ini_path") : "config.ini";
            ini = new Ini(new File(inipath == null ? "config.ini" : inipath));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Não foi possivel encontrar o arquivo de configuração!");
            System.exit(0);
        }

        try {
            //Conecta com banco de dados
            String databaseConfigPath = ini.get("Database", "configPath");
            Database.setStaticObject(new Database(databaseConfigPath));
            
            //Testa conexao database
            if(/*Database.getDatabase().testConnection()*/ true){                                    
                //Pega folha de pagamento
                File payrollFile = Selector.selectFile("", "CSV(Texto separado por virgulas)", ".csv");
                //Se o arquivo for valido
                if (Selector.verifyFile(payrollFile.getPath(), ".csv")) {

                    //Instancia o modelo folha
                    Payroll_Model payrollModel = new Payroll_Model(payrollFile);
                    //Extrai os dados do arquivo
                    payrollModel.extractDataOfFile();

                    //Instancia modelo lançamentos
                    Entry_Model entryModel = new Entry_Model(payrollModel.getValues(), payrollModel.getSalaries());
                    entryModel.addSalariesToEntries();
                    entryModel.addFGTSToEntry();
                    entryModel.addINSSToEntry();

                    //Cria o texto de importação
                    String importText = entryModel.getImportText();

                    //Salva csv na área de trabalho da pessoa
                    FileManager.save(
                            System.getProperty("user.home")
                            + "/Desktop/Intech "
                            + payrollModel.getValues().get("Ano")
                            + payrollModel.getValues().get("Mes")
                            + ".csv",
                            importText
                    );

                    //Exibe informação de onde foi salvo
                    JOptionPane.showMessageDialog(null, "Arquivo salvo na sua área de trabalho!", "Programa terminado!", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Arquivo de folha de pagamento inválido!", "Arquivo Inválido", JOptionPane.ERROR_MESSAGE);
                }
            }else{
                throw new Error("Erro ao conectar ao banco de dados!");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ocorreu um erro: " + e.getMessage(), "Ocorreu um erro", JOptionPane.ERROR_MESSAGE);
        } catch (Error e) {
            JOptionPane.showMessageDialog(null, "Ocorreu um erro: " + e.getMessage(), "Ocorreu um erro", JOptionPane.ERROR_MESSAGE);
        }

        System.exit(0);
    }
}
