package com.google.code.donkirkby;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/**
 * 
 */
public class StrokeImageApp {
	private static Log log = LogFactory.getLog(StrokeImageApp.class);
	

	public static void main(String[] args) {
		log.info("Starting.");

		try {
			StrokeImageFacade facade = new StrokeImageFacade();
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
					
					String strokeOrderData = 
						StrokeOrderDataProvider.getStrokeOrderData(character);
					if (strokeOrderData == null)
					{
						String msg = "No stroke order data found for " + character;
						throw new IllegalArgumentException(msg);
					}
					facade.generateImage(strokeOrderData, filenameRoot);
				}
			}finally
			{
				reader.close();
			}
			
			log.info("Success");
		} catch (Exception e) {
			log.error("Failure", e);
			System.exit(-1);
		}
	}
}
