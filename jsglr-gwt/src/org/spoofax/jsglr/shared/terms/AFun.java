package org.spoofax.jsglr.shared.terms;

import java.io.Serializable;

public class AFun implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String name;
	private int arity;

	AFun() {}
	
	AFun(String name, int arity, boolean quoted) {
		this.name = name;
		this.arity = arity;
	}

	public int getArity() {
		return arity;
	}

	public String getName() {
		return name;
	}

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof AFun))
			return false;
		AFun o = (AFun)obj;
		return o.arity == arity && o.name.equals(name);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + arity;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}
}
