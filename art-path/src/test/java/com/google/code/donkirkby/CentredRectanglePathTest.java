package com.google.code.donkirkby;

import org.junit.Assert;
import org.junit.Test;

public class CentredRectanglePathTest {
	@Test
	public void splitLeftRightMid() throws Exception {
		// SETUP
		DummyRandom random = new DummyRandom();
		random.setDefaultDouble(0.5);
		Path path = new CentredRectanglePath(
				new Cell(0, 0, 8, 6), 
				new Point(0, 3), 
				new Point(8, 3),
				random);
		Path[] expectedChildren = new Path[] {
				new CentredRectanglePath(
						new Cell(0, 0, 4, 6),
						new Point(0, 3),
						new Point(4, 3),
						random),
				new CentredRectanglePath(
						new Cell(4, 0, 8, 6),
						new Point(4, 3),
						new Point(8, 3),
						random),
		};
		
		// EXEC
		Path[] children = path.split();
		
		// VERIFY
		Assert.assertArrayEquals(expectedChildren, children);
	}

	@Test
	public void splitLeftRightMax() throws Exception {
		// SETUP
		DummyRandom random = new DummyRandom();
		random.setDefaultDouble(1.0);
		Path path = new CentredRectanglePath(
				new Cell(0, 0, 8, 6), 
				new Point(0, 3), 
				new Point(8, 3),
				random);
		Path[] expectedChildren = new Path[] {
				new CentredRectanglePath(
						new Cell(0, 0, 6, 6),
						new Point(0, 3),
						new Point(6, 3),
						random),
				new CentredRectanglePath(
						new Cell(6, 0, 8, 6),
						new Point(6, 3),
						new Point(8, 3),
						random),
		};
		
		// EXEC
		Path[] children = path.split();
		
		// VERIFY
		Assert.assertArrayEquals(expectedChildren, children);
	}

	@Test
	public void splitLeftRightMin() throws Exception {
		// SETUP
		DummyRandom random = new DummyRandom();
		random.setDefaultDouble(0.0);
		Path path = new CentredRectanglePath(
				new Cell(10, 0, 18, 6), 
				new Point(10, 3), 
				new Point(18, 3),
				random);
		Path[] expectedChildren = new Path[] {
				new CentredRectanglePath(
						new Cell(10, 0, 12, 6),
						new Point(10, 3),
						new Point(12, 3),
						random),
				new CentredRectanglePath(
						new Cell(12, 0, 18, 6),
						new Point(12, 3),
						new Point(18, 3),
						random),
		};
		
		// EXEC
		Path[] children = path.split();
		
		// VERIFY
		Assert.assertArrayEquals(expectedChildren, children);
	}

	// The split line is not random, because it's tied to the entry and exit
	// points. The crossing point is random, so it will be at the max. 
	@Test
	public void splitTopBottomMax() throws Exception {
		// SETUP
		DummyRandom random = new DummyRandom();
		random.setDefaultDouble(1.0);
		Path path = new CentredRectanglePath(
				new Cell(0, 0, 8, 10), 
				new Point(0, 5), 
				new Point(8, 5),
				random);
		Path[] expectedChildren = new Path[] {
				new CentredRectanglePath(
						new Cell(0, 0, 8, 5),
						new Point(0, 5),
						new Point(6, 5),
						random),
				new CentredRectanglePath(
						new Cell(0, 5, 8, 10),
						new Point(6, 5),
						new Point(8, 5),
						random),
		};
		
		// EXEC
		Path[] children = path.split();
		
		// VERIFY
		Assert.assertArrayEquals(expectedChildren, children);
	}

	// The split line is not random, because it's tied to the entry
	// point. The crossing point is not random, because it's tied to the exit
	// point. 
	@Test
	public void splitPinned() throws Exception {
		// SETUP
		DummyRandom random = new DummyRandom();
		Path path = new CentredRectanglePath(
				new Cell(0, 0, 8, 10), 
				new Point(0, 5), 
				new Point(4, 10),
				random);
		random.setDefaultDouble(1.0);
		Path[] expectedChildren = new Path[] {
				new CentredRectanglePath(
						new Cell(0, 0, 8, 5),
						new Point(0, 5),
						new Point(4, 5),
						random),
				new CentredRectanglePath(
						new Cell(0, 5, 8, 10),
						new Point(4, 5),
						new Point(4, 10),
						random),
		};
		
		// EXEC
		Path[] children = path.split();
		
		// VERIFY
		Assert.assertArrayEquals(expectedChildren, children);
	}

