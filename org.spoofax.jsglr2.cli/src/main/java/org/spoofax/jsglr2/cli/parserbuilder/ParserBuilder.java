package org.spoofax.jsglr2.cli.parserbuilder;

import org.metaborg.parsetable.query.ActionsForCharacterRepresentation;
import org.metaborg.parsetable.query.ProductionToGotoRepresentation;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr2.JSGLR2;
import org.spoofax.jsglr2.JSGLR2Variant;
import org.spoofax.jsglr2.cli.WrappedException;
import org.spoofax.jsglr2.imploder.ImploderVariant;
import org.spoofax.jsglr2.integration.ParseTableVariant;
import org.spoofax.jsglr2.parseforest.ParseForestConstruction;
import org.spoofax.jsglr2.parseforest.ParseForestRepresentation;
import org.spoofax.jsglr2.parser.ParserVariant;
import org.spoofax.jsglr2.reducing.Reducing;
import org.spoofax.jsglr2.stack.StackRepresentation;
import org.spoofax.jsglr2.stack.collections.ActiveStacksRepresentation;
import org.spoofax.jsglr2.stack.collections.ForActorStacksRepresentation;
import org.spoofax.jsglr2.tokens.TokenizerVariant;

import picocli.CommandLine.ArgGroup;
import picocli.CommandLine.Mixin;
import picocli.CommandLine.Option;

public class ParserBuilder {
    @Mixin private ParseTableOptions parseTableOptions = new ParseTableOptions();

    @SuppressWarnings("DefaultAnnotationParam") @ArgGroup(exclusive = true,
        heading = "Parser preset%n") private PresetOrVariantOptions presetOrVariantOptions =
            new PresetOrVariantOptions();

    public static class PresetOrVariantOptions {
        @Option(names = { "-p", "--preset" },
            description = "Parser variant preset: ${COMPLETION-CANDIDATES}") JSGLR2Variant.Preset preset = null;

        @ArgGroup(exclusive = false, heading = "Parser variant%n") ParserVariantOptions parserVariant =
            new ParserVariantOptions();

        JSGLR2Variant getVariant() throws WrappedException {
            return preset != null ? preset.variant : parserVariant.getVariant();
        }
    }

    public static class ParserVariantOptions {
        @Option(names = { "--activeStacks" },
            description = "Active stacks implementation: ${COMPLETION-CANDIDATES}") private ActiveStacksRepresentation activeStacksRepresentation =
                ActiveStacksRepresentation.standard();

        @Option(names = { "--forActorStacks" },
            description = "For actor stacks implementation: ${COMPLETION-CANDIDATES}") private ForActorStacksRepresentation forActorStacksRepresentation =
                ForActorStacksRepresentation.standard();

        @Option(names = { "--parseForest" },
            description = "Parse forest representation: ${COMPLETION-CANDIDATES}") private ParseForestRepresentation parseForestRepresentation =
                ParseForestRepresentation.standard();

        @Option(names = { "--parseForestConstruction" },
            description = "Parse forest construction method: ${COMPLETION-CANDIDATES}") private ParseForestConstruction parseForestConstruction =
                ParseForestConstruction.standard();

        @Option(names = { "--stack" },
            description = "Stack representation: ${COMPLETION-CANDIDATES}") private StackRepresentation stackRepresentation =
                StackRepresentation.standard();

        @Option(names = { "--reducing" },
            description = "Reducing implementation: ${COMPLETION-CANDIDATES}") private Reducing reducing =
                Reducing.standard();

        @Option(names = { "--recovery" }, description = "Recovery: ${COMPLETION-CANDIDATES}") private boolean recovery =
            false;

        @Option(names = { "--imploder" },
            description = "Imploder variant: ${COMPLETION-CANDIDATES}") private ImploderVariant imploderVariant =
                ImploderVariant.standard();

        @Option(names = { "--tokenizer" },
            description = "Tokenizer variant: ${COMPLETION-CANDIDATES}") private TokenizerVariant tokenizerVariant =
                TokenizerVariant.standard();

        JSGLR2Variant getVariant() throws WrappedException {
            ParserVariant parserVariant = new ParserVariant(activeStacksRepresentation, forActorStacksRepresentation,
                parseForestRepresentation, parseForestConstruction, stackRepresentation, reducing, recovery);

            JSGLR2Variant variant = new JSGLR2Variant(parserVariant, imploderVariant, tokenizerVariant);

            if(variant.isValid())
                return variant;
            else
                throw new WrappedException("Invalid parser variant");
        }
    }

    static class ParseTableVariantOptions {
        @Option(names = { "--actionsForCharacters" },
            description = "Actions for character representation: ${COMPLETION-CANDIDATES}") private ActionsForCharacterRepresentation actionsForCharacterRepresentation =
                ActionsForCharacterRepresentation.standard();

        @Option(names = { "--productionToGoto" },
            description = "Production to goto representation: ${COMPLETION-CANDIDATES}") private ProductionToGotoRepresentation productionToGotoRepresentation =
                ProductionToGotoRepresentation.standard();

        ParseTableVariant getVariant() {
            return new ParseTableVariant(actionsForCharacterRepresentation, productionToGotoRepresentation);
        }
    }

    public JSGLR2<IStrategoTerm> getJSGLR2() throws WrappedException {
        return presetOrVariantOptions.getVariant().getJSGLR2(parseTableOptions.getParseTable());
    }

}
