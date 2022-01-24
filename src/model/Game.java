package model;

import model.wordchecker.InMemoryScrabbleWordChecker;

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

    /**
     * Get all words formed from a player's move
     * @param move - the player's move
     * @param board - the current board
     * @return a list of words formed by the player's move
     */
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

    /**
     * Check if all the words in a list are defined in the dictionary
     * @param words - a list of words to be checked
     * @return true if all words in the list are valid, false otherwise
     */
    public boolean checkWordsValid(List<String> words) {
        boolean result = true;
        InMemoryScrabbleWordChecker checker = new InMemoryScrabbleWordChecker();
        for (String word : words) {
            result = (checker.isValidWord(word) != null) ? true : false;
            if (!result) {
                break;
            }
        }
        return result;
    }

    /**
     * Check if a move is valid and does not overwrite an existing tile on board
     * @param move - the move made by a player
     * @param board - current board
     * @return true if a move does not overwrite an existing tile, false otherwise
     */
    public boolean checkMoveOverwrite(Move move, Board board) {
        String word = move.getWord().toUpperCase();
        int startRow = board.convertRow(move.getPlaceRow());
        int startCol = board.convertCol(move.getPlaceCol());

        // invalid if a player is trying to overwrite an existing tile with another tile
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);

            if (move.getDirection() == Move.HORIZONTAL) {
                if (board.getTileOnBoard(startRow, startCol + i) != ' '
                        && board.getTileOnBoard(startRow, startCol + i) != c) {
                    return false;
                }

            } else if (move.getDirection() == Move.VERTICAL) {
                if (board.getTileOnBoard(startRow + i, startCol) != ' '
                        && board.getTileOnBoard(startRow + i, startCol) != c) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Check if a move is valid and all the tiles placed are inside the board
     * @param move - the move made by a player
     * @param board - current board
     * @return true if a move is inside the board, false otherwise
     */
    public boolean checkMoveInsideBoard(Move move, Board board) {
        String word = move.getWord().toUpperCase();
        int startRow = board.convertRow(move.getPlaceRow());
        int startCol = board.convertCol(move.getPlaceCol());

        // invalid if move is outside board size
        if (startCol + word.length() - 1 > Board.BOARD_SIZE
                || startRow + word.length() - 1 > Board.BOARD_SIZE) {
            return false;
        }
        return true;
    }

    /**
     * Calculate the total score of a move by a player
     * @param move - the move made by a player
     * @param board - current board
     * @return the total score of the player's move
     */
    public int calculateScore(Move move, Board board) {
        TileBag tileBag = new TileBag(System.getProperty("user.dir") + "/letters.txt");
        String word = move.getWord().toUpperCase();
        int startRow = board.convertRow(move.getPlaceRow());
        int startCol = board.convertCol(move.getPlaceCol());

        // Include multiplier for the calculation of the first move (placed tile only)
        int firstWordScore = 0;

        // Letter Multiplier
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            int charOriginalValue = tileBag.getLetterValue(c);
            if (move.getDirection() == Move.HORIZONTAL) {
                if (board.getTileOnBoard(startRow, startCol + i) == ' ') {
                    switch (board.getBoardMultiplier(board.getCoordinate(startRow, startCol + i))) {
                        case "3L":
                            charOriginalValue *= 3;
                            break;
                        case "2L":
                            charOriginalValue *= 2;
                            break;
                        default:
                            break;
                    }
                }
            } else if (move.getDirection() == Move.VERTICAL) {
                if (board.getTileOnBoard(startRow + i, startCol) == ' ') {
                    switch (board.getBoardMultiplier(board.getCoordinate(startRow + i, startCol))) {
                        case "3L":
                            charOriginalValue *= 3;
                            break;
                        case "2L":
                            charOriginalValue *= 2;
                            break;
                        default:
                            break;
                    }
                }
            }
            firstWordScore += charOriginalValue;
        }

        // Add value of other letter on its right and left or above and below
        List<Character> charFirstWord = new ArrayList<>();
        for (int i = 0; i < word.length(); i++) {
            charFirstWord.add(i, word.toUpperCase().charAt(i));
        }

        if (move.getDirection() == Move.HORIZONTAL) {

            // check the right side
            if (startCol + word.length() - 1 < Board.BOARD_SIZE - 1) {
                char nextChar = board.getTileOnBoard(startRow, startCol + word.length());
                int x = 0;
                while (nextChar != ' ') {
                    firstWordScore += tileBag.getLetterValue(nextChar);
                    charFirstWord.add(charFirstWord.size(), nextChar);
                    x++;
                    nextChar = board.getTileOnBoard(startRow, startCol + word.length() + x);
                }
            }

            // check the left side
            if (startCol > 0) {
                char nextChar = board.getTileOnBoard(startRow, startCol - 1);
                int x = 1;
                while (nextChar != ' ') {
                    firstWordScore += tileBag.getLetterValue(nextChar);
                    charFirstWord.add(0, nextChar);
                    x++;
                    nextChar = board.getTileOnBoard(startRow, startCol - x);
                }
            }

        } else if (move.getDirection() == Move.VERTICAL) {

            // check above
            if (startRow > 0) {
                char nextChar = board.getTileOnBoard(startRow - 1, startCol);
                int x = 1;
                while (nextChar != ' ') {
                    firstWordScore += tileBag.getLetterValue(nextChar);
                    charFirstWord.add(0, nextChar);
                    x++;
                    nextChar = board.getTileOnBoard(startRow - x, startCol);
                }
            }

            // check below
            if (startRow + word.length() - 1 < Board.BOARD_SIZE - 1) {
                char nextChar = board.getTileOnBoard(startRow + word.length(), startCol);
                int x = 0;
                while (nextChar != ' ') {
                    firstWordScore += tileBag.getLetterValue(nextChar);
                    charFirstWord.add(charFirstWord.size(), nextChar);
                    x++;
                    nextChar = board.getTileOnBoard(startRow + word.length() + x, startCol);
                }
            }
        }

        // Word multiplier
        for (int i = 0; i < word.length(); i++) {
            if (move.getDirection() == Move.HORIZONTAL) {
                if (board.getTileOnBoard(startRow, startCol + i) == ' ') {
                    switch (board.getBoardMultiplier(board.getCoordinate(startRow, startCol + i))) {
                        case "3W":
                            firstWordScore *= 3;
                            break;
                        case "2W":
                            firstWordScore *= 2;
                            break;
                        default:
                            break;
                    }
                }
            } else if (move.getDirection() == Move.VERTICAL) {
                if (board.getTileOnBoard(startRow + i, startCol) == ' ') {
                    switch (board.getBoardMultiplier(board.getCoordinate(startRow + i, startCol))) {
                        case "3W":
                            firstWordScore *= 3;
                            break;
                        case "2W":
                            firstWordScore *= 2;
                            break;
                        default:
                            break;
                    }
                }
            }
        }

        // calculate letter values only for the other moves (without multiplier)

        // first, remove the first word value as it has been calculated above
        String firstWord = "";
        for (char c : charFirstWord) {
            firstWord += c;
        }
        List<String> otherWords = getAllWords(move, board);
        otherWords.remove(firstWord);

        // then, iterate and add the letter values
        int otherWordsScore = 0;
        for (String words : otherWords) {
            for (int i = 0; i < words.length(); i++) {
                otherWordsScore += tileBag.getLetterValue(words.charAt(i));
            }
        }

        // Finally, return the addition of firstWordValue (with multiplier) and otherWordsValue (without multiplier)
        return firstWordScore + otherWordsScore;
    }

} // end of class
