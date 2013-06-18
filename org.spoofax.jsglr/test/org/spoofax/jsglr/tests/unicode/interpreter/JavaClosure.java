package org.spoofax.jsglr.tests.unicode.interpreter;

import java.lang.reflect.Method;

import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.terms.Term;

public class JavaClosure extends Closure {

	public JavaClosure(IStrategoTerm lambda, String[] operands, Environment e) {
		super(lambda, operands, e);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Object eval(IStrategoTerm[] operands, Environment e) {
		if (this.operands.length != operands.length) {
			throw new RuntimeException("Wrong number of arguments. Expected " + this.operands.length + " but got "+ operands.length );
		}
		String javaMethod = Term.asJavaString(this.lambda);
		try  {
			Method invoke = getMethod(javaMethod);
			return invokeMethod(invoke, operands, e);
		} catch (Exception e2) {
			e2.printStackTrace();
			throw new RuntimeException("Java Lambda failed.",e2);
		}
	}

	private Method getMethod(String lambda) throws Exception {
		String[] s = lambda.split("#");
		if (s.length != 2) {
			throw new RuntimeException("Invalid Java Method name " + lambda);
		}
		Class<?> c = Class.forName(s[0]);
		Class<?>[] params = new Class[this.operands.length+1];
		for (int i = 0; i < this.operands.length; i++) {
			params[i] = IStrategoTerm.class;
		}
		params[this.operands.length] = Environment.class;
		Method m = c.getMethod(s[1], params);
		return m;
	}
	
	private Object invokeMethod(Method method, IStrategoTerm[] operands, Environment e) throws Exception {
		Object[] params = new Object[this.operands.length+1];
		System.arraycopy(operands, 0, params, 0, operands.length);
		params[this.operands.length] = e;
		return method.invoke(null, params);
	}
	

}
