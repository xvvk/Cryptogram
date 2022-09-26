import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

/* PHRASES
today is a beautiful sunny day
pizza is the best food on the planet
computer science is very complex
summer is coming soon i cannot wait
glasgow is a friendly city
spring is peaceful and beautiful
there is nothing to watch on netflix
i love all things chocolate
when will this pandemic end
the dentist is a scary place
the sky is blue the grass is green
i hate winter as it is too cold
the great british bake off is elite
one day i want to visit new york
i will learn to play the piano
 */

public class game implements Serializable{

	private static final long serialVersionUID = 1L;
	private player currentplayer;
	private String type;
	private static players allplayers;
	private boolean check = false;
	cryptogram current;
	
	private static ArrayList<player> playersList;
	private ArrayList<String> encrypted;
	private ArrayList<Character> guesses;
	private ArrayList<String> record;
	private List<Character> commonLetters; // fixed list for common letter frequencies
	ArrayList<Character> hints; // arraylist for hints

	public game(player p, String type) {
		this.currentplayer = p;
		this.type = type;
		guesses = new ArrayList<>();
		record = new ArrayList<>();
		encrypted = new ArrayList<>();
		playersList = new ArrayList<player>();
		hints = new ArrayList<>();
	}

	public player getCurrentplayer() {
		return currentplayer;
	}
	public String getType(){
		return type;
	}
	public ArrayList<String> getEncrypted(){
		return encrypted;
	}
	public ArrayList<Character> getGuesses(){
		return guesses;
	}
	public ArrayList<String> getrecord(){
		return record;
	}
	public List<Character> getCommonLetters() {
		return commonLetters;
	}
	

	@SuppressWarnings("resource")
	private static String readLetter(){ // reads string from input
		Scanner reader;
		String input;
		reader = new Scanner(System.in);
		input = reader.next();
		return input;
	}

	@SuppressWarnings("resource")
	private int readNumber() { // reads string from input
		Scanner reader;
		int input = 0;
		reader = new Scanner(System.in);
		try{
			input = reader.nextInt();
		}catch(InputMismatchException e){
			System.err.println("Must be a number!");
		}
		return input;
	}

	public void generatecryptogram() throws FileNotFoundException {
		String filePath = "phrases.txt";
		currentplayer.incrementsCryptogramsPlayed();
		if (type.equals("letter")) {
			current =  new lettercryptogram(filePath);
		} else {
			current = new numbercryptogram(filePath);
		}
		encrypted = current.getEncryptedPhrase();

		if (type.equals("letter")) {
			for(String current : encrypted) {
				char c = current.charAt(0);
				if (!((int) c >= 97 && (int) c <= 122)) {
					guesses.add(c);
				} else {
					guesses.add('-');
				}
			}
		}else if (type.equals("number")) {
			for(String current : encrypted) {
				try {
					Integer.parseInt(current);
					guesses.add('-');
				}catch(NumberFormatException e) {
					guesses.add(current.charAt(0));
				}
			}
		}
	}

