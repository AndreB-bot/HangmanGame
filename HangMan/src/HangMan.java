
import java.util.Scanner;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.InputMismatchException;

public class HangMan {
	private static ArrayList<Character> resultList;
	private static ArrayList<Character> incorrList;
	private static ArrayList<Character> mword;
	private static ArrayList<Integer> pScores;
	private static Scanner input;
	private static Scanner input2;
	private static Scanner last;
	private static int left;
	private static int score;

	public static void main(String[] args){

		String y = "";
		do {	
			String word = "";
			Character blank = '_';
			Character letter;
			String typed;
			
			last = new Scanner(System.in);
			input2 = new Scanner(System.in);
			resultList = new ArrayList<Character>();
			incorrList = new ArrayList<Character>();

			left = 7;
			score = 0;

			File dictionary = new File("res/dictionary.txt");

			do {
				int random = (int)((Math.random() * 120000) + 1);

				try {
					input = new Scanner(dictionary);

					for(int count = 0; count <= random; count++) {
						input.nextLine();

						if(count == random) {
							word = input.nextLine();
						}
					}

					//System.out.println("\n"+word);
					mWord(word);


					for(int count = 0; count < word.length(); count++) {
						resultList.add(blank);
					}

					mainMenu();

					while(!allMatched() && left > 0) {
						try {
							typed = input2.nextLine();

							if(!Character.isLetter(typed.charAt(0))) {
								throw new InputMismatchException();
							}

							letter = Character.toLowerCase(typed.charAt(0));

							compare(letter);

						}
						catch(InputMismatchException ex) {
							System.out.print("\nInvalid input\n");
							System.out.print("\nPlease try again");
							input.hasNextLine();
							continue;
						}
						finally {
							mainMenu();
						}
					}

					reset();
				}

				catch(FileNotFoundException  ex) {
					System.out.println("Dictionary file not found.");
				}

			}while(left > 0);

			System.out.print("\n\nGAME OVER!\n");
			System.out.print("\nThe word was: " + word + "\n");
			endGame();
			
			System.out.print("\nPlay again? ");
			
			y = last.next();
		}while(y.equals("") || y.compareToIgnoreCase("yes") == 0 || y.compareToIgnoreCase("Y") == 0);
		
		input.close();
		input2.close();
		last.close();
	}

	public static void mWord(String word) {
		Character letter2;

		mword = new ArrayList<Character>();

		for(int count = 0; count < word.length(); count++) {
			char a = word.charAt(count);
			letter2 = a;
			mword.add(letter2);
		}
	}


	public static void compare(Character letter) {

		int points = 0;
		int intScore = 10;

		if(!resultList.contains(letter)) {
			if(mword.contains(letter)){
				for(int count = 0; count < mword.size(); count++) {
					if(mword.get(count).equals(letter)) {
						points++;
						resultList.set(count, letter);
					}
				}
				score += (points * intScore);
			}

			else {
				System.out.print("\nSorry, there were no "+letter+"'s");
				setincorrList(letter);
				left--;
			}
		}
		else System.out.print("\nLetter already exist");

		if (allMatched()) score += 100 + (left * 30);
	}



	public static void printResult(ArrayList<Character> resultList) {
		System.out.println("\n\n=====================================");
		System.out.print("\nHidden Word: ");
		for(int count = 0; count < resultList.size(); count++) {
			System.out.print(resultList.get(count) + " ");
		}
	}

	public static void mainMenu() {
		//if(allMatched()) {System.out.println("CORRECT!");}
		printResult(resultList);
		System.out.print("\n\n\nIncorrect Guess: ");
		getincorrList();
		System.out.println("\n\nGuess left: " + left);
		System.out.println("\nScore: " + score);
		System.out.print("\nEnter next guess: ");

	}

	public static void setincorrList(Character wrongGuess) {

		int index=0;

		if(incorrList.isEmpty()) {
			incorrList.add(wrongGuess);
		}
		else if(!incorrList.contains(wrongGuess)) {
			incorrList.add(index,wrongGuess);
		}
		Collections.sort(incorrList);
	}


	public static void getincorrList() {
		if(!incorrList.isEmpty()) {
			for(int count = 0; count < incorrList.size(); count++) {
				System.out.print(incorrList.get(count) +", ");
			}
		}
	}

	public static boolean allMatched() {
		int count = resultList.size();

		for(int index = 0; index < resultList.size(); index++) {
			Character a = mword.get(index);
			Character b = resultList.get(index);
			if(a.equals(b)) {
				count--;
			}
		}

		if(count == 0) return true;
		return false;
	}

	public static void reset() {
		resultList.clear();
		incorrList.clear();
		if(left > 0) left = 7;	
	}

	public static void endGame() {

		String name;

		pScores = new ArrayList<Integer>();
		ArrayList<String> hScores = new ArrayList<String>();


		int numPlayers = 5;
		int index = 0;

		File highscores = new File("res/HighScores.txt");
		PrintWriter write;

		if(!highscores.exists() && score > 0) {
			try {
				write = new PrintWriter(highscores);

				System.out.print("\n\nCongrats you're the only player.\n");
				System.out.print("\nWhat a life!\n");
				System.out.println("\nPlease enter your name: ");

				name = input2.nextLine();

				write.print(name + " ");
				write.print(score);

				write.close();
		
				input = new Scanner(highscores);
				System.out.print("\n\nHighscores:\n\n");

				while(input.hasNext()) {
					System.out.println(input.nextLine());
				}

				//input.close();
				//input2.close();
				//input.close();

			}catch(FileNotFoundException ex) {
				System.out.print("Make the parent file \"res\" "+
						"\nfor the highscore list"); 
			}
		}

		else if(highscores.exists() && score > 0) {

			try {
				input = new Scanner(highscores);

				while(input.hasNext()) {
					hScores.add(input.nextLine());
				}

				input = new Scanner(highscores);

				while(input.hasNext()) {
					input.next();
					pScores.add(input.nextInt());
				}

			}
			catch(Exception ex) {
				System.out.println("Dude! Where's the file?!");
			}
			if(score > pScores.get(pScores.size() - 1)) {
				pScores.add(score);

				Collections.sort(pScores, Collections.reverseOrder());

				index = pScores.indexOf(score);

				System.out.print("\n\nCongrats you made it into the top 5!.\n");
				System.out.print("\nPlease enter your name: \n");

				name = input2.nextLine();

				hScores.add(index, name + " " + score);

				try {

					write = new PrintWriter(highscores);


					//i know you don't like seeing for-loops like this but I'm tired of making methods 
					//and while loops. This feels like battleship all over again..lol
					for(int count = 0; count < hScores.size() && count < numPlayers; count++) {
						write.println(hScores.get(count));
					}

					write.close();

					input = new Scanner(highscores);

					System.out.print("\n\nHighscores:\n");

					while(input.hasNext()) {
						System.out.printf("%-3s \t %d %n",input.next(), input.nextInt());
					}

					input.close();
					input2.close();

				}
				catch(Exception ex) {
					System.out.println("You broke the game Ken!");
				}

			}
			else {
				System.out.println("\nNice try!");
				//input.close();
				//input2.close();
			}
		}
		else {
			//input.close();
			//input2.close();
		}
	}

}



