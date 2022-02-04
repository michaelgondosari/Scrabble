package network.server;

import exception.ExitProgram;
import exception.InvalidMoveException;
import exception.InvalidWordException;
import game.Game;
import game.Player;
import network.protocol.ProtocolMessages;
import network.protocol.ServerProtocol;
import game.tui.LocalTUI;
import game.tui.TerminalColors;

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

    private final String FEATURES = ProtocolMessages.PASS_TURN_FLAG + ProtocolMessages.CHAT_FLAG;

    // Game objects
    private Game newGame;
    private List<Player> players;
    LocalTUI tui;
    boolean gameOver;

    private Map<Player, ClientHandler> playerHandler;
    Player currentPlayer;
    ClientHandler currentHandler;
    ClientHandler opponentHandler;

    // --- Constructor -----------------------------

    /**
     * Constructor for the Server class
     */
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
                    view.showMessage("> [" + handler.getName() + "] connected!");

                    // After making network.client handler, if 2 players or more connected, send network.server ready, then break
                    synchronized (this) {
                        if (clients.size() > 1) {
                            broadcast(doServerReady()); // network.server ready confirmation
                            broadcast(tui.askReady()); // player ready confirmation
                            break;
                        }
                    }
                }

                // Wait until there are 2 clients that are ready
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

                    // Broadcast the initial board
                    broadcast(tui.printBoard(newGame.getBoard()));

                    while (!gameOver || !newGame.gameOver()) {

                        // Determine current player
                        currentPlayer = newGame.getCurrentPlayer();
                        currentHandler = playerHandler.get(currentPlayer);
                        currentHandler.setMyTurn(true);

                        // Determine other player (opponent)
                        List<ClientHandler> opponentList = new ArrayList<>();
                        for (ClientHandler ch : readyClients) {
                            opponentList.add(ch);
                        }
                        opponentList.remove(currentHandler);
                        opponentHandler = opponentList.get(0);

                        // Wait for the player to make a move
                        broadcast(doTurn());
                        currentHandler.sendMessage(tui.askCommand(currentPlayer));
                        wait();

                        // Next player's turn
                        currentHandler.setMyTurn(false);
                        newGame.nextPlayer();
                        broadcast(tui.printBoard(newGame.getBoard()));

                    } // end while not game over

                    doGameOver();

                } // end synchronized

            } catch (ExitProgram e) {
                openNewSocket = false;
                view.showMessage("Closing the network.server...");

            } catch (IOException e) {
                view.showMessage(TerminalColors.RED_BOLD + "A network.server IO error occurred: "
                        + e.getMessage() + TerminalColors.RESET);
                if (!view.getBoolean("Do you want to open a new socket (yes/no) ?")) {
                    openNewSocket = false;
                    view.showMessage("Closing the network.server...");
                }

            } catch (InterruptedException e) {
                view.showMessage(TerminalColors.RED_BOLD + e.getMessage() + TerminalColors.RESET);
            }
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
            int port = view.getInt("Please enter the network.server port (between 0 and 65535): ");

            // Try to open a new ServerSocket
            try {
                view.showMessage("Attempting to open a socket at " + host + " on port " + port + "...");
                ssock = new ServerSocket(port, 0, InetAddress.getByName(host));
                view.showMessage("Server started at " + host + " port " + port);
            } catch (IOException e) {
                view.showMessage(TerminalColors.RED_BOLD + "ERROR: could not create a socket on " + host
                        + " and port " + port + TerminalColors.RESET);
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
        gameOver = false;
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
     * Adds a clientHandler to the network.client list.
     * @requires network.client != null
     */
    public void addClient(ClientHandler client) {
        this.clients.add(client);
    }

    /**
     * Removes a clientHandler from the network.client list.
     * @requires network.client != null
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
     * Gets a list of names from the connected network.client
     * @return a list of names from the connected network.client
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
        String str = "Welcome to Scrabble, " + name + "!" + " (features: " + FEATURES + ")";
        return str;
    }

    @Override
    public synchronized void doWelcome(String name, String features) {
        String str = "Current online players in the game: " + clientNames() + " (features: " + FEATURES + ")";
        broadcast(str);
    }

    @Override
    public synchronized String doError(String errorCode) {
        String str = TerminalColors.RED_BOLD + "ERROR: " + errorCode + TerminalColors.RESET;
        return str;
    }

    @Override
    public synchronized String doServerReady() {
        return "The network.server is ready!";
    }

    @Override
    public synchronized void doServerReady(ClientHandler clientHandler) {
        readyClients.add(clientHandler);
        StringBuffer result = new StringBuffer();
        result.append("Players that are ready:");
        for (ClientHandler ch : readyClients) {
            result.append(System.lineSeparator() + ch.getName());
        }
        broadcast(result.toString());
        notifyAll();
    }

    @Override
    public synchronized String doStart() {
        StringBuffer result = new StringBuffer();
        result.append("Starting the game..." + System.lineSeparator());
        result.append("Players:");
        for (ClientHandler ch : readyClients) {
            result.append(System.lineSeparator() + ch.getName());
        }
        return result.toString();
    }

    @Override
    public synchronized void doAbort(ClientHandler clientHandler) {
        broadcast("Player " + clientHandler.getName() + " quits the game network.server.");
        doGameOver();
        notifyAll();
    }

    @Override
    public synchronized String doTiles(Player player) {
        String str = "Your current rack: " + player.getCurrentTiles();
        return str;
    }

    @Override
    public synchronized String doTurn() {
        String str = "It is now " + newGame.getCurrentPlayer().getName() + "'s turn.";
        return str;
    }

    @Override
    public synchronized void doMove(ClientHandler clientHandler, String coordinates) {
        String[] inputMove = coordinates.split(ProtocolMessages.AS);

        try {
            currentPlayer.makeMove(inputMove);
        } catch (InvalidMoveException e) {
            currentHandler.sendMessage(doError(ProtocolMessages.INVALID_MOVE)
                    + TerminalColors.RED_BOLD + ": " + e.getMessage() + TerminalColors.RESET);
            notifyAll();
        } catch (NumberFormatException nfe) {
            currentHandler.sendMessage(doError(ProtocolMessages.INVALID_MOVE) + TerminalColors.RED_BOLD
                    + ": " + "Please enter a valid number for the row!" + TerminalColors.RESET);
            notifyAll();
        }

        try {
            // If blank tile(s) is played
            if (currentPlayer.getMove().getWord().contains("-")) {
                String wordWithoutBlanks = newGame.removeMinus(currentPlayer.getMove());
                String wordWithoutReplacement = newGame.removeCharAfterMinus(currentPlayer.getMove());

                // Check if the move is valid
                currentPlayer.getMove().setWord(wordWithoutReplacement);
                if (newGame.checkMoveInsideBoard(currentPlayer.getMove())
                        && newGame.checkUsingAvailableTiles(currentPlayer.getMove())
                        && newGame.checkMoveOverwrite(currentPlayer.getMove())
                        && newGame.checkFirstMoveCenter(currentPlayer.getMove())
                        && newGame.checkMoveTouchTile(currentPlayer.getMove())) {

                    // Check if the word is valid
                    currentPlayer.getMove().setWord(wordWithoutBlanks);
                    if (newGame.checkWordsValid(newGame.getAllWords(currentPlayer.getMove()))) {
                        // If valid, then do the move :
                        // 1. remove tiles from player's rack
                        currentPlayer.getMove().setWord(wordWithoutReplacement);
                        currentPlayer.removeTilesFromRack(newGame.tilesToRemove(currentPlayer.getMove()));
                        // 2. calculate points and update the player's points
                        currentPlayer.getMove().setWord(wordWithoutBlanks);
                        int moveScore = newGame.calculateScore(currentPlayer.getMove());
                        currentPlayer.addScore(moveScore);
                        // 3. players draw new tiles
                        int tileUsed = newGame.tilesToRemove(currentPlayer.getMove()).size();
                        currentPlayer.addTilesToRack(newGame.getTileBag().drawTiles(tileUsed));
                        // 4. place tiles on board
                        newGame.placeTileOnBoard(currentPlayer.getMove());
                        // 5.print player's name, score, and current rack
                        StringBuffer sb = new StringBuffer();
                        sb.append(tui.updateAfterMove(newGame, moveScore) + System.lineSeparator());
                        opponentHandler.sendMessage(sb.toString());
                        sb.append(doTiles(currentPlayer));
                        currentHandler.sendMessage(sb.toString());
                        // Don't forget to notify the waiting thread
                        notifyAll();
                    }
                }
            }

            // If no blank tiles are played
            else {
                // Check if the move is valid
                if (newGame.checkMoveInsideBoard(currentPlayer.getMove())
                        && newGame.checkUsingAvailableTiles(currentPlayer.getMove())
                        && newGame.checkMoveOverwrite(currentPlayer.getMove())
                        && newGame.checkFirstMoveCenter(currentPlayer.getMove())
                        && newGame.checkMoveTouchTile(currentPlayer.getMove())) {

                    // Check if the word is valid
                    if (newGame.checkWordsValid(newGame.getAllWords(currentPlayer.getMove()))) {

                        // If move and word are valid, then do the move :
                        // 1. remove tiles from player's rack
                        currentPlayer.removeTilesFromRack(newGame.tilesToRemove(currentPlayer.getMove()));
                        // 2. calculate points and update the player's points
                        int moveScore = newGame.calculateScore(currentPlayer.getMove());
                        currentPlayer.addScore(moveScore);
                        // 3. players draw new tiles
                        int tileUsed = newGame.tilesToRemove(currentPlayer.getMove()).size();
                        currentPlayer.addTilesToRack(newGame.getTileBag().drawTiles(tileUsed));
                        // 4. place tiles on board
                        newGame.placeTileOnBoard(currentPlayer.getMove());
                        // 5.print player's name, score, and current rack
                        StringBuffer sb = new StringBuffer();
                        sb.append(tui.updateAfterMove(newGame, moveScore) + System.lineSeparator());
                        opponentHandler.sendMessage(sb.toString());
                        sb.append(doTiles(currentPlayer));
                        currentHandler.sendMessage(sb.toString());
                        // Don't forget to notify the waiting thread
                        notifyAll();
                    }
                }
            }
        } // end of try

        // If the move or word is invalid
        catch (InvalidMoveException | InvalidWordException e) {
            currentHandler.sendMessage(doError(ProtocolMessages.INVALID_MOVE)
                    + TerminalColors.RED_BOLD + ": " + e.getMessage() + TerminalColors.RESET);
            notifyAll();
        }
    }

    @Override
    public synchronized void doPass(ClientHandler clientHandler) {
        broadcast("Player " + clientHandler.getName() + " has passed his/her turn.");
        notifyAll();
    }

    @Override
    public synchronized void doPass(ClientHandler clientHandler, String tiles) {
        // Check if the move is valid
        List<Character> tilesToSwap = new ArrayList<>();
        String[] swapTile = tiles.split(" ");
        for (String s : swapTile) {
            tilesToSwap.add(s.toUpperCase().charAt(0));
        }
        try {
            if (currentPlayer.checkSwapTilesInRack(tilesToSwap)) {
                // If valid, then do the move
                currentPlayer.removeTilesFromRack(tilesToSwap);
                List<Character> swappedTiles = newGame.getTileBag().swapTiles(tilesToSwap);
                currentPlayer.addTilesToRack(swappedTiles);
                // send message
                broadcast("Player " + clientHandler.getName() + " has passed his/her turn and swap tiles.");
                currentHandler.sendMessage(doTiles(currentPlayer));
                notifyAll();
            }
        } catch (InvalidMoveException e) {
            currentHandler.sendMessage(doError(ProtocolMessages.INVALID_MOVE)
                    + TerminalColors.RED_BOLD + ": " + e.getMessage() + TerminalColors.RESET); // if swap is invalid
            notifyAll();
        }
    }

    @Override
    public synchronized void doGameOver() {
        gameOver = true;
        broadcast(tui.gameOver(newGame));
    }

    // ------------------ Main --------------------------

    /** Start a new HotelServer */
    public static void main(String[] args) {
        Server server = new Server();
        view.showMessage(TerminalColors.WHITE_BOLD + "Welcome to the Scrabble Server! Starting..." + TerminalColors.RESET);
        new Thread(server).start();
    }

} // end of class
