package test;

import exception.InvalidMoveException;
import exception.InvalidWordException;
import game.Game;
import game.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GameTest {

    Player p1 = new Player("Michael");
    Player p2 = new Player("Ronny");
    List<Player> players;
    Game newGame;

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
        String[] input1 = {"paste", "H", "B", "1"};
        try {
            p1.makeMove(input1);
            assertEquals(p1.getMove().getWord(),"PASTE");
            assertEquals(p1.getMove().getDirection(), 'H');
            assertEquals(p1.getMove().getPlaceCol(), 'B');
            assertEquals(p1.getMove().getPlaceRow(), 1);
        } catch (InvalidMoveException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testPlaceTileOnBoard() {
        String[] input1 = {"paste", "H", "B", "1"};
        try {
            p1.makeMove(input1);
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
        String[] input1 = {"horn", "H", "B", "4"};
        try {
            p1.makeMove(input1);
            List<String> wordsFormed = new ArrayList<>();
            wordsFormed.add("HORN");
            assertEquals(newGame.getAllWords(p1.getMove()), wordsFormed);
            newGame.placeTileOnBoard(p1.getMove());
        } catch (InvalidMoveException e) {
            e.printStackTrace();
        }

        String[] input2 = {"farm", "V", "D", "2"};
        try {
            p2.makeMove(input2);
            List<String> wordsFormed = new ArrayList<>();
            wordsFormed.add("FARM");
            assertEquals(newGame.getAllWords(p2.getMove()), wordsFormed);
            newGame.placeTileOnBoard(p2.getMove());
        } catch (InvalidMoveException e) {
            e.printStackTrace();
        }

        String[] input3 = {"paste", "h", "b", "6"};
        try {
            p1.makeMove(input3);
            List<String> wordsFormed = new ArrayList<>();
            wordsFormed.add("PASTE");
            wordsFormed.add("FARMS");
            assertEquals(newGame.getAllWords(p1.getMove()), wordsFormed);
            newGame.placeTileOnBoard(p1.getMove());
        } catch (InvalidMoveException e) {
            e.printStackTrace();
        }

        String[] input4 = {"mob", "h", "D", "5"};
        try {
            p2.makeMove(input4);
            List<String> wordsFormed = new ArrayList<>();
            wordsFormed.add("MOB");
            wordsFormed.add("NOT");
            wordsFormed.add("BE");
            assertEquals(newGame.getAllWords(p2.getMove()), wordsFormed);
            newGame.placeTileOnBoard(p2.getMove());
        } catch (InvalidMoveException e) {
            e.printStackTrace();
        }

        String[] input5 = {"BIT", "h", "a", "7"};
        try {
            p1.makeMove(input5);
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
            e.printStackTrace();
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
        String[] input1 = {"HORN", "H", "F", "8"};
        try {
            p1.makeMove(input1);
            assertEquals(newGame.calculateScore(p1.getMove()), 14);
            newGame.placeTileOnBoard(p1.getMove());
        } catch (InvalidMoveException e) {
            e.printStackTrace();
        }

        String[] input2 = {"FARM", "V", "H", "6"};
        try {
            p2.makeMove(input2);
            assertEquals(newGame.calculateScore(p2.getMove()), 9);
            newGame.placeTileOnBoard(p2.getMove());
        } catch (InvalidMoveException e) {
            e.printStackTrace();
        }

        String[] input3 = {"PASTE", "H", "F", "10"};
        try {
            p1.makeMove(input3);
            assertEquals(newGame.calculateScore(p1.getMove()), 25);
            newGame.placeTileOnBoard(p1.getMove());
        } catch (InvalidMoveException e) {
            e.printStackTrace();
        }

        String[] input4 = {"MOB", "H", "H", "9"};
        try {
            p2.makeMove(input4);
            assertEquals(newGame.calculateScore(p2.getMove()), 15);
            newGame.placeTileOnBoard(p2.getMove());
        } catch (InvalidMoveException e) {
            e.printStackTrace();
        }

        String[] input5 = {"BIT", "H", "E", "11"};
        try {
            p1.makeMove(input5);
            assertEquals(newGame.calculateScore(p1.getMove()), 16);
            newGame.placeTileOnBoard(p1.getMove());
        } catch (InvalidMoveException e) {
            e.printStackTrace();
        }

        String[] input6 = {"BOARD", "V", "E", "11"};
        try {
            p2.makeMove(input6);
            assertEquals(newGame.calculateScore(p2.getMove()), 8);
            newGame.placeTileOnBoard(p2.getMove());
        } catch (InvalidMoveException e) {
            e.printStackTrace();
        }

        String[] input7 = {"HEARD", "H", "A", "15"};
        try {
            p1.makeMove(input7);
            assertEquals(newGame.calculateScore(p1.getMove()), 30);
            newGame.placeTileOnBoard(p1.getMove());
        } catch (InvalidMoveException e) {
            e.printStackTrace();
        }

        String[] input8 = {"PLAYABLE", "H", "C", "13"};
        try {
            p2.makeMove(input8);
            assertEquals(newGame.calculateScore(p2.getMove()), 84);
            newGame.placeTileOnBoard(p2.getMove());
        } catch (InvalidMoveException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testRemoveMinus() {
        String[] blankTile1 = {"comp-UTER", "H", "H", "8"};
        try {
            p1.makeMove(blankTile1);
            String newWord = newGame.removeMinus(p1.getMove());
            assertEquals(newWord,"COMPUTER");
        } catch (InvalidMoveException e) {
            e.printStackTrace();
        }

        String[] blankTile2 = {"-COMPUTER", "H", "H", "8"};
        try {
            p1.makeMove(blankTile2);
            String newWord = newGame.removeMinus(p1.getMove());
            assertEquals(newWord,"COMPUTER");
        } catch (InvalidMoveException e) {
            e.printStackTrace();
        }

        String[] blankTile3 = {"COMPUTE-r", "H", "H", "8"};
        try {
            p1.makeMove(blankTile3);
            String newWord = newGame.removeMinus(p1.getMove());
            assertEquals(newWord,"COMPUTER");
        } catch (InvalidMoveException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testRemoveCharAfterMinus() {
        String[] blankTile1 = {"COMP-uter", "H", "H", "8"};
        try {
            p1.makeMove(blankTile1);
            String newWord = newGame.removeCharAfterMinus(p1.getMove());
            assertEquals(newWord,"COMP-TER");
        } catch (InvalidMoveException e) {
            e.printStackTrace();
        }

        String[] blankTile2 = {"-comp-UTER", "H", "H", "8"};
        try {
            p1.makeMove(blankTile2);
            String newWord = newGame.removeCharAfterMinus(p1.getMove());
            assertEquals(newWord,"-OMP-TER");
        } catch (InvalidMoveException e) {
            e.printStackTrace();
        }

        String[] blankTile3 = {"-COMP-ute-R", "H", "H", "8"};
        try {
            p1.makeMove(blankTile3);
            Exception exception = assertThrows(InvalidMoveException.class, () -> {
                newGame.removeCharAfterMinus(p1.getMove());
            });
        } catch (InvalidMoveException e) {
            e.printStackTrace();
        }
    }

} // end of class
