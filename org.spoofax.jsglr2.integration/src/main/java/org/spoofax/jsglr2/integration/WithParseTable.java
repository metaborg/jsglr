package org.spoofax.jsglr2.integration;

import org.metaborg.parsetable.IParseTable;

public interface WithParseTable {

    IParseTable getParseTable(ParseTableVariant variant) throws Exception;

}
