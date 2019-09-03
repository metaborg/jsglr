package org.spoofax.jsglr2;

import org.junit.Test;
import org.metaborg.parsetable.IParseTable;
import org.spoofax.jsglr2.parser.ParserVariant;

public class VariantTest {

    /**
     * This test will throw an IllegalStateException in JSGLR2Variants.getParser if the preset variants are invalid.
     * 
     * @see JSGLR2Variants.Variant#getJSGLR2(IParseTable)
     * @see ParserVariant#getParser(IParseTable)
     */
    @Test public void testPresetVariants() {
        for(JSGLR2Variants enumValue : JSGLR2Variants.values()) {
            enumValue.variant.getJSGLR2(null);
        }
    }

}
