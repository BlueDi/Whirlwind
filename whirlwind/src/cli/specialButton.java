package cli;

import javax.swing.*;
import java.awt.*;

public class SpecialButton extends JButton {
    private int row;
    private int col;

    SpecialButton(int r, int c) {
        row = r;
        col = c;
        this.setBackground(Color.GRAY);
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
}
