package org.spoofax.jsglr2.benchmark.jsglr2;

import org.metaborg.parsetable.query.ActionsForCharacterRepresentation;
import org.metaborg.parsetable.query.ProductionToGotoRepresentation;
import org.openjdk.jmh.annotations.Param;
import org.spoofax.jsglr2.JSGLR2Variants.ParserVariant;
import org.spoofax.jsglr2.benchmark.BenchmarkStringInputTestSetReader;
import org.spoofax.jsglr2.integration.IntegrationVariant;
import org.spoofax.jsglr2.integration.ParseTableVariant;
import org.spoofax.jsglr2.parseforest.ParseForestConstruction;
import org.spoofax.jsglr2.parseforest.ParseForestRepresentation;
import org.spoofax.jsglr2.parser.ParseException;
import org.spoofax.jsglr2.reducing.Reducing;
import org.spoofax.jsglr2.stack.StackRepresentation;
import org.spoofax.jsglr2.stack.collections.ActiveStacksRepresentation;
import org.spoofax.jsglr2.stack.collections.ForActorStacksRepresentation;
import org.spoofax.jsglr2.testset.StringInput;
import org.spoofax.jsglr2.testset.TestSet;

public abstract class JSGLR2BenchmarkParsing extends JSGLR2Benchmark<StringInput> {

    protected JSGLR2BenchmarkParsing(TestSet testSet) {
        super(new BenchmarkStringInputTestSetReader(testSet));
    }

    @Param({ "false" }) public boolean implode;

    @Param({ "DisjointSorted" }) ActionsForCharacterRepresentation actionsForCharacterRepresentation;

    @Param({ "JavaHashMap" }) ProductionToGotoRepresentation productionToGotoRepresentation;

    @Param({ "ArrayList", "ArrayListHashMap",
        "LinkedHashMap" }) public ActiveStacksRepresentation activeStacksRepresentation;

    @Param({ "ArrayDeque", "LinkedHashMap" }) public ForActorStacksRepresentation forActorStacksRepresentation;

    @Param({ "Basic", "Hybrid" }) public ParseForestRepresentation parseForestRepresentation;

    @Param({ "Full", "Optimized" }) public ParseForestConstruction parseForestConstruction;

    @Param({ "Basic", "Hybrid", "BasicElkhound", "HybridElkhound" }) public StackRepresentation stackRepresentation;

    @Param({ "Basic", "Elkhound" }) public Reducing reducing;

    @Override protected IntegrationVariant variant() {
        return new IntegrationVariant(
            new ParseTableVariant(actionsForCharacterRepresentation, productionToGotoRepresentation),
            new ParserVariant(activeStacksRepresentation, forActorStacksRepresentation, parseForestRepresentation,
                parseForestConstruction, stackRepresentation, reducing),
            imploderVariant, tokenizerVariant);
    }

    @Override protected boolean implode() {
        return implode;
    }

    @Override protected Object action(StringInput input) throws ParseException {
        return jsglr2.parser.parseUnsafe(input.content, input.filename, null);
    }

}
