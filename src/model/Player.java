package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Player {

    // --- Variables -------------------------------

    private String name;
    private int score;
    private List<Character> rack;
    private Move move;
//    private boolean isMyTurn;

    // --- Constructor -----------------------------

    /**
     * Constructor of Player
     * @param name - player's name
     */
    public Player (String name) {
        this.name = name;
        this.score = 0;
        this.rack = new ArrayList<>();
//        this.isMyTurn = false;
    }

    // --- Queries ---------------------------------

    /**
     * Get the player's name
     * @return player's name
     */
    public String getName() {
        return name;
    }

    /**
     * Get the player's score
     * @return player's score
     */
    public int getScore() {
        return score;
    }

    /**
     * Get the tile of a specific index from the player's rack
     * @param index - index of the tile in the rack
     * @return a tile of the index from the rack
     */
    public char getTile(int index) {
        return rack.get(index);
    }

    /**
     * Get a list of current tiles in the player's rack
     * @return all current tiles in the rack
     */
    public List<Character> getCurrentTiles() {
        return this.rack;
    }

    /**
     * Get the move of the player
     * @return the move of the player
     */
    public Move getMove() {
        return this.move;
    }

//    /**
//     * A boolean checking if it is the player turn
//     * @return true if it is the player's turn, false if not
//     */
//    public boolean getMyTurn() {
//        return isMyTurn;
//    }
//
//    /**
//     * Set the turn of a player
//     * @param isMyTurn - true if it is the player's turn, false if not
//     */
//    public void setMyTurn(boolean isMyTurn) {
//        this.isMyTurn = isMyTurn;
//    }

    // --- Commands --------------------------------

    /**
     * Get size of player's rack
     * @return how many tiles are in the player's current rack
     */
    public int getRackSize() {
        return rack.size();
    }

    /**
     * Clears the player's rack
     */
    public void clearRack() {
        rack.clear();
    }

    /**
     * Add points to player's current score
     * @param points - points to be added to player's current score
     */
    public void addScore(int points) {
        score += points;
    }

    /**
     * Helper class for useTiles
     * Remove a tile from the player's rack
     * @param tile - tile to be removed
     */
    private void removeTileFromRack(char tile) {
        rack.remove(rack.indexOf(tile));
    }

    /**
     * Remove a list of tiles from the player's rack
     * @param tilesToUse - list of tiles to be removed
     */
    public void removeTilesFromRack(List<Character> tilesToUse) {
        for (char tile : tilesToUse) {
            this.removeTileFromRack(tile);
        }
    }

    /**
     * Add tiles to the player's rack
     * @param tilesToAdd - list of tiles to be added to the rack
     */
    public void addTilesToRack(List<Character> tilesToAdd) {
        rack.addAll(tilesToAdd);
    }

    /**
     * Ask the player to make a move
     * @throws InvalidMoveException if the input is invalid
     */
    public void makeMove() throws InvalidMoveException {
        Scanner scanMove = new Scanner(System.in);

        String word = scanMove.next();
        char direction = scanMove.next().toUpperCase().charAt(0);
        char startCol = scanMove.next().toUpperCase().charAt(0);
        int startRow = scanMove.nextInt();

        if ( !(direction == 'H' || direction == 'V')
                || !(startCol >= 65 && startCol < 65 + Board.BOARD_SIZE)
                || !(startRow > 0 && startRow <= Board.BOARD_SIZE) ) {
            throw new InvalidMoveException("That is an invalid move!");
        }

        this.move = new Move(word, direction, startCol, startRow);
        scanMove.close();
    }

    /**
     * Check if the player has all the tiles that the player wants to swap
     * @param tilesToSwap - list of tiles that the player wants to swap
     * @return true if the player has all the tiles that the player wants to swap
     * @throws InvalidMoveException if there are tiles that the player does not have
     */
    public boolean checkSwapTilesInRack(List<Character> tilesToSwap) throws InvalidMoveException {
        List<Character> playerRack = getCurrentTiles();
        for (char c : tilesToSwap) {
            if (playerRack.contains(c)) {
                playerRack.remove(playerRack.indexOf(c));
            } else {
                throw new InvalidMoveException("There are some tiles not in your current rack!");
            }
        }
        return true;
    }

//    public static void main(String[] args) {
//        Board b = new Board();
//        Player p1 = new Player("Michael");
//        try {
//            p1.makeMove();
//        } catch (InvalidMoveException e) {
//            System.out.println(e.getMessage());
//        }
//    }

} // end of class
