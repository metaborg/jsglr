package org.spoofax.interpreter.library.jsglr;

import java.util.HashMap;
import java.util.Map;

import org.spoofax.interpreter.library.AbstractStrategoOperatorRegistry;
import org.spoofax.interpreter.terms.ITermFactory;
import org.spoofax.jsglr.client.ParseTable;
import org.spoofax.jsglr.io.ParseTableManager;

public class JSGLRLibrary extends AbstractStrategoOperatorRegistry {
    
	public static final String REGISTRY_NAME = "JSGLR";
	
	private ParseTableManager parseTableManager;
	
	private int parseTableCounter;
	
	private Map<Integer, ParseTable> parseTables;
	
	public JSGLRLibrary() {
		init();
        add(new JSGLR_parse_stratego());
        add(new JSGLR_open_parsetable());
        add(new JSGLR_parse_string_pt());
        add(new JSGLR_recover_parse_string());
	}

	public String getOperatorRegistryName() {
		return REGISTRY_NAME;
	}
	private void init() {
		parseTableCounter = 0;
		parseTables = new HashMap<Integer, ParseTable>();
		
	}

	public ParseTableManager getParseTableManager(ITermFactory factory) {
		if(parseTableManager == null)
			parseTableManager = new ParseTableManager(factory);
		return parseTableManager;
	}

	public int addParseTable(ParseTable pt) {
		final int ret = parseTableCounter;
		parseTables.put(ret, pt);
		parseTableCounter++;
		return ret;
	}

	public ParseTable getParseTable(int idx) {
		return parseTables.get(idx);
	}
}
