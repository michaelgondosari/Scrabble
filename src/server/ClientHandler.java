package server;

import protocol.ProtocolMessages;
import view.TerminalColors;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {

    // --- Variables -------------------------------

    private Socket sock;
    private BufferedReader in;
    private BufferedWriter out;
    private Server srv;

    private String name = "New player";
    private boolean isReady = false;
    private boolean isMyTurn = false;

    // --- Constructor -----------------------------

    /**
     * Constructor of the ClientHandler class
     * @param sock socket of the ClientHandler
     * @param srv server to be connected to
     */
    public ClientHandler (Socket sock, Server srv) {
        try {
            in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
            this.sock = sock;
            this.srv = srv;
        } catch (IOException e) {
            shutdown();
        }
    }

    // --- Queries ---------------------------------

    /**
     * Gets the name of the ClientHandler
     * @return the name of the ClientHandler
     */
    public String getName() {
        return this.name;
    }

    /**
     * Sets the name of the ClientHandler
     * @param name the name to be set to
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the ready status of the ClientHandler
     * @param ready true if ready, false otherwise
     */
    public void setReady(boolean ready) {
        isReady = ready;
    }

    /**
     * Sets the turn status of the ClientHandler
     * @param myTurn true if this is the current player to make a move, false otherwise
     */
    public void setMyTurn(boolean myTurn) {
        isMyTurn = myTurn;
    }

    // --- Commands --------------------------------

    /**
     * Continuously listens to client input and forwards the input to the
     * {@link #handleCommand(String)} method.
     */
    @Override
    public void run() {
        try {
            String msg = in.readLine();
            while (msg != null) {
                System.out.println("> [" + name + "] Incoming: " + msg);
                handleCommand(msg);
                out.newLine();
                out.flush();
                msg = in.readLine();
            }
            shutdown();
        } catch (IOException e) {
            shutdown();
        }
    }

    /**
     * Send a message to the client
     * @param msg message to be sent
     */
    public synchronized void sendMessage(String msg) {
        try {
            out.write(msg);
            out.newLine();
            out.flush();
        } catch (IOException e) {
            shutdown();
        }
    }

    /**
     * Handles commands received from the client by calling the according methods at the Server.
     * For example, when the message "HI:;:NAME:;:FEATURES" is received,
     * the method getHello() of Server should be called
     * and the output must be sent to the client.
     *
     * If the received input is not valid, send an "Unknown Command" message to the server.
     *
     * @param msg command from client
     * @throws IOException if an IO errors occur.
     */
    private void handleCommand(String msg) throws IOException {
        String[] msgArray = msg.toUpperCase().split(ProtocolMessages.SEPARATOR);
        String command = msgArray[0];

        switch (command) {

            case ProtocolMessages.HELLO:
                if (msgArray.length == 3) {
                    String name = msgArray[1];
                    String features = msgArray[2];
                    setName(name);
                    sendMessage(srv.getHello(name, features));
                    srv.doWelcome(name, features);
                } else {
                    sendMessage(TerminalColors.RED_BOLD + "Wrong command for HELLO!" + TerminalColors.RESET);
                }
                break;

            case ProtocolMessages.CLIENTREADY:
                if (msgArray.length == 2) {
                    String name = msgArray[1];
                    setReady(true);
                    srv.doServerReady(this);
                } else {
                    sendMessage(TerminalColors.RED_BOLD + "Wrong command for CLIENTREADY!" + TerminalColors.RESET);
                }
                break;

            case ProtocolMessages.MOVE:
                if (isMyTurn) {
                    if (msgArray.length == 3) {
                        String coordinate = msgArray[2];
                        srv.doMove(this, coordinate);
                    } else {
                        sendMessage(TerminalColors.RED_BOLD + "Wrong command for MOVE!" + TerminalColors.RESET);
                    }
                } else {
                    sendMessage(srv.doError(ProtocolMessages.OUT_OF_TURN)
                            + TerminalColors.RED_BOLD + ", it is not your turn!" + TerminalColors.RESET);
                }
                break;

            case ProtocolMessages.PASS:
                if (isMyTurn) {
                    if (msgArray.length == 1) { // skip
                        srv.doPass(this);
                    } else if (msgArray.length == 2) { // swap
                        String tiles = msgArray[1];
                        srv.doPass(this, tiles);
                    } else {
                        sendMessage(TerminalColors.RED_BOLD + "Wrong command for PASS!" + TerminalColors.RESET);
                    }
                } else {
                    sendMessage(srv.doError(ProtocolMessages.OUT_OF_TURN)
                            + TerminalColors.RED_BOLD + ", it is not your turn!" + TerminalColors.RESET);
                }
                break;

            case ProtocolMessages.ABORT:
                srv.doAbort(this);
                shutdown();

            case ProtocolMessages.MSGSEND:
                if (msgArray.length == 2) {
                    String chatMessage = msgArray[1];
                    srv.broadcast("[" + name + "]: " + chatMessage);
                } else {
                    sendMessage(srv.doError(ProtocolMessages.UNRECOGNIZED)
                            + TerminalColors.RED_BOLD + ", wrong command for CHAT" + TerminalColors.RESET);
                }
                break;

            default:
                sendMessage(TerminalColors.RED_BOLD + "Unknown command!" + TerminalColors.RESET);
                break;
        }
    }

    /**
     * Shut down the connection to this client by closing the socket and
     * the In- and OutputStreams.
     */
    private void shutdown() {
        System.out.println("> [" + name + "] Shutting down.");
        try {
            srv.removeClient(this);
            in.close();
            out.close();
            sock.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

} // end of class
