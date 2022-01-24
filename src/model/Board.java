package model;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Board {

    // --- Variables -------------------------------

    private static Map<String, String> boardScores;
    public static final int BOARD_SIZE = 15;
    private char[][] scrabbleBoard;
    private TileBag tileBag;

    // --- Constructor -----------------------------

    public Board () {

        // Initialize board
        this.initBoard();

        // Initialize new map for board value
        this.initBoardScores();

        // Initialize TileBag
        tileBag = new TileBag(System.getProperty("user.dir") + "/src/letters.txt");
    }

    // --- Queries ---------------------------------

    /**
     * Get the char of a square on the board
     * @param row - the row of the board
     * @param column - the column of the board
     * @return the char of the square
     * @requires row >= 0 && row <= 14 && column >= 0 && column <= 14
     */
    public char getTileOnBoard(int row, int column) {
        return scrabbleBoard[row][column];
    }

    /**
     * Get the char of a square on the board
     * @param row - the row of the board
     * @param column - the column of the board
     * @return the char of the square
     * @requires row >= 1 && row <= 15 && column >= 'A' && column <= 'O'
     */
    public char getTileOnBoard(char column, int row) {
        return scrabbleBoard[convertRow(row)][convertCol(column)];
    }

    // --- Commands --------------------------------

    /**
     * Initialize the board by making 15x15 empty 2d char arrays
     */
    public void initBoard() {
        scrabbleBoard = new char[BOARD_SIZE][BOARD_SIZE];
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int column = 0; column < BOARD_SIZE; column++) {
                scrabbleBoard[row][column] = ' ';
            }
        }
    }

    /**
     * Read the ScrabbleBoard.txt and put its value into a map
     */
    public void initBoardScores() {
        boardScores = new HashMap<>();
        try{
            readTxt(System.getProperty("user.dir") + "/src/ScrabbleBoard.txt");
        } catch (FileNotFoundException e) {
            System.out.println("File \"ScrabbleBoard.txt\" is missing.");
            System.exit(1);
        } catch (IOException e) {
            System.out.println("There is a problem with the file \"ScrabbleBoard.txt\".");
            System.exit(1);
        }
    }

    /**
     * Read file to get board coordinate and their multiplier scores, then putting both in maps of boardScores
     * @param fileName - file to read the board coordinate and their multiplier scores
     * @throws IOException - if file is not found or broken
     */
    private void readTxt(String fileName) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        String line;
        while ((line = br.readLine()) != null) {
            String[] boardTxt = line.split("/");
            String boardCoordinate = boardTxt[0];
            String boardValue = boardTxt[1];
            boardScores.put(boardCoordinate, boardValue);
        }
        br.close();
    }

    /**
     * Returns the coordinate of a specified row and column
     * in the format of (column)A-O + (row)1-15 => example: A0, B7, C15, ...
     * @param row - row of the board
     * @param column - column of the board
     * @return the customized coordinate
     * @requires row >= 0 && row < 15 && column >= 0 && column < 15
     * @ensures resultColumn >= A && resultColumn <= O && resultRow > 0 && resultRow <= 15
     */
    public String getCoordinate(int row, int column) {
        int resultRow = row + 1;
        char resultColumn = (char)(column + 65);
        return Character.toString(resultColumn) + Integer.toString(resultRow);
    }

    /**
     * Convert column name from Scrabble board to Java
     * @param col - column name in Scrabble board
     * @return column name in Java
     * @requires col >= 'A' && col <= 'O'
     * @ensures result >= 0 && result <= 14
     */
    public int convertCol(char col) {
        return col - 65;
    }

    /**
     * Convert row name from Scrabble board to Java
     * @param row - row name in Scrabble board
     * @return row name in Java
     * @requires row >= 1 && row <= 15
     * @ensures result >= 0 && result <= 14
     */
    public int convertRow(int row) {
        return row - 1;
    }

    /**
     * Checks the board once a word is put down
     * @param check - the word to be checked
     * @return boardScores.get(check), or "1" if check is not found in map
     */
    public String checkForBoardScore(String check) {
        return boardScores.getOrDefault(check, "1");
    }

    /**
     * Creates a deep copy of the scrabble board
     * @return
     */
    public Board deepCopy() {
        Board boardCopy = new Board();
        boardCopy.scrabbleBoard = Arrays.copyOf(this.scrabbleBoard, this.scrabbleBoard.length);
        return boardCopy;
    }

    /**
     * Place the word on the board
     * @param move - the move made by a player
     */
    public void placeWord (Move move) {
        if (move.getWordDoesExist()) {
            String word = move.getWord();
            int row = move.getPlaceRow();
            char col = move.getPlaceCol();

            for (int i = 0; i < word.length(); i++) {
                if (move.getDirection() == Move.HORIZONTAL) {
                    this.scrabbleBoard[row][col+i] = word.toUpperCase().charAt(i);
                } else if (move.getDirection() == Move.VERTICAL) {
                    this.scrabbleBoard[row+i][col] = word.toUpperCase().charAt(i);
                }
            }
        }
    }

//    public static void main(String[] args) {
//        Board b = new Board();
//        Board bcopy = b.deepCopy();
//        System.out.println(Arrays.deepToString(b.scrabbleBoard));
//        System.out.println(Arrays.deepToString(bcopy.scrabbleBoard));
//    }

} // end of class
