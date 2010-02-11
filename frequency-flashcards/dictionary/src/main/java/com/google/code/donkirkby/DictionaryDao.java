package com.google.code.donkirkby;

public interface DictionaryDao {

	public abstract EntryValue[] findAllEntryValuesByTraditionalCharacter(
			String character, int maxResults);

}