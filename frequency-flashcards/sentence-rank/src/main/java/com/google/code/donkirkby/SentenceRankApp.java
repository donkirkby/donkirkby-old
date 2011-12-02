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
	

	public static void main(String[] args) {
		log.info("Starting.");

		try {
			int MAX_RANK = 500;
			ArrayList<Sentence> sentences = loadSentences(MAX_RANK);

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
				for (Sentence sentence: sentences) {
					String link = 
							"<a href='http://tatoeba.org/eng/sentences/show/" + 
							sentence.getId() + "'>on Tatoeba</a>";
					writer.write(
							template
							.replace("$CHINESE$", sentence.getText())
							.replace("$LINK$", link));
	//				System.out.println(sentence.getText() + " " + sentence.getRank() + " " + sentence.getId());
				}
				writer.write(footer);
			}
			finally
			{
				writer.close();
			}
			
			log.info("Success");
		} catch (Exception e) {
			log.error("Failure", e);
			System.exit(-1);
		}
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


	private static ArrayList<Sentence> loadSentences(int MAX_RANK)
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

		ArrayList<Sentence> sentences = new ArrayList<Sentence>();
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
				while (csvReader.readRecord() && sentences.size() < 100)
				{
					String language = csvReader.get(1);
					if (language.equals("cmn"))
					{
						String text = csvReader.get(2);
						int maxRank = rankFinder.maxRank(text, classifier);
						if (maxRank < MAX_RANK)
						{
							Sentence sentence = new Sentence();
							sentence.setText(text);
							sentence.setId(csvReader.get(0));
							sentence.setRank(maxRank);
							sentences.add(sentence);
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
		Collections.sort(sentences);
		return sentences;
	}
}
