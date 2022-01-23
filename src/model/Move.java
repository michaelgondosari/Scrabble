package model;

import java.util.ArrayList;

public class Move {

    // --- Variables -------------------------------

    public static final int RIGHT = 1;
    public static final int DOWN = 9;

    private String word;
    private int direction;
    private int placeRow;
    private char placeCol;
    private boolean wordDoesExist;
    private ArrayList<String> secondWord;
    private int numberOfMoves;

    // --- Constructor -----------------------------

    /**
     * Constructor for Move class
     * @param word - word to be played
     * @param direction - direction of the word; (1)RIGHT or (9)DOWN
     * @param placeCol - starting column to place the word
     * @param placeRow - starting row to place the word
     * @requires placeCol between A and O && placeRow between 1 and 15
     */
    public Move(String word, int direction, char placeCol, int placeRow) {
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
     * @return 1(RIGHT) or 9(DOWN)
     */
    public int getDirection() {
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

    // --- Commands --------------------------------

    public void registerMove(Move move) {
        if (move.wordDoesExist) {
            numberOfMoves++;
        } else {
            System.out.println("Move is not valid, please try again!");
        }
    }

    public int scoreCalculator(){
        int score = 0;
        if (wordDoesExist) {
            // calculate score
        }
        return score;
    }

}
