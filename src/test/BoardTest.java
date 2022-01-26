package test;

import model.Board;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BoardTest {

    Board board;

    @BeforeEach
    public void setUp() {
        board = new Board();
    }

    @Test
    public void testSetGetTileOnBoard() {
        board.setTileOnBoard(0,0,'D');
        board.setTileOnBoard(0,1,'O');
        board.setTileOnBoard(0,2,'G');
        assertEquals(board.getTileOnBoard(0,0),'D');
        assertEquals(board.getTileOnBoard(0,1),'O');
        assertEquals(board.getTileOnBoard(0,2),'G');
    }

    @Test
    public void testInitBoard() {
        assertEquals(board.getTileOnBoard(0,0),' ');
        assertEquals(board.getTileOnBoard(14,14),' ');
        assertEquals(board.getBoardScores().get(board.getCoordinate(7,7)),"2W");
        assertEquals(board.getBoardScores().get(board.getCoordinate(1,5)),"3L");
        assertEquals(board.getBoardScores().get(board.getCoordinate(14,0)),"3W");
    }

    @Test
    public void testGetCoordinate() {
        assertEquals(board.getCoordinate(0,0), "A1");
        assertEquals(board.getCoordinate(7,7), "H8");
        assertEquals(board.getCoordinate(2,3), "D3");
    }

    @Test
    public void testConvertColRow() {
        assertEquals(board.convertCol('A'),0);
        assertEquals(board.convertCol('O'),14);
        assertEquals(board.convertCol('C'),2);
        assertEquals(board.convertRow(1),0);
        assertEquals(board.convertRow(15),14);
        assertEquals(board.convertRow(3),2);
    }

    @Test
    public void testGetBoardMultiplier() {
        assertEquals(board.getBoardMultiplier("O1"),"3W");
        assertEquals(board.getBoardMultiplier("M7"),"2L");
        assertEquals(board.getBoardMultiplier("D2"),"1");
    }

}
