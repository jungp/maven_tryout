package mavenProject;

import org.tartarus.martin.Stemmer;

public class WordStemmer {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String string = "greedy";
		
		Stemmer st = new Stemmer();
		
		st.add(string.toCharArray(), string.length());
		st.stem();
		
		System.out.println(st.toString());

	}

}
