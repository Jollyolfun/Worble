package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.platform.commons.util.CollectionUtils;

public class Wordle {

	// 2 = correct, 1 = in word but not right spot, 0 = wrong
	int[] checks;
	String goalWord;
	Set<Integer> indexSet = new HashSet<Integer>();

	public Wordle(String goalWord) {
		// when we make the word generator, we will make the goal a random word
		this.goalWord = goalWord.toLowerCase();

	}
	
	/**
	 * Checks if given word is the goal word. 
	 * @param guess The String the user entered as a guess
	 * @return True if guess is correct, False if not
	 */
	public boolean checkWord(String guess) {
		
		guess = guess.toLowerCase();
		this.indexSet = new HashSet<Integer>();
		checks = new int[5];

//		System.out.println("Goal: " + goalWord);
//		System.out.println("Guess: " + guess);

		if (guess.length() != 5) {
			return false;
		}
		// if the word is correct return true
		if (guess.equals(goalWord)) {
			for (int i = 0; i < 5; i++) {
				checks[i] = 2;
			}
			return true;
		}
		// These lists exist to retroactively check if a word has multiple of the
		// same letter, and correct mistakes where a letter is yellow that should be
		// grey
		List<String> goalList = new ArrayList<String>(Arrays.asList(goalWord.split("")));
		// iterates through the guess word and checks if the letters are in the right or
		// wrong spot
		for (int i = 0; i < guess.length(); i++) {
			if (guess.charAt(i) == goalWord.charAt(i)) {
				checks[i] = 2;
				goalList.set(i, "2");
			}
			else if (goalWord.contains(Character.toString(guess.charAt(i)))) {
				boolean yellow = checkWordHelper(guess, guess.charAt(i));
				if (yellow == true) {
					checks[i] = 1;
				} else {
					checks[i] = 0;
				}
			}
			else if (!goalWord.contains(Character.toString(guess.charAt(i)))) {
				checks[i] = 0;
				goalList.set(i, "0");
			}
		}
		
		return false;
	}
	
	/**
	 * Helper function to determine if guess is correct.
	 * @param guess The String the user entered as a guess
	 * @param currChar The character in the guess that is currently being checked
	 * @return True for the currChar to be yellow, false if currChar should be grey
	 */
	//Returns true for yellow and false for gray
	private boolean checkWordHelper(String guess, char currChar) {
		
		//Creates sets of numbers representing the indices of where the currChar is in
		//the guess word as well as the goal word
		Set<Integer> guessChars = new HashSet<Integer>();
		Set<Integer> goalChars = new HashSet<Integer>();
		
		//Actually adds the currChar to each respective set
		for (int i = 0; i < guess.length(); i++) {
			if (goalWord.charAt(i) == currChar) {
				goalChars.add(i);
			}
			if (guess.charAt(i) == currChar) {
				guessChars.add(i);
			}
		}
		
		//If the goal word has more of the currChar than the guess word has, 
		//you know the guess word currChar has to be yellow, so return true
		if (guessChars.size() <= goalChars.size()) {
			return true;
		}
		
		//Remove shared indices between the two sets from both sets 
		Set<Integer> sharedNum = new HashSet<Integer>();
		for (int num : guessChars) {
			if (goalChars.contains(num)) {
				sharedNum.add(num);
			}
		}
		
		for (int num : sharedNum) {
			guessChars.remove(num);
			goalChars.remove(num);
		}
		
		//If a currChar in the goal Word has been "used" by a former guess word currChar
		//to be marked yellow, remove it from the set of goal indices
		for (int num : indexSet) {
			goalChars.remove(num);
		}
		
		//If there are none of the currChar in the goal set after everything has been removed,
		//that means the guess currChar has to be gray. If there is a currChar still in the 
		//goal set, then that will be what turns the guess currChar yellow and return true, and 
		//that goal currChar is marked by already have been "used" (by adding it to a set)
		if (goalChars.size() == 0) {
			return false;
		} else {
			Integer[] goalIndex = goalChars.toArray(new Integer[goalChars.size()]);
			indexSet.add(goalIndex[0]);
			return true;
		}
		
	}
	
	/**
	 * Returns an int[] of correct/incorrect letters
	 * @return int[] of correct/incorrect letters
	 */
	public int[] getChecks() {
		return checks;
	}
}