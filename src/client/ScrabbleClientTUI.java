package client;

import exception.ExitProgram;
import exception.ServerUnavailableException;

import java.io.PrintWriter;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ScrabbleClientTUI {

    // --- Variables -------------------------------

    private PrintWriter console;
    private ScrabbleClient player;
    private Scanner scanner;

    // --- Constructor -----------------------------

    public ScrabbleClientTUI(ScrabbleClient player) {
        this.player = player;
        scanner = new Scanner(System.in);
        console = new PrintWriter(System.out, true);
    }

    // --- Queries ---------------------------------



    // --- Commands --------------------------------

    public void start() throws ServerUnavailableException {

    }

    public void handleUserInput(String input) throws ExitProgram, ServerUnavailableException {

    }

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
