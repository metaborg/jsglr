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

}
