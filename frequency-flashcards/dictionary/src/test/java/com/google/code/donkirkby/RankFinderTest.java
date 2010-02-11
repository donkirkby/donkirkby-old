package com.google.code.donkirkby;

import org.junit.Assert;
import org.junit.Test;

public class RankFinderTest {
	@Test
	public void testCharacterRank() throws Exception {
		// SETUP
		MockRankReader characterReader = new MockRankReader("a", "b", "c");
		MockRankReader wordReader = new MockRankReader();
		
		RankFinder finder = new RankFinder();
		finder.setCharacterReader(characterReader);
		finder.setWordReader(wordReader);
		
		// EXEC
		finder.setMaxCharacters(3);
		finder.load();
		int rankA = finder.getCharacterRank("a");
		int rankC = finder.getCharacterRank("c");
		
		// VERIFY
		Assert.assertEquals(
				"rank of A should match",
				1,
				rankA);
		Assert.assertEquals(
				"rank of C should match",
				3,
				rankC);
	}

	@Test
	public void testWordRank() throws Exception {
		// SETUP
		MockRankReader characterReader = new MockRankReader("a", "b", "c");
		MockRankReader wordReader = new MockRankReader("ab", "cc", "bb");
		
		RankFinder finder = new RankFinder();
		finder.setCharacterReader(characterReader);
		finder.setWordReader(wordReader);
		
		// EXEC
		finder.setMaxCharacters(3);
		finder.load();
		int rankCc = finder.getWordRank("cc");
		int rankBb = finder.getWordRank("bb");
		
		// VERIFY
		Assert.assertEquals(
				"rank of cc should match",
				2,
				rankCc);
		Assert.assertEquals(
				"rank of bb should match",
				3,
				rankBb);
	}
	
	@Test
	public void testWordWithUnknownCharacter() throws Exception {
		// SETUP
		MockRankReader characterReader = new MockRankReader("a", "b", "c");
		MockRankReader wordReader = new MockRankReader("ab", "cc", "ad", "bb");
		
		RankFinder finder = new RankFinder();
		finder.setCharacterReader(characterReader);
		finder.setWordReader(wordReader);
		
		// EXEC
		finder.setMaxCharacters(3);
		finder.load();
		int rankCc = finder.getWordRank("cc");
		int rankBb = finder.getWordRank("bb");
		boolean containsAb = finder.containsWord("ab");
		boolean containsBb = finder.containsWord("bb");
		
		// VERIFY
		Assert.assertEquals(
				"rank of cc should match",
				2,
				rankCc);
		Assert.assertEquals(
				"rank of cc should match",
				3,
				rankBb);
		Assert.assertTrue(
				"Should contain ab",
				containsAb);
		Assert.assertTrue(
				"Should contain bb",
				containsBb);
	}
	
	@Test
	public void testRankOfUnknownCharacter() throws Exception {
		// SETUP
		MockRankReader characterReader = new MockRankReader("a", "b", "c");
		MockRankReader wordReader = new MockRankReader();
		
		RankFinder finder = new RankFinder();
		finder.setCharacterReader(characterReader);
		finder.setWordReader(wordReader);
		
		// EXEC
		finder.setMaxCharacters(3);
		finder.load();
		int rankX = finder.getCharacterRank("x");
		
		// VERIFY
		Assert.assertEquals(
				"rank of x should match",
				4, // 1 + max characters loaded
				rankX);
	}
}
