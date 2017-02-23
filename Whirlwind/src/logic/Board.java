package logic;

public class Board {
	private Piece[][]board;

	public Board(int n){
		if(n<12)
		{System.out.println("incorrect option of cases");
			return;
			}
		board=new Piece[n][n];
		int l=0;
		//preciso arranjar uma maneira de mapear mais certa mas umas vezes a distancia entre pontos � de 5 outras vezes 6 outras vezes 1... 
		//aquilo do movimento do cavalo eu n sei como mapear em sequencia
		for(int i=0; i<board.length;i++){
			for(int k=0;k<board[i].length;k++){
				board[i][k]=new Piece();
				if(l==1||l==11||l==22||l==33||l==44||l==54||l==65||l==76||l==87||l==97||l==98||l==108||l==119||l==130||l==141||l==151||l==162||l==173||l==184||l==194){
					board[i][k]=new Piece("white");
					
				}
				if(l==6||l==17||l==27||l==28||l==38||l==49||l==60||l==71||l==81||l==92||l==103||l==114||l==124||l==135||l==146||l==157||l==167||l==168||l==178||l==189){
					board[i][k]=new Piece("black");
				}
				l++;
			}
		}


	}
	public void display(){
		for(int i=0; i<board.length;i++){
			for(int k=0;k<board[i].length;k++){
				System.out.print(board[i][k].getColor()+" ");
				
			}
			System.out.println();
			}
	}
}
