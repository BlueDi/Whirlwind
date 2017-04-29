package logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

import cli.GameFrame;
import util.Utility;

public class Game {
	private Board board;
	private GameFrame gameframe;
	private int GAMEMODE;
	private int activeplayer = -1;
	private List<Heur> first_calc;
	private int depth;

	/**
	 * Construtor da classe Game. Um board de dimensão 14 garante que ambos
	 * jogadores começam com 20 peças.
	 * 
	 * @throws Exception
	 */
	public Game() throws Exception {
		int dimensao_do_tabuleiro = 14;
		board = new Board(dimensao_do_tabuleiro);
		gameframe = new GameFrame(board);
		gameframe.setVisible(true);
		activeplayer = 1; // black;
	}

	public Game(int mode) throws Exception {
		this();
		GAMEMODE = mode;
	}

	public void setGAMEMODE(int gAMEMODE) {
		GAMEMODE = gAMEMODE;
	}

	/**
	 * Turno do jogo.
	 */
	private void turn() {
		board.display();
		gameframe.update(board);
		gameframe.setVisible(true);

		Piece move;

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		if (GAMEMODE == 1)
			move = turnPlayer();
		else if (GAMEMODE == 2 && activeplayer == 1)
			move = turnPlayer();
		else
			move = turnCPU();

		board.setPiece(move);
		System.out.println("Jogador " + activeplayer + " tem: " + (board.getPlayerPieces(activeplayer).size() - 1)
				+ " peças no tabuleiro.");
		changePlayer();
	}

	/**
	 * Turno de jogo do Computador.
	 */
	private Piece turnCPU() {
		depth = 3;
		Heur bestMove = miniMax();
		Piece bestPiece = new Piece(bestMove.row, bestMove.col, activeplayer);

		return bestPiece;
	}

	/**
	 * Turno de jogo do Jogador.
	 */
	private Piece turnPlayer() {
		Piece playerPiece = new Piece();
		Scanner reader = new Scanner(System.in);
		int row = -1;
		int col = -1;

		do {
			row = reader.nextInt() - 1;
			String column = reader.next();
			col = column.charAt(0) - 'a';

			if (row < 0 || row >= board.getSize() || col < 0 || col >= board.getSize())
				System.out.println("Try again. Position (" + (row + 1) + "," + column.charAt(0) + ") is not valid.");

			playerPiece = new Piece(row, col, activeplayer);
		} while (!board.checkValidMove(playerPiece));

		return playerPiece;
	}

	/**
	 * Inicia o jogo.
	 * 
	 * @throws InterruptedException
	 */
	public void startGame() throws InterruptedException {
		int turnId = 0;

		while (true) {
			System.out.println("\nTurn " + turnId++);
			turn();

			if (board.winnerBlack()) {
				Thread.sleep(9000);
				System.out.println("Black Won!");
				break;
			} else if (board.winnerWhite()) {
				Thread.sleep(9000);
				System.out.println("White Won!");
				break;
			}

		}
	}

	private void changePlayer() {
		activeplayer ^= 1;
	}

	/**
	 * Compara uma jogada com a atual melhor jogada.
	 * 
	 * @param move
	 *            jogada a analisar
	 */
	private void updateBestMove(Heur currentBestMove, Heur newMove) {
		if (newMove.value > currentBestMove.value) {
			currentBestMove.value = newMove.value;
			currentBestMove.row = newMove.row;
			currentBestMove.col = newMove.col;
			currentBestMove.movement = newMove.movement;
		}
	}

	/**
	 * Calcula o valor do tabuleiro para o jogador branco.
	 * 
	 * @param position
	 * @return valor do tabuleiro
	 */
	private int calcWhiteHeur(Heur position) {
		int value = 0;
		Queue<Piece> player_pieces = board.getPlayerPieces(activeplayer);
		Queue<Piece> enemy_pieces = board.getPlayerPieces(activeplayer ^ 1);

		for (Piece p : player_pieces) {
			if (p.getCol() == position.col)
				value++;
		}

		int[] inimigos_na_linha = new int[board.getSize()];

		for (Piece p : enemy_pieces) {
			inimigos_na_linha[p.getRow()]++;
			if (p.getCol() == position.col)
				value--;
		}

		value += inimigos_na_linha[position.row];

		return value;
	}

