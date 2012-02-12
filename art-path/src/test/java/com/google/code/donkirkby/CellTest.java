package com.google.code.donkirkby;

import org.junit.Assert;
import org.junit.Test;

public class CellTest {
	@Test
	public void split() throws Exception {
		// SETUP
		Cell cell = new Cell(0, 0, 2, 2);
		Cell[] expectedChildren = new Cell[] {
				new Cell(1, 0, 2, 1),
				new Cell(0, 0, 1, 1),
				new Cell(0, 1, 1, 2),
				new Cell(1, 1, 2, 2)				
		};
		
		// EXEC
		Cell[] children = cell.split();
		
		// VERIFY
		Assert.assertArrayEquals(expectedChildren, children);
	}
	
	@Test
	public void splitX() throws Exception {
		// SETUP
		Cell cell = new Cell(0, 0, 2, 2);
		Cell[] expectedChildren = new Cell[] {
				new Cell(0, 0, 1, 2),
				new Cell(1, 0, 2, 2)				
		};
		
		// EXEC
		Cell[] children = cell.splitX(1);
		
		// VERIFY
		Assert.assertArrayEquals(expectedChildren, children);
	}
	
	@Test
	public void equals() throws Exception {
		// SETUP
		Cell cell1 = new Cell(0, 0, 2, 2);
		Cell cell2 = new Cell(0, 0, 2, 2);
		Cell cell3 = new Cell(1, 1, 3, 3);
		
		// EXEC
		boolean isEqualToSame = cell1.equals(cell2);
		boolean isEqualToDifferent = cell1.equals(cell3);

		// VERIFY
		Assert.assertEquals(
				"Equals similar cell", 
				true, 
				isEqualToSame);
		Assert.assertEquals(
				"Equals different cell", 
				false, 
				isEqualToDifferent);
	}

	@Test
	public void string() throws Exception {
		// SETUP
		Cell cell = new Cell(0, 0, 2, 2);
		
		// EXEC
		String string = cell.toString();

		// VERIFY
		Assert.assertEquals(
				"String matches", 
				"Cell(0, 0, 2, 2)", 
				string);
	}
	
}
