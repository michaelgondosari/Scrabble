package test;

import game.Player;
import game.TileBag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

}