	/**
	 * Calcula o valor do tabuleiro para o jogador preto.
	 * 
	 * @param position
	 * @return valor do tabuleiro
	 */
	private int calcBlackHeur(Heur position) {
		int value = 0;
		Queue<Piece> player_pieces = board.getPlayerPieces(activeplayer);
		Queue<Piece> enemy_pieces = board.getPlayerPieces(activeplayer ^ 1);

		for (Piece p : player_pieces) {
			if (p.getRow() == position.row)
				value++;
		}

		int[] inimigos_na_linha = new int[board.getSize()];

		for (Piece p : enemy_pieces) {
			inimigos_na_linha[p.getCol()]++;
			if (p.getRow() == position.col)
				value--;
		}

		value += inimigos_na_linha[position.row];

		return value;
	}

	/**
	 * TODO: Calcula o valor do tabuleiro com a nova peça.
	 * 
	 * @param position
	 *            A peça que vai ser colocada no turno
	 */
	private void calcHeur(Heur position) {
		if (activeplayer == 0)
			position.value += calcWhiteHeur(position);
		else
			position.value += calcBlackHeur(position);
	}

	/**
	 * Calcula um movimento para a peça a analisar
	 * 
	 * @param position
	 * @return Heur que representa a melhor peça para o turno
	 */
	private Heur singularMoveOfAPiece(Heur position) {
		Heur position2 = new Heur();
		position2.row = position.row;
		position2.col = position.col;
		position2.movement = position.movement;

		position2.moveCalculator();

		Piece p = new Piece(position2.row, position2.col, activeplayer);

		if (board.setPiece(p)) {
			calcHeur(position2);

			if (depth > 0) {
				changePlayer();
				Heur enemymove = miniMax();
				changePlayer();

				position.value -= enemymove.value;
			}

			board.removePiece(position2.row, position2.col);
		}

		return position2;
	}

	/**
	 * Calcula todos os movimentos possíveis para a posição (row,col).
	 * 
	 * @param row
	 * @param col
	 */
	private void movesForAPiece(Heur bestMove, int row, int col) {
		for (int i = 0; i < 4; i++) {
			Heur position = new Heur();
			position.row = row;
			position.col = col;
			position.movement = i;
			Heur position2 = singularMoveOfAPiece(position);
			// first_calc.add(position2);

			updateBestMove(bestMove, position2);
		}
	}

	/**
	 * Algoritmo MiniMax.
	 * 
	 * @return Melhor peça possível de se colocar no turno
	 */
	private Heur miniMax() {
		depth--;
		Heur bestMove = new Heur();

		Queue<Piece> playerPieces = board.getPlayerPieces(activeplayer);
		Queue<Piece> tempPlayerPieces = playerPieces;
		first_calc = new ArrayList<Heur>();

		while (!tempPlayerPieces.isEmpty()) {
			/*
			 * TODO: calcular o valor do movimento e guardar em first_calc;
			 * first_calc[iterador] = heuristic(temp_plauer_pieces.first());
			 */
			// TODO: Monte-Carlo tree search talvez seja bom para nós

			Piece tempPiece = tempPlayerPieces.remove();

			movesForAPiece(bestMove, tempPiece.getRow(), tempPiece.getCol());
		}

		System.out.println("Melhor jogada para o jogador " + Utility.itop(activeplayer) + ": (" + (bestMove.row + 1)
				+ "," + Utility.itoc(bestMove.col) + "), " + Utility.itoa(bestMove.movement));

		return bestMove;
	}
}
