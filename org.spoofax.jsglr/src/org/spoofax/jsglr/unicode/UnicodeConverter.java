package org.spoofax.jsglr.unicode;

import java.nio.CharBuffer;

public class UnicodeConverter {

	public static String convertFromUnicode(String unicodeString) {

		for (char c : unicodeString.toCharArray()) {
			System.out.print((int) c + " ");
		}
		System.out.println();

		StringBuilder builder = new StringBuilder((int) (unicodeString.length() * 1.1));
		CharBuffer buffer = CharBuffer.wrap(unicodeString);
		while(buffer.hasRemaining()) {
			int nextChar = convertSingleUnicode(buffer);
			if (isUnicode(nextChar)) {
				builder.append(encodeUnicodeChar(nextChar));
			} else {
				builder.append((char)nextChar);
			}
		}
		for (char c : builder.toString().toCharArray()) {
			System.out.print((int) c + " ");
		}
		System.out.println();
		return builder.toString();
	}

	private static boolean isUnicode(int c) {
		return c >= 127 || c < 0;
	}

	private static final char UNICODE_PRAEFIX = 0x7;

	public static boolean isTwoByteChar(int c) {
		// Need the check for greater 0, because integer comparision is signed
		return c >= 0 && c <= getMaxTwoByteChar();
	}

	public static int getMaxTwoByteChar() {
		return 0xFFFF;
	}

	private static String encodeUnicodeChar(int c) {
		if (isTwoByteChar(c)) {
			return new String(new char[] { UNICODE_PRAEFIX, getByte(1, c), getByte(0, c) });
		} else {
			return new String(
					new char[] { UNICODE_PRAEFIX, getByte(3, c), getByte(2, c), getByte(1, c), getByte(0, c) });
		}
	}

	public static int convertSingleUnicode(CharBuffer buffer) {
		// Check whether this is a four bit word
		int c1 = buffer.get();
		System.out.println(c1);
			// Check that this char is not the beginning of a single UTF
			// Character
			// Shift out the bits which describe the character, only keep the
			// signal bits
			if ((c1 >> 10) == 0x36) {
				// Now validate the second
				int c2 = buffer.get();

				System.out.println(c2);
				if ((c2 >> 10) == 0x37) {
					// Now it is a valid 32 bit Unicode character
					// Concatenate the two 16 bits
					int val = c1 << 16 | c2;
					assert !UnicodeConverter.isTwoByteChar(val);
					return val;
				} else {
					// This is an invalid string
					throw new IllegalArgumentException("The character " + c1 +"is not a valid UTF-16 character");
				}
			}

			
		
		// Single 16 but Unicode Character
		return c1;
	}
	
	public static int convertSingleUnicodeAndForceEmpty(CharBuffer buffer) {
		int c = convertSingleUnicode(buffer);
		if (buffer.hasRemaining()) {
			throw new IllegalArgumentException("CharBuffer contains more than a single character");
		}
		return c;
	}

	public static char getByte(int num, int c) {
		int temp = c >> (num * 8);
		temp = temp & 0xFF;
		return (char) temp;
	}

}
