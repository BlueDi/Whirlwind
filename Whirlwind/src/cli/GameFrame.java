package cli;

import logic.Board;
import logic.Game;
import logic.Piece;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class GameFrame extends JFrame {

    private static final long serialVersionUID = 1L;
    private Color PLAYER1_COLOR = Color.BLACK;
    private Color PLAYER2_COLOR = Color.WHITE;
    private Color NO_PLAYER_COLOR = Color.GRAY;
    private Color BORDER_COLOR = Color.DARK_GRAY;
    private JPanel contentPane;
    
    private JLabel[][] grid;
    private int BOARD_SIZE;
    private Game logic;
    private ImageIcon icon0  = new ImageIcon("white.gif");
    private ImageIcon icon1  = new ImageIcon("black.gif");
    private ImageIcon icon2=new ImageIcon("winwhite.gif");
    private ImageIcon icon3=new ImageIcon("winblack.gif");
    private ArrayList<specialButton> buttons;
    public int win=0;
    
    
    
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
    public GameFrame(Board b,Game g) {
        this();
        logic=g;
        BOARD_SIZE = b.getSize();
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        contentPane.setLayout(new GridLayout(BOARD_SIZE + 2, BOARD_SIZE + 2));
        buttons=new ArrayList<specialButton>();
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
        Insets buttonMargin = new Insets(0, 0, 0, 0);
        for (int row = 0; row < BOARD_SIZE; row++) {
            insertLabel(PLAYER2_COLOR);
            for (int col = 0; col < BOARD_SIZE; col++) {
                char playerSymbol = b.getBoard()[row][col].getSymbol();
                grid[row][col] = new JLabel(row + " " + col);
              /*  grid[row][col].setBorder(new LineBorder(BORDER_COLOR));
                grid[row][col].setBackground(choosePieceColor(playerSymbol));
                grid[row][col].setOpaque(true);
                contentPane.add(grid[row][col]);*/
                specialButton j=new specialButton(row, col);
                j.setMargin(buttonMargin);
                ImageIcon icon = null;
                if(playerSymbol=='X'){
                icon = icon1;
                }
                if(playerSymbol=='O'){
                	icon = icon0;
                }
                if(playerSymbol=='+'){
                	icon = null;
                }
                j.setIcon(icon);
               contentPane.add(j);
               buttons.add(j);
               
        	   
               if(logic.getMode()==2 || logic.getMode()==1){
                j.addActionListener(new ActionListener() { 
                	  public void actionPerformed(ActionEvent e) { 
                	   specialButton now =(specialButton)e.getSource();
                	   if(now.getIcon()==null){
                		   
                		   
                		   if(logic.getActivePlayer()==1){
                			   if(logic.turnAction(now))
                			   now.setIcon(icon1);
                		   }
                		   else{
                			   if(logic.turnAction(now))
                			   now.setIcon(icon0);
                		   }
                		   if(win==1){
                			   
                			   for(int i=0; i<buttons.size();i++)
                				   buttons.get(i).setEnabled(false);
                			   
                			   
                		   }
                		   if(win==2){
                			   for(int i=0; i<buttons.size();i++)
                				   buttons.get(i).setEnabled(false);
                			   
                			   
                			   
                		   }
                		   
                	   if(logic.getMode()==2){
                		   logic.initiatebestMoveMessages();
                		   Piece p=logic.turnCPU();
                		   logic.setPiece(p);
                		   System.out.println(p.getRow()+","+p.getCol());
                		   for(int i=0; i<buttons.size();i++)
                			   if(buttons.get(i).getRow()==p.getRow() && buttons.get(i).getCol()==p.getCol()){
                				   if(logic.getActivePlayer()==1){
                					   buttons.get(i).setIcon(icon1);
                				   }
                				   else
                					   buttons.get(i).setIcon(icon0);
                			   }
                		   logic.checkwin();
                		   
                		   
                		   	if(win==1){
                			   
                			   for(int i=0; i<buttons.size();i++)
                				   buttons.get(i).setEnabled(false);
                			   
                			   
                		   }
                		   if(win==2){
                			   for(int i=0; i<buttons.size();i++)
                				   buttons.get(i).setEnabled(false);
                			   
                			   
                			   
                		   }
                		   logic.changePlayer();
                	   }
                	   
                		   }
                	  } 
                	} );
                }
                
            }
            insertLabel(PLAYER2_COLOR);
        }

        insertLine(PLAYER1_COLOR);

        add(contentPane);
        if(logic.getMode()==3){
     	   while(logic.checkwin()!=1 && logic.checkwin() !=2){
     		   
     	   logic.initiatebestMoveMessages();
   		   Piece p=logic.turnCPU();
   		   logic.setPiece(p);
   		   	for(int i=0; i<buttons.size();i++)
			   if(buttons.get(i).getRow()==p.getRow() && buttons.get(i).getCol()==p.getCol()){
				   if(logic.getActivePlayer()==1){
					   buttons.get(i).setIcon(icon1);
				   }
				   else
					   buttons.get(i).setIcon(icon0);
			   }
   		 if(logic.checkwin()	==2 || logic.checkwin()==1){
			   for(int i=0; i<buttons.size();i++)
				   buttons.get(i).setEnabled(false);
			   
			   
			   
		   }
		   logic.changePlayer();
		   
     	   try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
     	   
     	  
     	   
     	   }
 	   }
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
