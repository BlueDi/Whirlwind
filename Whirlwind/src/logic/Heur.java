package logic;

/**
 * Classe para juntar v�rios elementos que classificam uma jogada.
 */
class Heur {
    int row;
    int col;
    int movement;
    int value;

    /**
     * Calcula onde a pe�a ir� ficar.
     */
    void moveCalculator() {
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
