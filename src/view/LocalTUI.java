package view;

import model.Board;

public class LocalTUI {

    /**
     * Print the current Scrabble board
     * @param board - current Scrabble board
     * @return TUI of current Scrabble board
     */
    public static String printBoard(Board board) {

        StringBuffer boardView = new StringBuffer();

        // 1st line (A-O)
        boardView.append("   ");
        for (int col = 0; col < Board.BOARD_SIZE; col++) {
            boardView.append("  " + (char)(65 + col) + " ");
        }
        boardView.append("\n");

        // 2nd line
        boardView.append("   ┌");
        for (int col = 0; col < Board.BOARD_SIZE-1; col++) {
            boardView.append("───┬");
        }
        boardView.append("───┐");
        boardView.append("\n");

        // middle lines
        for (int row = 0; row < Board.BOARD_SIZE; row++) {
            boardView.append(((row < 9) ? " " : "") + (row + 1) + " │");
            for (int col = 0; col < Board.BOARD_SIZE; col++) {
                String coordinate = board.getCoordinate(row, col);
                switch (board.getBoardMultiplier(coordinate)) {
                    case "3W":
                        boardView.append(TerminalColors.RED_BACKGROUND);
                        break;
                    case "2W":
                        boardView.append(TerminalColors.PURPLE_BACKGROUND);
                        break;
                    case "3L":
                        boardView.append(TerminalColors.GREEN_BACKGROUND_BRIGHT);
                        break;
                    case "2L":
                        boardView.append(TerminalColors.BLUE_BACKGROUND_BRIGHT);
                        break;
                    case "1":
                        boardView.append(TerminalColors.WHITE_BACKGROUND);
                        break;
                }
                char tileOnBoard = board.getTileOnBoard(row, col);
                boardView.append(" " + (TerminalColors.BLACK_BOLD + ((tileOnBoard == ' ') ? " " : tileOnBoard)) + " ");
                boardView.append(TerminalColors.RESET);
                boardView.append("|");
            }
            boardView.append("\n");

            // make borders between the rows
            if (row < Board.BOARD_SIZE - 1) {
                boardView.append("   ├");
                for (int col = 0; col < Board.BOARD_SIZE - 1; col++) {
                    boardView.append("───┼");
                }
                boardView.append("───┤");
                boardView.append("\n");
            }
        }

        // last line
        boardView.append("   └");
        for (int i = 0; i < Board.BOARD_SIZE-1; i++) {
            boardView.append("───┴");
        }
        boardView.append("───┘");

        return boardView.toString();
    }

//    public static void main(String[] args) {
//        Board bp = new Board();
//        System.out.println(printBoard(bp));
//    }

}
