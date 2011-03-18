package rescore.views;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import rescore.models.YachtClassTableModel;

public class ClassListView extends JPanel {

	private YachtClassTableModel yachtClass;
	private JTable yachtClassTable;
	public ClassListView(){

		
		yachtClassTable = new JTable(new YachtClassTableModel());
		JScrollPane scrollPane = new JScrollPane(yachtClassTable);
		yachtClassTable.setFillsViewportHeight(true);
		
		JButton addModelButton = new JButton("Add New Class");
        addModelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
            	addNewModelActionPerformed(ae);
            }
        });
        
        this.add(BorderLayout.CENTER, scrollPane);
        this.add(BorderLayout.SOUTH, addModelButton);
      

    }
	
	private void addNewModelActionPerformed(ActionEvent ae){
    	AddYachtClassDialog aycDialog = new AddYachtClassDialog();
    	//aycDialog.okButton.addActionListener(null);
    	aycDialog.setVisible(true);
    }
	

}
