package util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UtilityTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @BeforeEach
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @Test
    public void testPrintLineOfChar() {
        Utility.printLineOfChar(5);
        assertEquals("A B C D E ", outContent.toString());
    }

    @Test
    public void testPrintDashedLine() {
        Utility.printDashedLine(5);
        assertEquals("------------" + System.getProperty("line.separator"), outContent.toString());
    }

    @Test
    public void testItoa() {
        assertEquals('f', Utility.itoa(-1));
        assertEquals('^', Utility.itoa(0));
        assertEquals('>', Utility.itoa(1));
        assertEquals('v', Utility.itoa(2));
        assertEquals('<', Utility.itoa(3));
        assertEquals('f', Utility.itoa(5));
    }

    @Test
    public void testItop() {
        assertEquals('e', Utility.itop(-1));
        assertEquals('O', Utility.itop(0));
        assertEquals('X', Utility.itop(1));
        assertEquals('f', Utility.itop(10));
    }

    @Test
    public void testRandom() {
        int min = 5;
        int max = 10;
        int rng;
        int rng2;

        for (int i = 0; i < 100; i++) {
            rng = Utility.random(min, max);

            assertTrue(rng >= min);
            assertTrue(rng <= max);
        }

        for (int i = 0; i < 100; i++) {
            rng2 = Utility.random();

            assertTrue(rng2 >= 0);
            assertTrue(rng2 <= 10);
        }
    }

}
