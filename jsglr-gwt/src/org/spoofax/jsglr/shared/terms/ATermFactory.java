package org.spoofax.jsglr.shared.terms;

import java.io.InputStream;

import org.spoofax.jsglr.client.NotImplementedException;

public class ATermFactory {

	public AFun makeAFun(String ctorName, int arity, boolean quoted) {
		return new AFun(ctorName, arity, quoted);
	}

	public ATermList makeList() {
		return new ATermList(this);
	}

	public ATermList makeList(ATerm... elements) {
		return new ATermList(this, elements);
	}

	public ATerm parse(String string) {
		throw new NotImplementedException();
	}

	public ATerm makeAppl(AFun afun, ATermList kids) {
		return new ATermAppl(this, afun, kids);
	}

	public ATerm readFromFile(InputStream stream) {
		throw new NotImplementedException();
	}

	public ATerm readFromFile(String stream) {
		throw new NotImplementedException();
	}

	public ATerm makeAppl(AFun afun, ATerm... kids) {
		return new ATermAppl(this, afun, kids);
	}

	public ATerm makeInt(int i) {
		return new ATermInt(this, i);
	}

}
