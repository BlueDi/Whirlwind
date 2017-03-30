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
	 * Um board de dimens�o 14 garante que ambos jogadores come�am com 20 pe�as.
	 * @throws Exception
	 */
	public Game() throws Exception{
		int dimensao_do_tabuleiro = 14;
		board = new Board(dimensao_do_tabuleiro);
		activeplayer = 1; //black;
	}

	/**
	 * Turno do jogo.
	 */
	private void turn(){
		board.display();
		best_move = miniMax();
		Piece best_piece = new Piece(best_move.row, best_move.col, activeplayer);

		board.setPiece(best_piece);
		System.out.println("Jogador " + activeplayer + " tem: " + (board.getPlayerPieces(activeplayer).size()-1) + " pe�as para jogar.");
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
			board.winnerWhite();
			board.winnerBlack();
			if(board.getWinnerBlack()){
				System.out.println("black won");
				break;
				}
			if(board.getWinnerWhite()){
				System.out.println("white won");
				break;
				}
		
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
	 * Calcula o valor do tabuleiro para o jogador branco.
	 * @param position
	 * @return valor do tabuleiro
	 */
	private int calcWhiteHeur(Heur position){
		int value = 0;
		Queue<Piece> player_pieces = board.getPlayerPieces(activeplayer);
		Queue<Piece> enemy_pieces = board.getPlayerPieces(activeplayer ^ 1);
		
		for(Piece p: player_pieces){
			if(p.getCol()==position.col)
				value++;
		}	

		int[] inimigos_na_linha = new int[board.getSize()];
		
		for(Piece p: enemy_pieces){
			inimigos_na_linha[p.getRow()]++;
			if(p.getCol()==position.col)
				value--;
		}
		value += inimigos_na_linha[position.row];
		
		return value;
	}
	
	/**
	 * Calcula o valor do tabuleiro para o jogador preto.
	 * @param position
	 * @return valor do tabuleiro
	 */
	private int calcBlackHeur(Heur position){
		int value = 0;
		Queue<Piece> player_pieces = board.getPlayerPieces(activeplayer);
		Queue<Piece> enemy_pieces = board.getPlayerPieces(activeplayer ^ 1);
		
		for(Piece p: player_pieces){
			if(p.getRow()==position.row)
				value++;
		}	

		int[] inimigos_na_linha = new int[board.getSize()];
		
		for(Piece p: enemy_pieces){
			inimigos_na_linha[p.getCol()]++;
			if(p.getRow()==position.col)
				value--;
		}
		value += inimigos_na_linha[position.row];
		
		return value;
	}

	/**
	 * TODO: Calcula o valor do tabuleiro com a nova pe�a.
	 * @param position A pe�a que vai ser colocada no turno
	 */
	private void calcHeur(Heur position){
		int value = 0;

		if(activeplayer == 0)
			value += calcWhiteHeur(position);
		else
			value += calcBlackHeur(position);
			

		position.value = value;//= Utility.random(1, 100);
	}

	/**
	 * Calcula um movimento para a pe�a a analisar
	 * @param position
	 * @return Heur que representa a melhor pe�a para o turno
	 */
	private Heur singularMoveOfAPiece(Heur position){
		Heur position2 = new Heur();
		position2.row = position.row;
		position2.col = position.col;
		position2.movement = position.movement;

		position2.moveCalculator();

		Piece p = new Piece(position2.row, position2.col, activeplayer);

		if(board.setPiece(p)){
			calcHeur(position2);
			board.removePiece(position2.row, position2.col);
		}

		return position2;
	}

	/**
	 * Calcula todos os movimentos poss�veis para a posi��o (row,col).
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
	 * @return Melhor pe�a poss�vel de se colocar no turno
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
			// TODO: Monte-Carlo tree search talvez seja bom para n�s

			Piece temp_piece = temp_player_pieces.remove();

			movesForAPiece(temp_piece.getRow(), temp_piece.getCol());

		}
		System.out.println("Melhor jogada para o jogador " + Utility.itop(activeplayer) +": (" + (best_move.row+1) +"," + Utility.itoc(best_move.col) + "), " + Utility.itoa(best_move.movement));
		return best_move;
	}
}
