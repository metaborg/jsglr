package org.spoofax.jsglr.unicode.charranges;

import java.util.Comparator;

public class UnicodeIntervalComparator implements Comparator<UnicodeInterval> {

	public int compare(UnicodeInterval o1, UnicodeInterval o2) {
		long deltaX = o1.x - o2.x;
		if (deltaX == 0) {
			return longToInt(o1.y - o2.y);
		}
		return longToInt(deltaX);
	}
	
	private int longToInt(long l) {
		if (l > 0) return 1;
		if (l < 0) return -1;
		return 0;
	}

}
