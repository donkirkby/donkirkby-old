package com.google.code.donkirkby;

import java.io.StringWriter;

import org.junit.Assert;
import org.junit.Test;

public class DeckWriterTest {
	@Test
	public void testWriteHeaderFooter() throws Exception {
		// SETUP
		StringWriter stringWriter = new StringWriter();
		DeckWriter writer = new DeckWriter();
		writer.setWriter(stringWriter);
		
		// EXEC
		writer.writeHeader();
		writer.writeFooter();
		
		// VERIFY
		Assert.assertEquals(
				"Text should match",
				"<?xml version='1.0' encoding='UTF-8'?>\r\n" +
				"<mnemosyne core_version='1'>\r\n" +
				"</mnemosyne>\r\n",
				stringWriter.toString());
	}
	
	@Test
	public void testCleanCards() throws Exception {
		// SETUP
		StringWriter stringWriter = new StringWriter();
		DeckWriter writer = new DeckWriter();
		writer.setWriter(stringWriter);
		
		// EXEC
		writer.writeCard(
				"Provincial Capitals",
				"Ontario",
				"Toronto");
		writer.writeCard(
				"Provincial Capitals",
				"Quebec",
				"Quebec City");
		
		// VERIFY
		Assert.assertEquals(
				"Text should match",
				"<item id='_0'>\r\n" +
				"<cat>Provincial Capitals</cat>\r\n" +
				"<Q>Ontario</Q>\r\n" +
				"<A>Toronto</A>\r\n" +
				"</item>\r\n" +
				"<item id='_1'>\r\n" +
				"<cat>Provincial Capitals</cat>\r\n" +
				"<Q>Quebec</Q>\r\n" +
				"<A>Quebec City</A>\r\n" +
				"</item>\r\n",
				stringWriter.toString());
	}
	
	@Test
	public void testSpecialCharacters() throws Exception {
		// SETUP
		StringWriter stringWriter = new StringWriter();
		DeckWriter writer = new DeckWriter();
		writer.setWriter(stringWriter);
		
		// EXEC
		writer.writeCard(
				"Provincial Capitals",
				"Labrador & Newfoundland",
				"St. John's <em>not St. John</em>");
		
		// VERIFY
		Assert.assertEquals(
				"Text should match",
				"<item id='_0'>\r\n" +
				"<cat>Provincial Capitals</cat>\r\n" +
				"<Q>Labrador &amp; Newfoundland</Q>\r\n" +
				"<A>St. John's &lt;em&gt;not St. John&lt;/em&gt;</A>\r\n" +
				"</item>\r\n",
				stringWriter.toString());
	}
}
