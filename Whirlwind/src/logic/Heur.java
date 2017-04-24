package logic;

/**
 * Classe para juntar vários elementos que classificam uma jogada.
 * 
 * @author diogo
 *
 */
class Heur {
	public int row;
	public int col;
	public int movement;
	public int value;

	/**
	 * Calcula onde a peça irá ficar.
	 */
	public void moveCalculator() {
		switch (movement) {
		case 0: // up
			row--;
			break;
		case 1: // right
			col++;
			break;
		case 2: // down
			row++;
			break;
		case 3: // left
			col--;
			break;
		default:
			break;
		}
	}
}
