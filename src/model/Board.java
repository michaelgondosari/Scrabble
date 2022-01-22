package model;

import java.io.*;
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

        // Initialize new map for board value
        this.initBoardScores();

        // Initialize TileBag
        tileBag = new TileBag(new File("letters.txt").getAbsolutePath());
    }

    // --- Queries ---------------------------------
    // --- Commands --------------------------------

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

    private void initBoardScores(){
        boardScores = new HashMap<>();
        try{
            readTxt(new File("ScrabbleBoard.txt").getAbsolutePath());
        } catch (FileNotFoundException e) {
            System.out.println("File \"ScrabbleBoard.txt\" is missing.");
            System.exit(1);
        } catch (IOException e) {
            System.out.println("There is a problem with the file \"ScrabbleBoard.txt\".");
            System.exit(1);
        }
    }

    // Checks the board once a word is put down
    public static String checkForBoardScore(String check){
        if (Board.boardScores.containsKey(check)){
            return Board.boardScores.get(check);
        }
        else{
            return null;
        }
    }

    //public char placeTile(int col, int row){
    //return this.scrabbleBoard[col][row];

    public void placeWord (Move move) {
        if (move.wordDoesExist){
            String word = move.word;
            int row = move.placeRow;
            int col = move.placeCol;
            for (int i=0; i<word.length(); i++) {
                if (move.direction == Move.Sideways) {
                    //increase row val to add word to the right
                    System.out.println("adding word horizontaly");
                    this.scrabbleBoard[row][col+i] = word.toUpperCase().charAt(i);
                } else if (move.direction == Move.downward) {
                    //increase col val to add word down
                    this.scrabbleBoard[row+i][col] = word.toUpperCase().charAt(i);
                }
            }
        }
    }

} // end of class
