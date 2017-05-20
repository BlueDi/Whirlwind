package cli;

import javax.swing.*;

public class specialButton extends JButton {
    private int row;
    private int col;

    specialButton(int r, int c) {
        row = r;
        col = c;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
}
