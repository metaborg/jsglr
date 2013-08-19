package org.spoofax.jsglr.unicode.charranges;

import java.util.ArrayList;
import java.util.List;

import org.spoofax.jsglr.unicode.UnicodeConverter;

/**
 * The UnicodeInterval is an interval of Unicode characters from a start to an
 * end integer value.
 * 
 * @author moritzlichter
 * 
 */
public class UnicodeInterval extends Pair<Long, Long> {

	public static long intToLong(int i) {
		long l = 0;
		l = l | i;
		return l;
	}
	
	public UnicodeInterval(Long start, Long end) {
		super(Math.min(start, end), Math.max(start, end));
	}
	
	public UnicodeInterval(int start, int end) {
		super(intToLong(start), intToLong(end));
	}

	public UnicodeInterval(Long val) {
		this(val, val);
	}
	
	public UnicodeInterval(int val) {
		this(val, val);
	}
	
	@Override
	public UnicodeInterval clone() {
		return new UnicodeInterval(x, y);
	}

	private boolean isContactAndSmaller(UnicodeInterval other) {
		return this.y +1 >= other.x && this.x <= other.x && this.y < other.y;
	}

	private boolean isOverlappingAndSmaller(UnicodeInterval other) {
		return this.y >= other.x && this.x <= other.x && this.y < other.y;
	}

	private boolean isInside(UnicodeInterval other) {
		return this.x >= other.x && this.y <= other.y;
	}

	public UnicodeInterval unite(UnicodeInterval other) {
		// We need to check that this and other at least contact (or overlap)
		if (this.isContactAndSmaller(other)) {
			return new UnicodeInterval(this.x, Math.max(this.y, other.y));
		} else if (other.isContactAndSmaller(this)) {
			return new UnicodeInterval(other.x, Math.max(this.y, other.y));
		} else if (this.isInside(other)) {
			return other;
		} else if (other.isInside(this)) {
			return this;
		}
		// Cannot unite, result are both
		return null;
	}

	public UnicodeInterval intersect(UnicodeInterval other) {
		// We again need to check for the same than for uniting, both intervals
		// need to overlap
		if (this.isOverlappingAndSmaller(other)) {
			return new UnicodeInterval(other.x, this.y);
		} else if (other.isOverlappingAndSmaller(this)) {
			return new UnicodeInterval(this.x, other.y);
		} else if (this.isInside(other)) {
			return this;
		} else if (other.isInside(this)) {
			return other;
		}
		// Cannot intersect, result is empty
		return null;
	}

	private UnicodeRange buildDiffInsideRange(UnicodeInterval inner) {
		UnicodeRange range = new UnicodeRange();
		if (this.x +1 < inner.x) {
			range.addInterval(new UnicodeInterval(this.x, inner.x-1));
		}
		if (this.y > inner.y+1) {
			range.addInterval(new UnicodeInterval(inner.y+1, this.y));
		}
		return range;
	}

	public UnicodeRange diff(UnicodeInterval other) {
		if (this.isOverlappingAndSmaller(other)) {
			if (this.x +1 < other.x) {
				return new UnicodeRange(new UnicodeInterval(this.x, other.x-1));
			}
			return new UnicodeRange();
		} else if (other.isOverlappingAndSmaller(this)) {
			if (other.y + 1 < this.y) {
				return new UnicodeRange(new UnicodeInterval(other.y + 1, this.y));
			}
			return new UnicodeRange();
		} else if (this.isInside(other)) {
			return new UnicodeRange();
		} else if (other.isInside(this)) {
			return this.buildDiffInsideRange(other);
		}
		// Nothing to diff
		return new UnicodeRange(this);
	}

	public List<UnicodeInterval> normalize() {
		List<UnicodeInterval> normalizedIntervals = new ArrayList<UnicodeInterval>(3);
		// Be careful, an UnicodeInterval may needs to be split in more when
		// more than the last byte differ
		// Calculate the equal bytes (from hsb) and get the equal part
		int numBytes = UnicodeConverter.isTwoByteCharacter(this.x.intValue()) ? 2 : 4;
		int numEqualBytes = 0;
		int equal = 0;
		for (int i = numBytes - 1; i >= 0; i--) {
			int byteX = UnicodeConverter.getByte(i, this.x.intValue());
			int byteY = UnicodeConverter.getByte(i, this.y.intValue());
			if (byteX == byteY) {
				numEqualBytes++;
				equal = equal << 8;
				equal = equal | byteX;
			} else {
				break;
			}
		}
		// Check whether there is more than a single byte
		if (numEqualBytes < numBytes - 1) {
			// Create three intervals
			// 1. initial.x - equalbytes | first from rest of initial x ff...
			// 2. equalbytes | first from rest of initial x + 1 | 0 ... -
			// equalbytes | first from rest of initial.y -1| ff...
			// 3. equalbytes | first from rest of initial.y | 0... - initial.y
			int endFirst = (equal << 8) | UnicodeConverter.getByte(numEqualBytes + 1, this.x.intValue());
			int startSecond = (equal << 8) | (UnicodeConverter.getByte(numEqualBytes + 1, this.y.intValue()));
			for (int i = numEqualBytes + 1; i < numBytes; i++) {
				endFirst = (endFirst << 8) | 0xff;
				startSecond = (startSecond << 8) | 0x0;
			}
			normalizedIntervals.add(new UnicodeInterval(this.x, (long)endFirst));
			normalizedIntervals.add(new UnicodeInterval((long)endFirst + 1, (long)startSecond - 1));
			normalizedIntervals.add(new UnicodeInterval((long) startSecond, this.y));
		} else {
			// Simple Case :)
			normalizedIntervals.add(this);
		}
		return normalizedIntervals;
	}

}