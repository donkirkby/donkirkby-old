package com.google.code.donkirkby;

import java.util.ArrayList;
import java.util.Random;

import org.junit.Assert;
import org.junit.Test;

public class TrianglePathTest {
	@SuppressWarnings("serial")
	private class DummyRandom extends Random {
		private ArrayList<Double> nextDoubles = new ArrayList<Double>();
		private Double defaultDouble = null;
		
		@Override
		public double nextDouble() {
			if (nextDoubles.size() > 0)
			{
				return nextDoubles.remove(0);
			}
			if (defaultDouble != null)
			{
				return defaultDouble;
			}
			throw new IllegalStateException("No double values available.");
		}
		
//		public void addDouble(double d){
//			nextDoubles.add(d);
//		}
		
		public void setDefaultDouble(Double d){
			defaultDouble = d;
		}
	}
	
	@Test
	public void splitOppositeInHalf()
	{
		// SETUP
		DummyRandom random = new DummyRandom();
		random.setDefaultDouble(0.5);
		TrianglePath path = new TrianglePath(
				new Point(0, 0),
				new Point(0, 100),
				new Point(100, 0),
				new Point(0, 50),
				new Point(50, 0),
				random);
		Path[] expectedPaths = new Path[] {
				new TrianglePath(
						new Point(0, 0),
						new Point(0, 100),
						new Point(50, 50),
						new Point(0, 50),
						new Point(25, 25),
						random),
				new TrianglePath(
						new Point(0, 0),
						new Point(50, 50),
						new Point(100, 0),
						new Point(25, 25),
						new Point(50, 0),
						random)
		};
		
		// EXEC
		Path[] paths = path.split();
		
		// VERIFY
		Assert.assertArrayEquals("paths", expectedPaths, paths);
	}
	
	// The random decisions choose positions evenly distributed between
	// 0.25 and 0.75 of the distance along a line segment.
	@Test
	public void splitOppositeMinimumDistances()
	{
		// SETUP
		DummyRandom random = new DummyRandom();
		random.setDefaultDouble(0.0);
		TrianglePath path = new TrianglePath(
				new Point(0, 0),
				new Point(0, 100),
				new Point(100, 0),
				new Point(0, 50),
				new Point(50, 0),
				random);
		Path[] expectedPaths = new Path[] {
				new TrianglePath(
						new Point(0, 0),
						new Point(0, 100),
						new Point(25, 75),
						new Point(0, 50),
						new Point(25*0.25, 75*0.25),
						random),
				new TrianglePath(
						new Point(0, 0),
						new Point(25, 75),
						new Point(100, 0),
						new Point(25*0.25, 75*0.25),
						new Point(50, 0),
						random)
		};
		
		// EXEC
		Path[] paths = path.split();
		
		// VERIFY
		Assert.assertArrayEquals("paths", expectedPaths, paths);
	}
	
	// Always splits along the longest side
	@Test
	public void splitExitSide()
	{
		// SETUP
		DummyRandom random = new DummyRandom();
		random.setDefaultDouble(0.5);
		TrianglePath path = new TrianglePath(
				new Point(0, 100),
				new Point(0, 0),
				new Point(100, 0),
				new Point(0, 50),
				new Point(50, 50),
				random);
		Path[] expectedPaths = new Path[] {
				new TrianglePath(
						new Point(0, 0),
						new Point(0, 100),
						new Point(25, 75),
						new Point(0, 50),
						new Point(12.5, 37.5),
						random),
				new TrianglePath(
						new Point(25, 75),
						new Point(0, 0),
						new Point(100, 0),
						new Point(12.5, 37.5),
						new Point(50, 50),
						random)
		};
		
		// EXEC
		Path[] paths = path.split();
		
		// VERIFY
		Assert.assertArrayEquals("paths", expectedPaths, paths);
	}
	
	// Always splits along the longest side
	@Test
	public void splitEntrySide()
	{
		// SETUP
		DummyRandom random = new DummyRandom();
		random.setDefaultDouble(0.5);
		TrianglePath path = new TrianglePath(
				new Point(0, 100),
				new Point(100, 0),
				new Point(0, 0),
				new Point(50, 50),
				new Point(0, 50),
				random);
		Path[] expectedPaths = new Path[] {
				new TrianglePath(
						new Point(25, 75),
						new Point(100, 0),
						new Point(0, 0),
						new Point(50, 50),
						new Point(12.5, 37.5),
						random),
				new TrianglePath(
						new Point(0, 0),
						new Point(25, 75),
						new Point(0, 100),
						new Point(12.5, 37.5),
						new Point(0, 50),
						random)
		};
		
		// EXEC
		Path[] paths = path.split();
		
		// VERIFY
		Assert.assertArrayEquals("paths", expectedPaths, paths);
	}
	
	@Test
	public void string()
	{
		// SETUP
		DummyRandom random = new DummyRandom();
		TrianglePath path = new TrianglePath(
				new Point(0, 0),
				new Point(0, 100),
				new Point(100, 0),
				new Point(0, 50),
				new Point(50, 0),
				random);
		
		// EXEC
		String string = path.toString();
		
		// VERIFY
		Assert.assertEquals(
				"string", 
				"TrianglePath((0,0), (0,100), (100,0), (0,50)->(50,0)", 
				string);
	}
	
	@Test
	public void equality()
	{
		// SETUP
		DummyRandom random = new DummyRandom();
		TrianglePath path1 = new TrianglePath(
				new Point(0, 0),
				new Point(0, 100),
				new Point(100, 0),
				new Point(0, 50),
				new Point(50, 0),
				random);
		TrianglePath path2 = new TrianglePath(
				new Point(0, 0),
				new Point(0, 100),
				new Point(100, 0),
				new Point(0, 50),
				new Point(50, 0),
				random);
		
		// EXEC
		Assert.assertEquals(
				"equality", 
				path1, 
				path2);
	}
	
	// Always splits along the longest side
	@Test
	public void splitChain()
	{
		// SETUP
		DummyRandom random = new DummyRandom();
		random.setDefaultDouble(0.5);
		TrianglePath bottom = new TrianglePath(
				new Point(50, 50),
				new Point(0, 0),
				new Point(100, 0),
				new Point(25, 25),
				new Point(75, 25),
				random);
		TrianglePath right = new TrianglePath(
				new Point(50, 50),
				new Point(100, 0),
				new Point(100, 100),
				new Point(75, 25),
				new Point(75, 75),
				random);
		TrianglePath top = new TrianglePath(
				new Point(50, 50),
				new Point(100, 100),
				new Point(0, 100),
				new Point(75, 75),
				new Point(25, 75),
				random);
		TrianglePath left = new TrianglePath(
				new Point(50, 50),
				new Point(0, 100),
				new Point(0, 0),
				new Point(25, 75),
				new Point(25, 25),
				random);
		bottom.append(right).append(top).append(left);
		
		// EXEC
		Path[] paths = bottom.split();
		Path bottomLeft = paths[0];
		Path bottomRight = paths[1];
		
		// VERIFY
		Assert.assertSame(
				"bottomLeft.previous", 
				left, 
				bottomLeft.getPrevious());
		Assert.assertSame(
				"bottomLeft.next", 
				bottomRight, 
				bottomLeft.getNext());
		Assert.assertSame(
				"left.next", 
				bottomLeft, 
				left.getNext());
		Assert.assertSame(
				"right.previous", 
				bottomRight, 
				right.getPrevious());
	}
}
