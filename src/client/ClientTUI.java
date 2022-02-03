package client;

import exception.ExitProgram;
import exception.InvalidMoveException;
import exception.ProtocolException;
import exception.ServerUnavailableException;
import protocol.ProtocolMessages;

import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ClientTUI implements ClientView {

    // --- Variables -------------------------------

    private Client client;
    private Scanner scanner;
    private PrintWriter console;

    // --- Constructor -----------------------------

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
//            printHelpMenu();
            String input = null;
            while (input == null) {
                input = scanner.nextLine();
                if (input == null) {
                    showMessage("Input cannot be empty!");
                }
            }
            try {
                handleUserInput(input);
            } catch (ExitProgram e) {
                repeat = false;
                try {
                    client.doAbort(client.getName());
                } catch (ProtocolException ex) {
                    ex.printStackTrace();
                }
            } catch (InvalidMoveException e) {
                showMessage(e.getMessage());
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
                String[] answerMove = getString(printAskMove()).split(ProtocolMessages.AS);
                String coordinate = client.translateMoveToCoordinate(answerMove);
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
    public InetAddress getIp() {
        InetAddress result = null;
        while (result == null) {
            System.out.println("Please enter a valid IP Address: ");
            String ip = scanner.nextLine();
            try {
                result = InetAddress.getByName(ip);
            } catch (UnknownHostException e) {
                showMessage(e.getMessage());
            }
        }
        return result;
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

    @Override
    public void printHelpMenu() {
        StringBuffer sb = new StringBuffer();
        sb.append(String.format("\nPossible commands, choose one from below options:"));
        sb.append("\n==========================================================");
        sb.append("\nMOVE  : enter a word onto the Scrabble board");
        sb.append("\nSWAP  : swap some or all of your current tiles");
        sb.append("\nSKIP  : skip your turn");
        sb.append("\nQUIT  : quit the game");
        sb.append("\nEnter your input: ");
        showMessage(sb.toString());
    }

    public String printAskMove() {
        StringBuffer sb = new StringBuffer();
        sb.append(String.format("\nPlayer %s, please enter your move in below format:", client.getName()));
        sb.append("\nword direction startingColumn startingRow");
        sb.append("\n==================================================================");
        sb.append("\nword          : Enter a valid English word");
        sb.append("\ndirection     : H for horizontal or V for vertical");
        sb.append("\nstaringColumn : choose one of A,B,C,D,E,F,G,H,I,J,K,L,M,N,O");
        sb.append("\nstartingRow   : choose one of 1,2,3,4,5,6,7,8,9,10,11,12,13,14,15");
        sb.append("\nFor blank tiles, put a minus sign and the letter that you want to change it to.");
        sb.append("\nExample: SCRA-BBLE H A 1");
        sb.append("\nEnter your input: ");
        return sb.toString();
    }

    public String printAskSwap() {
        StringBuffer sb = new StringBuffer();
        sb.append(String.format("\nPlayer %s, please enter your tiles to be swapped (separated by space):", client.getName()));
        sb.append("\nEnter your input: ");
        return sb.toString();
    }

} // end of class
