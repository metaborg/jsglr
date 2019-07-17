package org.spoofax.jsglr2.integration;

import java.util.Collections;

import org.metaborg.parsetable.IParseTable;

public interface WithParseTable {

    ParseTableWithOrigin getParseTable(ParseTableVariant variant) throws Exception;

    default Iterable<ParseTableWithOrigin> getParseTables(ParseTableVariant variant) throws Exception {
        return Collections.singleton(getParseTable(variant));
    }

    enum ParseTableOrigin {
        Sdf3Generation, ATerm
    }

    class ParseTableWithOrigin {
        public IParseTable parseTable;
        public ParseTableOrigin origin;

        public ParseTableWithOrigin(IParseTable parseTable, ParseTableOrigin origin) {
            this.parseTable = parseTable;
            this.origin = origin;
        }
    }

}
