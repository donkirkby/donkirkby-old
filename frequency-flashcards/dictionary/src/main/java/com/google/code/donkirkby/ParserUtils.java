/* Copied pieces from the ZDT project.
 * 
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParserUtils {
    private static final char NEUTRAL_TONE = '5';
	public static final String VALID_SPECIAL_CHARS = "=+. ;?,!-&。；？，！－＋＝";
    public static final String PINYIN_REGEX = "[aeiou](\\w)*:?\\d";
    private static Map<String, String> mMap1 = new HashMap<String, String>();
    private static MasterPinyinData mPinyinData = new MasterPinyinData("pinyin_table.txt");
    private static Map<String, String> mMap2 = new HashMap<String, String>();
    static {
    	mMap1.put("iang1", "i//aq//ng");
        mMap1.put("iang2", "i//aw//ng");
        mMap1.put("iang3", "i//ae//ng");
        mMap1.put("iang4", "i//ar//ng");
        mMap1.put("iong1", "i//oq//ng");
        mMap1.put("iong2", "i//ow//ng");
        mMap1.put("iong3", "i//oe//ng");
        mMap1.put("iong4", "i//or//ng");
        mMap1.put("uang1", "u//aq//ng");
        mMap1.put("uang2", "u//aw//ng");
        mMap1.put("uang3", "u//ae//ng");
        mMap1.put("uang4", "u//ar//ng");
        mMap1.put("ianr1", "i//aq//nr");
        mMap1.put("ianr2", "i//aw//nr");
        mMap1.put("ianr3", "i//ae//nr");
        mMap1.put("ongr1", "//oq//ngr");
        mMap1.put("ongr2", "//ow//ngr");
        mMap1.put("ongr3", "//oe//ngr");
        mMap1.put("ongr4", "//or//ngr");
        mMap1.put("ian4", "i//ar//n");
        mMap1.put("ang1", "//aq//ng");
        mMap1.put("ang2", "//aw//ng");
        mMap1.put("ang3", "//ae//ng");
        mMap1.put("ang4", "//ar//ng");
        mMap1.put("eng1", "//eq//ng");
        mMap1.put("eng2", "//ew//ng");
        mMap1.put("eng3", "//ee//ng");
        mMap1.put("eng4", "//er//ng");
        mMap1.put("ing1", "//iq//ng");
        mMap1.put("ing2", "//iw//ng");
        mMap1.put("ing3", "//ie//ng");
        mMap1.put("ing4", "//ir//ng");
        mMap1.put("ong1", "//oq//ng");
        mMap1.put("ong2", "//ow//ng");
        mMap1.put("ong3", "//oe//ng");
        mMap1.put("ong4", "//or//ng");
        mMap1.put("ian1", "i//aq//n");
        mMap1.put("ian2", "i//aw//n");
        mMap1.put("ian3", "i//ae//n");
        mMap1.put("ian4", "i//ar//n");
        mMap1.put("iao1", "i//aq//o");
        mMap1.put("iao2", "i//aw//o");
        mMap1.put("iao3", "i//ae//o");
        mMap1.put("iao4", "i//ar//o");
        mMap1.put("uan1", "u//aq//n");
        mMap1.put("uan2", "u//aw//n");
        mMap1.put("uan3", "u//ae//n");
        mMap1.put("uan4", "u//ar//n");
        mMap1.put("uai1", "u//aq//i");
        mMap1.put("uai2", "u//aw//i");
        mMap1.put("uai3", "u//ae//i");
        mMap1.put("uai4", "u//ar//i");
        mMap1.put("anr1", "//aq//nr");
        mMap1.put("anr2", "//aw//nr");
        mMap1.put("anr3", "//ae//nr");
        mMap1.put("anr4", "//ar//nr");
        mMap1.put("an1", "//aq//n");
        mMap1.put("an2", "//aw//n");
        mMap1.put("an3", "//ae//n");
        mMap1.put("an4", "//ar//n");
        mMap1.put("en1", "//eq//n");
        mMap1.put("en2", "//ew//n");
        mMap1.put("en3", "//ee//n");
        mMap1.put("en4", "//er//n");
        mMap1.put("in1", "//iq//n");
        mMap1.put("in2", "//iw//n");
        mMap1.put("in3", "//ie//n");
        mMap1.put("in4", "//ir//n");
        mMap1.put("ia1", "//iq//a");
        mMap1.put("ia2", "//iw//a");
        mMap1.put("ia3", "//ie//a");
        mMap1.put("ia4", "//ir//a");
        mMap1.put("ie1", "i//eq//");
        mMap1.put("ie2", "i//ew//");
        mMap1.put("ie3", "i//ee//");
        mMap1.put("ie4", "i//er//");
        mMap1.put("un1", "//uq//n");
        mMap1.put("un2", "//uw//n");
        mMap1.put("un3", "//ue//n");
        mMap1.put("un4", "//ur//n");
        mMap1.put("ui1", "u//iq//");
        mMap1.put("ui2", "u//iw//");
        mMap1.put("ui3", "u//ie//");
        mMap1.put("ui4", "u//ir//");
        mMap1.put("iu1", "i//uq//");
        mMap1.put("iu2", "i//uw//");
        mMap1.put("iu3", "i//ue//");
        mMap1.put("iu4", "i//ur//");
        mMap1.put("ao1", "//aq//o");
        mMap1.put("ao2", "//aw//o");
        mMap1.put("ao3", "//ae//o");
        mMap1.put("ao4", "//ar//o");
        mMap1.put("ou1", "//oq//u");
        mMap1.put("ou2", "//ow//u");
        mMap1.put("ou3", "//oe//u");
        mMap1.put("ou4", "//or//u");
        mMap1.put("ai1", "//aq//i");
        mMap1.put("ai2", "//aw//i");
        mMap1.put("ai3", "//ae//i");
        mMap1.put("ai4", "//ar//i");
        mMap1.put("ei1", "//eq//i");
        mMap1.put("ei2", "//ew//i");
        mMap1.put("ei3", "//ee//i");
        mMap1.put("ei4", "//er//i");
        mMap1.put("er2", "//ew//r");
        mMap1.put("er3", "//ee//r");
        mMap1.put("er4", "//er//r");
        mMap1.put("u:1", "//u:q//");
        mMap1.put("u:2", "//u:w//");
        mMap1.put("u:3", "//u:e//");
        mMap1.put("u:4", "//u:r//");
        mMap1.put("uo1", "u//oq//");
        mMap1.put("uo2", "u//ow//");
        mMap1.put("uo3", "u//oe//");
        mMap1.put("uo4", "u//or//");
        mMap1.put("ua1", "u//aq//");
        mMap1.put("ua2", "u//aw//");
        mMap1.put("ua3", "u//ae//");
        mMap1.put("ua4", "u//ar//");
        mMap1.put("ue1", "u//eq//");
        mMap1.put("ue2", "u//ew//");
        mMap1.put("ue3", "u//ee//");
        mMap1.put("ue4", "u//er//");
        mMap1.put("a1", "//aq//");
        mMap1.put("a2", "//aw//");
        mMap1.put("a3", "//ae//");
        mMap1.put("ar3", "//ae//r");
        mMap1.put("a4", "//ar//");
        mMap1.put("ar4", "//ar//r");
        mMap1.put("e1", "//eq//");
        mMap1.put("e2", "//ew//");
        mMap1.put("e3", "//ee//");
        mMap1.put("e4", "//er//");
        mMap1.put("o1", "//oq//");
        mMap1.put("o2", "//ow//");
        mMap1.put("o3", "//oe//");
        mMap1.put("o4", "//or//");
        mMap1.put("i1", "//iq//");
        mMap1.put("i2", "//iw//");
        mMap1.put("i3", "//ie//");
        mMap1.put("i4", "//ir//");
        mMap1.put("u1", "//uq//");
        mMap1.put("u2", "//uw//");
        mMap1.put("u3", "//ue//");
        mMap1.put("u4", "//ur//");

        mMap2.put("//aq//", "\u0101");
        mMap2.put("//aw//", "\u00E1");
        mMap2.put("//ae//", "\u01CE");
        mMap2.put("//ar//", "\u00E0");
        mMap2.put("//an//", "a");
        mMap2.put("//eq//", "\u0113");
        mMap2.put("//ew//", "\u00E9");
        mMap2.put("//ee//", "\u011B");
        mMap2.put("//er//", "\u00E8");
        mMap2.put("//en//", "e");
        mMap2.put("//iq//", "\u012B");
        mMap2.put("//iw//", "\u00ED");
        mMap2.put("//ie//", "\u01D0");
        mMap2.put("//ir//", "\u00EC");
        mMap2.put("//in//", "i");
        mMap2.put("//oq//", "\u014D");
        mMap2.put("//ow//", "\u00F3");
        mMap2.put("//oe//", "\u01D2");
        mMap2.put("//or//", "\u00F2");
        mMap2.put("//on//", "o");
        mMap2.put("//uq//", "\u016B");
        mMap2.put("//uw//", "\u00FA");
        mMap2.put("//ue//", "\u01D4");
        mMap2.put("//ur//", "\u00F9");
        mMap2.put("//u:q//", "\u01D6");
        mMap2.put("//u:w//", "\u01D8");
        mMap2.put("//u:e//", "\u01DA");
        mMap2.put("//u:r//", "\u01DC");
        mMap2.put("//un//", "u");
    }

    /**
     * Add tone marks to one or more pinyin characters.
     * @param pinyin the pinyin characters.
     * @return the pinyin characters with tone marks.
     */
    public static String addToneMarks(String pinyin) {
        // exact match
        String[] pinyinList = ParserUtils.splitPinyin(pinyin, true);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < pinyinList.length; i++) {
        	if (sb.length() > 0 && sb.charAt(sb.length()-1) != ' ') {
				sb.append(' ');
			}
            sb.append(ParserUtils.addToneMarksSinglePinyin(pinyinList[i].trim()));
        }
        return sb.toString();
    }
    
    /**
     * Takes a string of pinyin and splits it into separate words
     * @param stringOfPinyin String of pinyin to split.
     * @param addNeutralTone if true, appends '5' for any neutral tones
     * @return a String[] of individual pinyin words.
     * @throws IllegalArgumentException if no pinyin to split.
     */
    public static String[] splitPinyin(String stringOfPinyin, boolean addNeutralTone)
    {
    	try
    	{
	    	String[] masterPinyinList = mPinyinData.getMasterPinyinList();
	    	// Create a temp array to hold the pinyin parts as we
	    	// parse them.  Use the array index to keep them in the
	    	// correct order.
	    	String[] tempList = new String[stringOfPinyin.length()];
	    	List<String> tempList2 = new ArrayList<String>();
	    	
	    	// Create a copy of the original string of pinyin so
	    	// we can use it to find the index of matches since we're
	    	// mutating the original string.
	    	String strCopy = new String(stringOfPinyin);
	    	
	    	// Create a separate copy for matching specials
	    	String strCopy2 = new String(stringOfPinyin);
	    	
	    	// Loop through the master pinyin list and see if any of
	    	// the pinyin on the list matches something in our string of pinyin
	    	for (String pinyin : masterPinyinList)
			{
	    		// Regex match pinyin with optional tone.  Capture
	    		// two groups, the pinyin+tone and tone
	    		Pattern pat = Pattern.compile("("+pinyin+"([1-4_]?))", Pattern.CASE_INSENSITIVE);
	    		Matcher m = pat.matcher(stringOfPinyin);
	    		// A particular pinyin can match multiple times in the string.
	    		while (m.find())
				{
	    			String matchedPinyin = m.group(1);
					String tone = m.group(2);
					// Once we have a match, remove it from the original string.
					stringOfPinyin = stringOfPinyin.replace(matchedPinyin, "");
					int matchIndex = strCopy.indexOf(matchedPinyin);
					// If we didn't capture the tone, then we assume its neutral.
					if (tone.equals("") && addNeutralTone)
						matchedPinyin = matchedPinyin+NEUTRAL_TONE;
					// Masks off the matched pinyin from the copy.  So we
					// don't get thrown off by duplicates.
					strCopy = strCopy.replaceFirst(matchedPinyin, 
							getMask(matchedPinyin.length()));
	
					tempList[matchIndex] = matchedPinyin;
				}
			}
	    	
	    	// Strip out all the nulls from tempList so we have the pinyin in order
	    	for (String s : tempList)
	    	{
	    		if (s != null)
	    			tempList2.add(s);
	    	}
	    	
	    	// Now look for special characters in the original pinyin string so we
	    	// can add them back in as well.
	    	Pattern pat = Pattern.compile("(["+VALID_SPECIAL_CHARS+"]+)");
	    	Matcher m = pat.matcher(strCopy2);
	    	int pinyinIndex = 0;
	    	while (m.find()) {
	    		String matchedSpecial = m.group(1);
	    		int matchIndex = strCopy2.indexOf(matchedSpecial, pinyinIndex);
	    		// Look for the position that the matched special should go into
	    		int curPos = 0;
	    		for (int i = 0; i < tempList2.size(); i++)
	    		{
	    			String pinyin = tempList2.get(i);
	    			Matcher m2 = pat.matcher(pinyin);
	    			if (!m2.find())
	    			{
		    			curPos += pinyin.length();
		    			if (curPos == matchIndex) {
		    				pinyinIndex = i;
		    				break;
		    			}
	    			}
	    		}
	    		// Look for the position that the matched special should go into
	    		tempList2.add(pinyinIndex+1, matchedSpecial);
	    		pinyinIndex += 2;
	    		// remove matches from string so we can find duplicates
	    		strCopy2 = strCopy2.replaceFirst("["+matchedSpecial+"]+", "");
	    	}
	    	
	//    	List<String> result = new ArrayList<String>();
	//    	for (String item : tempList)
	//    	{
	//    		if (item != null)
	//    			result.add(item);
	//    	}
	//    	return result.toArray(new String[0]);
	    	return tempList2.toArray(new String[0]);
    	}catch (Exception ex)
    	{
    		throw new IllegalArgumentException("Invalid pinyin: '" + stringOfPinyin + "'", ex);
    	}
    }
	
    /**
     * Adds tone mark to a single pinyin character 
     * @param pinyin
     * @return the pinyin with tone.
     * @throws IllegalArgumentException if not a valid single pinyin string.
     */
    private static String addToneMarksSinglePinyin(String pinyin)
    {
        StringBuffer unicode = new StringBuffer();
        // look for the first vowel in the word
        Pattern pat = Pattern.compile(ParserUtils.PINYIN_REGEX);
        Matcher m = pat.matcher(pinyin);
        if (m.find())
        {
            String startSyllable = pinyin.substring(0, m.start());
            String endSyllable = m.group();
            char tone = endSyllable.toCharArray()[endSyllable.length()-1];
            if (tone == NEUTRAL_TONE)
            {
                // return the pinyin without the tone at the end
                return pinyin.substring(0, pinyin.length()-1);
            }
            
            // special case if ends in 'r'
            boolean endsWithR = false;
            if (endSyllable.charAt(endSyllable.length()-2) == 'r')
            {
                // split out the r
                String tempEnd = endSyllable.substring(0, endSyllable.length()-2);
                String tempTone = endSyllable.substring(endSyllable.length()-1);
                endSyllable = tempEnd + tempTone;
                endsWithR = true; 
            }
            String tempStr = (String)mMap1.get(endSyllable);
            if (tempStr == null)
            {
                // invalid pinyin
                throw new IllegalArgumentException("invalid single pinyin string: " + pinyin);
            }   
            // look for the substitutable character delimiter.  
            pat = Pattern.compile("//.:?.//");
            m = pat.matcher(tempStr);
            StringBuffer sb = new StringBuffer();
            if (m.find())
            {
                String subString = tempStr.substring(m.start(), m.end());
                sb.append(tempStr.substring(0, m.start()));
                sb.append((String)mMap2.get(subString));
                sb.append(tempStr.substring(m.end()));
            }
            
            unicode.append(startSyllable);
            unicode.append(sb);
            if (endsWithR)
                unicode.append('r');
        }
        else {
        	// No pinyin found.  Could be special character, so just return our string
        	return pinyin;
        }
        
        return unicode.toString();
    }
    
    /**
     * A string of asterisks (*) of length i
	 * @param maskLength the number of asterisks
	 * @return a string of asterisks
	 */
	private static String getMask(int maskLength) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < maskLength; i++)
		{
			sb.append("*");
		}
		return sb.toString();
	}
}
