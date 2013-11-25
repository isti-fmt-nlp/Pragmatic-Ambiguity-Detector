/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guiListener;

import controllers.ControlloreProgetto;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Observable;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author Peppe
 */
public class AggiungiDominio extends Observable implements ActionListener{
    private ControlloreProgetto cp=ControlloreProgetto.getInstance();
    private String dom;
    public AggiungiDominio(String dom){
    this.dom=dom;
    }
    @Override
    public void actionPerformed(ActionEvent ae) {
         FileNameExtensionFilter filter = new FileNameExtensionFilter("Only Txt e Pdf", "txt", "pdf");
           if(cp.analysisCompleted()){
             int dialogButton = JOptionPane.YES_NO_OPTION;
             int dialogResult = JOptionPane.showConfirmDialog (null, "Do you want to save the analysis results before adding?","Warning",dialogButton);
             if(dialogResult == JOptionPane.YES_OPTION){
                 Calendar c=Calendar.getInstance();
                 String nameDir = JOptionPane.showInputDialog(null, "Insert save dir name","Result-"+c.get(Calendar.DATE)+"-"+(c.get(Calendar.MONTH)+1)+"-"+c.get(Calendar.YEAR));
                 if(nameDir==null || nameDir.isEmpty())
                 return;
                 cp.salvaCancRisultati(nameDir);
                 cp.setAnalysis(false);
             }
             else{
                 cp.eliminaRisultati();
                 cp.setAnalysis(false);
             }
         }
         JFileChooser fileChooser = new JFileChooser();
         fileChooser.setAcceptAllFileFilterUsed(false);
         fileChooser.setFileFilter(filter);
         fileChooser.setDialogTitle("Selezionat File");
         fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
         if(fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){
                      cp.aggiungiDocumento(dom,fileChooser.getSelectedFile().getAbsolutePath());
                      this.setChanged();
                      this.notifyObservers();
                   }else{
                   System.out.println("Operation Aborted");
               }
    }
    
}
