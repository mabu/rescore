package rescore.models;

import javax.swing.table.AbstractTableModel;
import java.util.List;
import rescore.Yacht;

/** Holds data for the Swing JTable of yachts */

public class YachtTableModel extends AbstractTableModel {

    private String[] columnNames = { "Burės nr.", "Metai", "Kapitonas", "Savininkas" };
    private Object[][] data = getTableData();

   /** 
    * Utility method
    * converts a list of yachts to an array of arrays
    */
    private static Object[][] getTableData(){
    	List<Yacht> yachts = Yacht.getAll();
        Object[][] data = new Object[yachts.size()][];
        int i = 0;
        for(Yacht yacht : yachts){
            data[i] = new Object[] { yacht.getSailNumber(), yacht.getYear(), (yacht.getCaptain() == null ? null : yacht.getCaptain().getName()), (yacht.getOwner() == null ? null : yacht.getOwner().getName()) };
            i++;
        }
        return data;
    }

    public int getColumnCount() {
        return columnNames.length;
    }

    public int getRowCount() {
        return data.length;
    }

    public String getColumnName(int col) {
        return columnNames[col];
    }

    public Object getValueAt(int row, int col) {
        return data[row][col];
    }

    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }

}
