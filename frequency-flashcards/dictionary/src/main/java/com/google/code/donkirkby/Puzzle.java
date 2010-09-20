package com.google.code.donkirkby;

import java.util.ArrayList;

public class Puzzle {
	private ArrayList<String> wordChain = new ArrayList<String>();
	
	public int size() {
		return wordChain.size();
	}
	
	public Puzzle addWord(String word) {
		Puzzle copy = copy();
		copy.wordChain.add(word);
		return copy;
	}
	
	public Puzzle addPuzzle(Puzzle puzzle) {
		Puzzle copy = copy();
		for (int i = 1; i < puzzle.size(); i++) {
			copy.wordChain.add(puzzle.getWord(i));
		}
		
		return copy;
	}
	
	public String getStart() {
		return wordChain.get(0);
	}
	
	public String getEnd() {
		return wordChain.get(wordChain.size() - 1);
	}
	
	public String getWord(int i) {
		return wordChain.get(i);
	}
	
	public Puzzle copy() {
		Puzzle copy = new Puzzle();
		copy.wordChain.addAll(wordChain);
		return copy;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (String word : wordChain) {
			if (sb.length() > 0)
			{
				sb.append("-");
			}
			sb.append(word);
		}
		return sb.toString();
	}
}
