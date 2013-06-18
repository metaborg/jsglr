package org.spoofax.jsglr.tests.unicode.interpreter;

import java.util.HashMap;

public class Environment {
	
	private HashMap<String, Object> values;
	private Environment parent;
	
	public Environment() {
		this.values = new HashMap<String, Object>();
		this.parent = null;
	}
	
	public Environment(Environment e) {
		this.values = new HashMap<String, Object>();
		this.parent = e;
	}
	
	public void put(String identifier, Object value) {
		this.values.put(identifier, value);
	}
	
	public Object get(String identifier) {
		if (this.values.containsKey(identifier)) {
			return this.values.get(identifier);
		} else if (this.parent != null) {
			return this.parent.get(identifier);
		}
		return null;
	}
	
	public boolean contains(String identifier) {
		if (this.values.containsKey(identifier)) {
			return true;
		} else if (this.parent != null) {
			return parent.contains(identifier);
		}
		return false;
	}

	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		Environment e = this;
		while (e != null) {
			b.append("Environment [values=" + e.values + ", parent=");
			e = e.parent;
		}
		b.append("null");
		return b.toString();
	}
	
	

	
}
