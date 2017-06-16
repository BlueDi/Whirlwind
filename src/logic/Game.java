package logic;

import cli.GameFrame;
import cli.SpecialButton;
import util.Utility;

import java.util.*;

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
     * 8: CPU Easy vs CPU Hard<P>
     * 9: CPU Easy vs CPU Easy<P>
     * 10: CPU Medium vs CPU Medium<P>
     * 11: CPU Random vs CPU Random
     */
    private int GAMEMODE;
    /**
     * DIFFICULTY = 0: CPU é random<P>
     * DIFFICULTY = 1: CPU Easy<P>
     * DIFFICULTY = 2: CPU Medium<P>
     * DIFFICULTY = 3: CPU Hard
     */
    private int DIFFICULTY;
    /**
     * Profundidade do algoritmo MiniMax.
     */
    private int DEPTH = 5;
    private int depth;
    private int DISPLAY;
    private boolean DISPLAY_OFF = true;
    private int turnId;
    private int BOARDDIMENSION = 14;
    private boolean SLOWMODE = false;
    /**
     * Quando activeplayer = 1 é o turno do jogador com as peças pretas;<P>
     * Quando activeplayer = 0 é o turno do jogador com as peças brancas;<P>
     * As peças pretas jogam primeiro.
     */
    private int activeplayer = 1;
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
     * Inicia o jogo.
     *
     * @return Vencedor
     */
    public int startGame() {
        turnId = 0;
        int winner = -1;

        setFirstPlayerDifficulty();

        while (winner == -1) {
            turnId++;
            if (!DISPLAY_OFF)
                System.out.println("\nTurno " + turnId + ": Peças " + Utility.itoPlayer(activeplayer) + " a jogar");

            turn();

            if (DIFFICULTY != 0 && !DISPLAY_OFF)
                for (String s : bestMoveMessages) {
                    System.out.println(s);
                }

            winner = checkWin();
            changePlayerAndDifficulty();
        }

        return winner;
    }

    /**
     * Turno do jogo.
     */
    private void turn() {
        if (!DISPLAY_OFF)
            board.display();
        initiatebestMoveMessages();
        Piece move;

        if (SLOWMODE)
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        move = pieceToPlace();
        setPiece(move);

        if (!DISPLAY_OFF)
            System.out.println("Jogador das peças " + Utility.itoPlayer(activeplayer) + " tem: " + (board.getPlayerPieces(activeplayer).size() - 1) + " peças no tabuleiro.");
    }

    /**
     * Atualiza o Jogador e a Dificuldade.
     */
    private void changePlayerAndDifficulty() {
        changePlayer();
        changeDifficulty();
    }

    /**
     * Atualiza a Dificuldade.
     */
    private void changeDifficulty() {
        if (activeplayer == 1)
            setFirstPlayerDifficulty();
        else
            setSecondPlayerDifficulty();
    }

    /**
     * Atualiza a Dificuldade das peças pretas.
     */
    private void setFirstPlayerDifficulty() {
        if (GAMEMODE == 2 || GAMEMODE == 3)
            DIFFICULTY = 3;
        else if (GAMEMODE == 4 || GAMEMODE == 5 || GAMEMODE == 11)
            DIFFICULTY = 0;
        else if (GAMEMODE == 7 || GAMEMODE == 10)
            DIFFICULTY = 2;
        else
            DIFFICULTY = 1;
    }

    /**
     * Atualiza a Dificuldade das peças brancas.
     */
    private void setSecondPlayerDifficulty() {
        if (GAMEMODE == 2 || GAMEMODE == 3 || GAMEMODE == 5 || GAMEMODE == 7 || GAMEMODE == 8)
            DIFFICULTY = 3;
        else if (GAMEMODE == 6 || GAMEMODE == 10)
            DIFFICULTY = 2;
        else
            DIFFICULTY = 1;
    }

    /**
     * @return Peça a colocar no turno
     */
    private Piece pieceToPlace() {
        Piece move;
        if (GAMEMODE == 1)
            move = turnPlayer();
        else if (GAMEMODE == 2 && activeplayer == 1)
            move = turnPlayer();
        else
            move = turnCPU(DIFFICULTY);
        return move;
    }

    /**
     * Coloca a peça no tabuleiro.
     *
     * @param p Peça a ser colocada
     */
    public void setPiece(Piece p) {
        board.setPiece(p);
    }

    /**
     * Turno do Computador.
     *
     * @param difficulty Nível da Heuristica, ver DIFFICULTY
     * @return Peça a colocar neste turno
     */
    public Piece turnCPU(int difficulty) {
        depth = DEPTH;
        DIFFICULTY = difficulty;

        if (DIFFICULTY != 0) {
            Heur bestMove = miniMax();
            String message = "Melhor jogada para o jogador " + Utility.itop(activeplayer) + " no turno " + turnId + ": (" + (bestMove.row + 1) + "," + Utility.itoc(bestMove.col) + "). ";
            bestMoveMessages.addFirst(message);

            return new Piece(bestMove, activeplayer);
        }
        return turnRandomCPU();
    }

    /**
     * Turno random do CPU.
     *
     * @return Peça a colocar neste turno
     */
    private Piece turnRandomCPU() {
        ArrayList<Piece> possible_pieces = board.getFreeValidPieces(activeplayer);

        int random_index = Utility.random(0, possible_pieces.size() - 1);
        Piece piece_to_return = possible_pieces.get(random_index);
        piece_to_return.setPlayer(activeplayer);

        return piece_to_return;
    }

    /**
     * Turno do Jogador.
     * Peças devem ser colocadas introduzingo o número, depois espaço, depois letra em maiúscula.
     */
    private Piece turnPlayer() {
        Piece playerPiece;
        Scanner reader = new Scanner(System.in);
        int row;
        int col;

        System.out.println("Player " + activeplayer + " pick the position for your new piece.");
        do {
            try {
                row = Integer.parseInt(reader.next()) - 1;
            } catch (NumberFormatException e) {
                row = -1;
            }
            String column = reader.next();
            col = column.charAt(0) - 'A';

            playerPiece = new Piece(row, col, activeplayer);

            if (row < 0 || row >= BOARDDIMENSION || col < 0 || col >= BOARDDIMENSION || !board.checkValidMove(playerPiece))
                System.out.println("Try again. Position (" + (row + 1) + "," + column.charAt(0) + ") is not valid.");
        } while (!board.checkValidMove(playerPiece));

        return playerPiece;
    }

    /**
     * Função para o turno do Player quando carrega no botão da interface gráfica.
     *
     * @param b botão carregado
     * @return true se carregou num botão válido, caso contrário false
     */
    public boolean turnAction(SpecialButton b) {
        if (b != null && board.setPiece(new Piece(b.getRow(), b.getCol(), activeplayer))) {
            gameframe.win = checkWin();
            return true;
        }
        return false;
    }

    /**
     * Verifica se há vencedor.
     *
     * @return 1 caso as peças pretas tenham ganho, 0 se foram as peças brancas, -1 se ainda não há vencedor
     */
    public int checkWin() {
        if (activeplayer == 1 && board.winnerBlack())
            return 1;
        else if (activeplayer == 0 && board.winnerWhite())
            return 0;
        return -1;
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
                if (p.getCol() == (position.col + 1) || p.getCol() == (position.col - 1))
                    value++;
            }

            int[] inimigos_na_coluna = new int[BOARDDIMENSION];

            for (Piece p : enemy_pieces) {
                inimigos_na_coluna[p.getCol()]++;
                if (p.getCol() == position.col)
                    value++;
            }

            value += inimigos_na_coluna[position.col];
            if (position.col - 1 > 0)
                value += inimigos_na_coluna[position.col - 1];
            if (position.col + 1 < BOARDDIMENSION)
                value += inimigos_na_coluna[position.col + 1];
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

            value -= inimigos_na_coluna[position.row];
        }

        if (DIFFICULTY > 2) {
            for (Piece p : player_pieces) {
                if (p.getRow() == (position.row + 1) || p.getRow() == (position.row - 1))
                    value++;
            }
            int[] inimigos_na_linha = new int[BOARDDIMENSION];

            for (Piece p : enemy_pieces) {
                inimigos_na_linha[p.getRow()]++;
                if (p.getRow() == position.row)
                    value++;
            }

            value += inimigos_na_linha[position.row];
            if (position.row - 1 > 0)
                value += inimigos_na_linha[position.row - 1];
            if (position.row + 1 < BOARDDIMENSION)
                value += inimigos_na_linha[position.row + 1];
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
                changePlayerAndDifficulty();
                Heur enemymove = miniMax(p);
                changePlayerAndDifficulty();

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
