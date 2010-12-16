package org.spoofax.jsglr.shared.terms;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import org.spoofax.jsglr.client.NotImplementedException;
import org.spoofax.jsglr.client.imploder.IToken;

public abstract class ATerm implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public static final int INT = 1;
	public static final int APPL = 2;
	public static final int LIST = 3;
	public static final int STRING = 4;
	public static final int TUPLE = 5;
	public static final int PLACEHOLDER = 6;

	protected ATermFactory factory;
	
	private IToken leftToken, rightToken;

	ATerm() {}
	
	protected ATerm(ATermFactory factory) {
		this.factory = factory;
	}

	public abstract int getChildCount();

	public abstract ATerm getChildAt(int i);

	public abstract int getType();
	
	public void internalSetTokens(IToken leftToken, IToken rightToken) {
		this.leftToken = leftToken;
		this.rightToken = rightToken;
	}
	
	public IToken getLeftToken() {
		return leftToken;
	}
	
	public IToken getRightToken() {
		return rightToken;
	}

	public ATermFactory getFactory() {
		return factory;
	}

	public List match(String termAsString) throws ParseError {
		ATerm t = factory.parse(termAsString);
		if(simpleMatch(t)) {
			return new LinkedList();
		} else {
			return null;
		}
	}
	
	public boolean equals(Object o) {
		return simpleMatch((ATerm) o);
	}

	public abstract boolean simpleMatch(ATerm t);
	
	public abstract int hashCode();

	public boolean match(ATerm litStringAppl) {
		throw new NotImplementedException();
	}

	@Override
	public final String toString() {
		StringBuilder sb = new StringBuilder();
		toString(8, sb);
		return sb.toString();
	}

	protected abstract void toString(int depth, StringBuilder sb);
}
