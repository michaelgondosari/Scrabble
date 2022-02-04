package test;

import exception.InvalidMoveException;
import game.Player;
import game.TileBag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {

    Player p1;
    Player p2;

    @BeforeEach
    public void setUp() {
        p1 = new Player("Michael");
        p2 = new Player("Ronny");
    }

    @Test
    public void testInit() {
        assertEquals(p1.getName(),"Michael");
        assertEquals(p1.getScore(),0);
        assertEquals(p2.getName(),"Ronny");
        assertEquals(p2.getScore(),0);
    }

    @Test
    public void testRack() {
        TileBag tilebag = new TileBag(System.getProperty("user.dir") + "/src/letters.txt");
        p1.addTilesToRack(tilebag.drawTiles(7));
        p2.addTilesToRack(tilebag.drawTiles(10));
        assertEquals(p1.getRackSize(),7);
        assertEquals(p2.getRackSize(),10);

        List<Character> toRemove = p1.rackCopy();
        p1.removeTilesFromRack(toRemove);
        assertEquals(p1.getRackSize(),0);

        List<Character> rackCopy = p2.rackCopy();
        p2.clearRack();
        assertEquals(p2.getRackSize(),0);
        assertEquals(rackCopy.size(),10);
    }

    @Test
    public void testScore() {
        p1.addScore(10);
        assertEquals(p1.getScore(),10);
        p2.addScore(25);
        assertEquals(p2.getScore(),25);
    }

    @Test
    public void testMakeMove() throws InvalidMoveException {
        String[] p1Move = {"Scrabble", "V", "H", "8"};
        String[] p2Move = {"Wrong", "O", "7"};

        p1.makeMove(p1Move);
        assertEquals("SCRABBLE", p1.getMove().getWord());
        assertEquals('V', p1.getMove().getDirection());
        assertEquals('H', p1.getMove().getPlaceCol());
        assertEquals(8, p1.getMove().getPlaceRow());

        try {
            p2.makeMove(p2Move);
            Exception exception = assertThrows(InvalidMoveException.class, () -> {
                p2.makeMove(p2Move);
            });
        } catch (InvalidMoveException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void testSwapTilesInRack() throws InvalidMoveException {
        List<Character> tiles = new ArrayList<>();
        tiles.add('L');
        tiles.add('O');
        tiles.add('V');
        tiles.add('E');
        p1.addTilesToRack(tiles);

        List<Character> swapTrue = new ArrayList<>();
        swapTrue.add('V');
        swapTrue.add('O');

        List<Character> swapFalse = new ArrayList<>();
        swapFalse.add('L');
        swapFalse.add('X');

        assertTrue(p1.checkSwapTilesInRack(swapTrue));

        try {
            p1.checkSwapTilesInRack(swapFalse);
            Exception exception = assertThrows(InvalidMoveException.class, () -> {
                p1.checkSwapTilesInRack(swapFalse);
            });
        } catch (InvalidMoveException e) {
            System.out.println(e.getMessage());
        }
    }

}
