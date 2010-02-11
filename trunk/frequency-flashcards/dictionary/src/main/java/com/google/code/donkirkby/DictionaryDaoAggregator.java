package com.google.code.donkirkby;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DictionaryDaoAggregator implements DictionaryDao {
	List<DictionaryDao> sources;

	/* (non-Javadoc)
	 * @see com.google.code.donkirkby.DictionaryDao#findAllEntryValuesByTraditionalCharacter(java.lang.String, int)
	 */
	@Override
	public EntryValue[] findAllEntryValuesByTraditionalCharacter(
			String character, int maxResults) {
		/* return the entries sorted by traditional characters. */
		Comparator<EntryValue> comparator = new Comparator<EntryValue>() {
			@Override
			public int compare(EntryValue entry1, EntryValue entry2) {
				String traditional1 = entry1.getTraditionalChars();
				String traditional2 = entry2.getTraditionalChars();
				String pinyin1 = entry1.getPinyin();
				String pinyin2 = entry2.getPinyin();
				int result = traditional1.compareTo(traditional2);
				if (result == 0)
				{
					result = pinyin1.compareToIgnoreCase(pinyin2);
				}
				return result;
			}
			
		};
		List<EntryValue> results = new ArrayList<EntryValue>();
		for (DictionaryDao source : sources) {
			EntryValue[] rawResult = 
				source.findAllEntryValuesByTraditionalCharacter(
						character, 
						maxResults);
			for (EntryValue entry : rawResult) {
				int index = Collections.binarySearch(results, entry, comparator);
				if (index < 0)
				{
					// not in the list, so insert
					results.add(-index - 1, entry);
				}
			}
		}
		
		EntryValue[] resultArray = new EntryValue[results.size()];
		return results.toArray(resultArray);
	}

	public List<DictionaryDao> getSources() {
		return sources;
	}

	public void setSources(List<DictionaryDao> sources) {
		this.sources = sources;
	}

}
