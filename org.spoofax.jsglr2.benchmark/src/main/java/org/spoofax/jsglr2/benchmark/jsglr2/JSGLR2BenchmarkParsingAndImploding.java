package org.spoofax.jsglr2.benchmark.jsglr2;

import org.metaborg.parsetable.query.ActionsForCharacterRepresentation;
import org.metaborg.parsetable.query.ProductionToGotoRepresentation;
import org.openjdk.jmh.annotations.Param;
import org.spoofax.jsglr2.benchmark.BenchmarkStringInputTestSetReader;
import org.spoofax.jsglr2.integration.IntegrationVariant;
import org.spoofax.jsglr2.integration.ParseTableVariant;
import org.spoofax.jsglr2.parseforest.ParseForestConstruction;
import org.spoofax.jsglr2.parseforest.ParseForestRepresentation;
import org.spoofax.jsglr2.parser.ParseException;
import org.spoofax.jsglr2.parser.ParserVariant;
import org.spoofax.jsglr2.reducing.Reducing;
import org.spoofax.jsglr2.stack.StackRepresentation;
import org.spoofax.jsglr2.stack.collections.ActiveStacksRepresentation;
import org.spoofax.jsglr2.stack.collections.ForActorStacksRepresentation;
import org.spoofax.jsglr2.testset.StringInput;
import org.spoofax.jsglr2.testset.TestSet;

public abstract class JSGLR2BenchmarkParsingAndImploding extends JSGLR2Benchmark<StringInput> {

    protected JSGLR2BenchmarkParsingAndImploding(TestSet testSet) {
        super(new BenchmarkStringInputTestSetReader(testSet));
    }

    @Param({ "true" }) public boolean implode;

    @Param({ "Separated", "DisjointSorted" }) ActionsForCharacterRepresentation actionsForCharacterRepresentation;

    @Param({ "ForLoop", "JavaHashMap" }) ProductionToGotoRepresentation productionToGotoRepresentation;

    @Param({ "ArrayList" }) public ActiveStacksRepresentation activeStacksRepresentation;

    @Param({ "ArrayDeque" }) public ForActorStacksRepresentation forActorStacksRepresentation;

    @Param({ "Basic", "Hybrid" }) public ParseForestRepresentation parseForestRepresentation;

    @Param({ "Full", "Optimized" }) public ParseForestConstruction parseForestConstruction;

    @Param({ "Basic", "Hybrid", "HybridElkhound" }) public StackRepresentation stackRepresentation;

    @Param({ "Basic", "Elkhound" }) public Reducing reducing;

    @Override protected IntegrationVariant variant() {
        if(!implode)
            throw new IllegalStateException("this variant is not used for benchmarking");

        return new IntegrationVariant(
            new ParseTableVariant(actionsForCharacterRepresentation, productionToGotoRepresentation),
            new ParserVariant(activeStacksRepresentation, forActorStacksRepresentation, parseForestRepresentation,
                parseForestConstruction, stackRepresentation, reducing, false),
            imploderVariant, tokenizerVariant);
    }

    @Override protected boolean implode() {
        return implode;
    }

    @Override protected Object action(StringInput input) throws ParseException {
        return jsglr2.parseUnsafe(input.content, input.filename, null);
    }

}
