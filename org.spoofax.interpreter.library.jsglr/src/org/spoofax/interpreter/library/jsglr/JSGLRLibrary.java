package org.spoofax.interpreter.library.jsglr;

import java.util.HashMap;
import java.util.Map;

import org.spoofax.interpreter.adapter.aterm.ATermConverter;
import org.spoofax.interpreter.adapter.aterm.WrappedATermFactory;
import org.spoofax.interpreter.library.AbstractStrategoOperatorRegistry;
import org.spoofax.interpreter.terms.ITermFactory;
import org.spoofax.jsglr.client.ParseTable;
import org.spoofax.jsglr.client.ParseTableManager;

import aterm.ATermFactory;

public class JSGLRLibrary extends AbstractStrategoOperatorRegistry {
    
	public static final String REGISTRY_NAME = "JSGLR";
	
	private ParseTableManager parseTableManager;
	
	private ATermFactory atermFactory;
	
	private int parseTableCounter;
	
	private Map<Integer, ParseTable> parseTables;
	
	private ATermConverter atermConverter;
	
	private ITermFactory lastFactory;

	@Deprecated
	public JSGLRLibrary(WrappedATermFactory termFactory) {
		this(termFactory.getFactory());
    }
	
	public JSGLRLibrary(ATermFactory atermFactory) {
		this.atermFactory = atermFactory;
		init();
        add(new JSGLR_parse_stratego(atermFactory));
        add(new JSGLR_open_parsetable());
        add(new JSGLR_parse_string_pt(atermFactory));
	}

	public String getOperatorRegistryName() {
		return REGISTRY_NAME;
	}
	private void init() {
		parseTableCounter = 0;
		parseTables = new HashMap<Integer, ParseTable>();
		
	}

	public ParseTableManager getParseTableManager() {
		if(parseTableManager == null)
			parseTableManager = new ParseTableManager(atermFactory);
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
	
	public ATermConverter getATermConverter(ITermFactory factory) {
		if (lastFactory != factory)
			atermConverter = new ATermConverter(atermFactory, factory, true);
		return atermConverter;
	}
}
