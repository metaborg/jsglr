package org.spoofax.jsglr.tests.unicode;

public class CharTableGenerator {
	
	private static void printInt(int x) {
		if ( x < 10) {
			System.out.print("  " + x);
		} else if (x < 100) {
			System.out.print(" " + x);
		} else {
			System.out.print(x);
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		for (int i = 0; i < 255; i = i+8) {
			for (int j = 0; j < 8; j++) {
				printInt(i+j);
				System.out.print(" ");
				System.out.print((char)(i+j));
				System.out.print("  ");
			}
			System.out.println();
		}
	}

}
