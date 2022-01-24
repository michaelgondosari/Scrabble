package model;

public class TileOverwriteException extends Exception {

    public TileOverwriteException() {
        super("You cannot overwrite an existing tile on the board!");
    }

}
