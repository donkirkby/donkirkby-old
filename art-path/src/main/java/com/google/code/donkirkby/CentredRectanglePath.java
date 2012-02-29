package com.google.code.donkirkby;

import java.util.Random;

public class CentredRectanglePath extends Path {
	public static final int East = 0;
	public static final int NorthEast = 1;
	public static final int North = 2;
	public static final int NorthWest = 3;
	public static final int West = 4;
	public static final int SouthWest = 5;
	public static final int South = 6;
	public static final int SouthEast = 7;

	private Cell cell;
	private int inDirection, outDirection;
	private Point entry, exit, midPoint;
	private Random random;

	public CentredRectanglePath(
			Cell cell, 
			Point entry, 
			Point exit, 
			Random random) 
	{
		this.cell = cell;
		this.entry = entry;
		this.exit = exit;
		this.random = random;
		this.inDirection = calculateInDirection(entry);
		this.outDirection = (calculateInDirection(exit) + 4) % 8;
		midPoint = calculateMidPoint();
	}

	private int calculateInDirection(Point entry) {
		double entryX = entry.getX();
		double entryY = entry.getY();
		int inDirection;
		if (entryX == this.cell.getLeft())
		{
			if (entryY == this.cell.getTop())
			{
				inDirection = SouthEast;
			}
			else if (entryY == this.cell.getBottom())
			{
				inDirection = NorthEast;
			}
			else
			{
				inDirection = East;
			}
		}
		else if (entryX == this.cell.getRight())
		{
			if (entryY == this.cell.getTop())
			{
				inDirection = SouthWest;
			}
			else if (entryY == this.cell.getBottom())
			{
				inDirection = NorthWest;
			}
			else
			{
				inDirection = West;
			}
		}
		else
		{
			if (entryY == this.cell.getTop())
			{
				inDirection = South;
			}
			else
			{
				inDirection = North;
			}
		}
		return inDirection;
	}
	
	public Cell getCell()
	{
		return cell;
	}

	@Override
	public double calculateOptimalWidth(Image image) {
		double cellIntensity = image.getSquareIntensity(
				(int)cell.getLeft(),
				(int)cell.getTop(),
				(int)cell.getRight(),
				(int)cell.getBottom());
		double cellArea = 
				(cell.getRight() - cell.getLeft()) *
				(cell.getBottom() - cell.getTop());
		return calculateWidth(cellIntensity, cellArea, getLength());
	}

	@Override
	public double getLength() {
		return entry.distanceTo(midPoint) + midPoint.distanceTo(exit);
	}

	@Override
	public double[] getCoordinates() {
		return new double[] {
				entry.getX(), 
				entry.getY(), 
				midPoint.getX(), 
				midPoint.getY(), 
				exit.getX(), 
				exit.getY()
		};
	}

	@Override
	public Path[] split() {
		Cell[] cells;
		boolean isXSplit;
		if (entry.getX() == exit.getX() && 
				(entry.getX() == cell.getLeft() || 
				entry.getX() == cell.getRight()))
		{
			isXSplit = false;
		}
		else if (entry.getY() == exit.getY() && 
				(entry.getY() == cell.getTop() || 
				entry.getY() == cell.getBottom()))
		{
			isXSplit = true;
		}
		else
		{
			double width = cell.getRight() - cell.getLeft();
			double height = cell.getBottom() - cell.getTop();
			isXSplit = width > height;
		}
		
		boolean shouldSwap;
		if (isXSplit)
		{
			cells = cell.splitX(midPoint.getX());
			shouldSwap = entry.getX() > exit.getX();
		}
		else
		{
			cells = cell.splitY(midPoint.getY());
			shouldSwap = entry.getY() > exit.getY();
		}
		if (shouldSwap)
		{
			Cell temp = cells[0];
			cells[0] = cells[1];
			cells[1] = temp;
		}
		return createChildren(midPoint, cells);
	}

	private Point calculateMidPoint() {
		double midX, midY;
		if (inDirection == East || inDirection == West)
		{
			midY = entry.getY();
		}
		else if (outDirection == East || outDirection == West)
		{
			midY = exit.getY();
		}
		else
		{
			midY = cell.getTop() + (cell.getBottom() - cell.getTop()) * 
					(0.25 + 0.5*random.nextDouble());
		}
		if (inDirection == North || inDirection == South)
		{
			midX = entry.getX();
		}
		else if (outDirection == North || outDirection == South)
		{
			midX = exit.getX();
		}
		else
		{
			midX = cell.getLeft() + (cell.getRight() - cell.getLeft()) * 
					(0.25 + 0.5*random.nextDouble());
		}
		Point midPoint = new Point(midX, midY);
		return midPoint;
	}

	private Path[] createChildren(Point midPoint, Cell[] cells) {
		CentredRectanglePath child1 = new CentredRectanglePath(
				cells[0], 
				entry, 
				midPoint, 
				random);
		CentredRectanglePath child2 = new CentredRectanglePath(
				cells[1], 
				midPoint, 
				exit, 
				random);
		append(child2);
		append(child1);
		remove();
		return new Path[] {
				child1,
				child2,
		};
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
		    return true;
		}
		if (getClass() != obj.getClass()) 
		{
			return false;
		}
		CentredRectanglePath other = (CentredRectanglePath)obj;
		return 
			this.cell.equals(other.cell) &&
			this.entry.equals(other.entry) &&
			this.exit.equals(other.exit);
	}
	
	@Override
	public String toString()
	{
		return String.format(
				"CentredRectanglePath(%1$s, %2$s->%3$s)", 
				cell, 
				entry, 
				exit);
	}

	public static Path createStartPath(double width, double height) {
		Random random = new Random(0);
		Path path1 = new CentredRectanglePath(
				new Cell(0, 0, width*.5, height*.5), 
				new Point(width*.25, height*.5), 
				new Point(width*.5, height*.25), 
				random);
		Path path2 = new CentredRectanglePath(
				new Cell(width*.5, 0, width, height*.5),
				new Point(width*.5, height*.25), 
				new Point(width*.75, height*.5), 
				random);
		Path path3 = new CentredRectanglePath(
				new Cell(width*.5, height*.5, width, height),
				new Point(width*.75, height*.5), 
				new Point(width*.5, height*.75), 
				random);
		Path path4 = new CentredRectanglePath(
				new Cell(0, height*.5, width*.5, height),
				new Point(width*.5, height*.75), 
				new Point(width*.25, height*.5), 
				random);
		path1.append(path2);
		path2.append(path3);
		path3.append(path4);
		return path1;
	}
}
