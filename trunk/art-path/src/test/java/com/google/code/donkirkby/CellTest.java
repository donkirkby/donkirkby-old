package com.google.code.donkirkby;

import org.junit.Assert;
import org.junit.Test;

public class CellTest {
//	@Test
//	public void split() throws Exception {
//		// SETUP
//		Cell cell = new Cell(0, 0, 2, 2);
//		
//		// EXEC
//		Cell[] children = cell.split();
//		
//		// VERIFY
//		Assert.assertEquals(4, children.length);
//		Assert.assertEquals(0, children[0].getLeft());
//		Assert.assertEquals(0, children[0].getTop());
//		Assert.assertEquals(new Vector, actual)
//		assertPathEquals("Paths should match.", expectedPath, path);
//	}

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
	
}
