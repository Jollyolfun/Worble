package model;

import java.io.Serializable;

public class User implements Serializable {

	String username;
	String password;
	public int wins;
	public int losses;
	public int streak;
	private int oneGuess;
	private int twoGuesses;
	private int threeGuesses;
	private int fourGuesses;
	private int fiveGuesses;
	private int sixGuesses;
	
	/**
	 * Creates an instance of a users login details
	 * and # of wins/losses.
	 * @param username username for new account
	 * @param password password for new account
	 */
	public User (String username, String password) {
		this.username = username;
		this.password = password;
		this.wins = 0;
		this.losses = 0;
		this.oneGuess = 0;
		this.twoGuesses = 0;
		this.threeGuesses = 0;
		this.fourGuesses = 0;
		this.fiveGuesses = 0;
		this.sixGuesses = 0;
	}
	
	/**
	 * Returns users username
	 * @return String username The current User's username
	 */
	public String getUsername() {
		return username;
	}
	
	/**
	 * Checks if password entered by a user for a specific username
	 * matches the one saved for that username
	 * 
	 * @param check Entered password by the user
	 * @return True if password matches, False if not
	 */
	public boolean checkPassword(String check) {
		return check.equals(password);
	}
	
	/**
	 * Returns users total wins
	 * @return int # of wins
	 */
	public int getWins() {
		return wins;
	}
	
	/**
	 * Returns users total losses
	 * @return int # of losses
	 */
	public int getLosses() {
		return losses;
	}
	
	/**
	 * Returns the int of one guess
	 * @return int of oneGuess
	 */
	public int getOneGuess() {
		return oneGuess;
	}
	
	/**
	 * Returns the int of two guesses
	 * @return int of twoGuesses
	 */
	public int getTwoGuesses() {
		return twoGuesses;
	}
	
	/**
	 * Returns the int of three guesses
	 * @return int of threeGuesses
	 */
	public int getThreeGuesses() {
		return threeGuesses;
	}
	
	/**
	 * Returns the int of four guesses
	 * @return int of fourGuesses
	 */
	public int getFourGuesses() {
		return fourGuesses;
	}
	
	/**
	 * Returns the int of five guesses
	 * @return int of fiveGuesses
	 */
	public int getFiveGuesses() {
		return fiveGuesses;
	}
	
	/**
	 * Returns the int of six guesses
	 * @return int of sixGuesses
	 */
	public int getSixGuesses() {
		return sixGuesses;
	}
	
	/**
	 * Increments the number of guesses.
	 * @param newResult The number of guesses it took for
	 * the user to win the most recent game
	 */
	public void addNewResult(int newResult) {
		if (newResult == 0) {
			oneGuess += 1;
		} else if (newResult == 1) {
			twoGuesses += 1;
		} else if (newResult == 2) {
			threeGuesses += 1;
		} else if (newResult == 3) {
			fourGuesses += 1;
		} else if (newResult == 4) {
			fiveGuesses += 1;
		} else {
			sixGuesses +=1;
		}
	}
}
