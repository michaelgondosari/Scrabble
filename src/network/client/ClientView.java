package network.client;

import exception.ExitProgram;
import exception.InvalidMoveException;
import exception.ServerUnavailableException;

public interface ClientView {

    /**
     * Asks for user input continuously and handles communication accordingly using
     * the {@link #handleUserInput(String input)} method.
     *
     * If an ExitProgram exception is thrown, stop asking for input, send an exit
     * message to the network.server according to the network.protocol and close the connection.
     *
     * @throws ServerUnavailableException in case of IO exceptions.
     */
    public void start() throws ServerUnavailableException;

    /**
     * Split the user input on a space [ ] and handle it accordingly.
     * - If the input is valid, take the corresponding action (for example,
     *   when "PASS" is called, send a request to pass)
     * - If the input is invalid, show a message to the user and print the help menu.
     *
     * @param input The user input.
     * @throws ExitProgram               	When the user has indicated to exit the
     *                                    	program.
     * @throws ServerUnavailableException 	if an IO error occurs in taking the
     *                                    	corresponding actions.
     * @throws InvalidMoveException         if the move is invalid
     */
    public void handleUserInput(String input) throws ExitProgram, ServerUnavailableException, InvalidMoveException;

    /**
     * Writes the given message to standard output.
     * @param message the message to write to the standard output.
     */
    public void showMessage(String message);

    /**
     * Prints the question and asks the user to input a String.
     * @param question The question to show to the user
     * @return The user input as a String
     */
    public String getString(String question);

    /**
     * Prints the question and asks the user to input an Integer.
     * @param question The question to show to the user
     * @return The written Integer.
     */
    public int getInt(String question);

    /**
     * Prints the question and asks the user for a yes/no answer.
     * @param question The question to show to the user
     * @return The user input as boolean.
     */
    public boolean getBoolean(String question);

}
