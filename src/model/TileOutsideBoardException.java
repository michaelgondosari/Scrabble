package model;

public class TileOutsideBoardException extends Exception {

    public TileOutsideBoardException() {
        super("You cannot place a tile outside the board!");
    }

}