	public void playgame() throws FileNotFoundException { // runs the main game
		String input;
		boolean solutionDisplayed = false;
		while (!checkSolved() && solutionDisplayed == false ) {
				System.out.println();
				printgame();
				System.out.println(" ");
				System.out.println("What would you like to do?");
				System.out.println("Type 'guess' to guess a letter." );
				System.out.println("     'frequency' to get the frequencies of each letter." );
				System.out.println("     'undo' to undo the last guess." );
				System.out.println("     'hint' to reveal a letter." );
				System.out.println("     'solution' to show the solution.");
				System.out.println("     'save' to save your current game." );
				System.out.println("     'quit' to quit the game."); 
				
				
				boolean valid = false;
				while (!valid) {
					input = readLetter();
					char currentMapping;
					switch (input) {
					case "guess":
						if (type.equals("letter")) {
							currentMapping = assignLetter();
						} else {
							currentMapping = assignNumber();
						}
						if (currentMapping == '-') {
							enterLetter();
						} else {
							overwrite(currentMapping);
						}
						valid = true;
						updateStats();
						break;
					case "undo":
						undoLetter();
						valid = true;
						break;
					case "frequency":
						if (type.equals("letter")) {
							showLetFrequencies();
						} else {
							showNumFrequencies();
						}
						valid = true;
						break;
					case "hint":
						if(type.equals("letter")) {
							letterhint();
						} else {
							numberhint();
						}
						valid = true;
						break;
					case "solution":
						showSolution();
						solutionDisplayed = true;
						valid = true;
						break;
					case "save":
						overwritegame();
						valid = true;
						break;
					case "quit":
						valid = true;
						System.out.println("Exiting the game... See you next time!");
						updateStats(); // stats are updated
						saveStats();  // so stats are saved to file
						System.exit(0);
						break;
					default:
						System.err.println("Please enter either: ");
						System.err.println(" 'guess'");
						System.err.println(" 'undo'");
						System.err.println(" 'frequency'");
						System.err.println(" 'hint'");
						System.err.println(" 'solution'");
						System.err.println(" 'save'");
						System.err.println(" 'quit'");
						
						break;
					}
				}
			}
		
		if(solutionDisplayed == false) {
			printgame();
			System.out.println(" ");
			System.out.println("Congratulations, you completed the cryptogram!");
			currentplayer.incrementCryptogramsCompleted();
			updateStats();
		
			System.out.println(" ");
			System.out.println("Would you like to view your updated stats? yes/no");
			boolean valid = false;
			while(!valid) {
				String userResponse = readLetter();
				if (userResponse.equals("yes")) {
	
					String[] Stats = Statistics(currentplayer);
					System.out.println(" ");
					System.out.println("Here are your updated stats " + Stats[0] + "!");
					System.out.println(" ");
					System.out.println("Accuracy = " + Stats[1]);
					System.out.println("Total number of guesses = " + Stats[2]);
					System.out.println("Number of correct guesses = " + Stats[3]);
					System.out.println("Cryptograms played = " + Stats[4]);
					System.out.println("Cryptograms completed = " + Stats[5]);
					System.out.println(" ");
					valid = true;
				}
				else if (userResponse.equals("no")) {
					System.out.println(" ");
					valid = true;
				}
				else{
					System.err.println("Please enter 'yes' to load statistics or 'no' to continue");
				}
			}
		}
	}

	public void setAllplayers(players allplayers) {
		game.allplayers = allplayers;
	}

	public static player loadplayer(String username) {
		player currentPlayer;
		allplayers = new players();
		if (username == null) {
			System.err.println("Please re-enter your username!");
		}
		currentPlayer = allplayers.existingPlayer(username);
		if (currentPlayer == null) {
			System.out.println("There's no player with that username. A new user will be created for you!");
			System.out.println(" ");
			currentPlayer = new player(username, 0, 0, 0, 0, 0);
			allplayers.addPlayer(currentPlayer);
			return currentPlayer;
		}
		else {
			System.out.println("Would you like to load your details? yes/no");

			boolean valid = false;
			while(!valid) {
				String userResponse = readLetter();
				if (userResponse.equals("yes")) {
					String[] Stats = Statistics(currentPlayer);
					System.out.println(" ");
					System.out.println("Welcome back " + Stats[0] + "!");
					System.out.println(" ");
					System.out.println("Your details are: " );
					System.out.println("Accuracy = " + Stats[1]);
					System.out.println("Total number of guesses = " + Stats[2]);
					System.out.println("Number of correct guesses = " + Stats[3]);
					System.out.println("Cryptograms played = " + Stats[4]);
					System.out.println("Cryptograms completed = " + Stats[5]);
					System.out.println(" ");
					valid = true;
				}
				else if (userResponse.equals("no")) {
					System.out.println(" ");
					System.out.println("Welcome back " + currentPlayer.getUsername() + "!");
					System.out.println(" ");
					valid=true;
				}
				else{
					System.err.println("Please enter 'yes' to load statistics or 'no' to continue");
				}
			}
		}
		return currentPlayer;
	}

	public void updateStats() { // updates stats, does not save them to file
		if (currentplayer.getTotalGuesses() != 0 && currentplayer.getCorrectGuesses() != 0) {
			currentplayer.updateAccuracy();
		}
		allplayers.updatePlayer(currentplayer);
	}
	
	public static void saveStats() { // saves stats to file
		allplayers.saveplayers();
	}

