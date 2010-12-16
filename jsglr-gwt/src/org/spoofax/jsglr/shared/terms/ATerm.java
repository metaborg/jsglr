package org.spoofax.jsglr.shared.terms;

import java.util.List;

import org.spoofax.jsglr.client.NotImplementedException;

public abstract class ATerm {

	public static final int INT = 1;
	public static final int APPL = 2;
	public static final int LIST = 3;

	protected final ATermFactory factory;

	protected ATerm(ATermFactory factory) {
		this.factory = factory;
	}

	public abstract int getChildCount();

	public abstract ATerm getChildAt(int i);

	public abstract int getType();

	public ATermFactory getFactory() {
		return factory;
	}

	public List match(String termAsString) throws ParseError {
		throw new NotImplementedException();
	}

	public List match(ATerm litStringAppl) {
		throw new NotImplementedException();
	}

}
