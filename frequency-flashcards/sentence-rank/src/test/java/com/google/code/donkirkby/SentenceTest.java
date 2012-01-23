package com.google.code.donkirkby;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class SentenceTest 
{
	@Test
    public void maxRank()
    {
    	// SETUP
    	Sentence sentence1 = new Sentence();
    	Sentence sentence2 = new Sentence();
    	sentence1.setRanks(new int[] {1, 2, 5});
    	sentence2.setRanks(new int[] {1, 2, 3});
    	
    	// EXEC
    	int diff = sentence1.compareTo(sentence2);
    	
    	// VERIFY
    	Assert.assertEquals(
    			"sign of sentence1 - sentence2",
    			1,
    			Integer.signum(diff));
    }

	/** If the maximum ranks match, longer sentences come first. **/
	@Test
    public void length()
    {
    	// SETUP
    	Sentence sentence1 = new Sentence();
    	Sentence sentence2 = new Sentence();
    	sentence1.setRanks(new int[] {1, 2, 5});
    	sentence2.setRanks(new int[] {1, 2, 5, 3});
    	
    	// EXEC
    	int diff = sentence1.compareTo(sentence2);
    	
    	// VERIFY
    	Assert.assertEquals(
    			"sign of sentence1 - sentence2",
    			1,
    			Integer.signum(diff));
    }

	/** If the maximum ranks match, longer sentences come first. **/
	@Test
    public void sort()
    {
    	// SETUP
    	Sentence sentence1 = new Sentence();
    	Sentence sentence2 = new Sentence();
    	sentence1.setRanks(new int[] {1, 2, 5});
    	sentence2.setRanks(new int[] {1, 2, 5, 3});
    	Sentence[] expected = new Sentence[] {sentence2, sentence1};
    	Sentence[] a = new Sentence[] {sentence1, sentence2};
    	
    	// EXEC
    	Arrays.sort(a);
    	
    	// VERIFY
    	Assert.assertArrayEquals(
    			"sorted array",
    			expected,
    			a);
    }

	/** If the maximum ranks and lengths match, higher ranks on other chars
	 * come first. 
	 */
	@Test
    public void lowerRanks()
    {
    	// SETUP
    	Sentence sentence1 = new Sentence();
    	Sentence sentence2 = new Sentence();
    	sentence1.setRanks(new int[] {1, 2, 5});
    	sentence2.setRanks(new int[] {1, 3, 5});
    	
    	// EXEC
    	int diff = sentence1.compareTo(sentence2);
    	
    	// VERIFY
    	Assert.assertEquals(
    			"sign of sentence1 - sentence2",
    			1,
    			Integer.signum(diff));
    }
}
