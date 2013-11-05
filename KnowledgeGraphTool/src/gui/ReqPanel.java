package gui;

import Controllori.ControlloreProgetto;
import guiComponents.ModelloTabella;
import guiComponents.RendererCelleTabella;
import javax.swing.JPanel;
import data.Requirements;
import guiComponents.VisualizzaRequisito;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import java.awt.Dimension;
import java.util.Observable;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class ReqPanel extends JPanel{
	private Requirements reqs;
	private ModelloTabella tm=new ModelloTabella();
	private JTable table;
	private RendererCelleTabella rc=new RendererCelleTabella();
	private VisualizzaRequisito vr;
        
	public ReqPanel() {
		setPreferredSize(new Dimension(300, 400));
		setMinimumSize(new Dimension(300, 400));
		reqs=new Requirements();
		
		JScrollPane scrollPane = new JScrollPane();
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addComponent(scrollPane, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addComponent(scrollPane, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
		);
		table = new JTable();
		table.setFillsViewportHeight(true);
		table.setColumnSelectionAllowed(false);
		table.setRowSelectionAllowed(false);
                table.setModel(tm);
		tm.addColumn("Requirments");
		tm.addColumn("Jaccards");
		table.getColumnModel().getColumn(0).setCellRenderer(rc);
		table.getColumnModel().getColumn(1).setCellRenderer(rc);
		scrollPane.setViewportView(table);
                vr=new VisualizzaRequisito(reqs);
                table.getSelectionModel().addListSelectionListener(vr);
		setLayout(groupLayout);
	}
        public void clearRows(){
            int n=tm.getRowCount();
         for(int i=0;i<n;i++){
             tm.removeRow(0);
         }
        }
	public void viewReqs(String path){
                clearRows();
                ControlloreProgetto.getIstance().setNReqs(reqs.loadReqs(path));
                if(ControlloreProgetto.getIstance().AnalysisCompleted())
                    reqs.loadAnalysis(path);
                String txt;
		float jac;
                for(int i=0;i<reqs.getSize();i++){
			txt=reqs.getReq(i).getReq();
                        jac=reqs.getReq(i).getVal();
                        if(jac>=0)
                            tm.addRow(new Object[]{"R"+(i+1)+"-"+txt ,jac});
                        else
                            tm.addRow(new Object[]{"R"+(i+1)+"-"+txt});
		}
	}
	public Requirements getRequirements(){
		return reqs;
	}
	public JTable getTable(){
		return table;
        }
        public Observable getObservable(){
            return vr;
        }
}
