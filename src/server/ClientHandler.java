package server;

import protocol.ProtocolMessages;

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

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isReady() {
        return isReady;
    }

    public void setReady(boolean ready) {
        isReady = ready;
    }

    public boolean isMyTurn() {
        return isMyTurn;
    }

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
                    sendMessage("Wrong command for HELLO!");
                }
                break;

            case ProtocolMessages.CLIENTREADY:
                if (msgArray.length == 2) {
                    String name = msgArray[1];
                    setReady(true);
                    srv.doServerReady(this);
                } else {
                    sendMessage("Wrong command for CLIENTREADY!");
                }
                break;

                // check if current player makes the move?
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

}
