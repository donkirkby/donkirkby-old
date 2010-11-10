package com.google.code.donkirkby;

import java.lang.Character.UnicodeBlock;
import java.util.HashSet;
import java.util.Set;

public class CharacterClassifier {

	public boolean isChinese(char c) {
		Set<UnicodeBlock> chineseUnicodeBlocks = new HashSet<UnicodeBlock>();
		chineseUnicodeBlocks.add(UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS);
//			add(UnicodeBlock.CJK_COMPATIBILITY);
//			add(UnicodeBlock.CJK_COMPATIBILITY_FORMS);
//			add(UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS);
//			add(UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS_SUPPLEMENT);
//			add(UnicodeBlock.CJK_RADICALS_SUPPLEMENT);
//			add(UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION);
//			add(UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS);
//			add(UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A);
//			add(UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B);
//			add(UnicodeBlock.KANGXI_RADICALS);
//			add(UnicodeBlock.IDEOGRAPHIC_DESCRIPTION_CHARACTERS);
		
		return chineseUnicodeBlocks.contains(UnicodeBlock.of(c));
	}
}
