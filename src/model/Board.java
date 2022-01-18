package model;

import java.util.HashMap;
public class Board {

    // --- Variables -------------------------------
    private static HashMap<String, String> boardScores;
    public static final int BOARD_SIZE = 15;
    private char[][] scrabbleBoard;
    // --- Constructor -----------------------------
private void initBoardScores(){
    Board.boardScores = new HashMap<String, String>();
    // Triple word scores
    boardScores.put("A1", "3W");
    boardScores.put("A8", "3W");
    boardScores.put("A15", "3W");
    boardScores.put("H1", "3W");
    boardScores.put("H15", "3W");
    boardScores.put("O1", "3W");
    boardScores.put("O8", "3W");
    boardScores.put("O15", "3W");

    // Double word scores
    boardScores.put("B2", "2W");
    boardScores.put("B14", "2W");
    boardScores.put("C3", "2W");
    boardScores.put("C13", "2W");
    boardScores.put("D4", "2W");
    boardScores.put("D12", "2W");
    boardScores.put("E5", "2W");
    boardScores.put("E11", "2W");
    boardScores.put("K5", "2W");
    boardScores.put("K11", "2W");
    boardScores.put("L4", "2W");
    boardScores.put("l12", "2W");
    boardScores.put("M3", "2W");
    boardScores.put("M13", "2W");
    boardScores.put("N2", "2W");
    boardScores.put("N14", "2W");
    boardScores.put("H8", "2W");

    // Triple letter score
    boardScores.put("B6", "3L");
    boardScores.put("B10", "3L");
    boardScores.put("F2", "3L");
    boardScores.put("F6", "3L");
    boardScores.put("F10", "3L");
    boardScores.put("F14", "3L");
    boardScores.put("J2", "3L");
    boardScores.put("J6", "3L");
    boardScores.put("J10", "3L");
    boardScores.put("J14", "3L");
    boardScores.put("N6", "3L");
    boardScores.put("N10", "3L");

    // Double letter score
    boardScores.put("A4", "2L");
    boardScores.put("A12", "2L");
    boardScores.put("C7", "2L");
    boardScores.put("C9", "2L");
    boardScores.put("D1", "2L");
    boardScores.put("D8", "2L");
    boardScores.put("D15", "2L");
    boardScores.put("G3", "2L");
    boardScores.put("G7", "2L");
    boardScores.put("G9", "2L");
    boardScores.put("G13", "2L");
    boardScores.put("H4", "2L");
    boardScores.put("H12", "2L");
    boardScores.put("I3", "2L");
    boardScores.put("I7", "2L");
    boardScores.put("I9", "2L");
    boardScores.put("I13", "2L");
    boardScores.put("L1", "2L");
    boardScores.put("L8", "2L");
    boardScores.put("L15", "2L");
    boardScores.put("M7", "2L");
    boardScores.put("M9", "2L");
    boardScores.put("O4", "2L");
    boardScores.put("O12", "2L");

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
    public Board() {

    // --- Queries ---------------------------------
    // --- Commands --------------------------------



    }


}
