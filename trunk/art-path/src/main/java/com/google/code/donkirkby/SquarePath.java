package com.google.code.donkirkby;


public class SquarePath extends Path {
	
	public static final int East = 0;
	public static final int NorthEast = 1;
	public static final int North = 2;
	public static final int NorthWest = 3;
	public static final int West = 4;
	public static final int SouthWest = 5;
	public static final int South = 6;
	public static final int SouthEast = 7;
	public static final String[] DirectionNames = {
		"E",
		"NE",
		"N",
		"NW",
		"W",
		"SW",
		"S",
		"SE"
	};
	private Cell cell;
	private int inDirection, outDirection;
	/**
	 * Initialize a new object.
	 * @param cell The area that this section of the path passes through
	 * @param inDirection The direction of travel while entering this section.
	 * @param outDirection The direction of travel while exiting this section.
	 */
	public SquarePath(Cell cell, int inDirection, int outDirection) 
	{
		this.cell = cell;
		this.inDirection = inDirection;
		this.outDirection = outDirection;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
		    return true;
		}
		if (!(obj instanceof SquarePath)) 
		{
			return false;
		}
		SquarePath other = (SquarePath)obj;
		return 
			this.cell.equals(other.cell) &&
			this.inDirection == other.inDirection &&
			this.outDirection == other.outDirection;
	}
	
	@Override
	public String toString()
	{
		return String.format(
				"SquarePath(%1$s, %2$s, %3$s)", 
				cell, 
				DirectionNames[inDirection], 
				DirectionNames[outDirection]);
		
	}

	@Override
	public Path[] split() {
		Cell [] cells = this.cell.split();
		int [] directions;
		int [] cellIndexes;
		int adjustedIn, adjustedOut, offset1, sign, offset2;//offset moves inDirection to 0 or 1
		switch (inDirection)
		{
		case East:
		case NorthEast:
			offset1 = 0;
			break;
		case South:
		case SouthEast:
			offset1 = 2;
			break;
		case West:
		case SouthWest:
			offset1 = 4;
			break;
		case North:
		case NorthWest:
			offset1 = 6;
			break;
		default:
			throw createUnexpectedDirectionsException();
		}
		if ((outDirection + offset1) % 8 <= 4)
		{
			sign = 1;
			offset2 = 0;
		}
		else
		{// flip sign and adjust offset so that inDirection goes to 0 or 1 and outDirection goes to 0 to 4
			sign = -1;
			offset2 = (2 * (inDirection + offset1)) % 8;
		}
		adjustedIn = (sign*(inDirection + offset1) + offset2 + 16) % 8;
		adjustedOut = (sign*(outDirection + offset1) + offset2 + 16) % 8;
		switch (adjustedIn * 8 + adjustedOut)
		{
		case 0:
			directions = new int[] {NorthEast, East, SouthWest, East, NorthEast};
			cellIndexes = new int[] {1, 0, 2, 3};
			break;
		case 1:
			directions = new int[] {SouthEast, East, NorthWest, East, NorthEast};
			cellIndexes = new int[] {2, 3, 1, 0};
			break;
		case 2:
			directions = new int[] {SouthEast, East, North, West, NorthEast};
			cellIndexes = new int[] {2, 3, 0, 1};
			break;
		case 3:
			directions = new int[] {SouthEast, East, North, West, NorthWest};
			cellIndexes = new int[] {2, 3, 0, 1};
			break;
		case 8:
			directions = new int[] {NorthEast, North, East, South, NorthEast};
			cellIndexes = new int[] {2, 1, 0, 3};
			break;
		case 9:
			directions = new int[] {NorthEast, North, SouthEast, North, NorthEast};
			cellIndexes = new int[] {2, 1, 3, 0};
			break;
		case 10:
			directions = new int[] {NorthEast, East, North, West, NorthEast};
			cellIndexes = new int[] {2, 3, 0, 1};
			break;
		case 11:
			directions = new int[] {NorthEast, East, North, West, NorthWest};
			cellIndexes = new int[] {2, 3, 0, 1};
			break;
		case 12:
			directions = new int[] {NorthEast, East, North, West, SouthWest};
			cellIndexes = new int[] {2, 3, 0, 1};
			break;
			
		default:
			throw createUnexpectedDirectionsException();
		}
		
		//(sign*inDir + offset1) + offset2 = plannedInDir
		//inDir = (plannedInDir - offset2)*sign - offset1
		Path [] paths = new Path[4];
		for (int i = 0; i < paths.length; i++) {
			int index = paths.length - 1 - i;
			int cellIndex = 
					(((cellIndexes[index]*2+1-offset2)*sign - 
							offset1 + 15) % 8) / 2;
			Cell childCell = cells[cellIndex];
			paths[index] = new SquarePath(
					childCell, 
					((directions[index] - offset2)*sign - offset1 + 16) % 8, 
					((directions[index+1] - offset2)*sign - offset1 + 16) % 8);
			append(paths[index]);
		}
		remove();
		return paths;
	}

	private IllegalStateException createUnexpectedDirectionsException() {
		return new IllegalStateException(
				"Unexpected in/out directions: " + 
				DirectionNames[inDirection] + ", " +
				DirectionNames[outDirection] + ".");
	}

	@Override
	public double[] getCoordinates() {
		double x1, y1, x2, y2, x3, y3;
		x2 = (cell.getLeft() + cell.getRight()) / 2;
		y2 = (cell.getTop() + cell.getBottom()) / 2;
		switch (inDirection)
		{
		case East:
		case SouthEast:
		case NorthEast:
			x1 = cell.getLeft();
			break;
		case South:
		case North:
			x1 = x2;
			break;
		case West:
		case SouthWest:
		case NorthWest:
			x1 = cell.getRight();
			break;
		default:
			throw createUnexpectedDirectionsException();
		}
		switch (inDirection)
		{
		case South:
		case SouthEast:
		case SouthWest:
			y1 = cell.getTop();
			break;
		case East:
		case West:
			y1 = y2;
			break;
		case North:
		case NorthEast:
		case NorthWest:
			y1 = cell.getBottom();
			break;
		default:
			throw createUnexpectedDirectionsException();
		}
		switch (outDirection)
		{
		case East:
		case SouthEast:
		case NorthEast:
			x3 = cell.getRight();
			break;
		case South:
		case North:
			x3 = x2;
			break;
		case West:
		case SouthWest:
		case NorthWest:
			x3 = cell.getLeft();
			break;
		default:
			throw createUnexpectedDirectionsException();
		}
		switch (outDirection)
		{
		case South:
		case SouthEast:
		case SouthWest:
			y3 = cell.getBottom();
			break;
		case East:
		case West:
			y3 = y2;
			break;
		case North:
		case NorthEast:
		case NorthWest:
			y3 = cell.getTop();
			break;
		default:
			throw createUnexpectedDirectionsException();
		}
		return new double[] {x1, y1, x2, y2, x3, y3};
	}

	@Override
	public double getLength() {
		double edgeLength = cell.getRight() - cell.getLeft();
		double length =
				inDirection % 2 == 0
				? edgeLength / 2
				: edgeLength / 2 * Math.sqrt(2);
		length +=
				outDirection % 2 == 0
				? edgeLength / 2
				: edgeLength / 2 * Math.sqrt(2);
		return length;
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
		Path path1 = new SquarePath(new Cell(0, 0, width/2, height/2), SquarePath.North, SquarePath.East);
		Path path2 = new SquarePath(new Cell(width/2, 0, width, height/2), SquarePath.East, SquarePath.South);
		Path path3 = new SquarePath(new Cell(width/2, height/2, width, height), SquarePath.South, SquarePath.West);
		Path path4 = new SquarePath(new Cell(0, width/2, width/2, height), SquarePath.West, SquarePath.North);
		path1.append(path2);
		path2.append(path3);
		path3.append(path4);
		return path1;
	}
}
