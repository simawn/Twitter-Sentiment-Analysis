import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.io.*;
import java.text.SimpleDateFormat;
import common.*;

public class Driver {

	private static final String[] SENTIMENT_NAME = {"Very Negative", "Negative", "Neutral", "Positive", "Very Positive"};
	
	private static int[] tally = {0,0,0,0,0};
    
	private static Date date;
	private static String dateString;
	
	private static long searchRuntime;
	private static long analyseRuntime;
	
	private final static String EXTENSION = ".log";
	
	public static void main(String[] args) {
    	
    	TweetManager.setMaxTweets(100); //Set the maximum tweets to analyse
    	System.out.println("Enter a topic to analyse:");
    	Scanner keyboard = new Scanner(System.in);
        String topic = keyboard.nextLine();
        
        //Track date
        date = new Date(); //For console output
        dateString = new SimpleDateFormat("MMM'-'dd'-'yyyy'_at_'HH'_'mm'_'ss'_'zzz").format(date); //For file output
        
        ArrayList<String> tweets = null;
        
        long searchEndTime;
        long searchStartTime = System.currentTimeMillis(); //Start search timer
        
        try {
			tweets = TweetManager.getTweets(topic); // Query to retrieve tweets
			if (tweets.isEmpty()) {
				throw new NoResultException();
			}
		} catch (NoResultException e) {
			System.err.println(e.getMessage());
			System.exit(1);
		} finally {
			keyboard.close();
			searchEndTime = System.currentTimeMillis(); //End search timer
		}
        searchRuntime = searchEndTime - searchStartTime;
        
        long analyseEndTime;
        long analyseStartTime = System.currentTimeMillis(); //Start analyse timer
        
        NLP.init(); //Initialize NLP
        
        //keeping track of tweet #
        int count = 1;
        
        //Keeping track of score array
        ArrayList<Integer> score = new ArrayList<>();
        
        //Print out tweets + score
		for (String tweet : tweets) {
			int sentimentScore = NLP.findSentiment(tweet); //Analyse sentiment
			score.add(sentimentScore); //Adding score to array list
			System.out.println("[" + count + "] " + "::" + sentimentScore + ":: " + tweet);
			System.out.println();
			if(sentimentScore >= 0 && sentimentScore <= 4){ //Making sure that score is in range [0,4]. Avoids ArrayOutOfBounds.
				tally[sentimentScore]++;
			}
			count++;
		}
		
		analyseEndTime = System.currentTimeMillis(); //End analyse timer
		analyseRuntime = analyseEndTime - analyseStartTime;
		
		int total = 0;
		for(int i = 0; i < tally.length; i++){
			total += tally[i];
		}
		
		double amount = 0;
		for(int i = 0; i < tally.length; i++){
			amount += i*tally[i];
		}
		
		double average = amount/(double)total;
		
		System.out.println("============================================");
		System.out.println("Results for " + topic);
		System.out.println("============================================");
		for(int i = 0; i < tally.length; i++){
			System.out.println(SENTIMENT_NAME[i] + ": " + tally[i]);
		}
		System.out.println();
		System.out.println("TOTAL: " + total);
		System.out.println("AVERAGE SCORE: " + average + " (" + SENTIMENT_NAME[(int) Math.round(average)] + ")");
		
		
		System.out.println();
		System.out.println("Generated on: " + date);
		System.out.println("Search time: " + searchRuntime + "ms");
		System.out.println("Analyse time: " + analyseRuntime + "ms");
		System.out.println("Total time: " + (searchRuntime + analyseRuntime) + "ms");
		writeFile(tweets, score, topic, tally[0], tally[1], tally[2], tally[3], tally[4], total, average);
    }
	
	/**
	 * Write results to a file
	 * @param tweets ArrayList of tweets
	 * @param score ArrayList of scores
	 * @param topic Specified search topic
	 * @param _0 # of 0's
	 * @param _1 # of 1's
	 * @param _2 # of 2's
	 * @param _3 # of 3's
	 * @param _4 # of 4's
	 * @param total Total amount of tweets retrieved
	 * @param average Average score of all tweets
	 */
	private static void writeFile(ArrayList<String> tweets, ArrayList<Integer> score, String topic, int _0, int _1, int _2, int _3, int _4, int total, double average){
		System.out.println("WRITING FILE");
		//TODO trim topic illegal characters for file out
		PrintWriter fileOutput = null;
		try{
			fileOutput = new PrintWriter(new FileOutputStream(topic + "_" + dateString + EXTENSION, true));
			fileOutput.println("Generated on: " + date);
			fileOutput.println("============================================");
			fileOutput.println("Results for " + topic);
			fileOutput.println("============================================");
			for(int i = 0; i < tally.length; i++){
				fileOutput.println(SENTIMENT_NAME[i] + ": " + tally[i]);
			}
			fileOutput.println();
			fileOutput.println("TOTAL: " + total);
			fileOutput.println("AVERAGE SCORE: " + average + " (" + SENTIMENT_NAME[(int) Math.round(average)] + ")");
			fileOutput.println();
			fileOutput.println("Contents:");
			for(int i = 0; i < tweets.size(); i++){
				fileOutput.println("[" + (i+1) + "] ::" + score.get(i) + ":: " + tweets.get(i));
				fileOutput.println();
			}
			System.out.println("FILE WRITTEN");
		} catch(IOException e){
			System.out.println(e);
			System.exit(0);
		} finally {
			fileOutput.close();
		}
	}
}