package com.google.code.donkirkby;

import java.util.Arrays;

public class Sentence implements Comparable<Sentence>
{
	private String text;
	private int[] ranks;
	private int id;
	private boolean isSpoken;
	private boolean isChinese;
	
	public String getText() {
		if ( ! (isSpoken && isChinese)) {
			return text;
		}
		String result = String.format(
			"<a class='audioButton audioAvailable' " +
			"href='http://static.tatoeba.org/audio/sentences/cmn/%1$d.mp3' " +
			"title='Play audio' " +
			"onclick='return false'>Speak</a> " +
			"<span class='fade'>%2$s</span>",
			id,
			text);
		return result;
	}
	public void setText(String text) {
		this.text = text;
	}
	public int getMaxRank() {
		return ranks[0];
	}
	public int[] getRanks() {
		return ranks;
	}
	public void setRanks(int[] ranks) {
		this.ranks = ranks.clone();
		Arrays.sort(this.ranks);
		for (int left=0, right=this.ranks.length-1; left<right; left++, right--) {
		    // exchange the left and right
		    int temp = this.ranks[left];
		    this.ranks[left] = this.ranks[right];
		    this.ranks[right] = temp;
		}
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@Override
	public int compareTo(Sentence o) {
		int diff = ranks[0] - o.ranks[0];
		if (diff != 0)
		{
			return diff;
		}
		diff = o.ranks.length - ranks.length;
		if (diff != 0)
		{
			return diff;
		}
		for (int i = 0; i < ranks.length; i++) {
			int rank = ranks[i];
			int otherRank = o.ranks[i];
			if (rank != otherRank)
			{
				return otherRank - rank;
			}
		}
		return 0;
	}
	public boolean isSpoken() {
		return isSpoken;
	}
	public void setSpoken(boolean isSpoken) {
		this.isSpoken = isSpoken;
	}
	public boolean isChinese() {
		return isChinese;
	}
	public void setChinese(boolean isChinese) {
		this.isChinese = isChinese;
	}
}
