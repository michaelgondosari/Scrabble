package view;

import model.Board;
import model.Game;
import model.Player;

public class LocalTUI {

    /**
     * Print the current Scrabble board
     * @param board - current Scrabble board
     * @return TUI of current Scrabble board
     */
    public void printBoard(Board board) {

        StringBuffer boardView = new StringBuffer();

        // 1st line (A-O)
        System.out.println("");
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

        System.out.println(boardView);
    }

    public void askCommand(Player player) {
        System.out.println(String.format("\nPlayer %s, your turn, choose one from below options:", player.getName()));
        System.out.println("==========================================================");
        System.out.println("MOVE  : enter a word onto the Scrabble board");
        System.out.println("SWAP  : swap some or all of your current tiles");
        System.out.println("SKIP  : skip your turn");
        System.out.println("SCORE : view current score");
        System.out.println("QUIT  : quit the game");
        System.out.println("Your current rack: " + player.getCurrentTiles());
        System.out.println("> ");
    }

    public void askMove(Player player) {
        System.out.println(String.format("\nPlayer %s, please enter your move in below format:", player.getName()));
        System.out.println("word direction startingColumn startingRow");
        System.out.println("==================================================================");
        System.out.println("word          : Enter a valid English word");
        System.out.println("direction     : H for horizontal or V for vertical");
        System.out.println("staringColumn : choose one of A,B,C,D,E,F,G,H,I,J,K,L,M,N,O");
        System.out.println("startingRow   : choose one of 1,2,3,4,5,6,7,8,9,10,11,12,13,14,15");
        System.out.println("> ");
    }

    public void askSkip(Player player) {
        System.out.println(String.format("\nPlayer %s skips a turn.", player.getName()));
    }

    public void askSwap(Player player) {
        System.out.println(String.format("\nPlayer %s, please enter your tiles to be swapped (separated by comma):", player.getName()));
        System.out.println("> ");
    }

    public void askScore(Game game) {
        System.out.println("");
        for (Player p : game.getPlayers()) {
            System.out.println(String.format("Player : %s, Score : %d", p.getName(), p.getScore()));
        }
    }

    public void gameOver(Game game) {
        System.out.println("\nGame over!");
        System.out.println(String.format("Player %s wins with a score of %d.", game.getWinner().getName(), game.getWinner().getScore()));
    }

//    public static void main(String[] args) {
//        LocalTUI tui = new LocalTUI();
//        Board b = new Board();
//        Player p = new Player("Michael");
//        tui.printBoard(b);
//        tui.askMove(p);
//        tui.askCommand(p);
//        tui.askSwap(p);
//    }

}
