package ie.gmit.sw;

import java.util.Map;

import javax.swing.table.AbstractTableModel;
public class TypeSummaryTableModel extends AbstractTableModel{
	private static final long serialVersionUID = 777L;
	private String[] cols = {"Class", "Stability"};
	private Object[][] data = {};
	
	public TypeSummaryTableModel(String[][] arr) {
		data = arr;
	}

	public int getColumnCount() {
        return cols.length;
    }
	
    public int getRowCount() {
        return data.length;
	}

    public String getColumnName(int col) {
    	return cols[col];
    }

    public Object getValueAt(int row, int col) {
        return data[row][col];
	}
   
    public Class<?> getColumnClass(int c) {
        return getValueAt(0, c).getClass();
	}
}