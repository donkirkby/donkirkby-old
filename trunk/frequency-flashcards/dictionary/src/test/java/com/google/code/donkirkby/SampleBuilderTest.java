package com.google.code.donkirkby;

import org.junit.Assert;
import org.junit.Test;

public class SampleBuilderTest {
	@Test
	public void testOnePronunciation() throws Exception {
		// SETUP
		SampleBuilder builder = new SampleBuilder();
		EntryValue[] characterEntries = createEntries("a", "a1");
		EntryValue[] wordEntries = createEntries("ab", "a1 b1", "ca", "c1 a1");
		MockRankReader characterReader = new MockRankReader("a", "b", "c");
		MockRankReader wordReader = new MockRankReader("ca", "ab");
		builder.setRankFinder(createRankFinder(characterReader, wordReader));
		
		// EXEC
		EntryValue[] sampleEntries = 
			builder.build(characterEntries, wordEntries);
		
		// VERIFY
		Assert.assertEquals(
				"number of entries should match",
				1,
				sampleEntries.length);
		Assert.assertEquals(
				"Sample should match",
				wordEntries[1],
				sampleEntries[0].getSample());
	}

	@Test
	public void testCharacterRank() throws Exception {
		// SETUP
		SampleBuilder builder = new SampleBuilder();
		EntryValue[] characterEntries = createEntries("a", "a1");
		EntryValue[] wordEntries = createEntries("ab", "a1 b1", "ca", "c1 a1");
		MockRankReader characterReader = new MockRankReader("d", "a", "b", "c");
		MockRankReader wordReader = new MockRankReader();
		builder.setRankFinder(createRankFinder(characterReader, wordReader));
		
		// EXEC
		EntryValue[] sampleEntries = 
			builder.build(characterEntries, wordEntries);
		
		// VERIFY
		Assert.assertEquals(
				"Character rank should match",
				2,
				sampleEntries[0].getRank());
	}
	
	@Test
	public void testCapitalizedPinyin() throws Exception {
		// SETUP
		SampleBuilder builder = new SampleBuilder();
		EntryValue[] characterEntries = createEntries("a", "a1");
		EntryValue[] wordEntries = createEntries("ab", "a1 b1", "ca", "c1 A1");
		MockRankReader characterReader = new MockRankReader("a", "b", "c");
		MockRankReader wordReader = new MockRankReader("ca", "ab");
		builder.setRankFinder(createRankFinder(characterReader, wordReader));
		
		// EXEC
		EntryValue[] sampleEntries = 
			builder.build(characterEntries, wordEntries);
		
		// VERIFY
		Assert.assertEquals(
				"number of entries should match",
				1,
				sampleEntries.length);
		Assert.assertEquals(
				"Sample should match",
				wordEntries[1],
				sampleEntries[0].getSample());
	}
	
	@Test
	public void testTwoPronunciations() throws Exception {
		// SETUP
		SampleBuilder builder = new SampleBuilder();
		EntryValue[] characterEntries = createEntries("a", "a1", "a", "a2");
		EntryValue[] wordEntries = createEntries("ab", "a1 b1", "ca", "c1 a2");
		MockRankReader characterReader = new MockRankReader("a", "b", "c");
		MockRankReader wordReader = new MockRankReader("ca", "ab");
		builder.setRankFinder(createRankFinder(characterReader, wordReader));
		
		// EXEC
		EntryValue[] sampleEntries = 
			builder.build(characterEntries, wordEntries);
		
		// VERIFY
		Assert.assertEquals(
				"number of entries should match",
				2,
				sampleEntries.length);
		Assert.assertEquals(
				"Sample 1 should match",
				"ca",
				sampleEntries[0].getSample().getTraditionalChars());
		Assert.assertEquals(
				"Sample 2 should match",
				"ab",
				sampleEntries[1].getSample().getTraditionalChars());
	}
	
	@Test
	public void testRarePronunciation() throws Exception {
		// SETUP
		SampleBuilder builder = new SampleBuilder();
		EntryValue[] characterEntries = createEntries("a", "a1", "a", "a2");
		EntryValue[] wordEntries = createEntries("ab", "a1 b1", "ca", "c1 a1");
		MockRankReader characterReader = new MockRankReader("a", "b", "c");
		MockRankReader wordReader = new MockRankReader("ca", "ab");
		builder.setRankFinder(createRankFinder(characterReader, wordReader));
		
		// EXEC
		EntryValue[] sampleEntries = 
			builder.build(characterEntries, wordEntries);
		
		// VERIFY
		Assert.assertEquals(
				"number of entries should match",
				1,
				sampleEntries.length);
		Assert.assertEquals(
				"Sample 1 should match",
				wordEntries[1],
				sampleEntries[0].getSample());
	}
	
	@Test
	public void testFilterWords() throws Exception {
		// SETUP
		SampleBuilder builder = new SampleBuilder();
		EntryValue[] wordEntries = createEntries("ab", "a1 b1", "ca", "c1 a1");
		MockRankReader characterReader = new MockRankReader("a", "b", "c");
		MockRankReader wordReader = new MockRankReader("ca", "ab");
		builder.setRankFinder(createRankFinder(characterReader, wordReader));
		int maxCharacterRank = 2;
		
		// EXEC
		EntryValue[] filteredEntries = 
			builder.filterWords(wordEntries, maxCharacterRank);
		
		// VERIFY
		Assert.assertEquals(
				"number of entries should match",
				1,
				filteredEntries.length);
		Assert.assertEquals(
				"Selected word should match",
				wordEntries[0],
				filteredEntries[0]);
	}
	
	@Test
	public void testRareWordWithCommonCharacters() throws Exception {
		// SETUP
		SampleBuilder builder = new SampleBuilder();
		EntryValue[] wordEntries = createEntries("aab", "a1 a1 b1", "ca", "c1 a1");
		MockRankReader characterReader = new MockRankReader("a", "b", "c");
		MockRankReader wordReader = new MockRankReader("ca", "ab");
		builder.setRankFinder(createRankFinder(characterReader, wordReader));
		int maxCharacterRank = 2;
		
		// EXEC
		EntryValue[] filteredEntries = 
			builder.filterWords(wordEntries, maxCharacterRank);
		
		// VERIFY
		Assert.assertEquals(
				"number of entries should match",
				0,
				filteredEntries.length);
	}
	
	private RankFinder createRankFinder(MockRankReader characterReader,
			MockRankReader wordReader) {
		RankFinder rankFinder = new RankFinder();
		rankFinder.setCharacterReader(characterReader);
		rankFinder.setWordReader(wordReader);
		rankFinder.setMaxCharacters(characterReader.getItemCount());
		rankFinder.load();
		return rankFinder;
	}

	private EntryValue[] createEntries(String... strings) {
		int numEntries = strings.length / 2;
		EntryValue[] entries = new EntryValue[numEntries];
		for (int i = 0; i < numEntries; i++)
		{
			entries[i] = new EntryValue();
			entries[i].setTraditionalChars(strings[i*2]);
			entries[i].setPinyin(strings[i*2 + 1]);
		}
		return entries;
	}
}
