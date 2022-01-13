package model;

public class Square {

    // --- Variables -------------------------------

    private int row;
    private int column;

    // --- Constructor -----------------------------

    public Square(int row, int column) {
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
