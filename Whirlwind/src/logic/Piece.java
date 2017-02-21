package logic;

public class Piece {
	private String color;
	private boolean fill;

	public Piece(){
		color =null;
		fill=false;

	}
	public Piece(String color){
		if(!color.equals("white") && !color.equals("black")){
			color =null;
			fill=false;
			System.out.println("invalid color");
			return;
		}
		this.color=color;
		fill=true;

	}
}
