package logic;

public class Board {
	private Piece[][] board;

	/**O board está construido da seguinte maneira:	
	 * Coloca a primeira peça.
	 * Nessa linha coloca a cada 5 espaços uma nova peça, alternando o jogador, até não ter 5 espaços entre a peça e o fim do tabuleiro.
	 * Depois para a próxima linha coloca a primeira peça duas posições à direita da primeira peça, preenchendo antecipadamente as posições antes dessa peça. 
	 * 
	 * @param n tamanho do lado do tabuleiro
	 */
	public Board(int n){
		if(n<12) {
			System.out.println("incorrect option of cases");
			return;
		}

		int row, col;
		board = new Piece[n][n];
		for(col = 0; col < board.length; col++)
			for(row = 0; row < board.length; row++)
			board[col][row]=new Piece();	

		int line_start_position = 1; //posição da primeira peça da linha
		int player = 0;
		int row_position_checker = line_start_position, aux_pc;
		int row_color_picker = player, aux_cp=0;

		for(col = 0; col < board.length; col++){
			row_color_picker = player;
			row_position_checker = line_start_position;	
			
			//preencher o inicio da linha
			aux_pc = row_position_checker - 5;	
			aux_cp = row_color_picker;			
			while(aux_pc >= 0){
				aux_cp ^= 1;
				board[col][aux_pc].setPlayer(aux_cp);
				aux_pc -= 5;	
			}
			
			//preenchimento normal
			for(row = 0; row < board.length; row++){	
				if(row == row_position_checker){
					board[col][row].setPlayer(row_color_picker);
					row_position_checker += 5;

					row_color_picker ^= 1;
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
	 * Desenha o board na consola
	 */
	public void display(){
		for(int col = 0; col < board.length; col++){
			for(int row = 0; row < board.length; row++){
				System.out.print(board[col][row].getSymbol() + " ");
			}
			System.out.println();
		}
	}

}
