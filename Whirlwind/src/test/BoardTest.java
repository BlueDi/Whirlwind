package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import logic.Board;

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
	public void testSetPieceAbs() {
		//testar jogador 0
		assertEquals(board.checkFreePosition(0, 0), board.setPieceAbs(0, 0, 0));

		//testar colocar peça numa casa ocupada, ambos falham
		assertEquals(board.checkFreePosition(0, 1), board.setPieceAbs(0, 1, 0));

		//testar jogador 1
		assertEquals(board.checkFreePosition(0, 2), board.setPieceAbs(0, 2, 1));
	}

	@Test
	public void testCheckValidMove() {
		assertFalse(board.checkValidMove(-1, -1, 0));
		assertFalse(board.checkValidMove(0, 1, 0));
		assertFalse(board.checkValidMove(0, 1, 1));
		assertTrue(board.checkValidMove(1, 1, 0));
		assertFalse(board.checkValidMove(1, 1, 1));
	}

/*	@Test
	public void testWinner() throws Exception{   
		Board winboard = new Board();
		for(int i = 0; i < winboard.getBoard().length; i++)
			for(int j = 0; j < winboard.getBoard().length; j++)
			winboard.setPieceAbs(i, j, 0);
		assertTrue(winboard.winner(0));
	}
*/
}