	public void updategame() {
		int i = 0;
		if (type.equals("letter")) {
			lettercryptogram let = (lettercryptogram) current;
			for (String cur : encrypted) {
				char c = cur.charAt(0);
				if (((int) c >= 97 && (int) c <= 122)) {
					guesses.set(i, let.fetchVal(c));
				}
				i++;
			}
		} else if (type.equals("number")) {
			numbercryptogram num = (numbercryptogram) current;
			for (String cur : encrypted) {
				try {
					int n = Integer.parseInt(cur);
					guesses.set(i, num.fetchVal(n));
				} catch(NumberFormatException e) {
					//This is expected for punctuation!
				} finally{
					i++;
				}
			}
		}
	}

	public void enterLetter() { //enters letter to '-' spaces
		System.out.println("Enter a letter to map.");
		char letter = checkLetter();
		currentplayer.incrementTotalGuesses();
		if (current.updateMappings(letter)) {
			currentplayer.incrementCorrectGuesses();
		}
		currentplayer.updateAccuracy();
		updategame();
	}

	public void overwrite(char current){ // overwrites existing mapping or skips letter
		printgame();
		System.out.println("You have already guessed the letter '" + current + "' for this value.");
		System.out.println("Would you like to overwrite this letter? yes/no");
		boolean valid = false;
		while(!valid){
			String answer = readLetter();
			if (answer.equals("yes")){
				enterLetter();
				valid = true;
			}else if (answer.equals("no")){
				System.out.println("Keeping previous guess...");
				valid = true;
			}else {
				System.err.println("Please enter either 'yes' or 'no'");
			}
		}
	}

	public void undoLetter() {
		System.err.println("Size is : " + record.size());
		if (record.size() > 0){
			String s = record.get(record.size()-1);
			if (type.equals("letter")){
				lettercryptogram let = (lettercryptogram) current;
				char lastGuess = s.charAt(0);
				if(let.undoGuess(lastGuess)) {
					currentplayer.decrementCorrectGuesses();
				}
			} else {
				numbercryptogram num = (numbercryptogram) current;
				int lastGuess = Integer.parseInt(s);
				if(num.undoGuess(lastGuess)) {
					currentplayer.decrementCorrectGuesses();
				}
			}
			currentplayer.updateAccuracy();
			record.remove(s);
		}else{
			System.err.println("Nothing to undo!");
		}
		updategame();
	}

	public void printgame() { //prints user's solution so far
		System.out.println("Current puzzle state:");
		System.out.println(" ");
		for (int i = 0; i < guesses.size(); i++) {
			System.out.print(" " + current.getEncryptedPhrase().get(i) + " ");
		}
		System.out.println(" ");
		System.out.println();
		for (int i =0;i< guesses.size(); i++) {
			System.out.print(guesses.get(i));
		}
		System.out.println();
		boolean fullyGuessed = true;
		for(int a = 0; a < guesses.size(); a++){
			if(guesses.get(a) == '-'){ 
				fullyGuessed = false;}
		}
		if(fullyGuessed && check == false){
			System.out.println(" ");
			System.out.println("Looks like your guesses aren't all correct...");
		}
	}

	//INPUT VALIDATIONS

	public char assignLetter() { //checks if the mapping a user wants to make is valid
		boolean valid = false;
		lettercryptogram let = (lettercryptogram) current;
		String l = "";
		System.out.println("Enter the cryptogram letter you want to guess.");
		while (!valid) {
			l = readLetter();
			if (l.length() != 1) {  //NOT valid unless single character
				System.err.println("Please enter only a single letter.");
			} else if (!(let.checkKey(l.charAt(0)))){
				System.err.println("This isn't a letter in the cryptogram!");
			} else {
				valid = true;
			}
		}
		record.add(l);
		return let.getValue(l.charAt(0));
	}

	public char assignNumber(){ //checks if the mapping a user wants to make is valid
		boolean valid = false;
		numbercryptogram num = (numbercryptogram) current;
		int n = 0;
		System.out.println("Enter the cryptogram number you want to guess.");
		while (!valid) {
			n = readNumber();
			if (n < 1 || n > 26) {
				System.err.println("Please enter a number between 1 and 26.");
			} else if (!(num.checkKey(n))){
				System.err.println("This isn't a number in the cryptogram!");
			} else {
				valid = true;
			}
		}
		String guess = Integer.toString(n);
		record.add(guess);
		return num.getValue(n);
	}

