package com.google.code.donkirkby;

import java.nio.charset.Charset;
import java.util.Map;

import org.junit.Test;

public class CharsetTest {
	@Test
	public void testCharsets() throws Exception {
		Map<String,Charset> charsets = Charset.availableCharsets();
		
		for (String charsetName : charsets.keySet()) {
			System.out.println(charsetName);
		}
	}
}
