package model;

public class Square {

    // --- Variables -------------------------------

    private char row;
    private int column;

    // --- Constructor -----------------------------

    public Square(char row, int column) {
        this.row = row;
        this.column = column;
    }

    // --- Queries ---------------------------------

    public int getRow() {
        return this.row;
    }

    public int getColumn() {
        return this.column;
    }

    // --- Commands --------------------------------

}
