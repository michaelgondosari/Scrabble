package test;

import game.TileBag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TileBagTest {

    TileBag tilebag;

    @BeforeEach
    public void setUp() {
        tilebag = new TileBag(System.getProperty("user.dir") + "/src/letters.txt");
    }

    @Test
    public void testGetLetterAmountLeft() {
        assertEquals(tilebag.getLetterAmountLeft('A'),9);
        assertEquals(tilebag.getLetterAmountLeft('E'),12);
        assertEquals(tilebag.getLetterAmountLeft('-'),2);
    }

    @Test
    public void testGetLetterValue() {
        assertEquals(tilebag.getLetterValue('A'),1);
        assertEquals(tilebag.getLetterValue('Q'),10);
        assertEquals(tilebag.getLetterValue('-'),0);
    }

    @Test
    public void testDrawTiles() {
        assertEquals(tilebag.getTilesLeft(),100);
        List<Character> drawnTiles = tilebag.drawTiles(7);
        assertEquals(tilebag.getTilesLeft(),93);
        assertEquals(drawnTiles.size(),7);
    }

    @Test
    public void testSwapTiles() {
        List<Character> oldTiles = tilebag.drawTiles(5);
        assertEquals(tilebag.getTilesLeft(),95);
        List<Character> newTiles = tilebag.swapTiles(oldTiles);
        assertEquals(tilebag.getTilesLeft(),95);
        assertTrue(!newTiles.containsAll(oldTiles));
    }

}
