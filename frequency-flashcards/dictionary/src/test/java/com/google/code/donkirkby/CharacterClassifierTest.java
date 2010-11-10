package com.google.code.donkirkby;

import org.junit.Assert;
import org.junit.Test;

public class CharacterClassifierTest {
	@Test
	public void common() throws Exception {
		testClassifier('好', true);
	}
	
	@Test
	public void simplified() throws Exception {
		testClassifier('学', true);
	}
	
	@Test
	public void traditional() throws Exception {
		testClassifier('學', true);
	}
	
	@Test
	public void chineseNumber() throws Exception {
		testClassifier('五', true);
	}

	@Test
	public void chineseQuote() throws Exception {
		testClassifier('”', false);
	}
	
	@Test
	public void chinesePeriod() throws Exception {
		testClassifier('。', false);
	}
	

	private void testClassifier(char c, boolean expected) {
		// SETUP
		CharacterClassifier classifier = new CharacterClassifier();
		
		// EXEC
		boolean isChinese = classifier.isChinese(c);
		
		// VERIFY
		Assert.assertEquals(
				"isChinese",
				expected,
				isChinese);
	}

	@Test
	public void latin() throws Exception {
		testClassifier('X', false);
	}
}
