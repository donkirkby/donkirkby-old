package com.google.code.donkirkby;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class RankFinder {
	private static Log log = LogFactory.getLog(RankFinder.class);
	
	private int maxCharacters;
	private RankReader characterReader;
	private RankReader wordReader;
	private Map<String, Integer> characterRanks = new HashMap<String, Integer>();
	private Map<String, Integer> wordRanks = new HashMap<String, Integer>();

	public RankReader getCharacterReader() {
		return characterReader;
	}

	public void setCharacterReader(RankReader characterReader) {
		this.characterReader = characterReader;
	}

	public RankReader getWordReader() {
		return wordReader;
	}

	public void setWordReader(RankReader wordReader) {
		this.wordReader = wordReader;
	}

	public int getCharacterRank(String character) {
		Integer rank = characterRanks.get(character);
		return rank != null ? rank : maxCharacters + 1;
	}

	public int[] getRanks(String text, CharacterClassifier classifier) {
		int[] ranks = new int[text.length()];
		for (int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);
			ranks[i] = 
					classifier.isChinese(c)
					? getCharacterRank(text.substring(i, i+1))
					: 0;
		}
		return ranks;
	}

	public void load() {
		if (characterRanks.size() > 0 || wordRanks.size() > 0)
		{
			// already loaded.
			return;
		}
		
		characterReader.open();
		try
		{
			for (int rank = 1; rank <= maxCharacters; rank++)
			{
				String character = characterReader.nextItem();
				characterRanks.put(character, rank);
			}
		}finally
		{
			characterReader.close();
		}
		
		wordReader.open();
		try
		{
			int rank = 1;
			int unknownStreak = 0;
			while (wordReader.hasNext() && unknownStreak < 10)
			{
				String word = wordReader.nextItem();
				boolean hasUnknownCharacter = false;
				for (int i = 0; i < word.length() && !hasUnknownCharacter; i++)
				{
					String character = word.substring(i, i+1);
					hasUnknownCharacter = !containsCharacter(character);
				}
				if (!hasUnknownCharacter) {
					wordRanks.put(word, rank);
					rank++;
					unknownStreak = 0;
				}else
				{
					unknownStreak++;
				}
			}
		}finally
		{
			wordReader.close();
		}
		
		if (log.isInfoEnabled()) {
			log.info(String.format(
					"Loaded %1$d characters and %2$d words.",
					characterRanks.size(), 
					wordRanks.size()));
		}
	}

	public boolean containsCharacter(String character) {
		return characterRanks.containsKey(character);
	}

	public int getMaxCharacters() {
		return maxCharacters;
	}

	public void setMaxCharacters(int maxCharacters) {
		this.maxCharacters = maxCharacters;
	}

	public int getWordRank(String word) {
		return wordRanks.get(word);
	}

	public boolean containsWord(String word) {
		return wordRanks.containsKey(word);
	}

	public int maxRank(String text, CharacterClassifier classifier) {
		int maxRank = 0;
		for(char c: text.toCharArray())
		{
			if (classifier.isChinese(c))
			{
				int rank = getCharacterRank(String.valueOf(c));
	    		if (rank > maxRank)
	    		{
	    			maxRank = rank;
	    		}
			}
			else if (classifier.isJapanese(c))
			{
				maxRank = Integer.MAX_VALUE;
			}
		}
		return maxRank;
	}

}
