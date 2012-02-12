package com.google.code.donkirkby;

public class Point {
	private double x;
	private double y;
	
	public Point(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
		    return true;
		}
		if (!(obj instanceof Point)) 
		{
			return false;
		}
		Point other = (Point)obj;
		return 
			Math.abs(this.x - other.x) < 0.001 &&
			Math.abs(this.y - other.y) < 0.001;
	}
	
	@Override
	public String toString() {
		return String.format("(%.0f,%.0f)", x, y);
	}

	public Point moveToward(Point end, double fraction) {
		return new Point(x + (end.x - x)*fraction, y + (end.y - y)*fraction);
	}
	
	public double distanceTo(Point end) {
		return Math.sqrt(distanceSquaredTo(end));
	}

	public double distanceSquaredTo(Point end) {
		return (end.x - x)*(end.x - x) + (end.y - y)*(end.y - y);
	}
}
