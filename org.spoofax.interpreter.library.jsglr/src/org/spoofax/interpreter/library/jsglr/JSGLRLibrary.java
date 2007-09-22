package org.spoofax.interpreter.library.jsglr;

import java.util.HashMap;
import java.util.Map;

import org.spoofax.interpreter.adapter.aterm.WrappedATermFactory;
import org.spoofax.interpreter.library.AbstractStrategoOperatorRegistry;
import org.spoofax.interpreter.terms.ITermFactory;
import org.spoofax.jsglr.ParseTable;
import org.spoofax.jsglr.ParseTableManager;

import aterm.ATermFactory;

public class JSGLRLibrary extends AbstractStrategoOperatorRegistry {
    
	public static final String REGISTRY_NAME = "JSGLR";
	private ParseTableManager parseTableManager;
	private WrappedATermFactory factory;
	private int parseTableCounter;
	private Map<Integer, ParseTable> parseTables;

	public JSGLRLibrary(WrappedATermFactory termFactory) {
		this.factory = termFactory;
		init();
        add(new JSGLR_parse_stratego(factory));
        add(new JSGLR_open_parsetable());
        add(new JSGLR_parse_string_pt(factory));
    }

	private void init() {
		parseTableCounter = 0;
		parseTables = new HashMap<Integer, ParseTable>();
		
	}

	public ParseTableManager getParseTableManager() {
		if(parseTableManager == null)
			parseTableManager = new ParseTableManager(factory.getFactory());
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
	
	ATermFactory getATermFactory() {
		return factory.getFactory();
	}
}
