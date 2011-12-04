package com.google.code.donkirkby;

public class Sentence implements Comparable<Sentence>
{
	private String text;
	private Sentence translation;
	private int rank;
	private int id;
	
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
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@Override
	public int compareTo(Sentence o) {
		return rank - o.rank;
	}
	public Sentence getTranslation() {
		return translation;
	}
	public void setTranslation(Sentence translation) {
		this.translation = translation;
	}
}
