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
		l = i;
		l = l & 0x0ffffffffL;
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
		return this.y + 1 >= other.x && this.x <= other.x && this.y < other.y;
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
		if (this.x + 1 < inner.x) {
			range.addInterval(new UnicodeInterval(this.x, inner.x - 1));
		}
		if (this.y > inner.y + 1) {
			range.addInterval(new UnicodeInterval(inner.y + 1, this.y));
		}
		return range;
	}

	public UnicodeRange diff(UnicodeInterval other) {
		if (this.isOverlappingAndSmaller(other)) {
			if (this.x + 1 < other.x) {
				return new UnicodeRange(new UnicodeInterval(this.x, other.x - 1));
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

	private static long getByte(long b, int num, int maxBytes) {
		int bytesRemove = maxBytes - num - 1;
		b = b >> (8 * bytesRemove);
		b = b & 0xFF;
		return b;
	}

	/**
	 * This method creates a list of normalized intervals for this interval. The
	 * interval needs to be an Unicode interval with byte length 2 or 4. Ascii
	 * intervals are not supported because normalization is trival for ascii
	 * intervals. Unicode Intervals with mixed byte length are not supported
	 * either. For details about normalization see below.<br>
	 * <br>
	 * Definition: Interval is normalized iff it can be encoded as a single
	 * sequence of SDF charclasses.
	 * 
	 * Examples: The range 0x0101 - 0x0202 is not normalized. [\1\2][\1\2] does
	 * not express the characters in the range, because e.g. 0x0110 is not
	 * accepted. The range 0x0101 - 0x0110 is normalized and can be expressed as
	 * [\1][\1-\10].
	 * 
	 * Theorem:<br>
	 * Let [start - end] be an interval. Let N denote the number of bytes of the
	 * borders of the interval and n the number of equal bytes of the longest
	 * common byte prefixes of start and end. Eq. N = 4 and n = 2 for
	 * [0x00031232 - 0x00031432]. is normalized if one of the following is
	 * correct: <br>
	 * a) N-n <= 1<br>
	 * b) Let start = (Eq1, .., Eqn, X1, ..,Xk) and end = (Eq1, ..,Eqn, Y1, ..,
	 * Yk). [start - end] is normalized, if Xl==0x00 && Yl==0xFF for 1<=l<=k <br>
	 * <br>
	 * Proof:<br>
	 * a) Now it is start = (Eq1, .., Eqn, X1) and end = (Eq1, .., Eqn, Y1).
	 * This can be represented in SDF as [E1q]..[Eqn][X1-Y1]. <br>
	 * b) [start - end] can be represented as [Eq1]..[Eqn][\0-\255]..[\0-\255]
	 * 
	 * To generate SDF for an interval k, which is not normalized, this interval
	 * needs to be split into a set of intervals I, which as to fulfill the
	 * following properties:<br>
	 * 1) intersection of i in I = k and<br>
	 * 2) for i,l in I: intersection i and l = empty.<br>
	 * The first property guarantees that the same characters are accepted and
	 * the second that no ambiguities occur.
	 * 
	 * normalize:: Interval -> Set of Intervals <br>
	 * defines a function which creates for an interval k such a set of
	 * Intervals. Proof of correction is in comments in the code.
	 * 
	 * @return
	 */
	public List<UnicodeInterval> normalize() {
		List<UnicodeInterval> normalizedIntervals = new ArrayList<UnicodeInterval>(3);
		// normalize is recursive. Base case is that the interval is normalized.
		final long start = this.x.longValue();
		final long end = this.y.longValue();

		// Determine the number of bytes in this interval
		int numBytes = UnicodeConverter.isTwoByteCharacter((int) start) ? 2 : 4;

		// Check whether the interval is normalized
		// Assume we have the input:
		// X = (Eq1, Eq2, .. Eqn, X1, X2, .., Xk) and
		// Y = (Eq1, Eq2, .. Eqn, Y1, Y2, .., Yk)
		// Then numEqualBytes = n and
		// equal = (Eq1, Eq2, .. Eqn)

		// equal is needed to check whether the interval is normalized but
		// useful later

		int numEqualBytes = 0;
		long equal = 0;
		// Iterate through all bytes and compare
		for (int i = 0; i < numBytes; i++) {
			long byteX = getByte(start, i, numBytes);
			long byteY = getByte(end, i, numBytes);
		//	System.out.println(Long.toHexString(byteY));
			if (byteX == byteY) {
				numEqualBytes++;
				equal = equal << 8;
				equal = equal | byteX;

			//	System.out.println(Long.toHexString(equal));
			} else {
				break;
			}
		}
	//	System.out.println(Long.toHexString(equal));
		// According to the theorem, an interval is normalized, if
		// a) or
		boolean isNormalizedA = (numBytes - numEqualBytes) == 1;
		// b)
		boolean isNormalizedB = true;
		for (int i = numEqualBytes; i < numBytes; i++) {
			isNormalizedB = (getByte(start, i, numBytes) == 0x00) && (getByte(end, i, numBytes) == 0xFF);
			if (!isNormalizedB)
				break;
		}
		// Note: There is no proof that cases a) and b) cover all normalized
		// intervals, altough I think they do. Nevertheless, no proof is
		// necessary because it is enough to generate a set of normalized
		// intervals (matching a) or b)) for a normalized interval, which does
		// not match a) or b)

		// Check the base case:
		if (isNormalizedA || isNormalizedB) {
			// Just use this interval
			normalizedIntervals.add(this);
		} else {
			// Recursive case:

			// Think about three intervals:
			// 1. (Eq1, Eq2, .. Eqn, X1 , X2, .., Xk) - (Eq1, Eq2, .. Eqn, X1 ,
			// FF, .., FF)
			// 2. (Eq1, Eq2, .. Eqn, X1+1, 00, .., 00) - (Eq1, Eq2, .. Eqn,
			// Y1-1, FF, .., FF)
			// 3. (Eq1, Eq2, .. Eqn, Y1 , 00, .., 00) - (Eq1, Eq2, .. Eqn, Y1 ,
			// Y2, .., Yk)

			// The Intervals are normalized recursively. Due to constructions,
			// the second interval is normalized due to case b) of theorem.
			// and is not needed to be normalized recursively.
			// Recursion terminates, because number of equal bytes increases by
			// one in each recursive call and if k=1 the interval is normalized
			// by case a) of theorem.

			// The second interval may be empty if X1 + 1 = Y1

			// No think about two optimizations:
			// If X2 == 00, .., Xk == 00, then the first and second interval can
			// be united to
			// 1/2. (Eq1, Eq2, .. Eqn, X1 , 00, .., 00) - (Eq1, Eq2, .. Eqn,
			// Y1-1, FF, .., FF)
			// If Y1 == FF, .., Yk == FF, then the second and third interval can
			// be united to
			// 2/3. (Eq1, Eq2, .. Eqn, X1+1, 00, .., 00) - (Eq1, Eq2, .. Eqn, Y1
			// , FF, .., FF)
			// Note:
			// 1. Both united intervals are normalized and need no recursive
			// normalization due to case b).
			// 2. The two conditions are exclusive, because otherwise the
			// interval
			// is normalized by b) (and we are not in this if branch)

			// IMPLEMENTATION
			// equal = (Eq1, Eq2, .. Eqn)
			// endFirst = (Eq1, Eq2, .. Eqn, X1, FF, .., FF)
			// startThird = (Eq1, Eq2, .. Eqn, Y1, 00, .., 00)

			long endFirst = (equal << 8) | getByte(start, numEqualBytes, numBytes);
			long startThird = (equal << 8) | getByte(end, numEqualBytes, numBytes);
			for (int i = numEqualBytes + 1; i < numBytes; i++) {
				endFirst = (endFirst << 8) | 0xff;
				startThird = (startThird << 8) | 0x0;
			}

			// Check whether the second interval exists
			if (getByte(start, numEqualBytes, numBytes) + 1 == getByte(end, numEqualBytes, numBytes)) {
				// Add first and third interval recursively normalized
				normalizedIntervals.addAll(new UnicodeInterval(start, endFirst).normalize());
				normalizedIntervals.addAll(new UnicodeInterval(startThird, end).normalize());
			} else {
				// Now the third interval exists

				// Check the first optimization: Whether first and second
				// interval can be united
				boolean unite = true;
				for (int j = numEqualBytes + 1; j < numBytes; j++) {
					unite = getByte(start, j, numBytes) == 0x00;
					if (!unite)
						break;
				}
				if (unite) {
					// Add 1/2 and 3 interval, normalize third recursively
					normalizedIntervals.add(new UnicodeInterval(start, startThird - 1));
					normalizedIntervals.addAll(new UnicodeInterval(startThird, end).normalize());
				} else {
					// Recursively normalize first interval
					normalizedIntervals.addAll(new UnicodeInterval(start, endFirst).normalize());

					// Check whether second and third interval can be united
					unite = true;
					for (int j = numEqualBytes + 1; j < numBytes; j++) {
						unite = getByte(end, j, numBytes) == 0xFF;
						if (!unite)
							break;
					}
					if (unite) {
						// Add 2/3
						normalizedIntervals.add(new UnicodeInterval(endFirst + 1, end));
					} else {
						// Add second and third interval
						normalizedIntervals.add(new UnicodeInterval(endFirst + 1, startThird - 1));
						normalizedIntervals.addAll(new UnicodeInterval(startThird, end).normalize());
					}
				}
			}
		}
		return normalizedIntervals;
	}

	@Override
	public String toString() {
		return "UnicodeInterval [0x" + Long.toHexString(this.x.longValue()) + " - 0x"
				+ Long.toHexString(this.y.longValue()) + "]";

	}

}