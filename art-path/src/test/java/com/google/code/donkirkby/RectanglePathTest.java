package com.google.code.donkirkby;

import org.junit.Assert;
import org.junit.Test;

public class RectanglePathTest {
	@Test
	public void splitEastSouthMid() throws Exception {
		// SETUP
		DummyRandom random = new DummyRandom();
		random.setDefaultDouble(0.5);
		Path path = new RectanglePath(
				new Cell(0, 0, 8, 8), 
				new Point(0, 4), 
				new Point(6, 8),
				random);
		Path[] expectedChildren = new Path[] {
				new RectanglePath(
						new Cell(0, 0, 3, 8),
						new Point(0, 4),
						new Point(3, 4),
						random),
				new RectanglePath(
						new Cell(3, 0, 8, 8),
						new Point(3, 4),
						new Point(6, 8),
						random),
		};
		
		// EXEC
		Path[] children = path.split();
		
		// VERIFY
		Assert.assertArrayEquals(expectedChildren, children);
	}

	@Test
	public void splitEastSouthMax() throws Exception {
		// SETUP
		DummyRandom random = new DummyRandom();
		random.setDefaultDouble(1.0);
		Path path = new RectanglePath(
				new Cell(0, 0, 8, 8), 
				new Point(0, 4), 
				new Point(6, 8),
				random);
		Path[] expectedChildren = new Path[] {
				new RectanglePath(
						new Cell(0, 0, 6*0.75, 8),
						new Point(0, 4),
						new Point(6*0.75, 8*0.75),
						random),
				new RectanglePath(
						new Cell(6*0.75, 0, 8, 8),
						new Point(6*0.75, 8*0.75),
						new Point(6, 8),
						random),
		};
		
		// EXEC
		Path[] children = path.split();
		
		// VERIFY
		Assert.assertArrayEquals(expectedChildren, children);
	}

	@Test
	public void splitWestSouth() throws Exception {
		// SETUP
		DummyRandom random = new DummyRandom();
		random.setDefaultDouble(0.5);
		Path path = new RectanglePath(
				new Cell(0, 0, 8, 8), 
				new Point(8, 4), 
				new Point(2, 8),
				random);
		Path[] expectedChildren = new Path[] {
				new RectanglePath(
						new Cell(5, 0, 8, 8),
						new Point(8, 4),
						new Point(5, 4),
						random),
				new RectanglePath(
						new Cell(0, 0, 5, 8),
						new Point(5, 4),
						new Point(2, 8),
						random),
		};
		
		// EXEC
		Path[] children = path.split();
		
		// VERIFY
		Assert.assertArrayEquals(expectedChildren, children);
	}

	@Test
	public void splitNorthEast() throws Exception {
		// SETUP
		DummyRandom random = new DummyRandom();
		random.setDefaultDouble(0.5);
		Path path = new RectanglePath(
				new Cell(0, 0, 8, 8), 
				new Point(2, 8),
				new Point(8, 4), 
				random);
		Path[] expectedChildren = new Path[] {
				new RectanglePath(
						new Cell(0, 0, 5, 8),
						new Point(2, 8),
						new Point(5, 4),
						random),
				new RectanglePath(
						new Cell(5, 0, 8, 8),
						new Point(5, 4),
						new Point(8, 4),
						random),
		};
		
		// EXEC
		Path[] children = path.split();
		
		// VERIFY
		Assert.assertArrayEquals(expectedChildren, children);
	}

	@Test
	public void splitY() throws Exception {
		// SETUP
		DummyRandom random = new DummyRandom();
		random.setDefaultDouble(0.5);
		Path path = new RectanglePath(
				new Cell(0, 0, 8, 8), 
				new Point(0, 2), 
				new Point(4, 8),
				random);
		Path[] expectedChildren = new Path[] {
				new RectanglePath(
						new Cell(0, 0, 8, 5),
						new Point(0, 2),
						new Point(4, 5),
						random),
				new RectanglePath(
						new Cell(0, 5, 8, 8),
						new Point(4, 5),
						new Point(4, 8),
						random),
		};
		
		// EXEC
		Path[] children = path.split();
		
		// VERIFY
		Assert.assertArrayEquals(expectedChildren, children);
	}

	@Test
	public void equals() throws Exception {
		// SETUP
		Cell cell1 = new Cell(0, 0, 2, 2);
		Cell cell2 = new Cell(0, 0, 2, 2);
		Cell cell3 = new Cell(0, 0, 3, 3);
		DummyRandom random = new DummyRandom();
		Path path1 = new RectanglePath(
				cell1,
				new Point(0, 2),
				new Point(1.5, 2),
				random);
		Path path2 = new RectanglePath(
				cell2,
				new Point(0, 2),
				new Point(1.5, 2),
				random);
		Path path2b = new RectanglePath(
				cell2,
				new Point(0, 2),
				new Point(3, 2),
				random);
		Path path3 = new RectanglePath(
				cell3,
				new Point(0, 2),
				new Point(1.5, 2),
				random);

		// EXEC
		boolean isEqualToMatching = path1.equals(path2);
		boolean isEqualToDifferentInOut = path1.equals(path2b);
		boolean isEqualToDifferentCell = path1.equals(path3);

		// VERIFY
		Assert.assertEquals(
				"Equals similar path", 
				true,
				isEqualToMatching);
		Assert.assertEquals(
				"Equals different in/out", 
				false, 
				isEqualToDifferentInOut);
		Assert.assertEquals(
				"Equals different cell", 
				false, 
				isEqualToDifferentCell);
	}

	@Test
	public void string() throws Exception {
		// SETUP
		DummyRandom random = new DummyRandom();
		Path path1 = new RectanglePath(
				new Cell(0, 0, 4, 4), 
				new Point(0, 2), 
				new Point(3,4),
				random);
		
		// EXEC
		String string = path1.toString();

		// VERIFY
		Assert.assertEquals(
				"String matches", 
				"RectanglePath(Cell(0, 0, 4, 4), (0,2)->(3,4))", 
				string);
	}
}
