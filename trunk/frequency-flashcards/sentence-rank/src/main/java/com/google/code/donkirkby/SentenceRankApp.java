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
			Resource characterResource = 
				new ClassPathResource("/character_frequency_utf8.txt");
			DefaultRankReader reader = new DefaultRankReader();
			reader.setHeaderLineCount(8);
			reader.setResource(characterResource);
			reader.open();
			try
			{
				
				for (int frequency = 1; frequency <= 10; frequency++)
				{
					String character = reader.nextItem();
					String filenameRoot = String.format("output/%1$04d", frequency);
					
				}
			}finally
			{
				reader.close();
			}
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
						System.out.println(sentence);
						i++;
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
