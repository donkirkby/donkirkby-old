package com.google.code.donkirkby;

import java.util.Random;


public class RectanglePath extends Path {
	
	private Cell cell;
	private Point entry, exit;
	private Random random;
	/**
	 * Initialize a new object.
	 * @param cell The area that this section of the path passes through
	 * @param inDirection The direction of travel while entering this section.
	 * @param outDirection The direction of travel while exiting this section.
	 */
	public RectanglePath(
			Cell cell, Point entry, Point exit, Random random) 
	{
		this.cell = cell;
		this.entry = entry;
		this.exit = exit;
		this.random = random;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
		    return true;
		}
		if (!(obj instanceof RectanglePath)) 
		{
			return false;
		}
		RectanglePath other = (RectanglePath)obj;
		return 
			this.cell.equals(other.cell) &&
			this.entry.equals(other.entry) &&
			this.exit.equals(other.exit);
	}
	
	@Override
	public String toString()
	{
		return String.format(
				"RectanglePath(%1$s, %2$s->%3$s)", 
				cell, 
				entry, 
				exit);
		
	}

	@Override
	public Path[] split() {
		Point pathBreak = entry.moveToward(exit, getRandomSplit());
		Point crossing = new Point(
				pathBreak.getX(),
				cell.getTop() + 
				(cell.getBottom() - cell.getTop()) * getRandomSplit());
		Cell[] cells = cell.splitX(pathBreak.getX());
		Cell entryCell, exitCell;
		if (entry.getX() == cell.getLeft() || exit.getX() == cell.getRight())
		{
			entryCell = cells[0];
			exitCell = cells[1];
		}
		else
		{
			entryCell = cells[1];
			exitCell = cells[0];
		}
		return new Path[] {
				new RectanglePath(
						entryCell,
						entry, 
						crossing, 
						random),
				new RectanglePath(
						exitCell,
						crossing,
						exit, 
						random),
		};
	}

	private double getRandomSplit() {
		return 0.25 + 0.5 * random.nextDouble();
	}

	@Override
	public double[] getCoordinates() {
		// TODO: implement
		return null;
	}

	@Override
	public double getLength() {
		// TODO: implement
		return 0;
	}

	public Cell getCell() {
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

	public static Path createStartPath(double width, double height) {
		// TODO: implement
		return null;
	}
}
