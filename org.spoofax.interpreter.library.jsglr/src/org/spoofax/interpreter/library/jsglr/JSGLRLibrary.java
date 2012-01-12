/*
 * Copyright (c) 2005-2011, Karl Trygve Kalleberg <karltk near strategoxt dot org>
 *
 * Licensed under the GNU Lesser General Public License, v2.1
 */
package org.spoofax.interpreter.library.jsglr;

import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

import org.spoofax.interpreter.library.AbstractStrategoOperatorRegistry;
import org.spoofax.interpreter.terms.IStrategoInt;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.interpreter.terms.ITermFactory;
import org.spoofax.jsglr.client.ParseTable;
import org.spoofax.jsglr.io.ParseTableManager;

public class JSGLRLibrary extends AbstractStrategoOperatorRegistry {

	public static final String REGISTRY_NAME = "JSGLR";

	private ParseTableManager parseTableManager;

	private int parseTableCounter;

	private Map<Integer, ParseTable> parseTables;

    private final WeakHashMap<IStrategoTerm, IStrategoInt> parseTableCache =
            new WeakHashMap<IStrategoTerm, IStrategoInt>();

	public JSGLRLibrary() {
		init();
        add(new STRSGLR_open_parse_table());
        add(new STRSGLR_parse_string_pt());
        add(new STRSGLR_parse_stream_pt());
        add(new STRSGLR_recover_parse_string());
	}

	public String getOperatorRegistryName() {
		return REGISTRY_NAME;
	}

	private void init() {
		parseTableCounter = 0;
		parseTables = new HashMap<Integer, ParseTable>();

	}

	ParseTableManager getParseTableManager(ITermFactory factory) {
		if(parseTableManager == null)
			parseTableManager = new ParseTableManager(factory);
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

	WeakHashMap<IStrategoTerm, IStrategoInt> getParseTableCache() {
		return parseTableCache;
	}
}
