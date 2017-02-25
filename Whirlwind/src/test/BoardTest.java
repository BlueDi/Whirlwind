package test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import logic.Board;

public class BoardTest {
	int n = 16;    
	Board board;
	
    @Before public void initialize() throws Exception {
        board = new Board(n);
     }
	
	@Test
	public void testBoard() {
		int sum = 0;
		assertEquals(board.getColSize(), n);
		assertEquals(board.getRowSize(), n);
	}

	@Test
	public void testCheckFreePosition() {
		assertTrue(board.checkFreePosition(0, 0));
		assertFalse(board.checkFreePosition(0, 1));
		assertTrue(board.checkFreePosition(0, 2));
		assertTrue(board.checkFreePosition(1, 1));
	}

	@Test
	public void testSetPiece() {
		//testar jogador 0
		assertEquals(board.checkFreePosition(0, 0), board.setPiece(0, 0, 0));
		
		//testar colocar peça numa casa ocupada, ambos falham
		assertEquals(board.checkFreePosition(0, 1), board.setPiece(0, 1, 0));
		
		//testar jogador 1
		assertEquals(board.checkFreePosition(0, 2), board.setPiece(0, 2, 1));
	}

}
