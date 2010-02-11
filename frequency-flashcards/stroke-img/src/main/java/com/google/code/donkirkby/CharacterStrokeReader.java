package com.google.code.donkirkby;

import java.util.ArrayList;
import java.util.List;

/** Copied from ZDT project: http://zdt.sourceforge.net
 * Copyright (c) 2004-2006 Chris Fong and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     	Written by Erik Peterson
 * 		Reversed Engineered from "Chinese Font Player" by Zhou Qiong & Dong Dong
 *  	February 12, 2005
 *  	Permission is given to used this code in any way as long as this notice is preserved
 *  
 *  	Converted to SWT by Chris Fong
 *
 */
public class CharacterStrokeReader {
	public static List<CharacterSegment> read(String strokeData)
	{
		List<CharacterSegment> strokes = new ArrayList<CharacterSegment>();
		int i, start;

		StringBuffer tmpstroke = new StringBuffer();
		for (i = 0; i < strokeData.length() && strokeData.charAt(i) == ' '; i++) {
		}
		start = i;
		for (; i < strokeData.length(); i++) {
			if (strokeData.charAt(i) == '#' && i != start) {
				addStroke(strokes, tmpstroke);
				tmpstroke.setLength(0);
			}
			tmpstroke.append(strokeData.charAt(i));
		}
		addStroke(strokes, tmpstroke);
		
		return strokes;
	}

	private static void addStroke(
			List<CharacterSegment> strokes,
			StringBuffer strokeData) {
		CharacterSegment characterStroke = 
			new CharacterSegment(strokeData.toString());
		if (characterStroke.getShape().npoints > 0) {
			strokes.add(characterStroke);
		}
	}
}
