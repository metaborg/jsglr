package org.spoofax.jsglr.shared.terms;

import java.util.Iterator;

import org.spoofax.jsglr.client.NotImplementedException;

public class ATermList extends ATerm implements Iterable<ATerm> {

	private static final long serialVersionUID = 1L;

	private ATerm element;
	private ATermList next;

	ATermList() {}

	ATermList(ATermFactory factory, ATerm element, ATermList next) {
		super(factory);
		this.element = element;
		this.next = next;
	}

	public ATermList prepend(ATerm t) {
		return new ATermList(factory, t, this);
	}

	public boolean isEmpty() {
		return element == null;
	}

	public ATerm getFirst() {
		return element;
	}

	public ATermList getNext() {
		return next;
	}

	@Override
	public ATerm getChildAt(int index) {
		ATermList l = this;
		for(int i = 0; i < index; i++) {
			l = l.next;
		}
		return l.element;
	}

	@Override
	public int getChildCount() {
		int pos = 0;
		ATermList l = this;
		while(l.element != null) {
			pos++;
			l = l.next;
		}
		return pos;
	}

	@Override
	public int getType() {
		return ATerm.LIST;
	}

	private static class ATermListIterator implements Iterator<ATerm> {
		private ATermList underlying;

		ATermListIterator(ATermList underlying) {
			this.underlying = underlying;
		}

		public boolean hasNext() {
			return underlying.element != null;
		}

		public ATerm next() {
			final ATerm e = underlying.element;
			underlying = underlying.next;
			return e;
		}

		public void remove() {
			throw new RuntimeException("Not supported");
		}
	}

	public Iterator<ATerm> iterator() {
		return new ATermListIterator(this);
	}

	@Override
	protected void toString(int depth, StringBuilder sb) {
		if(depth == 0) {
			sb.append("...");
		} else {
			sb.append('[');
			ATermList l = this;
			while(l.element != null) {
				l.element.toString(depth - 1, sb);
				l = l.next;
				if(l.element != null) {
					sb.append(",");
				}
			}
			sb.append(']');
		}
	}

	@Override
	public boolean simpleMatch(ATerm t) {
		if(!(t instanceof ATermList)) {
			return false;
		}
		ATermList a = (ATermList)t;
		ATermList b = (ATermList)t;
		do {
			if(a.element == null && b.element == null) {
				return true;
			}
			if(a.element == null) {
				return false;
			}
			if(!a.element.equals(b.element)) {
				return false;
			}
			a = a.next;
			b = b.next;
		} while(true);
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (o instanceof ATermAppl)
			throw new NotImplementedException();
		return false;
	}
	
	@Override
	public int hashCode() {
		throw new NotImplementedException();
	}
}
