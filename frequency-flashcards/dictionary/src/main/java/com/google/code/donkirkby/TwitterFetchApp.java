package com.google.code.donkirkby;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashSet;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import twitter4j.RateLimitStatus;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

public class TwitterFetchApp {
	private static Log log = LogFactory.getLog(TwitterFetchApp.class);
	private static final String END_OF_INPUT = "\\Z";

	private String searchUrl;
	private RankFinder rankFinder;
	private RankReader characterReader;

    public static void main( String[] args )
    {
		log.info("Starting.");
		TwitterFetchApp app = null;

		try {
			ClassPathXmlApplicationContext springContext = 
				new ClassPathXmlApplicationContext("spring.xml");
			
			app = (TwitterFetchApp) springContext.getBean(
					"twitterFetchApp", 
					TwitterFetchApp.class);

			app.fetchTweets();
			log.info("Success");
		} catch (Exception e) {
			String msg = "Failure";
			log.error(msg, e);
			System.exit(-1);
		}
    }
	
    public void fetchTweets() throws IOException, TwitterException
    {
    	String tweetIdsFilename = "output/tweets/tweetIds.txt";
    	StringBuilder startTarget = new StringBuilder();
    	HashSet<Long> fetchedTweetIds = readOldTweetIds(tweetIdsFilename, startTarget);
    	
    	String lastTarget = fetchTweets(fetchedTweetIds, startTarget.toString());
		
    	
		writeTweetIds(fetchedTweetIds, lastTarget, tweetIdsFilename);
    }

