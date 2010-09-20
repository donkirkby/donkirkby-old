package com.google.code.donkirkby;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;

public class WordConnectorTest {
	@Test
	public void AddWord(){
		// SETUP
		WordConnector c = new WordConnector();
		
		// EXEC
		c.addWord("AB");
		c.addWord("BC");
		c.addWord("CD");
		
		ArrayList<Puzzle> puzzles = c.generatePuzzles();
		
		// VERIFY
		Assert.assertEquals(
				"puzzle count",
				1,
				puzzles.size());
		Assert.assertEquals(
				"puzzle",
				"AB-BC-CD",
				puzzles.get(0).toString());
		
	}
}
