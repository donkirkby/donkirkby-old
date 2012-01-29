package com.google.code.donkirkby;

import org.junit.Assert;
import org.junit.Test;

public class PointTest {
	@Test
	public void moveTowardHalf()
	{
		// SETUP
		Point start = new Point(0, 0);
		Point end = new Point(150, 100);
		Point expectedPoint = new Point(75, 50);
		
		// EXEC
		Point point = start.moveToward(end, 0.5);
		
		// VERIFY
		Assert.assertEquals("point", expectedPoint, point);
	}
	
	@Test
	public void moveTowardQuarter()
	{
		// SETUP
		Point start = new Point(110, 80);
		Point end = new Point(150, 100);
		Point expectedPoint = new Point(120, 85);
		
		// EXEC
		Point point = start.moveToward(end, 0.25);
		
		// VERIFY
		Assert.assertEquals("point", expectedPoint, point);
	}
	
	@Test
	public void string()
	{
		// SETUP
		Point point = new Point(150, 100);
		
		// EXEC
		String string = point.toString();
		
		// VERIFY
		Assert.assertEquals(
				"string", 
				"(150,100)", 
				string);
	}
	
	@Test
	public void equality()
	{
		// SETUP
		Point point1 = new Point(10, 30);
		Point point2 = new Point(10, 30);
		
		// EXEC
		Assert.assertEquals(
				"equality", 
				point1, 
				point2);
	}
	
	@Test
	public void calculateDistanceSquared()
	{
		// SETUP
		Point point1 = new Point(10, 30);
		Point point2 = new Point(100, 230);
		double expectedDistanceSquared = 90*90 + 200*200;
		
		// EXEC
		double distanceSquared = point1.distanceSquaredTo(point2);
		
		// VERIFY
		double delta = 0.001;
		Assert.assertEquals(
				"distance squared", 
				expectedDistanceSquared, 
				distanceSquared,
				delta);
	}
}
