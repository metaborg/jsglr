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
		this.ranges.add(initial);
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
				builder.append('|');
			}
		}
		builder.deleteCharAt(builder.length() - 1);
		builder.append(')');
		return builder.toString();
	}

}