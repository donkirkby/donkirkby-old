package com.google.code.donkirkby;

import java.util.ArrayList;
import java.util.List;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;

public class DictionaryDaoAggregatorTest {
	@Test
	public void testSingleSource() throws Exception {
		// SETUP
		List<DictionaryDao> sources = new ArrayList<DictionaryDao>();
		DictionaryDao source1 = EasyMock.createMock(DictionaryDao.class);
		sources.add(source1);
		
		DictionaryDaoAggregator aggregator = new DictionaryDaoAggregator();
		aggregator.setSources(sources);
		
		String expectedCharacter = "x";
		int expectedMaxResults = 100;
		
		EntryValue[] source1Results = new EntryValue[1];
		source1Results[0] = new EntryValue();
		source1Results[0].setTraditionalChars(expectedCharacter);
		source1Results[0].setPinyin("x1");

		// EXPECT
		EasyMock.expect(
				source1.findAllEntryValuesByTraditionalCharacter(
						expectedCharacter,
						expectedMaxResults)).andReturn(source1Results);
		EasyMock.replay(source1);
		
		// EXEC
		EntryValue[] result = 
			aggregator.findAllEntryValuesByTraditionalCharacter(
					expectedCharacter, 
					expectedMaxResults);
		
		// VERIFY
		Assert.assertArrayEquals(
				"Results should match.",
				source1Results,
				result);
		EasyMock.verify(source1);
	}
	
	@Test
	public void testTwoSources() throws Exception {
		// SETUP
		List<DictionaryDao> sources = new ArrayList<DictionaryDao>();
		DictionaryDao source1 = EasyMock.createMock(DictionaryDao.class);
		sources.add(source1);
		DictionaryDao source2 = EasyMock.createMock(DictionaryDao.class);
		sources.add(source2);
		
		DictionaryDaoAggregator aggregator = new DictionaryDaoAggregator();
		aggregator.setSources(sources);
		
		String expectedCharacter = "x%";
		int expectedMaxResults = 100;
		
		EntryValue[] source1Results = new EntryValue[1];
		source1Results[0] = new EntryValue();
		source1Results[0].setTraditionalChars("xz");
		source1Results[0].setPinyin("x1");
		EntryValue[] source2Results = new EntryValue[1];
		source2Results[0] = new EntryValue();
		source2Results[0].setTraditionalChars("xy");
		source2Results[0].setPinyin("x2");

		// EXPECT
		EasyMock.expect(
				source1.findAllEntryValuesByTraditionalCharacter(
						expectedCharacter,
						expectedMaxResults)).andReturn(source1Results);
		EasyMock.replay(source1);
		EasyMock.expect(
				source2.findAllEntryValuesByTraditionalCharacter(
						expectedCharacter,
						expectedMaxResults)).andReturn(source2Results);
		EasyMock.replay(source2);
		
		// EXEC
		EntryValue[] result = 
			aggregator.findAllEntryValuesByTraditionalCharacter(
					expectedCharacter, 
					expectedMaxResults);
		
		// VERIFY
		Assert.assertEquals(
				"Number of results should match.",
				2,
				result.length);
		Assert.assertEquals(
				"First result should match.",
				source2Results[0],
				result[0]);
		Assert.assertEquals(
				"Second result should match.",
				source1Results[0],
				result[1]);
		EasyMock.verify(source1);
		EasyMock.verify(source2);
	}
	
	@Test
	public void testDuplicates() throws Exception {
		// SETUP
		List<DictionaryDao> sources = new ArrayList<DictionaryDao>();
		DictionaryDao source1 = EasyMock.createMock(DictionaryDao.class);
		sources.add(source1);
		DictionaryDao source2 = EasyMock.createMock(DictionaryDao.class);
		sources.add(source2);
		
		DictionaryDaoAggregator aggregator = new DictionaryDaoAggregator();
		aggregator.setSources(sources);
		
		String expectedCharacter = "x%";
		int expectedMaxResults = 100;
		
		EntryValue[] source1Results = new EntryValue[1];
		source1Results[0] = new EntryValue();
		source1Results[0].setTraditionalChars("xz");
		source1Results[0].setPinyin("x1");
		source1Results[0].setDefinition("definition 1");
		EntryValue[] source2Results = new EntryValue[1];
		source2Results[0] = new EntryValue();
		source2Results[0].setTraditionalChars("xz");
		source2Results[0].setPinyin("x1");
		source2Results[0].setDefinition("definition 2");
		
		// EXPECT
		EasyMock.expect(
				source1.findAllEntryValuesByTraditionalCharacter(
						expectedCharacter,
						expectedMaxResults)).andReturn(source1Results);
		EasyMock.replay(source1);
		EasyMock.expect(
				source2.findAllEntryValuesByTraditionalCharacter(
						expectedCharacter,
						expectedMaxResults)).andReturn(source2Results);
		EasyMock.replay(source2);
		
		// EXEC
		EntryValue[] result = 
			aggregator.findAllEntryValuesByTraditionalCharacter(
					expectedCharacter, 
					expectedMaxResults);
		
		// VERIFY
		Assert.assertEquals(
				"Number of results should match.",
				1,
				result.length);
		Assert.assertEquals(
				"Result should match.",
				source1Results[0],
				result[0]);
		EasyMock.verify(source1);
		EasyMock.verify(source2);
	}
	
	@Test
	public void testDuplicatesDifferentCase() throws Exception {
		// SETUP
		List<DictionaryDao> sources = new ArrayList<DictionaryDao>();
		DictionaryDao source1 = EasyMock.createMock(DictionaryDao.class);
		sources.add(source1);
		DictionaryDao source2 = EasyMock.createMock(DictionaryDao.class);
		sources.add(source2);
		
		DictionaryDaoAggregator aggregator = new DictionaryDaoAggregator();
		aggregator.setSources(sources);
		
		String expectedCharacter = "x%";
		int expectedMaxResults = 100;
		
		EntryValue[] source1Results = new EntryValue[1];
		source1Results[0] = new EntryValue();
		source1Results[0].setTraditionalChars("xz");
		source1Results[0].setPinyin("x1");
		source1Results[0].setDefinition("definition 1");
		EntryValue[] source2Results = new EntryValue[1];
		source2Results[0] = new EntryValue();
		source2Results[0].setTraditionalChars("xz");
		source2Results[0].setPinyin("X1");
		source2Results[0].setDefinition("definition 2");
		
		// EXPECT
		EasyMock.expect(
				source1.findAllEntryValuesByTraditionalCharacter(
						expectedCharacter,
						expectedMaxResults)).andReturn(source1Results);
		EasyMock.replay(source1);
		EasyMock.expect(
				source2.findAllEntryValuesByTraditionalCharacter(
						expectedCharacter,
						expectedMaxResults)).andReturn(source2Results);
		EasyMock.replay(source2);
		
		// EXEC
		EntryValue[] result = 
			aggregator.findAllEntryValuesByTraditionalCharacter(
					expectedCharacter, 
					expectedMaxResults);
		
		// VERIFY
		Assert.assertEquals(
				"Number of results should match.",
				1,
				result.length);
		Assert.assertEquals(
				"Result should match.",
				source1Results[0],
				result[0]);
		EasyMock.verify(source1);
		EasyMock.verify(source2);
	}
}