	public char checkLetter() { //checks if letter is valid
		boolean valid = false;
		String l = "";
		while (!valid) { 			//repeats until valid
			l = readLetter();
			if (l.length() != 1) { 	//not valid unless single character
				System.err.println("Please enter only a single letter.");
			} else if ((int) l.charAt(0) < 97 || (int) l.charAt(0) > 122) { //NOT valid unless letter character
				System.err.println("Please enter a valid character!");
			} else if (current.isMapped(l.charAt(0))) { //NOT valid unless not already used
				System.err.println("You already chose this letter. Choose a different one!");
			} else {
				valid = true;
			}
		}
		return l.charAt(0);
	}

	private static void startgame(player p){
		System.out.println(" ");
		System.out.println("Please enter 'letter' for a Letter Cryptogram.");
		System.out.println("             'number' for a Number Cryptogram.");
		System.out.println("             'quit' to quit game.");

		try {
			game game = new game(p, getChoice());
			game.generatecryptogram();
			game.playgame();
		} catch (IOException | NullPointerException e) {
			e.printStackTrace();
		}
	}

	private static String getChoice() throws IOException {

		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		boolean check = false;
		String getChoice = null;

		while (!check) {
			getChoice = reader.readLine();
			if (getChoice.equals("quit")) {
				System.out.println("Exiting the game... See you next time!");
				saveStats();
				System.exit(0);
			} else if (getChoice.equals("letter") || getChoice.equals("number")) {
				check = true;
				getChoice = getChoice.equals("letter") ? "letter" : "number";   //if letter, get choice else number
				System.out.println(" ");
				System.out.println("You're now playing a " + getChoice + " cryptogram!");
			} else if(getChoice.equals("quit")) {
				System.out.println("Exiting the game... See you next time!");
				saveStats();
				System.exit(0);
			} else {
				System.err.println("Please enter a valid choice!");
			}
		}
		return getChoice;	
	}

	public static game loadgame(player p) {
		String filename = p.getUsername() + ".txt";
		File myfile = new File(filename);
		if (myfile.exists()){
			try{
				FileInputStream fi = new FileInputStream(myfile);
				ObjectInputStream oi = new ObjectInputStream(fi);
				game loadedGame = (game) oi.readObject();
				loadedGame.currentplayer = p;
				oi.close();
				fi.close();
				return loadedGame;
			}catch (FileNotFoundException e){
				System.err.println("File not found!");
			}catch (IOException e){
				System.err.println("Error initializing stream.");
			}catch (ClassNotFoundException e){
				e.printStackTrace();
			}
		} else {
			System.err.println("You don't have a game saved! Start a new one.");
			return null;
		}
		return null;
	}

	public void savegame(File myfile) {

		// file input and output stream to write objects into a file
		try {
			FileOutputStream f = new FileOutputStream(myfile, false);
			ObjectOutputStream o = new ObjectOutputStream(f);

			// Write objects to file
			o.writeObject(this);
			o.close();
			f.close();
			System.out.println("Your game has been successfully saved!");
		} catch (IOException e) {
			System.err.println("Error initializing stream.");
		}
		//formatsave();
	}


	public void overwritegame() {

		String filename = currentplayer.getUsername() + ".txt";
		File f = new File(filename);

		if(f.exists()) {
			System.out.println("There is already a saved game stored. Would you like to overwrite it? yes/no");
			boolean yes = false;
			while (!yes) {
				String inp = readLetter();
				if(inp.equals("yes")) {
					savegame(f);
					yes = true;					
				} else if(inp.equals("no")) {
					System.out.println("Keeping previous save...");
					yes = true;
				} else {
					System.err.println("Please enter 'yes' to save current game or 'no' to keep previous save.");
				}
			}
		} else {
			savegame(f);
		}
	}
	
