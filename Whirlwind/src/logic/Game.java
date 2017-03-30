package logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import util.Utility;

public class Game {
	private Board board;
	private int activeplayer = -1;
	private List<Heur> first_calc;
	private int depth;

	/**
	 * Construtor da classe Game.
	 * Um board de dimensão 14 garante que ambos jogadores começam com 20 peças.
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
		depth = 3;
		board.display();
		Heur best_move = miniMax();
		Piece best_piece = new Piece(best_move.row, best_move.col, activeplayer);

		board.setPiece(best_piece);
		System.out.println("Jogador " + activeplayer + " tem: " + (board.getPlayerPieces(activeplayer).size()-1) + " peças para jogar.");
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
	private void updateBestMove(Heur currentBestMove, Heur newMove){
		if(newMove.value > currentBestMove.value){
			currentBestMove.value = newMove.value;
			currentBestMove.row = newMove.row;
			currentBestMove.col = newMove.col;
			currentBestMove.movement = newMove.movement;
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
	 * TODO: Calcula o valor do tabuleiro com a nova peça.
	 * @param position A peça que vai ser colocada no turno
	 */
	private void calcHeur(Heur position){
		int value = 0;

		if(activeplayer == 0)
			value += calcWhiteHeur(position);
		else
			value += calcBlackHeur(position);

		position.value = value;
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

		Piece p = new Piece(position2.row, position2.col, activeplayer);

		if(board.setPiece(p)){
			calcHeur(position2);

			if(depth > 0){
				activeplayer ^= 1;
				Heur enemymove = miniMax();
				activeplayer ^= 1;

				position.value -= enemymove.value;
			}

			board.removePiece(position2.row, position2.col);
		}

		return position2;
	}

	/**
	 * Calcula todos os movimentos possíveis para a posição (row,col).
	 * @param row
	 * @param col
	 */
	private void movesForAPiece(Heur bestMove, int row, int col){
		for(int i = 0; i < 4; i++){		
			Heur position = new Heur();
			position.row = row;
			position.col = col;
			position.movement = i;
			Heur position2 = singularMoveOfAPiece(position);
			//first_calc.add(position2);



			updateBestMove(bestMove, position2);
		}
	}

	/**
	 * Algoritmo MiniMax.
	 * @return Melhor peça possível de se colocar no turno
	 */
	private Heur miniMax(){
		depth--;
		Heur bestMove = new Heur();

		Queue<Piece> playerPieces = board.getPlayerPieces(activeplayer);
		Queue<Piece> tempPlayerPieces = playerPieces;
		first_calc = new ArrayList<Heur>();

		while(!tempPlayerPieces.isEmpty()){
			/* TODO: calcular o valor do movimento e guardar em first_calc;
			 * first_calc[iterador] = heuristic(temp_plauer_pieces.first());
			 */
			// TODO: Monte-Carlo tree search talvez seja bom para nós

			Piece tempPiece = tempPlayerPieces.remove();

			movesForAPiece(bestMove, tempPiece.getRow(), tempPiece.getCol());
		}

		System.out.println("Melhor jogada para o jogador " + Utility.itop(activeplayer) +": (" + (bestMove.row+1) +"," + Utility.itoc(bestMove.col) + "), " + Utility.itoa(bestMove.movement));

		return bestMove;
	}
}
