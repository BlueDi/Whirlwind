package logic;

import java.util.LinkedList;
import java.util.Queue;

import util.Utility;

public class Board {
	private Piece[][] board = null;
	private boolean winwhite=false;
	private boolean winblack=false;

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
				board[row][col] = new Piece(row, col);	
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

	public Boolean getWinnerWhite(){
		return winwhite;
	}

	public Boolean getWinnerBlack(){
		return winblack;
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
		if(!checkFreePosition(p.getRow(), p.getCol()))
			return false;

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
	 * Verifica se o player Branco ganhou o jogo. 
	 * Procura a primeira peça Branca ao longo da linha 0 se não existir sabemos que é impossivel ter ganho, se encontrar usa o 
	 * {@link #auxwinnerWhite(int row, int col, boolean[][] a) auxwinnerWhite} para percorrer todas os locais à volta terminando com falso se não conseguir chegar ao outro extremo e mudando o estado do jogo para vitória se pelo contrário atingiu o outro extremo.
	 * @return true se fez a linha, false se não
	 */
	public Boolean winnerWhite() {
		boolean[][] visited = new boolean[board.length][board.length];
		winwhite = false;

		for(int i=0; i < board.length; i++)
			if(board[i][0].getPlayer() == 0 && auxwinnerWhite(i,0,visited))
				return winwhite;

		return false;
	}

	/**
	 * Neste caso para o white processa a posição atual [row][col] isto é termina com vitória se for o extremo certo associado ao jogador
	 * ,exemplo especifico,última coluna do tabuleiro, só tenta processar o local do tabuleiro se lá estiver uma peça do jogador e se ainda não tiver sido visitado
	 * @param row linha que está a tratar neste momento
	 * @param col coluna que está a ser tratado neste momento
	 * @param a array visited para perceber que posições foram testadas anteriormente e assim n se correr o risco de se repetir
	 * @return se é promising ou não através de bool, true se continuar, false se não for util continuar por este caminho
	 */
	public Boolean auxwinnerWhite(int row, int col, boolean[][] a) {
		if(winwhite)
			return true;

		if( col == board[board.length-1].length-1 
				&& board[row][col].getPlayer() == 0){
			winwhite = true;
			return true;
		}

		if(board[row][col].getPlayer() == 0
				&& !a[row][col]){
			a[row][col]=true;
			if(row+1 < board.length )
				auxwinnerWhite(row+1,col,a);
			if(col+1 < board.length)
				auxwinnerWhite(row,col+1,a);
			if(row-1 >= 0)
				auxwinnerWhite(row-1,col,a);
			if(col-1 >= 0)
				auxwinnerWhite(row,col-1,a);
		}

		return false;
	}

	/**
	 * Verifica se o player Preto ganhou o jogo.
	 * Procura a primeira peça Preta ao longo da linha 0, isto é o topo do tabuleiro se não existir sabemos que é impossivel ter ganho. 
	 * Se encontrar usa o {@link #auxwinnerBlack(int row, int col, boolean[][] a) auxwinnerBlack} para percorrer todos os locais à volta, 
	 * terminando com falso se não conseguir chegar ao outro extremo e mudando o estado do jogo para vitória se pelo contrário atingiu o outro extremo.
	 * @return true se fez a coluna, false se não
	 */
	public Boolean winnerBlack() {
		boolean[][] visited = new boolean[board.length][board.length];
		winblack = false;

		for(int i = 0; i < board.length; i++)
			if(board[0][i].getPlayer() == 1 && auxwinnerBlack(0,i,visited))
				return winblack;

		return false;
	}

	/**
	 * Neste caso para o Black processa a posição atual [row][col] isto é termina com vitória se for o extremo certo associado ao jogador, 
	 * exemplo especifico, última linha do tabuleiro, só tenta processar o local do tabuleiro se lá estiver uma peça do jogador e se ainda não tiver sido visitado
	 * @param row linha que está a tratar neste momento
	 * @param col coluna que está a ser tratado neste momento
	 * @param a array visited para perceber que posições foram testadas anteriormente e assim n se correr o risco de se repetir
	 * @return se é promising ou não através de bool, true se continuar, false se não for util continuar por este caminho
	 */
	public Boolean auxwinnerBlack(int row, int col, boolean[][] a) {
		if(winblack)
			return true;

		if( row == board[board.length-1].length-1 
				&& board[row][col].getPlayer() == 1){
			winblack = true;
			return true;
		}

		if(board[row][col].getPlayer() == 1
				&& !a[row][col]){
			a[row][col] = true;
			if(row+1 < board.length )
				auxwinnerBlack(row+1,col,a);
			if(col+1 < board.length)
				auxwinnerBlack(row,col+1,a);
			if(row-1 >= 0)
				auxwinnerBlack(row-1,col,a);
			if(col-1 >= 0)
				auxwinnerBlack(row,col-1,a);
		}
		
		return false;
	}
}
