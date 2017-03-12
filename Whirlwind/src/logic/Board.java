package logic;

import java.util.LinkedList;
import java.util.Queue;

import util.Utility;

public class Board {
	private Piece[][] board = null;
	private boolean win=false;

	/**
	 * Construtor vazio.
	 * Apenas cria um board [n]x[n] sem peças.
	 * Hard-coded para criar [14]x[14]
	 */
	public Board(){
		int n = 14;
		int col;
		int row;
		board = new Piece[n][n];
		for(row = 0; row < board.length; row++)
			for(col = 0; col < board.length; col++)
				board[row][col]=new Piece(row, col);	
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

		int row;
		int col;
		board = new Piece[n][n];

		for(row = 0; row < board.length; row++)
			for(col = 0; col < board.length; col++)
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
	 * @param board the board to set
	 */
	public void setBoard(Piece[][] board) {
		this.board = board;
	}
	
	/**
	 * Devolve o tamanho do board
	 * @return tamanho do lado do tabuleiro
	 */
	public int getSize() {
		return board.length;
	}	
	
	/**
	 * Preenche o tabuleiro com peças. 
	 * O board é preenchido da seguinte maneira:	
	 * Coloca a primeira peça.
	 * Nessa linha coloca a cada 5 espaços uma nova peça, alternando o jogador, até não ter 5 espaços entre a peça e o fim do tabuleiro.
	 * Depois para a próxima linha coloca a primeira peça duas posições à direita da primeira peça, preenchendo antecipadamente as posições antes dessa peça. 
	 */
	private void fillWithPieces(){
		if(board == null)
			throw new IllegalArgumentException("Board não inicializado.");

		int col;
		int row;
		int line_start_position = 1; //posição da primeira peça da linha
		int player = 0;
		int col_position_checker = line_start_position;
		int aux_pc;
		int col_player_picker = player;
		int aux_pp = 0;

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
		System.out.print("    ");
		Utility.printLineOfChar(board.length);
		System.out.println();	
		System.out.print("   ");	

		Utility.printDashedLine(board.length);

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

		Utility.printDashedLine(board.length);

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
	 * @return true se a posição ainda não tiver peça, false se já tiver
	 */
	public Boolean checkFreePosition(int row, int col){
		try{
			if(board[row][col].getPlayer() == -1){
				return true;
			}
		}
		catch(IndexOutOfBoundsException e){
			//System.out.println("Bad coords in checkFreePosition().");
		}
		return false;
	}

	/**
	 * Compara o player dos argumentos com o player da peça.
	 * @param row
	 * @param col
	 * @param player
	 * @return true se forem o mesmo player, false se não forem ou se a posição não tiver peça
	 */
	public Boolean checkOwner(int row, int col, int player){
		//System.out.println("(" + (row+1) + "," + (col+1) + ") player: " + board[row][col].getPlayer());
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
	 * Verifica se existem peças do player nas posições à volta de (row,col).
	 * @param Piece
	 * @return true se exister pelo menos um movimento possível, false se não
	 */
	public Boolean checkHasMoves(Piece p){
		try{
			if((p.getRow() + 1) < board.length && checkOwner(p.getRow() + 1, p.getCol(), p.getPlayer()))
				return true;
			if((p.getRow() - 1) >= 0 && checkOwner(p.getRow() - 1, p.getCol(), p.getPlayer()))
				return true;
			if((p.getCol() + 1) < board.length && checkOwner(p.getRow(), p.getCol() + 1, p.getPlayer()))
				return true;
			if((p.getCol() - 1) >= 0 && checkOwner(p.getRow(), p.getCol() - 1, p.getPlayer()))
				return true;
		}
		catch(IndexOutOfBoundsException e){}

		return false;
	}

	/**
	 * Verifica se o movimento é válido.
	 * O movimento é válido quando a posição não está já ocupada e há uma peça do jogador numa casa adjacente à desejada, quer na horizontal, quer na vertical.
	 * @param Piece
	 * @return true se for válido, false se não for válido
	 */
	public Boolean checkValidMove(Piece p){
		//System.out.println("Trying to put a player " + player + " piece in (" + (row+1) + "," + Utility.itoc(col) + ").");

		if(!checkFreePosition(p.getRow(), p.getCol())){
			//System.out.println("Not valid. There is another piece in that position.");
			return false;
		}

		if(checkHasMoves(p))
			return true;

		System.out.println("Not valid. There isn't a player " + p.getPlayer() + " piece next to (" + (p.getRow()+1) + "," + Utility.itoc(p.getCol()) + ").");
		return false;
	}

	/**
	 * Retorna a peça na posição (row,col).
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
	 * Coloca uma peça do player na posição (row,col).
	 * Não depende das regras do jogo, apenas tem que estar dentro do tabuleiro.
	 * @param Piece
	 * @return true se conseguiu rowocar, false se não conseguiu
	 */
	public Boolean setPiece(Piece p){
		try{
			if(checkValidMove(p)){
				//System.out.println("Peca colocada em (" + (p.getRow()+1) +"," + Utility.itoc(p.getCol()) + ")");
				board[p.getRow()][p.getCol()].setPlayer(p.getPlayer());
				return true;
			}
		}
		catch(IndexOutOfBoundsException e){
			System.out.println("Bad coords in setPiece().");
		}
		return false;
	}

	/**
	 * Retira a peça do tabuleiro.
	 * Na prática apenas atribui à peça o jogador -1 que representa a ausência de jogador
	 * @param row
	 * @param col
	 */
	public void removePiece(int row, int col){
		try{
			board[row][col].resetPlayer();
			//System.out.println("Peca removida de (" + (row+1) +"," + Utility.itoc(col) + ")");
		}
		catch(IndexOutOfBoundsException e){
			System.out.println("Bad coords in removePiece().");
		}
	}

	/**
	 * Coloca uma peça do player na posição (row,col).
	 * Não depende das regras do jogo, apenas tem que ser uma posição livre.
	 * @param Piece
	 * @return true se conseguiu colocar, false se não conseguiu
	 */
	public Boolean setPieceAbs(Piece p){
		try{
			if(checkFreePosition(p.getRow(), p.getCol())){
				board[p.getRow()][p.getCol()].setPlayer(p.getPlayer());
				return true;
			}
		}
		catch(IndexOutOfBoundsException e){
			System.out.println("Bad coords in setPieceAbs().");
		}
		return false;
	}

	/**
	 * Devolve todas as peças do player presentes no board.
	 * @param player
	 * @return Queue<Piece> Fila de todas as peças do player
	 */
	public Queue<Piece> getPlayerPieces(int player){ 
		Queue<Piece> player_pieces = new LinkedList<Piece>();
		for(Piece[] col: board)
			for(Piece p: col)
				if(p.getPlayer() == player)
					player_pieces.add(p);
		return player_pieces;

	}

	/**
	 * Verifica se o player ganhou o jogo.
	 * @param player
	 * @return true se fez a linha, false se não
	 * @throws Exception jogador não válido
	 */
	public Boolean winnerWhite() {
		win=false;
		boolean[][]visited=new boolean[board.length][board.length];
		for(int k=0;k<visited.length;k++)
			for(int j=0;j<visited[k].length;j++)
				visited[k][j]=false;

		for(int i=0;i<board.length;i++){
			if(board[i][0].getPlayer()==1 && auxwinnerWhite(i,0,visited))
				return true;

		}
		return false;

	}

	public Boolean auxwinnerWhite(int row, int col, boolean[][] a) {

		if(win)
			return true;

		if( col == board[board.length-1].length-1 && board[row][col].getPlayer()==1){
			win=true;
			return true;
		}

		if(board[row][col].getPlayer()==1 && !a[row][col]){
			a[row][col]=true;
			if(row+1<board.length)
				auxwinnerWhite(row+1,col,a);
			if(col+1<board.length)
				auxwinnerWhite(row,col+1,a);
			if(row-1>=0)
				auxwinnerWhite(row-1,col,a);
			if(col-1>=0)
				auxwinnerWhite(row,col-1,a);

		}
		return false;
	}
}
