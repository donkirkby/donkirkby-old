package com.google.code.donkirkby;

import java.nio.charset.Charset;

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
			int MAX_RANK = 1000;
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

			Resource sentenceResource =
					new ClassPathResource("/sentences.csv");
			CsvReader csvReader = new CsvReader(
					sentenceResource.getInputStream(), 
					Charset.forName("utf8"));
			try
			{
				int i = 0;
				csvReader.setDelimiter('\t');
				while (csvReader.readRecord() && i < 100)
				{
					String language = csvReader.get(1);
					if (language.equals("cmn"))
					{
						String sentence = csvReader.get(2);
						String id = csvReader.get(0);
						int maxRank = rankFinder.maxRank(sentence, classifier);
						if (maxRank < MAX_RANK)
						{
							System.out.println(sentence + " " + maxRank + " " + id);
							i++;
						}
					}
				}
			}
			finally
			{
				csvReader.close();
			}
			
			log.info("Success");
		} catch (Exception e) {
			log.error("Failure", e);
			System.exit(-1);
		}
	}
}
