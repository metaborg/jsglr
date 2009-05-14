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
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.interpreter.terms.TermConverter;
import org.spoofax.jsglr.ParseTable;
import org.spoofax.jsglr.SGLR;
import org.spoofax.jsglr.SGLRException;

import aterm.ATermFactory;

public class JSGLR_parse_string_pt extends JSGLRPrimitive {

	private WrappedATermFactory factory;

	protected JSGLR_parse_string_pt(WrappedATermFactory termFactory) {
		super("JSGLR_parse_string_pt", 1, 4);
		this.factory = termFactory;
	}

	@Override
	public boolean call(IContext env, Strategy[] svars, IStrategoTerm[] tvars)
			throws InterpreterException {
		
		if(!Tools.isTermString(tvars[0]))
			return false;
		if(!Tools.isTermInt(tvars[1]))
			return false;
		
		JSGLRLibrary lib = getLibrary(env);
		ParseTable pt = lib.getParseTable(Tools.asJavaInt(tvars[1]));
		
		SGLR parser = makeSGLR(factory.getFactory(), pt);
		// parser.setFilter(true);
		// parser.setHeuristicFilters(false);
		
		InputStream is = new ByteArrayInputStream(Tools.asJavaString(tvars[0]).getBytes());
		try {
			IStrategoTerm result = factory.wrapTerm(parser.parse(is));
			System.out.println(result);
			if (!(tvars[0] instanceof WrappedATerm))
				result = TermConverter.convert(env.getFactory(), result);
			
			env.setCurrent(result);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SGLRException e) {
			e.printStackTrace();
		}
		return false;
	}

	protected SGLR makeSGLR(ATermFactory factory, ParseTable table) {
		return new SGLR(factory, table);
	}
}
