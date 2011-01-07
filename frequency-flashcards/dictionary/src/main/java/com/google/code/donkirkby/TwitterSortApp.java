package com.google.code.donkirkby;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import twitter4j.TwitterException;

public class TwitterSortApp {
	private static Log log = LogFactory.getLog(TwitterSortApp.class);

	private static String stateFile = "output/tweets/sortState.txt";
	
	private String searchUrl;
	private RankFinder rankFinder;
	private CharacterClassifier characterClassifier;
	private BufferedReader consoleReader = 
		new BufferedReader(new InputStreamReader(System.in));
	private PrintWriter consoleWriter = new PrintWriter(System.out);

	
    public static void main( String[] args )
    {
		log.info("Starting.");
		TwitterSortApp app = null;

		try {
			ClassPathXmlApplicationContext springContext = 
				new ClassPathXmlApplicationContext("spring.xml");
			
			app = (TwitterSortApp) springContext.getBean(
					"twitterSortApp", 
					TwitterSortApp.class);

			app.sortTweets();
			log.info("Success");
		} catch (Exception e) {
			String msg = "Failure";
			log.error(msg, e);
			System.exit(-1);
		}
    }
	
    public void sortTweets() throws IOException, TwitterException
    {
		try {
			int[] sortState = readSortState();
			int fileIndex = sortState[0];
			int tweetsToSkip = sortState[1];
			do
			{
				tweetsToSkip = sortFile(fileIndex, tweetsToSkip);
				if (tweetsToSkip == 0) {
					fileIndex++;
				}
			} while (tweetsToSkip == 0);
			writeSortState(fileIndex, tweetsToSkip);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
    }

	private void writeSortState(int fileIndex, int tweetsToSkip) {
		try
		{
			PrintWriter writer = new PrintWriter(new FileWriter(stateFile));
			try
			{
				writer.println(fileIndex);
				writer.println(tweetsToSkip);
			}
			finally
			{
				writer.close();
			}
		}
		catch (IOException ex)
		{
			throw new RuntimeException(ex);
		}
		
	}

	// print out any "good" tweets in the requested file, skipping the first
	// few tweets in the file (good or bad).
	// return 0 if we should continue to the next file, or the number of tweets
	// we've scanned so far in this file if it's time to quit. return -1 if
	// the file doesn't exist.
	private int sortFile(int fileIndex, int tweetsToSkip)
			throws XMLStreamException, FactoryConfigurationError,
			FileNotFoundException, IOException {
		String encoding = "UTF-8";
		String filename = "output/tweets/tweets" + fileIndex + ".xml";
		if ( ! new File(filename).exists()) {
			return -1;
		}
		XMLStreamReader xmlReader = 
			XMLInputFactory.newInstance().createXMLStreamReader(
				new FileInputStream(
						filename), 
						encoding);
		try
		{
			String url = null;
			StringBuilder textBuilder = null;
			int tweetCount = 0;
			while (xmlReader.hasNext())
			{
				int event = xmlReader.next();
				if (event == XMLStreamReader.START_ELEMENT && 
						xmlReader.getName().getLocalPart().equals("tweet"))
				{
					url = xmlReader.getAttributeValue(null, "url");
					textBuilder = new StringBuilder();
				}
				if (event == XMLStreamReader.CHARACTERS && textBuilder != null)
				{
					textBuilder.append(xmlReader.getText());
				}
				if (event == XMLStreamReader.END_ELEMENT && textBuilder != null)
				{
					tweetCount++;
					if (tweetCount > tweetsToSkip)
					{
			    		String text = textBuilder.toString();
						int rank = maxRank(text);
						if (0 < rank && rank < 500)
						{
							System.out.println(
									"|| " + rank + " || [" + url + " ] || " + 
									text + " ||  ||");
							consoleWriter.println("[C]ontinue or (q)uit?");
							consoleWriter.flush();
							String line = consoleReader.readLine();
							if (line == null ||
									(line.length() != 0 && line.equalsIgnoreCase("Q")))
							{
								// User wants to quit.
								return tweetCount;
							}
						}
					}
					textBuilder = null;
				}
			}
		}
		finally
		{
			xmlReader.close();
		}
		return 0; // next file, please.
	}

	// returns array with fileIndex, tweetsToSkip.
    private int[] readSortState() {
    	try {
    		int[] result = new int[] {0, 0};
    		if ( ! new File(stateFile).exists()) {
    			return result;
    		}
			BufferedReader reader = new BufferedReader(new FileReader(stateFile));
			try
			{
				result[0] = Integer.parseInt(reader.readLine());
				result[1] = Integer.parseInt(reader.readLine());
				return result;
			}
			finally
			{
				reader.close();
			}
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		} catch (NumberFormatException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private int maxRank(String text) {
    	int maxRank = 0;
    	for(char c: text.toCharArray())
    	{
    		if (characterClassifier.isChinese(c))
    		{
	    		int rank = rankFinder.getCharacterRank(String.valueOf(c));
	    		if (rank > maxRank)
	    		{
	    			maxRank = rank;
	    		}
    		}
    		else if (characterClassifier.isJapanese(c))
    		{
    			maxRank = Integer.MAX_VALUE;
    		}
    	}
		return maxRank;
	}

	public String getSearchUrl() {
		return searchUrl;
	}

	public void setSearchUrl(String searchUrl) {
		this.searchUrl = searchUrl;
	}

	public void setRankFinder(RankFinder rankFinder) {
		this.rankFinder = rankFinder;
	}

	public RankFinder getRankFinder() {
		return rankFinder;
	}

	public void setCharacterClassifier(CharacterClassifier characterClassifier) {
		this.characterClassifier = characterClassifier;
	}

	public CharacterClassifier getCharacterClassifier() {
		return characterClassifier;
	}
}