	public void showSolution() {
		int i = 0;
		int n = 0;
		char ch;
		
		// applies correct mapping
		if (type.equals("letter")) {
			lettercryptogram let = (lettercryptogram) current;
			for (String cur : encrypted) {
				char c = cur.charAt(0);
				if (((int) c >= 97 && (int) c <= 122)) {
					ch = let.getTrueValue(c); // gets true value
					guesses.set(i, ch);
					let.checkKey(c); // checks it and sets current letter to c
					let.updateMappings(ch); // updates c to ch
				}
				i++;
			}
		} else if (type.equals("number")) {
			numbercryptogram num = (numbercryptogram) current;
			for (String cur : encrypted) {
				try {
					n = Integer.parseInt(cur); // parse int
					ch = num.getTrueValue(n); // gets true value
					guesses.set(i, num.getTrueValue(n));
					num.checkKey(n); // checks it and sets current letter to n
					num.updateMappings(ch); // updates n to ch
				} catch(NumberFormatException e) {
					//This is expected for punctuation!
				} finally{
					i++;
				}
			}
		}

		checkSolved(); // checks cryptogram is solved
		printgame(); // prints current state, aka solved cryptogram
		
		System.out.println("");
		System.out.println("Above is the solution to the puzzle.");
		System.out.println("");
	}

	public void showLetFrequencies(){
		char[] arr = current.getCryptoAlphabet().keySet().toString().toCharArray();
		System.out.println("");
		System.out.println("Letter Frequencies of Current Cryptogram: ");
		System.out.println("Cryptogram Value : Proportion in Cryptogram, Frequency(%)");
		double size = 0;
		double answer = 0;
		
		for(int i = 0; i < arr.length; i++) {
			int count  = 0;
			if((int) arr[i] >= 97 && (int) arr[i] <= 122) {
				for(int b = 0; b< current.getEncryptedPhrase().size();b++){
					if(arr[i] == current.getEncryptedPhrase().get(b).charAt(0)){
						count++;
					}
				}
				size = current.getEncryptedPhrase().size(); // gets size of cryptogram as double to use for calculation
				answer = ((count/size)*100); // gets frequency of current letter
				System.out.println(arr[i] + ": " + count + ", " + String.format("%.2f", answer) + "%"); // rounds to 2 dec places
			}
		}
		
		commonLetters();
	}
	
	public void showNumFrequencies(){
		numbercryptogram num = (numbercryptogram) current;
		HashMap<Integer, Character> numFreq = new HashMap<>();
		//String arr = current.getCryptoAlphabet().keySet().toString();
		numFreq = num.getCryptoAlphabet();
		Set<Integer> set = numFreq.keySet();
		Integer[] arr = set.toArray(new Integer[0]);
		System.out.println(arr);
		
		System.out.println("");
		System.out.println("Letter Frequencies of Current Cryptogram: ");
		System.out.println("Cryptogram Value : Proportion in Cryptogram, Frequency(%)");
		double size = 0;
		double answer = 0;
		
		for(int i = 0; i < set.size(); i++) {
			int count  = 0;
			if((arr[i] >= 1) && ((arr[i]) <= 26)) {
				for(int b = 0; b < current.getEncryptedPhrase().size(); b++){
					try {
						if((arr[i]) == Integer.parseInt(current.getEncryptedPhrase().get(b))) {
							count++;
						}
					} catch (NumberFormatException e) {
						//
					}
				}
				size = current.getEncryptedPhrase().size(); // gets size of cryptogram as double to use for calculation
				answer = ((count/size)*100); // gets frequency of current letter
				System.out.println(arr[i] + "  :  " + count + ", " + String.format("%.2f", answer) + "%"); // rounds to 2 dec places
			}
		}
		
		commonLetters();
	}
	
	public void commonLetters() {
		commonLetters = Arrays.asList('e', 'a', 'r', 'i', 'o', 't', 'n', 's', 'l', 
				'c', 'u', 'd', 'p', 'm', 'h', 'g', 'b', 'f', 'y', 'w', 'k', 'v', 'x', 'z', 'j', 'q');
		double array[] = new double[] {11.16, 8.49, 7.58, 7.54, 7.16, 6.95, 6.65, 5.73, 5.48, 
				4.53, 3.63, 3.38, 3.16, 3.01, 3, 2.47, 2.07, 1.81, 1.77, 1.28, 1.1, 1, 0.29, 0.27, 0.1965, 0.1962};
		System.out.println(" ");	
		System.out.println("Common Proportions of Letter Frequencies: ");
		System.out.print("{");
		for(int i = 0; i < commonLetters.size(); i++) {
			if(i == commonLetters.size()-1) {
				System.out.print(commonLetters.get(i) + ":" + array[i]);
			} else {
				System.out.print(commonLetters.get(i) + ":" + array[i] + "%, ");
			}
			if(i == 11) {
				System.out.println(""); // for formatting
			}
		}
		System.out.print("% }");
		System.out.println("");
	}

