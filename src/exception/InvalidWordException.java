package exception;

import game.tui.TerminalColors;

public class InvalidWordException extends Exception {

    public InvalidWordException() {
        super(TerminalColors.RED_BOLD + "The word is not found in the dictionary!" + TerminalColors.RESET);
    }

}
