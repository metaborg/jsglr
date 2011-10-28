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
import org.spoofax.jsglr.client.InvalidParseTableException;
import org.spoofax.jsglr.client.ParseTable;
import org.spoofax.jsglr.client.SGLR;
import org.spoofax.jsglr.shared.SGLRException;
import org.spoofax.terms.ParseError;
import org.spoofax.terms.TermFactory;
import org.spoofax.terms.io.binary.TermReader;

public class JSGLR_recover_parse_string extends JSGLR_parse_string_pt {
	
	private int cursorLocation;

	protected JSGLR_recover_parse_string() {
		super("JSGLR_recover_parse_string", 1, 5);
	}

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
		return super.call(env, svars, tvars);
	}

	@Override
	protected ParseTable getParseTable(IContext env, IStrategoTerm[] tvars) {
		String parseTableFile = asJavaString(tvars[1]);
		final TermFactory factory = new TermFactory();
		IStrategoTerm tableTerm;
		try {
			tableTerm = new TermReader(factory).parseFromFile(parseTableFile);
			return new ParseTable(tableTerm, factory);
		} catch (ParseError e) {
			IOAgent io = SSLLibrary.instance(env).getIOAgent();
			io.printError("JSGLR_recover_parse_string: could not parse " + parseTableFile + " - " + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			IOAgent io = SSLLibrary.instance(env).getIOAgent();
			io.printError("JSGLR_recover_parse_string: could not open " + parseTableFile + " - " + e.getMessage());
			e.printStackTrace();
		} catch (InvalidParseTableException e) {
			IOAgent io = SSLLibrary.instance(env).getIOAgent();
			io.printError("JSGLR_recover_parse_string: " + parseTableFile + " - " + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected IStrategoTerm call(IContext env, IStrategoString input,
			ParseTable table, String startSymbol)
			throws InterpreterException, IOException, SGLRException {
		
		SGLR parser = new SGLR(new Asfix2TreeBuilder(env.getFactory()), table);
		parser.setUseStructureRecovery(true);
		IStrategoTerm result = (IStrategoTerm) parser.parse(input.stringValue(), null, startSymbol, true, cursorLocation);
		return result;
	}
}
