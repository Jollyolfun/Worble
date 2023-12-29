package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;

/**
 * Generates a random word to be used as the goal word for
 * a game of Wordle, from a list of words in a dictionary file.
 **/
public class RandomWordGenerator {

	static ArrayList<String> dict;
	
	/**
	 * Picks string from the file and returns it
	 * @return Random word from dictionary file
	 * @throws FileNotFoundException
	 */
	public static String getWord() throws FileNotFoundException {
		
		dict = makeDict();
		int range = dict.size();
		Random random = new Random();
		int indexOfWord = random.nextInt(range);
		return dict.get(indexOfWord);
	}
	
	/**
	 * Gathers all of the word from the dictionary file and 
	 * stores them in an ArrayList.
	 * @return ArrayList of possible words
	 * @throws FileNotFoundException
	 */
	private static ArrayList<String> makeDict() throws FileNotFoundException {
		File dictFile = new File("src/documents/wordleDictionary.txt");

		ArrayList<String> dictionary = new ArrayList<>();

			Scanner dictReader = new Scanner(dictFile);
			while (dictReader.hasNextLine()) {
				String word = dictReader.nextLine();
				
				if (word.length() == 5) {
					dictionary.add(word);
				}
			}
		 
		return dictionary;
	}
	
	/**
	 * Returns the ArrayList of dictionary words
	 * @return ArrayList of possible words
	 */
	public static ArrayList<String> getDictionary() {
		return dict;
	}
}
