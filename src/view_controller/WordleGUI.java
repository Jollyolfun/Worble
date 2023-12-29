package view_controller;

import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.RandomWordGenerator;
import model.User;
import model.Wordle;

public class WordleGUI extends Application {

	private static final int NUM_ROWS = 6;
	private static final int NUM_COLS = 5;
	private static final int BOX_SIZE = 50;
	private static final int SPACING = 10;
	private HashSet<User> users;
	private Button[][] boxes = new Button[NUM_ROWS][NUM_COLS];
	private User currentUser;
	private int row;
	private int colIndex;
	private Wordle wordle;
	private boolean won;
	private boolean loggedIn;
	private ArrayList<String> dict;
	private ArrayList<Button> buttons;
	private String goalWord;
	private boolean finLoss;
	private Menu views;
	private MenuItem darkMode;
	private MenuItem lightMode;
	private Menu settings;
	private MenuItem newGame;
	boolean darkModeBool;
	boolean lightModeBool;
	Label titleLabel;
	MediaPlayer mediaPlayer;
	private Stage primaryStage;

	/**
	 * Sets up the stage with a login screen. Sets event handlers for
	 * login button as well.
	 * 
	 * @param primaryStage the Stage which holds all the GUI information and objects
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		addUsers();
		if (this.users == null) {
			this.users = new HashSet<User>();
		}

		goalWord = RandomWordGenerator.getWord();
		System.out.println(goalWord);
		wordle = new Wordle(goalWord);
		row = 0;
		colIndex = 0;
		//users = new HashSet<>();

		VBox root = new VBox();
		root.setAlignment(Pos.CENTER);
		root.setSpacing(10);

		Label titleLabel = new Label("Login Screen");
		titleLabel.setFont(Font.font(24));
		root.getChildren().add(titleLabel);

		GridPane grid = new GridPane();

		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));

		Label usernameLabel = new Label("Username:");
		grid.add(usernameLabel, 0, 1);

		TextField usernameField = new TextField();
		grid.add(usernameField, 1, 1);

		Label passwordLabel = new Label("Password:");
		grid.add(passwordLabel, 0, 2);

		PasswordField passwordField = new PasswordField();
		grid.add(passwordField, 1, 2);

		Button loginButton = new Button("Login");
		loginButton.setOnAction(event -> {
			// TODO: Check username and password, and if they are correct, show the
			// WordleGUI screen
			for (User user : users) {
				if (user.getUsername().equals(usernameField.getText()) && user.checkPassword(passwordField.getText())) {
					currentUser = user;
					System.out.println("Logged in");
					loggedIn = true;
					primaryStage.setScene(wordleView(primaryStage));
					break;

				}

			}
			if (!loggedIn) {
				System.out.println("Not a user, please user a valid username and password");

			}

		});

		Button signUpButton = new Button("Sign Up");
		signUpButton.setOnAction(event -> {
			if (!(usernameField.getText().equals("")) || !(passwordField.getText().equals(""))) {
				if (users.isEmpty()) {
					users.add(new User(usernameField.getText(), passwordField.getText()));
				} else {
					for (User user : users) {
						if (user.getUsername().equals(usernameField.getText())) {
							System.out.println("Username must not be in use");
							return;
						}
					}
					users.add(new User(usernameField.getText(), passwordField.getText()));
				}
			}

		});

		HBox buttonBox = new HBox();
		buttonBox.setAlignment(Pos.CENTER_RIGHT);
		buttonBox.getChildren().add(loginButton);
		buttonBox.getChildren().add(signUpButton);

		grid.add(buttonBox, 1, 4);

		root.getChildren().add(grid);

		Scene scene = new Scene(root, 400, 400);

		primaryStage.setScene(scene);
		primaryStage.setTitle("Login Screen");
		primaryStage.show();
		
		primaryStage.setOnCloseRequest((event) -> {
			saveUsers();
		});
	}
	/**
	 * Automatically saves the users in the UserHashet to a serializable file
	 */
	private void saveUsers() {
		try {
			FileOutputStream bytesToDisk = new FileOutputStream("users.ser");
			ObjectOutputStream outFile = new ObjectOutputStream(bytesToDisk);
			// outFile understands the writeObject message. Make the objects persist so they
			// can be read later.
			outFile.writeObject(users);
			outFile.close();
			System.out.println("Writing objects success");
		} catch (IOException ioe) {
			System.out.println("Writing objects failed");
		}

	}
	
