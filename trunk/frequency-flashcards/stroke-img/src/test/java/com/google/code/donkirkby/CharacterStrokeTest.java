package com.google.code.donkirkby;

import org.junit.Assert;
import org.junit.Test;

public class CharacterStrokeTest {
	@Test
	public void testAddNoPause() throws Exception {
		// SETUP
		CharacterStroke stroke = new CharacterStroke();
		CharacterSegment segment1 = new CharacterSegment("#1NO:0,0;1,1");
		CharacterSegment segment2 = new CharacterSegment("#1NO:0,0;1,1");
		
		// EXEC
		stroke.addSegment(segment1);
		boolean result = stroke.addSegment(segment2);
		
		// VERIFY
		Assert.assertTrue(
				"Stroke should get accepted.",
				result);
	}

	@Test
	public void testAddAfterPause() throws Exception {
		// SETUP
		CharacterStroke stroke = new CharacterStroke();
		CharacterSegment segment1 = new CharacterSegment("#1PO:0,0;1,1");
		CharacterSegment segment2 = new CharacterSegment("#1NO:0,0;1,1");
		
		// EXEC
		stroke.addSegment(segment1);
		boolean result = stroke.addSegment(segment2);
		
		// VERIFY
		Assert.assertFalse(
				"Stroke should get rejected.",
				result);
	}
}
