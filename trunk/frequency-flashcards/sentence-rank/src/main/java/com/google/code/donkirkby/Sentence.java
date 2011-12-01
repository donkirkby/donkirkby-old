package com.google.code.donkirkby;

public class Sentence implements Comparable<Sentence>
{
	private String text;
	private int rank;
	private String id;
	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public int getRank() {
		return rank;
	}
	public void setRank(int rank) {
		this.rank = rank;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	@Override
	public int compareTo(Sentence o) {
		return rank - o.rank;
	}
}
