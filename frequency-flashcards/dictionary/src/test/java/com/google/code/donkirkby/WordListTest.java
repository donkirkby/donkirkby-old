package com.google.code.donkirkby;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.junit.Assert;
import org.junit.Test;


public class WordListTest {
	@Test
	public void skipHighCharacters() throws Exception {
		// SETUP
		int maxCharacterRank = 3;
		
		MockRankReader wordReader = new MockRankReader("AB", "AD", "BC");
		
		WordList list = new WordList();
		list.setRankFinder(createRankFinder());
		list.setWordReader(wordReader);
		
		// EXEC
		Iterator<String> itr = list.iterator(maxCharacterRank);
		boolean hasNextAtStart = itr.hasNext();
		String word1 = itr.next();
		String word2 = itr.next();
		boolean hasNextAtEnd = itr.hasNext();
		
		// VERIFY
		Assert.assertEquals(
				"first word",
				"AB",
				word1);
		Assert.assertEquals(
				"second word",
				"BC",
				word2);
		Assert.assertEquals(
				"hasNext at start",
				true,
				hasNextAtStart);
		Assert.assertEquals(
				"hasNext at end",
				false,
				hasNextAtEnd);
	}
	
	@Test
	public void comeBackForHigherCharacters() throws Exception {
		// SETUP
		MockRankReader wordReader = new MockRankReader("AF", "AD", "BC");
		
		WordList list = new WordList();
		list.setRankFinder(createRankFinder());
		list.setWordReader(wordReader);
		
		// EXEC
		Iterator<String> itr1 = list.iterator(3);
		String word1 = itr1.next();
		Iterator<String> itr2 = list.iterator(4);
		String word2 = itr2.next();
		
		// VERIFY
		Assert.assertEquals(
				"first word",
				"BC",
				word1);
		Assert.assertEquals(
				"second word",
				"AD",
				word2);
	}
	
	@Test
	public void remove() throws Exception {
		// SETUP
		int maxCharacterRank = 10;
		
		MockRankReader wordReader = new MockRankReader("AB", "AD", "BC");
		
		WordList list = new WordList();
		list.setRankFinder(createRankFinder());
		list.setWordReader(wordReader);
		
		// EXEC
		Iterator<String> itr1 = list.iterator(maxCharacterRank);
		String word1 = itr1.next();
		itr1.remove();
		Iterator<String> itr2 = list.iterator(maxCharacterRank);
		String word2 = itr2.next();
		
		// VERIFY
		Assert.assertEquals(
				"first word",
				"AB",
				word1);
		Assert.assertEquals(
				"second word",
				"AD",
				word2);
	}
	
	@Test
	public void nextPastEnd() throws Exception {
		// SETUP
		int maxCharacterRank = 3;
		
		MockRankReader wordReader = new MockRankReader("AB", "FF");
		
		WordList list = new WordList();
		list.setRankFinder(createRankFinder());
		list.setWordReader(wordReader);
		
		// EXEC
		Iterator<String> itr = list.iterator(maxCharacterRank);
		itr.next();
		String msg = null;
		try
		{
			itr.next();
			
			Assert.fail("Should have thrown");
		}catch (NoSuchElementException ex)
		{
			msg = ex.getMessage();
		}
		
		// VERIFY
		Assert.assertEquals(
				"message",
				null,
				msg);
	}
	
	private RankFinder createRankFinder()
	{
		MockRankReader characterReader = 
			new MockRankReader("A", "B", "C", "D", "E", "F");
		MockRankReader wordReader = new MockRankReader();
		
		RankFinder finder = new RankFinder();
		finder.setCharacterReader(characterReader);
		finder.setWordReader(wordReader);
		finder.setMaxCharacters(6);
		return finder;
	}
}
