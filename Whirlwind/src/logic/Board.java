package logic;

public class Board {
	private Piece[][] board;

	/**
	 * O board está construido da seguinte maneira:	
	 * rowoca a primeira peça.
	 * Nessa linha rowoca a cada 5 espaços uma nova peça, alternando o jogador, até não ter 5 espaços entre a peça e o fim do tabuleiro.
	 * Depois para a próxima linha rowoca a primeira peça duas posições à direita da primeira peça, preenchendo antecipadamente as posições antes dessa peça. 
	 * 
	 * @param n tamanho do lado do tabuleiro
	 * @throws Exception se o board tiver uma dimensão muito pequena
	 */
	public Board(int n) throws Exception{
		if(n<12)
			throw new Exception("Board demasiado pequeno!");
		else if(n>20)
			throw new Exception("Board demasiado grande!");

		int col, row;
		board = new Piece[n][n];
		for(row = 0; row < board.length; row++)
			for(col = 0; col < board.length; col++)
				board[row][col]=new Piece();	

		int line_start_position = 1; //posição da primeira peça da linha
		int player = 0;
		int col_position_checker = line_start_position, aux_pc;
		int col_player_picker = player, aux_pp=0;

		for(row = 0; row < board.length; row++){
			col_player_picker = player;
			col_position_checker = line_start_position;	

			//preencher o inicio da linha
			aux_pc = col_position_checker - 5;	
			aux_pp = col_player_picker;			
			while(aux_pc >= 0){
				aux_pp ^= 1;
				board[row][aux_pc].setPlayer(aux_pp);
				aux_pc -= 5;	
			}

			//preenchimento normal
			for(col = 0; col < board.length; col++){	
				if(col == col_position_checker){
					board[row][col].setPlayer(col_player_picker);
					col_position_checker += 5;

					col_player_picker ^= 1;
				}
			}

			//preparar a próxima linha
			player ^= 1;
			line_start_position += 2;
			if(line_start_position > 13){
				line_start_position = 0;
				player ^= 1;
			}
		}
	}

	/**
	 * Desenha o tabuleiro com as peças na consola.
	 */
	public void display(){
		for(int row = 0; row < board.length; row++){
			for(int col = 0; col < board.length; col++){
				System.out.print(board[row][col].getSymbol() + " ");
			}
			System.out.println();
		}
		System.out.println();
	}

	/**
	 * Dá o tamanho das linhas.
	 * @return int que representa o número de colunas
	 */
	public int getRowSize(){
		return board[0].length;
	}

	/**
	 * Dá o tamanho das colunas.
	 * @return int que representa o número de linhas
	 */
	public int getColSize(){
		return board.length;
	}

	/**
	 * Calcula quantas peças de ambos os jogadores estão no tabuleiro.
	 * @return número de peças no tabuleiro
	 */
	public int getNumPieces(){
		int numPieces = 0;
		for(int row = 0; row < board.length; row++)
			for(int col = 0; col < board.length; col++)
				if(board[row][col].getPlayer() != -1)
					numPieces++;
		return numPieces;
	}

	/**
	 * Verifica se a posição não tem nenhuma peça.
	 * @param row
	 * @param col
	 * @return
	 */
	public Boolean checkFreePosition(int row, int col){
		try{
			if(board[row][col].getPlayer() == -1){
				return true;
			}
		}
		catch(IndexOutOfBoundsException e){
			System.out.println("Bad coords in checkFreePosition().");
		}
		return false;
	}

	/**
	 * rowoca uma peça do player na posição [row][col].
	 * @param row
	 * @param col
	 * @param player
	 * @return true se conseguiu rowocar, false se não conseguiu
	 */
	public Boolean setPiece(int row, int col, int player){
		try{
			if(checkFreePosition(row, col)){
				board[row][col].setPlayer(player);
				return true;
			}
		}
		catch(IndexOutOfBoundsException e){
			System.out.println("Bad coords in setPiece().");
		}
		return false;
	}
}
