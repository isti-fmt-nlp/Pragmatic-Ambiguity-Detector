/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import gui.MenuBar;
import gui.ProjectTree;
import gui.ReqBox;
import gui.ReqPanel;
import guiListener.AggiungiDominio;
import guiListener.AggiungiRequisiti;
import guiListener.ApriProgetto;
import guiListener.AvviaAnalisi;
import guiListener.ChiudiProgetto;
import supportGui.FileSelectorModel;
import guiListener.NuovoProgetto;
import guiListener.RemoveDominio;
import guiListener.RemoveRequirements;
import supportGui.RendererCelleTabella;
import guiListener.ThresholdChange;
import guiListener.VisualizzaRequisito;
import java.awt.Cursor;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JFrame;
import javax.swing.JTable;

/**
 *
 * @author Lipari
 */
public class GestoreObserver implements Observer{
    private JFrame main=null;
    private MenuBar menuBar;
    private ProjectTree projectTree;
    private ReqBox reqBox;
    private ReqPanel reqPanel;
    
    public GestoreObserver(JFrame main,MenuBar menuBar,ProjectTree projectTree,ReqBox reqBox,ReqPanel reqPanel){
        this.main=main;
        this.menuBar=menuBar;
        this.projectTree=projectTree;
        this.reqBox=reqBox;
        this.reqPanel=reqPanel;
    }
    public void addObservables(Observable[] o){
         for(Observable ob:o){
          ob.addObserver(this);
        }
    }
    public void addObservable (Observable o){
         o.addObserver(this);
    }

    @Override
    public void update(Observable o, Object o1) {
        ControlloreProgetto cp=ControlloreProgetto.getInstance();
        if(o.getClass().equals(VisualizzaRequisito.class))
            reqBox.getTextBox().setText((String)o1);
        if(o.getClass().equals(ThresholdChange.class)){
            JTable t=reqPanel.getTable();
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
            projectTree.getTree().setModel(fs);
            projectTree.enablePopUp(true);
            projectTree.enableAddRequirements(!cp.Requirements());
            menuBar.setMenuItemsEnable(true);
            menuBar.setMenuAddRequirementsEnable(!cp.Requirements());
            menuBar.enableAnalisi(cp.isReady());
            if(cp.AnalysisCompleted()){
                menuBar.enableThreshold(true);
                menuBar.enableSave(cp.AnalysisCompleted());
            }
            reqPanel.viewReqs(cp.getSource());
            
        }
        if(o.getClass().equals(ChiudiProgetto.class)){
            projectTree.getTree().setModel(null);
            projectTree.enablePopUp(false);
            menuBar.setMenuItemsEnable(false);
            menuBar.enableAnalisi(false);
            menuBar.enableThreshold(false);
            reqPanel.clearRows();
        }
        if(o.getClass().equals(AggiungiDominio.class)||o.getClass().equals(AggiungiRequisiti.class)){
            FileSelectorModel fs=new FileSelectorModel(cp.getSource());
            projectTree.getTree().setModel(fs);
            reqPanel.viewReqs(cp.getSource());
            menuBar.enableAnalisi(cp.isReady());
        }
         if(o.getClass().equals(RemoveDominio.class)||o.getClass().equals(RemoveRequirements.class)){
            FileSelectorModel fs=new FileSelectorModel(cp.getSource());
            projectTree.getTree().setModel(fs);
            reqPanel.viewReqs(cp.getSource());
            menuBar.enableAnalisi(cp.isReady());
        }
        if(o.getClass().equals(AggiungiRequisiti.class)){
             reqPanel.viewReqs(cp.getSource());
             menuBar.enableAnalisi(cp.isReady());
             FileSelectorModel fs=new FileSelectorModel(cp.getSource());
             projectTree.getTree().setModel(fs);
             projectTree.enableAddRequirements(false);
             menuBar.setMenuAddRequirementsEnable(false);
             
        }
        if(o.getClass().equals(RemoveRequirements.class)){
             reqPanel.viewReqs(cp.getSource());
             menuBar.enableAnalisi(cp.isReady());
             FileSelectorModel fs=new FileSelectorModel(cp.getSource());
             projectTree.getTree().setModel(fs);
             projectTree.enableAddRequirements(true);
             menuBar.setMenuAddRequirementsEnable(true);

        }

        if(o.getClass().equals(AvviaAnalisi.class)){
            if(o1==null){
                menuBar.enableSave(cp.AnalysisCompleted());
                reqPanel.viewReqs(cp.getSource());
                main.setEnabled(true);
                menuBar.enableThreshold(cp.AnalysisCompleted());
                FileSelectorModel fs=new FileSelectorModel(cp.getSource());
                projectTree.getTree().setModel(fs);
            }
            else{
                if(o1.equals("fail")){
                    main.setEnabled(true);
                     main.setCursor(null);
                }else{
                    main.setEnabled(false);
                    main.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            
                }
            }
        }
    }
}