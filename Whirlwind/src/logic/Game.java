package logic;

import cli.GameFrame;
import cli.specialButton;
import util.Utility;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class Game {
    private Board board;
    private GameFrame gameframe;
    /**
     * 1: Human vs Human<P>
     * 2: Human vs CPU<P>
     * 3: CPU Hard vs CPU Hard<P>
     * 4: CPU Random vs CPU Easy<P>
     * 5: CPU Random vs CPU Hard<P>
     * 6: CPU Easy vs CPU Medium<P>
     * 7: CPU Medium vs CPU Hard<P>
     * 8: CPU Easy vs CPU Hard
     */
    private int GAMEMODE;
    /**
     * DIFFICULTY = 0: CPU é random<P>
     * DIFFICULTY = 1: CPU Easy<P>
     * DIFFICULTY = 2: CPU Medium<P>
     * DIFFICULTY = 3: CPU Hard
     */
    private int DIFFICULTY = 0;
    /**
     * Profundidade do algoritmo MiniMax.
     */
    private int DEPTH = 3;
    private int depth;
    private int DISPLAY;
    private int turnId;
    private int BOARDDIMENSION = 14;
    private boolean SLOWMODE = false;
    /**
     * Quando activeplayer = 1 é o turno do jogador com as peças pretas;
     * Quando activeplayer = 0 é o turno do jogador com as peças brancas.
     */
    private int activeplayer = -1;
    private Deque<String> bestMoveMessages;

    /**
     * Cria o jogo escolhendo o display e o modo.
     * Um board de dimensão 14 garante que ambos jogadores começam com 20 peças.
     *
     * @param display Mode de display. GUI ou Consola
     * @param mode    Modo de jogo. Entre PVP, PVE, e EVE
     * @throws Exception falha a iniciar
     */
    public Game(int display, int mode) throws Exception {
        DISPLAY = display;
        GAMEMODE = mode;
        activeplayer = 1; // black;
        board = new Board(BOARDDIMENSION, boardPicker());

        if (DISPLAY == 1) {
            gameframe = new GameFrame(this);
            gameframe.setVisible(true);
        }
    }

    /**
     * Escolhe o tabuleiro entre os 2 predefinidos.
     *
     * @return Número correspondente ao tabuleiro
     */
    private int boardPicker() {
        return Utility.random(0, 1);
    }

    public Board getBoard() {
        return board;
    }

    public int getBOARDDIMENSION() {
        return BOARDDIMENSION;
    }

    public int getActivePlayer() {
        return activeplayer;
    }

    public int getGAMEMODE() {
        return GAMEMODE;
    }

    public void initiatebestMoveMessages() {
        bestMoveMessages = new LinkedList<>();
    }

    /**
     * Turno do jogo.
     */
    private void turn() {
        board.display();
        initiatebestMoveMessages();
        Piece move;

        if (SLOWMODE)
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        defineFirstPlayer();
        move = pieceToPlace();
        setPiece(move);

        System.out.println("Jogador das peças " + Utility.itoPlayer(activeplayer) + " tem: " + (board.getPlayerPieces(activeplayer).size() - 1) + " peças no tabuleiro.");
        changePlayer();
    }

    private void defineFirstPlayer() {
        if (GAMEMODE == 2 || GAMEMODE == 3)
            DIFFICULTY = 3;
        else if (GAMEMODE == 4 || GAMEMODE == 5)
            DIFFICULTY = 0;
        else if (GAMEMODE == 6 || GAMEMODE == 8)
            DIFFICULTY = 1;
        else if (GAMEMODE == 7)
            DIFFICULTY = 2;
        else
            DIFFICULTY = 1;
    }

    public Piece pieceToPlace() {
        Piece move;
        if (GAMEMODE == 1)
            move = turnPlayer();
        else if (GAMEMODE == 2 && activeplayer == 1)
            move = turnPlayer();
        else {
            move = turnCPU(DIFFICULTY);
            if (GAMEMODE == 4) {
                DIFFICULTY ^= 1;
            } else if (GAMEMODE == 5) {
                if (DIFFICULTY == 0)
                    DIFFICULTY = 3;
                else
                    DIFFICULTY = 0;
            } else if (GAMEMODE == 6) {
                if (DIFFICULTY == 1)
                    DIFFICULTY = 2;
                else
                    DIFFICULTY = 1;
            } else if (GAMEMODE == 7) {
                if (DIFFICULTY == 2)
                    DIFFICULTY = 3;
                else
                    DIFFICULTY = 2;
            } else if (GAMEMODE == 8) {
                if (DIFFICULTY == 1)
                    DIFFICULTY = 3;
                else
                    DIFFICULTY = 1;
            }
        }
        return move;
    }

    public void setPiece(Piece p) {
        board.setPiece(p);
    }

    /**
     * Turno de jogo do Computador.
     */
    public Piece turnCPU(int difficulty) {
        depth = DEPTH;

        if (difficulty != 0) {
            Heur bestMove = miniMax();
            String message = "Melhor jogada para o jogador " + Utility.itop(activeplayer) + " no turno " + turnId + ": (" + (bestMove.row + 1) + "," + Utility.itoc(bestMove.col) + "). ";
            bestMoveMessages.addFirst(message);

            return new Piece(bestMove, activeplayer);
        }
        return turnRandomCPU();
    }

    private Piece turnRandomCPU() {
        Piece p;
        int random_row;
        int random_col;

        //TODO: É preciso verificar a posição criada random, mas como está crasha
        //do {
        random_row = Utility.random(0, BOARDDIMENSION);
        random_col = Utility.random(0, BOARDDIMENSION);
        p = new Piece(random_row, random_col, activeplayer);
        //} while (board.checkValidMove(p));

        return p;
    }

    /**
     * Turno de jogo do Jogador.
     */
    private Piece turnPlayer() {
        Piece playerPiece;
        Scanner reader = new Scanner(System.in);
        int row;
        int col;

        System.out.println("Player " + activeplayer + " pick the position for your new piece.");

        do {
            row = reader.nextInt() - 1;
            String column = reader.next();
            col = column.charAt(0) - 'A';

            if (row < 0 || row >= BOARDDIMENSION || col < 0 || col >= BOARDDIMENSION)
                System.out.println("Try again. Position (" + (row + 1) + "," + column.charAt(0) + ") is not valid.");

            playerPiece = new Piece(row, col, activeplayer);
        } while (!board.checkValidMove(playerPiece));

        return playerPiece;
    }

    public boolean turnAction(specialButton b) {
    	if(b==null){
    		return false;
    	}
    	else{    		
    		if(board.setPiece(new Piece(b.getRow(),b.getCol(), activeplayer))){
    			
    	    	changePlayer();
    	    	if(checkwin()==1)
    	    		gameframe.win=1;
    	    	if(checkwin()==2)
    	    		gameframe.win=2;
    	    	
    	    	return true;
    		}
    		
    	
    	}
    	return false;
    }

    public int checkwin() {
        if (board.winnerBlack()) {
            return 1;

        }
        if (board.winnerWhite()) {
            return 2;

        }
        return 0;
    }

    /**
     * Inicia o jogo.
     */
    public int startGame() {
        turnId = 0;
        int winner = -1;

        while (winner == -1) {
            turnId++;
            System.out.println("\nTurno " + turnId + ": Peças " + Utility.itoPlayer(activeplayer) + " a jogar");
            turn();

            if (DIFFICULTY != 0)
                for (String s : bestMoveMessages) {
                    System.out.println(s);
                }

            if (activeplayer == 1 && board.winnerBlack())
                winner = 1;
            else if (activeplayer == 0 && board.winnerWhite())
                winner = 0;
        }

        return winner;
    }

    /**
     * Muda de jogador
     */
    public void changePlayer() {
        activeplayer ^= 1;
    }

    /**
     * Compara uma jogada com a atual melhor jogada.
     *
     * @param currentBestMove Melhor jogada atual
     * @param newMove         Jogada a verificar se &eacute; a melhor atual
     * @return Cortes Alfa-Beta: true se viável continuar à procura, false se não
     */
    private boolean updateBestMove(Heur currentBestMove, Heur newMove) {
        if (newMove.value > currentBestMove.value) {
            currentBestMove.value = newMove.value;
            currentBestMove.row = newMove.row;
            currentBestMove.col = newMove.col;
            currentBestMove.movement = newMove.movement;
            return true;
        }
        return false;
    }

    /**
     * Calcula o valor do tabuleiro para o jogador branco.
     *
     * @param position Nova peça no tabuleiro
     * @return valor do tabuleiro
     */
    private int calcWhiteHeur(Heur position) {
        int value = 0;
        Queue<Piece> player_pieces = board.getPlayerPieces(activeplayer);
        Queue<Piece> enemy_pieces = board.getPlayerPieces(activeplayer ^ 1);

        for (Piece p : player_pieces) {
            if (p.getRow() == position.row)
                value += 2;
        }

        if (DIFFICULTY > 1) {
            int[] inimigos_na_linha = new int[BOARDDIMENSION];

            for (Piece p : enemy_pieces) {
                inimigos_na_linha[p.getRow()]++;
                if (p.getRow() == position.row)
                    value--;
            }

            value += inimigos_na_linha[position.row];
        }

        if (DIFFICULTY > 2) {
            for (Piece p : player_pieces) {
                if (p.getCol() == position.col++ || p.getRow() == position.col--)
                    value += 1;
            }
        }

        return value;
    }

    /**
     * Calcula o valor do tabuleiro para o jogador preto.
     *
     * @param position Posição a calcular o valor da jogada
     * @return valor do tabuleiro
     */
    private int calcBlackHeur(Heur position) {
        int value = 0;
        Queue<Piece> player_pieces = board.getPlayerPieces(activeplayer);
        Queue<Piece> enemy_pieces = board.getPlayerPieces(activeplayer ^ 1);

        for (Piece p : player_pieces) {
            if (p.getCol() == position.col)
                value += 2;
        }

        if (DIFFICULTY > 1) {
            int[] inimigos_na_coluna = new int[BOARDDIMENSION];

            for (Piece p : enemy_pieces) {
                inimigos_na_coluna[p.getCol()]++;
                if (p.getCol() == position.col)
                    value--;
            }

            value += inimigos_na_coluna[position.row];
        }

        if (DIFFICULTY > 2) {
            for (Piece p : player_pieces) {
                if (p.getRow() == position.row++ || p.getRow() == position.row--)
                    value++;
            }
        }

        return value;
    }

    /**
     * Calcula o valor do tabuleiro com a nova peça.
     *
     * @param position A peça que vai ser colocada no turno
     */
    private void calcHeur(Heur position) {
        if (activeplayer == 0)
            position.value += calcWhiteHeur(position);
        else
            position.value += calcBlackHeur(position);
    }

    /**
     * Calcula um movimento para a peça a analisar
     *
     * @param position Nova peça no tabuleiro
     * @return Heur que representa a melhor peça para o turno
     */
    private Heur singularMoveOfAPiece(Heur position) {
        Heur valueOfMove = new Heur(position);
        valueOfMove.moveCalculator();

        Piece p = new Piece(valueOfMove, activeplayer);

        if (board.setPiece(p)) {
            calcHeur(valueOfMove);

            if (depth > 0) {
                changePlayer();
                Heur enemymove = miniMax(p);
                changePlayer();

                position.value -= enemymove.value;
            }

            board.removePiece(valueOfMove.row, valueOfMove.col);
        }

        return valueOfMove;
    }

    /**
     * Calcula todos os movimentos possíveis para a posição (row,col).
     * cortes alfa-beta
     *
     * @param actualBestMove Melhor peça a colocar no tabuleiro
     * @param pieceToCheck   Peça a calcular as jogadas possiveis
     */
    private Heur movesForAPiece(Heur actualBestMove, Piece pieceToCheck) {
        int LOWEST_VALUE = -99999;
        int row = pieceToCheck.getRow();
        int col = pieceToCheck.getCol();
        Heur originalBestMove = new Heur(actualBestMove);
        Heur[] movesForThis = new Heur[4];
        int lowest = 999999;

        for (int i = 0; i < 4; i++) {
            movesForThis[i] = new Heur(row, col, i);
            Heur bestForThisMove = singularMoveOfAPiece(movesForThis[i]);

            if (bestForThisMove.value < lowest)
                lowest = bestForThisMove.value;

            if (lowest < LOWEST_VALUE)
                return originalBestMove;

            updateBestMove(actualBestMove, bestForThisMove);
        }

        return actualBestMove;
    }

    /**
     * Algoritmo MiniMax.
     *
     * @return Melhor peça possível de se colocar no turno
     */
    private Heur miniMax() {
        depth--;
        Heur bestMove = new Heur();
        Queue<Piece> playerPieces = board.getPlayerPiecesWithPossibleMovements(activeplayer);

        while (!playerPieces.isEmpty()) {
            bestMove = movesForAPiece(bestMove, playerPieces.remove());
        }

        return bestMove;
    }

    private Heur miniMax(Piece p) {
        bestMoveMessages.add("Melhor jogada para o jogador " + Utility.itop(activeplayer) + " no turno " + (turnId + DEPTH - depth) + ": (" + (p.getRow() + 1) + "," + Utility.itoc(p.getCol()) + "). ");
        return miniMax();
    }
}
