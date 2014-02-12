package com.scm.util;

import org.apache.poi.ss.usermodel.Cell;

public class CellStyle {
	private String name;
	private double height;
	private double width;
	private int celltype;
	
	public CellStyle(){
		this.name = "";
		this.height = -1;
		this.width = -1;
		this.celltype = Cell.CELL_TYPE_STRING;
	}
	
	public CellStyle(String _n, double _w, double _h, int _celltype){
		this.name = _n;
		this.width = _w * 286;
		this.height = _h;
		this.celltype = _celltype;
	}
	
	public void setName(String _name){
		this.name = _name;
	}
	
	public void setHeight(double _h){
		this.height = _h;
	}
	
	public void setWidth(double _w){
		this.width = _w;
	}
	
	public String getName(){
		return this.name;
	}
	
	public double getHeight(){
		return this.height;
	}
	
	public double getWidth(){
		return this.width;
	}
	
	public int getCellType(){
		return this.celltype;
	}
}