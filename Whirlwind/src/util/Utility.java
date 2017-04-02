package util;

import java.util.Random;

public final class Utility {
	
	private Utility(){}
	
	/**
	 * Transforms an Integer into a char.
	 * @param i integer to transform
	 * @return char representation of i
	 */
	public static char itoc(int i){
		return (char) (65 + i);
	}

	/**
	 * Imprime uma linha de Letras.
	 * @param size n�mero de letras a imprimir
	 */
	public static void printLineOfChar(int size){
		char letter;
		for (int i = 0; i < size; i++){
			letter = Utility.itoc(i);
			System.out.print(letter + " ");
		}
	}

	/**
	 * Imprime uma linha de tra�os.
	 * ---------------------------
	 * @param size n�mero de tra�os a imprimir
	 */
	public static void printDashedLine(int size){
		for (int i = 0; i < size; i++){
			System.out.print("--");
		}
		System.out.println("--");
	}

	/**
	 * Transforms an Integer into an Arrow.
	 * @param i integer to transform
	 * @return char arrow representation of i
	 */
	public static char itoa(int i){
		if(i==0)
			return '^';
		if(i==1)
			return '>';
		if(i==2)
			return 'v';
		if(i==3)
			return '<';
		return 'f';
	}

	/**
	 * Transforms an Integer into a Player.
	 * @param i integer to transform
	 * @return char arrow representation of i
	 */
	public static char itop(int i){
		if(i==0)
			return 'O';
		if(i==1)
			return 'X';
		if(i==-1)
			return 'e';
		return 'f';
	}

	/**
	 * @return N�mero inteiro aleat�rio entre [0,10].
	 */
	public static int random(){
		return new Random().nextInt((10 - 0) + 1) + 0;
	}

	/**
	 * Cria um n�mero inteiro aleat�rio entre [min, max].
	 * @param min Valor m�nimo poss�vel
	 * @param max Valor m�ximo poss�vel
	 * @return N�mero aleat�rio dentro de [min, max]
	 */
	public static int random(int min, int max){
		return new Random().nextInt((max - min) + 1) + min;
	}

}
