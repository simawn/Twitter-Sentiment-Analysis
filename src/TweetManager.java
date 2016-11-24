import java.util.ArrayList;
import java.util.List;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

public class TweetManager {
//TODO: Do not use static?
	private static int maxTweets = 10; //Max tweets to analyze. Default: 10
	
	/**
	 * Retrive tweets about a certain topic
	 * @param topic Topic to search
	 * @return ArrayList of tweets with the topic
	 */
    public static ArrayList<String> getTweets(String topic) {

        Twitter twitter = new TwitterFactory().getInstance();
        ArrayList<String> tweetList = new ArrayList<String>();
        try {
        	//Modify filters below when needed
            Query query = new Query(topic + " -filter:retweets -filter:links -filter:replies -filter:images").lang("en");
            QueryResult result;
			query.count(maxTweets); // Return # of tweet
			result = twitter.search(query);
			List<Status> tweets = result.getTweets();
			for (Status tweet : tweets) {
				tweetList.add(tweet.getText());
			}
        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to search tweets: " + te.getMessage());
        }
        return tweetList;
    }
    
    /**
     * Method to set the maximum tweet retrieval
     * @param max Maximum tweet to return
     */
    public static void setMaxTweets(int max){
    	maxTweets = max;
    }
}