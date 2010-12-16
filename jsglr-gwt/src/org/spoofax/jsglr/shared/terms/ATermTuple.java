package org.spoofax.jsglr.shared.terms;

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
	protected boolean simpleMatch(ATerm t) {
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

}
