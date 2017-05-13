package logic;

public class Piece {
    private int player = -1;
    private int row = -1;
    private int col = -1;

    /**
     * Construtor definindo o jogador
     *
     * @param row Linha onde colocar a pe�a
     * @param col Coluna onde colocar a pe�a
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
     * Modifica o jogador atribuido � pe�a.
     */
    void resetPlayer() {
        this.player = -1;
    }

    /**
     * Serve para saber de quem � a pe�a.
     *
     * @return Jogador a quem a pe�a pertence
     */
    int getPlayer() {
        return this.player;
    }

    /**
     * Modifica o jogador atribuido � pe�a.
     *
     * @param player pode ser 0 ou 1
     */
    void setPlayer(int player) {
        if (player == 0 || player == 1)
            this.player = player;
    }

    /**
     * Serve para saber a linha da pe�a.
     *
     * @return linha onde a pe�a est�
     */
    public int getRow() {
        return this.row;
    }

    /**
     * Serve para saber a coluna da pe�a.
     *
     * @return coluna onde a pe�a est�
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
