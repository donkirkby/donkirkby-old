package com.google.code.donkirkby;

import java.util.Iterator;

public class UnfilteredWordList implements WordListInterface {
	private RankReader wordReader;
	
	@Override
	public Iterator<String> iterator(int maxCharacterRank) {
		return new Iterator<String>() {
		
			@Override
			public void remove() {
			}
		
			@Override
			public String next() {
				return wordReader.nextItem();
			}
		
			@Override
			public boolean hasNext() {
				return wordReader.hasNext();
			}
		
		};
	}

	public RankReader getWordReader() {
		return wordReader;
	}

	public void setWordReader(RankReader wordReader) {
		this.wordReader = wordReader;
		wordReader.open();
	}

}
