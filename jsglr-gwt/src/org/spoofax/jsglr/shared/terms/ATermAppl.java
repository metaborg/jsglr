package org.spoofax.jsglr.shared.terms;

import java.io.IOError;
import java.io.IOException;

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
	
	@Override
	public int hashCode() {
		throw new NotImplementedException();
	}

	public AFun getAFun() {
		return ctor;
	}

	@Override
	public void writeTo(Appendable sb, int depth) throws IOException {
		if(depth == 0) {
			sb.append("...");
		} else {
			sb.append(ctor.getName());
			sb.append('(');
			for(int i = 0; i < kids.length; i++) {
				if(i > 0)
					sb.append(",");
				kids[i].writeTo(sb, depth - 1);
			}
			sb.append(')');
		}
	}

	@Override
	public boolean simpleMatch(ATerm t) {
		if(!(t instanceof ATermAppl))
			return false;
		ATermAppl o = (ATermAppl)t;
		if(o.kids.length != kids.length)
			return false;
		for(int i = 0; i < kids.length; i++)
			if(!kids[i].simpleMatch(o.kids[i])) {
				System.out.println(kids[i] + "\n  !=  \n"   + o.kids[i]);
				return false;
			}
		return ctor.equals(o.ctor);
	}

}
