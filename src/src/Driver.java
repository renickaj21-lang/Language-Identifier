package src;

import java.text.Normalizer;
import java.util.Locale;
import java.util.Scanner;

public class Driver {
	private static final String VOWELS =
	        "aeiouy"          // basic Latin (y is a vowel in Czech, Polish, Finnish, Turkish, etc.)
	      + "àáâãäæ"
	      + "èéêëěęėē"
	      + "ìíîïıįī"
	      + "òóôõöőœ"
	      + "ùúûüűůųū"
	      + "ýÿ"
	      + "ąăā";
	
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter at least a few decently long sentences, or the identifier will likely fail.");
		System.out.print("Enter some text: ");
		
		String input = "";
		boolean validInput = false;
		while(!validInput) {
			input = scanner.nextLine();
			String lower = Normalizer.normalize(input, Normalizer.Form.NFC).toLowerCase(Locale.ROOT);
		
			//checking for adequate frequency of spaces to be language
			//Some languages do not use spaces between words so the check will be ignored in that case
			if(!nonSpaceScript(input)) {
				if (lower.isEmpty() ||
						((double) Identifier.instancesOf(input, " ") / input.length()) < 0.12) {
					System.out.println("Are you sure that is language? (If so, writing more should fix the problem.)");
					System.out.print("Enter some text:");
				}else {
					validInput = true;
				}
			}else {
				validInput = true;
			}
		}
		
		String output = Identifier.identifyLanguage(input);
		System.out.println("That is " + output);
		
		scanner.close();
	}
	
	/*
	 * returns true if the script is one that does not use spaces. false if it does
	 */
	public static boolean nonSpaceScript(String input) {
		if(Identifier.identifyLanguage(input).equals("Thai") || Identifier.identifyLanguage(input).equals("Burmese") ||
				Identifier.identifyLanguage(input).equals("Lao")) {
			return true;
		}
		return false;
	}
}
