package com.google.code.donkirkby;


public class Path {
	
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
	private Path next, previous;
	
	/**
	 * Initialize a new object.
	 * @param cell The area that this section of the path passes through
	 * @param inDirection The direction of travel while entering this section.
	 * @param outDirection The direction of travel while exiting this section.
	 */
	public Path(Cell cell, int inDirection, int outDirection) 
	{
		this.cell = cell;
		this.inDirection = inDirection;
		this.outDirection = outDirection;
		this.next = this.previous = this;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
		    return true;
		}
		if (!(obj instanceof Path)) 
		{
			return false;
		}
		Path other = (Path)obj;
		return 
			this.cell.equals(other.cell) &&
			this.inDirection == other.inDirection &&
			this.outDirection == other.outDirection;
	}
	
	@Override
	public String toString()
	{
		return String.format(
				"Path(%1$s, %2$s, %3$s)", 
				cell, 
				DirectionNames[inDirection], 
				DirectionNames[outDirection]);
		
	}

	public Path[] split() {
		Cell [] cells = this.cell.split();
		int [] directions;
		int [] cellIndexes;
		int adjustedIn, adjustedOut, offset, sign;//offset moves inDirection to 0 or 1
		switch (inDirection)
		{
		case East:
		case NorthEast:
			offset = 0;
			break;
		case South:
		case SouthEast:
			offset = 2;
			break;
		case West:
		case SouthWest:
			offset = 4;
			break;
		case North:
		case NorthWest:
			offset = 6;
			break;
		default:
			throw createUnexpectedDirectionsException();
		}
		if ((outDirection + offset) % 8 <= 4)
		{
			sign = 1;
		}
		else
		{// flip sign and adjust offset so that inDirection goes to 0 or 1 and outDirection goes to 0 to 4
			sign = -1;
			offset = 8 - offset;
		}
		adjustedIn = (sign*inDirection + offset + 8) % 8;
		adjustedOut = (sign*outDirection + offset + 8) % 8;
		switch (adjustedIn * 8 + adjustedOut)
		{
		case 0:
			directions = new int[] {NorthEast, East, SouthWest, East, NorthEast};
			cellIndexes = new int[] {1, 0, 2, 3};
			break;
		case 1:
			directions = new int[] {SouthEast, East, NorthWest, East, SouthEast};
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
		
		//sign*inDir + offset = plannedInDir
		//inDir = (plannedInDir - offset)*sign
		Path [] paths = new Path[4];
		for (int i = 0; i < paths.length; i++) {
			int index = paths.length - 1 - i;
			int cellIndex = (((cellIndexes[index]*2+1-offset)*sign + 7) % 8) / 2;
			Cell childCell = cells[cellIndex];
			paths[index] = new Path(
					childCell, 
					((directions[index] - offset)*sign + 8) % 8, 
					((directions[index+1] - offset)*sign + 8) % 8);
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

	public Path getNext() {
		return next;
	}

	public Path getPrevious() {
		return previous;
	}

	public void append(Path other) {
		if (other.next != other)
		{
			throw new IllegalStateException(
					"Path cannot be appended if it is already attached.");
		}
		other.next = this.next;
		this.next.previous = other;
		this.next = other;
		other.previous = this;
	}

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

	public void remove() {
		next.previous = previous;
		previous.next = next;
		previous = next = this;
	}

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

	public double getTotalLength() {
		double totalLength = 0;
		Path path = this;
		do
		{
			totalLength += path.getLength();
			path = path.getNext();
		}while (path != this);
		return totalLength;
	}

	public Cell getCell() {
		return cell;
	}
}
