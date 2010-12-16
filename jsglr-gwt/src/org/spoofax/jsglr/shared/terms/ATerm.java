package org.spoofax.jsglr.shared.terms;

import java.io.Serializable;
import java.util.List;

import org.spoofax.jsglr.client.NotImplementedException;

public abstract class ATerm implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public static final int INT = 1;
	public static final int APPL = 2;
	public static final int LIST = 3;
	public static final int STRING = 4;
	public static final int TUPLE = 5;
	public static final int PLACEHOLDER = 6;

	protected ATermFactory factory;

	ATerm() {}
	
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

	public boolean match(ATerm litStringAppl) {
		throw new NotImplementedException();
	}

}
