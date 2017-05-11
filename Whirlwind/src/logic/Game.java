package logic;

import cli.GameFrame;
import util.Utility;

import java.util.*;

public class Game {
    private Board board;
    private GameFrame gameframe;
    private int GAMEMODE;
    private int DISPLAY;
    private int turnId;
    private int DEPTH = 3;
    private int activeplayer = -1;
    private List<Heur> first_calc;
    private int depth;
    private Deque<String> bestMoveMessages;

    /**
     * Cria o jogo escolhendo o display e o modo.
     * Um board de dimens�o 14 garante que ambos jogadores come�am com 20 pe�as.
     *
     * @param display Mode de display. GUI ou Consola
     * @param mode    Modo de jogo. Entre PVP, PVE, e EVE
     * @throws Exception falha a iniciar
     */
    public Game(int display, int mode) throws Exception {
        DISPLAY = display;
        GAMEMODE = mode;
        activeplayer = 1; // black;
        int dimensao_do_tabuleiro = 14;
        board = new Board(dimensao_do_tabuleiro, boardPicker());

        if (DISPLAY == 1) {
            gameframe = new GameFrame(board);
            gameframe.setVisible(true);
        }
    }

    /**
     * Escolhe o tabuleiro entre os 2 predefinidos.
     *
     * @return N�mero correspondente ao tabuleiro
     */
    private int boardPicker() {
        return Utility.random(0, 1);
    }

    /**
     * Turno do jogo.
     */
    private void turn() {
        board.display();
        bestMoveMessages = new LinkedList<>();
        Piece move;

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (GAMEMODE == 1)
            move = turnPlayer();
        else if (GAMEMODE == 2 && activeplayer == 1)
            move = turnPlayer();
        else
            move = turnCPU();

        board.setPiece(move);

        if (DISPLAY == 1) {
            gameframe.update(move);
            gameframe.setVisible(true);
        }

        System.out.println("Jogador " + activeplayer + " tem: " + (board.getPlayerPieces(activeplayer).size() - 1)
                + " pe�as no tabuleiro.");
        changePlayer();
    }

    /**
     * Turno de jogo do Computador.
     */
    private Piece turnCPU() {
        depth = DEPTH;
        Heur bestMove = miniMax();

        bestMoveMessages.addFirst("Melhor jogada para o jogador " + Utility.itop(activeplayer) + " no turno " + turnId + ": (" + (bestMove.row + 1) + "," + Utility.itoc(bestMove.col) + "). ");

        return new Piece(bestMove.row, bestMove.col, activeplayer);
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

            if (row < 0 || row >= board.getSize() || col < 0 || col >= board.getSize())
                System.out.println("Try again. Position (" + (row + 1) + "," + column.charAt(0) + ") is not valid.");

            playerPiece = new Piece(row, col, activeplayer);
        } while (!board.checkValidMove(playerPiece));

