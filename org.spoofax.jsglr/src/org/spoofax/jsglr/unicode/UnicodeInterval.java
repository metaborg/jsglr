package org.spoofax.jsglr.unicode;

import java.util.ArrayList;
import java.util.List;

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
		int numBytes = UnicodeConverter.isTwoByteCharacter(this.x) ? 2 : 4;
		int numEqualBytes = 0;
		int equal = 0;
		for (int i = numBytes - 1; i >= 0; i--) {
			int byteX = UnicodeConverter.getByte(i, this.x);
			int byteY = UnicodeConverter.getByte(i, this.y);
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
			int endFirst = (equal << 8) | UnicodeConverter.getByte(numEqualBytes + 1, this.x);
			int startSecond = (equal << 8) | (UnicodeConverter.getByte(numEqualBytes + 1, this.y));
			for (int i = numEqualBytes + 1; i < numBytes; i++) {
				endFirst = (endFirst << 8) | 0xff;
				startSecond = (startSecond << 8) | 0x0;
			}
			normalizedIntervals.add(new UnicodeInterval(this.x, endFirst));
			normalizedIntervals.add(new UnicodeInterval(endFirst + 1, startSecond - 1));
			normalizedIntervals.add(new UnicodeInterval(startSecond, this.y));
		} else {
			// Simple Case :)
			normalizedIntervals.add(this);
		}
		return normalizedIntervals;
	}

}