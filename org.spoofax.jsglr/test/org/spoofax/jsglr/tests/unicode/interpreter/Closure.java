package org.spoofax.jsglr.tests.unicode.interpreter;

import java.util.Arrays;

import org.spoofax.interpreter.terms.IStrategoTerm;

public class Closure {

	protected Environment e;
	protected String[] operands;
	protected IStrategoTerm lambda;
	public Closure(IStrategoTerm lambda, String[] operands, Environment e) {
		super();
		this.e = new Environment(e);
		this.lambda = lambda;
		this.operands = operands;
	}
	public Environment getE() {
		return e;
	}
	public IStrategoTerm getLambda() {
		return lambda;
	}
	
	public String[] getOperands() {
		return operands;
	}
	
	public Object eval(IStrategoTerm[] operands, Environment e) {
		Environment enew = new Environment(this.e);
		if (this.operands.length != operands.length) {
			throw new RuntimeException("Wrong number of operands: Expected " + this.operands.length + " but got " + operands.length + "for\n" + this);
		}
		for (int i = 0; i < this.operands.length; i++) {
			enew.put(this.operands[i], Interpreter.eval(operands[i], e));
		}
		return Interpreter.eval(this.lambda, enew);
	}
	
	@Override
	public String toString() {
		return "Closure [operands="/* + Arrays.toString(operands) + ", lambda=" + lambda + ", e=" + e + "*/;
	}
	
	
	

}
