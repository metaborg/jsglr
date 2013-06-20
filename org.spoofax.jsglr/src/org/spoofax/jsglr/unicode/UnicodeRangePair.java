package org.spoofax.jsglr.unicode;

/**
 * A UnicodeRangePair is a pair of two UnicodeRanges where the first range only
 * contains 2 byte unicode characters and the last one only 4 byte unicode
 * characters
 * 
 * @author moritzlichter
 * 
 */
public class UnicodeRangePair extends Pair<UnicodeRange, UnicodeRange> {

	/**
	 * Create an empty UnicodeRangePair
	 */
	public UnicodeRangePair() {
		this(new UnicodeRange(), new UnicodeRange());
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
	public UnicodeRangePair(int start, int end) {
		this(null, null);
		if (start > end) {
			throw new IllegalArgumentException("Cannot create Range: start > end");
		}
		// Check how many bytes start and end needs
		if (UnicodeConverter.isTwoByteCharacter(end)) {
			// Both two bytes
			this.x = new UnicodeRange(new UnicodeInterval(start, end));
			this.y = new UnicodeRange();
		} else if (!UnicodeConverter.isTwoByteCharacter(start)) {
			// Both four bytes
			this.y = new UnicodeRange(new UnicodeInterval(start, end));
			this.x = new UnicodeRange();
		} else {
			// Mixed, split the interval into start - Max 2 Bytes and Min 4
			// Bytes - end
			this.x = new UnicodeRange(new UnicodeInterval(start, UnicodeConverter.getMaxTwoByteChar()));
			this.y = new UnicodeRange(new UnicodeInterval(UnicodeConverter.getMaxTwoByteChar() + 1, end));
		}
	}

	public UnicodeRangePair(UnicodeRange r2, UnicodeRange r4) {
		super(r2, r4);
	}

	/**
	 * Unites this UnicodeRangePair with the given one.
	 * 
	 * @param p
	 *            the pait to unite with
	 */
	public void unite(UnicodeRangePair p) {
		this.x.unite(p.x);
		this.y.unite(p.y);
	}

	public void intersect(UnicodeRangePair p) {
		this.x.intersect(p.x);
		this.y.intersect(p.y);
	}
	
	public void diff(UnicodeRangePair p) {
		this.x.diff(p.x);
		this.y.diff(p.y);
	}
	
	public void normalize() {
		this.x.normalize();
		this.y.normalize();
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		// First the 007 to indicate an unicode character
		builder.append("[\\" + (int) UnicodeConverter.UNICODE_PRAEFIX + "]");
		// Check whether which ranges are not empty
		if (!this.x.isEmpty() && !this.y.isEmpty()) {
			// both ranges, create alternative
			builder.append('(');
			builder.append(this.x.toString());
			builder.append('|');
			builder.append(this.y.toString());
			builder.append(')');
		} else if (this.x.isEmpty()) {
			builder.append(this.y.toString());
		} else {
			builder.append(this.x.toString());
		}
		return builder.toString();
	}
}