	public boolean checkSolved(){ 			// checks if all guesses filled
		for (Character c : getGuesses()){
			if (c == '-'){
				return false; 				// if not all guessed
			}
		}
		if (current.getCryptoAlphabet().equals(current.getUserAlphabet())){
			check = true;
			return true; 
		} else { 
			return false;
		}
	}
	
	private static String continueGame() {
		System.out.println("Do you want to keep playing? yes/no");
		String cont = readLetter();
		System.out.println(" ");
		
		return cont;
	}
	
	public boolean checkHint(char c) { // checks for duplicate hints
		boolean duplicate = false;
		if(!hints.isEmpty()) {
			for(int i = 0; i < hints.size(); i++) {
				if(hints.get(i).equals(c)) { // if char c is in hints
					duplicate = true; // duplicate
				}
			}
		}
		return duplicate; // return true if there is a duplicate, else false
	}

	public void letterhint() {
		System.out.println("");
		lettercryptogram let = (lettercryptogram) current;
		int rand = 0; // for random int
		char c;
		char ch;
		boolean check = false;
		
		String randLetter; // for random letter
		
		do { // loop until it's not ' ', ensures hint is a letter and it is not a duplicate
			rand = new Random().nextInt(encrypted.size());
			randLetter = encrypted.get(rand);
			c = randLetter.charAt(0);
			check = checkHint(c);
		}
		while (!((int) c >= 97 && (int) c <= 122) || check);
		
		ch = let.getTrueValue(c); // gets true value
		let.checkKey(c); // checks it and sets current letter to c
		let.updateMappings(ch); // updates c to ch
		hints.add(c); // add current hint

		System.out.println("");
		for(int i = 0; i < encrypted.size(); i++) {
			if(encrypted.get(i).charAt(0) == c) {
				if(guesses.get(i) != '-') {
					System.out.println("The mapping for '" + c + "' has been overwritten with true value '" + ch + "'");
					break;
				} else {
					System.out.println("Current phrase contains letter: '" + ch + "'");
					break;
				}
			}
		}
		
		updategame();	
		
	}

	public void numberhint(){
		System.out.println("");
		numbercryptogram num = (numbercryptogram) current;
		int rand = 0; // for random int
		int n =  0;
		int compare = 0;
		char ch;
		String randNumber; // for random number
		boolean check = false;

		do { // loop until it's not ' ', ensures hint is a number
			rand = new Random().nextInt(encrypted.size());
			randNumber = encrypted.get(rand);
			try {
				n = Integer.parseInt(randNumber);
			} catch (NumberFormatException e) {
				//
			}
			check = checkHint((char) n);
		}
		while (!(n >= 1 && n <= 26) || check);

		ch = num.getTrueValue(n); // get true value of n
		num.checkKey(n); // checks it and sets current number to n
		current.updateMappings(ch);
		hints.add((char) n); // add current hint to hints
		
		for(int i = 0; i < encrypted.size(); i++) {
			try {
			compare = Integer.parseInt(encrypted.get(i));
			
			if(compare == n) {
				if(guesses.get(i) != '-') {
					System.out.println("The mapping for '" + n + "' has been overwritten with true value '" + ch + "'");
					break;
				} else {
					System.out.println("Current phrase contains letter: '" + ch + "'");
					break;
				}
			}
			
			} catch (NumberFormatException e) {
				//
			}
		}

		updategame();
	}

