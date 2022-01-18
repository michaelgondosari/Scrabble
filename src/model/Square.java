package model;

public class Square {

    // --- Variables -------------------------------

    private char column; // A to O
    private int row; // 1 to 15
    private char tile;

    // --- Constructor -----------------------------

    public Square(char column, int row) {
        this.column = column;
        this.row = row;
        this.tile = '$';
    }

    public Square(char column, int row, char tile) {
        this.column = column;
        this.row = row;
        this.tile = tile;
    }

    // --- Queries ---------------------------------

    public int getRow() {
        return this.row;
    }

    public int getColumn() {
        return this.column;
    }

    public void setTile(char tile) {
        this.tile = tile;
    }

    public char getTile() {
        return this.tile;
    }

    public boolean hasTile() {
        return this.tile != '$';
    }

    // --- Commands --------------------------------

}
