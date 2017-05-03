package cli;

import logic.Board;
import logic.Piece;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class GameFrame extends JFrame {

    private static final long serialVersionUID = 1L;
    private Color PLAYER1_COLOR = Color.BLACK;
    private Color PLAYER2_COLOR = Color.WHITE;
    private Color NO_PLAYER_COLOR = Color.GRAY;
    private Color BORDER_COLOR = Color.DARK_GRAY;
    private JPanel contentPane;
    private JLabel[][] grid;
    private int BOARD_SIZE;

    /**
     * Create the frame.
     */
    private GameFrame() {
        setName("Whirlwind");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(100, 100, 1080, 600);
        setVisible(true);
    }

    /**
     * Create the frame setting a board.
     *
     * @param b Tabuleiro a ser desenhado
     */
    public GameFrame(Board b) {
        this();
        BOARD_SIZE = b.getSize();
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        contentPane.setLayout(new GridLayout(BOARD_SIZE + 2, BOARD_SIZE + 2));
        create(b);
    }

    /**
     * Cria o tabuleiro inicial, com as definições iniciais.
     *
     * @param b Tabuleiro a ser desenhado
     */
    private void create(Board b) {
        grid = new JLabel[BOARD_SIZE][BOARD_SIZE];

        insertLine(PLAYER1_COLOR);

        for (int row = 0; row < BOARD_SIZE; row++) {
            insertLabel(PLAYER2_COLOR);
            for (int col = 0; col < BOARD_SIZE; col++) {
                char playerSymbol = b.getBoard()[row][col].getSymbol();
                grid[row][col] = new JLabel(row + " " + col);
                grid[row][col].setBorder(new LineBorder(BORDER_COLOR));
                grid[row][col].setBackground(choosePieceColor(playerSymbol));
                grid[row][col].setOpaque(true);
                contentPane.add(grid[row][col]);
            }
            insertLabel(PLAYER2_COLOR);
        }

        insertLine(PLAYER1_COLOR);

        add(contentPane);
    }

    private void insertLine(Color color) {
        for (int col = 0; col < BOARD_SIZE + 2; col++)
            insertLabel(color);
    }

    private void insertLabel(Color color) {
        JLabel border;

        border = new JLabel();
        border.setBorder(new LineBorder(color));
        border.setBackground(color);
        border.setOpaque(true);

        contentPane.add(border);
    }

    /**
     * Atualiza o estado do Tabuleiro na interface gráfica. Recebe a peça nova
     * para atualizar o tabuleiro.
     *
     * @param p Nova peça a ser colocada no tabuleiro
     */
    public void update(Piece p) {
        int row = p.getRow();
        int col = p.getCol();

        contentPane.removeAll();

        insertLine(PLAYER1_COLOR);

        grid[row][col] = new JLabel(row + " " + col);
        grid[row][col].setBorder(new LineBorder(BORDER_COLOR));
        grid[row][col].setBackground(choosePieceColor(p.getSymbol()));
        grid[row][col].setOpaque(true);

        // Adicionar todas as JLabels ao JPanel
        for (JLabel[] jpa : grid) {
            insertLabel(PLAYER2_COLOR);
            for (JLabel jp : jpa)
                contentPane.add(jp);
            insertLabel(PLAYER2_COLOR);
        }

        insertLine(PLAYER1_COLOR);

        add(contentPane);
    }

    /**
     * Define a cor da Peça do Jogador
     *
     * @param symbol Simbolo do qual se quer obter a cor correspondente
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
