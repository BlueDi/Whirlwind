package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import logic.Board;
import logic.Piece;

public class BoardTest {
	@Test
	public void testBoard() throws Exception {
		int n = 14;
		Board board = new Board(n);

		assertEquals(board.getBoard().length, n);
	}

	@Test
	public void testFillWithPieces() throws Exception{
		int n = 14;
		Board board = new Board(n);

		for(int row = 0; row < n; row++){
			int firstpiece = -1;

			for(int col = 0; col < n; col++){
				if(firstpiece == -1 && board.getPiece(row, col).getPlayer() != -1){
					firstpiece = col;
				}
				else if(board.getPiece(row, col).getPlayer() != -1){
					assertTrue((col-firstpiece)==5);//verifica se o espaço entre peças é 5
					assertFalse(board.getPiece(row, firstpiece).getPlayer() == board.getPiece(row, col).getPlayer());//verifica se as peças são de jogadores diferentes
					firstpiece = col;
				}
			}
		}
	}

	@Test
	public void testCheckFreePosition() throws Exception {
		int n = 14;
		Board board = new Board(n);

		assertTrue(board.checkFreePosition(0, 0));
		assertFalse(board.checkFreePosition(0, 1));
		assertTrue(board.checkFreePosition(0, 2));
		assertTrue(board.checkFreePosition(1, 1));
	}

	@Test
	public void testSetPieceAbs() throws Exception {
		int n = 14;
		Board board = new Board(n);

		//testar jogador 0
		Piece p = new Piece(0, 0, 0);
		assertEquals(board.checkFreePosition(0, 0), board.setPieceAbs(p));

		//testar colocar peça numa casa ocupada, ambos falham
		p = new Piece(0, 1, 0);
		assertEquals(board.checkFreePosition(0, 1), board.setPieceAbs(p));

		//testar jogador 1
		p = new Piece(0, 2, 1);
		assertEquals(board.checkFreePosition(0, 2), board.setPieceAbs(p));
	}

	@Test
	public void testCheckValidMove() throws Exception {
		int n = 14;
		Board board = new Board(n);

		Piece p = new Piece(-1, -1, 0);
		assertFalse(board.checkValidMove(p));

		p = new Piece(0, 1, 0);
		assertFalse(board.checkValidMove(p));

		p = new Piece(0, 1, 1);
		assertFalse(board.checkValidMove(p));

		p = new Piece(1, 1, 0);
		assertTrue(board.checkValidMove(p));

		p = new Piece(1, 1, 1);
		assertFalse(board.checkValidMove(p));
	}

	@Test
	public void testWinner() throws Exception{   
		Board winBoardWhite = new Board();
		Board winBoardBlack = new Board();
		
		for(int i = 0; i < winBoardWhite.getSize(); i++)
			for(int j = 0; j < winBoardWhite.getSize(); j++){
				Piece pw = new Piece(i, j, 0);
				winBoardWhite.setPieceAbs(pw);
			}
		
		
		for(int i = 0; i < winBoardBlack.getSize(); i++)
			for(int j = 0; j < winBoardBlack.getSize(); j++){
				Piece pb = new Piece(i, j, 1);
				winBoardBlack.setPieceAbs(pb);
			}
		
		assertTrue(winBoardWhite.winnerWhite());
		assertTrue(winBoardBlack.winnerBlack());	
		
		assertFalse(winBoardWhite.winnerBlack());
		assertFalse(winBoardBlack.winnerWhite());
	}
}
