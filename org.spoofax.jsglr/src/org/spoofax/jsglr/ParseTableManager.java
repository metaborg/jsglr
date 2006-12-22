/*
 * Created on 26. nov.. 2006
 *
 * Copyright (c) 2005, Karl Trygve Kalleberg <karltk@ii.uib.no>
 * 
 * Licensed under the GNU General Public License, v2
 */
package org.spoofax.jsglr;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import aterm.ATerm;
import aterm.ATermFactory;
import aterm.pure.PureFactory;

public class ParseTableManager {
    
    private Map<String, ParseTable> knownTables;
    private ATermFactory factory;
    
    public ParseTableManager() {
        factory = new PureFactory();
        knownTables = new HashMap<String, ParseTable>();
    }
    
    public ParseTableManager(ATermFactory factory) {
        this.factory = factory;
        knownTables = new HashMap<String, ParseTable>();
    }
    
    public ParseTable loadFromFile(String filename) throws FileNotFoundException, IOException, InvalidParseTableException {
        if(knownTables.containsKey(filename))
            return knownTables.get(filename);
        
        ParseTable pt = loadFromStream(new FileInputStream(filename));
        
        knownTables.put(filename, pt);
        return pt;
    }
    
    public ParseTable loadFromStream(InputStream r) throws IOException, InvalidParseTableException {
        if(SGLR.isDebugging()) {
            Tools.debug("loadFromStream()");
        }
        long start = System.currentTimeMillis();
        ATerm pt = factory.readFromFile(r);

        ParseTable parseTable = new ParseTable(pt);
        long elapsed = System.currentTimeMillis() - start;

        if (SGLR.isLogging()) {
            Tools.logger("Loading parse table took " + elapsed/1000.0f + "s");
            Tools.logger("No. of states: ", parseTable.getStateCount());
            Tools.logger("No. of productions: ", parseTable.getProductionCount());
            Tools.logger("No. of action entries: ", parseTable.getActionCount());
            Tools.logger("No. of gotos entries: ", parseTable.getGotoCount());

            Tools.logger((parseTable.hasRejects() ? "Includes" : "Excludes"), " rejects");
            Tools.logger((parseTable.hasPriorities() ? "Includes" : "Excludes"), " priorities");
            Tools.logger((parseTable.hasPrefers() ? "Includes" : "Excludes"), " prefer actions");
            Tools.logger((parseTable.hasAvoids() ? "Includes" : "Excludes"), " avoid actions");
        }
        
        return parseTable;
    }

    public ATermFactory getFactory() {
        return factory;
    }

}
