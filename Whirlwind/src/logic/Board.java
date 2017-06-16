package logic;

import util.Utility;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Board {
    private Piece[][] board = null;
    private boolean winWhite = false;
    private boolean winBlack = false;
    private boolean[][] visited = null;
    private int FIRST_PIECE = 0;
    private int PLAYER_BLACK = 1;
    private int PLAYER_WHITE = 0;

    /**
     * Construtor vazio. Apenas cria um board [n]x[n] sem peças. Hard-coded para
     * criar [14]x[14]
     */
    Board() {
        int n = 14;
        int col;
        int row;
        board = new Piece[n][n];

        for (row = 0; row < board.length; row++)
            for (col = 0; col < board.length; col++)
                board[row][col] = new Piece(row, col);
    }

    /**
     * Construtor de um board quadrado. O board é preenchido pela função
     * auxiliar FillWithPieces().
     *
     * @param n tamanho do lado do tabuleiro
     * @throws IndexOutOfBoundsException se o board tiver uma dimensão muito pequena ou muito grande
     */
    Board(int n, int boardPicker) throws IndexOutOfBoundsException {
        if (n < 12)
            throw new IndexOutOfBoundsException("Board demasiado pequeno!");
        else if (n > 20)
            throw new IndexOutOfBoundsException("Board demasiado grande!");

        FIRST_PIECE = boardPicker;

        int row;
        int col;
        board = new Piece[n][n];

        for (row = 0; row < board.length; row++)
            for (col = 0; col < board.length; col++)
                board[row][col] = new Piece(row, col);

        fillWithPieces();
    }

    /**
     * @return the board
     */
    public Piece[][] getBoard() {
        return board;
    }

    /**
     * Devolve o tamanho do board
     *
     * @return tamanho do lado do tabuleiro
     */
    public int getSize() {
        return board.length;
    }

    /**
     * Preenche o tabuleiro com peças. O board é preenchido da seguinte maneira:
     * Coloca a primeira peça. Nessa linha coloca a cada 5 espaços uma nova
     * peça, alternando o jogador, até não ter 5 espaços entre a peça e o fim do
     * tabuleiro. Depois para a próxima linha coloca a primeira peça duas
     * posições à direita da primeira peça, preenchendo antecipadamente as
     * posições antes dessa peça.
     */
    private void fillWithPieces() {
        int col;
        int row;
        int line_start_position = 1; // posição da primeira peça da linha
        int player = FIRST_PIECE;
        int col_position_checker;
        int aux_pc;
        int col_player_picker;
        int aux_pp;

        for (row = 0; row < board.length; row++) {
            col_player_picker = player;
            col_position_checker = line_start_position;

            // preencher o inicio da linha
            aux_pc = col_position_checker - 5;
            aux_pp = col_player_picker;
            while (aux_pc >= 0) {
                aux_pp ^= 1;
                board[row][aux_pc].setPlayer(aux_pp);
                aux_pc -= 5;
            }

            // preenchimento normal
            for (col = 0; col < board.length; col++) {
                if (col == col_position_checker) {
                    board[row][col].setPlayer(col_player_picker);
                    col_position_checker += 5;

                    col_player_picker ^= 1;
                }
            }

            // preparar a próxima linha
            player ^= 1;
            line_start_position += 2;
            if (line_start_position > 13) {
                line_start_position = 0;
                player ^= 1;
            }
        }
    }

    /**
     * Desenha o tabuleiro com as peças na consola. Primeiro desenha a linha de
     * caracteres que representam as possíveis colunas. Depois para cada linha,
     * imprime o caracter que a representa, dependendo se é maior que 9 ou não,
     * e depois o conteúdo da linha.
     */
    void display() {
        System.out.print("    ");
        Utility.printLineOfChar(board.length);
        System.out.println();
        System.out.print("   ");

        Utility.printDashedLine(board.length);

        for (int row = 0; row < board.length; row++) {
            if (row < 9)
                System.out.print(row + 1 + "  |");
            else
                System.out.print(row + 1 + " |");

            for (int col = 0; col < board.length; col++)
                System.out.print(board[row][col].getSymbol() + " ");

            System.out.println("|");
        }
        System.out.print("   ");

        Utility.printDashedLine(board.length);

        System.out.println();
    }

    /**
     * Calcula quantas peças de ambos os jogadores estão no tabuleiro.
     *
     * @return número de peças no tabuleiro
     */
    public int getNumPieces() {
        int numPieces = 0;
        for (Piece[] aBoard : board)
            for (int col = 0; col < board.length; col++)
                if (aBoard[col].getPlayer() != -1)
                    numPieces++;
        return numPieces;
    }

    /**
     * Verifica se a posição não tem nenhuma peça.
     *
     * @param row Linha da posição a verificar
     * @param col Coluna da posição a verificar
     * @return true se a posição ainda não tiver peça, false se já tiver
     */
    Boolean checkFreePosition(int row, int col) {
        try {
            if (board[row][col].getPlayer() == -1) {
                return true;
            }
        } catch (IndexOutOfBoundsException e) {
            // System.err.println("Bad coords in checkFreePosition().");
        }
        return false;
    }

    /**
     * Compara o player dos argumentos com o player da peça.
     *
     * @param row    Linha da posição a verificar
     * @param col    Coluna da posição a verificar
     * @param player Jogador a comparar com o dono da peça
     * @return true se forem o mesmo player, false se não forem ou se a posição
     * não tiver peça
     */
    private Boolean checkOwner(int row, int col, int player) {
        try {
            return board[row][col].getPlayer() == player;
        } catch (IndexOutOfBoundsException e) {
            System.err.println("Bad coords in checkOwner().");
        }
        return false;
    }

    /**
     * Verifica se existem peças do player nas posições à volta de (row,col).
     *
     * @param p Peça que se vai verificar se tem peças à volta
     * @return true se exister pelo menos um movimento possível, false se não
     */
    private Boolean checkHasPlayerNext(Piece p) {
        if ((p.getRow() + 1) < board.length && checkOwner(p.getRow() + 1, p.getCol(), p.getPlayer()))
            return true;
        if ((p.getRow() - 1) >= 0 && checkOwner(p.getRow() - 1, p.getCol(), p.getPlayer()))
            return true;
        if ((p.getCol() + 1) < board.length && checkOwner(p.getRow(), p.getCol() + 1, p.getPlayer()))
            return true;
        if ((p.getCol() - 1) >= 0 && checkOwner(p.getRow(), p.getCol() - 1, p.getPlayer()))
            return true;

        return false;
    }

    /**
     * Verifica se existem posições livres nas posições à volta de (row,col).
     *
     * @param p Peça que se vai verificar se tem posições livres à volta
     * @return true se exister pelo menos um movimento possível, false se não
     */
    private Boolean checkHasEmptyNext(Piece p) {
        if ((p.getRow() + 1) < board.length && checkFreePosition(p.getRow() + 1, p.getCol()))
            return true;
        if ((p.getRow() - 1) >= 0 && checkFreePosition(p.getRow() - 1, p.getCol()))
            return true;
        if ((p.getCol() + 1) < board.length && checkFreePosition(p.getRow(), p.getCol() + 1))
            return true;
        if ((p.getCol() - 1) >= 0 && checkFreePosition(p.getRow(), p.getCol() - 1))
            return true;

        return false;
    }

    /**
     * Verifica se o movimento é válido. O movimento é válido quando a posição
     * não está já ocupada e há uma peça do jogador numa casa adjacente à
     * desejada, quer na horizontal, quer na vertical.
     *
     * @param p Peça a ser verificada
     * @return true se for válido, false se não for válido
     */
    Boolean checkValidMove(Piece p) {
        if (!checkFreePosition(p.getRow(), p.getCol()))
            return false;

        if (checkHasPlayerNext(p))
            return true;

        //System.err.println("Not valid. There isn't a player " + Utility.itop(p.getPlayer()) + " piece next to (" + (p.getRow() + 1) + "," + Utility.itoc(p.getCol()) + ").");
        return false;
    }

    /**
     * Retorna a peça na posição (row,col).
     *
     * @param row linha desejada
     * @param col coluna desejada
     * @return Piece na posição
     */
    Piece getPiece(int row, int col) {
        try {
            return board[row][col];
        } catch (IndexOutOfBoundsException e) {
            System.err.println("Bad coords in getPiece().");
        }
        return null;
    }

    /**
     * Coloca uma peça do player na posição (row,col). Não depende das regras do jogo, apenas tem que estar dentro do tabuleiro.
     *
     * @param p Peça a colocar
     * @return true se conseguiu colocar, false se não conseguiu
     */
    Boolean setPiece(Piece p) {
        try {
            if (checkValidMove(p)) {
                //System.out.println("Peca colocada em (" + (p.getRow() + 1) + "," + Utility.itoc(p.getCol()) + ")");
                board[p.getRow()][p.getCol()].setPlayer(p.getPlayer());
                return true;
            }
        } catch (IndexOutOfBoundsException e) {
            System.err.println("Bad coords in setPiece().");
        }
        return false;
    }

    /**
     * Retira a peça do tabuleiro. Na prática apenas atribui à peça o jogador -1 que representa a ausência de jogador
     *
     * @param row Linha da peça a remover
     * @param col Coluna da peça a remover
     */
    void removePiece(int row, int col) {
        try {
            board[row][col].resetPlayer();
            //System.out.println("Peca removida de (" + (row + 1) + "," + Utility.itoc(col) + ")");
        } catch (IndexOutOfBoundsException e) {
            //System.err.println("Bad coords in removePiece().");
        }
    }

    /**
     * Coloca uma peça do player na posição (row,col). Não depende das regras do jogo, apenas tem que ser uma posição livre.
     *
     * @param p Peça a colocar
     * @return true se conseguiu colocar, false se não conseguiu
     */
    Boolean setPieceAbs(Piece p) {
        try {
            if (checkFreePosition(p.getRow(), p.getCol())) {
                board[p.getRow()][p.getCol()].setPlayer(p.getPlayer());
                return true;
            }
        } catch (IndexOutOfBoundsException e) {
            System.err.println("Bad coords in setPieceAbs().");
        }
        return false;
    }

    /**
     * Devolve todas as peças do player presentes no board.
     *
     * @param player Jogador de que se quer obter as peças
     * @return Queue<Piece> Fila de todas as peças do player
     */
    Queue<Piece> getPlayerPieces(int player) {
        Queue<Piece> player_pieces = new LinkedList<>();

        for (Piece[] col : board)
            for (Piece p : col)
                if (p.getPlayer() == player)
                    player_pieces.add(p);

        return player_pieces;
    }

    /**
     * Devolve todas as peças livres no board.
     *
     * @return Queue<Piece> Fila de todas as peças do player
     */
    private Queue<Piece> getFreePieces() {
        Queue<Piece> free_pieces = new LinkedList<>();

        for (Piece[] col : board)
            for (Piece p : col)
                if (p.getPlayer() == -1)
                    free_pieces.add(p);

        return free_pieces;
    }

    /**
     * Devolve um Array List com todas as posições em que o jogador pode colocar peças.
     *
     * @param player Jogador a obter as posições
     * @return Array List das posições livres e válidas
     */
    ArrayList<Piece> getFreeValidPieces(int player) {
        Queue<Piece> free_pieces = getFreePieces();
        ArrayList<Piece> free_valid_pieces = new ArrayList<>();

        while (!free_pieces.isEmpty()) {
            Piece p = free_pieces.remove();
            p.setPlayer(player);
            if (checkHasPlayerNext(p)) {
                p.resetPlayer();
                free_valid_pieces.add(p);
            }
            p.resetPlayer();
        }

        return free_valid_pieces;
    }

    /**
     * Devolve todas as peças do player presentes no board que têm posições livres ortogonalmente.
     *
     * @param player Jogador de que se quer obter as peças
     * @return Queue<Piece> Fila de todas as peças do player com posições livres
     */
    Queue<Piece> getPlayerPiecesWithPossibleMovements(int player) {
        Queue<Piece> player_pieces_with_movements = new LinkedList<>();
        Queue<Piece> player_pieces = getPlayerPieces(player);

        while (!player_pieces.isEmpty()) {
            Piece piece_to_check = player_pieces.remove();

            if (checkHasEmptyNext(piece_to_check))
                player_pieces_with_movements.add(piece_to_check);
        }

        return player_pieces_with_movements;
    }

    /**
     * Verifica se o player Branco ganhou o jogo. Procura a primeira peça Branca
     * ao longo da linha 0 se não existir sabemos que é impossivel ter ganho, se
     * encontrar usa o {@link #auxwinnerWhite(int row, int col)
     * auxwinnerWhite} para percorrer todas os locais à volta terminando com
     * falso se não conseguir chegar ao outro extremo e mudando o estado do jogo
     * para vitória se pelo contrário atingiu o outro extremo.
     *
     * @return true se fez a linha, false se não
     */
    Boolean winnerWhite() {
        visited = new boolean[board.length][board.length];
        winWhite = false;

        for (int row = 0; row < board.length; row++)
            if (board[row][0].getPlayer() == PLAYER_WHITE && auxwinnerWhite(row, 0) && winWhite)
                return true;

        return false;
    }

    /**
     * Neste caso para o white processa a posição atual [row][col] isto é
     * termina com vitória se for o extremo certo associado ao jogador ,exemplo
     * especifico,última coluna do tabuleiro, só tenta processar o local do
     * tabuleiro se lá estiver uma peça do jogador e se ainda não tiver sido
     * visitado. As posições já visitadas são guardadas em visited.
     *
     * @param row linha que está a tratar neste momento
     * @param col coluna que está a ser tratado neste momento
     * @return se é promising ou não através de bool, true se continuar, false se não for util continuar por este caminho
     */
    private Boolean auxwinnerWhite(int row, int col) {
        if (winWhite)
            return true;

        if (col == getSize() - 1 && board[row][col].getPlayer() == PLAYER_WHITE) {
            winWhite = true;
            return true;
        }

        if (board[row][col].getPlayer() == PLAYER_WHITE && !visited[row][col]) {
            visited[row][col] = true;

            // Verificar posições ortogonais
            if (auxwinnerWhiteOrtogonal(row, col) || auxwinnerWhiteDiagonal(row, col))
                return true;
        }

        return false;
    }

    private Boolean auxwinnerWhiteOrtogonal(int row, int col) {
        if (row + 1 < board.length && auxwinnerWhite(row + 1, col))
            return true;
        else if (col + 1 < board.length && auxwinnerWhite(row, col + 1))
            return true;
        else if (row - 1 >= 0 && auxwinnerWhite(row - 1, col))
            return true;
        else if (col - 1 >= 0 && auxwinnerWhite(row, col - 1))
            return true;

        return false;
    }

    private Boolean auxwinnerWhiteDiagonal(int row, int col) {
        if (row + 1 < board.length && col + 1 < board.length && auxwinnerWhite(row + 1, col + 1))
            return true;
        else if (row + 1 < board.length && col - 1 >= 0 && auxwinnerWhite(row + 1, col - 1))
            return true;
        else if (row - 1 >= 0 && col + 1 < board.length && auxwinnerWhite(row - 1, col + 1))
            return true;
        else if (row - 1 >= 0 && col - 1 >= 0 && auxwinnerWhite(row - 1, col - 1))
            return true;

        return false;
    }

    /**
     * Verifica se o player Preto ganhou o jogo. Procura a primeira peça Preta
     * ao longo da linha 0, isto é o topo do tabuleiro se não existir sabemos
     * que é impossivel ter ganho. Se encontrar usa o
     * {@link #auxwinnerBlack(int row, int col) auxwinnerBlack}
     * para percorrer todos os locais à volta, terminando com falso se não
     * conseguir chegar ao outro extremo e mudando o estado do jogo para vitória
     * se pelo contrário atingiu o outro extremo.
     *
     * @return true se fez a coluna, false se não
     */
    Boolean winnerBlack() {
        visited = new boolean[board.length][board.length];
        winBlack = false;

        for (int i = 0; i < board.length; i++)
            if (board[0][i].getPlayer() == PLAYER_BLACK && auxwinnerBlack(0, i) && winBlack) {
                return true;
            }

        return false;
    }

    /**
     * Neste caso para o Black processa a posição atual [row][col] isto é
     * termina com vitória se for o extremo certo associado ao jogador, exemplo
     * especifico, última linha do tabuleiro, só tenta processar o local do
     * tabuleiro se lá estiver uma peça do jogador e se ainda não tiver sido
     * visitado. As posições já visitadas são guardadas em visited.
     *
     * @param row linha que está a tratar neste momento
     * @param col coluna que está a ser tratado neste momento
     * @return se é promising ou não através de bool, true se continuar, false se não for util continuar por este caminho
     */
    private Boolean auxwinnerBlack(int row, int col) {
        if (winBlack)
            return true;

        if (row == getSize() - 1 && board[row][col].getPlayer() == PLAYER_BLACK) {
            winBlack = true;
            return true;
        }

        if (board[row][col].getPlayer() == PLAYER_BLACK && !visited[row][col]) {
            visited[row][col] = true;

            if (auxwinnerBlackOrtogonal(row, col) || auxwinnerBlackDiagonal(row, col))
                return true;
        }

        return false;
    }

    private Boolean auxwinnerBlackOrtogonal(int row, int col) {
        if (row + 1 < board.length && auxwinnerBlack(row + 1, col))
            return true;
        else if (col + 1 < board.length && auxwinnerBlack(row, col + 1))
            return true;
        else if (row - 1 >= 0 && auxwinnerBlack(row - 1, col))
            return true;
        else if (col - 1 >= 0 && auxwinnerBlack(row, col - 1))
            return true;

        return false;
    }

    private Boolean auxwinnerBlackDiagonal(int row, int col) {
        if (row + 1 < board.length && col + 1 < board.length && auxwinnerBlack(row + 1, col + 1))
            return true;
        else if (row + 1 < board.length && col - 1 >= 0 && auxwinnerBlack(row + 1, col - 1))
            return true;
        else if (row - 1 >= 0 && col + 1 < board.length && auxwinnerBlack(row - 1, col + 1))
            return true;
        else if (row - 1 >= 0 && col - 1 >= 0 && auxwinnerBlack(row - 1, col - 1))
            return true;

        return false;
    }
}
