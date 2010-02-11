package com.google.code.donkirkby;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class RankReaderTest {
	@Test
	public void testNext() throws Exception {
		// SETUP
		Resource characterResource = 
			new ClassPathResource("/character_frequency_utf8.txt");
		DefaultRankReader reader = new DefaultRankReader();
		reader.setResource(characterResource);
		reader.setHeaderLineCount(8);
		
		// EXEC
		String item1;
		String item2;
		reader.open();
		try
		{
			item1 = reader.nextItem();
			item2 = reader.nextItem();
		}finally
		{
			reader.close();
		}
		
		// VERIFY
		Assert.assertEquals(
				"Item should match",
				"的",
				item1);
		Assert.assertEquals(
				"Item should match",
				"是",
				item2);
	}
	
	@Test
	public void testParsingTokensOn() throws Exception {
		// SETUP
		String input = "AB\nCD\n";
		Resource characterResource = 
			new ByteArrayResource(input.getBytes());
		DefaultRankReader reader = new DefaultRankReader();
		reader.setResource(characterResource);
		// defaults to parsing tokens
		
		// EXEC
		String item1;
		String item2;
		reader.open();
		try
		{
			item1 = reader.nextItem();
			item2 = reader.nextItem();
		}finally
		{
			reader.close();
		}
		
		// VERIFY
		Assert.assertEquals(
				"Item should match",
				"AB",
				item1);
		Assert.assertEquals(
				"Item should match",
				"CD",
				item2);
	}
	
	@Test
	public void testParsingTokensOff() throws Exception {
		// SETUP
		String input = "AB\nCD\n";
		Resource characterResource = 
			new ByteArrayResource(input.getBytes());
		DefaultRankReader reader = new DefaultRankReader();
		reader.setResource(characterResource);
		reader.setParsingTokens(false);
		
		// EXEC
		String item1;
		String item2;
		reader.open();
		try
		{
			item1 = reader.nextItem();
			item2 = reader.nextItem();
		}finally
		{
			reader.close();
		}
		
		// VERIFY
		Assert.assertEquals(
				"Item should match",
				"A",
				item1);
		Assert.assertEquals(
				"Item should match",
				"C",
				item2);
	}
	
	@Test
	public void testOnePerLineOn() throws Exception {
		// SETUP
		String input = "AB CD\nEF\n";
		Resource characterResource = 
			new ByteArrayResource(input.getBytes());
		DefaultRankReader reader = new DefaultRankReader();
		reader.setResource(characterResource);
		// defaults to one per line.
		
		// EXEC
		String item1;
		String item2;
		reader.open();
		try
		{
			item1 = reader.nextItem();
			item2 = reader.nextItem();
		}finally
		{
			reader.close();
		}
		
		// VERIFY
		Assert.assertEquals(
				"Item should match",
				"AB",
				item1);
		Assert.assertEquals(
				"Item should match",
				"EF",
				item2);
	}
	
	@Test
	public void testLastLineFeedMissing() throws Exception {
		// SETUP
		String input = "AB CD\nEF";
		Resource characterResource = 
			new ByteArrayResource(input.getBytes());
		DefaultRankReader reader = new DefaultRankReader();
		reader.setResource(characterResource);
		// defaults to one per line.
		
		// EXEC
		String item1;
		String item2;
		reader.open();
		try
		{
			item1 = reader.nextItem();
			item2 = reader.nextItem();
		}finally
		{
			reader.close();
		}
		
		// VERIFY
		Assert.assertEquals(
				"Item should match",
				"AB",
				item1);
		Assert.assertEquals(
				"Item should match",
				"EF",
				item2);
	}
	
	@Test
	public void testSkipHeader() throws Exception {
		// SETUP
		String input = "AB CD\nEF\nGH\nIJ\n";
		Resource characterResource = 
			new ByteArrayResource(input.getBytes());
		DefaultRankReader reader = new DefaultRankReader();
		reader.setResource(characterResource);
		reader.setHeaderLineCount(3);
		// defaults to one per line.
		
		// EXEC
		String item1;
		reader.open();
		try
		{
			item1 = reader.nextItem();
		}finally
		{
			reader.close();
		}
		
		// VERIFY
		Assert.assertEquals(
				"Item should match",
				"IJ",
				item1);
	}
	
	@Test
	public void testNothingAfterHeaderAndMissingLastLinefeed() throws Exception {
		// SETUP
		String input = "AB CD\nEF\nGH IJ";
		Resource characterResource = 
			new ByteArrayResource(input.getBytes());
		DefaultRankReader reader = new DefaultRankReader();
		reader.setResource(characterResource);
		reader.setHeaderLineCount(3);
		// defaults to one per line.
		
		// EXEC
		boolean hasNext;
		reader.open();
		try
		{
			hasNext = reader.hasNext();
		}finally
		{
			reader.close();
		}
		
		// VERIFY
		Assert.assertEquals(
				"Item should match",
				false,
				hasNext);
	}
	
	@Test
	public void testOnePerLineOff() throws Exception {
		// SETUP
		String input = "AB CD\nEF\nGH\n";
		Resource characterResource = 
			new ByteArrayResource(input.getBytes());
		DefaultRankReader reader = new DefaultRankReader();
		reader.setResource(characterResource);
		reader.setOnePerLine(false);
		
		// EXEC
		String item1;
		String item2;
		String item3;
		reader.open();
		try
		{
			item1 = reader.nextItem();
			item2 = reader.nextItem();
			item3 = reader.nextItem();
		}finally
		{
			reader.close();
		}
		
		// VERIFY
		Assert.assertEquals(
				"Item should match",
				"AB",
				item1);
		Assert.assertEquals(
				"Item should match",
				"CD",
				item2);
		Assert.assertEquals(
				"Item should match",
				"EF",
				item3);
	}
	
	@Test
	public void testMinTokenLength() throws Exception {
		// SETUP
		String input = "AB C\nEF\nGH\n";
		Resource characterResource = 
			new ByteArrayResource(input.getBytes());
		DefaultRankReader reader = new DefaultRankReader();
		reader.setResource(characterResource);
		reader.setOnePerLine(false);
		reader.setMinTokenLength(2);
		
		// EXEC
		String item1;
		String item2;
		reader.open();
		try
		{
			item1 = reader.nextItem();
			item2 = reader.nextItem();
		}finally
		{
			reader.close();
		}
		
		// VERIFY
		Assert.assertEquals(
				"Item 1 should match",
				"AB",
				item1);
		Assert.assertEquals(
				"Item 2 should match",
				"EF",
				item2);
	}
}
