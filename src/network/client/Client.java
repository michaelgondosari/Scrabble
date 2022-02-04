package network.client;

import exception.ExitProgram;
import exception.ProtocolException;
import exception.ServerUnavailableException;
import game.tui.TerminalColors;
import network.protocol.ClientProtocol;
import network.protocol.ProtocolMessages;

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

    private final static String FEATURES = ProtocolMessages.PASS_TURN_FLAG + ProtocolMessages.CHAT_FLAG;

    // --- Constructor -----------------------------

    /**
     * Constructor for the Client class
     */
    public Client() {
        serverSock = null;
        in = null;
        out = null;
        tui = new ClientTUI(this);
    }

    // --- Queries ---------------------------------

    /**
     * Set the name of the network.client
     * @param name name of the network.client
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get this network.client's name
     * @return this network.client's name
     */
    public String getName() {
        return this.name;
    }

    // --- Commands --------------------------------

    /**
     * Starts a new Client thread
     * to keep on reading incoming message in the stream
     */
    public void run() {
        try {
            String line;
            while ((line = in.readLine()) != null) {
                tui.showMessage(line);
            }
        } catch (IOException e) {
            tui.showMessage(TerminalColors.RED_BOLD + e.getMessage() + TerminalColors.RESET);
        }
    }

    /**
     * Creates a connection to the network.server. Requests the IP and port to
     * connect to at the view (TUI).
     *
     * The method continues to ask for an IP and port and attempts to connect
     * until a connection is established or until the user indicates to exit
     * the program.
     *
     * @throws ExitProgram if a connection is not established and the user
     * 				       indicates to want to exit the program.
     * @ensures serverSock contains a valid socket connection to a network.server
     */
    public void createConnection() throws ExitProgram {
        clearConnection();
        while (serverSock == null) {
            String host = tui.getString("Enter a host: ");
            int port = tui.getInt("Enter a valid port (between 0 and 65535): ");

            // try to open a Socket to the network.server
            try {
                InetAddress addr = InetAddress.getByName(host);
                tui.showMessage("Attempting to connect to " + addr + ":" + port + "...");
                serverSock = new Socket(addr, port);
                in = new BufferedReader(new InputStreamReader(serverSock.getInputStream()));
                out = new BufferedWriter(new OutputStreamWriter(serverSock.getOutputStream()));
            } catch (IOException | IllegalArgumentException e) {
                tui.showMessage(TerminalColors.RED_BOLD + "ERROR: could not create a socket on "
                        + host + " and port " + port + "." + TerminalColors.RESET);

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
     * Sends a message to the connected network.server, followed by a new line.
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
                tui.showMessage(TerminalColors.RED_BOLD + e.getMessage() + TerminalColors.RESET);
                throw new ServerUnavailableException(TerminalColors.RED_BOLD
                        + "Could not write to network.server." + TerminalColors.RESET);
            }
        } else {
            throw new ServerUnavailableException(TerminalColors.RED_BOLD
                    + "Could not write to network.server." + TerminalColors.RESET);
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
            tui.showMessage(TerminalColors.RED_BOLD + e.getMessage() + TerminalColors.RESET);
        }
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
    }

    @Override
    public void doClientReady(String name) throws ServerUnavailableException {
        if (name != null) {
            sendMessage(ProtocolMessages.CLIENTREADY
                    + ProtocolMessages.SEPARATOR
                    + name
            );
        }
    }

    @Override
    public void doAbort(String name) throws ServerUnavailableException, ProtocolException {
        if (name != null) {
            sendMessage(ProtocolMessages.ABORT
                    + ProtocolMessages.SEPARATOR
                    + name
            );
            closeConnection();
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
        }
    }

    @Override
    public void doPass() throws ServerUnavailableException {
        sendMessage(ProtocolMessages.PASS);
    }

    @Override
    public void doPass(String tiles) throws ServerUnavailableException {
        if (tiles != null) {
            sendMessage(ProtocolMessages.PASS
                    + ProtocolMessages.SEPARATOR
                    + tiles
            );
        }
    }

    public void doChat(String msg) throws ServerUnavailableException {
        if (msg != null) {
            sendMessage(ProtocolMessages.MSGSEND
                    + ProtocolMessages.SEPARATOR
                    + msg
            );
        }
    }

    // --- Main ------------------------------------

    /**
     * Starts a new Client by creating a connection,
     * followed by the HELLO handshake as defined in the network.protocol.
     * After a successful connection and handshake, the view is started.
     * The view asks for user input and handles all further calls to methods of this class.
     *
     * When errors occur, or when the user terminates a network.server connection,
     * the user is asked whether a new connection should be made.
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
