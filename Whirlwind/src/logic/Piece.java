package logic;

public class Piece {
	private int player;

	/**
	 * Construtor de uma peça vazia.
	 * Serve para inicializar o tabuleiro.
	 */
	public Piece(){
		player = -1;
	}

	/**
	 * Construtor definindo o jogador
	 * @param player pode ser 0 ou 1
	 */
	public Piece(int player){
		this.player = player;
		if(this.player != 0 & this.player != 1)
			player = -1;
	}

	/**
	 * Modifica o jogador atribuido à peça. 
	 * @param player pode ser 0 ou 1
	 */
	public void setPlayer(int player) {
		if(player == 0 || player == 1)
			this.player = player;
	}

	/**
	 * Serve para saber de quem é a peça.
	 * @return Jogador a quem a peça pertence
	 */
	public int getPlayer(){
		return this.player;
	}

	/**
	 * Traduz o jogador para um simbolo. 
	 * @return Simbolo do jogador
	 */
	public char getSymbol(){
		switch(player){
		case 0:
			return 'O';
		case 1:
			return 'X';
		default:
			return '+';
		}
	}
}
