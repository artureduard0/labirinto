
public class Point {
	private int row;
	private int col;
	private char symbol;
	private String status;
	private boolean empilhado;
	
	public Point(int row, int col, char sym) {
		this.row=row;
		this.col=col;
		this.symbol=sym;
		this.empilhado = false;
		this.status = null;
	}
	
	public void setStatus(String s) {
		this.status = s;
	}
	
	public void setSimbolo(char sym) {
		this.symbol=sym;
	}
	
	public void setEmpilhado(boolean e) {
		empilhado = e;
	}
	
	public char getSimbolo() {
		return symbol;
	}
	
	public String getStatus() {
		return status;
	}
	
	public int getColuna() {
		return col;
	}
	
	public int getLinha() {
		return row;
	}
	
	public boolean isEmpilhado() {
		return this.empilhado;
	}
	
	@Override
	public String toString() {
		return "("+row+","+col+")";
	}	
}
