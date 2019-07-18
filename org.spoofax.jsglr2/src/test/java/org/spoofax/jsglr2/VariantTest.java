package org.spoofax.jsglr2;

import org.junit.Test;
import org.metaborg.parsetable.IParseTable;

public class VariantTest {

    /**
     * This test will throw an IllegalStateException in JSGLR2Variants.getParser if the preset variants are invalid.
     * 
     * @see JSGLR2Variants#getParser(IParseTable, JSGLR2Variants.ParserVariant)
     */
    @Test public void testPresetVariants() {
        JSGLR2.standard((IParseTable) null);
        JSGLR2.dataDependent(null);
        JSGLR2.layoutSensitive(null);
        JSGLR2.incremental(null);
    }

}
