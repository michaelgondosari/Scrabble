package network.server;

import game.tui.TerminalColors;

import java.io.PrintWriter;
import java.util.Scanner;

public class ServerTUI implements ServerView {

    // --- Variables -------------------------------

    private PrintWriter console;
    private Scanner scanner;

    // --- Constructor -----------------------------

    /**
     * Constructor of the ServerTUI class
     */
    public ServerTUI() {
        console = new PrintWriter(System.out, true);
        scanner = new Scanner(System.in);
    }

    // --- Commands --------------------------------

    @Override
    public void showMessage(String message) {
        console.println(message);
    }

    @Override
    public String getString(String question) {
        showMessage(question);
        String answer = scanner.nextLine();
        return answer;
    }

    @Override
    public int getInt(String question) {
        int answerInt = 0;
        while (true) {
            showMessage(question);
            try {
                answerInt = Integer.parseInt(scanner.nextLine());
                return answerInt;
            } catch (NumberFormatException e) {
                showMessage(TerminalColors.RED_BOLD + "That is not a valid number!" + TerminalColors.RESET);
            }
        }
    }

    @Override
    public boolean getBoolean(String question) {
        showMessage(question);
        String answer;
        while (true) {
            answer = scanner.nextLine();
            if (answer.equalsIgnoreCase("YES")) {
                return true;
            } else if (answer.equalsIgnoreCase("NO")) {
                return false;
            } else {
                showMessage(TerminalColors.RED_BOLD + "Invalid input! Enter yes or no." + TerminalColors.RESET);
            }
        }
    }

} // end of class
