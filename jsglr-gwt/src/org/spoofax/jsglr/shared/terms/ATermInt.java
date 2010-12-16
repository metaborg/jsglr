package org.spoofax.jsglr.shared.terms;

public class ATermInt extends ATerm {

	private static final long serialVersionUID = 1L;
	
	private int value;
	
	ATermInt() {}

	ATermInt(ATermFactory factory, int value) {
		super(factory);
		this.value = value;
	}

	@Override
	public int getChildCount() {
		return 0;
	}

	public int getInt() {
		return value;
	}

	@Override
	public ATerm getChildAt(int i) {
		return null;
	}

	@Override
	public int getType() {
		return ATerm.INT;
	}

	@Override
	public String toString() {
		return Integer.toString(value);
	}

	@Override
	protected boolean simpleMatch(ATerm t) {
		if(!(t instanceof ATermInt))
			return false;
		ATermInt a = (ATermInt)t;
		return a.value == value;
	}
}
