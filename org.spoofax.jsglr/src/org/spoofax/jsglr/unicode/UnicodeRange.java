package org.spoofax.jsglr.unicode;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * A UnicodeRange is a list of UnicodeIntervals.
 * 
 * @author moritzlichter
 * 
 */
public class UnicodeRange implements Iterable<UnicodeInterval> {

	private List<UnicodeInterval> ranges;

	public UnicodeRange() {
		this.ranges = new LinkedList<UnicodeInterval>();
	}

	public UnicodeRange(UnicodeInterval initial) {
		this();
		// Be careful, an UnicodeInterval may needs to be split in more when more than the last byte differ
		// Calculate the equal bytes (from hsb) and get the equal part
		int numBytes = UnicodeConverter.isTwoByteCharacter(initial.x) ? 2 : 4;
		int numEqualBytes = 0;
		int equal = 0;
		for (int i = numBytes-1; i >= 0; i--) {
			int byteX = UnicodeConverter.getByte(i, initial.x) ;
			int byteY = UnicodeConverter.getByte(i, initial.y);
			if (byteX== byteY) {
				numEqualBytes++;
				equal = equal << 8;
				equal = equal | byteX;
			} else {
				break;
			}
		}
		//Check whether there is more than a single byte
		if (numEqualBytes < numBytes - 1) {
			// Create three intervals
			// 1. initial.x - equalbytes | first from rest of initial x  ff...
			// 2. equalbytes | first from rest of initial x + 1 | 0 ... - equalbytes | first from rest of initial.y -1| ff...
			// 3. equalbytes | first from rest of initial.y | 0... - initial.y
			int endFirst = (equal << 8) | UnicodeConverter.getByte(numEqualBytes+1, initial.x);
			int startSecond = (equal << 8) | (UnicodeConverter.getByte(numEqualBytes+1, initial.y) );
			for (int i = numEqualBytes+1; i < numBytes; i++) {
				endFirst = (endFirst << 8) | 0xff;
				startSecond = (startSecond << 8) | 0x0;
			}
			this.ranges.add(new UnicodeInterval(initial.x, endFirst));
			this.ranges.add(new UnicodeInterval(endFirst+1, startSecond-1));
			this.ranges.add(new UnicodeInterval(startSecond, initial.y));
		} else {
			//Simple Case :)
			this.ranges.add(initial);
		}
	}

	public void unite(UnicodeRange r) {
		this.ranges.addAll(r.ranges);
	}

	public Iterator<UnicodeInterval> iterator() {
		return this.ranges.iterator();
	}

	public boolean isEmpty() {
		return ranges.isEmpty();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();

		builder.append('(');
		if (!this.ranges.isEmpty()) {
			int num = UnicodeConverter.isTwoByteCharacter(this.ranges.get(0).x) ? 2 : 4;
			for (UnicodeInterval r : this) {
				builder.append("(");
				for (int i = num - 1; i >= 0; i--) {
					builder.append('[');
					int start = Math.min(r.x, r.y);
					int end = Math.max(r.x, r.y);
					// System.out.println("Range from " + start + " to " + end);
					builder.append("\\");
					builder.append((int) UnicodeConverter.getByte(i, start));
					if (start != end) {
						builder.append('-');
						builder.append("\\");
						builder.append((int) UnicodeConverter.getByte(i, end));
					}
					builder.append(']');
				}
				builder.append(")");
				builder.append('|');
			}
		}
		builder.deleteCharAt(builder.length() - 1);
		builder.append(')');
		return builder.toString();
	}

}