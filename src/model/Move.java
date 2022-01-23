package model;

import model.wordchecker.InMemoryScrabbleWordChecker;

import java.util.ArrayList;

public class Move {

    // --- Variables -------------------------------

    public static final char HORIZONTAL = 'H';
    public static final char VERTICAL = 'V';

    private String word;
    private char direction;
    private int placeRow;
    private char placeCol;
    private boolean wordDoesExist;
    private ArrayList<String> secondWord;
    private int numberOfMoves;

    // --- Constructor -----------------------------

    /**
     * Constructor for Move class
     * @param word - word to be played
     * @param direction - direction of the word; (H)HORIZONTAL or (V)VERTICAL
     * @param placeCol - starting column to place the word
     * @param placeRow - starting row to place the word
     * @requires placeCol between A and O && placeRow between 1 and 15
     */
    public Move(String word, char direction, char placeCol, int placeRow) {
        this.word = word;
        this.direction = direction;
        this.placeRow = placeRow;
        this.placeCol = placeCol;
        this.wordDoesExist = false; // need to be validated
        this.numberOfMoves = 0;
    }

    // --- Queries ---------------------------------

    /**
     * Get the word
     * @return the word played
     */
    public String getWord() {
        return this.word;
    }

    /**
     * Get the direction of the word
     * @return H(HORIZONTAL) or V(VERTICAL)
     */
    public char getDirection() {
        return this.direction;
    }

    /**
     * Get the starting row
     * @return the int of starting row
     */
    public int getPlaceRow() {
        return this.placeRow;
    }

    /**
     * Get the starting column
     * @return the char of starting column
     */
    public char getPlaceCol() {
        return this.placeCol;
    }

    /**
     * Returns if the word is valid
     * @return true if the word is valid, false otherwise
     */
    public boolean getWordDoesExist() {
        return this.wordDoesExist;
    }

    /**
     * Set the word validity
     * @param isValid - true to set the word to valid, false otherwise
     */
    public void setWordDoesExist(boolean isValid) {
        this.wordDoesExist = isValid;
    }

    // --- Commands --------------------------------

    /**
     * Register the move
     * @param move - move made by a player
     */
    public void registerMove(Move move) {
        if (move.wordDoesExist) {
            numberOfMoves++;
            setWordDoesExist(true);
        } else {
            System.out.println("Move is not valid, please try again!");
        }
    }

    /**
     * Convert a string to char array
     * @param word - string to be converted
     * @return char array of the string
     */
    public char[] stringToCharArray(String word) {
        char[] result = new char[word.length()];
        for (int i = 0; i < word.length(); i++) {
            result[i] = word.charAt(i);
        }
        return result;
    }

    /**
     * Check if the word played is valid
     * @param word - word played by a player
     * @return true if word is in the dictionary, false otherwise
     */
    public boolean checkWordValidity(String word) {
        InMemoryScrabbleWordChecker checker = new InMemoryScrabbleWordChecker();
        return (checker.isValidWord(word) != null) ? true : false;
    }

    /**
     * Calculate the score of a word
     * @param word - string to be calculated
     * @return score of the string (according to Scrabble)
     */
    public int scoreCalculator(String word) {
        int score = 0;
        if (checkWordValidity(word) == true) {
            TileBag tileBag = new TileBag(System.getProperty("user.dir") + "/model/letters.txt");
            char[] charWord = stringToCharArray(word);
            for (char c : charWord) {
                score += tileBag.getLetterValue(c);
            }
        }
        return score;
    }

} // end of class
