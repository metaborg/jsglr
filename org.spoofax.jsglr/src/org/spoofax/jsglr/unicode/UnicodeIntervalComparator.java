package org.spoofax.jsglr.unicode;

import java.util.Comparator;

public class UnicodeIntervalComparator implements Comparator<UnicodeInterval> {

	public int compare(UnicodeInterval o1, UnicodeInterval o2) {
		int deltaX = o1.x - o2.x;
		if (deltaX == 0) {
			return o1.y - o2.y;
		}
		return deltaX;
	}

}
