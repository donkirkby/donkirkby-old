package com.google.code.donkirkby;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.regex.Pattern;

import org.springframework.core.io.Resource;

public class DefaultRankReader implements RankReader {
	private Resource resource;
	private Scanner scanner;
	private int headerLineCount;
	private int linesToSkip;
	private String nextItem;
	private Pattern tokenPattern;
	private Pattern delimiterPattern;
	private boolean isParsingTokens = true;
	private boolean isOnePerLine = true;
	private int minTokenLength = 1;
	
	public DefaultRankReader() {
		setPattern();
	}

	private void setPattern() {
		if (isParsingTokens)
		{
			tokenPattern = Pattern.compile("\\S+");
			delimiterPattern = Pattern.compile("\\s+");
		}else
		{
			tokenPattern = Pattern.compile("\\S");
			delimiterPattern = Pattern.compile("\\s*");
		}
		if (scanner != null)
		{
			scanner.useDelimiter(delimiterPattern);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.google.code.donkirkby.RankReader#nextItem()
	 */
	public String nextItem() {
		if (!hasNext())
		{
			throw new NoSuchElementException();
		}
		String currentItem = nextItem;
		prepareNextItem();
		return currentItem;
	}

	private void prepareNextItem() {
		nextItem = null;
		boolean isAtFileEnd = false;
		do {
			while(linesToSkip > 0 && scanner.hasNextLine())
			{
				scanner.nextLine();
				linesToSkip--;
			}
			if (scanner.hasNext(tokenPattern))
			{
				nextItem = scanner.next(tokenPattern);
				if (nextItem.length() < minTokenLength)
				{
					nextItem = null;
				}
			}
			
			if (isOnePerLine) {
				linesToSkip = 1;
				isAtFileEnd = !scanner.hasNextLine();
			}else
			{
				isAtFileEnd = !scanner.hasNext(tokenPattern);
			}
		}while (nextItem == null && !isAtFileEnd);
	}
	
	/* (non-Javadoc)
	 * @see com.google.code.donkirkby.RankReader#open()
	 */
	public void open()
	{
		try {
			InputStream inputStream = resource.getInputStream();
			InputStreamReader reader = 
				new InputStreamReader(inputStream, "utf8");
			scanner = new Scanner(reader);
			scanner.useDelimiter(delimiterPattern);
			linesToSkip = headerLineCount;
			prepareNextItem();
		} catch (IOException e) {
			throw new RuntimeException(
					"Failed to open frequency resource.", 
					e);
		}
	}

	/* (non-Javadoc)
	 * @see com.google.code.donkirkby.RankReader#close()
	 */
	public void close() {
		if (scanner != null) {
			scanner.close();
			scanner = null;
		}
	}

	public Resource getResource() {
		return resource;
	}

	public void setResource(Resource resource) {
		this.resource = resource;
	}

	public int getHeaderLineCount() {
		return headerLineCount;
	}

	public void setHeaderLineCount(int headerLineCount) {
		this.headerLineCount = headerLineCount;
	}

	/* (non-Javadoc)
	 * @see com.google.code.donkirkby.RankReader#hasNext()
	 */
	public boolean hasNext() {
		return nextItem != null;
	}

	public boolean isParsingTokens() {
		return isParsingTokens;
	}

	/** Sets whether the reader parses complete words separated by white space or reads
	 * individual characters. Defaults to true.
	 * @param isParsingTokens true if it should parse complete words.
	 */
	public void setParsingTokens(boolean isParsingTokens) {
		this.isParsingTokens = isParsingTokens;
		setPattern();
	}

	public boolean isOnePerLine() {
		return isOnePerLine;
	}

	/**Sets whether the reader only reads the first token from each line.
	 * @param isOnePerLine
	 */
	public void setOnePerLine(boolean isOnePerLine) {
		this.isOnePerLine = isOnePerLine;
	}

	public int getMinTokenLength() {
		return minTokenLength;
	}

	public void setMinTokenLength(int minTokenLength) {
		this.minTokenLength = minTokenLength;
	}
}
