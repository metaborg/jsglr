package org.spoofax.jsglr.shared.terms;

public class ATermString extends ATerm {

	private static final long serialVersionUID = 1L;
	
	private String value;
	
	ATermString() {}
	
	ATermString(ATermFactory factory, String value) {
		super(factory);
		this.value = value;
	}
	
	@Override
	public int getChildCount() {
		return 0;
	}

	@Override
	public ATerm getChildAt(int i) {
		throw new ArrayIndexOutOfBoundsException();
	}

	@Override
	public int getType() {
		return ATerm.STRING;
	} 
	
	@Override
	public String toString() {
		return "\"" + value + "\"";
	}

	@Override
	protected boolean simpleMatch(ATerm t) {
		if(!(t instanceof ATermString))
			return false;
		ATermString o = (ATermString)t;
		return o.value.equals(value);
	}
}
