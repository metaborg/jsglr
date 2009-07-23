package org.spoofax.interpreter.library.jsglr;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.spoofax.interpreter.adapter.aterm.WrappedATerm;
import org.spoofax.interpreter.adapter.aterm.WrappedATermFactory;
import org.spoofax.interpreter.core.IContext;
import org.spoofax.interpreter.core.InterpreterException;
import org.spoofax.interpreter.core.Tools;
import org.spoofax.interpreter.stratego.Strategy;
import org.spoofax.interpreter.terms.IStrategoAppl;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.interpreter.terms.TermConverter;
import org.spoofax.jsglr.ParseTable;
import org.spoofax.jsglr.SGLR;
import org.spoofax.jsglr.SGLRException;

import aterm.ATermFactory;

public class JSGLR_parse_string_pt extends JSGLRPrimitive {

	private final WrappedATermFactory factory;
	
	private SGLRException lastException;
	
	private String lastPath;

	protected JSGLR_parse_string_pt(WrappedATermFactory termFactory) {
		super("JSGLR_parse_string_pt", 1, 4);
		this.factory = termFactory;
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

		lastPath = Tools.asJavaString(tvars[3]);
		Strategy onParseError = svars[0];
		
		JSGLRLibrary lib = getLibrary(env);
		ParseTable pt = lib.getParseTable(Tools.asJavaInt(tvars[1]));
		if (pt == null)
			return false;
		
		SGLR parser = makeSGLR(factory.getFactory(), pt);
		
		InputStream is = new ByteArrayInputStream(Tools.asJavaString(tvars[0]).getBytes());
		try {
			IStrategoTerm result = factory.wrapTerm(parser.parse(is, startSymbol));
			if (!(tvars[0] instanceof WrappedATerm))
				result = TermConverter.convert(env.getFactory(), result);
			
			env.setCurrent(result);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SGLRException e) {
			lastException = e;
			IStrategoTerm errorTerm = factory.wrapTerm(e.toTerm(lastPath));
			env.setCurrent(TermConverter.convert(env.getFactory(), errorTerm));
			
			// TODO: Stratego doesn't seem to print the erroneous line in Java
			return onParseError.evaluate(env);
		}
		return false;
	}

	// overridden in JSGLR_parse_string_compat
	protected SGLR makeSGLR(ATermFactory factory, ParseTable table) {
		return new SGLR(factory, table);
	}
}
