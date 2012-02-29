package com.google.code.donkirkby;

import java.util.Random;

import org.junit.Assert;
import org.junit.Test;

public class RectanglePathTest {
	@Test
	public void splitLeftBottomMid() throws Exception {
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
	public void splitLeftBottomMax() throws Exception {
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
	public void splitRightBottom() throws Exception {
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
	public void splitBottomRight() throws Exception {
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
	public void splitHorizontal() throws Exception {
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

	/*
	 * Starting path does not go around a corner, and the gap is to small
	 * to split.
	 */
	@Test
	public void splitSmallGapCrossingVertical() throws Exception {
		// SETUP
		DummyRandom random = new DummyRandom();
		random.setDefaultDouble(0.5);
		Path path = new RectanglePath(
				new Cell(0, 0, 8, 8), 
				new Point(0, 4), 
				new Point(8, 5),
				random);
		Path[] expectedChildren = new Path[] {
				new RectanglePath(
						new Cell(0, 0, 4, 8),
						new Point(0, 4),
						new Point(4, 4),
						random),
				new RectanglePath(
						new Cell(4, 0, 8, 8),
						new Point(4, 4),
						new Point(8, 5),
						random),
		};
		
		// EXEC
		Path[] children = path.split();
		
		// VERIFY
		Assert.assertArrayEquals(expectedChildren, children);
	}

	@Test
	public void splitSmallGapCrossingHorizontal() throws Exception {
		// SETUP
		DummyRandom random = new DummyRandom();
		random.setDefaultDouble(0.5);
		Path path = new RectanglePath(
				new Cell(0, 0, 8, 8), 
				new Point(4, 0), 
				new Point(5, 8),
				random);
		Path[] expectedChildren = new Path[] {
				new RectanglePath(
						new Cell(0, 0, 8, 4),
						new Point(4, 0),
						new Point(4, 4),
						random),
				new RectanglePath(
						new Cell(0, 4, 8, 8),
						new Point(4, 4),
						new Point(5, 8),
						random),
		};
		
		// EXEC
		Path[] children = path.split();
		
		// VERIFY
		Assert.assertArrayEquals(expectedChildren, children);
	}

	@Test
	public void splitSmallGapCrossingHorizontalReversed() throws Exception {
		// SETUP
		DummyRandom random = new DummyRandom();
		random.setDefaultDouble(0.5);
		Path path = new RectanglePath(
				new Cell(0, 0, 8, 8), 
				new Point(5, 8),
				new Point(4, 0), 
				random);
		Path[] expectedChildren = new Path[] {
				new RectanglePath(
						new Cell(0, 4, 8, 8),
						new Point(5, 8),
						new Point(4, 4),
						random),
				new RectanglePath(
						new Cell(0, 0, 8, 4),
						new Point(4, 4),
						new Point(4, 0),
						random),
		};
		
		// EXEC
		Path[] children = path.split();
		
		// VERIFY
		Assert.assertArrayEquals(expectedChildren, children);
	}

	@Test
	public void splitBigGapCrossingHorizontal() throws Exception {
		// SETUP
		DummyRandom random = new DummyRandom();
		random.setDefaultDouble(0.5);
		Path path = new RectanglePath(
				new Cell(0, 0, 2, 8), 
				new Point(0, 2), 
				new Point(2, 6),
				random);
		Path[] expectedChildren = new Path[] {
				new RectanglePath(
						new Cell(0, 0, 2, 4),
						new Point(0, 2),
						new Point(1, 4),
						random),
				new RectanglePath(
						new Cell(0, 4, 2, 8),
						new Point(1, 4),
						new Point(2, 6),
						random),
		};
		
		// EXEC
		Path[] children = path.split();
		
		// VERIFY
		Assert.assertArrayEquals(expectedChildren, children);
	}

	@Test
	public void splitBigGapCrossingHorizontalNotReversed() throws Exception {
		// SETUP
		DummyRandom random = new DummyRandom();
		random.setDefaultDouble(0.5);
		Path path = new RectanglePath(
				new Cell(0, 0, 2, 8), 
				new Point(2, 2), 
				new Point(0, 6),
				random);
		Path[] expectedChildren = new Path[] {
				new RectanglePath(
						new Cell(0, 0, 2, 4),
						new Point(2, 2),
						new Point(1, 4),
						random),
				new RectanglePath(
						new Cell(0, 4, 2, 8),
						new Point(1, 4),
						new Point(0, 6),
						random),
		};
		
		// EXEC
		Path[] children = path.split();
		
		// VERIFY
		Assert.assertArrayEquals(expectedChildren, children);
	}

	@Test
	public void splitChainCorner() throws Exception {
		// SETUP
		Random random = new Random();
		Path path1 = new RectanglePath(
				new Cell(0, 0, 8, 8), 
				new Point(4, 8), 
				new Point(8, 4), 
				random);
		Path path2 = new RectanglePath(
				new Cell(8, 0, 16, 8),
				new Point(8, 4), 
				new Point(12, 8), 
				random);
		Path path3 = new RectanglePath(
				new Cell(8, 8, 16, 16),
				new Point(12, 8), 
				new Point(8, 12), 
				random);
		Path path4 = new RectanglePath(
				new Cell(0, 8, 8, 16),
				new Point(8, 12), 
				new Point(4, 8), 
				random);
		
		// EXEC
		path1.append(path2);
		path2.append(path3);
		path3.append(path4);
		Path[] children = path1.split(); //1a, 1b
		Path after1b = children[1].getNext();
		Path before1a = children[0].getPrevious();
		Path after4 = path4.getNext();
		Path before2 = path2.getPrevious();
		
		// VERIFY
		Assert.assertEquals("after1d", path2, after1b);
		Assert.assertEquals("before1a", path4, before1a);
		Assert.assertEquals("after4", children[0], after4);
		Assert.assertEquals("before2", children[1], before2);
	}

	@Test
	public void splitChainCrossing() throws Exception {
		// SETUP
		Random random = new Random();
		Path path1 = new RectanglePath(
				new Cell(0, 0, 8, 8), 
				new Point(2, 8), 
				new Point(8, 4), 
				random);
		Path path2 = new RectanglePath(
				new Cell(8, 0, 16, 8),
				new Point(8, 4), 
				new Point(14, 8), 
				random);
		Path path3 = new RectanglePath(
				new Cell(9, 8, 16, 16),
				new Point(14, 8), 
				new Point(9, 14), 
				random);
		Path path4 = new RectanglePath(
				new Cell(7, 8, 9, 16),
				new Point(9, 14), 
				new Point(7, 10), 
				random);
		Path path5 = new RectanglePath(
				new Cell(0, 8, 7, 16),
				new Point(7, 10), 
				new Point(2, 8), 
				random);
		
		// EXEC
		path1.append(path2);
		path2.append(path3);
		path3.append(path4);
		path4.append(path5);
		Path[] children = path4.split(); //4a, 4b
		Path after4b = children[1].getNext();
		Path before4a = children[0].getPrevious();
		Path after3 = path3.getNext();
		Path before5 = path5.getPrevious();
		
		// VERIFY
		Assert.assertEquals("after4d", path5, after4b);
		Assert.assertEquals("before4a", path3, before4a);
		Assert.assertEquals("after3", children[0], after3);
		Assert.assertEquals("before5", children[1], before5);
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
	
	@Test 
	public void createStartPath() throws Exception {
		// EXEC
		Path path1 = RectanglePath.createStartPath(500, 200);
		Path path2 = path1.getNext();
		Path path3 = path2.getNext();
		Path path4 = path3.getNext();
		Path afterPath4 = path4.getNext();
		
		// VERIFY
		Assert.assertNotSame("path2", path1, path2);
		Assert.assertNotSame("path3", path1, path3);
		Assert.assertSame("after path4", path1, afterPath4);
	}
}
