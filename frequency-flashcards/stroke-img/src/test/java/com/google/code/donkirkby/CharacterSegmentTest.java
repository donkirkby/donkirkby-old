package com.google.code.donkirkby;

import java.awt.Polygon;

import org.junit.Assert;
import org.junit.Test;

public class CharacterSegmentTest {
	@Test
	public void testParsing() throws Exception {
		// SETUP
		CharacterSegment segment = 
			new CharacterSegment("#8PR:1,2;3,4");
		
		// EXEC
		int direction = segment.getDirection();
		Polygon shape = segment.getShape();
		boolean isPause = segment.isPause();
		boolean isRadical = segment.isRadical();
		
		// VERIFY
		Assert.assertEquals(
				"Direction should match",
				8,
				direction);
		Assert.assertEquals(
				"X in shape's second point should match",
				3,
				shape.xpoints[1]);
		Assert.assertTrue(
				"Should pause",
				isPause);
		Assert.assertTrue(
				"Should be radical",
				isRadical);
	}
	
	@Test
	public void testStartPoints() throws Exception {
		testOneDirection(
				CharacterSegment.SOUTH_EAST, 
				"100,100;100,-100;-100;-100;-100,100", 
				-100, 
				-100);
		testOneDirection(
				CharacterSegment.SOUTH_WEST, 
				"100,100;100,-100;-100;-100;-100,100", 
				100, 
				-100);
		testOneDirection(
				CharacterSegment.NORTH_WEST, 
				"100,100;100,-100;-100;-100;-100,100", 
				100, 
				100);
		testOneDirection(
				CharacterSegment.NORTH_EAST, 
				"100,100;100,-100;-100;-100;-100,100", 
				-100, 
				100);
	}

	private void testOneDirection(int direction, String points,
			int expectedStartX, int expectedStartY) {
		// SETUP
		CharacterSegment segment = 
			new CharacterSegment(
					"#" + direction +
					"PR:" +
					points);
		
		// EXEC
		int startX = segment.getStartX();
		int startY = segment.getStartY();
		
		// VERIFY
		Assert.assertEquals(
				"Start X should match for direction " + direction,
				expectedStartX,
				startX);
		Assert.assertEquals(
				"Start Y should match for direction " + direction,
				expectedStartY,
				startY);
	}
	
	@Test
	public void testEndPoint() throws Exception {
		int direction = CharacterSegment.SOUTH_EAST;
		// SETUP
		CharacterSegment segment = 
			new CharacterSegment(
					"#" + direction +
					"PR:" +
					"100,100;100,-100;-100;-100;-100,100");
		
		// EXEC
		int endX = segment.getEndX();
		int endY = segment.getEndY();
		
		// VERIFY
		Assert.assertEquals(
				"End X should match for direction " + direction,
				100,
				endX);
		Assert.assertEquals(
				"End Y should match for direction " + direction,
				100,
				endY);
	}
}