        return playerPiece;
    }

    /**
     * Inicia o jogo.
     */
    public int startGame() {
        turnId = 0;
        int winner = 0;

        while (winner == 0) {
            turnId++;
            System.out.println("\nTurno " + turnId + ": " + Utility.itop(activeplayer) + " a jogar");
            turn();

            for (String s : bestMoveMessages) {
                System.out.println(s);
            }

            if (board.winnerBlack())
                winner = 1;
            else if (board.winnerWhite())
                winner = 2;
        }

        return winner;
    }

    private void changePlayer() {
        activeplayer ^= 1;
    }

    /**
     * Compara uma jogada com a atual melhor jogada.
     *
     * @param currentBestMove Melhor jogada atual
     * @param newMove         Jogada a verificar se &eacute; a melhor atual
     */
    private void updateBestMove(Heur currentBestMove, Heur newMove) {
        if (newMove.value > currentBestMove.value) {
            currentBestMove.value = newMove.value;
            currentBestMove.row = newMove.row;
            currentBestMove.col = newMove.col;
            currentBestMove.movement = newMove.movement;
        }
    }

    /**
     * Calcula o valor do tabuleiro para o jogador branco.
     *
     * @param position Nova pe�a no tabuleiro
     * @return valor do tabuleiro
     */
    private int calcWhiteHeur(Heur position) {
        int value = 0;
        Queue<Piece> player_pieces = board.getPlayerPieces(activeplayer);
        Queue<Piece> enemy_pieces = board.getPlayerPieces(activeplayer ^ 1);

        for (Piece p : player_pieces) {
            if (p.getRow() == position.row)
                value++;
        }

        int[] inimigos_na_linha = new int[board.getSize()];

        for (Piece p : enemy_pieces) {
            inimigos_na_linha[p.getCol()]++;
            if (p.getRow() == position.col)
                value--;
        }

        value += inimigos_na_linha[position.row];

        return value;
    }

    /**
     * Calcula o valor do tabuleiro para o jogador preto.
     *
     * @param position Posi��o a calcular o valor da jogada
     * @return valor do tabuleiro
     */
    private int calcBlackHeur(Heur position) {
        int value = 0;
        Queue<Piece> player_pieces = board.getPlayerPieces(activeplayer);
        Queue<Piece> enemy_pieces = board.getPlayerPieces(activeplayer ^ 1);

        for (Piece p : player_pieces) {
            if (p.getCol() == position.col)
                value++;
        }

        int[] inimigos_na_linha = new int[board.getSize()];

        for (Piece p : enemy_pieces) {
            inimigos_na_linha[p.getRow()]++;
            if (p.getCol() == position.col)
                value--;
        }

        value += inimigos_na_linha[position.row];

        return value;
    }

    /**
     * TODO: Calcula o valor do tabuleiro com a nova pe�a.
     *
     * @param position A pe�a que vai ser colocada no turno
     */
    private void calcHeur(Heur position) {
        if (activeplayer == 0)
            position.value += calcWhiteHeur(position);
        else
            position.value += calcBlackHeur(position);
    }

    /**
     * Calcula um movimento para a pe�a a analisar
     *
     * @param position Nova pe�a no tabuleiro
     * @return Heur que representa a melhor pe�a para o turno
     */
    private Heur singularMoveOfAPiece(Heur position) {
        Heur position2 = new Heur();
        position2.row = position.row;
        position2.col = position.col;
        position2.movement = position.movement;

        position2.moveCalculator();

        Piece p = new Piece(position2.row, position2.col, activeplayer);

        if (board.setPiece(p)) {
            calcHeur(position2);

            if (depth > 0) {
                changePlayer();
                Heur enemymove = miniMax(p);
                changePlayer();

                position.value -= enemymove.value;
            }

            board.removePiece(position2.row, position2.col);
        }

        return position2;
    }

    /**
     * Calcula todos os movimentos poss�veis para a posi��o (row,col).
     *
     * @param bestMove Melhor pe�a a colocar no tabuleiro
     * @param row      Linha da posi��o a calcular as jogadas possiveis
     * @param col      Coluna da posi�ao a calcular as jogadas possiveis
     */
    private void movesForAPiece(Heur bestMove, int row, int col) {
        for (int i = 0; i < 4; i++) {
            Heur position = new Heur();
            position.row = row;
            position.col = col;
            position.movement = i;
            Heur bestForThisMove = singularMoveOfAPiece(position);
            // first_calc.add(position2);

            updateBestMove(bestMove, bestForThisMove);
        }
    }

    /**
     * Algoritmo MiniMax.
     *
     * @return Melhor pe�a poss�vel de se colocar no turno
     */
    private Heur miniMax() {
        depth--;
        Heur bestMove = new Heur();

        Queue<Piece> playerPieces = board.getPlayerPieces(activeplayer);
        Queue<Piece> tempPlayerPieces = playerPieces;
        first_calc = new ArrayList<>();

        while (!tempPlayerPieces.isEmpty()) {
            /*
             * TODO: calcular o valor do movimento e guardar em first_calc;
			 * first_calc[iterador] = heuristic(temp_plauer_pieces.first());
			 */
            // TODO: Monte-Carlo tree search talvez seja bom para n�s

            Piece tempPiece = tempPlayerPieces.remove();

            movesForAPiece(bestMove, tempPiece.getRow(), tempPiece.getCol());
        }

        return bestMove;
    }

    private Heur miniMax(Piece p) {
        bestMoveMessages.add("Melhor jogada para o jogador " + Utility.itop(activeplayer) + " no turno " + (turnId + DEPTH - depth) + ": (" + (p.getRow() + 1) + "," + Utility.itoc(p.getCol()) + "). ");
        return miniMax();
    }
}
