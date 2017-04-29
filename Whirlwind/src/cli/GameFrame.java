package cli;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import logic.Board;

public class GameFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private int row;
	private int col;

	/**
	 * Create the frame.
	 */
	private GameFrame() {
		setName("Whirlwind");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1080, 600);
		setVisible(true);
	}

	/**
	 * Create the frame setting a board.
	 */
	public GameFrame(Board b) {
		this();
		row = b.getSize();
		col = b.getSize();
		contentPane = new JPanel();
		add(contentPane);
		update(b);
	}

	private void resetFrame() {
		getContentPane().remove(contentPane);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
		contentPane.setLayout(new GridLayout(row, col));
		add(contentPane);
	}

	public void update(Board b) {
		resetFrame();
		JLabel[][] grid = new JLabel[row][col];

		for (int i = 0; i < row; i++)
			for (int j = 0; j < col; j++) {
				grid[i][j] = new JLabel(" " + b.getBoard()[i][j].getSymbol());
				grid[i][j].setBorder(new LineBorder(Color.BLACK));
				grid[i][j].setBackground(Color.WHITE);
				grid[i][j].setOpaque(true);
				contentPane.add(grid[i][j]);
			}
		add(contentPane);
	}

}
