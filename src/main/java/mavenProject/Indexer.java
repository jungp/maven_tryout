package mavenProject;

import java.util.Iterator;
import java.util.SortedMap;
import java.util.TreeMap;

public class Indexer {
	
	private static String clean(String word) {
		word = word.toLowerCase();
		word = word.replaceAll("[^a-zäöüß]", "");
		if (word.isEmpty()) {
			return null;
		}
		return word;
	}
	
	private static String[] tokenize(String text) {
		return text.split("\\s");
		
	}
	
	private static String index(String document, SortedMap<String, Integer> seeklist, String index){
		document = document.toLowerCase();
		
		String[] words = tokenize(document);
		String word = "";
		int addPosition = 0;
		String newlyAddedEntry = "";
		
		for (int i = 0; i < words.length; i++){
			
			word = clean(words[i]);
			if (!seeklist.containsKey(word)){ // if it does not exist yet
				
				seeklist.put(word, 0); // add word to list, update index pointer later
				newlyAddedEntry = word + ":" + document.indexOf(word) + ";";
				
				SortedMap<String, Integer> restBeginningFromCurrEle = seeklist.tailMap(word);
				Iterator<Integer> it = restBeginningFromCurrEle.values().iterator();

				int nextValue = it.next(); // ignore first element of list because .tailMap includes this WORD as first element
				
				if (it.hasNext()){
					
					nextValue = it.next();
					
					if (nextValue == 0){
						
						// we are adding in the beginning
						//newlyAddedEntry = word + ":" + document.indexOf(word) + ";";
						index = newlyAddedEntry + index;
						addPosition = 0;
						
					} else {
						
						// adding in the middle
						String firstPart = index.substring(0, nextValue);
						String secondPart = index.substring(nextValue, index.length());
						
						//newlyAddedEntry = word + ":" + document.indexOf(word) + ";";
						index = firstPart + newlyAddedEntry + secondPart;
						
						addPosition = nextValue;
					}

					// shift rest back accordingly
					for(String key : restBeginningFromCurrEle.keySet()){
						if (!key.equals(word)){ // ignore first element of list
							seeklist.put(key, seeklist.get(key) + newlyAddedEntry.length());
						}	
					}
					
				} else {
					// this must be the last element (or first element right after list creation)
					index += newlyAddedEntry;	
					addPosition = index.length() - newlyAddedEntry.length();
				}
				
				// update seek list
				seeklist.put(word, addPosition);
				
			} else { // word exists already
				
				SortedMap<String, Integer> restBeginningFromCurrEle = seeklist.tailMap(word);
				Iterator<Integer> it = restBeginningFromCurrEle.values().iterator();
				
				it.next(); // ignore first element of list because .tailMap includes this WORD as first element
				
				if (it.hasNext()){
					// not last element
					String sub = index.substring(seeklist.get(word));
					sub = sub.replaceFirst(";", ",NEW#;"); // just extend the pointers list
					index = index.substring(0, seeklist.get(word)) + sub;
					
					// update rest of seeklist
					for(String key : restBeginningFromCurrEle.keySet()){
						if (!key.equals(word)){ // ignore first element of list
							seeklist.put(key, seeklist.get(key) + ",NEW#".length());
						}	
					}
				} else {
					// this is the last element
					index = index.substring(0, index.length() - 1) + ",NEW#;"; // just add to the end
				}
				
			} // containsKey else	
		} // word loop
		return index;
	}
	
	public static void main(String[] args) {
		
		String document = "The cow cow cow eats eats grass. Then the cow goes poop.";
		SortedMap<String, Integer> seeklist = new TreeMap<String, Integer>();
		String index = ""; // simulation of a file where everything is stored consecutively (and alphabetically ordered)
		
		index = index(document, seeklist, index);
		
		// do some testing
		
		System.out.println(index);
		
		for (String key : seeklist.keySet()){
			System.out.println("Key: " + key);
		}
		
		System.out.println(seeklist.get("grass"));
		System.out.println(seeklist.get("eats"));
		System.out.println(seeklist.get("the"));
		System.out.println(seeklist.get("cow"));
		System.out.println(seeklist.get("poop"));
		System.out.println(seeklist.get("then"));
		System.out.println(seeklist.get("goes"));
		
		// simulate query "cow"
		
		String temp = index.substring(seeklist.get("cow"));
		String result = temp.substring(0, temp.indexOf(';'));
		System.out.println("Query result: " + result);

		
	}

}
