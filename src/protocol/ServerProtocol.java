package protocol;

import server.ClientHandler;

public interface ServerProtocol {

    /**
     * Returns a String to be sent as a response to a Client HELLO request,
     * including the name of the hotel:
     * ProtocolMessages.HELLO:;:[NAMES]:;:FEATURES;
     *
     * @param name The list of names already in the server including your own
     * @param features The extra optional features supported by this client
     *
     * @return textual result, to be shown to the user
     */
    public String getHello(String name, String features);

    /**
     * This message is broadcasted to every client connected in the room,
     * so that they are aware of the new connection,
     * as well as what features they support:
     * ProtocolMessages.WELCOME:;:NAME:;:FEATURES;
     *
     * @param name The name of the player who just joined
     * @param features The extra optional features supported by this client
     */
    public void doWelcome(String name, String features);

    /**
     * The appropriate error code is sent by the server when something has gone wrong
     * @param errorCode the appropriate error code sent by the server
     * @return textual result, to be shown to the user
     */
    public String doError(String errorCode);

    /**
     * This is sent by the server to all clients connected to indicate that the server is ready to start.
     * The first of these commands will be sent without arguments by the server to indicate readiness.
     * @return textual result, to be shown to the user
     */
    public String doServerReady();

    /**
     * This is sent by the server to all clients connected to indicate that the server is ready to start,
     * and so are the clients names in the arguments.
     *
     * @param clientHandler The name of the client that is ready to start
     * @return textual result, to be shown to the user
     */
    public void doServerReady(ClientHandler clientHandler);

    /**
     * This is sent by the server as the first command after starting a game.
     * All the names of the players participating in the game will be given as arguments.
     * It will be followed by the TILES and TURN commands from the server
     *
     * @return textual result, to be shown to the user
     */
    public String doStart();

    /**
     * This is broadcast by the server to all clients to indicate that another player has just disconnected.
     * This player will pass every turn he/she gets automatically
     *
     * @param name The name of the disconnected client
     * @return textual result, to be shown to the user
     */
    public String doAbort(String name);

    /**
     * This is sent by the server when a player is drawing new tiles.
     * This happens after every move, then all the tiles are sent back (new & old)
     * Each player connected will get different randomized tiles.
     *
     * @param tiles The tiles assigned to the receiver of this message. The tiles will be displayed in the form:
     * A X C...
     * Using a space [ ] as separator, and a [-] for a blank cell
     *
     * @return textual result, to be shown to the user
     */
    public String doTiles(String tiles);

    /**
     * Used to inform every client connected who has to make a move
     * @param name The name of the player whose current turn it is
     * @return textual result, to be shown to the user
     */
    public String doTurn(String name);

    /**
     * This message is broadcast by the server to all connected clients
     * to let them know of a valid move made by another player,
     * as well as the coordinates and the points the player gained.
     * Server will then send TILES to the player
     *
     * @param name The name of the player who made the move
     * @param coordinates Coordinates that were chosen by the player
     * @param gainedPoints The points gained by the player following that move
     * @return textual result, to be shown to the user
     */
    public String doMove(String name, String coordinates, int gainedPoints);

    /**
     * This command is broadcast by the server to indicate which client passed their turn
     * so that the rest knows that a player is finished
     *
     * @param name The name of the player who made the pass
     * @return textual result, to be shown to the user
     */
    public String doPass(String name);

    /**
     * This command is sent by the server to the player that just passed
     * to confirm that the pass worked
     * and to assign the new tiles to the player.
     *
     * @param name The name of the player who made the pass
     * @param tiles The new tiles for the player that passed. in the format: D O G
     * @return textual result, to be shown to the user
     */
    public String doPass(String name, String tiles);

    /**
     * This message is broadcast by the server to let all clients know
     * that the game is over and to announce who the winner is
     *
     * @param name The name of the player who won the game.
     * @return textual result, to be shown to the user
     */
    public String doGameOver(String name);


}
