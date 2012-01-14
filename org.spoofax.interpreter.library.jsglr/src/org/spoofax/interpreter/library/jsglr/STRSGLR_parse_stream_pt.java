/*
 * Copyright (c) 2005-2011, Karl Trygve Kalleberg <karltk near strategoxt dot org>
 *
 * Licensed under the GNU Lesser General Public License, v2.1
 */
package org.spoofax.interpreter.library.jsglr;

import java.io.BufferedReader;
import java.io.IOException;

import org.spoofax.interpreter.core.IContext;
import org.spoofax.interpreter.core.InterpreterException;
import org.spoofax.interpreter.core.Tools;
import org.spoofax.interpreter.library.IOAgent;
import org.spoofax.interpreter.library.ssl.SSLLibrary;
import org.spoofax.interpreter.stratego.Strategy;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr.client.Asfix2TreeBuilder;
import org.spoofax.jsglr.client.ITreeBuilder;

public class STRSGLR_parse_stream_pt extends JSGLRPrimitive {

	protected STRSGLR_parse_stream_pt() {
		super("STRSGLR_parse_stream_pt", 1, 4);
	}

	/**
	 * svars: 0 => on parse error
	 * tvars: 0 => stream fd, 1 => table, 2 => startsymbol, 3 => path
	 */
	@Override
	public boolean call(IContext env, Strategy[] svars, IStrategoTerm[] tvars)
			throws InterpreterException {

		if (!Tools.isTermInt(tvars[0]))
			return false;
		if (!Tools.isTermInt(tvars[1]))
			return false;
		if(!Tools.isTermString(tvars[3]))
			return false;

		String startSymbol = STRSGLR_parse_string_pt.computeStartSymbol(tvars[2]);

		STRSGLR_parse_string_pt parser = (STRSGLR_parse_string_pt) getLibrary(env).get(STRSGLR_parse_string_pt.NAME);

		String text;
		try {
			text = readFile(SSLLibrary.instance(env).getIOAgent(), Tools.asJavaInt(tvars[0]));
		} catch (IOException e) {
			// FIXME add more structure;
			IStrategoTerm errorTerm = env.getFactory().makeString(e.getMessage());
			env.setCurrent(errorTerm);
			return svars[0].evaluate(env);
		}

		return parser.doParse(
				env,
				text,
				Tools.asJavaInt(tvars[1]),
				startSymbol,
				Tools.asJavaString(tvars[3]),
				svars[0]);
	}

	protected ITreeBuilder createTreeBuilder(IContext env) {
		return new Asfix2TreeBuilder(env.getFactory());
	}

	private String readFile(IOAgent io, int fd) throws IOException {
		BufferedReader br = new BufferedReader(io.getReader(fd));
		StringBuilder sb = new StringBuilder();
		do {
			sb.append(br.readLine());
			sb.append('\n');
		} while(br.ready());
		return sb.toString();
	}
}
