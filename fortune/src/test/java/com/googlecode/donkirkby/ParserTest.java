package com.googlecode.donkirkby;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;


public class ParserTest
{
	@Test
	public void parse()
	{
		// SETUP
		String input = "abc\n%\ndef\n%\nghi";
		Parser p = new Parser();
		
		// EXEC
		List<String> messages = p.parse(input);
		
		// VERIFY
		Assert.assertEquals("size", 3, messages.size());
		Assert.assertEquals("first message", "abc", messages.get(0));
		Assert.assertEquals("second message", "def", messages.get(1));
	}

	@Test
	public void multiline()
	{
		// SETUP
		String input = "abc\n%\ndef\nghi";
		Parser p = new Parser();
		
		// EXEC
		List<String> messages = p.parse(input);
		
		// VERIFY
		Assert.assertEquals("size", 2, messages.size());
		Assert.assertEquals("first message", "abc", messages.get(0));
		Assert.assertEquals("second message", "def\r\nghi", messages.get(1));
	}
	
	@Test
	public void whiteSpace()
	{
		// SETUP
		String input = "abc\n   % \t \ndef";
		Parser p = new Parser();
		
		// EXEC
		List<String> messages = p.parse(input);
		
		// VERIFY
		Assert.assertEquals("size", 2, messages.size());
		Assert.assertEquals("first message", "abc", messages.get(0));
		Assert.assertEquals("second message", "def", messages.get(1));
	}
}
