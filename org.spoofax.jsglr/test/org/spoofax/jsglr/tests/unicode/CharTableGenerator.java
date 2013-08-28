package org.spoofax.jsglr.tests.unicode;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.spoofax.jsglr.unicode.UnicodeConverter;
import org.spoofax.jsglr.unicode.charranges.MixedUnicodeRange;

public class CharTableGenerator {

	private static void printInt(int x) {
		if (x < 10) {
			System.out.print("  " + x);
		} else if (x < 100) {
			System.out.print(" " + x);
		} else {
			System.out.print(x);
		}
	}
	
	private static void printCharArray(char[] ca) {
		for (char c : ca) {
			System.out.print((int)c + " ");
		}
	}
	
	private static void printStringChars(String s) {
		printCharArray(s.toCharArray());
	}

	private static String toString(long val) {
		String s1=  UnicodeConverter.unicodeNumberToString((int) val);
		if (val < 127) {
			return"\\" + s1;
		}
		return s1;
	}
	
	private static String toUnicodeNumber(long val) {
		if (UnicodeConverter.isAscii((int)val)) {
			return "\\" + val;
		}
		return "\\u" + Integer.toHexString((int)val) + "\\u";
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		long intervalStart = Long.MAX_VALUE;
		long lastLowerCase = Long.MAX_VALUE;
		long max = 0;
		max = max | UnicodeConverter.LAST_UNICODE;
		System.out.println(max);
		MixedUnicodeRange range = new MixedUnicodeRange();
		String charClass = "[";
		String numberCharClass = "[";
		max = 127;
		Byte[] types = new Byte[]{Character.CONNECTOR_PUNCTUATION, Character.DASH_PUNCTUATION, Character.INITIAL_QUOTE_PUNCTUATION, Character.OTHER_PUNCTUATION};
		//Character.CURRENCY_SYMBOL, Character.MATH_SYMBOL, Character.MODIFIER_SYMBOL , Character.OTHER_SYMBOL};
		Set<Byte> typesList = new HashSet<Byte>();
		typesList.addAll(Arrays.asList(types));
		for (long i = 0; i < max; i++) {
			if (typesList.contains(new Integer(Character.getType((int) i)).byteValue())) {
				if (lastLowerCase != i - 1) {
					intervalStart = i;
				}
				System.out.println(toString(i));
				lastLowerCase = i;
			} else if (lastLowerCase == i - 1) {
				System.out.println(intervalStart + " - " + lastLowerCase);
				if (intervalStart != lastLowerCase) {
					charClass += toString(intervalStart) + "-" + toString(lastLowerCase);
					numberCharClass += toUnicodeNumber(intervalStart) + "-" + toUnicodeNumber(lastLowerCase);
				} else {
					charClass += toString(lastLowerCase);
					numberCharClass += toUnicodeNumber(lastLowerCase);
				}
				range.unite(new MixedUnicodeRange((int) intervalStart, (int) lastLowerCase));
			}

		}
		charClass +="]";
		numberCharClass += "]";
		System.out.println(charClass);
		System.out.println(numberCharClass);
	}

}
