package org.spoofax.jsglr.unicode;

/**
 * The UnicodeInterval is an interval of Unicode characters from a start to an
 * end integer value.
 * 
 * @author moritzlichter
 * 
 */
public class UnicodeInterval extends Pair<Integer, Integer> {
	
	public UnicodeInterval(Integer start, Integer end) {
		super(start, end);
	}

	public UnicodeInterval(Integer val) {
		this(val, val);
	}
	
}