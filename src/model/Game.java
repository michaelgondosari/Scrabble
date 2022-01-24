package model;

import java.util.ArrayList;
import java.util.List;

public class Game {

    // --- Variables -------------------------------

    private List<Player> players;
    private Board board;
    private TileBag tileBag;

    // --- Constructor -----------------------------

    public Game(List<Player> players) {
        this.players = players;
        this.board = new Board();
        this.tileBag = new TileBag(System.getProperty("user.dir") + "/src/letters.txt");
    }

    // --- Commands --------------------------------

    public List<String> getAllWords(Move move, Board board) {
        List<String> result = new ArrayList<>();
        String word = move.getWord();
        int startRow = board.convertRow(move.getPlaceRow());
        int startCol = board.convertCol(move.getPlaceCol());

        List<Character> charFirstWord = new ArrayList<>();
        for (int i = 0; i < word.length(); i++) {
            charFirstWord.add(i, word.toUpperCase().charAt(i));
        }

        if (move.getDirection() == Move.HORIZONTAL) {

            // check the right side
            if (startCol + word.length() - 1 < Board.BOARD_SIZE - 1) {
                char nextChar = board.getTileOnBoard(startRow, startCol + word.length());
                int i = 0;
                while (nextChar != ' ') {
                    charFirstWord.add(charFirstWord.size(), nextChar);
                    i++;
                    nextChar = board.getTileOnBoard(startRow, startCol + word.length() + i);
                }
            }

            // check the left side
            if (startCol > 0) {
                char nextChar = board.getTileOnBoard(startRow, startCol - 1);
                int i = 1;
                while (nextChar != ' ') {
                    charFirstWord.add(0, nextChar);
                    i++;
                    nextChar = board.getTileOnBoard(startRow, startCol - i);
                }
            }

            // combine right and left
            String leftRight = "";
            for (char c : charFirstWord) {
                leftRight += c;
            }
            
            // add to list result
            if (leftRight != "") {
                result.add(leftRight);
            }

            // check above and below each placed tile
            for (int i = 0; i < word.length(); i++) {
                List<Character> charAdditionalWords = new ArrayList<>();
                if (board.getTileOnBoard(startRow, startCol + i) == ' ') { // only check the tile placed by player, not existing tile on board
                    char c = word.charAt(i);
                    charAdditionalWords.add(c);
                    // check above
                    if (startRow > 0) {
                        char nextChar = board.getTileOnBoard(startRow - 1, startCol);
                        int x = 1;
                        while (nextChar != ' ') {
                            charAdditionalWords.add(0, nextChar);
                            x++;
                            nextChar = board.getTileOnBoard(startRow - x, startCol);
                        }
                    }
                    // check below
                    if (startRow + word.length() - 1 < Board.BOARD_SIZE - 1) {
                        char nextChar = board.getTileOnBoard(startRow + word.length(), startCol);
                        int x = 0;
                        while (nextChar != ' ') {
                            charAdditionalWords.add(charAdditionalWords.size(), nextChar);
                            x++;
                            nextChar = board.getTileOnBoard(startRow + word.length() + x, startCol);
                        }
                    }
                    // combine above and below
                    String upDown = "";
                    for (char upDownAdditional : charAdditionalWords) {
                        upDown += upDownAdditional;
                    }
                    // add to list result
                    if (upDown != "") {
                        result.add(upDown);
                    }
                }
            }


        } else if (move.getDirection() == Move.VERTICAL) {

            // check above
            if (startRow > 0) {
                char nextChar = board.getTileOnBoard(startRow - 1, startCol);
                int i = 1;
                while (nextChar != ' ') {
                    charFirstWord.add(0, nextChar);
                    i++;
                    nextChar = board.getTileOnBoard(startRow - i, startCol);
                }
            }

            // check below
            if (startRow + word.length() - 1 < Board.BOARD_SIZE - 1) {
                char nextChar = board.getTileOnBoard(startRow + word.length(), startCol);
                int i = 0;
                while (nextChar != ' ') {
                    charFirstWord.add(charFirstWord.size(), nextChar);
                    i++;
                    nextChar = board.getTileOnBoard(startRow + word.length() + i, startCol);
                }
            }

            // combine above and below
            String upDown = "";
            for (char c : charFirstWord) {
                upDown += c;
            }
            
            //add to list result
            if (upDown != "") {
                result.add(upDown);
            }

            // check left and right of each placed tile
            for (int i = 0; i < word.length(); i++) {
                List<Character> charAdditionalWords = new ArrayList<>();
                if (board.getTileOnBoard(startRow + i, startCol) == ' ') { // only check the tile placed by player, not existing tile on board
                    char c = word.charAt(i);
                    charAdditionalWords.add(c);
                    // check left
                    if (startCol > 0) {
                        char nextChar = board.getTileOnBoard(startRow, startCol - 1);
                        int x = 1;
                        while (nextChar != ' ') {
                            charAdditionalWords.add(0, nextChar);
                            x++;
                            nextChar = board.getTileOnBoard(startRow, startCol - x);
                        }
                    }
                    // check right
                    if (startCol + word.length() - 1 < Board.BOARD_SIZE - 1) {
                        char nextChar = board.getTileOnBoard(startRow, startCol + word.length());
                        int x = 0;
                        while (nextChar != ' ') {
                            charAdditionalWords.add(charAdditionalWords.size(), nextChar);
                            x++;
                            nextChar = board.getTileOnBoard(startRow, startCol + word.length() + x);
                        }
                    }
                    // combine left and right
                    String leftRight = "";
                    for (char leftRightAdditional : charAdditionalWords) {
                        leftRight += leftRightAdditional;
                    }
                    // add to list result
                    if (leftRight != "") {
                        result.add(leftRight);
                    }
                }
            }
        }
        return result;
    }

    public boolean checkWords(List<String> words) {
        boolean result = false;

        // check all words formed from above method
        return result;
    }

    public boolean checkMove(Move move, Board board) {
        boolean isValidMove = true;

        String word = move.getWord().toUpperCase();
        int startRow = board.convertRow(move.getPlaceRow());
        int startCol = board.convertCol(move.getPlaceCol());

        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);

            if (move.getDirection() == Move.HORIZONTAL) {
                if (board.getTileOnBoard(startRow, startCol + i) != ' '
                        && board.getTileOnBoard(startRow, startCol + i) != c) {
                    isValidMove = false;
                }

            } else if (move.getDirection() == Move.VERTICAL) {
                if (board.getTileOnBoard(startRow + i, startCol) != ' '
                        && board.getTileOnBoard(startRow + i, startCol) != c) {
                    isValidMove = false;
                }
            }
        }

        // add if move is outside board size

        return isValidMove;
    }

}
