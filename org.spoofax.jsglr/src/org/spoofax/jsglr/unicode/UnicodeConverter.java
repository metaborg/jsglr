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

	public static final int FIRST_UNICODE = 128;
	public static final int LAST_UNICODE = 0x10FFFD;

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
	public static String encodeUnicodeToAscii(String unicodeString) {
		return encodeUnicode(unicodeString, UNICODE_TO_ASCII_ENCODER);
	}

	public static String encodeUnicodeToBacklashU(String unicodeString) {
		return encodeUnicode(unicodeString, UNICODE_TO_BACKSLASH_U_ENCODER);
	}

	private static String encodeUnicode(String unicodeString, UnicodeEncoder encoder) {
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
			int nextChar = extractFirstUnicodeCharacter(buffer);
			// Encode the unicode character to ascii if necessary
			if (isUnicode(nextChar)) {
				builder.append(encoder.encodeUnicodeCharacter(nextChar));
			} else {
				builder.append((char) nextChar);
			}
		}
		return builder.toString();
	}

	private static interface UnicodeEncoder {
		public String encodeUnicodeCharacter(int nextChar);
	}

	public static final UnicodeEncoder UNICODE_TO_ASCII_ENCODER = new UnicodeEncoder() {

		public String encodeUnicodeCharacter(int nextChar) {
			return UnicodeConverter.encodeUnicodeCharacterToAscii(nextChar);
		}
	};

	public static final UnicodeEncoder UNICODE_TO_BACKSLASH_U_ENCODER = new UnicodeEncoder() {

		public String encodeUnicodeCharacter(int nextChar) {
			return "\\u" + Integer.toHexString(utf164ByteToNumber(nextChar)) + "\\u";
		}
	};

	public static String unicodeBackslashUToString(String string) {
		int value = Integer.parseInt(string, 16);
		return unicodeNumberToString(value);
	}
	
	public static String unicodeNumberToString(int number) {
		return new String(numberToUtf16_4Byte(number));
	}

	public static boolean isUnicode(int c) {
		// Need two check for greater 127 or less 0 because integer comparison
		// is signed
		return c >= FIRST_UNICODE || c < 0;
	}

	public static boolean isAscii(int c) {
		return c < FIRST_UNICODE && c >= 0;
	}

	public static int getFirstUnicode() {
		return FIRST_UNICODE;
	}

	public static int getLastUnicode() {
		return LAST_UNICODE;
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

	private static final int UNICODE_4_BYTE_PATTERN_MSBS = 0x36;
	private static final int UNICODE_4_BYTE_PATTERN_LSBS = 0x37;

	private static int utf164ByteToNumber(int encoded) {
		boolean needToByte = encoded < 0 || encoded >= 0x10000;
		if (needToByte) {
			int first = encoded & 0x03ff0000;
			int second = encoded & 0x000003ff;
			first = first >> 16;
			// System.out.println(first + " " + second);
			int res = (first << 10) | second;
			if (res >= 0x10000) {
				res += 0x10000;
			}
			return res;
		} else {
			return encoded;
		}
	}
	
	public static int numberToInt(int number) {
		return toUnicodeCharacter(new String(numberToUtf16_4Byte(number)));
	}

	private static char[] numberToUtf16_4Byte(int number) {
		boolean needTwoChars = number < 0 || number >= 0x10000;
		if (needTwoChars) {
			number -= 0x10000;
		} else {
			return new char[]{(char)number};
		}
		int first = number >> 10;
		int second = number & 0x000003ff;
		if (needTwoChars) {
			first = UNICODE_4_BYTE_PATTERN_MSBS << 10 | first;
			second = UNICODE_4_BYTE_PATTERN_LSBS << 10 | second;
		}
		char firstC = (char) first;
		char secondC = (char) second;
		if (firstC != 0) {
			return new char[] { firstC, secondC };
		} else {
			return new char[] { secondC };
		}
	}

	/**
	 * Checks whether the two given bytes decode the start of a four byte
	 * unicode character.
	 * 
	 * @param c
	 *            the character to check
	 * @return whether c are the first two bytes of a four byte character,
	 *         otherwise c is a completecharacter
	 */
	private static boolean is4ByteUnicodeBegin(int c) {
		return (c >> 10) == UNICODE_4_BYTE_PATTERN_MSBS;
	}

	/**
	 * Validates that the given two bytes are two valid lsbs of a four byte
	 * unicode character.
	 * 
	 * @param lsbs
	 *            the two last bytes
	 * @throws IllegalArgumentException
	 *             when lsbs is invalid
	 */
	private static void validate4ByteUnicodeLSBs(int lsbs) {
		if ((lsbs >> 10) != UNICODE_4_BYTE_PATTERN_LSBS) {
			throw new IllegalArgumentException("The character " + lsbs + "is not valid LSBs of UTF-16 character");
		}
	}

	public static int toUnicodeCharacter(String s) {
		return extractFirstUnicodeCharacterAndForceEmptyBuffer(CharBuffer.wrap(s));
	}

	/**
	 * Extracts the first unicode character in the given buffer. If the first
	 * character only covers the first char in the buffer, this char is read. If
	 * the first character needs two chars, because it is an 32 bit value, the
	 * first two chars are read if possible.
	 * 
	 * @param buffer
	 *            the buffer to decode its first character
	 * @return the decoded character
	 */
	public static int extractFirstUnicodeCharacter(CharBuffer buffer) {
		// Check whether this is a four bit word
		int c1 = buffer.get();
		// Check that this char is not the beginning of a single UTF
		// Character
		// Shift out the bits which describe the character, only keep the
		// signal bits
		if (is4ByteUnicodeBegin(c1)) {
			// Now validate the second
			int c2 = buffer.get();
			validate4ByteUnicodeLSBs(c2);
			// Now it is a valid 32 bit Unicode character
			// Concatenate the two 16 bits
			int val = c1 << 16 | c2;
			assert !UnicodeConverter.isTwoByteCharacter(val);
			return val;
		}

		// Single 16 but Unicode Character
		return c1;
	}

	public static int extractFirstUnicodeCharacterAndForceEmptyBuffer(CharBuffer buffer) {
		int c = extractFirstUnicodeCharacter(buffer);
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

	/**
	 * Decodes the given ascii String to a regular string with unicode
	 * characters instead of the ascii decoded unicode characters.
	 * 
	 * @param asciiString
	 *            the to ascii encoded unicode string
	 * @return the decoded unicode string
	 */
	public static String decodeAsciiToUnicode(String asciiString) {
		CharBuffer readBuffer = CharBuffer.wrap(asciiString);
		// New string could not be longer than the inut string
		CharBuffer writeBuffer = CharBuffer.allocate(asciiString.length());
		// Decode characters while the inout is not empty
		while (readBuffer.hasRemaining()) {
			decodeFirstCharacter(readBuffer, writeBuffer);
		}
		// now the result is in write Buffer
		int position = writeBuffer.position();
		writeBuffer.rewind();
		return writeBuffer.subSequence(0, position).toString();
	}

	/**
	 * Decodes the first character from the readBuffer to unicode and writes it
	 * to the write buffer. Note that more than one char is consumed for an
	 * unicode character and up two two chars may be written for an unicode
	 * character.
	 * 
	 * @param readBuffer
	 *            the input buffer
	 * @param writeBuffer
	 *            the output buffer
	 */
	private static void decodeFirstCharacter(CharBuffer readBuffer, CharBuffer writeBuffer) {
		// Get the first char
		char c = readBuffer.get();
		if (c != UNICODE_PRAEFIX) {
			assert !isUnicode(c);
			// Just ascii
			writeBuffer.put(c);
		} else {
			// Now we have unicode, so at leadt read the first to chars
			char c1 = readBuffer.get();
			char c2 = readBuffer.get();

			// Combine both chars
			int r1 = c1 << 8 | c2;
			if (is4ByteUnicodeBegin(r1)) {
				// Read the next
				c1 = readBuffer.get();
				c2 = readBuffer.get();
				int r2 = c1 << 8 | c2;
				validate4ByteUnicodeLSBs(r2);
				// Now we have the character, write both parts in the buffer
				writeBuffer.append((char) r1);
				writeBuffer.append((char) r2);
			} else {
				// Got only a two byte value
				writeBuffer.append((char) r1);
			}
		}
	}

}
