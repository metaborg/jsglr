package org.spoofax.jsglr.tests.unicode.interpreter;

import java.lang.reflect.InvocationTargetException;

import org.spoofax.interpreter.terms.IStrategoList;
import org.spoofax.interpreter.terms.IStrategoTerm;
import static org.spoofax.terms.Term.*;

public class Interpreter {

	public static Object eval(IStrategoTerm term) {
		Object result = eval(term, new Environment());
		if (!(result instanceof Closure)) {
			throw new RuntimeException("Top Level is not a Lambda");
		}
		System.out.println("Eval final lambda");
		Closure lambda = (Closure) result;
		if (lambda.getOperands().length != 0) {
			throw new RuntimeException("Top Level Lambda is not allowed to have operands");
		}
		return evalLambdaApp(lambda, new IStrategoTerm[0], null);
	}
	
	public static Object eval(IStrategoTerm term, Environment e) {
		//System.out.println(term);
		//System.out.println(e);
		String type = tryGetConstructor(term).getName();
		if (type.equals("Integer")) {
			return evalInteger(term.getSubterm(0));
		} else if (type.equals("Identifier")) {
			return evalIdentifier(term.getSubterm(0), e);
		} else if (type.equals("Lambda")) {
			return evalLambda(term, e, Closure.class);
		} else if (type.equals("JavaLambda")) {
			return evalLambda(term, e, JavaClosure.class);
		} else if (type.equals("LambdaApp")) {
			return evalLambdaApp(term, 0, 1, e);
		} else if (type.equals("ExpLambdaApp")) {
			return evalLambdaApp(term, 1, 3, e);
		} else if (type.equals("Let")) {
			return evalLet(term, e);
		}
		throw new RuntimeException("Unsupported term type "+ type);
	}
	
	private static Integer evalInteger(IStrategoTerm term) {
		int i = Integer.parseInt(asJavaString(term));
		return i;
	}
	
	private static Object evalIdentifier(IStrategoTerm term, Environment e) {
		String identifier = asJavaString(term);
		if (!e.contains(identifier)) {
			throw new RuntimeException("Unbound variable " + identifier +"in\n"+term.toString());
		}
		return e.get(identifier);
	}
	
	private static Closure evalLambda(IStrategoTerm term, Environment e, Class<? extends Closure> closureClass) {
		IStrategoTerm expression = term.getSubterm(2);
		IStrategoList operandsList = (IStrategoList)term.getSubterm(1);
		String[] operands = new String[operandsList.getSubtermCount()];
		for (int i = 0; i < operandsList.getSubtermCount(); i++) {
			operands[i] = asJavaString(operandsList.getSubterm(i));
		}
		try {
			return closureClass.getConstructor(IStrategoTerm.class, String[].class, Environment.class).newInstance(expression,operands,  e);
		} catch (Exception e1) {
			throw new RuntimeException("Failed to create closure for " + closureClass);
		}
	}

	private static Object evalLambdaApp(IStrategoTerm term, int lambdaPos, int operandsPos, Environment e) {
		IStrategoTerm lambdaExpr = term.getSubterm(lambdaPos);
		Object lambda = eval(lambdaExpr, e);
		if (! (lambda instanceof Closure)) {
			throw new RuntimeException("Expected a Lambda but got " + lambda);
		}
		IStrategoList operands = (IStrategoList) term.getSubterm(operandsPos);
		return evalLambdaApp((Closure)lambda, operands.getAllSubterms(), e);
	}
	
	private static Object evalLambdaApp(Closure lambda, IStrategoTerm[] operands, Environment e) {
		return lambda.eval(operands, e);
	}
	
	private static Object evalLet(IStrategoTerm term, Environment e) {
		IStrategoTerm declarationTerm = term.getSubterm(0);
		IStrategoTerm expTerm = term.getSubterm(1);
		IStrategoList declarations = (IStrategoList) declarationTerm.getSubterm(1);
		Environment enew = new Environment(e);
		for (int i = 0; i < declarations.getSubtermCount(); i++) {
			evalDelcaration(declarations.getSubterm(i), enew, enew);
		}
		return eval(expTerm, enew);
	}
	
	private static void evalDelcaration(IStrategoTerm term, Environment e, Environment eput) {
		String identifier = asJavaString(term.getSubterm(0));
		IStrategoTerm expr = term.getSubterm(2);
		eput.put(identifier, eval(expr,e));
	}

}
