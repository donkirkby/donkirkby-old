package com.google.code.donkirkby;

import java.util.Dictionary;
import java.util.Hashtable;

public class CustomDictionaryDao implements DictionaryDao {
	private static Dictionary<String, EntryValue> customEntries =
		new Hashtable<String, EntryValue>();
	static
	{
		EntryValue entry = new EntryValue();
		entry.setTraditionalChars("變");
		entry.setSimplifiedChars("变");
		entry.setPinyin("biàn");
		entry.setDefinition("to change; to become different; to transform; to vary; rebellion");
		addEntry(entry);
		
		entry = new EntryValue();
		entry.setTraditionalChars("裡");
		entry.setSimplifiedChars("里");
		entry.setPinyin("lǐ");
		entry.setDefinition("village; within; inside; internal; interior");
		addEntry(entry);
		
		entry = new EntryValue();
		entry.setTraditionalChars("算");
		entry.setSimplifiedChars("算");
		entry.setPinyin("suàn");
		entry.setDefinition("regard as; to figure; to calculate; to compute");
		addEntry(entry);
	}

	private static void addEntry(EntryValue entry) {
		customEntries.put(entry.getTraditionalChars(), entry);
	}

	@Override
	public EntryValue[] findAllEntryValuesByTraditionalCharacter(
			String character, int maxResults) {
		EntryValue entry = customEntries.get(character);
		if (entry == null)
		{
			return new EntryValue[0];
		}
		return new EntryValue[] { entry };
	}

}
