package server;

import exception.ExitProgram;
import model.Game;
import model.Player;
import protocol.ProtocolMessages;
import protocol.ServerProtocol;
import view.LocalTUI;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Server implements Runnable, ServerProtocol {

    // --- Variables -------------------------------

    private ServerSocket ssock;
    private List<ClientHandler> clients;
    private static ServerTUI view;
    private List<ClientHandler> readyClients;

    private final String FEATURES = ProtocolMessages.PASS_TURN_FLAG;

    // Game objects
    private Game newGame;
    private List<Player> players;
    LocalTUI tui;


    private Map<Player, ClientHandler> playerHandler;

    // --- Constructor -----------------------------

    public Server() {
        clients = new ArrayList<>();
        view = new ServerTUI();
        readyClients = new ArrayList<>();
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

                    Socket sock = ssock.accept();
                    ClientHandler handler = new ClientHandler(sock, this);
                    addClient(handler);
                    new Thread(handler).start();
                    view.showMessage("[" + handler.getName() + "] connected!");

                    // after making client handler, if 2 players or more connected, send server ready, then break
                    synchronized (this) {
                        if (clients.size() > 1) {
                            broadcast(doServerReady()); // server ready confirmation
                            broadcast(tui.askReady()); // player ready confirmation
                            break;
                        }
                    }
                }

                // wait until there are 2 ready clients
                synchronized (this) {
                    while (true) {
                        wait();
                        if (readyClients.size() > 1) {
                            break;
                        }
                        broadcast("Waiting for 1 more player to join...");
                    }
                    broadcast(doStart());
                }

                // Initialize the game
                setupGame();

                synchronized (this) {
                    boolean playAgain = true;
                    while (playAgain) {

                        // Broadcast the initial board
                        broadcast(tui.printBoard(newGame.getBoard()));

                        while (!newGame.gameOver()) {

                            // determine current player
                            Player currentPlayer = newGame.getCurrentPlayer();
                            ClientHandler currentHandler = playerHandler.get(currentPlayer);
                            currentHandler.setMyTurn(true);

                            // game logic here


                            // Next player's turn
                            currentHandler.setMyTurn(false);
                            newGame.nextPlayer();
                            broadcast(tui.printBoard(newGame.getBoard()));

                        } // end while not game over

                    } // end while play again
                } // end synchronized

            } catch (ExitProgram e) {
                openNewSocket = false;

            } catch (IOException e) {
                view.showMessage("A server IO error occurred: " + e.getMessage());
                if (!view.getBoolean("Do you want to open a new socket (yes/no) ?")) {
                    openNewSocket = false;
                }

            } catch (InterruptedException e) {
                view.showMessage(e.getMessage());
            }

            view.showMessage("Closing the server...");
        }

    }

    /**
     * Sets up a new Hotel using {@link #setupGame()} and opens a new
     * ServerSocket at localhost on a user-defined port.
     *
     * The user is asked to input a port, after which a socket is attempted to be opened.
     * If the attempt succeeds, the method ends.
     * If the attempt fails, the user decides to try again,
     * after which an ExitProgram exception is thrown or a new port is entered.
     *
     * @throws ExitProgram if a connection can not be created on the given
     *                     port and the user decides to exit the program.
     * @ensures a serverSocket is opened.
     */
    public void setup() throws ExitProgram {

        ssock = null;
        while (ssock == null) {
            String host = view.getString("Please enter the host: ");
            int port = view.getInt("Please enter the server port (between 0 and 65535) : ");

            // try to open a new ServerSocket
            try {
                view.showMessage("Attempting to open a socket at " + host + " on port " + port + "...");
                ssock = new ServerSocket(port, 0, InetAddress.getByName(host));
                view.showMessage("Server started at " + host + " port " + port);
            } catch (IOException e) {
                view.showMessage("ERROR: could not create a socket on " + host + " and port " + port);
                if (!view.getBoolean("Do you want to try again?")) {
                    throw new ExitProgram("User indicated to exit the program.");
                }
            }
        }
    }

    /**
     * Set up a new game with connected clients
     */
    public void setupGame() {
        players = new ArrayList<>();
        playerHandler = new HashMap<>();
        for (ClientHandler ch : readyClients) {
            Player newPlayer = new Player(ch.getName());
            players.add(newPlayer);
            playerHandler.put(newPlayer, ch);
        }
        newGame = new Game(players);
    }

    /**
     * Adds a clientHandler to the client list.
     * @requires client != null
     */
    public void addClient(ClientHandler client) {
        this.clients.add(client);
    }

    /**
     * Removes a clientHandler from the client list.
     * @requires client != null
     */
    public void removeClient(ClientHandler client) {
        this.clients.remove(client);
    }

    /**
     * Sends a message using the collection of connected ClientHandlers
     * to all connected Clients.
     * @param msg message that is sent
     */
    public synchronized void broadcast(String msg) {
        for (ClientHandler ch : clients) {
            ch.sendMessage(msg);
        }
    }

    /**
     * Gets a list of names from the connected client
     * @return a list of names from the connected client
     */
    public List<String> clientNames() {
        List<String> names = new ArrayList<>();
        for (ClientHandler ch : clients) {
            names.add(ch.getName());
        }
        return names;
    }

    // --- Handle Commands -------------------------

    @Override
    public synchronized String getHello(String name, String features) {
        return ProtocolMessages.HELLO
                + ProtocolMessages.SEPARATOR
                + clientNames()
                + ProtocolMessages.SEPARATOR
                + FEATURES;
    }

    @Override
    public synchronized void doWelcome(String name, String features) {
        broadcast(ProtocolMessages.WELCOME
                    + ProtocolMessages.SEPARATOR
                    + name
                    + ProtocolMessages.SEPARATOR
                    + features);
    }

    @Override
    public synchronized String doError(String errorCode) {
        return ProtocolMessages.ERROR
                + ProtocolMessages.SEPARATOR
                + errorCode;
    }

    @Override
    public synchronized String doServerReady() {
        return ProtocolMessages.SERVERREADY;
    }

    @Override
    public synchronized void doServerReady(ClientHandler clientHandler) {
        readyClients.add(clientHandler);
        StringBuffer result = new StringBuffer();
        result.append(ProtocolMessages.SERVERREADY);
        for (ClientHandler ch : readyClients) {
            result.append(ProtocolMessages.SEPARATOR);
            result.append(ch.getName());
        }
        broadcast(result.toString());
        notifyAll();
    }

    @Override
    public synchronized String doStart() {
        StringBuffer result = new StringBuffer();
        result.append(ProtocolMessages.START);
        for (ClientHandler ch : readyClients) {
            result.append(ProtocolMessages.SEPARATOR);
            result.append(ch.getName());
        }
        return result.toString();
    }

    @Override
    public synchronized String doAbort(String name) {
        return ProtocolMessages.ABORT
                + ProtocolMessages.SEPARATOR
                + name;
    }

    @Override
    public synchronized String doTiles(String tiles) {
        return ProtocolMessages.TILES
                + ProtocolMessages.SEPARATOR
                + tiles;
    }

    @Override
    public synchronized String doTurn(String name) {
        return ProtocolMessages.TURN
                + ProtocolMessages.SEPARATOR
                + name;
    }

    @Override
    public synchronized String doMove(String name, String coordinates, int gainedPoints) {
        return ProtocolMessages.MOVE
                + ProtocolMessages.SEPARATOR
                + name
                + ProtocolMessages.SEPARATOR
                + coordinates
                + ProtocolMessages.SEPARATOR
                + gainedPoints;
    }

    @Override
    public synchronized String doPass(String name) {
        return ProtocolMessages.PASS
                + ProtocolMessages.SEPARATOR
                + name;
    }

    @Override
    public synchronized String doPass(String name, String tiles) {
        return ProtocolMessages.PASS
                + ProtocolMessages.SEPARATOR
                + name
                + ProtocolMessages.SEPARATOR
                + tiles;
    }

    @Override
    public synchronized String doGameOver(String name) {
        return ProtocolMessages.GAMEOVER
                + ProtocolMessages.SEPARATOR
                + name;
    }

    // ------------------ Main --------------------------

    /** Start a new HotelServer */
    public static void main(String[] args) {
        Server server = new Server();
        view.showMessage("Welcome to the Scrabble Server! Starting...");
        new Thread(server).start();
    }

} // end of class
