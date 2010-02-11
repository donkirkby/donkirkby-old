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

import java.util.HashMap;
import java.util.Map;


public class InMemoryStrokeDataDao implements IStrokeDataDao
{

	private Map<String, String> strokeDataMap = new HashMap<String, String>();
	
	public void saveStrokeData(String character, String strokeData) 
	{
		strokeDataMap.put(character, strokeData);
	}

	public String getStrokeDataForCharacter(String character) {
		return strokeDataMap.get(character);
	}
}