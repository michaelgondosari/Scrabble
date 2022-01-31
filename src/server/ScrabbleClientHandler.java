package server;

import java.io.*;
import java.net.Socket;

public class ScrabbleClientHandler implements Runnable {

    // --- Variables -------------------------------

    /** The In/OutputStreams and Socket */
    private BufferedReader in;
    private BufferedWriter out;
    private Socket sock;

    /** The connected ScrabbleServer */
    private ScrabbleServer srv;

    /** Name of this ClientHandler */
    private String name;

    // --- Constructor -----------------------------

    public ScrabbleClientHandler(Socket sock, ScrabbleServer srv, String name) {
        try {
            in = new BufferedReader(
                    new InputStreamReader(sock.getInputStream()));
            out = new BufferedWriter(
                    new OutputStreamWriter(sock.getOutputStream()));
            this.sock = sock;
            this.srv = srv;
            this.name = name;
        } catch (IOException e) {
            shutDown();
        }
    }

    // --- Queries ---------------------------------



    // --- Commands --------------------------------

    @Override
    public void run() {
        String msg;
        try {
            msg = in.readLine();
            while (msg != null) {
                System.out.println("> [" + name + "] Incoming: " + msg);
                handleCommand(msg);
                out.newLine();
                out.flush();
                msg = in.readLine();
            }
        } catch (IOException e) {
            shutDown();
        }
    }

    private void handleCommand(String msg) throws IOException {
        // translate disini
    }

    private void shutDown() {
        System.out.println("Player " + name + " exiting.");
        try {
            in.close();
            out.close();
            sock.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        srv.removePlayer(this);
    }

}
