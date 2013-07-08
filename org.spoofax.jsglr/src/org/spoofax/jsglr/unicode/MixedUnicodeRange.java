package org.spoofax.jsglr.unicode;

import java.util.LinkedList;

import org.spoofax.interpreter.terms.IStrategoTerm;

/**
 * A UnicodeRangePair is a pair of two UnicodeRanges where the first range only
 * contains 2 byte unicode characters and the last one only 4 byte unicode
 * characters
 * 
 * @author moritzlichter
 * 
 */
public class MixedUnicodeRange {
	
	private static final UnicodeInterval ASCII_INTERVAL = new UnicodeInterval(0, UnicodeConverter.FIRST_UNICODE-1);
	private static final UnicodeInterval UTF16_2BYTE_INTERVAL = new UnicodeInterval(UnicodeConverter.FIRST_UNICODE, UnicodeConverter.getMaxTwoByteChar());
	private static final UnicodeInterval UTF16_4BYTE_INTERVAL = new UnicodeInterval((long)UnicodeConverter.getMaxTwoByteChar()+1, UnicodeInterval.intToLong(UnicodeConverter.getLastUnicode()));

	private UnicodeRange utf16_2byte;
	private UnicodeRange utf16_4byte;
	private UnicodeRange ascii;
	
	/**
	 * Create an empty UnicodeRangePair
	 */
	public MixedUnicodeRange() {
		this(new UnicodeRange(), new UnicodeRange(), new UnicodeRange());
	}
	
	

	/**
	 * Creates a new UnicodeRangePair from the given value to the given value.
	 * If necessary, the interval is split into a two byte and a four byte
	 * interval.
	 * 
	 * @param start
	 *            the start of the interval
	 * @param end
	 *            the end of the interval
	 * @throws IllegalArgumentException
	 *             when start > end
	 */
	public MixedUnicodeRange(int start, int end) {
		this();
		if (start > end) {
			int temp = start;
			start = end;
			end = temp;
		}
	//	System.out.println("Create for: " + start + " - " + end);
		if (UnicodeConverter.isAscii(end)) {
			this.ascii = new UnicodeRange(new UnicodeInterval(start, end));
		} else if (UnicodeConverter.isAscii(start)) {
			this.ascii = new UnicodeRange(new UnicodeInterval(start, UnicodeConverter.getFirstUnicode()-1));
			start = UnicodeConverter.getFirstUnicode();
		} 
		if (!UnicodeConverter.isAscii(start)) {
		// Check how many bytes start and end needs
		if (UnicodeConverter.isTwoByteCharacter(end)) {
			// Both two bytes
			this.utf16_2byte = new UnicodeRange(new UnicodeInterval(start, end));
		} else if (!UnicodeConverter.isTwoByteCharacter(start)) {
			// Both four bytes
			this.utf16_4byte = new UnicodeRange(new UnicodeInterval(start, end));
		} else {
			// Mixed, split the interval into start - Max 2 Bytes and Min 4
			// Bytes - end
			this.utf16_2byte = new UnicodeRange(new UnicodeInterval(start, UnicodeConverter.getMaxTwoByteChar()));
			this.utf16_4byte = new UnicodeRange(new UnicodeInterval(UnicodeConverter.getMaxTwoByteChar() + 1, end));
		}
		}
	}

	public MixedUnicodeRange(UnicodeRange ascii, UnicodeRange r2, UnicodeRange r4) {
		this.ascii = ascii;
		this.utf16_2byte = r2;
		this.utf16_4byte = r4;
	}

	/**
	 * Unites this UnicodeRangePair with the given one.
	 * 
	 * @param p
	 *            the pait to unite with
	 */
	public void unite(MixedUnicodeRange p) {
		this.ascii.unite(p.ascii);
		this.utf16_2byte.unite(p.utf16_2byte);
		this.utf16_4byte.unite(p.utf16_4byte);
	}

	public void intersect(MixedUnicodeRange p) {
		this.ascii.intersect(p.ascii);
		this.utf16_2byte.intersect(p.utf16_2byte);
		this.utf16_4byte.intersect(p.utf16_4byte);
	}
	
	public void diff(MixedUnicodeRange p) {
		this.ascii.diff(p.ascii);
		this.utf16_2byte.diff(p.utf16_2byte);
		this.utf16_4byte.diff(p.utf16_4byte);
	}
	
	public void invert() {
		this.ascii.invert(ASCII_INTERVAL);
		this.utf16_2byte.invert(UTF16_2BYTE_INTERVAL);
		this.utf16_4byte.invert(UTF16_4BYTE_INTERVAL);
	}
	
	public void normalize() {
		this.ascii.normalize();
		this.utf16_2byte.normalize();
		this.utf16_4byte.normalize();
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		// First the 007 to indicate an unicode character
		builder.append("[\\" + (int) UnicodeConverter.UNICODE_PRAEFIX + "]");
		// Check whether which ranges are not empty
		if (!this.utf16_2byte.isEmpty() && !this.utf16_4byte.isEmpty()) {
			// both ranges, create alternative
			builder.append('(');
			builder.append(this.utf16_2byte.toString());
			builder.append('|');
			builder.append(this.utf16_4byte.toString());
			builder.append(')');
		} else if (this.utf16_2byte.isEmpty()) {
			builder.append(this.utf16_4byte.toString());
		} else {
			builder.append(this.utf16_2byte.toString());
		}
		return builder.toString();
	}
	
	public IStrategoTerm toAST() {
		//System.out.println("Produce AST for:");
		//System.out.println("A: "+ this.ascii);
		//System.out.println("2: "+ this.utf16_2byte);
		//System.out.println("4: " + this.utf16_4byte);
		LinkedList<IStrategoTerm> alternativeTerms = new LinkedList<IStrategoTerm>();
		if (!this.ascii.isEmpty()) {
			alternativeTerms.add(this.ascii.toAST());
		}
		if (!this.utf16_2byte.isEmpty()) {
			alternativeTerms.add(prependSeven(this.utf16_2byte.toAST()));
		}
		if (!this.utf16_4byte.isEmpty()) {
			alternativeTerms.add(prependSeven(this.utf16_4byte.toAST()));
		}
		return UnicodeUtils.makeOrSymbol(alternativeTerms);
	}
	
	private IStrategoTerm prependSeven(IStrategoTerm term) {
		IStrategoTerm sevenCharClass = UnicodeUtils.makeCharClass(UnicodeConverter.UNICODE_PRAEFIX);
		LinkedList<IStrategoTerm> list = new LinkedList<IStrategoTerm>();
		list.add(sevenCharClass);
		list.add(term);
		return UnicodeUtils.makeSymbolSeq(list);
	}
}