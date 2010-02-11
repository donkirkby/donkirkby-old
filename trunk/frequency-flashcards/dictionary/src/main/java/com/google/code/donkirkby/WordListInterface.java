package com.google.code.donkirkby;

import java.util.Iterator;

public interface WordListInterface {

	public abstract Iterator<String> iterator(final int maxCharacterRank);

}