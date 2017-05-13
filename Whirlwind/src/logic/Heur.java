package logic;

/**
 * Classe para juntar vários elementos que classificam uma jogada.
 */
class Heur {
    int row;
    int col;
    int movement;
    int value;

    Heur() {
        value = -99999;
    }

    Heur(int row, int col, int movement) {
        this();
        this.row = row;
        this.col = col;
        this.movement = movement;
    }

    Heur(Heur h) {
        row = h.row;
        col = h.col;
        movement = h.movement;
        value = h.value;
    }

    /**
     * Calcula onde a peça irá ficar.
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
