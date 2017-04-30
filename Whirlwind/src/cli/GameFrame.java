package cli;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import logic.Board;
import logic.Piece;

public class GameFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private Color PLAYER1_COLOR = Color.BLACK;
	private Color PLAYER2_COLOR = Color.WHITE;
	private Color NO_PLAYER_COLOR = Color.GRAY;
	private JPanel contentPane;
	private JLabel[][] grid;
	private int BOARD_SIZE;
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
	 * 
	 * @param b
	 *            Tabuleiro a ser desenhado
	 */
	public GameFrame(Board b) {
		this();
		BOARD_SIZE = b.getSize();
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
		contentPane.setLayout(new GridLayout(BOARD_SIZE, BOARD_SIZE));
		create(b);
	}

	/**
	 * Cria o tabuleiro inicial, com as definições iniciais.
	 * 
	 * @param b
	 *            Tabuleiro a ser desenhado
	 */
	public void create(Board b) {
		grid = new JLabel[BOARD_SIZE][BOARD_SIZE];

		for (int row = 0; row < BOARD_SIZE; row++)
			for (int col = 0; col < BOARD_SIZE; col++) {
				char playerSymbol = b.getBoard()[row][col].getSymbol();
				grid[row][col] = new JLabel();
				grid[row][col].setBorder(new LineBorder(Color.BLACK));
				grid[row][col].setBackground(choosePieceColor(playerSymbol));
				grid[row][col].setOpaque(true);
				contentPane.add(grid[row][col]);
			}
		add(contentPane);
	}

	/**
	 * Atualiza o estado do Tabuleiro na interface gráfica. Recebe a peça nova
	 * para atualizar o tabuleiro.
	 * 
	 * @param p
	 *            Nova peça a ser colocada no tabuleiro
	 */
	public void update(Piece p) {
		row = p.getRow();
		col = p.getCol();

		// Remover todas as JLabels do JPanel
		for (JLabel[] jpa : grid)
			for (JLabel jp : jpa)
				contentPane.remove(jp);

		grid[row][col] = new JLabel();
		grid[row][col].setBorder(new LineBorder(Color.BLACK));
		grid[row][col].setBackground(choosePieceColor(p.getSymbol()));
		grid[row][col].setOpaque(true);

		// Adicionar todas as JLabels ao JPanel
		for (JLabel[] jpa : grid)
			for (JLabel jp : jpa)
				contentPane.add(jp);

		add(contentPane);
	}

	/**
	 * Define a cor da Peça do Jogador
	 * 
	 * @param symbol
	 *            Simbolo do qual se quer obter a cor correspondente
	 * @return Cor correspondente ao simbolo
	 */
	private Color choosePieceColor(char symbol) {
		if (symbol == 'X')
			return PLAYER1_COLOR;
		else if (symbol == 'O')
			return PLAYER2_COLOR;
		return NO_PLAYER_COLOR;
	}
}
