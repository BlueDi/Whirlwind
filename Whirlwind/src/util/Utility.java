package util;

import java.util.Random;

public final class Utility {
	static Random rng = new Random();
	
	/**
	 * Transforms an Integer into a char.
	 * @param i integer to transform
	 * @return char representation of i
	 */
	public static char itoc(int i){
		return (char) (65 + i);
	}
	
	public static int random(){
        return rng.nextInt((11 - 0) + 1) + 0;
	}

	public static int random(int min, int max){
		return rng.nextInt((max - min) + 1) + min;
	}
	
}
