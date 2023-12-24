package utils;

// My own pair.
public class MapDimensions {
	private int rows;
	private int columns;
	
	public MapDimensions(int rows, int columns) {
		this.rows = rows;
		this.columns = columns;
	}
	
	public MapDimensions() {
		
	}
	
	public void setRows(int rows) {
		this.rows = rows;
	}
	
	public int getRows() {
		return rows;
	}
	
	public void setColumns(int columns) {
		this.columns = columns;
	}
	
	public int getColumns() {
		return columns;
	}

}
