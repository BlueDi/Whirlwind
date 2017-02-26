package logic;

import util.Utility;

public class Board {
	private Piece[][] board = null;

	/**
	 * Construtor vazio.
	 * Apenas cria um board [n]x[n] sem pe�as.
	 */
	public Board(){
		int n = 12;
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
		System.out.print("    ");
		char j;
		for (int i = 0; i < board.length; i++){
			j = Utility.itoc(i);
			System.out.print(j + " ");
		}
		System.out.println();	
		System.out.print("   ");	
		for (int i = 0; i < board.length; i++){
			j = Utility.itoc(i);
			System.out.print("--");
		}
		System.out.println("-");

		for(int row = 0; row < board.length; row++){
			if(row<9)
				System.out.print(row+1 +"  |");
			else{
				System.out.print(row+1 +" |");
			}

			for(int col = 0; col < board.length; col++){
				System.out.print(board[row][col].getSymbol() + " ");
			}
			System.out.println("|");
		}		
		System.out.print("   ");	
		for (int i = 0; i < board.length; i++){
			j = Utility.itoc(i);
			System.out.print("--");
		}
		System.out.println("-");
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
	 * Compara o player dos argumentos com o player da pe�a.
	 * @param row
	 * @param col
	 * @param player
	 * @return true se forem o mesmo player, false se n�o forem ou se a posi��o n�o tiver pe�a
	 */
	public Boolean checkOwner(int row, int col, int player){
		System.out.println("(" + (row+1) + "," + (col+1) + ") player: " + board[row][col].getPlayer());
		try{
			if(board[row][col].getPlayer() == player)
				return true;
		}
		catch(IndexOutOfBoundsException e){
			System.out.println("Bad coords in checkOwner().");
		}
		return false;
	}

	/**
	 * Verifica se o movimento � v�lido.
	 * O movimento � v�lido quando h� uma pe�a do jogador numa casa adjacente � desejada, quer na horizontal, quer na vertical.
	 * @param row
	 * @param col
	 * @param player
	 * @return true se for v�lido, false se n�o for v�lido
	 */
	public Boolean checkValidMove(int row, int col, int player){
		System.out.println("Trying to put a player " + player + " piece in (" + (row+1) + "," + Utility.itoc(col) + ").");

		if(!checkFreePosition(row, col)){
			System.out.println("Not valid. There is another piece in that position.");
			return false;
		}

		try{
			if((row+1) < board.length)
				if (checkOwner(row+1, col, player))
					return true;
			if((row-1) >= 0)
				if (checkOwner(row-1, col, player))
					return true;
			if((col+1) < board.length)
				if (checkOwner(row, col+1, player))
					return true;
			if((col-1) >= 0)
				if (checkOwner(row, col-1, player))
					return true;
		}
		catch(IndexOutOfBoundsException e){

		}

		System.out.println("Not valid. There isn't a player " + player + " piece next to (" + (row+1) + "," + Utility.itoc(col) + ").");
		return false;
	}

	/**
	 * Retorna a pe�a na posi��o (row,col).
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
	 * Coloca uma pe�a do player na posi��o (row,col).
	 * N�o depende das regras do jogo, apenas tem que estar dentro do tabuleiro.
	 * @param row
	 * @param col
	 * @param player a quem a pe�a pertence
	 * @return true se conseguiu rowocar, false se n�o conseguiu
	 */
	public Boolean setPiece(int row, int col, int player){
		try{
			if(checkValidMove(row, col, player)){
				board[row][col].setPlayer(player);
				return true;
			}
		}
		catch(IndexOutOfBoundsException e){
			System.out.println("Bad coords in setPiece().");
		}
		return false;
	}

	/**
	 * Coloca uma pe�a do player na posi��o (row,col).
	 * N�o depende das regras do jogo, apenas tem que ser uma posi��o livre.
	 * @param row
	 * @param col
	 * @param player a quem a pe�a pertence
	 * @return true se conseguiu rowocar, false se n�o conseguiu
	 */
	public Boolean setPieceAbs(int row, int col, int player){
		try{
			if(checkFreePosition(row, col)){
				board[row][col].setPlayer(player);
				return true;
			}
		}
		catch(IndexOutOfBoundsException e){
			System.out.println("Bad coords in setPieceAbs().");
		}
		return false;
	}

	/**
	 * Verifica se o player ganhou o jogo.
	 * @param player
	 * @return true se fez a linha, false se n�o
	 * @throws Exception jogador n�o v�lido
	 */
	public Boolean winner(int player) throws Exception{
		if(player != 0 && player != 1)
			throw new Exception("Invalid player!");
		return false;
	}
}
