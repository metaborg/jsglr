/*
 * Copyright (c) 2005-2011, Karl Trygve Kalleberg <karltk near strategoxt dot org>
 * 
 * Licensed under the GNU Lesser General Public License, v2.1
 */
package org.spoofax.interpreter.library.jsglr;

import static org.spoofax.interpreter.core.Tools.asJavaInt;
import static org.spoofax.interpreter.core.Tools.asJavaString;

import java.io.IOException;

import org.spoofax.interpreter.core.IContext;
import org.spoofax.interpreter.core.InterpreterException;
import org.spoofax.interpreter.core.Tools;
import org.spoofax.interpreter.library.IOAgent;
import org.spoofax.interpreter.library.ssl.SSLLibrary;
import org.spoofax.interpreter.stratego.Strategy;
import org.spoofax.interpreter.terms.IStrategoAppl;
import org.spoofax.interpreter.terms.IStrategoString;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr.client.Asfix2TreeBuilder;
import org.spoofax.jsglr.client.FilterException;
import org.spoofax.jsglr.client.ParseTable;
import org.spoofax.jsglr.client.SGLR;
import org.spoofax.jsglr.shared.SGLRException;

public class JSGLR_parse_string_pt extends JSGLRPrimitive {
	
	private SGLRException lastException;
	
	private String lastPath;
	
	protected JSGLR_parse_string_pt() {
		super("JSGLR_parse_string_pt", 1, 4);
	}
	
	protected JSGLR_parse_string_pt(String name, int svars, int tvars) {
		super(name, svars, tvars);
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

	/**
	 * tvars: 0 => input string, 1 => table, 2 => startsymbol, 3 => path
	 */
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
		
		ParseTable table = getParseTable(env, tvars);
		if (table == null)
			return false;

		try {
			IStrategoTerm result = call(env, (IStrategoString) tvars[0], table, startSymbol);
			env.setCurrent(result);
			return result != null;
		} catch (IOException e) {
			IOAgent io = SSLLibrary.instance(env).getIOAgent();
			io.printError("JSGLR_parse_string_pt: could not parse " + getLastPath() + " - " + e.getMessage());
			return false;
		} catch (SGLRException e) {
			lastException = e;
			IStrategoTerm errorTerm = e.toTerm(lastPath);
			if (e instanceof FilterException) {
				// HACK: print stack trace for this internal error
				e.printStackTrace();
			}
			env.setCurrent(errorTerm);
			
			// FIXME: Stratego doesn't seem to print the erroneous line in Java
			return svars[0].evaluate(env);
		}
	}

	protected ParseTable getParseTable(IContext env, IStrategoTerm[] tvars) {
		JSGLRLibrary lib = getLibrary(env);
		ParseTable table = lib.getParseTable(asJavaInt(tvars[1]));
		return table;
	}
	
	protected IStrategoTerm call(IContext env, IStrategoString input,
			ParseTable table, String startSymbol)
			throws InterpreterException, IOException, SGLRException {
		
		SGLR parser = new SGLR(new Asfix2TreeBuilder(env.getFactory()), table);
		IStrategoTerm result = (IStrategoTerm) parser.parse(input.stringValue(), null, startSymbol);
		
		return result;
	}
}
