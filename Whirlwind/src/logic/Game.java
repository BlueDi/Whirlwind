package logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import util.Utility;

public class Game {
	private Board board;
	private int activeplayer = -1;

	public Game() throws Exception{
		board = new Board(12);
		activeplayer = 0; //black;
	}

	private void moveCalculator(Heur h){
		switch (h.movement){
		case 0:
			h.row--;
			break;
		case 1:
			h.col++;
			break;
		case 2:
			h.row++;
			break;
		case 3:
			h.col--;
			break;
		default:
			break;
		}
	}

	private void turn(){
		board.display();
		Heur best_move = MiniMax();
		moveCalculator(best_move);
		board.setPiece(best_move.row, best_move.col, activeplayer);
		activeplayer ^= 1;
	}

	public void startGame() throws InterruptedException{
		while(true){
			turn();
			System.out.println("Jogador " + activeplayer + " tem: " + board.getPlayerPieces(activeplayer).size() + " peças para jogar.");
			Thread.sleep(1000);//atrasar para ver a funcionar
		}
	}

	class Heur
	{
		public int row, col; 
		public int movement;
		public int value;
	};

	public Heur MiniMax(){
		Queue<Piece> player_pieces = board.getPlayerPieces(activeplayer);
		Queue<Piece> temp_player_pieces = player_pieces;
		List<Heur> first_calc = new ArrayList<Heur>();
		Heur temp = new Heur();
		Heur best_move = new Heur();
		temp.col = 1;
		while(!temp_player_pieces.isEmpty()){
			/* TODO: calcular o valor do movimento e guardar em first_calc;
			 * first_calc[iterador] = heuristic(temp_plauer_pieces.first());
			 */
			// TODO: Monte-Carlo tree search talvez seja bom para nós

			Piece temp_piece = temp_player_pieces.remove();

			temp.row = temp_piece.getRow();
			temp.col = temp_piece.getCol();

			if(board.setPiece(temp.row-1, temp.col, activeplayer)){
				//up = 0
				temp.movement = 0;
				temp.value = Utility.random(1, 100); // = board.getValue() //heuristica
				first_calc.add(temp);
				if(temp.value>best_move.value){
					best_move.value = temp.value;
					best_move.col = temp.col;
					best_move.row = temp.row;
					best_move.movement = temp.movement;
				}
				board.removePiece(temp.row-1, temp.col);
			}

			if(board.setPiece(temp.row, temp.col+1, activeplayer)){
				//right = 1
				temp.movement = 1;
				temp.value = Utility.random(1, 100); // = board.getValue() //heuristica
				first_calc.add(temp);
				if(temp.value>best_move.value){
					best_move.value = temp.value;
					best_move.col = temp.col;
					best_move.row = temp.row;
					best_move.movement = temp.movement;
				}
				board.removePiece(temp.row, temp.col+1);
			}

			if(board.setPiece(temp.row+1, temp.col, activeplayer)){
				//down = 2
				temp.movement = 2;
				temp.value = Utility.random(1, 100); // = board.getValue() //heuristica
				first_calc.add(temp);
				if(temp.value>best_move.value){
					best_move.value = temp.value;
					best_move.col = temp.col;
					best_move.row = temp.row;
					best_move.movement = temp.movement;
				}
				board.removePiece(temp.row+1, temp.col);
			}

			if(board.setPiece(temp.row, temp.col-1, activeplayer)){
				//left = 3
				temp.movement = 3;
				temp.value = Utility.random(1, 100); // = board.getValue() //heuristica
				first_calc.add(temp);
				if(temp.value>best_move.value){
					best_move.value = temp.value;
					best_move.col = temp.col;
					best_move.row = temp.row;
					best_move.movement = temp.movement;
				}
				board.removePiece(temp.row, temp.col-1);
			}

		}
		System.out.println((best_move.row+1) + " " + (best_move.col+1));
		return best_move;
	}
}
