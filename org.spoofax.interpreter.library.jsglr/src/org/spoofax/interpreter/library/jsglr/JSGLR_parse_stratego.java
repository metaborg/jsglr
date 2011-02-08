package org.spoofax.interpreter.library.jsglr;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.spoofax.interpreter.core.IContext;
import org.spoofax.interpreter.core.InterpreterException;
import org.spoofax.interpreter.core.Tools;
import org.spoofax.interpreter.stratego.Strategy;
import org.spoofax.interpreter.terms.IStrategoString;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.interpreter.terms.ITermFactory;
import org.spoofax.jsglr.client.InvalidParseTableException;
import org.spoofax.jsglr.client.ParseTable;
import org.spoofax.jsglr.io.ParseTableManager;
import org.spoofax.jsglr.io.SGLR;
import org.spoofax.jsglr.shared.SGLRException;

public class JSGLR_parse_stratego extends JSGLRPrimitive {
	
	private SGLR strategoSGLR;
	
	JSGLR_parse_stratego() {
		super("JSGLR_parse_stratego", 0, 1);
	}
	
	@Override
	public boolean call(IContext env, Strategy[] svars,
			IStrategoTerm[] tvars) throws InterpreterException {
		
		if (!Tools.isTermString(tvars[0]))
			return false;
		String path = ((IStrategoString)tvars[0]).stringValue();
		IStrategoTerm parsed = null;
		if(strategoSGLR == null)
			initialize(env.getFactory());
		if(strategoSGLR == null)
			return false;
		try {
			parsed = (IStrategoTerm) strategoSGLR.parse(new BufferedInputStream(new FileInputStream(path)), null);
		} catch (FileNotFoundException e) {
			System.err.println("File not found.");
			return false;
		} catch (IOException e) {
			System.err.println("IOEXC");
			return false;
		} catch (SGLRException e) {
			System.err.println("SGLR Exc");
			return false;
		}
		if (parsed == null)
			return false;
		env.setCurrent(parsed);
		return true;
	}

	private void initialize(ITermFactory factory) {
		// FIXME this must be cleaned
		ParseTableManager ptm = new ParseTableManager(factory);

		ParseTable pt;
		try {
			pt = ptm.loadFromFile(System.getProperty("share.dir") + "/Stratego.tbl");
			strategoSGLR = new SGLR(factory, pt);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidParseTableException e) {
			e.printStackTrace();
		}
	}

}
