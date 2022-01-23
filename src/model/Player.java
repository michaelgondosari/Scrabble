package model;

import java.util.ArrayList;
import java.util.List;

public class Player {

    // --- Variables -------------------------------

    private String name;
    private int score;
    private List<Character> rack;
    private boolean isMyTurn;

    // --- Constructor -----------------------------

    /**
     * Constructor of Player
     * @param name - player's name
     * @param rack - player's tile rack
     * @param isMyTurn - true if it is the player's turn, false otherwise
     */
    public Player (String name, List<Character> rack, boolean isMyTurn) {
        this.name = name;
        this.score = 0;
        this.rack = new ArrayList<>(rack);
        this.isMyTurn = isMyTurn;
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
        return new ArrayList<>(rack);
    }

    /**
     * A boolean checking if it is the player turn
     * @return true if it is the player's turn, false if not
     */
    public boolean getMyTurn() {
        return isMyTurn;
    }

    /**
     * Set the turn of a player
     * @param isMyTurn - true if it is the player's turn, false if not
     */
    public void setMyTurn(boolean isMyTurn) {
        this.isMyTurn = isMyTurn;
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
     * @param points - points to be added to player's current score
     */
    public void addScore(int points) {
        score += points;
    }

    /**
     * Helper class for useTiles
     * Use a tile from the player's rack and remove it from the rack
     * @param tile - tile to be used
     */
    private void useTile(char tile) {
        rack.remove(tile);
    }

    /**
     * Use a list of tiles from the player's rack and remove those from the rack
     * @param tilesToUse - list of tiles to be used
     */
    public void useTiles(List<Character> tilesToUse) {
        for (char tile : tilesToUse) {
            this.useTile(tile);
        }
    }

    /**
     * Add tiles to the player's rack
     * @param tilesToAdd - list of tiles to be added to the rack
     */
    public void addTiles(List<Character> tilesToAdd) {
        rack.addAll(tilesToAdd);
    }

} // end of class
