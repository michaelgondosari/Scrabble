package controller;

import exception.InvalidMoveException;
import exception.InvalidWordException;
import model.Game;
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
        Scanner scanner = new Scanner(System.in);

        // Initiate game and players
        System.out.println("Welcome, let's play a game of Scrabble!\n");
        System.out.println("Please enter the name of Player 1: ");
        System.out.print("> ");
        Player player1 = new Player(scanner.nextLine());
        players.add(player1);
        System.out.println("Please enter the name of Player 2: ");
        System.out.print("> ");
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
                boolean loopSwitchAgain = true;
                while (loopSwitchAgain) {
                    tui.askCommand(currentPlayer);

                    if (scanner.hasNextLine()) {
                        String command = scanner.nextLine().toUpperCase();
                        switch (command) {

                            case "MOVE":
                                tui.askMove(currentPlayer);

                                String[] inputMove = scanner.nextLine().split(" ");

                                try {
                                    currentPlayer.makeMove(inputMove);
                                } catch (InvalidMoveException e) {
                                    System.err.println(e.getMessage());
                                    break;
                                } catch (NumberFormatException nfe) {
                                    System.err.println("Please enter a valid number for the row!");
                                    break;
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
                                                tui.updateAfterMove(newGame, moveScore);
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
                                                tui.updateAfterMove(newGame, moveScore);
                                            }
                                        }
                                    }
                                } // end of try

                                // If the move or word is invalid
                                catch (InvalidMoveException | InvalidWordException e) {
                                    System.err.println(e.getMessage());
                                    break;
                                }

                                loopSwitchAgain = false; // go to next player
                                break;

                            case "SWAP":
                                tui.askSwap(currentPlayer);
                                // Check if the move is valid
                                List<Character> tilesToSwap = new ArrayList<>();
                                String[] swapTile = scanner.nextLine().split(" ");
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
                                    break;
                                }
                                loopSwitchAgain = false; // go to next player
                                break;

                            case "SKIP":
                                tui.askSkip(currentPlayer);
                                loopSwitchAgain = false; // go to next player
                                break;

                            case "SCORE":
                                // Print score and loop the switch again
                                tui.askScore(newGame);
                                break;

                            case "QUIT":
                                tui.gameOver(newGame);
                                System.out.println("Thank you for playing, see you next time!");
                                scanner.close();
                                System.exit(0);

                            default:
                                System.err.println("Unknown command!");
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
