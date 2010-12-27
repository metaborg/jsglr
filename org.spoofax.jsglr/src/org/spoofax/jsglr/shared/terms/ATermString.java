package org.spoofax.jsglr.shared.terms;

import java.io.IOException;

/**
 * A String term.
 * 
 * For compatibility with the traditional ATerm library,
 * this class extends ATermAppl.
 * 
 * @author Lennart Kats <lennart add lclnet.nl>
 */
public class ATermString extends ATermAppl {

	private static final long serialVersionUID = 1L;
	
	private String value;
	
	ATermString() {}
	
	ATermString(ATermFactory factory, String value) {
		super(factory, null, ATermFactory.EMPTY);
		this.value = value;
		assert value != null;
	}
	
	@Override
	public int getChildCount() {
		return 0;
	}
	
	public String getString() {
		return value;
	}
	
	@Deprecated
	@Override
	public String getName() {
		return value;
	}
	
	@Override
	public AFun getAFun() {
		throw new UnsupportedOperationException();
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
	public void writeTo(Appendable sb, int depth) throws IOException {
		if(depth == 0) {
			sb.append("...");
		} else {
			sb.append('"');
			sb.append(value);
			sb.append('\"');
		}
	}

	@Override
	public boolean simpleMatch(ATerm t) {
		if(!(t instanceof ATermString))
			return false;
		ATermString o = (ATermString)t;
		return o.value.equals(value);
	}
	
	@Override
	public int hashCode() {
		return value.hashCode();
	}
}
