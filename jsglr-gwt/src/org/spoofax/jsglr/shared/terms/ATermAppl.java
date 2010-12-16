package org.spoofax.jsglr.shared.terms;

import java.util.List;

import org.spoofax.jsglr.client.NotImplementedException;

public class ATermAppl extends ATerm {

	private static final long serialVersionUID = 1L;
	
	private AFun ctor;
	private ATerm[] kids;

	ATermAppl() {}
	
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

	public boolean match(ATerm term) {
		throw new NotImplementedException();
	}

	public AFun getAFun() {
		return ctor;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(ctor.getName());
		sb.append('(');
		for(int i = 0; i < kids.length; i++) {
			if(i > 0)
				sb.append(",");
			sb.append(kids[i].toString());
		}
		sb.append(')');
		return sb.toString();
	}

}
