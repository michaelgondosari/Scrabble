package game;

import exception.InvalidMoveException;
import game.tui.TerminalColors;

import java.util.ArrayList;
import java.util.List;

public class Player {

    // --- Variables -------------------------------

    private String name;
    private int score;
    private List<Character> rack;
    private Move move;

    // --- Constructor -----------------------------

    /**
     * Constructor of Player
     * @param name - player's name
     */
    public Player (String name) {
        this.name = name;
        this.score = 0;
        this.rack = new ArrayList<>();
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
     * @param points points to be added to player's current score
     */
    public void addScore(int points) {
        score += points;
    }

    /**
     * Helper class for useTiles
     * Remove a tile from the player's rack
     * @param tile tile to be removed
     */
    private void removeTileFromRack(char tile) {
        getCurrentTiles().remove((Character) tile);
    }

    /**
     * Remove a list of tiles from the player's rack
     * @param tilesToRemove list of tiles to be removed
     */
    public void removeTilesFromRack(List<Character> tilesToRemove) {
        for (char tile : tilesToRemove) {
            removeTileFromRack(tile);
        }
    }

    /**
     * Add tiles to the player's rack
     * @param tilesToAdd list of tiles to be added to the rack
     */
    public void addTilesToRack(List<Character> tilesToAdd) {
        rack.addAll(tilesToAdd);
    }

    /**
     * Player makes a move
     *
     * @param moveCommand the move made by the player
     * @throws InvalidMoveException if the input is invalid
     */
    public void makeMove(String[] moveCommand) throws InvalidMoveException {
        if (moveCommand.length != 4) {
            throw new InvalidMoveException(TerminalColors.RED_BOLD + "Invalid move command!" + TerminalColors.RESET);
        }
        String word = moveCommand[0].toUpperCase();
        char direction = moveCommand[1].toUpperCase().charAt(0);
        char startCol = moveCommand[2].toUpperCase().charAt(0);
        int startRow = Integer.parseInt(moveCommand[3]);
        this.move = new Move(word, direction, startCol, startRow);
    }

    /**
     * Creates a deep copy of the player's current rack
     * @return a deep copy of the player's current rack
     */
    public List<Character> rackCopy() {
        List<Character> rackCopy = new ArrayList<>();
        for (char c : getCurrentTiles()) {
            rackCopy.add(c);
        }
        return rackCopy;
    }

    /**
     * Check if the player has all the tiles that the player wants to swap
     *
     * @param tilesToSwap list of tiles that the player wants to swap
     * @return true if the player has all the tiles that the player wants to swap
     * @throws InvalidMoveException if there are tiles that the player does not have
     */
    public boolean checkSwapTilesInRack(List<Character> tilesToSwap) throws InvalidMoveException {
        List<Character> playerRack = this.rackCopy();
        for (char c : tilesToSwap) {
            if (playerRack.contains(c)) {
                playerRack.remove((Character) c);
            } else {
                throw new InvalidMoveException(TerminalColors.RED_BOLD
                        + "There are some tiles not in your current rack!" + TerminalColors.RESET);
            }
        }
        return true;
    }

} // end of class
