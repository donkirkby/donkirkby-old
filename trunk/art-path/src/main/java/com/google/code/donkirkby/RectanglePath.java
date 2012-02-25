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
	
	public Cell getCell() {
		return cell;
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
		switch(entrySide + exitSide)
		{
		case RIGHT + TOP:
		case RIGHT + BOTTOM:
		case LEFT + TOP:
		case LEFT + BOTTOM:
			return splitCorner(entrySide, exitSide);
		case LEFT + RIGHT:
		case TOP + BOTTOM:
			return splitCrossing(entrySide, exitSide);
		default:
			throw new IllegalStateException("Unexpected entry/exit combination: " + entrySide + exitSide);
		}
	}

	private Path[] splitCrossing(int entrySide, int exitSide) {
		boolean isHorizontalSplit = entrySide == LEFT || entrySide == RIGHT;
		double gap =
				isHorizontalSplit
				? Math.abs(entry.getY() - exit.getY())
				: Math.abs(entry.getX() - exit.getX());
		double otherDimension =
				isHorizontalSplit
				? (cell.getRight() - cell.getLeft())
				: (cell.getBottom() - cell.getTop());
		boolean isReversed;
		if (gap < otherDimension)
		{
			isHorizontalSplit = ! isHorizontalSplit;
			isReversed = entrySide == BOTTOM || entrySide == RIGHT;
		}
		else
		{
			isReversed = 
					isHorizontalSplit
					? entry.getY() > exit.getY()
					: entry.getX() > exit.getX();
		}
		return createChildren(isHorizontalSplit, isReversed);
	}

	private Path[] splitCorner(int entrySide, int exitSide) {
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
			corner = new Point(cell.getRight(), cell.getTop());
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
		return createChildren(isHorizontalSplit, isReversed);
	}

	private Path[] createChildren(boolean isHorizontalSplit, boolean isReversed) {
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
		RectanglePath child1 = new RectanglePath(
				entryCell,
				entry, 
				crossing, 
				random);
		RectanglePath child2 = new RectanglePath(
				exitCell,
				crossing,
				exit, 
				random);
		getPrevious().append(child1);
		child1.append(child2);
		this.remove();
		return new Path[] { child1, child2 };
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
		return new double[] 
				{ entry.getX(), entry.getY(), exit.getX(), exit.getY() };
	}

	@Override
	public double getLength() {
		return entry.distanceTo(exit);
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
		Random random = new Random(0);
		Path path1 = new RectanglePath(
				new Cell(0, 0, width*.5, height*.5), 
				new Point(width*.25, height*.5), 
				new Point(width*.5, height*.25), 
				random);
		Path path2 = new RectanglePath(
				new Cell(width*.5, 0, width, height*.5),
				new Point(width*.5, height*.25), 
				new Point(width*.75, height*.5), 
				random);
		Path path3 = new RectanglePath(
				new Cell(width*.5, height*.5, width, height),
				new Point(width*.75, height*.5), 
				new Point(width*.5, height*.75), 
				random);
		Path path4 = new RectanglePath(
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
