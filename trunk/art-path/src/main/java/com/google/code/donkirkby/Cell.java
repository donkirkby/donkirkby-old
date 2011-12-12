package com.google.code.donkirkby;


public class Cell {
	private double x1, y1, x2, y2;
	
	public Cell(double x1, double y1, double x2, double y2) 
	{
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
		    return true;
		}
		if (!(obj instanceof Cell)) 
		{
			return false;
		}
		Cell other = (Cell)obj;
		return 
			this.x1 == other.x1 &&
			this.y1 == other.y1 &&
			this.x2 == other.x2 &&
			this.y2 == other.y2;
	}
}