	@Test
	public void splitBottomTop() throws Exception {
		// SETUP
		DummyRandom random = new DummyRandom();
		Path path = new CentredRectanglePath(
				new Cell(0, 0, 8, 10), 
				new Point(4, 10),
				new Point(0, 5), 
				random);
		random.setDefaultDouble(1.0);
		Path[] expectedChildren = new Path[] {
				new CentredRectanglePath(
						new Cell(0, 5, 8, 10),
						new Point(4, 10),
						new Point(4, 5),
						random),
				new CentredRectanglePath(
						new Cell(0, 0, 8, 5),
						new Point(4, 5),
						new Point(0, 5),
						random),
		};
		
		// EXEC
		Path[] children = path.split();
		
		// VERIFY
		Assert.assertArrayEquals(expectedChildren, children);
	}

	// If the entry and exit are on the same side, then you may not be able to
	// split the thicker dimension.
	@Test
	public void splitThin() throws Exception {
		// SETUP
		DummyRandom random = new DummyRandom();
		random.setDefaultDouble(0.5);
		Path path = new CentredRectanglePath(
				new Cell(0, 0, 8, 10), 
				new Point(0, 0),
				new Point(8, 0), 
				random);
		Path[] expectedChildren = new Path[] {
				new CentredRectanglePath(
						new Cell(0, 0, 4, 10),
						new Point(0, 0),
						new Point(4, 5),
						random),
				new CentredRectanglePath(
						new Cell(4, 0, 8, 10),
						new Point(4, 5),
						new Point(8, 0),
						random),
		};
		
		// EXEC
		Path[] children = path.split();
		
		// VERIFY
		Assert.assertArrayEquals(expectedChildren, children);
	}

	// Coordinates get pinned just like during splitting.
	@Test
	public void getCoordinatesPinned() throws Exception {
		// SETUP
		DummyRandom random = new DummyRandom();
		Path path = new CentredRectanglePath(
				new Cell(0, 0, 8, 10), 
				new Point(0, 5), 
				new Point(4, 10),
				random);
		double [] expectedCoordinates = new double[] {0, 5, 4, 5, 4, 10};
		
		// EXEC
		double [] coordinates = path.getCoordinates();
		
		// VERIFY
		Assert.assertEquals(
				"length", 
				expectedCoordinates.length, 
				coordinates.length);
		for (int i = 0; i < coordinates.length; i++) {
			
			Assert.assertEquals(
					"coordinate " + i, 
					expectedCoordinates[i], 
					coordinates[i],
					0.001);
		}
	}

	@Test
	public void getLength() throws Exception {
		// SETUP
		DummyRandom random = new DummyRandom();
		Path path = new CentredRectanglePath(
				new Cell(0, 0, 8, 10), 
				new Point(0, 5), 
				new Point(4, 10),
				random);
		double expectedLength = 4 + 5;
		
		// EXEC
		double length = path.getLength();
		
		// VERIFY
		Assert.assertEquals(
				"length",
				expectedLength,
				length, 
				0.0001);
	}
	
	@Test
	public void equals() throws Exception {
		// SETUP
		Cell cell1 = new Cell(0, 0, 2, 2);
		Cell cell2 = new Cell(0, 0, 2, 2);
		Cell cell3 = new Cell(0, 0, 3, 3);
		DummyRandom random = new DummyRandom();
		random.setDefaultDouble(0.5);
		Path path1 = new CentredRectanglePath(
				cell1,
				new Point(0, 2),
				new Point(1.5, 2),
				random);
		Path path2 = new CentredRectanglePath(
				cell2,
				new Point(0, 2),
				new Point(1.5, 2),
				random);
		Path path2b = new CentredRectanglePath(
				cell2,
				new Point(0, 2),
				new Point(3, 2),
				random);
		Path path3 = new CentredRectanglePath(
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
		Path path1 = new CentredRectanglePath(
				new Cell(0, 0, 4, 4), 
				new Point(0, 2), 
				new Point(3,4),
				random);
		
		// EXEC
		String string = path1.toString();

		// VERIFY
		Assert.assertEquals(
				"String matches", 
				"CentredRectanglePath(Cell(0, 0, 4, 4), (0,2)->(3,4))", 
				string);
	}
	
	@Test 
	public void chainSplit() throws Exception {
		// EXEC
		Path path1 = CentredRectanglePath.createStartPath(500, 200);
		Path path2 = path1.getNext();
		Path path3 = path2.getNext();
		Path[] children = path2.split();
		Path path2a = children[0];
		Path path2b = children[1];
		
		Path after1 = path1.getNext();
		Path before3 = path3.getPrevious();
		Path after2a = path2a.getNext();
		
		// VERIFY
		Assert.assertSame("after path1", path2a, after1);
		Assert.assertSame("before path3", path2b, before3);
		Assert.assertSame("after path2a", path2b, after2a);
	}
	
	@Test 
	public void createStartPath() throws Exception {
		// EXEC
		Path path1 = CentredRectanglePath.createStartPath(500, 200);
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
