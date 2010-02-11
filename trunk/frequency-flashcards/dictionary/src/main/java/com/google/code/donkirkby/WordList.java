package com.google.code.donkirkby;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class WordList implements WordListInterface {
	private RankFinder rankFinder;
	private RankReader wordReader;
	private class Entry {
		public String word;
		public int maxCharacterRank;
	}
	private List<Entry> entries = new LinkedList<Entry>();

	public RankFinder getRankFinder() {
		return rankFinder;
	}

	public void setRankFinder(RankFinder rankFinder) {
		this.rankFinder = rankFinder;
		rankFinder.load();
	}

	public RankReader getWordReader() {
		return wordReader;
	}

	public void setWordReader(RankReader wordReader) {
		this.wordReader = wordReader;
		wordReader.open();
	}

	/* (non-Javadoc)
	 * @see com.google.code.donkirkby.WordListInterface#iterator(int)
	 */
	public Iterator<String> iterator(final int maxCharacterRank) {
		final ListIterator<Entry> listIterator = entries.listIterator();
		Iterator<String> itr = new Iterator<String>() {
		
			/* (non-Javadoc)
			 * @see java.util.Iterator#remove()
			 */
			@Override
			public void remove() {
				listIterator.remove();
			}
		
			/* (non-Javadoc)
			 * @see java.util.Iterator#next()
			 */
			@Override
			public String next() {
				hasNext();
				String result = listIterator.next().word;
				return result;
			}
		
			/* (non-Javadoc)
			 * @see java.util.Iterator#hasNext()
			 */
			@Override
			public boolean hasNext() {
				while (listIterator.hasNext())
				{
					Entry entry = listIterator.next();
					if (entry.maxCharacterRank <= maxCharacterRank)
					{
						listIterator.previous();
						return true;
					}
				}
				while (!listIterator.hasNext())
				{
					if (!wordReader.hasNext())
					{
						return false;
					}
					Entry entry = new Entry();
					entry.word = wordReader.nextItem();
					entry.maxCharacterRank = 0;
					for (int i = 0; i < entry.word.length(); i++) {
						String character = entry.word.substring(i, i+1);
						try {
							entry.maxCharacterRank = Math.max(
									entry.maxCharacterRank,
									rankFinder.getCharacterRank(character));
						} catch (RuntimeException e) {
							String msg = 
								"Can't find rank of '" + character + "'.";
							throw new RuntimeException(msg, e);
						}
					}
					listIterator.add(entry);
					if (entry.maxCharacterRank <= maxCharacterRank)
					{
						listIterator.previous();
					}
				}
				return true;
			}
		
		};
		return itr;
	}
}
