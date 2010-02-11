package com.google.code.donkirkby;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class MockRankReader extends DefaultRankReader {
	private List<String> items = new ArrayList<String>();
	private Iterator<String> iterator;
	
	public MockRankReader(String... items) {
		addItems(items);
	}
	
	@Override
	public String nextItem() {
		return iterator.next();
	}
	
	@Override
	public boolean hasNext() {
		return iterator.hasNext();
	}
	
	@Override
	public void open() {
		iterator = items.iterator();
	}
	
	@Override
	public void close() {
		iterator = null;
	}
	
	public void addItems(String... items)
	{
		this.items.addAll(Arrays.asList(items));
	}
	
	public int getItemCount()
	{
		return items.size();
	}
}
