package test;

import exception.InvalidMoveException;
import exception.InvalidWordException;
import model.Game;
import model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

public class GameTest {

    Player p1 = new Player("Michael");
    Player p2 = new Player("Ronny");
    List<Player> players;
    Game newGame;
    Scanner scanner;

    @BeforeEach
    public void setUp() {
        players = new ArrayList<>();
        players.add(p1);
        players.add(p2);
        newGame = new Game(players);
    }

    @Test
    public void testNextPlayer() {
        if (newGame.getCurrentPlayer() == p1) {
            newGame.nextPlayer();
            assertEquals(newGame.getCurrentPlayer(),p2);
        } else if (newGame.getCurrentPlayer() == p2) {
            newGame.nextPlayer();
            assertEquals(newGame.getCurrentPlayer(),p1);
        }
    }

    @Test
    public void testMakeMove() {
        String input1 = "paste H B 1";
        scanner = new Scanner(input1);
        try {
            p1.makeMove(scanner);
            assertEquals(p1.getMove().getWord(),"paste");
            assertEquals(p1.getMove().getDirection(), 'H');
            assertEquals(p1.getMove().getPlaceCol(), 'B');
            assertEquals(p1.getMove().getPlaceRow(), 1);
        } catch (InvalidMoveException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testPlaceTileOnBoard() {
        String input1 = "paste H B 1";
        scanner = new Scanner(input1);
        try {
            p1.makeMove(scanner);
            newGame.placeTileOnBoard(p1.getMove());
            assertEquals(newGame.getBoard().getTileOnBoard('B',1),'P');
            assertEquals(newGame.getBoard().getTileOnBoard('C',1),'A');
            assertEquals(newGame.getBoard().getTileOnBoard('D',1),'S');
            assertEquals(newGame.getBoard().getTileOnBoard('E',1),'T');
            assertEquals(newGame.getBoard().getTileOnBoard('F',1),'E');
        } catch (InvalidMoveException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetAllWords() {
        String input1 = "horn H B 4";
        scanner = new Scanner(input1);
        try {
            p1.makeMove(scanner);
            List<String> wordsFormed = new ArrayList<>();
            wordsFormed.add("HORN");
            assertEquals(newGame.getAllWords(p1.getMove()), wordsFormed);
            newGame.placeTileOnBoard(p1.getMove());
        } catch (InvalidMoveException e) {
            e.printStackTrace();
        }

        String input2 = "farm V D 2";
        scanner = new Scanner(input2);
        try {
            p2.makeMove(scanner);
            List<String> wordsFormed = new ArrayList<>();
            wordsFormed.add("FARM");
            assertEquals(newGame.getAllWords(p2.getMove()), wordsFormed);
            newGame.placeTileOnBoard(p2.getMove());
        } catch (InvalidMoveException e) {
            e.printStackTrace();
        }

        String input3 = "paste h b 6";
        scanner = new Scanner(input3);
        try {
            p1.makeMove(scanner);
            List<String> wordsFormed = new ArrayList<>();
            wordsFormed.add("PASTE");
            wordsFormed.add("FARMS");
            assertEquals(newGame.getAllWords(p1.getMove()), wordsFormed);
            newGame.placeTileOnBoard(p1.getMove());
        } catch (InvalidMoveException e) {
            e.printStackTrace();
        }

        String input4 = "mob h D 5";
        scanner = new Scanner(input4);
        try {
            p2.makeMove(scanner);
            List<String> wordsFormed = new ArrayList<>();
            wordsFormed.add("MOB");
            wordsFormed.add("NOT");
            wordsFormed.add("BE");
            assertEquals(newGame.getAllWords(p2.getMove()), wordsFormed);
            newGame.placeTileOnBoard(p2.getMove());
        } catch (InvalidMoveException e) {
            e.printStackTrace();
        }

        String input5 = "BIT h a 7";
        scanner = new Scanner(input5);
        try {
            p1.makeMove(scanner);
            List<String> wordsFormed = new ArrayList<>();
            wordsFormed.add("BIT");
            wordsFormed.add("PI");
            wordsFormed.add("AT");
            assertEquals(newGame.getAllWords(p1.getMove()), wordsFormed);
            newGame.placeTileOnBoard(p1.getMove());
        } catch (InvalidMoveException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCheckWordsValid() {
        List<String> validWords = new ArrayList<>();
        validWords.add("these");
        validWords.add("Are");
        validWords.add("VALID");
        validWords.add("wOrDs");
        try {
            assertTrue(newGame.checkWordsValid(validWords));
        } catch (InvalidWordException e) {
            e.printStackTrace(); // it should not go here
        }

        List<String> invalidWords = new ArrayList<>();
        invalidWords.add("these");
        invalidWords.add("Are");
        invalidWords.add("INVALID");
        invalidWords.add("wOrDs");
        invalidWords.add("bsdhfbgsdkhiu");
        Exception exception = assertThrows(InvalidWordException.class, () -> {
            newGame.checkWordsValid(invalidWords);
        });
    }

    @Test
    public void testCalculateScore() {
        String input1 = "HORN H F 8";
        scanner = new Scanner(input1);
        try {
            p1.makeMove(scanner);
            assertEquals(newGame.calculateScore(p1.getMove()), 14);
            newGame.placeTileOnBoard(p1.getMove());
        } catch (InvalidMoveException e) {
            e.printStackTrace();
        }

        String input2 = "FARM V H 6";
        scanner = new Scanner(input2);
        try {
            p2.makeMove(scanner);
            assertEquals(newGame.calculateScore(p2.getMove()), 9);
            newGame.placeTileOnBoard(p2.getMove());
        } catch (InvalidMoveException e) {
            e.printStackTrace();
        }

        String input3 = "PASTE H F 10";
        scanner = new Scanner(input3);
        try {
            p1.makeMove(scanner);
            assertEquals(newGame.calculateScore(p1.getMove()), 25);
            newGame.placeTileOnBoard(p1.getMove());
        } catch (InvalidMoveException e) {
            e.printStackTrace();
        }

        String input4 = "MOB H H 9";
        scanner = new Scanner(input4);
        try {
            p2.makeMove(scanner);
            assertEquals(newGame.calculateScore(p2.getMove()), 15);
            newGame.placeTileOnBoard(p2.getMove());
        } catch (InvalidMoveException e) {
            e.printStackTrace();
        }

        String input5 = "BIT H E 11";
        scanner = new Scanner(input5);
        try {
            p1.makeMove(scanner);
            assertEquals(newGame.calculateScore(p1.getMove()), 16);
            newGame.placeTileOnBoard(p1.getMove());
        } catch (InvalidMoveException e) {
            e.printStackTrace();
        }

        String input6 = "BOARD V E 11";
        scanner = new Scanner(input6);
        try {
            p2.makeMove(scanner);
            assertEquals(newGame.calculateScore(p2.getMove()), 8);
            newGame.placeTileOnBoard(p2.getMove());
        } catch (InvalidMoveException e) {
            e.printStackTrace();
        }

        String input7 = "HEARD H A 15";
        scanner = new Scanner(input7);
        try {
            p1.makeMove(scanner);
            assertEquals(newGame.calculateScore(p1.getMove()), 30);
            newGame.placeTileOnBoard(p1.getMove());
        } catch (InvalidMoveException e) {
            e.printStackTrace();
        }
    }


}
