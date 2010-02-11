package com.google.code.donkirkby;

import java.awt.Shape;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;

import org.junit.Assert;
import org.junit.Test;

public class ArrowBuilderTest {
	@Test
	public void testSingleSegment() throws Exception {
		// SETUP
		Path2D expectedPath = new Path2D.Double();
		expectedPath.moveTo(0, 0);
		expectedPath.lineTo(100, 0);
//		expectedPath.lineTo(95, -5);
//		expectedPath.moveTo(100, 0);
//		expectedPath.lineTo(95, 5);
		
		// EXEC
		ArrowBuilder builder = new ArrowBuilder();
		builder.addSegment(0, 0, 100, 0);
		Shape path = builder.getShape();
		
		assertPathEquals("Paths should match.", expectedPath, path);
	}

	@Test
	public void testTwoSegments() throws Exception {
		// SETUP
		Path2D expectedPath = new Path2D.Double();
		expectedPath.moveTo(0, 0);
		expectedPath.lineTo(100, 0);
		expectedPath.lineTo(100, 100);
//		expectedPath.lineTo(95, -5);
//		expectedPath.moveTo(100, 0);
//		expectedPath.lineTo(95, 5);
		
		// EXEC
		ArrowBuilder builder = new ArrowBuilder();
		builder.addSegment(0, 0, 100, 0);
		builder.addSegment(100, 0, 100, 100);
		Shape path = builder.getShape();
		
		assertPathEquals("Paths should match.", expectedPath, path);
	}
	
	private void assertPathEquals(String message, Path2D expectedPath,
			Shape path) {
		PathIterator pathIterator = path.getPathIterator(null);
		PathIterator expectedPathIterator = expectedPath.getPathIterator(null);
		double[] expectedCoords = new double[6];
		double[] coords = new double[6];
		int segmentNumber = 0;
		while (!expectedPathIterator.isDone()) {
			Assert.assertFalse(
					message + " Path should not be shorter than expected.",
					pathIterator.isDone());
			int expectedSegmentType = 
				expectedPathIterator.currentSegment(expectedCoords);
			int segmentType =
				pathIterator.currentSegment(coords);
			
			Assert.assertEquals(
					message + " Segment type of segment " + segmentNumber + 
					" should match.",
					expectedSegmentType,
					segmentType);
			for (int i = 0; i < expectedCoords.length; i++) {
				double expectedCoord = expectedCoords[i];
				double coord = coords[i];
				
				Assert.assertEquals(
						message + " Coordinate " + i + " of segment " + 
						segmentNumber + " should match",
						expectedCoord,
						coord,
						0.1); // accuracy
			}
			pathIterator.next();
			expectedPathIterator.next();
			segmentNumber++;
		}
	}
}