	private HashSet<Long> readOldTweetIds(String tweetIdsFilename, StringBuilder startTarget)
			throws FileNotFoundException, IOException {
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

	private void writeTweetIds(
			HashSet<Long> fetchedTweetIds,
			String lastTarget,
			String tweetIdsFilename) throws FileNotFoundException {
		FileOutputStream outStream = new FileOutputStream(tweetIdsFilename);
    	PrintWriter printWriter = new PrintWriter(outStream);
		try 
		{
			printWriter.println(lastTarget);
			for (Long tweetId: fetchedTweetIds)
			{
				printWriter.println(tweetId);
			}
		}
		finally
		{
			printWriter.close();
		}
	}

	// returns current search target when limit was reached.
	private String fetchTweets(HashSet<Long> fetchedTweetIds, String startTarget)
			throws FactoryConfigurationError {
		OutputStreamWriter fileWriter;
    	XMLStreamWriter xmlWriter;
    	String target = "";
    	String encoding = "UTF-8";
		try {
			Twitter twitter = new TwitterFactory().getInstance();
			RateLimitStatus limitStatus = twitter.getRateLimitStatus();
			int remainingHits = limitStatus.getRemainingHits();
			int secondsUntilReset = limitStatus.getSecondsUntilReset();
			if (remainingHits == 0)
			{
				log.error(
						"Limit reached. Resets in " + 
						secondsUntilReset / 60 + " minutes");
				return startTarget;
			}
			
			FileOutputStream outStream = new FileOutputStream(chooseFilename());
			fileWriter = new OutputStreamWriter(outStream, encoding);
			XMLOutputFactory factory = XMLOutputFactory.newInstance();
			xmlWriter = factory.createXMLStreamWriter(fileWriter);
			try
			{
				xmlWriter.writeStartDocument(encoding, "1.0");
				xmlWriter.writeStartElement("tweets");
		    	//"url":"http://twitter.com/kaifulee/status/4695878440"
		    	Pattern pattern = Pattern.compile("\"url\":\"([^\"]*/status/(\\d*))\"");
		    	characterReader.open();
				try
				{
					while ( ! startTarget.equals(target))
					{
						target = characterReader.nextItem(); 
					}
					boolean isLimitReached = false;
					while ( ! isLimitReached)
					{
						isLimitReached = fetchTweetsForTarget(
								fetchedTweetIds, 
								xmlWriter,
								pattern, 
								twitter, 
								target);
						if ( ! isLimitReached)
						{
							target = characterReader.nextItem();
						}
					}
				}finally
				{
					characterReader.close();
				}
		    	xmlWriter.writeEndElement();
			}
			finally
			{
				xmlWriter.close();
				fileWriter.close();
			}
			return target;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private String chooseFilename() 
	{
		int i = 0;
		while (true)
		{
			String filename = "output/tweets/tweets" + i + ".xml";
			if ( ! new File(filename).exists())
			{
				log.info("Writing to file " + filename);
				return filename;
			}
			i++;
		}
	}

    // return true if a Twitter or Google limit has been reached.
	private boolean fetchTweetsForTarget(
			HashSet<Long> fetchedTweetIds,
			XMLStreamWriter xmlWriter, 
			Pattern pattern, 
			Twitter twitter,
			String target) throws IOException,
			XMLStreamException 
	{
		int remainingHits;
		int secondsUntilReset;
		try
		{
			RateLimitStatus limitStatus = twitter.getRateLimitStatus();
			remainingHits = limitStatus.getRemainingHits();
			secondsUntilReset = limitStatus.getSecondsUntilReset();
		}
		catch (TwitterException ex)
		{
			throw new RuntimeException(ex);
		}
		int totalResultCount = 0;
		for (int i = 0; i < 8 && remainingHits > 0; i++)
		{
			String result = getSearchPage(totalResultCount, target);
			Matcher matcher = pattern.matcher(result);
			int resultCount = 0;
			
			while (matcher.find() && remainingHits > 0) 
			{
				long statusId = Long.decode(matcher.group(2));
				if ( ! fetchedTweetIds.contains(statusId))
				{
					String statusUrl = matcher.group(1);
					fetchedTweetIds.add(statusId);
					String text;
					try {
						Status status = twitter.showStatus(statusId);
						text = status.getText();
						xmlWriter.writeStartElement("tweet");
						xmlWriter.writeAttribute("id", Long.toString(statusId));
						xmlWriter.writeAttribute("url", statusUrl);
						//write id and maybe url
						xmlWriter.writeCharacters(text);
						xmlWriter.writeEndElement();
						xmlWriter.flush();
						remainingHits = 
							status.getRateLimitStatus().getRemainingHits();
					} catch (TwitterException e) {
						RateLimitStatus limitStatus = e.getRateLimitStatus();
						remainingHits = 
							limitStatus == null
							? remainingHits
							: limitStatus.getRemainingHits();
						if (e.getStatusCode() == Twitter.NOT_FOUND ||
								e.getStatusCode() == Twitter.FORBIDDEN)
						{
							log.warn(
									"Unable to retrieve " + statusUrl +
									" response code " + e.getStatusCode());
						}
						else if (e.getStatusCode() == Twitter.BAD_REQUEST)
						{
							if (remainingHits != 0)
							{
								throw new RuntimeException(e);
							}
						}
					}
				}
				resultCount++;
			}
			totalResultCount += resultCount;
			if (resultCount == 0)
			{
				log.error("After totalResultCount " + totalResultCount + result);
				return true;
			}
		}
		if (remainingHits == 0)
		{
			log.error(
					"Limit reached. Resets in " + 
					secondsUntilReset / 60 + " minutes");
			return true;
		}
		log.info(
				"Fetch total is " + fetchedTweetIds.size() + " after target " + 
				target + " with " + remainingHits + " remaining Twitter hits.");
		return false;
	}

	private String getSearchPage(int start, String target)
    {
		//http://www.javapractices.com/topic/TopicAction.do?Id=147
		URL url;
		try {
			String encodedTarget = URLEncoder.encode(target, "UTF-8");
			url = new URL(
					searchUrl + "&start=" + start + 
					"&q=site:twitter.com%20inurl:status%20" + encodedTarget);
			String result = null;
			URLConnection connection = null;
			connection = url.openConnection();
			Scanner scanner = new Scanner(connection.getInputStream());
			scanner.useDelimiter(END_OF_INPUT);
			result = scanner.next();
			Thread.sleep(10000); // throttle requests to avoid getting blocked.
			return result;
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
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

	public RankReader getCharacterReader() {
		return characterReader;
	}

	public void setCharacterReader(RankReader characterReader) {
		this.characterReader = characterReader;
	}
	
}
