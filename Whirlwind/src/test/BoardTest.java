package test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import logic.Board;
import logic.Piece;

public class BoardTest {
	int n = 14;    
	Board board;

	@Before public void initialize() throws Exception {
		board = new Board(n);
	}

	@Test
	public void testBoard() {
		assertEquals(board.getBoard().length, n);
	}

	@Test
	public void testBoardInt() {
		testBoard();
		testFillWithPieces();
	}

	@Test
	public void testFillWithPieces(){
		Piece[][] testing_board = board.getBoard();
		for(int row = 0; row < n; row++){
			int firstpiece = -1;

			for(int col = 0; col < n; col++){
				if(firstpiece == -1 && board.getPiece(row, col).getPlayer() != -1){
					firstpiece = col;
				}
				else if(board.getPiece(row, col).getPlayer() != -1){
					assertTrue((col-firstpiece)==5);
					assertFalse(board.getPiece(row, firstpiece).getPlayer() == board.getPiece(row, col).getPlayer());
					firstpiece = col;
				}
			}
		}
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
