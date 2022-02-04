package network.protocol;

import game.Player;
import network.server.ClientHandler;

public interface ServerProtocol {

    /**
     * Returns a String to be sent as a response to a Client HELLO request,
     * including the name of the hotel:
     * ProtocolMessages.HELLO:;:[NAMES]:;:FEATURES;
     *
     * @param name The list of names already in the network.server including your own
     * @param features The extra optional features supported by this network.client
     *
     * @return textual result, to be shown to the user
     */
    public String getHello(String name, String features);

    /**
     * This message is broadcasted to every network.client connected in the room,
     * so that they are aware of the new connection,
     * as well as what features they support:
     * ProtocolMessages.WELCOME:;:NAME:;:FEATURES;
     *
     * @param name The name of the player who just joined
     * @param features The extra optional features supported by this network.client
     */
    public void doWelcome(String name, String features);

    /**
     * The appropriate error code is sent by the network.server when something has gone wrong
     * @param errorCode the appropriate error code sent by the network.server
     * @return textual result, to be shown to the user
     */
    public String doError(String errorCode);

    /**
     * This is sent by the network.server to all clients connected to indicate that the network.server is ready to start.
     * The first of these commands will be sent without arguments by the network.server to indicate readiness.
     * @return textual result, to be shown to the user
     */
    public String doServerReady();

    /**
     * This is sent by the network.server to all clients connected to indicate that the network.server is ready to start,
     * and so are the clients names in the arguments.
     *
     * @param clientHandler The network.client handler that is ready to start
     */
    public void doServerReady(ClientHandler clientHandler);

    /**
     * This is sent by the network.server as the first command after starting a game.
     * All the names of the players participating in the game will be given as arguments.
     * It will be followed by the TILES and TURN commands from the network.server
     *
     * @return textual result, to be shown to the user
     */
    public String doStart();

    /**
     * This is broadcast by the network.server to all clients to indicate that another player has just disconnected.
     * This player will pass every turn he/she gets automatically
     *
     * @param clientHandler The disconnected network.client
     * @return textual result, to be shown to the user
     */
    public void doAbort(ClientHandler clientHandler);

    /**
     * This is sent by the network.server when a player is drawing new tiles.
     * This happens after every move, then all the tiles are sent back (new & old)
     * Each player connected will get different randomized tiles.
     *
     * @param player The player owning the tiles
     *
     * @return textual result, to be shown to the user
     */
    public String doTiles(Player player);

    /**
     * Used to inform every network.client connected who has to make a move
     * @return textual result, to be shown to the user
     */
    public String doTurn();

    /**
     * This message is broadcast by the network.server to all connected clients
     * to let them know of a valid move made by another player,
     * as well as the coordinates and the points the player gained.
     * Server will then send TILES to the player
     *
     * @param clientHandler The player who made the move
     * @param coordinates Coordinates of move that were chosen by the player
     * @return textual result, to be shown to the user
     */
    public void doMove(ClientHandler clientHandler, String coordinates);

    /**
     * This command is broadcast by the network.server to indicate which network.client passed their turn
     * so that the rest knows that a player is finished
     *
     * @param clientHandler The player who made the pass
     */
    public void doPass(ClientHandler clientHandler);

    /**
     * This command is sent by the network.server to the player that just passed
     * to confirm that the pass worked
     * and to assign the new tiles to the player.
     *
     * @param clientHandler The player who made the pass
     * @param tiles The new tiles for the player that passed. in the format: D O G
     * @return textual result, to be shown to the user
     */
    public void doPass(ClientHandler clientHandler, String tiles);

    /**
     * This message is broadcast by the network.server to let all clients know
     * that the game is over and to announce who the winner is
     */
    public void doGameOver();

}
