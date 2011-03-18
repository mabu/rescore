package rescore.views;

import javax.swing.*;
import java.awt.*;
import rescore.models.YachtTableModel;

public class YachtListView extends JPanel {

	public YachtListView(){

        JTable yachtTable = new JTable(new YachtTableModel());
        JScrollPane scrollPane = new JScrollPane(yachtTable);
        yachtTable.setFillsViewportHeight(true);
        
        this.add(BorderLayout.CENTER, scrollPane);

    }

}
