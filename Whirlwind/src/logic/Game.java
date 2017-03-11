package logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import util.Utility;

public class Game {
	private Board board;
	private int activeplayer = -1;
	private Heur best_move;
	private List<Heur> first_calc;

	/**
	 * Construtor da classe Game.
	 * @throws Exception
	 */
	public Game() throws Exception{
		int dimensao_do_tabuleiro = 12;
		board = new Board(dimensao_do_tabuleiro);
		activeplayer = 0; //black;
	}

	/**
	 * Turno do jogo.
	 */
	private void turn(){
		board.display();
		best_move = miniMax();
		//best_move.moveCalculator();
		board.setPiece(best_move.row, best_move.col, activeplayer);
		System.out.println("Jogador " + activeplayer + " tem: " + board.getPlayerPieces(activeplayer).size() + " peças para jogar.");
		activeplayer ^= 1;
	}

	/**
	 * Inicia o jogo.
	 * @throws InterruptedException
	 */
	public void startGame() throws InterruptedException{
		while(true){
			turn();
			Thread.sleep(900);//atrasar para ver a funcionar
		}
	}

	/**
	 * Compara uma jogada com a atual melhor jogada.
	 * @param move jogada a analisar
	 */
	private void updateBestMove(Heur move){
		if(move.value > best_move.value){
			best_move.value = move.value;
			best_move.row = move.row;
			best_move.col = move.col;
			best_move.movement = move.movement;
		}
	}

	/**
	 * TODO: Calcula o valor do tabuleiro com a nova peça.
	 * @param position A peça que vai ser colocada no turno
	 * @return int que representa o valor do tabuleiro
	 */
	private int calcHeur(Heur position){
		return Utility.random(1, 100); // = board.getValue() //heuristica
	}

	/**
	 * Calcula um movimento para a peça a analisar
	 * @param position
	 * @return Heur que representa a melhor peça para o turno
	 */
	private Heur singularMoveOfAPiece(Heur position){
		Heur position2 = new Heur();
		position2.row = position.row;
		position2.col = position.col;
		position2.movement = position.movement;

		position2.moveCalculator();

		if(board.setPiece(position2.row, position2.col, activeplayer)){
			position2.value = calcHeur(position);
			board.removePiece(position2.row, position2.col);
		}

		return position2;
	}

	/**
	 * Calcula todos os movimentos possíveis para a posição (row,col).
	 * @param row
	 * @param col
	 */
	private void movesForAPiece(int row, int col){
		for(int i = 0; i < 4; i++){		
			Heur position = new Heur();
			position.row = row;
			position.col = col;
			position.movement = i;
			Heur position2 = singularMoveOfAPiece(position);
			first_calc.add(position2);
			updateBestMove(position2);
		}
	}

	/**
	 * Algoritmo MiniMax.
	 * @return Melhor peça possível de se colocar no turno
	 */
	private Heur miniMax(){
		Queue<Piece> player_pieces = board.getPlayerPieces(activeplayer);
		Queue<Piece> temp_player_pieces = player_pieces;
		first_calc = new ArrayList<Heur>();
		best_move = new Heur();

		while(!temp_player_pieces.isEmpty()){
			/* TODO: calcular o valor do movimento e guardar em first_calc;
			 * first_calc[iterador] = heuristic(temp_plauer_pieces.first());
			 */
			// TODO: Monte-Carlo tree search talvez seja bom para nós

			Piece temp_piece = temp_player_pieces.remove();

			movesForAPiece(temp_piece.getRow(), temp_piece.getCol());

		}
		System.out.println("Melhor jogada para o jogador " + Utility.itop(activeplayer) +": (" + (best_move.row+1) +"," + Utility.itoc(best_move.col) + "), " + Utility.itoa(best_move.movement));
		return best_move;
	}
}
