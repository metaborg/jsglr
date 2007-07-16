package org.spoofax.interpreter.library.jsglr;

import java.util.HashMap;
import java.util.Map;

import org.spoofax.interpreter.library.AbstractStrategoOperatorRegistry;
import org.spoofax.interpreter.terms.ITermFactory;
import org.spoofax.jsglr.ParseTable;
import org.spoofax.jsglr.ParseTableManager;

import aterm.pure.PureFactory;

import org.spoofax.interpreter.adapter.aterm.WrappedATermFactory;
//import aterm.pure.StrATermFactory;

public class JSGLRLibrary extends AbstractStrategoOperatorRegistry {
    
	public static final String REGISTRY_NAME = "JSGLR";
	private ParseTableManager parseTableManager;
	private ITermFactory factory;
	private int parseTableCounter;
	private Map<Integer, ParseTable> parseTables;

	public JSGLRLibrary(ITermFactory factory2) {
		this.factory = factory2;
		init();
        add(new JSGLR_parse_stratego(getATermFactory()));
        add(new JSGLR_open_parsetable());
        add(new JSGLR_parse_string_pt());
    }

	private void init() {
		parseTableCounter = 0;
		parseTables = new HashMap<Integer, ParseTable>();
		
	}

	public ParseTableManager getParseTableManager() {
		if(parseTableManager == null)
			parseTableManager = new ParseTableManager(getATermFactory());
		return parseTableManager;
	}

	int addParseTable(ParseTable pt) {
		final int ret = parseTableCounter;
		parseTables.put(ret, pt);
		parseTableCounter++;
		return ret;
	}

	ParseTable getParseTable(int idx) {
		return parseTables.get(idx);
	}

	ITermFactory getFactory() {
		return factory;
	}
	
	PureFactory getATermFactory() {
		//in case of using the branch of ATerm implementing the IStrategoTerm
		//interface, uncomment the following line
		//return ((StrATermFactory)factory).getFactory();
		return ((WrappedATermFactory)factory).getFactory();
	}
}
