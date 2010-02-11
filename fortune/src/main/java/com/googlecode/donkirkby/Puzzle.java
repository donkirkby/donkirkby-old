package com.googlecode.donkirkby;

import java.util.ArrayList;
import java.util.List;

public class Puzzle
{
	private List<String> triples;
	private String blanks;
	
	public Puzzle(String message)
	{
		triples = new ArrayList<String>();
		char triple[] = new char[3];
		char blankReplacements[] = {'a', 'b', 'c'};
		blanks = "";
		int tripleIndex = 0;
		for (int messageIndex = 0; messageIndex < message.length(); messageIndex++)
		{
			char c = message.charAt(messageIndex);
			if ( ! Character.isLetter(c))
			{
				blanks += c;
			}
			else
			{
				triple[tripleIndex] = Character.toUpperCase(c);
				if (Character.isUpperCase(c))
				{
					blanks += 
						Character.toUpperCase(blankReplacements[tripleIndex]);
				} 
				else
				{
					blanks += blankReplacements[tripleIndex];
				}
				tripleIndex++;
				if (tripleIndex == 3)
				{
					triples.add(new String(triple));
					tripleIndex = 0;
				}
			}
		}
		if (tripleIndex != 0)
		{
			// Didn't come out evenly, so don't use it.
			triples = null;
			blanks = null;
		}
	}

	public List<String> getTriples()
	{
		return triples;
	}

	public String getBlanks()
	{
		return blanks;
	}

}
