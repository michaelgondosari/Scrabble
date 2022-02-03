package protocol;

import exception.ProtocolException;
import exception.ServerUnavailableException;

public interface ClientProtocol {

    /**
     * Handles the following server-client handshake:
     * 1. Client sends ProtocolMessages.HELLO:;:NAME:;:FEATURES to server
     * 2. Server returns one line containing ProtocolMessages.HELLO:;:[NAMES]:;:FEATURES to client
     *
     * This method sends the HELLO and checks whether the server response is valid
     * (must contain HELLO, list of names already in the server, and features)
     * If the response is not valid, this method throws a ProtocolException.
     * If the response is valid, a welcome message from server is forwarded to the view.
     *
     * @param name name of client
     * @param features features supported by client
     *
     * @throws ServerUnavailableException if IO errors occur.
     * @throws ProtocolException          if the server response is invalid.
     */
    public void handleHello(String name, String features) throws ServerUnavailableException, ProtocolException;

    /**
     * This is sent by each client to indicate that they are ready.
     * Once the server receives this, a new SERVERREADY is issued with the name of said client
     *
     * @param name name of client
     * @throws ServerUnavailableException if IO errors occur.
     */
    public void doClientReady(String name) throws ServerUnavailableException, ProtocolException;

    /**
     * This may be sent by the client to indicate that they are disconnecting from the server.
     *
     * @param name name of client
     * @throws ServerUnavailableException if IO errors occur.
     */
    public void doAbort(String name) throws ServerUnavailableException, ProtocolException;

    /**
     * This is sent by the client to indicate a move on the board.
     * The move will then have to be validated by the server, and a specific error code may be received.
     *
     * @param name name of client
     * @param coordinates Coordinates in the form: <cell><coordinate(0-224)>
     * Example: D1 O2 G3
     * Using a space [ ] as separator, and a [-?] for a blank cell,
     * where ? is the letter that the blank cell is replacing
     *
     * @throws ServerUnavailableException if IO errors occur.
     */
    public void doMove(String name, String coordinates) throws ServerUnavailableException;

    /**
     * This command is sent by the client to indicate they would like to pass their turn,
     * and if they do not want to swap any tiles
     * @throws ServerUnavailableException if IO errors occur.
     */
    public void doPass() throws ServerUnavailableException;

    /**
     * This command is sent by the client to indicate they would like to pass their turn,
     * and if they want to swap any tiles
     * @param tiles The tiles that the client would like to swap in the format: D O G
     * @throws ServerUnavailableException if IO errors occur.
     */
    public void doPass(String tiles) throws ServerUnavailableException;

}
