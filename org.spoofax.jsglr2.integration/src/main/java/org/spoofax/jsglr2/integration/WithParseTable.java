package org.spoofax.jsglr2.integration;

import java.util.Collections;

import org.metaborg.parsetable.IParseTable;

public interface WithParseTable {

    IParseTable getParseTable(ParseTableVariant variant) throws Exception;

    default Iterable<IParseTable> getParseTables(ParseTableVariant variant) throws Exception {
        return Collections.singleton(getParseTable(variant));
    }

}
