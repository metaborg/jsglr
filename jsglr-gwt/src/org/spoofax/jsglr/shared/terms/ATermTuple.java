package org.spoofax.jsglr.shared.terms;

import org.spoofax.jsglr.client.NotImplementedException;

public class ATermTuple extends ATerm {

	private static final long serialVersionUID = 1L;
	private ATerm[] elements;

	ATermTuple() {}
	
	ATermTuple(ATermFactory factory, ATerm[] elements) {
		super(factory);
		this.elements = elements;
	}
	
	@Override
	public int getChildCount() {
		return elements.length;
	}

	@Override
	public ATerm getChildAt(int i) {
		return elements[i];
	}

	@Override
	public int getType() {
		return ATerm.TUPLE;
	}

	@Override
	public boolean simpleMatch(ATerm t) {
		if(!(t instanceof ATermTuple))
			return false;
		ATermTuple o = (ATermTuple)t;
		if(elements.length != o.elements.length)
			return false;
		for(int i = 0; i < elements.length; i++)
			if(elements[i].equals(o.elements[i]))
				return false;
		return true;
	}

	@Override
	protected void toString(int depth, StringBuilder sb) {
		if(depth == 0) {
			sb.append("...");
		} else {
			sb.append('(');
			for(int i = 0; i < elements.length; i++) {
				if(i > 0)
					sb.append(",");
				elements[i].toString(depth - 1, sb);
			}
			sb.append(')');
		}
	}
	
	@Override
	public int hashCode() {
		throw new NotImplementedException();
	}
}
