package com.google.code.donkirkby;


public class Cell {
	private double left, top, right, bottom;
	
	public Cell(double left, double top, double right, double bottom) 
	{
		this.left = left;
		this.top = top;
		this.right = right;
		this.bottom = bottom;
	}
	
	public double getLeft()
	{
		return left;
	}
	
	public double getTop()
	{
		return top;
	}
	
	public double getRight()
	{
		return right;
	}
	
	public double getBottom()
	{
		return bottom;
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
			this.left == other.left &&
			this.top == other.top &&
			this.right == other.right &&
			this.bottom == other.bottom;
	}
	
	@Override
	public String toString()
	{
		return String.format(
				"Cell(%1$.0f, %2$.0f, %3$.0f, %4$.0f)", 
				left, 
				top, 
				right, 
				bottom);
		
	}

	public Cell[] split() {
		return new Cell[] 
		{
				new Cell((left + right)/2, top, right, (top + bottom)/2),
				new Cell(left, top, (left + right)/2, (top + bottom)/2),
				new Cell(left, (top + bottom)/2, (left + right)/2, bottom),
				new Cell((left + right)/2, (top + bottom)/2, right, bottom)
		};
	}

	public Cell[] splitX(double x) {
		return new Cell[] 
		{
				new Cell(left, top, x, bottom),
				new Cell(x, top, right, bottom)
		};
	}
}
