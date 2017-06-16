package logic;

import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GameTest {
    @Test
    public void testBoard() throws Exception {
        Game game = new Game(0, 5);
        int winner = game.startGame();
        assertEquals(winner, 0);
    }
}
