package logic;

public class Board {
	private Piece[][]board;

	public Board(int n){
		if(n!=12 && n!=20)
		{System.out.println("incorrect option of cases");
			return;
			}
		board=new Piece[n][n];
		for(int i=0; i<board.length;i++){
			for(int k=0;k<board[i].length;k++){
				board[i][k]=new Piece();
			}
		}


	}
}