	@SuppressWarnings("resource")
	public static void main(String[] args) throws FileNotFoundException {

		System.out.println("CS207 Team 3 Cryptogram Game");
		System.out.println("Please enter your username:");

		Scanner input = new Scanner(System.in);
		String username = input.next();
		System.out.println(" ");

		player currentplayer = loadplayer(username);

		boolean continuegame = true;

		do {
			System.out.println("Please enter 'new' to start a new game ");
			System.out.println("             'load' to load a saved game.");
			System.out.println("             'leaderboard' for top 10 players");
			System.out.println("             'quit' to quit game.");
			boolean valid = false;

			while (!valid) {
				String inp = readLetter();
				if (inp.equals("new")) {
					allplayers.updatePlayer(currentplayer);
					startgame(currentplayer);
					valid = true;
				} else if (inp.equals("load")) {
					if(loadgame(currentplayer)!= null){
						valid = true;
						loadgame(currentplayer).playgame();
					}
				}
				else if(inp.equals("leaderboard")){
					toptenPlayers();
					System.out.println("");
					System.out.println("Please enter 'new' to start a new game ");
					System.out.println("             'load' to load a saved game.");
					System.out.println("             'leaderboard' for top 10 players");
					System.out.println("             'quit' to quit game.");
				}
				else if(inp.equals("quit")) {
					System.out.println("See you next time!");
					saveStats();
					System.exit(0);
				}
				else {
					System.err.println("Please enter 'new' to start a new game ");
					System.err.println("             'load' to load a saved game.");
					System.err.println("             'leaderboard' for top 10 players.");
					System.err.println("             'quit' to quit game.");
				}
			}

			//ask if they want to continue
			String cont = continueGame();

			if(cont.equals("yes")) {
				System.out.println("Awesome!");
				continuegame = true;
			} else if (cont.equals("no")) {
				System.out.println("See you next time.");
				continuegame = false;
			} else {
				System.err.println("Did not recognise input - please choose an option below");
			}
		}
		while(continuegame);
		System.out.println("Exiting the game...");
		saveStats();
	}

	
	public static void toptenPlayers() {
		playersList = allplayers.getAllplayers(); // populate playersList with players in file
		Collections.sort(playersList); // sort them by completed cryptograms
		int size = playersList.size(); // total number of players
		int rank = 0; // player's rank in scoreboard
		int limit = 0; // for filling out blank spaces if there's less than 10 players
		boolean check = false; // check for if players w score > 1 exist
		
		// check for at least one successfully solved cryptogram
		if(!playersList.isEmpty()) { // check for not empty
			check = true;
		} else {
			System.err.println("There are no player stats stored.");
		}
		
		if(check == true) {
			System.out.println("");
			System.out.println("Top Ten Players, Ranked by Successfully Completed Cryptograms:");
			if(size < 10) {
				for(int i = 0; i < size; i++) { // uses size to avoid index out of bounds for arraylist
					rank = i+1;
					if(playersList.get(i).getCryptogramsCompleted() >= 1) {
						System.out.println(rank + " " + playersList.get(i).getUsername() + "  " + playersList.get(i).getCryptogramsCompleted());
					} else {
						System.out.println(rank + " ");
					}
				}
				// prints empty spaces depending on how many is left
				limit = 10 - size;
				for(int i = 0; i <= limit; i++) {
					rank = size++;
					System.out.println(rank);
				}
			} else {
				for(int i = 0; i < 10; i++) { // changed to 10 here to only get first 10
					rank = i+1;
					if(playersList.get(i).getCryptogramsCompleted() >= 1) {
						System.out.println(rank + " " + playersList.get(i).getUsername() + "  " + playersList.get(i).getCryptogramsCompleted());
					} else {
						System.out.println(rank + " ");
					}
				}
			}
		} else {
			System.err.println("There are no players that have successfully completed a cryptogram.");
		}
	}

	// STATISTICS
	public static String[] Statistics(player currentPlayer){

		String userName = currentPlayer.getUsername();
		String accuracy = Double.toString(currentPlayer.getAccuracy());
		String totalGuesses = Integer.toString(currentPlayer.getTotalGuesses());
		String CorrectGuesses = Integer.toString(currentPlayer.getCorrectGuesses());
		String NumofCompletedCrypts = Integer.toString(currentPlayer.getCryptogramsCompleted());
		String NumofCryptsPlayed = Integer.toString(currentPlayer.getCryptogramsPlayed());
		return new String[]{userName,accuracy, totalGuesses,CorrectGuesses,NumofCryptsPlayed,NumofCompletedCrypts};
	}
}
