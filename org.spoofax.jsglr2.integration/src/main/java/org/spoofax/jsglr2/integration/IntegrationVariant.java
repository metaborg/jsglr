package org.spoofax.jsglr2.integration;

import java.util.Arrays;
import java.util.List;

import org.metaborg.parsetable.query.ActionsForCharacterRepresentation;
import org.metaborg.parsetable.query.ProductionToGotoRepresentation;
import org.spoofax.jsglr2.JSGLR2Variants;
import org.spoofax.jsglr2.imploder.ImploderVariant;
import org.spoofax.jsglr2.parseforest.ParseForestConstruction;
import org.spoofax.jsglr2.parseforest.ParseForestRepresentation;
import org.spoofax.jsglr2.reducing.Reducing;
import org.spoofax.jsglr2.stack.StackRepresentation;
import org.spoofax.jsglr2.stack.collections.ActiveStacksRepresentation;
import org.spoofax.jsglr2.stack.collections.ForActorStacksRepresentation;
import org.spoofax.jsglr2.tokens.TokenizerVariant;

public class IntegrationVariant {
    public final ParseTableVariant parseTable;
    public final JSGLR2Variants.Variant jsglr2;
    public final JSGLR2Variants.ParserVariant parser;
    public final ImploderVariant imploder;
    public final TokenizerVariant tokenizer;

    public IntegrationVariant(ParseTableVariant parseTableVariant, JSGLR2Variants.ParserVariant parserVariant,
        ImploderVariant imploderVariant, TokenizerVariant tokenizer) {
        this.parseTable = parseTableVariant;
        this.jsglr2 = new JSGLR2Variants.Variant(parserVariant, imploderVariant, tokenizer);
        this.parser = parserVariant;
        this.imploder = imploderVariant;
        this.tokenizer = tokenizer;
    }

    public IntegrationVariant(ParseTableVariant parseTableVariant, JSGLR2Variants.Variant jsglr2Variant) {
        this.parseTable = parseTableVariant;
        this.jsglr2 = jsglr2Variant;
        this.parser = jsglr2Variant.parser;
        this.imploder = jsglr2Variant.imploder;
        this.tokenizer = jsglr2Variant.tokenizer;
    }

    public String name() {
        return parseTable.name() + "//" + jsglr2.name();
    }

    @Override public boolean equals(Object o) {
        if(this == o) {
            return true;
        }
        if(o == null || getClass() != o.getClass()) {
            return false;
        }

        IntegrationVariant that = (IntegrationVariant) o;

        return parseTable.equals(that.parseTable) && jsglr2.equals(that.jsglr2);
    }

    public static List<IntegrationVariant> testVariants() {
        //@formatter:off
        return Arrays.asList(
            new IntegrationVariant(new ParseTableVariant(ActionsForCharacterRepresentation.Separated,      ProductionToGotoRepresentation.ForLoop),     new JSGLR2Variants.ParserVariant(ActiveStacksRepresentation.ArrayList,     ForActorStacksRepresentation.ArrayDeque,    ParseForestRepresentation.Basic,  ParseForestConstruction.Full, StackRepresentation.Basic,          Reducing.Basic),       ImploderVariant.TokenizedRecursive,   TokenizerVariant.Null),
            new IntegrationVariant(new ParseTableVariant(ActionsForCharacterRepresentation.DisjointSorted, ProductionToGotoRepresentation.JavaHashMap), new JSGLR2Variants.ParserVariant(ActiveStacksRepresentation.ArrayList,     ForActorStacksRepresentation.ArrayDeque,    ParseForestRepresentation.Basic,  ParseForestConstruction.Full, StackRepresentation.Basic,          Reducing.Basic),       ImploderVariant.Recursive,            TokenizerVariant.Recursive),
            new IntegrationVariant(new ParseTableVariant(ActionsForCharacterRepresentation.Separated,      ProductionToGotoRepresentation.ForLoop),     new JSGLR2Variants.ParserVariant(ActiveStacksRepresentation.LinkedHashMap, ForActorStacksRepresentation.LinkedHashMap, ParseForestRepresentation.Hybrid, ParseForestConstruction.Full, StackRepresentation.Hybrid,         Reducing.Basic),       ImploderVariant.Iterative,            TokenizerVariant.Iterative),
            new IntegrationVariant(new ParseTableVariant(ActionsForCharacterRepresentation.Separated,      ProductionToGotoRepresentation.ForLoop),     new JSGLR2Variants.ParserVariant(ActiveStacksRepresentation.LinkedHashMap, ForActorStacksRepresentation.LinkedHashMap, ParseForestRepresentation.Hybrid, ParseForestConstruction.Full, StackRepresentation.HybridElkhound, Reducing.Elkhound),    ImploderVariant.TokenizedRecursive,   TokenizerVariant.Null),/*
            new IntegrationVariant(new ParseTableVariant(ActionsForCharacterRepresentation.Separated,      ProductionToGotoRepresentation.ForLoop),     new ParserVariant(ActiveStacksRepresentation.LinkedHashMap, ForActorStacksRepresentation.LinkedHashMap, ParseForestRepresentation.Hybrid, ParseForestConstruction.Optimized, StackRepresentation.Hybrid,         Reducing.Basic)),
            new IntegrationVariant(new ParseTableVariant(ActionsForCharacterRepresentation.Separated,      ProductionToGotoRepresentation.ForLoop),     new ParserVariant(ActiveStacksRepresentation.LinkedHashMap, ForActorStacksRepresentation.LinkedHashMap, ParseForestRepresentation.Hybrid, ParseForestConstruction.Optimized, StackRepresentation.HybridElkhound, Reducing.Elkhound)),*/
            new IntegrationVariant(new ParseTableVariant(ActionsForCharacterRepresentation.Separated,      ProductionToGotoRepresentation.ForLoop),     new JSGLR2Variants.ParserVariant(ActiveStacksRepresentation.LinkedHashMap, ForActorStacksRepresentation.LinkedHashMap, ParseForestRepresentation.Incremental, ParseForestConstruction.Full, StackRepresentation.Hybrid,    Reducing.Incremental), ImploderVariant.RecursiveIncremental, TokenizerVariant.Recursive),
            // data-dependent parser
            new IntegrationVariant(new ParseTableVariant(ActionsForCharacterRepresentation.Separated,      ProductionToGotoRepresentation.ForLoop),     new JSGLR2Variants.ParserVariant(ActiveStacksRepresentation.ArrayList,     ForActorStacksRepresentation.ArrayDeque,    ParseForestRepresentation.DataDependent,   ParseForestConstruction.Full, StackRepresentation.Basic, Reducing.DataDependent),   ImploderVariant.TokenizedRecursive, TokenizerVariant.Null),
            // layout-sensitive parser
            new IntegrationVariant(new ParseTableVariant(ActionsForCharacterRepresentation.Separated,      ProductionToGotoRepresentation.ForLoop),     new JSGLR2Variants.ParserVariant(ActiveStacksRepresentation.ArrayList,     ForActorStacksRepresentation.ArrayDeque,    ParseForestRepresentation.LayoutSensitive, ParseForestConstruction.Full, StackRepresentation.Basic, Reducing.LayoutSensitive), ImploderVariant.TokenizedRecursive, TokenizerVariant.Null)
        );
        //@formatter:on
    }
}
