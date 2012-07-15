package com.google.code.donkirkby;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.csvreader.CsvReader;

/**
 * 
 */
public class SentenceRankApp {
	private static Log log = LogFactory.getLog(SentenceRankApp.class);

	private ArrayList<Sentence> chineseSentences;
	private HashMap<Integer, Sentence> sentenceMap;
	private HashMap<Integer, ArrayList<Integer>> links;
	private static int MAX_RANK = 500;

	public static void main(String[] args) {
		log.info("Starting.");

		try {
			new SentenceRankApp().generate();
			
			log.info("Success");
		} catch (Exception e) {
			log.error("Failure", e);
			System.exit(-1);
		}
	}


	private void generate() 
	{
		try
		{
			loadSentences(MAX_RANK);
			loadLinks();
			loadAudio();
	
			String template = loadTemplate();
			String startTag = "$START-TEMPLATE$";
			int templateStart = template.indexOf(startTag) + startTag.length();
			String endTag = "$END-TEMPLATE$";
			int templateEnd = template.indexOf(endTag);
			String header = template.substring(0, templateStart);
			String footer = template.substring(templateEnd + endTag.length());
			template = template.substring(templateStart, templateEnd);
			
			OutputStreamWriter writer = new OutputStreamWriter(
					new FileOutputStream("output/sentences.html"), 
					"utf8");
			try
			{
				writer.write(header);
				int i = 0;
				for (Sentence sentence: chineseSentences) {
					i++;
					String anchorId = 
							sentence.getId() + "rank" + sentence.getMaxRank();
					String translation = findDirectTranslation(sentence.getId());
					String noTranslation;
					if (translation != null)
					{
						noTranslation = "";
					}
					else
					{
						translation = findIndirectTranslation(sentence.getId());
						noTranslation =
								translation.length() == 0
								? "no direct translation found"
								: "[indirect]";
					}
					writer.write(
							template
							.replace("$CHINESE$", sentence.getText())
							.replace("$ENGLISH$", translation)
							.replace("$NO-ENGLISH$", noTranslation)
							.replace("$ANCHOR-ID$", anchorId)
							.replace("$INDEX$", Integer.toString(i))
							.replace(
									"$SENTENCE-ID$", 
									Integer.toString(sentence.getId())));
	//				System.out.println(sentence.getText() + " " + sentence.getRank() + " " + sentence.getId());
				}
				writer.write(footer);
			}
			finally
			{
				writer.close();
			}
		}
		catch (IOException ex)
		{
			throw new RuntimeException(ex);
		}
	}

	/* Returns a direct translation, or null if none found. */
	private String findDirectTranslation(int sentenceId)
	{
		ArrayList<Integer> translations = links.get(sentenceId);
		if (translations == null)
		{
			return null;
		}
		for (Integer translationId : translations) {
			Sentence translation = sentenceMap.get(translationId);
			if (translation != null)
			{
				return translation.getText();
			}
		}
		return null;
	}

	/* Returns indirect translations, but only searches one hop away.
	 * Returns empty string if none found.
	 */
	private String findIndirectTranslation(int sentenceId) {
		ArrayList<Integer> directTranslations = links.get(sentenceId);
		if (directTranslations == null)
		{
			return "";
		}
		for (Integer directTranslationId : directTranslations) {
			ArrayList<Integer> indirectTranslations = 
					links.get(directTranslationId);
			if (indirectTranslations == null)
			{
				continue;
			}
			for (Integer indirectTranslationId : indirectTranslations) {
				Sentence indirectTranslation = 
						sentenceMap.get(indirectTranslationId);
				if (indirectTranslation != null 
						&& indirectTranslationId != sentenceId)
				{
					return indirectTranslation.getText();
				}
			}
		}
		return "";
	}


	private static String loadTemplate() {
		try
		{
			StringWriter writer = new StringWriter();
			PrintWriter printer = new PrintWriter(writer);
			Resource templateResource = 
				new ClassPathResource("/template.html");
			InputStream inputStream = templateResource.getInputStream();
			BufferedReader reader = 
				new BufferedReader(new InputStreamReader(inputStream, "utf8"));
			try
			{
				String line;
				do
				{
					line = reader.readLine();
					if (line != null)
					{
						printer.println(line);
					}
				}while (line != null);
			}
			finally
			{
				inputStream.close();
				printer.close();
			}
			String template = writer.toString();
			return template;
		}
		catch (IOException ex)
		{
			throw new RuntimeException(ex);
		}
	}


