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
	 * Apenas cria um board [n]x[n] sem pe�as.
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
	 * O board � preenchido pela fun��o auxiliar FillWithPieces().
	 * @param n tamanho do lado do tabuleiro
	 * @throws Exception se o board tiver uma dimens�o muito pequena ou muito grande
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
	 * Preenche o tabuleiro com pe�as. 
	 * O board � preenchido da seguinte maneira:	
	 * Coloca a primeira pe�a.
	 * Nessa linha coloca a cada 5 espa�os uma nova pe�a, alternando o jogador, at� n�o ter 5 espa�os entre a pe�a e o fim do tabuleiro.
	 * Depois para a pr�xima linha coloca a primeira pe�a duas posi��es � direita da primeira pe�a, preenchendo antecipadamente as posi��es antes dessa pe�a. 
	 *
	 */
	private void FillWithPieces(){
		if(board == null)
			throw new NullPointerException("Board n�o inicializado.");

		int col, row;
		int line_start_position = 1; //posi��o da primeira pe�a da linha
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

			//preparar a pr�xima linha
			player ^= 1;
			line_start_position += 2;
			if(line_start_position > 13){
				line_start_position = 0;
				player ^= 1;
			}
		}
	}

	/**
	 * Desenha o tabuleiro com as pe�as na consola.
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
	 * Calcula quantas pe�as de ambos os jogadores est�o no tabuleiro.
	 * @return n�mero de pe�as no tabuleiro
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
	 * Verifica se a posi��o n�o tem nenhuma pe�a.
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
	 * Retorna a pe�a na posi��o [row]x[col].
	 * @param row linha desejada
	 * @param col coluna desejada
	 * @return Piece na posi��o
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
	 * Coloca uma pe�a do player na posi��o [row]x[col].
	 * @param row
	 * @param col
	 * @param player a quem a pe�a pertence
	 * @return true se conseguiu rowocar, false se n�o conseguiu
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
