package controller;

import exception.InvalidMoveException;
import exception.InvalidWordException;
import model.Game;
import model.Player;
import view.LocalTUI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class PlayGame {

    public static void main(String[] args) {

        System.out.println(System.getProperty("user.dir"));

        // Initiate variables
        Game newGame;
        List<Player> players = new ArrayList<>();
        LocalTUI tui = new LocalTUI();
        Scanner scanner1 = new Scanner(System.in);;

        // Initiate game and players
        System.out.println("Welcome, let's play a game of Scrabble!\n");
        System.out.println("Please enter the name of Player 1: ");
        Player player1 = new Player(scanner1.nextLine());
        players.add(player1);
        System.out.println("Please enter the name of Player 2: ");
        Player player2 = new Player(scanner1.nextLine());
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
                boolean loopSwitchAgain = true;
                while (loopSwitchAgain) {
                    tui.askCommand(currentPlayer);

                    if (scanner1.hasNextLine()) {
                        String command = scanner1.nextLine().toUpperCase();
                        switch (command) {

                            case "MOVE":
                                tui.askMove(currentPlayer);

                                String[] inputMove = scanner1.nextLine().split(" ");
                                System.out.println(Arrays.toString(inputMove));

                                try {
                                    currentPlayer.makeMove(inputMove);
                                } catch (InvalidMoveException | NumberFormatException e) {
                                    System.err.println(e.getMessage());
                                }

                                // Check if the move is valid
                                try {
                                    if ( newGame.checkMoveOverwrite(currentPlayer.getMove())
                                            && newGame.checkMoveInsideBoard(currentPlayer.getMove())
                                            && newGame.checkUsingAvailableTiles(currentPlayer.getMove()) ) {
                                        // If a blank tile is played, change it to another letter
                                        if (currentPlayer.getMove().getWord().contains("?")) {
                                            System.out.println("Please input the letter for '?' : ");
                                            char blankTile = scanner1.nextLine().toUpperCase().charAt(0);
                                            currentPlayer.getMove().getWord().replace('?', blankTile);
                                        }
                                        // Check if the word is valid
                                        if (newGame.checkWordsValid(newGame.getAllWords(currentPlayer.getMove()))) {
                                            // If valid, then do the move :
                                            // 1. remove tiles from player's rack
                                            currentPlayer.removeTilesFromRack(newGame.tilesToRemove(currentPlayer.getMove()));
                                            // 2. place tiles on board
                                            newGame.placeTileOnBoard(currentPlayer.getMove());
                                            // 3. calculate points and update the player's points
                                            currentPlayer.addScore(newGame.calculateScore(currentPlayer.getMove()));
                                            // 4. players draw new tiles
                                            int tileUsed = newGame.tilesToRemove(currentPlayer.getMove()).size();
                                            currentPlayer.addTilesToRack(newGame.getTileBag().drawTiles(tileUsed));
                                            // 5.print player's name, score, and current rack
                                            System.out.println(String.format("Player %s, points : %d", currentPlayer.getName(), currentPlayer.getScore()));
                                            System.out.println("New rack : " + currentPlayer.getCurrentTiles());
                                        }
                                    }
                                } catch (InvalidMoveException | InvalidWordException e) {
                                    System.err.println(e.getMessage());
                                }
                                loopSwitchAgain = false;
                                break;

                            case "SWAP":
                                tui.askSwap(currentPlayer);
                                // Check if the move is valid
                                List<Character> tilesToSwap = new ArrayList<>();
                                String[] swapTile = scanner1.nextLine().split(",");
                                for (String s : swapTile) {
                                    tilesToSwap.add(s.toUpperCase().charAt(0));
                                }
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
                                loopSwitchAgain = false;
                                break;

                            case "SKIP":
                                tui.askSkip(currentPlayer);
                                loopSwitchAgain = false;
                                break;

                            case "SCORE":
                                // Print score and loop the switch again
                                tui.askScore(newGame);
                                break;

                            case "QUIT":
                                tui.gameOver(newGame);
                                System.out.println("Thank you for playing, see you next time!");
                                scanner1.close();
                                System.exit(0);

                            default:
                                System.out.println("Unknown command!");
                                break;

                        } // end switch for command
                    } // end of scanner has next line


                } // end while loop for switch

                // Next player's turn
                newGame.nextPlayer();
                tui.printBoard(newGame.getBoard());

            } // end while not game over

            // Game Over
            tui.gameOver(newGame);
            System.out.println("Play again (y/n): ");
            char charPlayAgain = scanner1.next().charAt(0);
            if (charPlayAgain == 'n') {
                playAgain = false;
            }

            // If players want to play again, reset the game
            newGame.reset();

        } // end while play again

        // If players don't want to play again, close the game
        System.out.println("Thank you for playing, see you next time!");
        scanner1.close();
        System.exit(0);

    } // end of main

} // end of class
