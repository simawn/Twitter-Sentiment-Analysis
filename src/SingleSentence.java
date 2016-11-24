import java.util.Scanner;

public class SingleSentence {
	public static void main(String args[]){
		System.out.println("Enter a sentence:");
		Scanner input = new Scanner(System.in);
		String sentence = input.nextLine();
		NLP.init();
		System.out.println("Score: " + NLP.findSentiment(sentence));
		input.close();
	}
}