	private void loadSentences(int MAX_RANK)
	{
		Resource characterResource = 
			new ClassPathResource("/character_frequency_utf8.txt");
		DefaultRankReader reader = new DefaultRankReader();
		reader.setHeaderLineCount(8);
		reader.setResource(characterResource);
		Resource wordResource =
				new ClassPathResource("/phrase_frequency_utf8.txt");
		DefaultRankReader wordReader = new DefaultRankReader();
		wordReader.setResource(wordResource);
		wordReader.setHeaderLineCount(1);
		RankFinder rankFinder = new RankFinder();
		rankFinder.setCharacterReader(reader);
		rankFinder.setWordReader(wordReader);
		rankFinder.setMaxCharacters(MAX_RANK);
		rankFinder.load();
		CharacterClassifier classifier = new CharacterClassifier();

		chineseSentences = new ArrayList<Sentence>();
		sentenceMap = new HashMap<Integer, Sentence>();
		try
		{
			Resource sentenceResource =
					new ClassPathResource("/sentences.csv");
			CsvReader csvReader = new CsvReader(
					sentenceResource.getInputStream(), 
					Charset.forName("utf8"));
			
			try
			{
				csvReader.setDelimiter('\t');
				csvReader.setUseTextQualifier(false);
				while (csvReader.readRecord())
				{
					String language = csvReader.get(1);
					if (language.equals("cmn") && chineseSentences.size() < 1000000)
					{
						String text = csvReader.get(2);
						int[] ranks = rankFinder.getRanks(text, classifier);
						Sentence sentence = new Sentence();
						sentence.setRanks(ranks);
						int maxRank = sentence.getMaxRank();
						if (0 < maxRank && maxRank < MAX_RANK)
						{
							sentence.setText(text);
							sentence.setId(Integer.parseInt(csvReader.get(0)));
							sentence.setChinese(true);
							chineseSentences.add(sentence);
							sentenceMap.put(sentence.getId(), sentence);
						}
					}
					else if (language.equals("eng"))
					{
						Sentence sentence = new Sentence();
						sentence.setText(csvReader.get(2));
						sentence.setId(Integer.parseInt(csvReader.get(0)));
						sentenceMap.put(sentence.getId(), sentence);
					}
				}
			}
			finally
			{
				csvReader.close();
			}
		}
		catch (IOException ex)
		{
			throw new RuntimeException(ex);
		}
		Collections.sort(chineseSentences);
	}
	
	private void loadLinks()
	{
		try
		{
			links = new HashMap<Integer, ArrayList<Integer>>();
			Resource linksResource =
					new ClassPathResource("/links.csv");
			CsvReader csvReader = new CsvReader(
					linksResource.getInputStream(), 
					Charset.forName("utf8"));
			
			try
			{
				csvReader.setDelimiter('\t');
				while (csvReader.readRecord())
				{
					int id1 = Integer.parseInt(csvReader.get(0));
					int id2 = Integer.parseInt(csvReader.get(1));
					ArrayList<Integer> set1 = links.get(id1);
					ArrayList<Integer> set2 = links.get(id2);
					if (set1 == null)
					{
						set1 = new ArrayList<Integer>();
						links.put(id1, set1);
					}
					if (set2 == null)
					{
						set2 = new ArrayList<Integer>();
						links.put(id2, set2);
					}
					set1.add(id2);
					set2.add(id1);
				}
			}
			finally
			{
				csvReader.close();
			}
		}
		catch (IOException ex)
		{
			throw new RuntimeException(ex);
		}
	}

	private void loadAudio()
	{
		try
		{
			Resource tagsResource =
					new ClassPathResource("/tags.csv");
			CsvReader csvReader = new CsvReader(
					tagsResource.getInputStream(), 
					Charset.forName("utf8"));
			
			try
			{
				csvReader.setDelimiter('\t');
				while (csvReader.readRecord())
				{
					int id = Integer.parseInt(csvReader.get(0));
					String tag = csvReader.get(1);
					if (tag.equals("has audio")) {
						Sentence sentence = sentenceMap.get(id);
						if (sentence != null) {
							sentence.setSpoken(true);
						}
					}
				}
			}
			finally
			{
				csvReader.close();
			}
		}
		catch (IOException ex)
		{
			throw new RuntimeException(ex);
		}
	}
}
