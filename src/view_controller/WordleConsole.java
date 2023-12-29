package view_controller;


import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import model.RandomWordGenerator;
import model.Wordle;

public class WordleConsole {

	static boolean[] checks;
	static Wordle game;
	
	/**
	 * Runs a game of wordle in the console.
	 * @param args
	 * @throws FileNotFoundException
	 */
	public static void main(String[] args) throws FileNotFoundException {
		String goalWord = RandomWordGenerator.getWord();
		ArrayList<String> dict = RandomWordGenerator.getDictionary();
		game = new Wordle(goalWord);

		boolean playing = true;
		Scanner userInput = new Scanner(System.in);
		int attempts = 0;
		while (playing) {


			if (attempts == 6) {
				System.out.println("Sorry, you ran out of attempts! The word was: " + goalWord);
				System.out.println();
				System.out.print("Would you like to play again? Type Y if yes and N if no: ");

				String again = userInput.nextLine();
				if (again.equals("Y")) {
					goalWord = RandomWordGenerator.getWord();
					game = new Wordle(goalWord);
					attempts = 0;
					continue;
				}
				else if (again.equals("N")) {
					break;
				}
				
			}

			System.out.print("Please enter a guess: ");
			String guessWord = userInput.nextLine();
			if (guessWord.length() != 5) {
				System.out.println();
				System.out.println("Your guess must be 5 letters");
				System.out.println();
				continue;
			}
			else if (!dict.contains(guessWord)) {
				System.out.println();
				System.out.println("Not a valid word");
				System.out.println();
				continue;
			}

			boolean status = game.checkWord(guessWord);
			if (status == true) {
				attempts += 1;
				System.out.println("Congratulations! It took you " + attempts + " attempt(s).");
				System.out.println();
				System.out.print("Would you like to play again? Type Y if yes and N if no: ");
				String again = userInput.nextLine();
				if (again.toLowerCase().equals("y")) {
					goalWord = RandomWordGenerator.getWord();
					game = new Wordle(goalWord);
					attempts = 0;
					continue;
				}
				else if (again.toLowerCase().equals("n")) {
					break;
				}
				

			}
			int[] checks = game.getChecks();

			String[] guessWordArray = guessWord.split("");

			System.out.println();

			for (int i = 0; i < 5; i++) {
				if (checks[i] == 2) {
					System.out.print("C ");
				}
				if (checks[i] == 1) {
					System.out.print("* ");
				}
				if (checks[i] == 0) {
					System.out.print("X ");
				}
			}

			System.out.println();
			for (int i = 0; i < 5; i++) {
				System.out.print(guessWordArray[i] + " ");

			}

			System.out.println();

			attempts += 1;
			System.out.println();

		}
		System.out.println();

		System.out.println("Thanks for playing!");

	}
	
	
	


}