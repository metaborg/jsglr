/*
 * Copyright (c) 2005-2011, Karl Trygve Kalleberg <karltk near strategoxt dot org>
 * 
 * Licensed under the GNU Lesser General Public License, v2.1
 */
package org.spoofax.interpreter.library.jsglr;

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
import org.spoofax.jsglr.client.FilterException;
import org.spoofax.jsglr.client.InvalidParseTableException;
import org.spoofax.jsglr.client.ParseTable;
import org.spoofax.jsglr.client.SGLR;
import org.spoofax.jsglr.client.imploder.TermTreeFactory;
import org.spoofax.jsglr.client.imploder.TreeBuilder;
import org.spoofax.jsglr.shared.SGLRException;
import org.spoofax.terms.TermFactory;

public class JSGLR_recover_parse_string extends JSGLRPrimitive {
	
	// TODO: remove me -- this is redundant and way to complex; there's also a jsglr-enable-recovery
	
	private int cursorLocation;

	protected JSGLR_recover_parse_string() {
		super("JSGLR_recover_parse_string", 1, 5);
	}

	/**
	 * tvars: 0 => input string, 1 => parse table term, 2 => startsymbol, 3 => path
	 */
	@Override
	public boolean call(IContext env, Strategy[] svars, IStrategoTerm[] tvars)
			throws InterpreterException {
		if (Tools.isTermInt(tvars[4])) {
			cursorLocation = Tools.asJavaInt(tvars[4]);
		} else if (tvars[4].getSubtermCount() == 0 && tvars[4].getTermType() == IStrategoTerm.APPL && ((IStrategoAppl) tvars[2]).getConstructor().getName().equals("None")) {
			cursorLocation = -1;
		} else {
			return false;
		}
		if (!Tools.isTermString(tvars[0]))
			return false;
		if (!isParseTableTerm(tvars[1]))
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

		String lastPath = asJavaString(tvars[3]);
		
		ParseTable table = getParseTable(env, tvars);
		if (table == null)
			return false;
		try {
			IStrategoTerm result = call(env, (IStrategoString) tvars[0], table, startSymbol);
			env.setCurrent(result);
			return result != null;
		} catch (IOException e) {
			IOAgent io = SSLLibrary.instance(env).getIOAgent();
			io.printError("JSGLR_recover_parse_string: could not parse " + lastPath + " - " + e.getMessage());
			return false;
		} catch (SGLRException e) {
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

	private boolean isParseTableTerm(IStrategoTerm pt) {
		return pt.getTermType() == IStrategoTerm.APPL && ((IStrategoAppl) pt).getConstructor().getName().equals("parse-table");
	}

	protected ParseTable getParseTable(IContext env, IStrategoTerm[] tvars) {
		final TermFactory factory = new TermFactory();
		IStrategoTerm tableTerm = tvars[1];		
		try {
			return new ParseTable(tableTerm, factory);
		} catch (InvalidParseTableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} 
		 
	}

	protected IStrategoTerm call(IContext env, IStrategoString input,
			ParseTable table, String startSymbol)
			throws InterpreterException, IOException, SGLRException {
		SGLR parser = new SGLR(new TreeBuilder(new TermTreeFactory(env.getFactory())), table);
		parser.setUseStructureRecovery(true);
		IStrategoTerm result = (IStrategoTerm) parser.parse(input.stringValue(), null, startSymbol, true, cursorLocation);
		return result;
	}
}
