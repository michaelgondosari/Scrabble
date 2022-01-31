package server;

import exception.ExitProgram;
import model.Game;
import model.Player;
import protocol.ProtocolMessages;
import view.LocalTUI;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ScrabbleServer implements Runnable {

    // --- Variables -------------------------------

    private ServerSocket servSock;
    private List<ScrabbleClientHandler> playersClientHandler;
    private ScrabbleServerTUI serverTUI;

    private Game newGame;
    private List<Player> players;
    private LocalTUI tui;

    // --- Constructor -----------------------------

    public ScrabbleServer() {
        playersClientHandler = new ArrayList<>();
        serverTUI = new ScrabbleServerTUI();
        players = new ArrayList<>();
        tui = new LocalTUI();
    }

    // --- Commands --------------------------------

    @Override
    public void run() {
        boolean openNewSocket = true;
        while (openNewSocket) {
            try {
                setup();

                while (true) {
                    Socket sock = servSock.accept();

                }
            } catch (ExitProgram ep) {
                openNewSocket = false;
            } catch (IOException ioe) {
                serverTUI.showMessage("A server IO error occurred: " + ioe.getMessage());
            }
        }
    }

    public void removePlayer(ScrabbleClientHandler player) {
        this.players.remove(player);
    }

    public void setupGame() {

    }

    public void setup() throws ExitProgram {

    }

    // --- Server Commands -------------------------

    public String getHello() {
        return ProtocolMessages.HELLO
                + ProtocolMessages.SEPARATOR
                + players
                + ProtocolMessages.SEPARATOR
                + ProtocolMessages.PASS_TURN_FLAG;
    }

} // end of class
