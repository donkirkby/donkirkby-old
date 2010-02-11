package com.google.code.donkirkby;

public interface RankReader {

	public String nextItem();

	public void open();

	public void close();

	public boolean hasNext();

}