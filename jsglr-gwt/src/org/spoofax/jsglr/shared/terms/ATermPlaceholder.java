package org.spoofax.jsglr.shared.terms;

public class ATermPlaceholder extends ATermAppl {

	private static final long serialVersionUID = 1L;
	
	ATermPlaceholder() {}
	
	public ATermPlaceholder(ATermFactory factory, AFun fun, ATerm template) {
		super(factory, fun, template);
	}

	@Override
	public int getType() {
		return ATerm.PLACEHOLDER;
	}
	
	public ATerm getPlaceholder() {
		return getChildAt(0);
	}

}
