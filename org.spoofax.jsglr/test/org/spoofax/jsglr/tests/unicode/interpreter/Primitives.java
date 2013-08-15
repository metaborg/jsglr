package org.spoofax.jsglr.tests.unicode.interpreter;

import org.spoofax.interpreter.terms.IStrategoTerm;

public class Primitives {
	
	private static Integer evalInteger(IStrategoTerm term, Environment e, String error) {
		Object value = Interpreter.eval(term, e);
		if (!(value instanceof Integer)) {
			throw new RuntimeException(error + " must evaluate to an Integer but got " + value);
		}
		return (Integer) value;
	}

	public static Object if_impl(IStrategoTerm conditionTerm, IStrategoTerm thenTerm, IStrategoTerm elseTerm, Environment e) {
		Integer condition = evalInteger(conditionTerm, e, "Condition");
		if (condition == 0) {
			return Interpreter.eval(elseTerm, e);
		} else {
			return Interpreter.eval(thenTerm, e);
		}
	}
	
	public static Object and_impl(IStrategoTerm phiTerm, IStrategoTerm psiTerm, Environment e) {
		Integer phi = evalInteger(phiTerm, e, "First and argument");
		if ( phi != 0) {
			Integer psi = evalInteger(psiTerm, e, "Second and argument");
			if (psi != 0) {
				return 1;
			}
		}
		return 0;
	}
	
	public static Object ident_impl(IStrategoTerm firstTerm, IStrategoTerm secondTerm, Environment e) {
		Object first = Interpreter.eval(firstTerm, e);
		Object second = Interpreter.eval(secondTerm, e);
		if (first == null && second == null) {
			return 1;
		}
		if (first == null) {
			return 0;
		}
		if (first.equals(second)) {
			return 1;
		}
		return 0;
	}
	
	public static Object equal_impl(IStrategoTerm firstTerm, IStrategoTerm secondTerm, Environment e) {
		Object first = Interpreter.eval(firstTerm, e);
		Object second = Interpreter.eval(secondTerm, e);
		return first == second ? 1 : 0;
	}
	
	
	public static Object null_impl(Environment e) {
		return null;
	}
	
	public static Object unique_impl(Environment e) {
		return new Object();
	}
	
	public static Object times_impl(IStrategoTerm mul1Term, IStrategoTerm mul2Term, Environment e) {
		Integer mul1 = evalInteger(mul1Term, e, "First times argument");
		Integer mul2 = evalInteger(mul2Term, e, "Second times argument");
		return mul1 * mul2;
	}
	
	public static Object add_impl(IStrategoTerm mul1Term, IStrategoTerm mul2Term, Environment e) {
		Integer mul1 = evalInteger(mul1Term, e, "First subtract argument");
		Integer mul2 = evalInteger(mul2Term, e, "Second subtract argument");
		return mul1 + mul2;
	}
	
	public static Object negate_impl(IStrategoTerm mul1Term, Environment e) {
		Integer mul1 = evalInteger(mul1Term, e, "First subtract argument");
		return -mul1;
	}
	
	public static Object print_impl(IStrategoTerm printTerm, Environment e) {
		Object value = Interpreter.eval(printTerm, e);
		System.out.println(value);
		return value;
	}
	

}
