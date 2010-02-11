package com.google.code.donkirkby;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SampleBuilder {
	private RankFinder rankFinder;

	public EntryValue[] build(
			EntryValue[] characterEntries,
			EntryValue[] wordEntries) {
		int numSamplesFound = 0;
		int characterRank = rankFinder.getCharacterRank(
				characterEntries[0].getTraditionalChars());
		List<EntryValue> knownCharacterEntries = new ArrayList<EntryValue>();
		for (EntryValue characterEntry : characterEntries) {
			characterEntry.setRank(characterRank);
			for (EntryValue wordEntry : wordEntries) {
				String word = wordEntry.getTraditionalChars();
				if (rankFinder.containsWord(word)) {
					boolean isMatchingPinyin = 
						isMatchingPinyin(characterEntry, wordEntry);
					int wordRank = rankFinder.getWordRank(word);
					wordEntry.setRank(wordRank);
					EntryValue currentSample = characterEntry.getSample();
					boolean isBest = isMatchingPinyin
						&& (currentSample == null 
								|| wordRank < currentSample.getRank());
					if (isBest)
					{
						characterEntry.setSample(wordEntry);
					}
				}
			}
			knownCharacterEntries.add(characterEntry);
			if (characterEntry.getSample() != null) {
				numSamplesFound++;
			}
		}
		if (numSamplesFound > 0)
		{
			for (int i=knownCharacterEntries.size()-1; i >= 0; i--) {
				if (knownCharacterEntries.get(i).getSample() == null) {
					knownCharacterEntries.remove(i);
				}
			}
		}
		Collections.sort(
				knownCharacterEntries, 
				new Comparator<EntryValue>() {
					@Override
					public int compare(EntryValue entry1, EntryValue entry2) {
						EntryValue sample1 = entry1.getSample();
						EntryValue sample2 = entry2.getSample();
						boolean hasSample1 = sample1 != null;
						boolean hasSample2 = sample2 != null;
						if (!hasSample1 && !hasSample2)
						{
							return 0;
						}
						if (!hasSample2)
						{
							return -1;
						}
						if (!hasSample1)
						{
							return 1;
						}
						return sample1.getRank() - sample2.getRank();
					}
				});
		EntryValue[] result = new EntryValue[knownCharacterEntries.size()];
		return knownCharacterEntries.toArray(result);
	}

	private boolean isMatchingPinyin(EntryValue characterEntry,
			EntryValue wordEntry) {
		char character = characterEntry.getTraditionalChars().charAt(0);
		char[] characters = wordEntry.getTraditionalChars().toCharArray();
		String[] wordPinyins = wordEntry.getPinyin().split(" ");
		for (int i = 0; i < characters.length; i++) {
			char c = characters[i];
			if (c == character)
			{
				if (i < wordPinyins.length)
				{
					return wordPinyins[i].equalsIgnoreCase(
							characterEntry.getPinyin());
				}else
				{
					// special case for er as in zher.
					return true;
				}
			}
		}
		return false;
	}

	public RankFinder getRankFinder() {
		return rankFinder;
	}

	public void setRankFinder(RankFinder rankFinder) {
		this.rankFinder = rankFinder;
	}

	public EntryValue[] filterWords(
			EntryValue[] wordEntries,
			int maxCharacterRank) {
		List<EntryValue> filteredEntries = new ArrayList<EntryValue>();
		for (EntryValue wordEntry : wordEntries) {
			String traditional = wordEntry.getTraditionalChars();
			boolean isRare = !rankFinder.containsWord(traditional);
			for (int i = 0; i < traditional.length() && !isRare; i++)
			{
				String character = traditional.substring(i, i+1);
				int characterRank = rankFinder.getCharacterRank(character);
				isRare = characterRank > maxCharacterRank;
			}
			if (!isRare)
			{
				filteredEntries.add(wordEntry);
			}
		}
		EntryValue[] result = new EntryValue[filteredEntries.size()];
		return filteredEntries.toArray(result);
	}
	
}
