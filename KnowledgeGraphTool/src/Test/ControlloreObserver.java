/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Test;
import controllers.ControlloreProgetto;
import guiListener.AggiungiDominio;
import guiListener.AggiungiRequisiti;
import guiListener.ApriProgetto;
import guiListener.AvviaAnalisi;
import guiListener.ChiudiProgetto;
import supportGui.FileSelectorModel;
import supportGui.RendererCelleTabella;
import guiListener.ThresholdChange;
import guiListener.VisualizzaRequisito;
import guiListener.NuovoProgetto;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JTable;
/**
 *
 * @author Lipari
 */
public class ControlloreObserver extends javax.swing.JFrame implements Observer {
    /**
     * Creates new form ProveFrame
     */
    public ControlloreObserver() {
        initComponents();
        /*Initializie Observer*/
        Observable[] obs=menuBar1.getObservable();
        for(Observable ob:obs){
          ob.addObserver(this);
        }
        reqPanel1.getObservable().addObserver(this);
        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        menuBar1 = new gui.MenuBar();
        reqPanel1 = new gui.ReqPanel();
        reqBox1 = new gui.ReqBox();
        projectTree1 = new gui.ProjectTree();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(reqBox1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(projectTree1, javax.swing.GroupLayout.DEFAULT_SIZE, 249, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(reqPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(744, 744, 744))
            .addComponent(menuBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(menuBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(projectTree1, javax.swing.GroupLayout.DEFAULT_SIZE, 363, Short.MAX_VALUE)
                    .addComponent(reqPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(reqBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ControlloreObserver.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ControlloreObserver.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ControlloreObserver.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ControlloreObserver.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ControlloreObserver().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private gui.MenuBar menuBar1;
    private gui.ProjectTree projectTree1;
    private gui.ReqBox reqBox1;
    private gui.ReqPanel reqPanel1;
    // End of variables declaration//GEN-END:variables

    @Override
    public void update(Observable o, Object o1) {
            ControlloreProgetto cp=ControlloreProgetto.getInstance();
            if(o.getClass().equals(VisualizzaRequisito.class))
                reqBox1.getTextBox().setText((String)o1);
            if(o.getClass().equals(ThresholdChange.class)){
			JTable t=reqPanel1.getTable();
			RendererCelleTabella rc=(RendererCelleTabella)t.getCellRenderer(0, 0);
			for(int i=0;i<t.getRowCount();i++){
				if((Float)t.getValueAt(i, 1)<=(Float)o1)
					rc.setAlert(i);
				else
					rc.removeAlert(i);
				t.repaint();
				}
            }
            if(o.getClass().equals(ApriProgetto.class)||o.getClass().equals(NuovoProgetto.class)){
                FileSelectorModel fs=new FileSelectorModel(cp.getSource());
                projectTree1.getTree().setModel(fs);
                menuBar1.setMenuItemsEnable(true);
                menuBar1.enableAnalisi(cp.isReady());
                if(cp.analysisCompleted()){
                    menuBar1.enableThreshold(true);
                    menuBar1.enableSave(cp.analysisCompleted());
                }
                reqPanel1.viewReqs(cp.getSource());
            }
            if(o.getClass().equals(ChiudiProgetto.class)){
                projectTree1.getTree().setModel(null);
                menuBar1.setMenuItemsEnable(false);
                menuBar1.enableAnalisi(false);
                menuBar1.enableThreshold(false);
            }
             if(o.getClass().equals(AggiungiDominio.class)||o.getClass().equals(AggiungiRequisiti.class)){
                FileSelectorModel fs=new FileSelectorModel(cp.getSource());
                projectTree1.getTree().setModel(fs);
                menuBar1.enableAnalisi(cp.isReady());
             }
             if(o.getClass().equals(AvviaAnalisi.class)){
                 if(o1==null){
                 menuBar1.enableSave(cp.analysisCompleted());
                 reqPanel1.viewReqs(cp.getSource());
                 this.setEnabled(true);
                 menuBar1.enableThreshold(cp.analysisCompleted());
                 }
                 else{
                     if(o1.equals("fail"))
                        this.setEnabled(true);
                     else
                         this.setEnabled(false);
                 }
             }
        }
}
