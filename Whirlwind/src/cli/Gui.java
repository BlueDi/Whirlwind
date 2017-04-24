package cli;

import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import logic.Board;
import logic.Piece;

public class Gui {

	private JFrame frame;
	private JPanel panel;
	private JButton button;
	private JLabel label;
	private Board board;

	public Gui() {
		frame = new JFrame("Whirlwind");
		frame.setVisible(true);
		frame.setSize(600, 400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		panel = new JPanel();
		panel.setBackground(Color.YELLOW);

		button = new JButton("Click");

		label = new JLabel("Label de teste");

		panel.add(button);
		panel.add(label);

		frame.add(panel);
	}

	public Gui(Board b) {
		this();
		board = b;
	}

	public void setBoard(Board board) {
		this.board = board;
	}

	public void update(Board b) {
		for (Piece[] lp : board.getBoard())
			for (Piece p : lp) {
				String s = "" + p.getSymbol();
				panel.add(new JLabel(s));
			}

		panel.repaint();
		frame.add(panel);
		frame.repaint();
	}
}
