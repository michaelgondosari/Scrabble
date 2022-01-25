package controller;

import model.Game;
import model.InvalidMoveException;
import model.Player;
import view.LocalTUI;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PlayGame {

    public static void main(String[] args) {

        // Initiate variables
        Game newGame;
        List<Player> players = new ArrayList<>();
        LocalTUI tui = new LocalTUI();

        // Initiate game and players
        System.out.println("Welcome, let's play a game of Scrabble!\n");
        System.out.println("Please enter the name of Player 1: ");
        Scanner scanner = new Scanner(System.in);
        Player player1 = new Player(scanner.nextLine());
        players.add(player1);
        System.out.println("Please enter the name of Player 2: ");
        Player player2 = new Player(scanner.nextLine());
        players.add(player2);

        newGame = new Game(players);

        // Begin game
        boolean playAgain = true;
        while (playAgain) {

            // Print the initial board
            tui.printBoard(newGame.getBoard());

            while (!newGame.gameOver()) {
                Player currentPlayer = newGame.getCurrentPlayer();

                // Ask player for a command
                tui.askCommand(currentPlayer);
                switch (scanner.next().toUpperCase()) {
                    case "MOVE":
                        tui.askMove(currentPlayer);
                        try {
                            currentPlayer.makeMove();
                        } catch (InvalidMoveException e) {
                            System.err.println(e.getMessage());
                        }
                        // Check if the move is valid


                        // If valid, then do the move


                        // Next player's turn
                        newGame.nextPlayer();
                        tui.printBoard(newGame.getBoard());
                        break;

                    case "SWAP":
                        tui.askSwap(currentPlayer);
                        // Check if the move is valid
                        List<Character> tilesToSwap = new ArrayList<>();
                        do {
                            char swapTile = scanner.next().toUpperCase().charAt(0);
                            tilesToSwap.add(swapTile);
                        } while (scanner.hasNext());
                        try {
                            if (currentPlayer.checkSwapTilesInRack(tilesToSwap)) {
                                // If valid, then do the move
                                currentPlayer.removeTilesFromRack(tilesToSwap);
                                List<Character> swappedTiles = newGame.getTileBag().swapTiles(tilesToSwap);
                                currentPlayer.addTilesToRack(swappedTiles);
                                System.out.println("Your new rack: " + currentPlayer.getCurrentTiles());
                            }
                        } catch (InvalidMoveException e) {
                            System.err.println(e.getMessage()); // if swap is invalid
                        }

                        // Next player's turn
                        newGame.nextPlayer();
                        tui.printBoard(newGame.getBoard());
                        break;

                    case "SKIP":
                        tui.askSkip(currentPlayer);
                        // Next player's turn
                        newGame.nextPlayer();
                        tui.printBoard(newGame.getBoard());
                        break;

                    case "QUIT":
                        tui.gameOver(newGame);
                        System.out.println("Thank you for playing, see you next time!");
                        scanner.close();
                        System.exit(0);

                    case "SCORE":
                        // Print score and loop the switch again
                        tui.askScore(newGame);

                } // end switch for command

            } // end while not game over

            // Game Over
            tui.gameOver(newGame);
            System.out.println("Play again (y/n): ");
            char charPlayAgain = scanner.next().charAt(0);
            if (charPlayAgain == 'n') {
                playAgain = false;
            }

            // If players want to play again, reset the game
            newGame.reset();

        } // end while play again

        // If players don't want to play again, close the game
        System.out.println("Thank you for playing, see you next time!");
        scanner.close();
        System.exit(0);

    } // end of main

} // end of class
