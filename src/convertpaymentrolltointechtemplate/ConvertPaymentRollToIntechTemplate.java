package convertpaymentrolltointechtemplate;

import convertpaymentrolltointechtemplate.Model.Entry_Model;
import convertpaymentrolltointechtemplate.Model.Payroll_Model;
import fileManager.Args;
import fileManager.Selector;
import java.io.File;
import javax.swing.JOptionPane;
import org.ini4j.Ini;

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
        
        //Pega folha de pagamento
        File payrollFile = Selector.selectFile("", "CSV(Texto separado por virgulas)", ".csv");
        //Se o arquivo for valido
        if(Selector.verifyFile(payrollFile.getPath(),".csv")){
            
            //Instancia o modelo folha
            Payroll_Model payrollModel = new Payroll_Model(payrollFile);
            //Extrai os dados do arquivo
            payrollModel.extractDataOfFile();
            
            //Instancia modelo lançamentos
            Entry_Model entryModel = new Entry_Model(payrollModel.getValues(), payrollModel.getSalaries());
            
            
        }else{
            JOptionPane.showMessageDialog(null, "Arquivo de folha de pagamento inválido!","Arquivo Inválido",JOptionPane.ERROR_MESSAGE);
        }
        
        System.exit(0);
    }
}
