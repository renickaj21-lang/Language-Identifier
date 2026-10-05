package src;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import java.text.Normalizer;
import java.util.Locale;

public class Identifier {
	
	public static int instancesOf(String text, String target) {
	    if (text == null || target == null || target.isEmpty()) {
	        return 0;
	    }

	    int count = 0;
	    for (int i = 0; i <= text.length() - target.length(); i++) {
	        if (text.startsWith(target, i)) {
	            count++;
	        }
	    }
	    return count;
	}
	
	public static String[] filterFor(String[] first, String[] second) {
	    if (first == null || second == null) {
	        return new String[0];
	    }

	    Set<String> lookup = new HashSet<>();
	    for (String s : second) {
	        lookup.add(s);
	    }

	    return Arrays.stream(first)
	                 .filter(lookup::contains)
	                 .toArray(String[]::new);
	}
	
	public static String identifyLanguage(String input) {
		//Not all languages can be returned from this list but it is good to have all for readability
		String[] languageList = {"English", "French", "Portuguese", "Spanish", 
				"Italian", "German", "Dutch", "Danish", "Swedish", "Norwegian", "Icelandic", 
				"Czech", "Polish", "Serbo-Croat", "Hungarian", "Albanian", "Romanian", "Finnish", "Estonian",
				"Lithuanian", "Latvian", "Turkish", "Azerbaijani", "Georgian", "Armenian",
				"Guarani", "Kurmanji Kurdish", "Ukrainian", "Russian", "Macedonian", "Serbian",
				"Vietnamese", "Greek", "Thai", "Lao", "Burmese", "Persian", "Arabic"}; 
		
		if(instancesOf(input, " ") == 0 || input.length() == 0)
			return "";
		
		input = Normalizer.normalize(input, Normalizer.Form.NFC);
		input = input.toLowerCase();
		
		/*
		 * Cyrillic languages
		 * Ukrainian, 
		 */
		
		//these letters are in ukrainian but not russian
		int ukrainianTotal = instancesOf(input, "і") + instancesOf(input, "ї") + 
				instancesOf(input, "є") + instancesOf(input, "ґ");
		if(ukrainianTotal > 0)
			return "Ukrainian";
		
		/*
		 * Serbian-unique letters
		 */
		if(instancesOf(input, "ђ") + instancesOf(input, "ћ") > 0)
			return "Serbian";
		
		/*
		 * Macedonian-unique letters
		 */
		if(instancesOf(input, "ѓ") + instancesOf(input, "ќ") + instancesOf(input, "ѕ") > 0)
			return "Macedonian";
		
		/*
		 * Serbian - Macedonian commonalities
		 */
		if(instancesOf(input, "ј") + instancesOf(input, "љ") + 
				instancesOf(input, "њ") + instancesOf(input, "џ") > 0) {
			String[] arr = {"Macedonian", "Serbian"};
		    languageList = filterFor(languageList, arr);
		}
		
		//hopefully it doesn't get here but Macedonian is more likely to
		//not be distinguishable for certain
		if(Arrays.asList(languageList).contains("Serbian") && Arrays.asList(languageList).contains("Macedonian") && 
				!Arrays.asList(languageList).contains("English"))
			return "Macedonian";
			
		
		/*
		 * This is tested last; if none of the above are detected, this one checks 
		 * for Cyrillic in general and gives Russian if it is
		 */
		int cyrillicTotal = instancesOf(input, "о") + instancesOf(input, "е") + 
				instancesOf(input, "а") + instancesOf(input, "и") + instancesOf(input, "н") + 
				instancesOf(input, "т") + instancesOf(input, "с");
		if(cyrillicTotal > 0)
			return "Russian";
		
		
		/*
		 * For georgian and armenian, calling the most common seven
		 */
		
		int georgianTotal = instancesOf(input, "ა") + instancesOf(input, "ი") +
		instancesOf(input, "ე") + instancesOf(input, "ო") + instancesOf(input, "ნ")+
		instancesOf(input, "რ") + instancesOf(input, "ს");
		if(georgianTotal > 0)
			return "Georgian";
		
		int armenianTotal = instancesOf(input, "ա") + instancesOf(input, "ե") +
		instancesOf(input, "ն") + instancesOf(input, "ր") + instancesOf(input, "ի") +
		instancesOf(input, "ո") + instancesOf(input, "ս");
				if(armenianTotal > 0)
					return "Armenian";
		
		
		
		/*
		 * These are for the languages in Arabic Script
		 */
		int persianTotal = instancesOf(input, "پ")
		          + instancesOf(input, "چ")
		          + instancesOf(input, "ژ")
		          + instancesOf(input, "گ")
		          + instancesOf(input, "ک")
		          + instancesOf(input, "ی");
		if(persianTotal > 0)
			return "Persian";
		
		//this is checking taa marbuta and alif maqsura
		if(instancesOf(input, "\u0629") + instancesOf(input, "\u0649") > 0)
			return "Arabic";
		
		
		/*
		 * These ones are all the only major language in their Script, so they are
		 * returned immediately.
		 * 
		 * I only check for the most common 7 letters to avoid unnecessary calls
		 * to the instancesOf() method
		 */
		//checking for Greek
		int greekTotal = instancesOf(input, "α") + instancesOf(input, "ι") + 
				instancesOf(input, "ο") + instancesOf(input, "ε") + instancesOf(input, "τ") + 
				instancesOf(input, "ν") + instancesOf(input, "ς");
		if(greekTotal > 0)
			return "Greek";
		
		//Checking for Thai
		int thaiTotal = instancesOf(input, "า") + instancesOf(input, "น") + 
				instancesOf(input, "อ") + instancesOf(input, "ร") + instancesOf(input, "ก") +
				instancesOf(input, "เ") + instancesOf(input, "ม");
		if(thaiTotal > 0)
			return "Thai";
		
		//Checking for Burmese
		int burmeseTotal = instancesOf(input, "က") + instancesOf(input, "န") + 
				instancesOf(input, "မ") + instancesOf(input, "တ") + instancesOf(input, "ရ") + 
				instancesOf(input, "ပ") + instancesOf(input, "သ");
		if(burmeseTotal > 0)
			return "Burmese";
		
		//Checking for Lao
		int laoTotal = instancesOf(input, "າ") + instancesOf(input, "ນ") + 
				instancesOf(input, "ວ") + instancesOf(input, "ອ") + instancesOf(input, "ກ") + 
				instancesOf(input, "ລ") + instancesOf(input, "ເ");
		if(laoTotal > 0)
			return "Lao";
		
		
		/*
		 * The below is all for Latin Script.
		 */
		
		//This will be added to in each of the following, with each check 
		//adding the number of that accent in the input
		int accentCount = 0;
		
		//every single check that is not for acute accents and does not return immediately should set this to false
		//This is being used for hungarian edge cases so its name is a little misleading
		boolean acuteOnly = true;
		
		int vietnameseCount = instancesOf(input, "ấ") + instancesOf(input, "ầ") + instancesOf(input, "ẩ")
        + instancesOf(input, "ẫ") + instancesOf(input, "ậ") + instancesOf(input, "ắ")
        + instancesOf(input, "ằ") + instancesOf(input, "ẳ") + instancesOf(input, "ẵ")
        + instancesOf(input, "ặ") + instancesOf(input, "ớ") + instancesOf(input, "ờ")
        + instancesOf(input, "ở") + instancesOf(input, "ỡ") + instancesOf(input, "ợ")
        + instancesOf(input, "ứ") + instancesOf(input, "ừ") + instancesOf(input, "ử")
        + instancesOf(input, "ữ") + instancesOf(input, "ự") + instancesOf(input, "ơ");
		/*
		 * check for ấ, ầ, ẩ, ẫ, ậ, ắ, ằ, ẳ, ẵ, ặ, ớ, ờ, ở, ỡ, ợ, ứ, ừ, ử, ữ, ự, ơ
		 * Vietnamese
		 */
		if(vietnameseCount > 0){
			return "Vietnamese";
		 }
		
		/*
		 * ẽ, ĩ, ũ, ỹ, g̃
		 * Guarani
		 */
		if(instancesOf(input, "ẽ") + instancesOf(input, "ĩ") + 
				instancesOf(input, "ũ") + instancesOf(input, "ỹ") + instancesOf(input, "g\u0303") > 0){
			return "Guarani";
		 }
		
		/*
		 * Checking for ã
		 * the array will contain only portuguese if that letter is found
		 */
		if(instancesOf(input, "ã") > 0){
			accentCount += instancesOf(input, "ã");
		    String[] arr = {"Portuguese", "Guarani"};
		    languageList = filterFor(languageList, arr);
		    acuteOnly = false;
		 }
		
		/*
		 * Checking for õ
		 * Portuguese, Estonian
		 */
		if(instancesOf(input, "õ") > 0){
			accentCount += instancesOf(input, "õ");
		    String[] arr = {"Portuguese", "Estonian", "Guarani"};
		    languageList = filterFor(languageList, arr);
		    acuteOnly = false;
		 }
		
		/*
		 * check for ë
		 * French, Albanian, Dutch
		 */
		if(instancesOf(input, "ë") > 0){
			accentCount += instancesOf(input, "ë");
		    String[] arr = {"French", "Albanian", "Dutch"};
		    languageList = filterFor(languageList, arr);
		    acuteOnly = false;
		    
		    //if it is above a certain frequency it is definitely Albanian 
		    if(((double) instancesOf(input, "ë")) / ((double) input.length()) >= 0.04) {
		    	return "Albanian";
		    }
		 }
		
		/*
		 * check for ä
		 * German, Finnish, Estonian, Swedish
		 */
		if(instancesOf(input, "ä") > 0){
			accentCount += instancesOf(input, "ä");
		    acuteOnly = false;
		    
		  //if it is above a certain frequency it is definitely Finnish or Estonian
		    if(((double) instancesOf(input, "ä")) / ((double) input.length()) >= 0.015) {
		    	String[] arr = {"Finnish", "Estonian", "Swedish"};
			    languageList = filterFor(languageList, arr);
		    }else {
		    	String[] arr = {"Finnish", "German", "Estonian", "Swedish"};
			    languageList = filterFor(languageList, arr);
		    }
		 }
		
		/*
		 * check for é
		 * French, Portuguese, Spanish, Italian, Czech, Hungarian, Dutch
		 */
		if(instancesOf(input, "é") > 0){
		    accentCount += instancesOf(input, "é");
		    String[] arr = {"French", "Portuguese", "Spanish", "Italian", "Czech", "Hungarian", 
		    		"Dutch", "Icelandic", "Norwegian", "Guarani"};
		    languageList = filterFor(languageList, arr);
		}

		/*
		 * check for í, ú
		 * Portuguese, Spanish, Czech
		 */
		if(instancesOf(input, "í") + instancesOf(input, "ú") > 0){
		    accentCount += instancesOf(input, "í") + instancesOf(input, "ú");
		    String[] arr = {"Portuguese", "Spanish", "Czech", "Hungarian", "Icelandic", "Guarani"};
		    languageList = filterFor(languageList, arr);
		}

		/*
		 * check for á
		 * Portuguese, Spanish, Dutch, Czech
		 */
		if(instancesOf(input, "á") > 0){
		    accentCount += instancesOf(input, "á");
		    String[] arr = {"Portuguese", "Spanish", "Dutch", "Czech", "Icelandic", 
		    		"Hungarian", "Guarani"};
		    languageList = filterFor(languageList, arr);
		}

		/*
		 * check for ó
		 * Portuguese, Spanish, Italian, Dutch, Czech
		 */
		if(instancesOf(input, "ó") > 0){
		    accentCount += instancesOf(input, "ó");
		    String[] arr = {"Portuguese", "Spanish", "Italian", "Dutch", "Czech", 
		    		"Icelandic", "Hungarian", "Guarani"};
		    languageList = filterFor(languageList, arr);
		}

		/*
		 * check for à
		 * French, Portuguese, Italian, Dutch
		 */
		if(instancesOf(input, "à") > 0){
		    accentCount += instancesOf(input, "à");
		    String[] arr = {"French", "Portuguese", "Italian", "Dutch"};
		    languageList = filterFor(languageList, arr);
		    acuteOnly = false;
		}

		/*
		 * check for ì
		 * Italian
		 */
		if(instancesOf(input, "ì") > 0){
		    return "Italian";
		}
		
		/*
		 * check for ì, ù
		 * Italian
		 */
		if(instancesOf(input, "ù") > 0){
			accentCount += instancesOf(input, "à");
		    String[] arr = {"French", "Italian"};
		    languageList = filterFor(languageList, arr);
		    acuteOnly = false;
		}
		
		/*
		 * check for ò
		 * Italian, Norwegian
		 */
		if(instancesOf(input, "ò") > 0){
		    accentCount += instancesOf(input, "ò");
		    String[] arr = {"Norwegian", "Italian"};
		    languageList = filterFor(languageList, arr);
		    acuteOnly = false;
		}

		/*
		 * check for è,
		 * Italian, Dutch, French
		 */
		if(instancesOf(input, "è") > 0){
		    accentCount += instancesOf(input, "è");
		    String[] arr = {"Italian", "Dutch", "French"};
		    languageList = filterFor(languageList, arr);
		    acuteOnly = false;
		}

		/*
		 * check for â,
		 * Portuguese, French, Romanian
		 */
		if(instancesOf(input, "â") > 0){
		    accentCount += instancesOf(input, "â");
		    String[] arr = {"Portuguese", "French", "Romanian"};
		    languageList = filterFor(languageList, arr);
		    acuteOnly = false;
		}

		/*
		 * check for ê
		 * Portuguese, French, Dutch
		 */
		if(instancesOf(input, "ê") > 0){
		    accentCount += instancesOf(input, "ê");
		    String[] arr = {"Portuguese", "French", "Dutch", "Kurmanji Kurdish"};
		    languageList = filterFor(languageList, arr);
		    acuteOnly = false;
		}

		/*
		 * check for î
		 * French, Romanian
		 */
		if(instancesOf(input, "î") > 0){
		    accentCount += instancesOf(input, "î");
		    String[] arr = {"French", "Romanian", "Kurmanji Kurdish"};
		    languageList = filterFor(languageList, arr);
		    acuteOnly = false;
		}

		/*
		 * check for ô
		 * Portuguese, French, Dutch
		 */
		if(instancesOf(input, "ô") > 0){
		    accentCount += instancesOf(input, "ô");
		    String[] arr = {"Portuguese", "French", "Dutch"};
		    languageList = filterFor(languageList, arr);
		    acuteOnly = false;
		}

		/*
		 * check for û
		 * French, Kurmanji Kurdish
		 */
		if(instancesOf(input, "û") > 0){
			accentCount += instancesOf(input, "û");
		    String[] arr = {"French", "Kurmanji Kurdish"};
		    languageList = filterFor(languageList, arr);
		    acuteOnly = false;
		}

		/*
		 * check for ö
		 * German, Finnish, Hungarian
		 */
		if(instancesOf(input, "ö") > 0){
		    accentCount += instancesOf(input, "ö");
		    String[] arr = {"German", "Finnish", "Hungarian", "Estonian", "Swedish", "Icelandic"};
		    languageList = filterFor(languageList, arr);
		}

		/*
		 * check for ï
		 * French, Dutch
		 */
		if(instancesOf(input, "ï") > 0){
		    accentCount += instancesOf(input, "ï");
		    String[] arr = {"French", "Dutch"};
		    languageList = filterFor(languageList, arr);
		    acuteOnly = false;
		}

		/*
		 * check for ü
		 * French, Spanish, German, Dutch, Hungarian, Estonian
		 */
		if(instancesOf(input, "ü") > 0){
		    accentCount += instancesOf(input, "ü");
		    String[] arr = {"French", "Spanish", "German", "Dutch", "Hungarian", "Estonian"};
		    languageList = filterFor(languageList, arr);
		}

		/*
		 * check for ç
		 * French, Portuguese, Albanian, Turkish, Kurmanji Kurdish
		 */
		if(instancesOf(input, "ç") > 0){
		    accentCount += instancesOf(input, "ç");
		    String[] arr = {"French", "Portuguese", "Albanian", "Turkish", "Kurmanji Kurdish"};
		    languageList = filterFor(languageList, arr);
		    acuteOnly = false;
		}

		/*
		 * check for ñ
		 * Spanish, Guarani
		 */
		if(instancesOf(input, "ñ") > 0){
			accentCount += instancesOf(input, "ñ");
		    String[] arr = {"Spanish", "Guarani"};
		    languageList = filterFor(languageList, arr);
		    acuteOnly = false;
		    //returns Spanish if it is not already limited to Guarani
		    if(Arrays.asList(languageList).contains("Spanish"))
		    	return "Spanish";
		    
		}

		/*
		 * check for ß
		 * German
		 */
		if(instancesOf(input, "ß") > 0){
			return "German";
		}

		/*
		 * check for ů
		 * Czech
		 */
		if(instancesOf(input, "ů") > 0){
		    return "Czech";
		}

		/*
		 * check for ř, ď, ť, ň
		 * Czech
		 */
		if(instancesOf(input, "ř") + instancesOf(input, "ď") + instancesOf(input, "ť") + instancesOf(input, "ň") > 0){
			return "Czech";
		}
		
		/*
		 * check for ý
		 * Czech, Icelandic, Guarani
		 */
		if(instancesOf(input, "ý") > 0){
		    accentCount += instancesOf(input, "ý");
		    String[] arr = {"Czech", "Icelandic", "Guarani"};
		    languageList = filterFor(languageList, arr);
		    acuteOnly = false;
		}

		/*
		 * check for š, ž
		 * Czech, Serbo-Croat, Lithuanian, Latvian, Estonian
		 */
		if(instancesOf(input, "š") + instancesOf(input, "ž") > 0){
		    accentCount += instancesOf(input, "š") + instancesOf(input, "ž");
		    String[] arr = {"Czech", "Serbo-Croat", "Lithuanian", "Latvian", "Estonian"};
		    languageList = filterFor(languageList, arr);
		    acuteOnly = false;
		}
		
		/*
		 * check for č
		 * Czech, Serbo-Croat, Lithuanian, Latvian
		 */
		if(instancesOf(input, "č") > 0){
		    accentCount += instancesOf(input, "č");
		    String[] arr = {"Czech", "Serbo-Croat", "Lithuanian", "Latvian"};
		    languageList = filterFor(languageList, arr);
		    acuteOnly = false;
		}

		/*
		 * check for đ
		 * Serbo-Croat
		 */
		if(instancesOf(input, "đ") > 0){
		    return "Serbo-Croat";
		}

		/*
		 * check for ń, ś, ź
		 * Polish
		 */
		if(instancesOf(input, "ń") + instancesOf(input, "ś") + instancesOf(input, "ź") > 0){
			return "Polish";
		}

		/*
		 * check for ć
		 * Polish, Serbo-Croat
		 */
		if(instancesOf(input, "ć") > 0){
		    accentCount += instancesOf(input, "ć");
		    String[] arr = {"Polish", "Serbo-Croat"};
		    languageList = filterFor(languageList, arr);
		    acuteOnly = false;
		}

		/*
		 * check for ł, ż
		 * Polish
		 */
		if(instancesOf(input, "ł") + instancesOf(input, "ż") > 0){
		    return "Polish";
		}

		/*
		 * check for ą, ę
		 * Polish, Lithuanian
		 */
		if(instancesOf(input, "ą") + instancesOf(input, "ę") > 0){
		    accentCount += instancesOf(input, "ą") + instancesOf(input, "ę");
		    String[] arr = {"Polish", "Lithuanian"};
		    languageList = filterFor(languageList, arr);
		    acuteOnly = false;
		}

		/*
		 * check for į, ų, ė
		 * Lithuanian
		 */
		if(instancesOf(input, "į") + instancesOf(input, "ų") + instancesOf(input, "ė") > 0){
		    return "Lithuanian";
		}

		/*
		 * check for ū
		 * Lithuanian, Latvian
		 */
		if(instancesOf(input, "ū") > 0){
		    accentCount += instancesOf(input, "ū");
		    String[] arr = {"Lithuanian", "Latvian"};
		    languageList = filterFor(languageList, arr);
		    acuteOnly = false;
		}

		/*
		 * check for ā, ē, ī, ģ, ķ, ļ, ņ
		 * Latvian
		 */
		if(instancesOf(input, "ā") + instancesOf(input, "ē") + instancesOf(input, "ī") + instancesOf(input, "ģ")
		        + instancesOf(input, "ķ") + instancesOf(input, "ļ") + instancesOf(input, "ņ") > 0){
		    return "Latvian";
		}

		/*
		 * check for ő, ű
		 * Hungarian
		 */
		if(instancesOf(input, "ő") + instancesOf(input, "ű") > 0){
		    return "Hungarian";
		}

		/*
		 * check for ș, ț, ă
		 * Romanian
		 */
		if(instancesOf(input, "ș") + instancesOf(input, "ț") + instancesOf(input, "ă") > 0){
		    return "Romanian";
		}

		/*
		 * check for å
		 * Finnish, Danish, Norwegian, Swedish
		 */
		if(instancesOf(input, "å") > 0){
			accentCount += instancesOf(input, "å");
			
			if(((double) instancesOf(input, "å") / input.length()) > 0.005) {
				String[] arr = {"Swedish", "Norwegian", "Danish"};
			    languageList = filterFor(languageList, arr);
			}else {
				String[] arr = {"Finnish", "Danish", "Swedish", "Norwegian"};
			    languageList = filterFor(languageList, arr);
			}
			
		    acuteOnly = false;
		}
		
		/*
		 * check for æ
		 * Danish, Icelandic, Norwegian
		 */
		if(instancesOf(input, "æ") > 0){
			accentCount += instancesOf(input, "æ");
		    String[] arr = {"Danish", "Icelandic", "Norwegian"};
		    languageList = filterFor(languageList, arr);
		    acuteOnly = false;
		}
		
		/*
		 * check for ø
		 * Danish, Icelandic, Norwegian
		 */
		if(instancesOf(input, "ø") > 0){
			accentCount += instancesOf(input, "ø");
		    String[] arr = {"Danish", "Icelandic", "Norwegian"};
		    languageList = filterFor(languageList, arr);
		    acuteOnly = false;
		}
		
		/*
		 * check for ð
		 * Icelandic
		 */
		if(instancesOf(input, "ð") > 0){
			return "Icelandic";
		}
		
		/*
		 * check for þ
		 * Icelandic
		 */
		if(instancesOf(input, "þ") > 0){
			return "Icelandic";
		}

		/*
		 * Check for ə
		 * Azerbaijani
		 */
		if(instancesOf(input, "ə") > 0){
		    return "Azerbaijani";
		}
		
		/*
		 * check for ğ, ı
		 * Turkish
		 * 
		 * These letters could also be contained in Azerbaijani, but if there is no ə
		 * it is always a better guess to go with Turkish due to ə's frequency in Azerbaijani.
		 */
		if(instancesOf(input, "ğ") + instancesOf(input, "ı") > 0){
		    return "Turkish";
		}
		
		/*
		 * check for ş
		 * Turkish, Kurmanji Kurdish
		 * same situation as above with azerbaijani
		 */
		if(instancesOf(input, "ş") > 0){
			accentCount += instancesOf(input, "ş");
		    String[] arr = {"Turkish", "Kurmanji Kurdish"};
		    languageList = filterFor(languageList, arr);
		    acuteOnly = false;
		}
		
		//returns the one remaining element if there is only one
		if(languageList.length == 1)
			return languageList[0];
		
		/*
		 * If there was only ò, it is definitely Italian
		 */
		if(Arrays.asList(languageList).contains("Norwegian") && Arrays.asList(languageList).contains("Italian") && 
				!Arrays.asList(languageList).contains("English"))
			return "Italian";
		
		/*
		 * If only Swedish and Norwegian (And danish, Icelandic, Finnish) remain, it is probably Norwegian since 
		 * the others probably would be otherwise identified already
		 */
		if(Arrays.asList(languageList).contains("Swedish") && Arrays.asList(languageList).contains("Norwegian") && 
				!Arrays.asList(languageList).contains("English"))
			return "Swedish";
		
		/*
		 * I need to distinguish between Danish and Norwegian if only those two remain (which is most likely)
		 * This one is really hard to guess without actually speaking the language
		 */
		if(Arrays.asList(languageList).contains("Danish") && Arrays.asList(languageList).contains("Norwegian") && 
				!Arrays.asList(languageList).contains("English"))
			return "It's either Danish or Norwegian. These are really hard to tell apart unless you speak one.";
		
		/*
		 * If the languages czech, serbo-croat, lithuanian, and latvian remain (or a subset thereof), return serbo-croat
		 * The only way for any combination of those four is that only the haceks are present, which is necessarily all four
		 * I guess estonian could theoretically be there but it would be narrowed down by other accents
		 * this is really unlikely to run because czech has a lot of long vowel accents
		 */
		if(Arrays.asList(languageList).contains("Serbo-Croat") && Arrays.asList(languageList).contains("Czech") && 
				!Arrays.asList(languageList).contains("English"))
			return "Serbo-Croat";
		
		/*
		 * Estonian has more diacritics than Finnish and they are pretty common. If only the two of them remain,
		 * it is almost certainly Finnish as long as the input is of adequate length
		 */
		if(Arrays.asList(languageList).contains("Finnish") && Arrays.asList(languageList).contains("Estonian") && 
				!Arrays.asList(languageList).contains("English"))
			return "Finnish";
		
		//This means it had a and o umlaut. We are going to assume it is german
		//since it did not trigger the return for Finnish earlier based on frequency
		//This could be improved upon later
		if(Arrays.asList(languageList).contains("German") && Arrays.asList(languageList).contains("Finnish") && 
				!Arrays.asList(languageList).contains("English"))
			return "German";
		
		//checking the case where we get hungarian with only acutes. Much higher frequency than in other languages.
		//The occasional cooked up French text could be problematic, or a pretty short Czech text. 
		//Length of input will solve this.
		int acuteCount = instancesOf(input, "á") + instancesOf(input, "í")
        + instancesOf(input, "ó") + instancesOf(input, "ú") + instancesOf(input, "é");
		if(acuteOnly && accentCount > 0 && ((double) acuteCount / input.length()) > 0.03)
			return "Hungarian";
		
		//I need to handle Portuguese, Spanish, and French with accents
		//Could make better later using circonflex accents
		if(accentCount > 0) {
			int éCases = instancesOf(input, "é");
			int acuteNonECount = instancesOf(input, "á") + instancesOf(input, "í")
	        + instancesOf(input, "ó") + instancesOf(input, "ú");
			
			//This should be really really accurate for longer cases in French
			if(((double)éCases / input.length()) > 0.01 && acuteNonECount == 0)
				return "French";
			
			//Italian has an occasional é
			if(acuteNonECount == 0 && Arrays.asList(languageList).contains("Italian")) {
				return "Italian";
			}
			
			//Spanish probably has the least out of these options, especially than portuguese
			if(((double) acuteNonECount / input.length()) < 0.01 && instancesOf(input,"ç") == 0) {
				return "Spanish";
			}
			
			if(instancesOf(input, "ç") > 0 && acuteNonECount > 0 && Arrays.asList(languageList).contains("Portuguese"))
				return "Portuguese";
			
			//in case none of the above are caught, returns Portuguese since it is most likely in these options
			if(acuteNonECount > 0)
				return "Portuguese";
			
		}
		
		/*These languages will be allowed to remain if accentCount is still 0:
		 * English, Italian, Dutch
		 */
		
		if(accentCount == 0) {
			String[] newLanguageList = {"English", "Italian", "Dutch"};
			return noAccents(newLanguageList, input);
		}
		/*
		 * It is probably incredibly difficult to stumble on to this. Almost all cases 
		 * are handled above.
		 */
		String language = "A really niche edge case. Impressive";
		return language;
	}
	
	public static String noAccents(String[] languageList, String input) {
		//Italian is known for having almost all words ending in a vowel.
		//If it's over 60% in the input, then Italian is identified.
		if((instancesOf(input, "a ") + instancesOf(input, "e ") + instancesOf(input, "i ")
        + instancesOf(input, "o ") + instancesOf(input, "u ")) / instancesOf(input, " ") > 0.6)
			return "Italian";
		
		//If I have time later on, I can check for specific words that only appear in one or the other
		int digraphCount = instancesOf(input, "ij") + instancesOf(input, "aa") + instancesOf(input, "ee")
        + instancesOf(input, "uu") + instancesOf(input, "sch") + instancesOf(input, "cht");
		if(((double) digraphCount / input.length()) > 0.025)
			return "Dutch";
		//things to check in dutch: "ij", double vowels, "sch", " vl"?, "cht"?
		
		//Then return english if neither of those
		return "English";
	}
}
