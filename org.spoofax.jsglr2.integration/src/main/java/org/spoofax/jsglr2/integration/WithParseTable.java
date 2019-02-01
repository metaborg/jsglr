package org.spoofax.jsglr2.integration;

import org.metaborg.parsetable.IParseTable;
import org.spoofax.jsglr2.JSGLR2Variants;

public interface WithParseTable {

    IParseTable getParseTable(JSGLR2Variants.ParseTableVariant variant) throws Exception;

}
