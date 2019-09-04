package org.spoofax.jsglr2;

import org.junit.Test;
import org.metaborg.parsetable.IParseTable;
import org.spoofax.jsglr2.parser.ParserVariant;

public class JSGLR2VariantTest {

    /**
     * This test will throw an IllegalStateException in JSGLR2Variants.getParser if the preset variants are invalid.
     * 
     * @see JSGLR2Variant#getJSGLR2(IParseTable)
     * @see ParserVariant#getParser(IParseTable)
     */
    @Test public void testPresetVariants() {
        for(JSGLR2Variant.Preset enumValue : JSGLR2Variant.Preset.values()) {
            enumValue.variant.getJSGLR2(null);
        }
    }

}
