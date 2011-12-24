package com.google.code.donkirkby;

import java.io.BufferedReader;
import java.io.StringReader;

import junit.framework.Assert;

import org.junit.Test;

public class ImageTest {
	@Test
	public void load() throws Exception
	{
		// SETUP
		BufferedReader reader = new BufferedReader(new StringReader(
				"P2 3 3 100 0 10 20 1 11 21 2 12 22"));
		Image image = new Image();
		
		// EXEC
		image.load(reader);
		reader.close();
		double pixel00 = image.getPixel(0, 0);
		double pixel21 = image.getPixel(2, 1);
		
		// VERIFY
		Assert.assertEquals("pixel 0, 0", 0, pixel00, 0.001);
		Assert.assertEquals("pixel 2, 2", 0.21, pixel21, 0.001);
	}
	
	@Test
	public void loadWithComment() throws Exception
	{
		// SETUP
		BufferedReader reader = new BufferedReader(new StringReader(
				"P2\n#ignore this\n3 3 100 0 10 20 1 11 21 2 12 22"));
		Image image = new Image();
		
		// EXEC
		image.load(reader);
		reader.close();
		double pixel00 = image.getPixel(0, 0);
		double pixel21 = image.getPixel(2, 1);
		
		// VERIFY
		Assert.assertEquals("pixel 0, 0", 0, pixel00, 0.001);
		Assert.assertEquals("pixel 2, 2", 0.21, pixel21, 0.001);
	}
	
	@Test
	public void getCell() throws Exception
	{
		// SETUP
		BufferedReader reader = new BufferedReader(new StringReader(
				"P2 3 3 100  100 100 0 100 100 0 100 100 0"));
		Image image = new Image();
		
		// EXEC
		image.load(reader);
		reader.close();
		double cell0022 = image.getCell(0, 0, 2, 2);
		double cell1133 = image.getCell(1, 1, 3, 3);
		
		// VERIFY
		Assert.assertEquals("pixel 0, 0", 1.0, cell0022, 0.001);
		Assert.assertEquals("pixel 2, 2", 0.5, cell1133, 0.001);
	}
}
