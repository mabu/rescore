package rescore.views;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import rescore.models.YachtClassTableModel;

public class YachtClassListView extends JPanel {

	private JTable yachtClassTable;
	public YachtClassListView(){

		yachtClassTable = new JTable(new YachtClassTableModel());
		JScrollPane scrollPane = new JScrollPane(yachtClassTable);
		yachtClassTable.setFillsViewportHeight(true);
		
        this.add(BorderLayout.CENTER, scrollPane);
    }
	
}
