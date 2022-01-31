package server;

import java.io.PrintWriter;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ScrabbleServerTUI {

    // --- Variables -------------------------------

    private PrintWriter console;
    private Scanner scanner;

    // --- Constructor -----------------------------

    public ScrabbleServerTUI() {
        console = new PrintWriter(System.out, true);
        scanner = new Scanner(System.in);
    }

    // --- Queries ---------------------------------

    // --- Commands --------------------------------

    public void showMessage(String message) {
        console.println(message);
    }

    public String getString(String question) {
        String answerStr = "";
        while (answerStr.equals("")) {
            console.println(question);
            try {
                answerStr = scanner.nextLine();
            } catch (NoSuchElementException e) {
                console.println("Input cannot be blank!");
            }
        }
        return answerStr;
    }

    public int getInt(String question) {
        int answerInt = -1;
        while (answerInt == -1) {
            console.println(question);
            try {
                answerInt = scanner.nextInt();
            } catch (InputMismatchException e) {
                console.println("That is not a valid number!");
            }
        }
        return answerInt;
    }

    public boolean getBoolean(String question) {
        console.println(question);
        return scanner.nextBoolean();
    }

}
