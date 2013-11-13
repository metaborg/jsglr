package org.spoofax.jsglr.tests.unicode;

import static org.junit.Assert.*;

import org.junit.Test;
import org.spoofax.jsglr.unicode.UnicodeConverter;

public class UnicodeConverterTest {

	private static final String UNICODE_STRING1 = new String(new char[]{'a','f', 0x32f8, 'c', 0x1202, 0x00f6, 'd', 0x36<<10 | 0x12, 0x37 << 10 | 0x82, 'e'});
	private static final String ASCII_STRING1 = new String(new char[]{'a', 'f', 0x7, 0x32, 0xf8, 'c', 0x7, 0x12, 0x02, 0x7, 0x00, 0xf6, 'd', 0x7, 0x36<<2, 0x12, 0x37<<2, 0x82, 'e'});
	
	@Test
	public void testEncodeUnicode() {
		String encodedString = UnicodeConverter.encodeUnicodeToAscii(UNICODE_STRING1);
		assertEquals(ASCII_STRING1, encodedString);
	}
	
	@Test
	public void testDecodeUnicode() {
		String decodedString = UnicodeConverter.decodeAsciiToUnicode(ASCII_STRING1);
		assertEquals(UNICODE_STRING1, decodedString);
	}
	
	@Test
	public void testEncodeDecode() {
		assertEquals(UNICODE_STRING1, UnicodeConverter.decodeAsciiToUnicode(UnicodeConverter.encodeUnicodeToAscii(UNICODE_STRING1)));
	}
	
	@Test
	public void testEncodeDecode2() {
		assertEquals("ԱՑ", UnicodeConverter.decodeAsciiToUnicode(UnicodeConverter.encodeUnicodeToAscii("ԱՑ")));
	}
	
	

}
