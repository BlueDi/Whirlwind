package cli;

import logic.Board;
import logic.Game;
import logic.Piece;
import util.Utility;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.ArrayList;

public class GameFrame extends JFrame {

    private static final long serialVersionUID = 1L;
    public int win = 0;
    private Color PLAYER1_COLOR = Color.BLACK;
    private Color PLAYER2_COLOR = Color.WHITE;
    private JPanel contentPane;
    private int BOARD_SIZE;
    private Game logic;
    private ImageIcon icon1 = new ImageIcon("black.gif");
    private ImageIcon icon2 = new ImageIcon("white.gif");
    private ImageIcon icon1win = new ImageIcon("winblack.gif");
    private ImageIcon icon2win = new ImageIcon("winwhite.gif");
    private ArrayList<specialButton> buttons;


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
     * Cria a frame configurando o tabuleiro.
     *
     * @param actualgame Estado do jogo
     */
    public GameFrame(Game actualgame) {
        this();
        logic = actualgame;
        BOARD_SIZE = logic.getBOARDDIMENSION();
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        contentPane.setLayout(new GridLayout(BOARD_SIZE + 2, BOARD_SIZE + 2));
        buttons = new ArrayList<>();
        create(logic.getBoard());
    }

    /**
     * Cria o tabuleiro inicial, com as definições iniciais.
     *
     * @param b Tabuleiro a ser desenhado
     */
    private void create(Board b) {
        Insets buttonMargin = new Insets(0, 0, 0, 0);

        insertLine(PLAYER1_COLOR);

        for (int row = 0; row < BOARD_SIZE; row++) {
            insertLabel(PLAYER2_COLOR, String.valueOf(row + 1));
            for (int col = 0; col < BOARD_SIZE; col++) {
                char playerSymbol = b.getBoard()[row][col].getSymbol();
                specialButton board_position = new specialButton(row, col);
                board_position.setMargin(buttonMargin);

                ImageIcon icon;
                if (playerSymbol == 'X') {
                    icon = icon1;
                    board_position.setBackground(PLAYER1_COLOR);
                } else if (playerSymbol == 'O') {
                    icon = icon2;
                    board_position.setBackground(PLAYER2_COLOR);
                } else
                    icon = null;
                board_position.setIcon(icon);

                contentPane.add(board_position);
                buttons.add(board_position);

                if (logic.getGAMEMODE() == 2 || logic.getGAMEMODE() == 1)
                    PlayervsPlayer(board_position);
            }
            insertLabel(PLAYER2_COLOR, String.valueOf(row + 1));
        }

        insertLine(PLAYER1_COLOR);

        add(contentPane);

        if (logic.getGAMEMODE() == 3) {
            CPUvsCPU();
        }

        win = logic.checkwin();
        if (win == 1 || win == 2)
            endGame();
    }

    private void PlayervsPlayer(specialButton board_position) {
        board_position.addActionListener(e -> {
            specialButton now = (specialButton) e.getSource();
            boolean CPUturn = false;

            if (logic.getActivePlayer() == 1) {
                if (logic.turnAction(now)) {
                    CPUturn = true;
                    now.setIcon(icon1);
                    now.setBackground(PLAYER1_COLOR);
                }
            } else {
                if (logic.turnAction(now)) {
                    CPUturn = true;
                    now.setIcon(icon2);
                    now.setBackground(PLAYER2_COLOR);
                }
            }

            if (win == 1 || win == 2)
                endGame();

            if (logic.getGAMEMODE() == 2 && CPUturn) {
                logic.initiatebestMoveMessages();

                Piece p = logic.turnCPU(3);
                logic.setPiece(p);
                updateButton(p);
                System.out.println(p.getRow() + "," + p.getCol());

                logic.checkwin();
                logic.changePlayer();
            }
        });
    }

    private void CPUvsCPU() {
        while (logic.checkwin() != 1 && logic.checkwin() != 2) {
            logic.initiatebestMoveMessages();
            Piece p = logic.turnCPU(1);
            logic.setPiece(p);
            updateButton(p);
            logic.changePlayer();
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void endGame() {
        win = logic.checkwin();
        if (win == 1)
            System.out.println("Black Won!");
        else if (win == 0)
            System.out.println("White Won!");
        for (specialButton button : buttons)
            button.setEnabled(false);
    }

    private void updateButton(Piece p) {
        for (specialButton button : buttons) {
            if (button.getRow() == p.getRow() && button.getCol() == p.getCol()) {
                if (logic.getActivePlayer() == 1) {
                    button.setIcon(icon1);
                    button.setBackground(PLAYER1_COLOR);
                } else if (logic.getActivePlayer() == 0) {
                    button.setIcon(icon2);
                    button.setBackground(PLAYER2_COLOR);
                }
            }
        }
    }

    private void insertLine(Color color) {
        insertLabel(color, null);
        for (int col = 1; col < BOARD_SIZE + 1; col++) {
            char letter = Utility.itoc(col - 1);
            String text = String.valueOf(letter);
            insertLabel(color, text);
        }
        insertLabel(color, null);
    }

    private void insertLabel(Color color, String text) {
        JLabel border;

        border = new JLabel(text, SwingConstants.CENTER);
        border.setBorder(new LineBorder(color));
        border.setBackground(color);
        border.setOpaque(true);

        contentPane.add(border);
    }
}
