package logic;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {
    @Test
    void testBoard() throws Exception {
        int n = 14;
        Board board = new Board(n, 0);

        assertEquals(board.getBoard().length, n);
    }

    @Test
    void testSmallBoard() {
        try {
            new Board(5, 0);
        } catch (IndexOutOfBoundsException e) {
            assertEquals("Board demasiado pequeno!", e.getMessage());
        }
    }

    @Test
    void testBigBoard() {
        try {
            new Board(500, 0);
        } catch (IndexOutOfBoundsException e) {
            assertEquals("Board demasiado grande!", e.getMessage());
        }
    }

    @Test
    void testFillWithPieces() throws Exception {
        int n = 14;
        Board board = new Board(n, 0);

        for (int row = 0; row < n; row++) {
            int firstpiece = -1;

            for (int col = 0; col < n; col++)
                if (firstpiece == -1 && board.getPiece(row, col).getPlayer() != -1) {
                    firstpiece = col;
                } else if (board.getPiece(row, col).getPlayer() != -1) {
                    assertTrue((col - firstpiece) == 5);
                    assertFalse(board.getPiece(row, firstpiece).getPlayer() == board.getPiece(row, col).getPlayer());
                    firstpiece = col;
                }
        }
    }

    @Test
    void testCheckFreePosition() throws Exception {
        int n = 14;
        Board board = new Board(n, 0);

        assertTrue(board.checkFreePosition(0, 0));
        assertFalse(board.checkFreePosition(0, 1));
        assertTrue(board.checkFreePosition(0, 2));
        assertTrue(board.checkFreePosition(1, 1));
    }

    @Test
    void testSetPieceAbs() throws Exception {
        int n = 14;
        Board board = new Board(n, 0);

        // testar jogador 0
        Piece p = new Piece(0, 0, 0);
        assertEquals(board.checkFreePosition(0, 0), board.setPieceAbs(p));

        // testar colocar peça numa casa ocupada, ambos falham
        p = new Piece(0, 1, 0);
        assertEquals(board.checkFreePosition(0, 1), board.setPieceAbs(p));

        // testar jogador 1
        p = new Piece(0, 2, 1);
        assertEquals(board.checkFreePosition(0, 2), board.setPieceAbs(p));
    }

    @Test
    void testCheckValidMove() {
        int n = 14;
        Board board = new Board(n, 0);

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
    void testWinnerSimple() throws Exception {
        Board winBoardWhite = new Board();
        Board winBoardBlack = new Board();

        for (int i = 0; i < winBoardWhite.getSize(); i++)
            winBoardWhite.setPieceAbs(new Piece(2, i, 0));

        for (int i = 0; i < winBoardBlack.getSize(); i++)
            winBoardBlack.setPieceAbs(new Piece(i, 2, 1));

        assertTrue(winBoardWhite.winnerWhite());
        assertFalse(winBoardWhite.winnerBlack());

        assertTrue(winBoardBlack.winnerBlack());
        assertFalse(winBoardBlack.winnerWhite());
    }

    @Test
    void testWinnerComplexWhite() throws Exception {
        Board winBoardWhite = new Board();

        winBoardWhite.setPieceAbs(new Piece(2, 0, 0));
        winBoardWhite.setPieceAbs(new Piece(2, 1, 0));
        winBoardWhite.setPieceAbs(new Piece(3, 1, 0));
        winBoardWhite.setPieceAbs(new Piece(4, 1, 0));
        winBoardWhite.setPieceAbs(new Piece(5, 1, 0));
        winBoardWhite.setPieceAbs(new Piece(5, 2, 0));
        winBoardWhite.setPieceAbs(new Piece(5, 3, 0));
        winBoardWhite.setPieceAbs(new Piece(6, 3, 0));
        winBoardWhite.setPieceAbs(new Piece(7, 3, 0));
        winBoardWhite.setPieceAbs(new Piece(8, 3, 0));
        winBoardWhite.setPieceAbs(new Piece(8, 2, 0));
        winBoardWhite.setPieceAbs(new Piece(8, 1, 0));
        winBoardWhite.setPieceAbs(new Piece(9, 1, 0));
        winBoardWhite.setPieceAbs(new Piece(10, 1, 0));
        winBoardWhite.setPieceAbs(new Piece(10, 2, 0));
        winBoardWhite.setPieceAbs(new Piece(10, 3, 0));
        winBoardWhite.setPieceAbs(new Piece(10, 4, 0));
        winBoardWhite.setPieceAbs(new Piece(10, 5, 0));
        winBoardWhite.setPieceAbs(new Piece(10, 6, 0));
        winBoardWhite.setPieceAbs(new Piece(9, 6, 0));
        winBoardWhite.setPieceAbs(new Piece(8, 6, 0));
        winBoardWhite.setPieceAbs(new Piece(8, 7, 0));
        winBoardWhite.setPieceAbs(new Piece(7, 7, 0));
        winBoardWhite.setPieceAbs(new Piece(7, 8, 0));
        winBoardWhite.setPieceAbs(new Piece(6, 8, 0));
        winBoardWhite.setPieceAbs(new Piece(6, 9, 0));
        winBoardWhite.setPieceAbs(new Piece(5, 9, 0));
        winBoardWhite.setPieceAbs(new Piece(5, 10, 0));
        winBoardWhite.setPieceAbs(new Piece(4, 10, 0));
        winBoardWhite.setPieceAbs(new Piece(2, 10, 0));
        winBoardWhite.setPieceAbs(new Piece(2, 8, 0));
        winBoardWhite.setPieceAbs(new Piece(10, 7, 0));

        for (int i = winBoardWhite.getSize() / 2; i < winBoardWhite.getSize(); i++)
            winBoardWhite.setPieceAbs(new Piece(3, i, 0));

        assertTrue(winBoardWhite.winnerWhite());
    }

    @Test
    void testWinnerComplexBlack() throws Exception {
        Board winBoardBlack = new Board();

        for (int i = 0; i < winBoardBlack.getSize() / 2 + 1; i++)
            winBoardBlack.setPieceAbs(new Piece(i, 2, 1));

        winBoardBlack.setPieceAbs(new Piece(winBoardBlack.getSize() / 2, 3, 1));

        for (int i = winBoardBlack.getSize() / 2; i < winBoardBlack.getSize(); i++)
            winBoardBlack.setPieceAbs(new Piece(i, 4, 1));

        assertTrue(winBoardBlack.winnerBlack());
    }
}
