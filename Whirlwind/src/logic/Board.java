package logic;

public class Board {
	private Piece[][] board = null;

	/**
	 * @return the board
	 */
	public Piece[][] getBoard() {
		return board;
	}

	/**
	 * @param board the board to set
	 */
	public void setBoard(Piece[][] board) {
		this.board = board;
	}

	/**
	 * Construtor vazio.
	 * Apenas cria um board [n]x[n] sem peças.
	 */
	public Board(){
		int n = 14;
		int col, row;
		board = new Piece[n][n];
		for(row = 0; row < board.length; row++)
			for(col = 0; col < board.length; col++)
				board[row][col]=new Piece();	
	}

	/**
	 * Construtor de um board quadrado.
	 * O board é preenchido pela função auxiliar FillWithPieces().
	 * @param n tamanho do lado do tabuleiro
	 * @throws Exception se o board tiver uma dimensão muito pequena ou muito grande
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

		FillWithPieces();
	}

	/**
	 * Preenche o tabuleiro com peças. 
	 * O board é preenchido da seguinte maneira:	
	 * Coloca a primeira peça.
	 * Nessa linha coloca a cada 5 espaços uma nova peça, alternando o jogador, até não ter 5 espaços entre a peça e o fim do tabuleiro.
	 * Depois para a próxima linha coloca a primeira peça duas posições à direita da primeira peça, preenchendo antecipadamente as posições antes dessa peça. 
	 *
	 */
	private void FillWithPieces(){
		if(board == null)
			throw new NullPointerException("Board não inicializado.");

		int col, row;
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
	 * Retorna a peça na posição [row]x[col].
	 * @param row linha desejada
	 * @param col coluna desejada
	 * @return Piece na posição
	 */
	public Piece getPiece(int row, int col){
		try{
			return board[row][col];
		}
		catch(IndexOutOfBoundsException e){
			System.out.println("Bad coords in getPiece().");
		}
		return null;
	}

	/**
	 * Coloca uma peça do player na posição [row]x[col].
	 * @param row
	 * @param col
	 * @param player a quem a peça pertence
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
