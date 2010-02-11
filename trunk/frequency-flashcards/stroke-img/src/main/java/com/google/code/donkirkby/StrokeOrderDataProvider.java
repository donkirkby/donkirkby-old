/*
 * Copyright (c) 2004-2006 Chris Fong and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Chris Fong - initial API and implementation
 */
package com.google.code.donkirkby;

import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;



public class StrokeOrderDataProvider {

	private static IStrokeDataDao strokeDataDao;
	private static Log mLogger = LogFactory.getLog(StrokeOrderDataProvider.class);
	
	private static void checkLoaded()
	{
		if (strokeDataDao != null)
			return;

		long time = System.currentTimeMillis();
		
		strokeDataDao = new InMemoryStrokeDataDao();
		try
		{
			InputStream is = StrokeOrderDataProvider.class.getResourceAsStream(
					"/zdtStrokeData.txt");
			StrokeOrderDataParser parser = new StrokeOrderDataParser(
					new InputStreamReader(is, "utf-8"), 
					strokeDataDao);
			parser.parse();
		}
		catch (Exception e)
		{
			mLogger.fatal("Exception loading stroke data", e);
		}
		mLogger.info("Time to parse stroke data " + (System.currentTimeMillis() - time));
	}
	
	/**
	 * @param character a Chinese character
	 * @return true if stroke data exists for the character or false if not.
	 */
	public static boolean hasStrokeOrderData(String character) {
		checkLoaded();
			
		return strokeDataDao.getStrokeDataForCharacter(character) != null;
	}
	
	/**
	 * @param character a Chinese character
	 * @return the stroke order data for the passed in character or null if
	 * no data
	 */
	public static String getStrokeOrderData(String character) {
		checkLoaded();
		
		String strokeOrderData = 
			strokeDataDao.getStrokeDataForCharacter(character);
		return strokeOrderData;
	}

}
