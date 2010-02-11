package com.google.code.donkirkby;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Generate flashcards for Chinese vocabulary.
 *
 */
public class DictionaryApp 
{
	private static Log log = LogFactory.getLog(DictionaryApp.class);
	
	private DictionaryDao dao;
	private RankReader characterReader;
	private RankFinder rankFinder;
	private SampleBuilder sampleBuilder;
	private WordListInterface wordList;

	private StrokeImageFacade strokeImageFacade;
	
	private String currentCharacter;
	
	private String deckNameTemplate;
	private String categoryTemplate;
	private int deckSize;
	private int wordsPerDeck = -1;
	private int characterLimit;
	
    public static void main( String[] args )
    {
		log.info("Starting.");
		DictionaryApp app = null;

		try {
			ClassPathXmlApplicationContext springContext = 
				new ClassPathXmlApplicationContext("spring.xml");
			
			app = (DictionaryApp) springContext.getBean(
					"lessonApp", 
					DictionaryApp.class);

			app.generateCards();
			log.info("Success");
		} catch (Exception e) {
			String msg = "Failure";
			if (app != null && app.currentCharacter != null) {
				msg += " on character " + app.currentCharacter;
			}
			log.error(msg, e);
			System.exit(-1);
		}
    }
    
    public void generateCards()
    {
		strokeImageFacade = new StrokeImageFacade();
		List<CharacterSummary> summaries = new ArrayList<CharacterSummary>();
		
		int numCardsInDeck = 0;
		int totalNumCards = 0;
		int totalNumCharacters = 0;
		int totalNumWords = 0;
		characterReader.open();
		try
		{
			for (int rank = 1; hasMoreCharacters(rank); rank++)
			{
				String character = characterReader.nextItem();
				currentCharacter = character;
				EntryValue[] characterEntries = null;
				try {
					characterEntries = dao.findAllEntryValuesByTraditionalCharacter(
							character, 
							1000);
				} catch (RuntimeException e) {
					log.warn(
							"Error loading definition for rank " + rank + 
							": " + character,
							e);
					continue;
				}
				if (characterEntries == null || characterEntries.length == 0)
				{
					log.warn(
							"No entries found for rank " + rank + 
							": " + character);
					continue;
				}
				String strokeOrderData =
					findStrokeOrderData(characterEntries, rank);

				EntryValue[] wordEntries;
				try {
					wordEntries = dao.findAllEntryValuesByTraditionalCharacter(
							"%" + character + "%", 
							1000);
				} catch (RuntimeException e) {
					log.warn(
							"Error loading word definitions for rank " + rank + 
							": " + character,
							e);
					continue;
				}
				characterEntries = 
					sampleBuilder.build(characterEntries, wordEntries);
				wordEntries = sampleBuilder.filterWords(
						wordEntries, 
						characterEntries[0].getRank());
				
				List<EntryValue> entryList = Arrays.asList(characterEntries);
				List<EntryValue> wordList = Arrays.asList(wordEntries);
				
				CharacterSummary summary = 
					summarize(entryList, wordList, strokeOrderData);
				summaries.add(summary);
				int numCards = countCards(summary);
				totalNumCards += numCards;
				totalNumCharacters++;
				numCardsInDeck += numCards;
				if (deckSize > 0 && (rank % deckSize == 0))
				{
					int numWordCards = writeDeck(summaries);
					totalNumWords += numWordCards;
					totalNumCards += numWordCards;
					numCardsInDeck = 0;
					summaries.clear();
				}
			}
			if (!summaries.isEmpty())
			{
				int numWordCards = writeDeck(summaries);
				totalNumWords += numWordCards;
				totalNumCards += numWordCards;
			}
		}finally
		{
			characterReader.close();
		}
		log.info(String.format(
				"Generated %1$d cards with %2$d characters and %3$d words.",
				totalNumCards,
				totalNumCharacters,
				totalNumWords));
    }

	private boolean hasMoreCharacters(int rank) {
		if (characterLimit > 0 && rank > characterLimit)
		{
			return false;
		}
		return characterReader.hasNext();
	}

	private String findStrokeOrderData(EntryValue[] characterEntries, int rank){
		Set<String> searchedCharacters = new HashSet<String>();
		for (EntryValue entryValue : characterEntries) {
			
			String simplifiedCharacter = entryValue.getSimplifiedChars();
			if (!searchedCharacters.contains(simplifiedCharacter))
			{
				searchedCharacters.add(simplifiedCharacter);
				String strokeOrderData = 
					StrokeOrderDataProvider.getStrokeOrderData(
							simplifiedCharacter);
				
				if (strokeOrderData != null) {
					return strokeOrderData;
				}
			}
		}
		
		StringBuilder msg = new StringBuilder();
		msg.append("Stroke order data not found for rank " + rank + ": ");
		for (String string : searchedCharacters) {
			msg.append(string + " ");
		}
		log.warn(msg);
		return null;
	}

