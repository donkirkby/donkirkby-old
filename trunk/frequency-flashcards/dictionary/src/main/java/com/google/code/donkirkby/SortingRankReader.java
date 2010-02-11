package com.google.code.donkirkby;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class SortingRankReader implements RankReader {
	private RankReader rankReader;
	private RankFinder rankFinder;
	private List<String> items;
	private Iterator<String> iterator;
	private String currentItem;

	@Override
	public void close() {
		items = null;
		iterator = null;
	}

	@Override
	public boolean hasNext() {
		return iterator.hasNext();
	}

	@Override
	public String nextItem() {
		String nextItem;
		do
		{
			nextItem = iterator.next();
		}while (currentItem != null && currentItem.equals(nextItem) && iterator.hasNext());
		currentItem = nextItem;
		return nextItem;
	}

	@Override
	public void open() {
		items = new ArrayList<String>();
		rankReader.open();
		try
		{
			while (rankReader.hasNext())
			{
				items.add(rankReader.nextItem());
			}
		}finally
		{
			rankReader.close();
		}

		Comparator<String> comparator = new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				return rankFinder.getCharacterRank(o1) - rankFinder.getCharacterRank(o2);
			}
		};
		Collections.sort(items, comparator);
		iterator = items.iterator();
	}

	public RankReader getRankReader() {
		return rankReader;
	}

	public void setRankReader(RankReader rankReader) {
		this.rankReader = rankReader;
	}

	public RankFinder getRankFinder() {
		return rankFinder;
	}

	public void setRankFinder(RankFinder rankFinder) {
		this.rankFinder = rankFinder;
	}
}
