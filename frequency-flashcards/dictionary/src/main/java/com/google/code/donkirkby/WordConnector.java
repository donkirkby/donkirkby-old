package com.google.code.donkirkby;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WordConnector {
	private ArrayList<String> words = new ArrayList<String>();
	private Map<String, Map<String, Puzzle>> pathLengths = 
		new HashMap<String, Map<String, Puzzle>>();
	
	public void addWord(String word) {
		words.add(word);
	}

	public ArrayList<Puzzle> generatePuzzles() {
		ArrayList<Puzzle> puzzles = new ArrayList<Puzzle>();
		Puzzle empty = new Puzzle();
		for (String startWord : words) {
			HashMap<String, Puzzle> targets = new HashMap<String, Puzzle>();
			pathLengths.put(startWord, targets);
			for (String linkedWord : GetLinkedWords(startWord)) {
				targets.put(
						linkedWord, 
						empty.addWord(startWord).addWord(linkedWord));
			}
		}
		for (String startWord : words) {
			addAllLinkedWords(startWord);
		}
		for (String startWord : words) {
			Map<String, Puzzle> targets = pathLengths.get(startWord);
			for (Map.Entry<String, Puzzle> entry : targets.entrySet()) {
				String target = entry.getKey();
				if (entry.getValue().size() > 2 && target.compareTo(startWord) > 0) {
					puzzles.add(entry.getValue());
				}
			}
		}
		return puzzles;
	}
	
	private void addAllLinkedWords(String startWord) {
		Map<String, Puzzle> targets = pathLengths.get(startWord);
		ArrayList<String> targetWords = new ArrayList<String>();
		targetWords.addAll(targets.keySet());
		for (String target : targetWords) {
			Puzzle puzzle = targets.get(target);
			addLinkedWords(startWord, targets, target, puzzle);
		}
	}

	private void addLinkedWords(
			String startWord,
			Map<String, Puzzle> startTargets,
			String target, 
			Puzzle puzzle) {
		Map<String, Puzzle> nextTargets = pathLengths.get(target);
		for (Map.Entry<String, Puzzle> entry : nextTargets.entrySet()) {
			String nextTarget = entry.getKey();
			Puzzle addedPuzzle = entry.getValue();
			Puzzle existingPuzzle = startTargets.get(nextTarget);
			int newLength = puzzle.size() + addedPuzzle.size();
			if (existingPuzzle == null || newLength < existingPuzzle.size())
			{
				Puzzle newPuzzle = puzzle.addPuzzle(addedPuzzle);
				startTargets.put(nextTarget, newPuzzle);
				addLinkedWords(startWord, startTargets, nextTarget, newPuzzle);
			}
		}
	}

	private ArrayList<String> GetLinkedWords(String word) {
		ArrayList<String> linkedWords = new ArrayList<String>();
		for (String nextWord : words)
		{
			for (int i = 0; i < nextWord.length(); i++)
			{
				if (word.contains(nextWord.subSequence(i, i+1)))
				{
					linkedWords.add(nextWord);
					break;
				}
			}
		}
		return linkedWords;
	}
}
