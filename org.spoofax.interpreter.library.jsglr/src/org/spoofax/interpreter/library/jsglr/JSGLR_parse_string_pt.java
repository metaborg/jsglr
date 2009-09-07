package org.spoofax.interpreter.library.jsglr;

import static org.spoofax.interpreter.core.Tools.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

import org.spoofax.interpreter.adapter.aterm.WrappedATerm;
import org.spoofax.interpreter.adapter.aterm.WrappedATermFactory;
import org.spoofax.interpreter.core.IContext;
import org.spoofax.interpreter.core.InterpreterException;
import org.spoofax.interpreter.core.Tools;
import org.spoofax.interpreter.library.IOAgent;
import org.spoofax.interpreter.library.ssl.SSLLibrary;
import org.spoofax.interpreter.stratego.Strategy;
import org.spoofax.interpreter.terms.IStrategoAppl;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.interpreter.terms.TermConverter;
import org.spoofax.jsglr.ParseTable;
import org.spoofax.jsglr.SGLR;
import org.spoofax.jsglr.SGLRException;

public class JSGLR_parse_string_pt extends JSGLRPrimitive {

	private final WrappedATermFactory factory;
	
	private SGLRException lastException;
	
	private String lastPath;

	protected JSGLR_parse_string_pt(WrappedATermFactory factory) {
		super("JSGLR_parse_string_pt", 1, 4);
		this.factory = factory;
	}
	
	protected JSGLR_parse_string_pt(WrappedATermFactory factory, String name, int svars, int tvars) {
		super(name, svars, tvars);
		this.factory = factory;
	}
	
	public String getLastPath() {
		return lastPath;
	}
	
	public SGLRException getLastException() {
		return lastException;
	}
	
	public void clearLastException() {
		lastException = null;
	}

	@Override
	public boolean call(IContext env, Strategy[] svars, IStrategoTerm[] tvars)
			throws InterpreterException {
		
		clearLastException();
		
		if (!Tools.isTermString(tvars[0]))
			return false;
		if (!Tools.isTermInt(tvars[1]))
			return false;
		if(!Tools.isTermString(tvars[3]))
			return false;

		String startSymbol;
		if (Tools.isTermString(tvars[2])) {
			startSymbol = Tools.asJavaString(tvars[2]);
		} else if (tvars[2].getSubtermCount() == 0 && tvars[2].getTermType() == IStrategoTerm.APPL && ((IStrategoAppl) tvars[2]).getConstructor().getName().equals("None")) {
			startSymbol = null;
		} else {
			return false;
		}

		lastPath = asJavaString(tvars[3]);
		
		JSGLRLibrary lib = getLibrary(env);
		ParseTable table = lib.getParseTable(asJavaInt(tvars[1]));
		if (table == null)
			return false;

		try {
			IStrategoTerm result = call(env, asJavaString(tvars[0]), table, startSymbol, tvars[0] instanceof WrappedATerm);
			env.setCurrent(result);
			return result != null;
		} catch (IOException e) {
			PrintStream err = SSLLibrary.instance(env).getIOAgent().getOutputStream(IOAgent.CONST_STDERR);
			err.println("JSGLR_parse_string_pt: could not parse " + getLastPath() + " - " + e.getMessage());
			return false;
		} catch (SGLRException e) {
			lastException = e;
			IStrategoTerm errorTerm = factory.wrapTerm(e.toTerm(lastPath));
			env.setCurrent(TermConverter.convert(env.getFactory(), errorTerm));
			
			// FIXME: Stratego doesn't seem to print the erroneous line in Java
			return svars[0].evaluate(env);
		}
	}
	
	public IStrategoTerm call(IContext env, String input,
			ParseTable table, String startSymbol, boolean outputWrappedATerm)
			throws InterpreterException, IOException, SGLRException {
		
		SGLR parser = new SGLR(factory.getFactory(), table);
		
		InputStream is = new ByteArrayInputStream(input.getBytes());
		IStrategoTerm result = factory.wrapTerm(parser.parse(is, startSymbol));
		if (!outputWrappedATerm)
			result = TermConverter.convert(env.getFactory(), result);
		
		return result;
	}
}
