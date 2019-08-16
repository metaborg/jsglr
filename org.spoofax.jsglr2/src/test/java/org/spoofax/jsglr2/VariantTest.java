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
        for (JSGLR2Variants enumValue : JSGLR2Variants.values()) {
            JSGLR2Variants.getJSGLR2(null, enumValue.variant);
        }
    }

}
