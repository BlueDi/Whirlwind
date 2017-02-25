package cli;

import logic.*;

public class Main {

	public static void main(String[] args) throws Exception {
		Board b = new Board(14);
		b.display();
		b.setPiece(0, 2, 1);
		b.display();
	}

}
