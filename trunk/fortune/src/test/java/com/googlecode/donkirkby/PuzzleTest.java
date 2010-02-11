package com.googlecode.donkirkby;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;


public class PuzzleTest
{
	@Test
	public void getOneTriple()
	{
		// SETUP
		Puzzle puzzle = new Puzzle("abc");
		
		// EXEC
		List<String> triples = puzzle.getTriples();
		
		// VERIFY
		Assert.assertEquals("triples size", 1, triples.size());
		Assert.assertEquals("first triple", "ABC", triples.get(0));
	}

	@Test
	public void getTwoTriples()
	{
		// SETUP
		Puzzle puzzle = new Puzzle("abCDef");
		
		// EXEC
		List<String> triples = puzzle.getTriples();
		
		// VERIFY
		Assert.assertEquals("triples size", 2, triples.size());
		Assert.assertEquals("second triple", "DEF", triples.get(1));
	}
	
	@Test
	public void ignoreNonalpha()
	{
		// SETUP
		Puzzle puzzle = new Puzzle("a b-CD, e1f");
		
		// EXEC
		List<String> triples = puzzle.getTriples();
		
		// VERIFY
		Assert.assertEquals("triples size", 2, triples.size());
		Assert.assertEquals("first triple", "ABC", triples.get(0));
		Assert.assertEquals("second triple", "DEF", triples.get(1));
	}
	
	@Test
	public void blanks()
	{
		// SETUP
		Puzzle puzzle = new Puzzle("Baby, it's me!");
		
		// EXEC
		String blanks = puzzle.getBlanks();
		
		// VERIFY
		Assert.assertEquals("blanks", "Abca, bc'a bc!", blanks);
	}
	
	@Test
	public void notAMultipleOfThree()
	{
		// SETUP
		Puzzle puzzle = new Puzzle("a b-CD, e1fx");
		
		// EXEC
		List<String> triples = puzzle.getTriples();
		String blanks = puzzle.getBlanks();
		
		// VERIFY
		Assert.assertEquals("triples", null, triples);
		Assert.assertEquals("blanks", null, blanks);
	}
	
}
