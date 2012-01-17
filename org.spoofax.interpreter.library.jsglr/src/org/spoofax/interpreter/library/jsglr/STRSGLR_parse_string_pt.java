/*
 * Copyright (c) 2005-2011, Karl Trygve Kalleberg <karltk near strategoxt dot org>
 *
 * Licensed under the GNU Lesser General Public License, v2.1
 */
package org.spoofax.interpreter.library.jsglr;

import java.util.concurrent.atomic.AtomicBoolean;

import org.spoofax.interpreter.core.IContext;
import org.spoofax.interpreter.core.InterpreterException;
import org.spoofax.interpreter.core.Tools;
import org.spoofax.interpreter.stratego.Strategy;
import org.spoofax.interpreter.terms.IStrategoAppl;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr.client.Asfix2TreeBuilder;
import org.spoofax.jsglr.client.Disambiguator;
import org.spoofax.jsglr.client.FilterException;
import org.spoofax.jsglr.client.ITreeBuilder;
import org.spoofax.jsglr.client.ParseTable;
import org.spoofax.jsglr.client.SGLR;
import org.spoofax.jsglr.shared.SGLRException;

public class STRSGLR_parse_string_pt extends JSGLRPrimitive {

	public final static String NAME = "STRSGLR_parse_string_pt";

	private final static String INVALID = "";

	private SGLRException lastException;

	private String lastPath;

	private final Disambiguator filterSettings;

	private final AtomicBoolean recoveryEnabled;

	protected STRSGLR_parse_string_pt(String name, Disambiguator filterSettings, AtomicBoolean recoveryEnabled) {
		super(name, 1, 4);
		this.filterSettings = filterSettings;
		this.recoveryEnabled = recoveryEnabled;
	}

	protected STRSGLR_parse_string_pt(Disambiguator filterSettings, AtomicBoolean recoveryEnabled) {
		this(NAME, filterSettings, recoveryEnabled);
	}

	public final String getLastPath() {
		return lastPath;
	}

	public final SGLRException getLastException() {
		return lastException;
	}

	public final void clearLastException() {
		lastException = null;
	}

	public final boolean isRecoveryEnabled() {
		return recoveryEnabled.get();
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

		String startSymbol = computeStartSymbol(tvars[2]);
		if(startSymbol == INVALID)
			return false;

		ParseTable table = getLibrary(env).getParseTable(Tools.asJavaInt(tvars[1]));
		if (table == null)
			return false;

		return doParse(env,
				Tools.asJavaString(tvars[0]),
				table,
				startSymbol,
				Tools.asJavaString(tvars[3]),
				svars[0]);
	}

	protected static String computeStartSymbol(IStrategoTerm symbolTerm) {
		if (Tools.isTermString(symbolTerm)) {
			return Tools.asJavaString(symbolTerm);
		} else if (symbolTerm.getSubtermCount() == 0
				&& symbolTerm.getTermType() == IStrategoTerm.APPL
				&& ((IStrategoAppl) symbolTerm).getConstructor().getName().equals("None")) {
			return null;
		} else {
			return INVALID;
		}

	}

	protected boolean doParse(IContext env, String text, ParseTable table, String startSymbol, String filePath, Strategy errorCallback) throws InterpreterException {
		lastPath = filePath;

		try {
			SGLR parser = new SGLR(createTreeBuilder(env), table);
			parser.setDisambiguator(filterSettings);
			parser.setUseStructureRecovery(isRecoveryEnabled());
			IStrategoTerm result = (IStrategoTerm) parser.parse(text, null, startSymbol);
			env.setCurrent(result);
			return result != null;
		} catch (SGLRException e) {
			lastException = e;
			IStrategoTerm errorTerm = e.toTerm(getLastPath());
			if (e instanceof FilterException) {
				// HACK: print stack trace for this internal error
				e.printStackTrace();
			}
			env.setCurrent(errorTerm);

			// FIXME: Stratego doesn't seem to print the erroneous line in Java
			return errorCallback.evaluate(env);
		}
	}

	protected ITreeBuilder createTreeBuilder(IContext env) {
		return new Asfix2TreeBuilder(env.getFactory());
	}
}
