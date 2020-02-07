package org.spoofax.jsglr2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.metaborg.parsetable.IParseTable;
import org.spoofax.jsglr2.imploder.ImploderVariant;
import org.spoofax.jsglr2.parseforest.ParseForestConstruction;
import org.spoofax.jsglr2.parseforest.ParseForestRepresentation;
import org.spoofax.jsglr2.parser.ParserVariant;
import org.spoofax.jsglr2.reducing.Reducing;
import org.spoofax.jsglr2.stack.StackRepresentation;
import org.spoofax.jsglr2.stack.collections.ActiveStacksRepresentation;
import org.spoofax.jsglr2.stack.collections.ForActorStacksRepresentation;
import org.spoofax.jsglr2.tokens.TokenizerVariant;

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

    @Test public void testVariantMessage() {
        JSGLR2Variant variant =
            new JSGLR2Variant(
                new ParserVariant(ActiveStacksRepresentation.standard(), ForActorStacksRepresentation.standard(),
                    ParseForestRepresentation.Incremental, ParseForestConstruction.standard(),
                    StackRepresentation.Hybrid, Reducing.Elkhound, false),
                ImploderVariant.RecursiveIncremental, TokenizerVariant.Recursive);
        try {
            variant.getJSGLR2(null);
        } catch(IllegalStateException e) {
            assertEquals(
                "Invalid JSGLR2 parser variant: both Reducing and ParseForestRepresentation should use Incremental, both Reducing and StackRepresentation should use Elkhound",
                e.getMessage());
            return;
        }
        fail("Should throw an exception");
    }

}
