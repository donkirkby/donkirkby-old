package com.google.code.donkirkby;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
import java.net.*;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.Twitter;

public class TwitterApp {
	private static Log log = LogFactory.getLog(TwitterApp.class);
	private static final String END_OF_INPUT = "\\Z";

	private String searchUrl;
	
    public static void main( String[] args )
    {
		log.info("Starting.");
		TwitterApp app = null;

		try {
			ClassPathXmlApplicationContext springContext = 
				new ClassPathXmlApplicationContext("spring.xml");
			
			app = (TwitterApp) springContext.getBean(
					"twitterApp", 
					TwitterApp.class);

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
    	//"url":"http://twitter.com/kaifulee/status/4695878440"
    	Pattern pattern = Pattern.compile("\"url\":\"[^\"]*/status/(\\d*)\"");
    	Twitter twitter = new TwitterFactory().getInstance();
    	int resultCount = 0;
    	for (int i = 0; i < 4; i++)
    	{
	    	String result = getSearchPage(resultCount);
	        Matcher matcher = pattern.matcher(result);

        	while (matcher.find()) 
        	{
        		long statusId = Long.decode(matcher.group(1));
        		String text = twitter.showStatus(statusId).getText();
        		log.info("found status: " + statusId + ": " + text);
        		resultCount++;
        	}
        	if (resultCount == 0)
        	{
        		log.error(result);
        	}
        }
    }

    private String getSearchPage(int start) throws IOException
    {
		//http://www.javapractices.com/topic/TopicAction.do?Id=147
		URL url = new URL(searchUrl + "&start=" + start);
		String result = null;
		URLConnection connection = null;
		connection = url.openConnection();
		Scanner scanner = new Scanner(connection.getInputStream());
		scanner.useDelimiter(END_OF_INPUT);
		result = scanner.next();
		return result;
    }
	public String getSearchUrl() {
		return searchUrl;
	}

	public void setSearchUrl(String searchUrl) {
		this.searchUrl = searchUrl;
	}
}
