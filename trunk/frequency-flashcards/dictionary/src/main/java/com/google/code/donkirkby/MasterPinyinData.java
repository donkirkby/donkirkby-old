/* Copied from ZDT project.
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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MasterPinyinData {

	private String[] masterPinyinList = null;
	private BufferedReader reader = null;
	private Log mLogger = LogFactory.getLog(MasterPinyinData.class);
	
	public MasterPinyinData(String pinyinTableFileName)
	{
		InputStream is = getClass().getResourceAsStream("/"+pinyinTableFileName);
		reader = new BufferedReader(new InputStreamReader(is));
		loadData();
	}
	/**
	 * Loads the data from a file.  Expects pinyin separated by tabs
	 * over multiple lines.
	 */
	private void loadData() {		
		String line = null;
		List<String> pinyinList = new ArrayList<String>();
		try
		{
			long time = System.currentTimeMillis();
			
			while ((line = reader.readLine()) != null)
	        {
				// Ignore comments
				if (line.startsWith("#"))
					continue;
				String[] pinyinLine = line.split("\t");
				for (String pinyin : pinyinLine)
				{
					if (!pinyin.equals("")) {
						pinyinList.add(pinyin.trim());
					}
				}
	        }
			mLogger.debug("Time to parse pinyin table file: " +
					(System.currentTimeMillis() - time));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		// Sort by string length.  I want the longest strings first.
		Collections.sort(pinyinList, new Comparator<String>() {
			public int compare(String s1, String s2) {
				return s2.length() - s1.length();
			}
		});
		masterPinyinList = pinyinList.toArray(new String[0]);
	}

	/**
	 * This String[] contains all valid pinyin combinations (w/o tones)
	 * which is read from the pinyin table file.
	 * @return a String[] of pinyin
	 */
	public String[] getMasterPinyinList() {
		return masterPinyinList;
	}

}
