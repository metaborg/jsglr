package org.spoofax.jsglr.unicode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.tools.ant.util.UnicodeUtil;
import org.spoofax.interpreter.terms.IStrategoTerm;

/**
 * A UnicodeRange is a list of UnicodeIntervals.
 * 
 * @author moritzlichter
 * 
 */
public class UnicodeRange implements Iterable<UnicodeInterval>, Cloneable {

	private Set<UnicodeInterval> ranges;

	public UnicodeRange() {
		this.ranges = new HashSet<UnicodeInterval>();
	}
	
	@Override
	public UnicodeRange clone() {
		UnicodeRange newRange = new UnicodeRange();
		for (UnicodeInterval i : this.ranges) {
			newRange.addInterval(i.clone());
		}
		return newRange;
	}

	public UnicodeRange(UnicodeInterval initial) {
		this();
		this.ranges.add(initial);
	}
	
	public UnicodeRange(UnicodeInterval i1, UnicodeInterval i2) {
		this();
		this.ranges.add(i1);
		this.ranges.add(i2);
	}

	public void addInterval(UnicodeInterval i) {
		this.ranges.add(i);
	}
	
	private void unite(UnicodeInterval unite) {
		Set<UnicodeInterval> newRange = new HashSet<UnicodeInterval>();
		UnicodeInterval temp = unite;
		for (UnicodeInterval l : this) {
			UnicodeInterval newI = temp.unite(l);
			if (newI == null) {
				newRange.add(l);
			} else {
				temp = newI;
			}
		}
		newRange.add(temp);
		this.ranges = newRange;
	}
	
	public void unite(UnicodeRange r) {
		if (this.isEmpty()) {
			this.ranges = new HashSet<UnicodeInterval>(r.ranges);
		} else {
			for (UnicodeInterval other : r) {
				this.unite(other);
			}
		}
	}
	
	private void intersect(UnicodeInterval intersect) {
		Set<UnicodeInterval> newRange = new HashSet<UnicodeInterval>();
		for (UnicodeInterval i : this) {
			UnicodeInterval newI = intersect.intersect(i);
			if (newI != null) {
				newRange.add(newI);
			}
		}
		this.ranges = newRange;
	}
	
	public void intersect(UnicodeRange r) {
		for (UnicodeInterval other : r) {
			this.intersect(other);
		}
	}
	
	private void diff(UnicodeInterval diff) {
		Set<UnicodeInterval> newRange = new HashSet<UnicodeInterval>();
		for (UnicodeInterval i : this) {
			newRange.addAll(i.diff(diff).ranges);
		}
		this.ranges = newRange;
	}
	
	public void diff(UnicodeRange r) {
		for (UnicodeInterval other : r) {
			this.diff(other);
		}
	}
	
	public void invert(UnicodeInterval universe) {
		UnicodeRange r2 = this.clone();
		this.ranges.clear();
		this.ranges.add(universe);
		this.diff(r2);
		
	}

	public Iterator<UnicodeInterval> iterator() {
		return this.ranges.iterator();
	}

	public boolean isEmpty() {
		return ranges.isEmpty();
	}

	public void normalize() {
		Set<UnicodeInterval> newRange = new HashSet<UnicodeInterval>();
		for (UnicodeInterval i : this) {
			newRange.addAll(i.normalize());
		}
		this.ranges = newRange;
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ranges == null) ? 0 : ranges.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UnicodeRange other = (UnicodeRange) obj;
		if (ranges == null) {
			if (other.ranges != null)
				return false;
		} else if (!ranges.equals(other.ranges))
			return false;
		return true;
	}

	@Override
	public String toString() {
		/*StringBuilder builder = new StringBuilder();
		
		// This is for Debug purpose that we get an reasonable order of the intervals
		List<UnicodeInterval> intervalList = new ArrayList<UnicodeInterval>(this.ranges);
		Collections.sort(intervalList, new UnicodeIntervalComparator());

		builder.append('(');
		if (!this.ranges.isEmpty()) {
			int num = UnicodeConverter.isTwoByteCharacter(intervalList.get(0).x.intValue()) ? 2 : 4;
			for (UnicodeInterval r : intervalList) {
				builder.append("(");
				for (int i = num - 1; i >= 0; i--) {
					builder.append('[');
					long start = Math.min(r.x, r.y);
					long end = Math.max(r.x, r.y);
					// System.out.println("Range from " + start + " to " + end);
					builder.append("\\");
					builder.append((int) UnicodeConverter.getByte(i, (int)start));
					if (start != end) {
						builder.append('-');
						builder.append("\\");
						builder.append((int) UnicodeConverter.getByte(i, (int)end));
					}
					builder.append(']');
				}
				builder.append(")");
				builder.append('|');
			}
		}
		builder.deleteCharAt(builder.length() - 1);
		builder.append(')');
		return builder.toString();*/
		return this.ranges.toString();
	}
	
	public IStrategoTerm toAST() {
		// This is for Debug purpose that we get an reasonable order of the intervals
		List<UnicodeInterval> intervalList = new ArrayList<UnicodeInterval>(this.ranges);
		Collections.sort(intervalList, new UnicodeIntervalComparator());
		
		LinkedList<IStrategoTerm> orTerms = new LinkedList<IStrategoTerm> ();
		if (!this.ranges.isEmpty()) {
			int num = UnicodeConverter.isTwoByteCharacter(intervalList.get(0).x.intValue()) ? 2 : 4;
			if (UnicodeConverter.isAscii(intervalList.get(0).x.intValue())) {
				num = 1;
			}
			for (UnicodeInterval r : intervalList) {
				LinkedList<IStrategoTerm> seqBytes = new LinkedList<IStrategoTerm>();
				for (int i = num - 1; i >= 0; i--) {
					int start = UnicodeConverter.getByte(i,r.x.intValue());
					int end = UnicodeConverter.getByte(i,r.y.intValue());
					IStrategoTerm term;
					if (start != end) {
						term = UnicodeUtils.makeCharRange(start, end);
					} else {
						term = UnicodeUtils.makeCharClass(start);
					}
					seqBytes.add(UnicodeUtils.charClassToSymbol(term));
				}
				orTerms.add(UnicodeUtils.makeSymbolSeq(seqBytes));
			}
			return UnicodeUtils.makeOrSymbol(orTerms);
		} else {
			return UnicodeUtils.makeEmptySymbol();
		}
	}
	

}