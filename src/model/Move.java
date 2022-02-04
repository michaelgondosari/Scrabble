package model;

public class Move {

    // --- Variables -------------------------------

    public static final char HORIZONTAL = 'H';
    public static final char VERTICAL = 'V';

    private String word;
    private char direction;
    private int placeRow;
    private char placeCol;

    // --- Constructor -----------------------------

    /**
     * Constructor for Move class
     *
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
    }

    // --- Queries ---------------------------------

    /**
     * Get the word
     * @return the word played
     */
    public String getWord() {
        return this.word;
    }

    public void setWord(String wordBlankTile) {
        this.word = wordBlankTile;
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

} // end of class
