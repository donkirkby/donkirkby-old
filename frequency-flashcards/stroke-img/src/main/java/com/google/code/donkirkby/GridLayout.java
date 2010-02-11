package com.google.code.donkirkby;



public class GridLayout {
	private int rows;
	private int columns;

	public void setItems(int items) {
		int minGaps = items;
		int rowsWithMinGaps = 1;
		int columnsWithMinGaps = items;
		rows = 1;
		boolean isDone = false;
		while (!isDone)
		{
			columns = items / rows;
			int remainder = items % rows;
			if (remainder != 0)
			{
				columns++;
			}
			if ((columns <= 8 * rows) && (columns >= 2 * rows))
			{
				if (remainder == 0)
				{
					return;
				}
				int gaps = rows - remainder;
				if (gaps < minGaps)
				{
					minGaps = gaps;
					rowsWithMinGaps = rows;
					columnsWithMinGaps = columns;
				}
			}
			isDone = columns <= 2 * rows;
			rows++;
		}
		rows = rowsWithMinGaps;
		columns = columnsWithMinGaps;
	}

	public int getRows() {
		return rows;
	}

	public int getColumns() {
		return columns;
	}
}
