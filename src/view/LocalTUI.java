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
    public String printBoard(Board board) {

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

        return boardView.toString();
    }

    public String askReady() {
        return "Type ready to start the game: ";
    }

    public String askCommand(Player player) {
        StringBuffer sb = new StringBuffer();
        sb.append(String.format("\nPlayer %s, your turn, choose one from below options:", player.getName()));
        sb.append("\n==========================================================");
        sb.append("\nMOVE  : enter a word onto the Scrabble board");
        sb.append("\nSWAP  : swap some or all of your current tiles");
        sb.append("\nSKIP  : skip your turn");
//        sb.append("\nSCORE : view current score");
        sb.append("\nQUIT  : quit the game");
        sb.append("\nCHAT  : send a message to other players");
        sb.append("\nYour current rack: " + player.getCurrentTiles());
        sb.append("\nEnter your input: ");
        return sb.toString();
    }

    public String askMove(Player player) {
        StringBuffer sb = new StringBuffer();
        sb.append(String.format("\nPlayer %s, please enter your move in below format:", player.getName()));
        sb.append("\nword direction startingColumn startingRow");
        sb.append("\n==================================================================");
        sb.append("\nword          : Enter a valid English word");
        sb.append("\ndirection     : H for horizontal or V for vertical");
        sb.append("\nstaringColumn : choose one of A,B,C,D,E,F,G,H,I,J,K,L,M,N,O");
        sb.append("\nstartingRow   : choose one of 1,2,3,4,5,6,7,8,9,10,11,12,13,14,15");
        sb.append("\nEnter your input: ");
        return sb.toString();
    }

    public String askSkip(Player player) {
        StringBuffer sb = new StringBuffer();
        sb.append(String.format("\nPlayer %s skips a turn.", player.getName()));
        return sb.toString();
    }

    public String askSwap(Player player) {
        StringBuffer sb = new StringBuffer();
        sb.append(String.format("\nPlayer %s, please enter your tiles to be swapped (separated by space):", player.getName()));
        sb.append("\nEnter your input: ");
        return sb.toString();
    }

//    public String askScore(Game game) {
//        StringBuffer sb = new StringBuffer();
//        for (Player p : game.getPlayers()) {
//            sb.append(String.format("\nPlayer : %s, Score : %d", p.getName(), p.getScore()));
//        }
//        return sb.toString();
//    }

    public String updateAfterMove(Game game, int moveScore) {
        StringBuffer sb = new StringBuffer();
        sb.append(String.format("\nPlayer %s, move score : %d, total points : %d",
                game.getCurrentPlayer().getName(), moveScore, game.getCurrentPlayer().getScore()));
//        sb.append("\nNew rack : " + game.getCurrentPlayer().getCurrentTiles());
        return sb.toString();
    }

    public String gameOver(Game game) {
        StringBuffer sb = new StringBuffer();
        sb.append("\nGame over!");
        sb.append(String.format("\nPlayer %s wins with a score of %d.", game.getWinner().getName(), game.getWinner().getScore()));
        return sb.toString();
    }

} // end of class
