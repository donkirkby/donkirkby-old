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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class StrokeOrderDataParser {
	private static Log mLogger = LogFactory.getLog(StrokeOrderDataParser.class);

	private BufferedReader reader;
	private IStrokeDataDao strokeDataDao;
	
	/**
	 * Takes a stream of characters in the strokeorderdata format
	 * @param reader
	 */
	public StrokeOrderDataParser(Reader reader, IStrokeDataDao strokeDataDao) {
		this.reader = new BufferedReader(reader);
		this.strokeDataDao = strokeDataDao;
	}

	public void parse() {
		long time = System.currentTimeMillis();
		String line = null;
		try
		{
		while ((line = this.reader.readLine()) != null)
        {
			Pattern pat = Pattern.compile("([^\\s])\\t(.+)");
			Matcher m = pat.matcher(line);
			if (m.find())
			{
				String character = m.group(1);
				String strokeData = m.group(2);
				strokeDataDao.saveStrokeData(character, strokeData);
			}
        }
		}
		catch (IOException e)
		{
			mLogger.fatal("Error parsing character stream", e);
		}
		System.out.println("Time to parse file: " + (System.currentTimeMillis() - time));
	}
	
	public void outputDB(Reader reader) {
		this.reader = new BufferedReader(reader);
		String line = null;
		try
		{
			long time = System.currentTimeMillis();
			while ((line = this.reader.readLine()) != null)
	        {
				Pattern pat = Pattern.compile("([^\\s])\\t.*[\"']StrokeData[\"'] VALUE =\\s*[\"']([^\"']+)[\"']");
				Matcher m = pat.matcher(line);
				if (m.find())
				{
					String character = m.group(1);
					String strokeData = m.group(2).trim();
					if (!strokeData.startsWith("#"))
					{
						continue;
					}
					strokeDataDao.saveStrokeData(character, strokeData);
				}
	        }
			System.out.println("Time to parse file: " + (System.currentTimeMillis() - time));
		}
		catch (IOException e)
		{
			mLogger.fatal("Error parsing character stream", e);
		}
	}
	
	public void outputIntermediateFile(Reader reader) {
		this.reader = new BufferedReader(reader);
		
		String line = null;
		try
		{
			OutputStream os = new FileOutputStream("c:/temp/stroke/pork.txt");
			OutputStreamWriter osw = new OutputStreamWriter(os, "utf-8");
			BufferedWriter writer = new BufferedWriter(osw);
			long time = System.currentTimeMillis();
			while ((line = this.reader.readLine()) != null)
	        {
				Pattern pat = Pattern.compile("([^\\s])\\t.*[\"']StrokeData[\"'] VALUE =\\s*[\"']([^\"']+)[\"']");
				Matcher m = pat.matcher(line);
				if (m.find())
				{
					String character = m.group(1);
					String strokeData = m.group(2).trim();
					if (!strokeData.startsWith("#"))
					{
						continue;
					}
					writer.write(character + "\t" + strokeData);
					writer.newLine();
				}
	        }
			System.out.println("Time to parse file: " + (System.currentTimeMillis() - time));
		}
		catch (IOException e)
		{
			mLogger.fatal("Error parsing character stream", e);
		}
	}

	public static void main(String[] args)
	{
		try {
			InputStream is = new FileInputStream("c:/temp/stroke/strokeorderdata-utf8.txt");
			InputStreamReader reader = new InputStreamReader(is, "utf-8");
			StrokeOrderDataParser parser = new StrokeOrderDataParser(reader, null);
			parser.outputIntermediateFile(reader);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
}
