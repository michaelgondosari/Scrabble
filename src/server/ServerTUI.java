package server;

import java.io.PrintWriter;
import java.util.Scanner;

public class ServerTUI implements ServerView {

    // --- Variables -------------------------------

    private PrintWriter console;
    private Scanner scanner;

    // --- Constructor -----------------------------

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
                showMessage("That is not a valid number!");
            }
        }
    }

    @Override
    public boolean getBoolean(String question) {
        showMessage(question);
        String answer = scanner.nextLine();
        while (true) {
            if (answer.equalsIgnoreCase("YES")) {
                return true;
            } else if (answer.equalsIgnoreCase("NO")) {
                return false;
            } else {
                showMessage("Invalid input! Enter yes or no.");
            }
        }
    }

}
