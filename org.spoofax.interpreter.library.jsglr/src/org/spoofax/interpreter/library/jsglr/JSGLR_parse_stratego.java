package org.spoofax.interpreter.library.jsglr;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.spoofax.interpreter.IConstruct;
import org.spoofax.interpreter.IContext;
import org.spoofax.interpreter.InterpreterException;
import org.spoofax.interpreter.Tools;
import org.spoofax.interpreter.adapter.aterm.WrappedATermFactory;
import org.spoofax.interpreter.library.AbstractPrimitive;
import org.spoofax.interpreter.terms.IStrategoString;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr.InvalidParseTableException;
import org.spoofax.jsglr.ParseTable;
import org.spoofax.jsglr.ParseTableManager;
import org.spoofax.jsglr.SGLR;
import org.spoofax.jsglr.SGLRException;

import aterm.ATerm;
import aterm.pure.PureFactory;

public class JSGLR_parse_stratego extends AbstractPrimitive {
	private SGLR StrategoSGLR;
	private WrappedATermFactory factory;
	
	JSGLR_parse_stratego(WrappedATermFactory factory) {
		super("JSGLR_parse_stratego", 0, 1);
		this.factory = factory;
	}
	
	@Override
	public boolean call(IContext env, IConstruct[] svars,
			IStrategoTerm[] tvars) throws InterpreterException {
		
		if (!Tools.isTermString(tvars[0]))
			return false;
		String path = ((IStrategoString)tvars[0]).getValue();
		ATerm parsed = null;
		if(StrategoSGLR == null)
			initialize();
		if(StrategoSGLR == null)
			return false;
		try {
			parsed = StrategoSGLR.parse(new FileInputStream(path));
			System.err.println("Yes!");
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
		System.err.println(parsed);
		if (parsed == null)
			return false;
        // FIXME this is dangerous!
        WrappedATermFactory fac = (WrappedATermFactory) env.getFactory();
		env.setCurrent(fac.wrapTerm(parsed));
		return true;
	}

	private void initialize() {
		// FIXME this must be cleaned
		PureFactory pf = ((WrappedATermFactory) factory).getFactory();
		ParseTableManager ptm = new ParseTableManager(pf);

		ParseTable pt;
		try {
				pt = ptm.loadFromFile("data/Stratego.tbl");
				StrategoSGLR = new SGLR(pf, pt);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidParseTableException e) {
			e.printStackTrace();
		}
	}

}
