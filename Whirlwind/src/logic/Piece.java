package logic;

public class Piece {
    private int player = -1;
    private int row = -1;
    private int col = -1;

    /**
     * Construtor definindo o jogador
     *
     * @param row Linha onde colocar a peça
     * @param col Coluna onde colocar a peça
     */
    Piece(int row, int col) {
        this.row = row;
        this.col = col;
    }

    /**
     * Construtor definindo o jogador
     *
     * @param player pode ser 0 ou 1
     */
    Piece(int row, int col, int player) {
        this(row, col);
        if (player == 0 || player == 1)
            this.player = player;
    }

    Piece(Heur h, int player) {
        row = h.row;
        col = h.col;
        if (player == 0 || player == 1)
            this.player = player;
    }

    /**
     * Modifica o jogador atribuido à peça.
     */
    void resetPlayer() {
        this.player = -1;
    }

    /**
     * Serve para saber de quem é a peça.
     *
     * @return Jogador a quem a peça pertence
     */
    int getPlayer() {
        return this.player;
    }

    /**
     * Modifica o jogador atribuido à peça.
     *
     * @param player pode ser 0 ou 1
     */
    void setPlayer(int player) {
        if (player == 0 || player == 1)
            this.player = player;
    }

    /**
     * Serve para saber a linha da peça.
     *
     * @return linha onde a peça está
     */
    public int getRow() {
        return this.row;
    }

    /**
     * Serve para saber a coluna da peça.
     *
     * @return coluna onde a peça está
     */
    public int getCol() {
        return this.col;
    }

    /**
     * Traduz o jogador para um simbolo.
     *
     * @return Simbolo do jogador
     */
    public char getSymbol() {
        switch (player) {
            case 0:
                return 'O';
            case 1:
                return 'X';
            default:
                return '+';
        }
    }
}
