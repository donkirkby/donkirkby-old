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
import java.util.HashSet;

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
	private HashMap<Integer, HashSet<Integer>> links;
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
	
			String template = loadTemplate();
			int templateStart = template.indexOf("$START-TEMPLATE$");
			int templateEnd = template.indexOf("$END-TEMPLATE$");
			String header = template.substring(0, templateStart);
			String footer = template.substring(templateEnd);
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
							sentence.getId() + "rank" + sentence.getRank();
					String translation =
							sentence.getTranslation() != null
							? sentence.getTranslation().getText()
							: "";
					String noTranslation;
					if (translation.length() != 0)
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


	private String findIndirectTranslation(int sentenceId) {
		HashSet<Integer> translationIds = links.get(sentenceId);
		if (translationIds != null)
		{
			for (Integer translationId : translationIds)
			{
				if (translationId != sentenceId)
				{
					Sentence translation = sentenceMap.get(translationId);
					if (translation != null)
					{
						return translation.getText();
					}
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
					printer.println(line);
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
				while (csvReader.readRecord())
				{
					String language = csvReader.get(1);
					if (language.equals("cmn") && chineseSentences.size() < 1000000)
					{
						String text = csvReader.get(2);
						int maxRank = rankFinder.maxRank(text, classifier);
						if (0 < maxRank && maxRank < MAX_RANK)
						{
							Sentence sentence = new Sentence();
							sentence.setText(text);
							sentence.setId(Integer.parseInt(csvReader.get(0)));
							sentence.setRank(maxRank);
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
			links = new HashMap<Integer, HashSet<Integer>>();
			Resource sentenceResource =
					new ClassPathResource("/links.csv");
			CsvReader csvReader = new CsvReader(
					sentenceResource.getInputStream(), 
					Charset.forName("utf8"));
			
			try
			{
				csvReader.setDelimiter('\t');
				while (csvReader.readRecord())
				{
					int id1 = Integer.parseInt(csvReader.get(0));
					int id2 = Integer.parseInt(csvReader.get(1));
					Sentence sentence1 = sentenceMap.get(id1);
					Sentence sentence2 = sentenceMap.get(id2);
					if (sentence1 != null && sentence2 != null)
					{
						sentence1.setTranslation(sentence2);
						sentence2.setTranslation(sentence1);
					}
					HashSet<Integer> set1 = links.get(id1);
					HashSet<Integer> set2 = links.get(id2);
					if (set1 != null && set2 != null)
					{
						set1.addAll(set2);
						links.put(id2, set1);
					}
					else if (set1 != null)
					{
						links.put(id2, set1);
						set1.add(id2);
					}
					else if (set2 != null)
					{
						links.put(id1, set2);
						set2.add(id1);
					}
					else
					{
						set1 = new HashSet<Integer>();
						set1.add(id1);
						set1.add(id2);
						links.put(id1, set1);
						links.put(id2, set1);
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