	/**
	 * Automatically adds all the previous users into the HashSet of users
	 */
	public void addUsers() {
		
		FileInputStream rawBytes;
		try {
			rawBytes = new FileInputStream("users.ser"); // Read the .ser file just created
			ObjectInputStream inFile = new ObjectInputStream(rawBytes);
			// Read the entire object from the file on disk. Casts required
			@SuppressWarnings("unchecked")
			HashSet<User> savedUsers = (HashSet<User>) inFile.readObject();
			inFile.close();
			this.users = savedUsers;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (EOFException e) {
			this.users = new HashSet<User>();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
	}

	public static void main(String[] args) {
		launch(args);
	}

	/**
	 * Sets the stage of the wordle game with the boxes and keyboard layout.
	 * @param primaryStage the Stage which holds all the GUI information and objects
	 * @return the scene of the wordle game.
	 */
	private Scene wordleView(Stage primaryStage) {
		// SETTING UP MENU
		views = new Menu("Views");

		lightMode = new MenuItem("Light Mode");
		darkMode = new MenuItem("Dark Mode");
		newGame = new MenuItem("New Game");
		views.getItems().addAll(darkMode, lightMode);
		settings = new Menu("Settings");
		settings.getItems().addAll(views);
		settings.getItems().addAll(newGame);
		MenuBar menuBar = new MenuBar();
		menuBar.getMenus().addAll(settings);

		// END OF MENU SETUP

		VBox root = new VBox(menuBar);
		root.setSpacing(SPACING);
		root.setPadding(new Insets(SPACING));
		root.setAlignment(Pos.TOP_CENTER);

		// Add the title label
		titleLabel = new Label("Wordle");
		titleLabel.setFont(Font.font("Georgia", FontWeight.BOLD, 24));
		root.getChildren().add(titleLabel);

		// Add the grid of boxes
		GridPane boxGrid = new GridPane();

		boxGrid.setHgap(SPACING);
		boxGrid.setVgap(SPACING);
		for (int i = 0; i < NUM_ROWS; i++) {
			for (int j = 0; j < NUM_COLS; j++) {
				Button box = new Button();
				box.setPrefSize(BOX_SIZE, BOX_SIZE + 30);

				// find a way to disable button without making it low opacity
				// box.setDisable(true);
				box.setStyle("-fx-background-color: #ffffff; -fx-border-color: #d4d6da; -fx-border-width: 2;");

				box.setTextFill(Color.BLACK);
				box.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 17));
				boxes[i][j] = box;
				boxGrid.add(box, j, i);
			}
		}
		HBox boxGridWrapper = new HBox(boxGrid);
		boxGridWrapper.setAlignment(Pos.CENTER);
		root.getChildren().add(boxGridWrapper);

		// Add the keyboard
		char[][] keyboardLayout = { { 'Q', 'W', 'E', 'R', 'T', 'Y', 'U', 'I', 'O', 'P' },
				{ 'A', 'S', 'D', 'F', 'G', 'H', 'J', 'K', 'L' }, { '⏎', 'Z', 'X', 'C', 'V', 'B', 'N', 'M', '⌫' } };
		VBox keyboard = new VBox();
		keyboard.setSpacing(SPACING);
		keyboard.setAlignment(Pos.CENTER);
		buttons = new ArrayList<>();
		
		// Create and return the scene
		Scene scene = new Scene(root, 500, 600);
				
		// calls method which lays out the colors for the gui's light and dark mode
		menuHandler(root, menuBar, primaryStage);
				
		for (char[] row : keyboardLayout) {
			HBox rowBox = new HBox();
			rowBox.setSpacing(SPACING);
			rowBox.setAlignment(Pos.CENTER);
			for (char c : row) {

				Button button = new Button(Character.toString(c));
				button.setStyle("-fx-background-color: #d4d6da;");
				button.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 15));
				buttons.add(button);
				button.setPrefSize(BOX_SIZE + 75, BOX_SIZE + 50);
				// if dark mode is clicked then change buttons to white text

				button.setOnAction(event -> handleLetterButtonClick(button.getText(), primaryStage));
				rowBox.getChildren().add(button);

			}
			keyboard.getChildren().add(rowBox);
		}
		root.getChildren().add(keyboard);

    	if (lightModeBool) {
    		lightModeBool = false;
    		lightMode.fire();
    	}
    	
    	if (darkModeBool) {
    		darkModeBool = false;
    		darkMode.fire();
    	}
    	else {
    		lightMode.fire();
    	}

		/*
		 * THIS CODE HANDLES IF THE KEYBOARD IS USED TO TYPE A LETTER
		 */
		scene.setOnKeyPressed((KeyEvent event) -> {
			dict = RandomWordGenerator.getDictionary();

			String letter = event.getText().toUpperCase();
			// event.getCode().equals(KeyCode.ENTER);
			// event.getCode().equals(KeyCode.BACK_SPACE);

			if (won || finLoss) {
				return;
			}
			if (!event.getCode().equals(KeyCode.ENTER) && !event.getCode().equals(KeyCode.BACK_SPACE)) {
				if (colIndex > 4 && row >= 5) {
					// @TODO set up end on full board
					Alert alert = new Alert(Alert.AlertType.INFORMATION);
					alert.setHeaderText("Can't type any more");
					alert.setContentText("Row is full");
					alert.show();
					Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(2), even -> {
						alert.setResult(ButtonType.CLOSE);
						alert.close();
					}));
					timeline.play();

					return;
				} else if (colIndex == 5) {
					Alert alert = new Alert(Alert.AlertType.INFORMATION);
					alert.setHeaderText("Can't type any more");
					alert.setContentText("Row is full");
					alert.show();
					Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(2), even -> {
						alert.setResult(ButtonType.CLOSE);
						alert.close();
					}));
					timeline.play();

					return;
				} else {
					boxes[row][colIndex].setText(letter);
					if (darkModeBool) {
						boxes[row][colIndex].setStyle(
								"-fx-background-color: #000000; -fx-border-color: #818384; -fx-border-width: 2; -fx-text-fill: white;");
					} else if (lightModeBool) {
						boxes[row][colIndex].setStyle(
								"-fx-background-color: #ffffff; -fx-border-color: black; -fx-border-width: 2;");
					}
					colIndex += 1;

				}
			} else {
				if (event.getCode().equals(KeyCode.BACK_SPACE) && colIndex > 0) {
					colIndex -= 1;
					boxes[row][colIndex].setText("");
					if (darkModeBool) {
						boxes[row][colIndex].setStyle(
								"-fx-background-color: #000000; -fx-border-color: #3a3a3c; -fx-border-width: 2; -fx-text-fill: white;");
					} else if (lightModeBool) {
						boxes[row][colIndex].setStyle(
								"-fx-background-color: #ffffff; -fx-border-color: #d4d6da; -fx-border-width: 2;");
					}

				} else if (event.getCode().equals(KeyCode.ENTER)) {
					String word = boxes[row][0].getText() + boxes[row][1].getText() + boxes[row][2].getText()
							+ boxes[row][3].getText() + boxes[row][4].getText();
					word = word.strip().toLowerCase();
					if (word.length() != 5) {
						// @TODO show that you must have a 5 letter word maybe with an alert
						Alert alert = new Alert(Alert.AlertType.INFORMATION);
						alert.setHeaderText("Invalid Word");
						alert.setContentText("Word must be 5 letters");
						alert.show();
						Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(2), even -> {
							alert.setResult(ButtonType.CLOSE);
							alert.close();
						}));
						timeline.play();
					}

					else if (!dict.contains(word)) {
						Alert alert = new Alert(Alert.AlertType.INFORMATION);
						alert.setHeaderText("Word not in dictionary");
						alert.setContentText("Please enter a valid word");
						alert.show();
						Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(2), even -> {
							alert.setResult(ButtonType.CLOSE);
							alert.close();
						}));
						timeline.play();
						return;
					} else {
						won = wordle.checkWord(word);
						ArrayList<String> correctLetters = new ArrayList<>();

						if (won) {
							// Plays mp3 when you win
							String songToPlay = "music/WordleWinZelda.mp3";
							String path = songToPlay;
							File file = new File(path);
							URI uri = file.toURI();
							Media media = new Media(uri.toString());
							mediaPlayer = new MediaPlayer(media);
							mediaPlayer.play();

							for (int i = 0; i < 5; i++) {
								boxes[row][i].setStyle("-fx-background-color: #6ca965; -fx-text-fill: white;");
								for (Button button : buttons) {
									if (button.getText().equals(boxes[row][i].getText())) {

										button.setStyle("-fx-background-color: #6ca965; -fx-text-fill: white;");

									}
								}
								correctLetters.add(boxes[row][i].getText());
							}

							System.out.println("Congrats!");
							
							
							// @TODO set what happens when you win!!!!!!!!
							currentUser.wins ++;
							currentUser.streak ++;
							primaryStage.setScene(statsView(primaryStage));
							
							
							
						} else {
							ArrayList<String> incorrectLetters = new ArrayList<>();
							ArrayList<String> semiCorrectLetters = new ArrayList<>();

							int[] checks = wordle.getChecks();

							if (darkModeBool) {
								for (int i = 0; i < 5; i++) {
									if (checks[i] == 0) {
										boxes[row][i].setStyle("-fx-background-color: #3a3a3c; -fx-text-fill: white;");

										incorrectLetters.add(Character.toString(word.charAt(i)));
									} else if (checks[i] == 1) {
										boxes[row][i].setStyle("-fx-background-color: #c8b653; -fx-text-fill: white;");
										semiCorrectLetters.add(Character.toString(word.charAt(i)));

									} else if (checks[i] == 2) {
										boxes[row][i].setStyle("-fx-background-color: #6ca965; -fx-text-fill: white;");
										correctLetters.add(Character.toString(word.charAt(i)));

									}
								}
								for (Button button : buttons) {
									if (correctLetters.contains(button.getText().toLowerCase())) {
										button.setStyle("-fx-background-color: #6ca965; -fx-text-fill: white;");
									} else if (semiCorrectLetters.contains(button.getText().toLowerCase())) {
										button.setStyle("-fx-background-color: #c8b653; -fx-text-fill: white;");

									}
									if (incorrectLetters.contains(button.getText().toLowerCase())
											&& !goalWord.contains(button.getText().toLowerCase())) {
										button.setStyle("-fx-background-color: #3a3a3c; -fx-text-fill: white;");
									}
								}
								if (!won && row == 5 && colIndex == 5) {
									String songToPlay = "music/LossSoundZelda.mp3";
									String path = songToPlay;
									File file = new File(path);
									URI uri = file.toURI();
									Media media = new Media(uri.toString());
									mediaPlayer = new MediaPlayer(media);
									mediaPlayer.play();

									finLoss = true;
									System.out.println("You lost");
									System.out.println("Word was: " + goalWord);
									currentUser.losses ++;
									currentUser.streak = 0;
									primaryStage.setScene(statsView(primaryStage));
									return;

								}
							}

							if (lightModeBool) {
								for (int i = 0; i < 5; i++) {
									if (checks[i] == 0) {
										boxes[row][i].setStyle("-fx-background-color: #787c7f; -fx-text-fill: white;");

										incorrectLetters.add(Character.toString(word.charAt(i)));
									} else if (checks[i] == 1) {
										boxes[row][i].setStyle("-fx-background-color: #c8b653; -fx-text-fill: white;");
										semiCorrectLetters.add(Character.toString(word.charAt(i)));

									} else if (checks[i] == 2) {
										boxes[row][i].setStyle("-fx-background-color: #6ca965; -fx-text-fill: white;");
										correctLetters.add(Character.toString(word.charAt(i)));

									}
								}
								for (Button button : buttons) {
									if (correctLetters.contains(button.getText().toLowerCase())) {
										button.setStyle("-fx-background-color: #6ca965; -fx-text-fill: white;");
									} else if (semiCorrectLetters.contains(button.getText().toLowerCase())) {
										button.setStyle("-fx-background-color: #c8b653; -fx-text-fill: white;");

									}
									if (incorrectLetters.contains(button.getText().toLowerCase())
											&& !goalWord.contains(button.getText().toLowerCase())) {
										button.setStyle("-fx-background-color: #787c7f; -fx-text-fill: white;");
									}
								}
								if (!won && row == 5 && colIndex == 5) {

									String songToPlay = "music/LossSoundZelda.mp3";
									String path = songToPlay;
									File file = new File(path);
									URI uri = file.toURI();
									Media media = new Media(uri.toString());
									mediaPlayer = new MediaPlayer(media);
									mediaPlayer.play();

									finLoss = true;
									System.out.println("You lost");
									System.out.println("Word was: " + goalWord);
									currentUser.losses ++;
									currentUser.streak = 0;
									primaryStage.setScene(statsView(primaryStage));
									return;

								}
							}

						}
						row += 1;
						colIndex = 0;

					}
				}
			}

		});

		primaryStage.setTitle("Wordle");
		primaryStage.setScene(scene);
		return scene;
	}

	/**
	 * Handles the event when a letter is clicked.
	 * @param letter the String representation of the letter that was clicked by the user
	 * @param primaryStage the Stage which holds all the GUI information and objects
	 */
	private void handleLetterButtonClick(String letter, Stage  primaryStage) {
		/*
		 * if the button clicked is a letter, check if it is in the index bounds, and if
		 * so add it and iterate
		 */
		dict = RandomWordGenerator.getDictionary();

		if (won || finLoss) {
			return;
		}
		if (!letter.equals("⏎") && !letter.equals("⌫")) {
			if (won) {

			}
			if (colIndex > 4 && row >= 5) {
				// @TODO set up end on full board
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				alert.setHeaderText("Can't type any more");
				alert.setContentText("Row is full");
				alert.show();
				Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(2), event -> {
					alert.setResult(ButtonType.CLOSE);
					alert.close();
				}));
				timeline.play();

				return;
			} else if (colIndex == 5) {
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				alert.setHeaderText("Can't type any more");
				alert.setContentText("Row is full");
				alert.show();
				Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(2), event -> {
					alert.setResult(ButtonType.CLOSE);
					alert.close();
				}));
				timeline.play();

				return;
				// @TODO show that you cant type any more maybe with alert
			} else {

				boxes[row][colIndex].setText(letter);
				if (darkModeBool) {
					boxes[row][colIndex].setStyle(
							"-fx-background-color: #000000; -fx-border-color: #818384; -fx-border-width: 2; -fx-text-fill: white;");
				} else if (lightModeBool) {
					boxes[row][colIndex]
							.setStyle("-fx-background-color: #ffffff; -fx-border-color: black; -fx-border-width: 2;");
				}
				colIndex += 1;

			}

			/*
			 * if the letter is backspace, decrement and reset text, if it is enter, check
			 * if the word is 5 letters, and if so, check and show correct letters and
			 * positions.
			 */
		} else {
			if (letter.equals("⌫") && colIndex > 0) {
				colIndex -= 1;
				boxes[row][colIndex].setText("");
				if (darkModeBool) {
					boxes[row][colIndex].setStyle(
							"-fx-background-color: #000000; -fx-border-color: #3a3a3c; -fx-border-width: 2; -fx-text-fill: white;");
				} else if (lightModeBool) {
					boxes[row][colIndex]
							.setStyle("-fx-background-color: #ffffff; -fx-border-color: #d4d6da; -fx-border-width: 2;");
				}

			} else if (letter.equals("⏎")) {
				String word = boxes[row][0].getText() + boxes[row][1].getText() + boxes[row][2].getText()
						+ boxes[row][3].getText() + boxes[row][4].getText();
				word = word.strip().toLowerCase();
				if (word.length() != 5) {
					// @TODO show that you must have a 5 letter word maybe with an alert
					Alert alert = new Alert(Alert.AlertType.INFORMATION);
					alert.setHeaderText("Invalid Word");
					alert.setContentText("Word must be 5 letters");
					alert.show();
					Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(2), event -> {
						alert.setResult(ButtonType.CLOSE);
						alert.close();
					}));
					timeline.play();
				}

				else if (!dict.contains(word)) {
					Alert alert = new Alert(Alert.AlertType.INFORMATION);
					alert.setHeaderText("Word not in dictionary");
					alert.setContentText("Please enter a valid word");
					alert.show();
					Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(2), event -> {
						alert.setResult(ButtonType.CLOSE);
						alert.close();
					}));
					timeline.play();
					return;
				} else {
					won = wordle.checkWord(word);
					if (won) {
						for (int i = 0; i < 5; i++) {
							boxes[row][i].setStyle("-fx-background-color: #6ca965; -fx-text-fill: white;");
						}
						System.out.println("Congrats!");
						
						
						// @TODO set what happens when you win!!!!!!!!
						currentUser.wins ++;
						currentUser.streak ++;
						primaryStage.setScene(statsView(primaryStage));

						
						
					} else {
						ArrayList<String> incorrectLetters = new ArrayList<>();
						ArrayList<String> correctLetters = new ArrayList<>();
						ArrayList<String> semiCorrectLetters = new ArrayList<>();

						int[] checks = wordle.getChecks();

						if (darkModeBool) {
							for (int i = 0; i < 5; i++) {
								if (checks[i] == 0) {
									boxes[row][i].setStyle("-fx-background-color: #3a3a3c; -fx-text-fill: white;");

									incorrectLetters.add(Character.toString(word.charAt(i)));
								} else if (checks[i] == 1) {
									boxes[row][i].setStyle("-fx-background-color: #c8b653; -fx-text-fill: white;");
									semiCorrectLetters.add(Character.toString(word.charAt(i)));

								} else if (checks[i] == 2) {
									boxes[row][i].setStyle("-fx-background-color: #6ca965; -fx-text-fill: white;");
									correctLetters.add(Character.toString(word.charAt(i)));

								}
							}
							for (Button button : buttons) {
								if (correctLetters.contains(button.getText().toLowerCase())) {
									button.setStyle("-fx-background-color: #6ca965; -fx-text-fill: white;");
								} else if (semiCorrectLetters.contains(button.getText().toLowerCase())) {
									button.setStyle("-fx-background-color: #c8b653; -fx-text-fill: white;");

								}
								if (incorrectLetters.contains(button.getText().toLowerCase())
										&& !goalWord.contains(button.getText().toLowerCase())) {
									button.setStyle("-fx-background-color: #3a3a3c; -fx-text-fill: white;");
								}
							}
							if (!won && row == 5 && colIndex == 5) {
								finLoss = true;
								System.out.println("You lost");
								System.out.println("Word was: " + goalWord);
								currentUser.losses ++;
								currentUser.streak = 0;
								primaryStage.setScene(statsView(primaryStage));
								return;

							}
						}

						if (lightModeBool) {
							for (int i = 0; i < 5; i++) {
								if (checks[i] == 0) {
									boxes[row][i].setStyle("-fx-background-color: #787c7f; -fx-text-fill: white;");

									incorrectLetters.add(Character.toString(word.charAt(i)));
								} else if (checks[i] == 1) {
									boxes[row][i].setStyle("-fx-background-color: #c8b653; -fx-text-fill: white;");
									semiCorrectLetters.add(Character.toString(word.charAt(i)));

								} else if (checks[i] == 2) {
									boxes[row][i].setStyle("-fx-background-color: #6ca965; -fx-text-fill: white;");
									correctLetters.add(Character.toString(word.charAt(i)));

								}
							}
							for (Button button : buttons) {
								if (correctLetters.contains(button.getText().toLowerCase())) {
									button.setStyle("-fx-background-color: #6ca965; -fx-text-fill: white;");
								} else if (semiCorrectLetters.contains(button.getText().toLowerCase())) {
									button.setStyle("-fx-background-color: #c8b653; -fx-text-fill: white;");

								}
								if (incorrectLetters.contains(button.getText().toLowerCase())
										&& !goalWord.contains(button.getText().toLowerCase())) {
									button.setStyle("-fx-background-color: #787c7f; -fx-text-fill: white;");
								}
							}
							if (!won && row == 5 && colIndex == 5) {
								finLoss = true;
								System.out.println("You lost");
								System.out.println("Word was: " + goalWord);
								currentUser.losses ++;
								currentUser.streak = 0;
								primaryStage.setScene(statsView(primaryStage));
								return;

							}
						}

					}
					row += 1;
					colIndex = 0;

				}
			}
		}

	}
	
	/**
	 * Handles the menu button with dark/light mode and new game.
	 * @param root VBox that controls which stage is being shown 
	 * @param menuBar The menu bar to switch between light and dark mode, as well as 
	 * start a new game
	 * @param primaryStage the Stage which holds all the GUI information and objects
	 */
	public void menuHandler(VBox root, MenuBar menuBar, Stage primaryStage) {
		darkMode.setOnAction((event) -> {
			settings.setStyle("-fx-background-color: grey;");
			if (darkModeBool) {
				return;
			}
			root.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
			menuBar.setStyle("-fx-background-color: #000000; -fx-text-fill: #ffffff;");
			titleLabel.setStyle("-fx-text-fill: #ffffff;");

			for (Button button : buttons) {
				String[] styleForBox = button.getStyle().split(" ");
				// if the color means it is incorrect change it to dark mode incorrect color
				if (styleForBox[1].equals("#787c7f;")) {
					button.setStyle("-fx-background-color: #3a3a3c; -fx-text-fill: white;");
				}
				// if the color is neutral, change it to the black neutral color
				else if (styleForBox[1].equals("#d4d6da;")) {
					button.setStyle("-fx-background-color: #818384; -fx-text-fill: white;");

				}

			}
			for (int i = 0; i < 6; i++) {
				for (int j = 0; j < 5; j++) {
					String[] styleForBox = boxes[i][j].getStyle().split(" ");
					// if it is green or yellow ignore
					if (styleForBox[1].equals("#6ca965;") || styleForBox[1].equals("#c8b653;")) {
						continue;
					}
					// if it is incorrect change it to the dark incorrect color
					else if (styleForBox[1].equals("#787c7f;")) {
						boxes[i][j].setStyle("-fx-background-color: #3a3a3c; -fx-text-fill: white;");
						continue;
					}
					// otherwise if it is empty change it to the corresponding color
					if (boxes[i][j].getText().equals("")) {
						boxes[i][j].setStyle(
								"-fx-background-color: #000000; -fx-border-color: #3a3a3c; -fx-border-width: 2; -fx-text-fill: white;");
					} else {
						// if the box currently contains a letter, set the according values
						boxes[i][j].setStyle(
								"-fx-background-color: #000000; -fx-border-color: #818384; -fx-border-width: 2; -fx-text-fill: white;");

					}

				}
			}
			darkModeBool = true;
			lightModeBool = false;

		});

		// END OF DARK MODE SETUP

		lightMode.setOnAction((event) -> {
			settings.setStyle("-fx-background-color: lightgrey;");
			if (lightModeBool) {
				return;
			}
			root.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
			menuBar.setStyle("-fx-background-color: #ffffff; -fx-text-fill: #000000;");
			titleLabel.setStyle("-fx-text-fill: #000000;");

			for (Button button : buttons) {
				String[] styleForBox = button.getStyle().split(" ");
				// if the color is incorrect change it to the lght mode incorrect
				if (styleForBox[1].equals("#3a3a3c;")) {
					button.setStyle("-fx-background-color: #787c7f; -fx-text-fill: white;");
				}
				// if the color is neutral, change it to the light neutral color
				else if (styleForBox[1].equals("#818384;")) {
					button.setStyle("-fx-background-color: #d4d6da; -fx-text-fill: black;");

				}

			}

			for (int i = 0; i < 6; i++) {
				for (int j = 0; j < 5; j++) {
					String[] styleForBox = boxes[i][j].getStyle().split(" ");
					// if the box is green or yellow, ignore
					if (styleForBox[1].equals("#6ca965;") || styleForBox[1].equals("#c8b653;")) {
						continue;
					}

					// if the box is an incorrect letter, sets it to the light mode equivalent for
					// incorrect
					else if (styleForBox[1].equals("#3a3a3c;")) {
						boxes[i][j].setStyle("-fx-background-color: #787c7f; -fx-text-fill: white;");
						continue;
					}

					// if the box is empty, sets the colors to the according values
					if (boxes[i][j].getText().equals("")) {
						boxes[i][j].setStyle(
								"-fx-background-color: #ffffff; -fx-border-color: #d4d6da; -fx-border-width: 2;");
					} else {
						// if the box currently has a letter, sets the colors to the according values
						boxes[i][j].setStyle(
								"-fx-background-color: #ffffff; -fx-border-color: black; -fx-border-width: 2;");
					}

				}
			}

			darkModeBool = false;
			lightModeBool = true;

		});

		newGame.setOnAction((event) -> {
			try {
				goalWord = RandomWordGenerator.getWord();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			wordle = new Wordle(goalWord);
			System.out.println(goalWord);
			row = 0;
			colIndex = 0;
			won = false;
			finLoss = false;

			// if it is light mode, reset all the colors to default light mode colors
			// and erase letters
			if (lightModeBool) {
				for (Button button : buttons) {
					button.setStyle("-fx-background-color: #d4d6da; -fx-text-fill: black;");

				}

				for (int i = 0; i < 6; i++) {
					for (int j = 0; j < 5; j++) {

						boxes[i][j].setStyle(
								"-fx-background-color: #ffffff; -fx-border-color: #d4d6da; -fx-border-width: 2;");
						boxes[i][j].setText("");

					}
				}
			} else if (darkModeBool) {
				for (Button button : buttons) {
					button.setStyle("-fx-background-color: #818384; -fx-text-fill: white;");
				}

				for (int i = 0; i < 6; i++) {
					for (int j = 0; j < 5; j++) {

						boxes[i][j].setStyle(
								"-fx-background-color: #000000; -fx-border-color: #3a3a3c; -fx-border-width: 2; -fx-text-fill: white;");
						boxes[i][j].setText("");

					}
				}
			}

		});
		
	}
	
	/**
	 * Sets the post-game view of the users stats, and a new game button.
	 * @param primaryStage the Stage which holds all the GUI information and objects
	 * @return the scene of users stats.
	 */
	private Scene statsView(Stage primaryStage) {
	    VBox root = new VBox();
	    root.setAlignment(Pos.CENTER);
	    root.setSpacing(10);

	    Label titleLabel = new Label("Stats");
	    titleLabel.setFont(Font.font(24));
	    root.getChildren().add(titleLabel);

	    Label usernameLabel = new Label(currentUser.getUsername());
	    root.getChildren().add(usernameLabel);
	    
	    int wins = currentUser.wins;
	    int losses = currentUser.losses;
	    int gamesPlayed = wins + losses;
	    int winPercentage = (wins * 100) / gamesPlayed;
	    int winStreak = currentUser.streak;
	    
	    Label gamesPlayedLabel = new Label("Games Played: " + gamesPlayed);
	    root.getChildren().add(gamesPlayedLabel);

	    Label gamesWonLabel = new Label("Games Won: " + wins);
	    root.getChildren().add(gamesWonLabel);
	    
	    Label winPercentageLabel = new Label("Win Percentage: " + winPercentage + '%');
	    root.getChildren().add(winPercentageLabel);
	    
	    Label winStreakLabel = new Label("Win Streak: " + winStreak);
	    root.getChildren().add(winStreakLabel);
	    
	    currentUser.addNewResult(row);
	    
	    //Make bar graph
	    String oneString = "1";  
	    String twoString = "2";  
	    String threeString = "3";  
	    String fourString = "4";  
	    String fiveString = "5";
	    String sixString = "6";
	    
	    CategoryAxis yaxis= new CategoryAxis();  
	    int maxGuess = Collections.max(Arrays.asList(currentUser.getOneGuess(),currentUser.getTwoGuesses(),
	    		currentUser.getThreeGuesses(),currentUser.getFourGuesses(),
	    		currentUser.getFiveGuesses(),currentUser.getSixGuesses()));
	    NumberAxis xaxis = new NumberAxis(0,maxGuess,1);   
	    xaxis.setLabel("Number of Guesses"); 
	    yaxis.setLabel("Correct Guess Length"); 
	    
	    BarChart<Integer,String> bar = new BarChart(xaxis,yaxis);  
	    bar.setTitle("Guess Distribution");  
	    
	    XYChart.Series<Integer,String> series = new XYChart.Series<>();  
	    series.getData().add(new XYChart.Data(currentUser.getOneGuess(),oneString));  
	    series.getData().add(new XYChart.Data(currentUser.getTwoGuesses(),twoString));  
	    series.getData().add(new XYChart.Data(currentUser.getThreeGuesses(),threeString));  
	    series.getData().add(new XYChart.Data(currentUser.getFourGuesses(),fourString));
	    series.getData().add(new XYChart.Data(currentUser.getFiveGuesses(),fiveString));
	    series.getData().add(new XYChart.Data(currentUser.getSixGuesses(),sixString));
	    
	    bar.getData().add(series); 
	    
	    root.getChildren().add(bar);

	    Button backButton = new Button("Play Again");
	    Button logoutButton = new Button("Logout");

	    
	    backButton.setOnAction(event -> {
	    	primaryStage.setScene(wordleView(primaryStage));
	    	
	    	if (lightModeBool) {
	    		lightModeBool = false;
	    		lightMode.fire();
	    	}
	    	
	    	if (darkModeBool) {
	    		darkModeBool = false;
	    		darkMode.fire();
	    	}

			// if it is light mode, reset all the colors to default light mode colors
			// and erase letters
			newGame.fire();

		});
	    logoutButton.setOnAction(event -> {
	    	
	    	try {
	    		saveUsers();
				start(primaryStage);
				newGame.fire();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		});

	    root.getChildren().add(backButton);
	    root.getChildren().add(logoutButton);


	    Scene scene = new Scene(root, 400, 600);

	    return scene;
	}

}