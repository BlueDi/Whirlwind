package util;

import java.util.Random;

public final class Utility {
	private static Random rng = new Random();

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
	 * @param size número de letras a imprimir
	 */
	public static void printLineOfChar(int size){
		char letter;
		for (int i = 0; i < size; i++){
			letter = Utility.itoc(i);
			System.out.print(letter + " ");
		}
	}

	/**
	 * Imprime uma linha de traços.
	 * ---------------------------
	 * @param size número de traços a imprimir
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

	public static int random(){
		return rng.nextInt((11 - 0) + 1) + 0;
	}

	public static int random(int min, int max){
		return rng.nextInt((max - min) + 1) + min;
	}

}
