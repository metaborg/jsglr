package org.spoofax.jsglr.unicode;

import java.nio.CharBuffer;

/**
 * This class contains static helper functions for converting a unicode string
 * to an ascii string and vice versa. The unicode characters are converted to a
 * sequence of ascii characters. For that is is assumed that the ascii character
 * 7 (the "beep" character) is not used anymore today. This character is
 * inserted to indicate that the following ascii characters are the byte values
 * of a unicode character. The unicode character is stores in UTF-16, so either
 * as 16 or 32 bit value, so as two or four ascii characters. of a u
 * 
 * @author moritzlichter
 * 
 */
public class UnicodeConverter {

	/**
	 * The praefix to indicate a Unicode character in an ascii string
	 */
	public static final char UNICODE_PRAEFIX = 0x7;

	/**
	 * Converts a given unicode string to a ascii string which converted unicode
	 * characters. A Unicode character is converted as follows: Ascii symbol 7
	 * (beep symbol) followed by the byte values of the unicode character
	 * encoded as UTF-16. This method assumes that the beep symbol is not
	 * contained in the given String.
	 * 
	 * @param unicodeString
	 *            the unicode String to convery
	 * @return the resulting ascii String
	 */
	public static String convertFromUnicode(String unicodeString) {
		// Reserve a bit more space because the string might get longer and thus
		// we do not need to reallocate an array
		StringBuilder builder = new StringBuilder((int) (unicodeString.length() * 1.1));
		// Wrap the unicode String in an buffer
		CharBuffer buffer = CharBuffer.wrap(unicodeString);
		// Process the buffer until it is empty
		while (buffer.hasRemaining()) {
			// Convert the unicde character. This possibly merges two chars
			// because a char is 16 bit in Java and some UTF-16 characters have
			// 32 bit.
			int nextChar = decodeFirstUnicodeCharacter(buffer);
			// Encode the unicode character to ascii if necessary
			if (isUnicode(nextChar)) {
				builder.append(encodeUnicodeCharacterToAscii(nextChar));
			} else {
				builder.append((char) nextChar);
			}
		}
		return builder.toString();
	}

	private static boolean isUnicode(int c) {
		// Need two check for greater 127 or less 0 because integer comparison
		// is signed
		return c > 127 || c < 0;
	}

	/**
	 * Checks whether the given character consists of a single char or more
	 * (two).
	 * 
	 * @param c
	 *            the character to check
	 * @return true when c can be stored in a char
	 */
	public static boolean isTwoByteCharacter(int c) {
		// Need the check for greater 0, because integer comparison is signed
		return c >= 0 && c <= getMaxTwoByteChar();
	}

	public static int getMaxTwoByteChar() {
		return 0xFFFF;
	}

	private static String encodeUnicodeCharacterToAscii(int c) {
		if (isTwoByteCharacter(c)) {
			return new String(new char[] { UNICODE_PRAEFIX, getByte(1, c), getByte(0, c) });
		} else {
			return new String(
					new char[] { UNICODE_PRAEFIX, getByte(3, c), getByte(2, c), getByte(1, c), getByte(0, c) });
		}
	}

	/**
	 * Decodes the first unicode character in the given buffer. If the first
	 * character only covers the first char in the buffer, this char is read. If
	 * the first character needs two chars, because it is an 32 bit value, the
	 * first two chars are read if possible.
	 * 
	 * @param buffer
	 *            the buffer to decode its first character
	 * @return the decoded character
	 */
	public static int decodeFirstUnicodeCharacter(CharBuffer buffer) {
		// Check whether this is a four bit word
		int c1 = buffer.get();
		// Check that this char is not the beginning of a single UTF
		// Character
		// Shift out the bits which describe the character, only keep the
		// signal bits
		if ((c1 >> 10) == 0x36) {
			// Now validate the second
			int c2 = buffer.get();
			if ((c2 >> 10) == 0x37) {
				// Now it is a valid 32 bit Unicode character
				// Concatenate the two 16 bits
				int val = c1 << 16 | c2;
				assert !UnicodeConverter.isTwoByteCharacter(val);
				return val;
			} else {
				// This is an invalid string
				throw new IllegalArgumentException("The character " + c1 + "is not a valid UTF-16 character");
			}
		}

		// Single 16 but Unicode Character
		return c1;
	}

	public static int decodeFirstUnicodeCharacterAndForceEmptyBuffer(CharBuffer buffer) {
		int c = decodeFirstUnicodeCharacter(buffer);
		if (buffer.hasRemaining()) {
			throw new IllegalArgumentException("CharBuffer contains more than a single character");
		}
		return c;
	}

	/**
	 * Returns the value of the byte with number num in c. The lsb has number 0.
	 * 
	 * @param num
	 *            the number of the requested byte
	 * @param c
	 *            value to get the byte from
	 * @return the num-th byte of c
	 */
	public static char getByte(int num, int c) {
		// Shift out the trailing not necessary bits and remove the leading one
		// with the bitwise and
		int temp = c >> (num * 8);
		temp = temp & 0xFF;
		return (char) temp;
	}

}
