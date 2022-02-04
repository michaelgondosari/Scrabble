package network.client;

import exception.ExitProgram;
import exception.InvalidMoveException;
import exception.ProtocolException;
import exception.ServerUnavailableException;
import network.protocol.ProtocolMessages;
import game.tui.TerminalColors;

import java.io.PrintWriter;
import java.util.Scanner;

public class ClientTUI implements ClientView {

    // --- Variables -------------------------------

    private Client client;
    private Scanner scanner;
    private PrintWriter console;

    // --- Constructor -----------------------------

    /**
     * Constructor of the ClientTUI class
     * @param client network.client class connected to this TUI
     */
    public ClientTUI(Client client) {
        this.client = client;
        scanner = new Scanner(System.in);
        console = new PrintWriter(System.out, true);
    }

    // --- Commands --------------------------------

    @Override
    public void start() throws ServerUnavailableException {
        boolean repeat = true;
        while (repeat) {
            String input = null;
            while (input == null) {
                input = scanner.nextLine();
                if (input == null) {
                    showMessage(TerminalColors.RED_BOLD + "Input cannot be empty!" + TerminalColors.RESET);
                }
            }
            try {
                handleUserInput(input);
            } catch (ExitProgram e) {
                showMessage(TerminalColors.RED_BOLD + e.getMessage() + TerminalColors.RESET);
                repeat = false;
                try {
                    client.doAbort(client.getName());
                } catch (ProtocolException ex) {
                    showMessage(TerminalColors.RED_BOLD + ex.getMessage() + TerminalColors.RESET);
                }
            } catch (InvalidMoveException e) {
                showMessage(TerminalColors.RED_BOLD + e.getMessage() + TerminalColors.RESET);
            }
        }
    }

    @Override
    public void handleUserInput(String input) throws ExitProgram, ServerUnavailableException, InvalidMoveException {
        String[] inputArray = input.toUpperCase().split(ProtocolMessages.AS);
        String command = inputArray[0];

        switch (command) {
            case "READY":
                client.doClientReady(client.getName());
                break;
            case "MOVE":
                String coordinate = getString(printAskMove());
                client.doMove(client.getName(), coordinate);
                break;
            case "SWAP":
                String tilesToSwap = getString(printAskSwap());
                client.doPass(tilesToSwap);
                break;
            case "SKIP":
                client.doPass();
                break;
            case "QUIT":
                throw new ExitProgram("Exiting...");
            case "CHAT":
                String message = getString("Enter your message:");
                client.doChat(message);
                break;
            default:
                showMessage("Unknown command!");
                break;
        }
    }

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
                showMessage(TerminalColors.RED + "That is not a valid number!" + TerminalColors.RESET);
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
                showMessage(TerminalColors.RED_BOLD + "Invalid input! Enter yes or no." + TerminalColors.RESET);
            }
        }
    }

    /**
     * Ask the user about the move to be made
     * @return String to ask user for the move
     */
    public String printAskMove() {
        StringBuffer sb = new StringBuffer();
        sb.append(String.format("Player %s, please enter your move in below format:"
                + System.lineSeparator(), client.getName()));
        sb.append("word direction startingColumn startingRow" + System.lineSeparator());
        sb.append("==================================================================" + System.lineSeparator());
        sb.append("word          : Enter a valid English word" + System.lineSeparator());
        sb.append("direction     : H for horizontal or V for vertical" + System.lineSeparator());
        sb.append("staringColumn : choose one of A,B,C,D,E,F,G,H,I,J,K,L,M,N,O" + System.lineSeparator());
        sb.append("startingRow   : choose one of 1,2,3,4,5,6,7,8,9,10,11,12,13,14,15" + System.lineSeparator());
        sb.append("For blank tiles, put a minus sign and the letter that you want to change it to." + System.lineSeparator());
        sb.append("Example: SCRABBLE V H 8   or   SCRA-BBLE H A 1" + System.lineSeparator());
        sb.append("Enter your input: " + System.lineSeparator());
        return sb.toString();
    }

    /**
     * Ask the user about the tiles to be swapped
     * @return String to ask user for the swap
     */
    public String printAskSwap() {
        StringBuffer sb = new StringBuffer();
        sb.append(String.format("Player %s, please enter your tiles to be swapped (separated by space):"
                + System.lineSeparator(), client.getName()));
        sb.append("Enter your input: " + System.lineSeparator());
        return sb.toString();
    }

} // end of class
