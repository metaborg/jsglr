package org.spoofax.jsglr.shared.terms;

public class AFun {

	private final String name;
	private final int arity;
	private final boolean quoted;

	public AFun(String name, int arity, boolean quoted) {
		this.name = name;
		this.arity = arity;
		this.quoted = quoted;
	}

	public int getArity() {
		return arity;
	}

	public String getName() {
		return name;
	}

}
