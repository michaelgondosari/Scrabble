package client;

import exception.ExitProgram;
import exception.InvalidMoveException;
import exception.ProtocolException;
import exception.ServerUnavailableException;
import model.Board;
import model.Move;
import protocol.ClientProtocol;
import protocol.ProtocolMessages;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class Client implements ClientProtocol, Runnable {

    // --- Variables -------------------------------

    private Socket serverSock;
    private BufferedReader in;
    private BufferedWriter out;
    private static ClientTUI tui;
    private static String name;

    private final static String FEATURES = ProtocolMessages.PASS_TURN_FLAG;

    // --- Constructor -----------------------------

    public Client() {
        serverSock = null;
        in = null;
        out = null;
        tui = new ClientTUI(this);
    }

    // --- Queries ---------------------------------

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    // --- Commands --------------------------------

    /**
     * Starts a new Client by creating a connection,
     * followed by the HELLO handshake as defined in the protocol.
     * After a successful connection and handshake, the view is started.
     * The view asks for user input and handles all further calls to methods of this class.
     *
     * When errors occur, or when the user terminates a server connection,
     * the user is asked whether a new connection should be made.
     */
    public void run() {
        // continuously read incoming message
        try {
            String line;
            while ((line = in.readLine()) != null) {
                tui.showMessage(line);
            }
        } catch (IOException e) {
            tui.showMessage(e.getMessage());
        }
    }

        /**
     * Creates a connection to the server. Requests the IP and port to
     * connect to at the view (TUI).
     *
     * The method continues to ask for an IP and port and attempts to connect
     * until a connection is established or until the user indicates to exit
     * the program.
     *
     * @throws ExitProgram if a connection is not established and the user
     * 				       indicates to want to exit the program.
     * @ensures serverSock contains a valid socket connection to a server
     */
    public void createConnection() throws ExitProgram {
        clearConnection();
        while (serverSock == null) {
            String host = tui.getString("Enter a host: ");
            int port = tui.getInt("Enter a valid port (between 0 and 65535): ");

            // try to open a Socket to the server
            try {
                InetAddress addr = InetAddress.getByName(host);
                System.out.println("Attempting to connect to " + addr + ":"
                        + port + "...");
                serverSock = new Socket(addr, port);
                in = new BufferedReader(new InputStreamReader(
                        serverSock.getInputStream()));
                out = new BufferedWriter(new OutputStreamWriter(
                        serverSock.getOutputStream()));
            } catch (IOException e) {
                System.out.println("ERROR: could not create a socket on "
                        + host + " and port " + port + ".");

                //Do you want to try again?
                if (!tui.getBoolean("Do you want to try again (yes/no) ?")) {
                    throw new ExitProgram("Exiting...");
                }
            }
        }
    }

    /**
     * Resets the serverSocket and In- and OutputStreams to null.
     *
     * Always make sure to close current connections via shutdown()
     * before calling this method!
     */
    public void clearConnection() {
        serverSock = null;
        in = null;
        out = null;
    }

    /**
     * Sends a message to the connected server, followed by a new line.
     * The stream is then flushed.
     *
     * @param msg the message to write to the OutputStream.
     * @throws ServerUnavailableException if IO errors occur.
     */
    public synchronized void sendMessage(String msg)
            throws ServerUnavailableException {
        if (out != null) {
            try {
                out.write(msg);
                out.newLine();
                out.flush();
            } catch (IOException e) {
                tui.showMessage(e.getMessage());
                throw new ServerUnavailableException("Could not write to server.");
            }
        } else {
            throw new ServerUnavailableException("Could not write to server.");
        }
    }



    /**
     * Reads and returns one line from the server.
     *
     * @return the line sent by the server.
     * @throws ServerUnavailableException if IO errors occur.
     */
    public String readLineFromServer()
            throws ServerUnavailableException {
        if (in != null) {
            try {
                // Read and return answer from Server
                String answer = in.readLine();
                if (answer == null) {
                    throw new ServerUnavailableException("Could not read from server.");
                }
                return answer;
            } catch (IOException e) {
                throw new ServerUnavailableException("Could not read from server.");
            }
        } else {
            throw new ServerUnavailableException("Could not read from server.");
        }
    }

    /**
     * Reads and returns multiple lines from the server until the end of
     * the text is indicated using a line containing ProtocolMessages.EOT.
     *
     * @return the concatenated lines sent by the server.
     * @throws ServerUnavailableException if IO errors occur.
     */
    public String readMultipleLinesFromServer()
            throws ServerUnavailableException {
        if (in != null) {
            try {
                // Read and return answer from Server
                StringBuilder sb = new StringBuilder();
                String line;
                while((line = in.readLine()) != null) {
                    sb.append(line + System.lineSeparator());
                }
                return sb.toString();
            } catch (IOException e) {
                throw new ServerUnavailableException("Could not read from server.");
            }
        } else {
            throw new ServerUnavailableException("Could not read from server.");
        }
    }

    /**
     * Closes the connection by closing the In- and OutputStreams, as
     * well as the serverSocket.
     */
    public void closeConnection() {
        System.out.println("Closing the connection...");
        try {
            in.close();
            out.close();
            serverSock.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Translates move to coordinate used by protocol
     * @param move move made by player
     * @return translation of move into protocol coordinate
     * @throws InvalidMoveException if move is invalid
     */
    public String translateMoveToCoordinate(String[] move) throws InvalidMoveException {
        String coordinate = "";
        try {
            String word = move[0].toUpperCase();
            char direction = move[1].toUpperCase().charAt(0);
            char startCol = move[2].toUpperCase().charAt(0);
            int startRow = Integer.parseInt(move[3]);
            int index = ((startCol-65) * Board.BOARD_SIZE) + (startRow-1);

            if (startCol < 65 || startCol > 79) {
                throw new InvalidMoveException("That column does not exist!");
            }
            if (startRow < 0 || startRow > 14) {
                throw new InvalidMoveException("That row does not exist!");
            }
            for (int i = 0; i < word.length(); i++) {
                if (direction == Move.HORIZONTAL) {
                    coordinate += String.format("%s%s ", word.charAt(i), (index + i));
                } else if (direction == Move.VERTICAL) {
                    coordinate += String.format("%s%s ", word.charAt(i), (index + (i * Board.BOARD_SIZE)));
                } else {
                    throw new InvalidMoveException("That direction does not exist!");
                }
            }

        } catch (NumberFormatException e) {
            tui.showMessage("Wrong row input!");
        } catch (IndexOutOfBoundsException e) {
            tui.showMessage("Wrong move command!");
        }

        return coordinate;
    }

    // --- Handle Commands -------------------------

    @Override
    public void handleHello(String name, String features) throws ServerUnavailableException, ProtocolException {
        sendMessage(ProtocolMessages.HELLO
                + ProtocolMessages.SEPARATOR
                + name
                + ProtocolMessages.SEPARATOR
                + features
        );

//        String[] serverAnswer = readLineFromServer().split(ProtocolMessages.SEPARATOR);
//        if (serverAnswer[0].equals(ProtocolMessages.HELLO) && serverAnswer.length == 3) {
//            tui.showMessage("Welcome to Scrabble, " + name);
//        } else {
//            throw new ProtocolException("Response is not valid!");
//        }
    }

    @Override
    public void doClientReady(String name) throws ServerUnavailableException {
        if (name != null) {
            sendMessage(ProtocolMessages.CLIENTREADY
                    + ProtocolMessages.SEPARATOR
                    + name
            );

//            String[] serverAnswer = readLineFromServer().split(ProtocolMessages.SEPARATOR);
//            if (serverAnswer[0].equals(ProtocolMessages.SERVERREADY) && serverAnswer.length == 2) {
//                tui.showMessage("You are ready. Waiting for 1 more player to join...");
//            } else if (serverAnswer[0].equals(ProtocolMessages.SERVERREADY) && serverAnswer.length == 3) {
//                tui.showMessage("You and the opponent are ready. Game will begin soon...");
//            } else {
//                throw new ProtocolException("Response is not valid!");
//            }
        }
    }

    @Override
    public void doAbort(String name) throws ServerUnavailableException, ProtocolException {
        if (name != null) {
            sendMessage(ProtocolMessages.ABORT
                    + ProtocolMessages.SEPARATOR
                    + name
            );

            String[] serverAnswer = readLineFromServer().split(ProtocolMessages.SEPARATOR);
            if (serverAnswer[0].equals(ProtocolMessages.ABORT)) {
                closeConnection();
            } else {
                throw new ProtocolException("Response is not valid!");
            }
        }
    }

    @Override
    public void doMove(String name, String coordinates) throws ServerUnavailableException {
        if (name != null && coordinates != null) {
            sendMessage(ProtocolMessages.MOVE
                    + ProtocolMessages.SEPARATOR
                    + name
                    + ProtocolMessages.SEPARATOR
                    + coordinates
            );
            tui.showMessage("[server] : " + readLineFromServer());
        }
    }

    @Override
    public void doPass() throws ServerUnavailableException {
        sendMessage(ProtocolMessages.PASS);
        tui.showMessage("[server] : " + readLineFromServer());
    }

    @Override
    public void doPass(String tiles) throws ServerUnavailableException {
        if (tiles != null) {
            sendMessage(ProtocolMessages.PASS
                    + ProtocolMessages.SEPARATOR
                    + tiles
            );
            tui.showMessage("[server] : " + readLineFromServer());
        }
    }

    // --- Main ------------------------------------

    /**
     * This method starts a new Client.
     */
    public static void main(String[] args) {
        Client client = new Client();
        while (true) {
            try {
                client.setName(tui.getString("Please input your name: "));
                client.createConnection();
                new Thread(client).start();
                client.handleHello(name, FEATURES);
                tui.start();

            } catch (ExitProgram | ServerUnavailableException | ProtocolException e) {
                tui.showMessage(e.getMessage());
                boolean tryAgain = tui.getBoolean("Do you want to try again?");
                if (!tryAgain) {
                    break;
                }
            }
        }
    }

} // end of class
