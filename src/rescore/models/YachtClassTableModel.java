package rescore.models;

import java.util.List;
import javax.swing.table.AbstractTableModel;
import rescore.YachtClass;

public class YachtClassTableModel extends AbstractTableModel {

    private String[] columnNames = { "Modelis", "Koeficientas", "Proj. metai", "Ilgis", "Plotis" };
    private Object[][] data = getTableData();
	

    private static Object[][] getTableData(){
    	List<YachtClass> yachtClasses = YachtClass.getAll();
        Object[][] data = new Object[yachtClasses.size()][];
        int i = 0;
        for(YachtClass yachtClass : yachtClasses){
            data[i] = new Object[] { yachtClass.getName(), yachtClass.getCoefficient(), yachtClass.getProjectYear(), yachtClass.getLength(), yachtClass.getWidth() } ;
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
