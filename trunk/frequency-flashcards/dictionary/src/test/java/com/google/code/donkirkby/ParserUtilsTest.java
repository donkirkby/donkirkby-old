package com.google.code.donkirkby;

import org.junit.Assert;
import org.junit.Test;

public class ParserUtilsTest {
	@Test
	public void testAddToneMarks() throws Exception {
		// SETUP
		String rawPinyin = "ni3 hao4";
		
		// EXEC
		String pinyin = ParserUtils.addToneMarks(rawPinyin);
		
		// VERIFY
		Assert.assertEquals(
				"pinyin should match.",
				"nǐ hào",
				pinyin);
	}
	
	@Test
	public void testAddToneMarksWithSpaces() throws Exception {
		// SETUP
		String rawPinyin = "ni3hao4";
		
		// EXEC
		String pinyin = ParserUtils.addToneMarks(rawPinyin);
		
		// VERIFY
		Assert.assertEquals(
				"pinyin should match.",
				"nǐ hào",
				pinyin);
	}
}
