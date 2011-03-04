package rescore.views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import rescore.models.YachtTableModel;

public class YachtManagerView extends JFrame {

	public YachtManagerView(){

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(500, 500);

        JTable yachtTable = new JTable(new YachtTableModel());
        JScrollPane scrollPane = new JScrollPane(yachtTable);
        yachtTable.setFillsViewportHeight(true);
        
        this.getContentPane().add(BorderLayout.CENTER, scrollPane);

        JButton addYachtButton = new JButton("Add New Yacht");
        addYachtButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
            	addNewYachtActionPerformed(ae);
            }
        });

		JMenuBar myMenu = MenuBar.prepareMenuBar();
        this.setJMenuBar(myMenu);
        this.getContentPane().add(BorderLayout.SOUTH, addYachtButton);

        

    }
    
    private void addNewYachtActionPerformed(ActionEvent ae){
    	AddYachtDialog ayDialog = new AddYachtDialog();
    	ayDialog.setVisible(true);
    }

}
