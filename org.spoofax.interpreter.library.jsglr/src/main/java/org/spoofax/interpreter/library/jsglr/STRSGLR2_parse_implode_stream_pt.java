package org.spoofax.interpreter.library.jsglr;

import org.metaborg.parsetable.IParseTable;
import org.spoofax.interpreter.core.IContext;
import org.spoofax.interpreter.core.InterpreterException;
import org.spoofax.interpreter.core.Tools;
import org.spoofax.interpreter.library.ssl.SSLLibrary;
import org.spoofax.interpreter.stratego.Strategy;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr2.JSGLR2;
import org.spoofax.jsglr2.parseforest.hybrid.HybridParseForest;
import org.spoofax.jsglr2.parsetable.ParseTableReadException;

import java.io.IOException;

/**
 * @author Jeff Smits <j.smits-1@tudelft.nl>
 */
public class STRSGLR2_parse_implode_stream_pt extends JSGLRPrimitive {
	private final static String NAME = "STRSGLR2_parse_implode_stream_pt";

	STRSGLR2_parse_implode_stream_pt() {
		super(NAME, 0, 4);
	}
	
	/**
	 * tvars: [input, table, startsymbol, path]
	 */
	@Override
	public boolean call(IContext env, Strategy[] svars, IStrategoTerm[] tvars)
			throws InterpreterException {

		if (!Tools.isTermString(tvars[0]))
			return false;
		if (!Tools.isTermInt(tvars[1]))
			return false;
		if(!Tools.isTermString(tvars[3]))
			return false;

		String startSymbol = STRSGLR_parse_string_pt.computeStartSymbol(tvars[2]);
		//noinspection StringEquality
		if(startSymbol == STRSGLR_parse_string_pt.INVALID)
			return false;

		String input;
		try {
			input = STRSGLR_parse_stream_pt.readFile(SSLLibrary.instance(env).getIOAgent(), Tools.asJavaInt(tvars[0]));
		} catch (IOException e) {
			IStrategoTerm errorTerm = env.getFactory().makeString(e.getMessage());
			env.setCurrent(errorTerm);
			return svars[0].evaluate(env);
		}

		IParseTable table = getLibrary(env).getV2ParseTable(Tools.asJavaInt(tvars[1]));
		String lastPath = Tools.asJavaString(tvars[3]);

		IStrategoTerm result = doParse(input, startSymbol, table, lastPath);
		if (result == null) {
			return false;
		}

		env.setCurrent(result);
		return true;
	}

	private static IStrategoTerm doParse(String input, String startSymbol, IParseTable pt, String path) {
		try {
			JSGLR2<HybridParseForest, IStrategoTerm> parser = JSGLR2.standard(pt);
			return parser.parse(input, path, startSymbol);
		} catch (ParseTableReadException e) {
			return null;
		}
	}

}
