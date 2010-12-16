package org.spoofax.jsglr.shared.terms;

import java.util.List;

import org.spoofax.jsglr.client.NotImplementedException;

public class ATermAppl extends ATerm {

	private final AFun ctor;
	private final ATerm[] kids;

	ATermAppl(ATermFactory factory, AFun ctor, ATerm... kids) {
		super(factory);
		this.ctor = ctor;
		this.kids = kids;
	}

	@Override
	public int getChildCount() {
		return ctor.getArity();
	}

	public String getName() {
		return ctor.getName();
	}

	@Override
	public ATerm getChildAt(int i) {
		return kids[i];
	}

	@Override
	public int getType() {
		return ATerm.APPL;
	}

	public List match(ATerm term) {
		throw new NotImplementedException();
	}

	public AFun getAFun() {
		return ctor;
	}


}
