package com.google.code.donkirkby;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashSet;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

public class TwitterSortApp {
	private static Log log = LogFactory.getLog(TwitterSortApp.class);
	private static final String END_OF_INPUT = "\\Z";

	private String searchUrl;
	private RankFinder rankFinder;
	private CharacterClassifier characterClassifier;
	
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
    	XMLStreamReader xmlReader;
    	XMLStreamWriter xmlWriter;
    	String encoding = "UTF-8";
		try {
			xmlReader = XMLInputFactory.newInstance().createXMLStreamReader(
					new FileInputStream("output/tweets/tweets4.xml"), encoding);
			xmlWriter = XMLOutputFactory.newInstance().createXMLStreamWriter(
					new FileOutputStream("output/tweets/sortedTweets4.xml"), encoding);
			try
			{
				xmlWriter.writeStartDocument(encoding, "1.0");
				xmlWriter.writeStartElement("tweets");
				
				String statusId = null;
				StringBuilder textBuilder = null;
				while (xmlReader.hasNext())
				{
					int event = xmlReader.next();
					if (event == XMLStreamReader.START_ELEMENT && 
							xmlReader.getName().getLocalPart().equals("tweet"))
					{
						statusId = xmlReader.getAttributeValue(null, "id");
						textBuilder = new StringBuilder();
					}
					if (event == XMLStreamReader.CHARACTERS && textBuilder != null)
					{
						textBuilder.append(xmlReader.getText());
					}
					if (event == XMLStreamReader.END_ELEMENT && textBuilder != null)
					{
		        		String text = textBuilder.toString();
						int rank = maxRank(text);
						if (rank < 500)
						{
							xmlWriter.writeStartElement("tweet");
							xmlWriter.writeAttribute("id", statusId);
							xmlWriter.writeAttribute("rank", Integer.toString(rank));
							xmlWriter.writeCharacters(text);
							xmlWriter.writeEndElement();
						}
						statusId = null;
						textBuilder = null;
					}
				}
				/*
		    	//"url":"http://twitter.com/kaifulee/status/4695878440"
		    	Twitter twitter = new TwitterFactory().getInstance();
		    	String targets = "以二三四五十世是";
		    	for (char target: targets.toCharArray())
		    	{
			    	int totalResultCount = 0;
			    	for (int i = 0; i < 8; i++)
			    	{
				    	String result = getSearchPage(totalResultCount, target);
				        Matcher matcher = pattern.matcher(result);
				        int resultCount = 0;
			
			        	while (matcher.find()) 
			        	{
			        		long statusId = Long.decode(matcher.group(1));
			        		String text = twitter.showStatus(statusId).getText();
			        		int rank = maxRank(text);
			        		xmlWriter.writeStartElement("tweet");
			        		xmlWriter.writeAttribute("rank", Integer.toString(rank));
			        		//write id and maybe url
			        		xmlWriter.writeCharacters(text);
			        		xmlWriter.writeEndElement();
			        		//log.info("found status with rank " + rank + ": " + statusId + ": " + text);
			        		resultCount++;
			        	}
			        	if (resultCount == 0)
			        	{
			        		log.error("After totalResultCount " + totalResultCount + result);
			        	}
			        	totalResultCount += resultCount;
			        }
		    	}*/
		    	xmlWriter.writeEndElement();
			}
			finally
			{
				xmlWriter.close();
			}
		} catch (Exception e) {
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
    	}
		return maxRank;
	}

	private String getSearchPage(int start, char target) throws IOException
    {
		//http://www.javapractices.com/topic/TopicAction.do?Id=147
		URL url = new URL(
				searchUrl + "&start=" + start + 
				"&q=site:twitter.com%20inurl:status%20" + target);
		String result = null;
		URLConnection connection = null;
		connection = url.openConnection();
		Scanner scanner = new Scanner(connection.getInputStream());
		scanner.useDelimiter(END_OF_INPUT);
		result = scanner.next();
		return result;
    }

	private HashSet<Long> readTweets(
			String tweetIdsFilename, 
			StringBuilder startTarget)
	throws FileNotFoundException, IOException 
	{
		HashSet<Long> fetchedTweetIds = new HashSet<Long>();
		File tweetIdsFile = new File(tweetIdsFilename);
		if ( ! tweetIdsFile.exists())
		{
			return fetchedTweetIds;
		}
		FileInputStream stream = new FileInputStream(tweetIdsFilename);
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		try
		{
			startTarget.append(reader.readLine());
			String line;
			do
			{
				line = reader.readLine();
				if (line != null)
				{
					fetchedTweetIds.add(Long.decode(line));
				}
			}while (line != null);
		}
		finally
		{
			reader.close();
		}
		return fetchedTweetIds;
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
