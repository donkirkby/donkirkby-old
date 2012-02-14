package com.google.code.donkirkby;

import java.util.Random;


public class RectanglePath extends Path {
	
	private final int RIGHT = 0, TOP = 1, LEFT = 2, BOTTOM = 4;
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
		int entrySide = getSide(entry);
		int exitSide = getSide(exit);
		boolean isReversed; // bottom before top or right before left
		boolean isHorizontalSplit; // split boundary is horizontal
		boolean isHorizontalReversed; // isReversed when we split horizontally
		boolean isVerticalReversed; // isReversed when we split vertically
		// We assume that we will split the entry side, then compare distance
		// to corner and reverse if needed.
		isHorizontalSplit = entrySide == LEFT || entrySide == RIGHT;
		isHorizontalReversed = entrySide == BOTTOM || exitSide == TOP;
		isVerticalReversed = entrySide == RIGHT || exitSide == LEFT;
		Point corner;
		switch (entrySide + exitSide)
		{
		case RIGHT + TOP:
			corner = new Point(cell.getRight(), cell.getBottom());
			break;
		case RIGHT + BOTTOM:
			corner = new Point(cell.getRight(), cell.getBottom());
			break;
		case LEFT + BOTTOM:
			corner = new Point(cell.getLeft(), cell.getBottom());
			break;
		case LEFT + TOP:
			corner = new Point(cell.getLeft(), cell.getTop());
			break;
		default:
			throw new IllegalStateException("Unexpected entry/exit combination: " + entrySide + exitSide);
		}
		double entryDistance = entry.distanceSquaredTo(corner);
		double exitDistance = exit.distanceSquaredTo(corner);
		if (entryDistance < exitDistance)
		{
			isHorizontalSplit = ! isHorizontalSplit;
		}
		isReversed = 
				isHorizontalSplit ? isHorizontalReversed : isVerticalReversed;
		Point pathBreak = entry.moveToward(exit, getRandomSplit());
		Point crossing;
		if (isHorizontalSplit)
		{
			crossing = new Point(
					cell.getLeft() + 
					(cell.getRight() - cell.getLeft()) * getRandomSplit(),
					pathBreak.getY());
		}
		else
		{
			crossing = new Point(
					pathBreak.getX(),
					cell.getTop() + 
					(cell.getBottom() - cell.getTop()) * getRandomSplit());
		}
		Cell[] cells = 
				isHorizontalSplit
				? cell.splitY(pathBreak.getY())
				: cell.splitX(pathBreak.getX());
		Cell entryCell = isReversed ? cells[1] : cells[0];
		Cell exitCell = isReversed ? cells[0] : cells[1];
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
	
	private int getSide(Point point) {
		return
				point.getX() == cell.getRight()
				? RIGHT
				: point.getY() == cell.getTop()
				? TOP
				: point.getX() == cell.getLeft()
				? LEFT
				: BOTTOM;
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