	/**
	 * @param summaries
	 * @return The number of word cards written.
	 */
	private int writeDeck(List<CharacterSummary> summaries) {
		int numWordCards;
		int minRank = summaries.get(0).entry.getRank();
		int maxRank = summaries.get(summaries.size()-1).entry.getRank();
		String deckName = buildName(deckNameTemplate, minRank, maxRank, "%1$04d");
		OutputStreamWriter fileWriter;
		try {
			FileOutputStream outStream = new FileOutputStream(deckName);
			fileWriter = new OutputStreamWriter(outStream, "UTF-8");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		try
		{
			DeckWriter writer = new DeckWriter();
			writer.setWriter(fileWriter);
			writer.writeHeader();
			String baseCategory = buildName(categoryTemplate, minRank, maxRank, "%1$4d") + " - "; 
			String categoryEnglish = baseCategory + "English";
			writer.writeCategory(categoryEnglish);
			String categoryHanzi = baseCategory + "Hanzi";
			writer.writeCategory(categoryHanzi);
			String categoryTraditional = baseCategory + "Traditional";
			writer.writeCategory(categoryTraditional);
			String categoryPinyin = baseCategory + "Pinyin";
			writer.writeCategory(categoryPinyin);
			String categoryWords = baseCategory + "Words";
			writer.writeCategory(categoryWords);
			
			writeEnglishCards(summaries, writer, categoryEnglish);
			writeHanziCards(summaries, writer, categoryHanzi);
			writeTraditionalCards(summaries, writer, categoryTraditional);
			writePinyinCards(summaries, writer, categoryPinyin);
			
			numWordCards = writeWordCards(summaries, writer, categoryWords);
			
			writer.writeFooter();
		}finally
		{
			try {
				fileWriter.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		
		return numWordCards;
	}

	private String buildName(
			String template, 
			int minRank, 
			int maxRank,
			String numberFormat) {
		String deckName = template.replace(
				"#{minrank}", 
				String.format(numberFormat, minRank)).replace(
						"#{maxrank}", 
						String.format(numberFormat, maxRank));
		return deckName;
	}

	private int countCards(CharacterSummary summary) {
		int numCards =
			1 // English card
			+ 1 // Hanzi card
			+ summary.allEntries.size(); // pinyin cards
		
		String traditionalChars = summary.entry.getTraditionalChars();
		String simplifiedChars = summary.entry.getSimplifiedChars();
		if (!traditionalChars.equals(simplifiedChars)) {
			numCards++; // traditional card
		}
		return numCards;
	}

	private void writeEnglishCards(
			List<CharacterSummary> summaries,
			DeckWriter writer, 
			String categoryName) {
		for (CharacterSummary summary : summaries)
		{
			if (summary.strokeOrderData != null) {
				try {
					strokeImageFacade.generateImage(
							summary.strokeOrderData,
							"output/" + getFilenameRoot(summary.entry));
				} catch (RuntimeException e) {
					summary.strokeOrderData = null;
					int rank = summary.entry.getRank();
					String simplifiedCharacter = 
						summary.entry.getSimplifiedChars();
					log.warn("Stroke image failed for rank " + rank +
							": " + simplifiedCharacter, e);
				}
			}
			
			String definition = summary.entry.getDefinition();
			definition = definition.replaceAll("[Ss]urname\\s+\\w+", "surname");
			String question = 
				"Write the character:<br>" + definition;
			StringBuilder answer = new StringBuilder();
			answer.append(getImageTag(summary));
			String simplifiedChars = summary.entry.getSimplifiedChars();
			String traditionalChars = summary.entry.getTraditionalChars();
			if (!simplifiedChars.equals(traditionalChars)) {
				answer.append("traditional: " + traditionalChars + "<br>");
			}
			answer.append(summary.entry.getPinyin());
			if (summary.samples != null)
			{
				answer.append("<br>" + summary.samples);
			}
			writer.writeCard(
					categoryName, 
					question, 
					answer.toString());
		}
	}

	private String getImageTag(CharacterSummary summary) {
		
		if (summary.strokeOrderData != null) {
			return 
				"<img src=\"" + getFilenameRoot(summary.entry) + ".png\"><br>";
		}
		return summary.entry.getSimplifiedChars() + " (strokes unavailable)<br>";
	}

	private String getFilenameRoot(EntryValue entry) {
		String filenameRoot = 
			String.format("stroke-images/%1$04d", entry.getRank());
		return filenameRoot;
	}

	private void writeHanziCards(
			List<CharacterSummary> summaries,
			DeckWriter writer, 
			String categoryName) {
		for (CharacterSummary summary : summaries)
		{
			StringBuilder answer = new StringBuilder();
			String simplifiedChars = summary.entry.getSimplifiedChars();
			String traditionalChars = summary.entry.getTraditionalChars();
			if (!simplifiedChars.equals(traditionalChars)) {
				answer.append("traditional: " + traditionalChars + "<br>");
			}
			answer.append(summary.entry.getPinyin() + "<br>");
			answer.append(summary.entry.getDefinition());
			if (summary.samples != null)
			{
				answer.append("<br>" + summary.samples);
			}
			writer.writeCard(
					categoryName, 
					simplifiedChars, 
					answer.toString());
		}
	}
	
	private int writeWordCards(
			List<CharacterSummary> summaries,
			DeckWriter writer, 
			String categoryName) {
		int maxRank = summaries.get(summaries.size()-1).entry.getRank();
		Iterator<String> itr = wordList.iterator(maxRank);
		int wordCount = 0;
		while (itr.hasNext() && moreWordsNeeded(wordCount))
		{
			String word = itr.next();
			itr.remove();
			EntryValue[] wordEntries = null;
			try {
				wordEntries = dao.findAllEntryValuesByTraditionalCharacter(
						word, 
						1000);
			} catch (RuntimeException e) {
				log.warn(
						"Error loading definition for word: " + word,
						e);
				continue;
			}
			if (wordEntries == null || wordEntries.length == 0)
			{
				log.warn(
						"No entries found for word: " + word);
				continue;
			}
			
			EntryValue firstEntry = wordEntries[0];
			String simplifiedChars = firstEntry.getSimplifiedChars();
			StringBuilder answer = new StringBuilder();
			String traditionalChars = firstEntry.getTraditionalChars();
			if (!simplifiedChars.equals(traditionalChars)) {
				answer.append("traditional: " + traditionalChars + "<br>");
			}
			for (EntryValue entry : wordEntries) {
				if (entry != firstEntry)
				{
					answer.append("<br>");
				}
				answer.append(entry.getPinyin().replaceAll(" ", ""));
				answer.append(" " + entry.getDefinition());
			}
			writer.writeCard(
					categoryName, 
					simplifiedChars, 
					answer.toString());
			wordCount++;
		}
		return wordCount;
	}

	/** Checks if the current deck has room for more word cards.
	 * @param wordCount the number of word cards already in the current deck.
	 * @return
	 */
	private boolean moreWordsNeeded(int wordCount) {
		return 
			wordsPerDeck >= 0
			? wordCount < wordsPerDeck
			: true;
	}
	
	private void writeTraditionalCards(
			List<CharacterSummary> summaries,
			DeckWriter writer, 
			String categoryName) {
		for (CharacterSummary summary : summaries)
		{
			String simplifiedChars = summary.entry.getSimplifiedChars();
			String traditionalChars = summary.entry.getTraditionalChars();
			if (!simplifiedChars.equals(traditionalChars)) {
				StringBuilder answer = new StringBuilder();
				answer.append("simplified: " + simplifiedChars + "<br>");
				answer.append(summary.entry.getPinyin() + "<br>");
				answer.append(summary.entry.getDefinition());
				if (summary.samples != null)
				{
					answer.append("<br>" + summary.samples);
				}
				writer.writeCard(
						categoryName, 
						traditionalChars, 
						answer.toString());
			}
		}
	}
	
	private void writePinyinCards(
			List<CharacterSummary> summaries,
			DeckWriter writer, 
			String categoryName) {
		for (CharacterSummary summary : summaries)
		{
			List<EntryValue> entryList = summary.allEntries;
			for (EntryValue entry : entryList) {
				StringBuilder question = new StringBuilder();
				question.append("Write the character: ");
				String characterPinyin = entry.getPinyin();
				question.append(characterPinyin);
				StringBuilder answer = new StringBuilder();
				answer.append(getImageTag(summary));
				String simplifiedChars = entry.getSimplifiedChars();
				String traditionalChars = entry.getTraditionalChars();
				if (!simplifiedChars.equals(traditionalChars)) {
					answer.append("traditional: " + traditionalChars + "<br>");
				}
				answer.append(entry.getDefinition());
				
				if (entry.getSample() != null)
				{
					String samplePinyin = entry.getSample().getPinyin();
					samplePinyin = samplePinyin.replaceAll(" ", "");
					question.append("<br>(" + samplePinyin);
					question.append(" de " + characterPinyin + ")");
					
					answer.append("<br>" + display(entry.getSample()));
				}
				
				writer.writeCard(
						categoryName, 
						question.toString(), 
						answer.toString());
			}
		}
	}
	
	private CharacterSummary summarize(
			List<EntryValue> characterList,
			List<EntryValue> wordList, 
			String strokeOrderData) {
		StringBuilder definition = new StringBuilder();
		StringBuilder pinyin = new StringBuilder();
		StringBuilder samples = new StringBuilder();
		for (int entryIndex = 0; entryIndex < characterList.size(); entryIndex++)
		{
			if (characterList.size() > 1)
			{
				if (entryIndex > 0)
				{
					definition.append("<br>");
					pinyin.append(" ");
				}
				if (samples.length() > 0) {
					samples.append(" ");
				}
				definition.append((entryIndex+1) + ": ");
				pinyin.append((entryIndex+1) + ": ");
			}
			EntryValue entry = characterList.get(entryIndex);
			definition.append(entry.getDefinition());
			pinyin.append(entry.getPinyin().replaceAll(" ", ""));
			EntryValue sample = entry.getSample();
			if (sample != null)
			{
				samples.append(display(sample));
			}
		}
		
		Collections.sort(
				wordList, 
				new Comparator<EntryValue>() {
					@Override
					public int compare(EntryValue entry1, EntryValue entry2) {
						String traditional1 = entry1.getTraditionalChars();
						String traditional2 = entry2.getTraditionalChars();
						return traditional1.compareTo(traditional2);
					}
				});
		List<List<EntryValue>> wordLists = new ArrayList<List<EntryValue>>();
		String prevWord = "";
		for (EntryValue entryValue : wordList) {
			String currentWord = entryValue.getTraditionalChars();
			if (!currentWord.equals(prevWord)) {
				prevWord = currentWord;
				wordLists.add(new ArrayList<EntryValue>());
			}
			List<EntryValue> currentList = wordLists.get(wordLists.size()-1);
			currentList.add(entryValue);
		}
		
		CharacterSummary summary = new CharacterSummary();
		summary.allEntries = characterList;
		summary.words = wordLists;
		summary.strokeOrderData = strokeOrderData;
		EntryValue summaryEntry = new EntryValue();
		EntryValue firstEntry = characterList.get(0);
		summaryEntry.setSimplifiedChars(firstEntry.getSimplifiedChars());
		summaryEntry.setTraditionalChars(firstEntry.getTraditionalChars());
		summaryEntry.setRank(firstEntry.getRank());
		summaryEntry.setDefinition(definition.toString());
		summaryEntry.setPinyin(pinyin.toString());
		summary.entry = summaryEntry;
		if (samples.length() > 0) {
			summary.samples = samples.toString();
		}
		return summary;
	}

	private String display(EntryValue entry) {
		StringBuilder result = new StringBuilder();
		result.append(entry.getSimplifiedChars());
		if (!entry.getSimplifiedChars().equals(entry.getTraditionalChars())) {
			result.append(" (" + entry.getTraditionalChars() + ")");
		}
		result.append(" " + entry.getPinyin().replaceAll(" ", ""));
		result.append(" " + entry.getDefinition());
		
		return result.toString();
	}

	public DictionaryDao getDao() {
		return dao;
	}

	public void setDao(DictionaryDao dao) {
		this.dao = dao;
	}

	public RankReader getCharacterReader() {
		return characterReader;
	}

	public void setCharacterReader(RankReader characterReader) {
		this.characterReader = characterReader;
	}

	public RankFinder getRankFinder() {
		return rankFinder;
	}

	public void setRankFinder(RankFinder rankFinder) {
		this.rankFinder = rankFinder;
	}

	public SampleBuilder getSampleBuilder() {
		return sampleBuilder;
	}

	public void setSampleBuilder(SampleBuilder sampleBuilder) {
		this.sampleBuilder = sampleBuilder;
	}
	
	private class CharacterSummary {
		EntryValue entry;
		String samples;
		List<EntryValue> allEntries;
		List<List<EntryValue>> words;
		String strokeOrderData;
	}

	public WordListInterface getWordList() {
		return wordList;
	}

	public void setWordList(WordListInterface wordList) {
		this.wordList = wordList;
	}

	public String getDeckNameTemplate() {
		return deckNameTemplate;
	}

	public void setDeckNameTemplate(String deckNameTemplate) {
		this.deckNameTemplate = deckNameTemplate;
	}

	public String getCategoryTemplate() {
		return categoryTemplate;
	}

	public void setCategoryTemplate(String categoryTemplate) {
		this.categoryTemplate = categoryTemplate;
	}

	public int getDeckSize() {
		return deckSize;
	}

	public void setDeckSize(int deckSize) {
		this.deckSize = deckSize;
	}

	public int getCharacterLimit() {
		return characterLimit;
	}

	public void setCharacterLimit(int characterLimit) {
		this.characterLimit = characterLimit;
	}

	public int getWordsPerDeck() {
		return wordsPerDeck;
	}

	public void setWordsPerDeck(int wordsPerDeck) {
		this.wordsPerDeck = wordsPerDeck;
	}
}
