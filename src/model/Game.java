package model;

import exception.InvalidMoveException;
import exception.InvalidWordException;
import model.wordchecker.InMemoryScrabbleWordChecker;
import view.LocalTUI;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Game {

    // --- Variables -------------------------------

    private List<Player> players;
    private Board board;
    private TileBag tileBag;
    private int currentPlayerIndex;

    // --- Constructor -----------------------------

    public Game(List<Player> players) {
        // Generate new board
        this.board = new Board();

        // Generate new tilebag
        this.tileBag = new TileBag(System.getProperty("user.dir") + "/src/letters.txt");

        // Generate new list of players
        this.players = players;
        for (Player player : players) {
            player.addTilesToRack(tileBag.drawTiles(7));
        }
        Random r = new Random();
        this.currentPlayerIndex = r.nextInt(players.size()); // randomly decides which player starts
    }

    // --- Queries ---------------------------------

    /**
     * Get the current board
     * @return current board
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Get a list of players in the current game
     * @return a list of players in the current game
     */
    public List<Player> getPlayers() {
        return players;
    }

    /**
     * Get who is the current player
     * @return the current player
     */
    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    /**
     * Get the current tile bag
     * @return the current tile bag
     */
    public TileBag getTileBag() {
        return tileBag;
    }

    // --- Commands --------------------------------

    /**
     * Change turn to the next player
     */
    public void nextPlayer() {
        this.currentPlayerIndex = (this.currentPlayerIndex + 1) % players.size();
    }

    /**
     * Get all words formed from a player's move
     * @param move - the player's move
     * @return a list of words formed by the player's move
     */
    public List<String> getAllWords(Move move) {
        List<String> result = new ArrayList<>();
        String word = move.getWord().toUpperCase();
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
            if (leftRight.length() > 1) {
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
                        int x = 1;
                        char nextChar = '@';
                        while (nextChar != ' ') {
                            nextChar = startRow - x >= 0 ? board.getTileOnBoard(startRow - x, startCol + i) : ' ';
                            if (nextChar != ' ') {
                                charAdditionalWords.add(0, nextChar);
                            }
                            x++;
                        }
                    }
                    // check below
                    if (startRow + word.length() - 1 < Board.BOARD_SIZE - 1) {
                        int x = 1;
                        char nextChar = '@';
                        while (nextChar != ' ') {
                            nextChar = startRow + x < Board.BOARD_SIZE ? board.getTileOnBoard(startRow + i - x, startCol) : ' ';
                            if (nextChar != ' ') {
                                charAdditionalWords.add(charAdditionalWords.size(), nextChar);
                            }
                            x++;
                        }
                    }
                    // combine above and below
                    String upDown = "";
                    for (char upDownAdditional : charAdditionalWords) {
                        upDown += upDownAdditional;
                    }
                    // add to list result
                    if (upDown.length() > 1) {
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
            if (upDown.length() > 1) {
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
                        int x = 1;
                        char nextChar = '@';
                        while (nextChar != ' ') {
                            nextChar = startCol - x >= 0 ? board.getTileOnBoard(startRow + i, startCol - x) : ' ';
                            if (nextChar != ' ') {
                                charAdditionalWords.add(0, nextChar);
                            }
                            x++;
                        }
                    }
                    // check right
                    if (startCol + word.length() - 1 < Board.BOARD_SIZE - 1) {
                        int x = 1;
                        char nextChar = '@';
                        while (nextChar != ' ') {
                            nextChar = startCol + x < Board.BOARD_SIZE ? board.getTileOnBoard(startRow + i, startCol + x) : ' ';
                            if (nextChar != ' ') {
                                charAdditionalWords.add(charAdditionalWords.size(), nextChar);
                            }
                            x++;
                        }
                    }


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
                    if (leftRight.length() > 1) {
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
     * @throws InvalidWordException if word is not found in the dictionary
     */
    public boolean checkWordsValid(List<String> words) throws InvalidWordException {
        InMemoryScrabbleWordChecker checker = new InMemoryScrabbleWordChecker();
        for (String word : words) {
            if (checker.isValidWord(word) == null) {
                throw new InvalidWordException();
            }
        }
        return true;
    }

    /**
     * Check if a move is valid and does not overwrite an existing tile on board
     * @param move - the move made by a player
     * @return true if a move does not overwrite an existing tile
     * @throws InvalidMoveException if a move overwrites an existing tile
     */
    public boolean checkMoveOverwrite(Move move) throws InvalidMoveException {
        String word = move.getWord().toUpperCase();
        int startRow = board.convertRow(move.getPlaceRow());
        int startCol = board.convertCol(move.getPlaceCol());

        // invalid if a player is trying to overwrite an existing tile with another tile
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);

            if (move.getDirection() == Move.HORIZONTAL) {
                if (board.getTileOnBoard(startRow, startCol + i) != ' '
                        && board.getTileOnBoard(startRow, startCol + i) != c) {
                    throw new InvalidMoveException("You cannot overwrite an existing tile on the board!");
                }

            } else if (move.getDirection() == Move.VERTICAL) {
                if (board.getTileOnBoard(startRow + i, startCol) != ' '
                        && board.getTileOnBoard(startRow + i, startCol) != c) {
                    throw new InvalidMoveException("You cannot overwrite an existing tile on the board!");
                }
            }
        }
        return true;
    }

    /**
     * Check if a move is valid and all the tiles placed are inside the board
     * @param move - the move made by a player
     * @return true if a move is inside the board
     * @throws InvalidMoveException if a move is outside the board
     */
    public boolean checkMoveInsideBoard(Move move) throws InvalidMoveException {
        String word = move.getWord().toUpperCase();
        int startRow = board.convertRow(move.getPlaceRow());
        int startCol = board.convertCol(move.getPlaceCol());

        // invalid if move is outside board size
        if (startCol + word.length() - 1 > Board.BOARD_SIZE
                || startRow + word.length() - 1 > Board.BOARD_SIZE) {
            throw new InvalidMoveException("You cannot place a tile outside the board!");
        }
        return true;
    }

    /**
     * Check if a move made is using available tiles
     * @param move - the move made by a player
     * @return true if a move made is using available tiles
     * @throws InvalidMoveException if there are tile(s) used not from the current rack
     */
    public boolean checkUsingAvailableTiles(Move move) throws InvalidMoveException {
        String word = move.getWord().toUpperCase();
        int startRow = board.convertRow(move.getPlaceRow());
        int startCol = board.convertCol(move.getPlaceCol());

        List<Character> wordChar = tilesToRemove(move);

        // compare the list with current rack
        List<Character> playerRack = getCurrentPlayer().rackCopy();
        for (char c : wordChar) {
            if (playerRack.contains(c)) {
                playerRack.remove((Character) c);
            } else {
                throw new InvalidMoveException("You must use the tiles from your rack!");
            }
        }

        // if all tiles are available in the rack, check is true
        return true;
    }

    /**
     * Exclude the letter in player's move that already exists on the board accordingly
     * @param move - the move made by a player
     * @return a modified list of letters from the move's word
     */
    public List<Character> tilesToRemove(Move move) {
        String word = move.getWord().toUpperCase();
        int startRow = board.convertRow(move.getPlaceRow());
        int startCol = board.convertCol(move.getPlaceCol());

        List<Character> result = new ArrayList<>();
        for (int i = 0; i < word.length(); i++) {
            result.add(word.charAt(i));
        }

        // check board if there are tiles already placed
        if (move.getDirection() == Move.HORIZONTAL) {
            for (int i = 0; i < word.length(); i++) {
                if (board.getTileOnBoard(startRow, startCol + i) != ' ') {
                    // remove it from the char list
                    result.remove((Character) board.getTileOnBoard(startRow, startCol + i));
                }
            }
        } else if (move.getDirection() == Move.VERTICAL) {
            for (int i = 0; i < word.length(); i++) {
                if (board.getTileOnBoard(startRow + i, startCol) != ' ') {
                    // remove it from the char list
                    result.remove((Character) board.getTileOnBoard(startRow + i, startCol));
                }
            }
        }

        return result;
    }


    /**
     * Calculate the total score of a move by a player
     * @param move - the move made by a player
     * @return the total score of the player's move
     */
    public int calculateScore(Move move) {
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
        List<String> otherWords = getAllWords(move);
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

    /**
     * Returns if the game is over
     * @return true if game is finished, false otherwise
     */
    public boolean gameOver() {
        boolean gameOver = false;
        if (this.tileBag.getTilesLeft() == 0
                && this.players.get(currentPlayerIndex).getRackSize() == 0) {
            gameOver = true;
        }
        return gameOver;
    }

    /**
     * Get the winner of the current game
     * @return the player who wins the game
     */
    public Player getWinner() {
        Player winner = null;
        int highestScore = -1;
        for (Player p : players) {
            if (p.getScore() > highestScore) {
                winner = p;
                highestScore = p.getScore();
            }
        }
        return winner;
    }

    /**
     * Resets the board
     */
    public void reset() {
        this.board.initBoard();
        this.tileBag = new TileBag(System.getProperty("user.dir") + "/src/letters.txt");
        Random r = new Random();
        this.currentPlayerIndex = r.nextInt(players.size());
    }

    /**
     * Place the tiles on the board
     * @param move - the move made by a player
     */
    public void placeTileOnBoard(Move move) {
        String word = move.getWord().toUpperCase();
        int startRow = board.convertRow(move.getPlaceRow());
        int startCol = board.convertCol(move.getPlaceCol());

        for (int i = 0; i < word.length(); i++) {
            if (move.getDirection() == Move.HORIZONTAL) {
                if (board.getTileOnBoard(startRow, startCol + i) != ' ') {
                    continue; // not putting the tile if there is existing tile on board (most likely this letter is not on the rack either)
                }
                board.setTileOnBoard(startRow, startCol + i, word.toUpperCase().charAt(i));
            } else if (move.getDirection() == Move.VERTICAL) {
                if (board.getTileOnBoard(startRow + i, startCol) != ' ') {
                    continue; // not putting the tile if there is existing tile on board (most likely this letter is not on the rack either)
                }
                board.setTileOnBoard(startRow + i, startCol, word.toUpperCase().charAt(i));
            }
        }
    }



    public static void main(String[] args) {

        LocalTUI tui = new LocalTUI();
        Scanner scanner;
        Player p1 = new Player("Michael");
        Player p2 = new Player("Chiko");
        List<Player> players = new ArrayList<>();
        players.add(p1);
        players.add(p2);
        Game newGame = new Game(players);

        String input1 = "paste H B 1";
        String input2 = "bit H A 2";
        scanner = new Scanner(input1);
        try {
            p1.makeMove(scanner);
            newGame.placeTileOnBoard(p1.getMove());
        } catch (InvalidMoveException e) {
            e.printStackTrace();
        }
        scanner = new Scanner(input2);
        try {
            p2.makeMove(scanner);
            System.out.println(newGame.getAllWords(p2.getMove()));
            newGame.placeTileOnBoard(p2.getMove());
        } catch (InvalidMoveException e) {
            e.printStackTrace();
        }

        System.out.println(newGame.getBoard().getTileOnBoard('B',1));

        tui.printBoard(newGame.getBoard());

        System.out.println(System.getProperty("user.dir"));
    }


} // end of class
