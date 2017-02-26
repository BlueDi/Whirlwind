package logic;

import util.Utility;

public class Game {
	private Board board;
	private int activeplayer = -1;

	public Game() throws Exception{
		board = new Board(12);
		activeplayer = 0; //black;
	}

	private void turn(){
		board.display();
		board.setPiece(Utility.random(), Utility.random(), activeplayer);
		activeplayer ^= 1;
	}

	public void startGame(){
		while(true)
			turn();
	}
}
