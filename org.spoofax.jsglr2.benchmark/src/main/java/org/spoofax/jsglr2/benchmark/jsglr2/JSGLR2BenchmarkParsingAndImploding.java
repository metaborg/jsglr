package org.spoofax.jsglr2.benchmark.jsglr2;

import org.openjdk.jmh.annotations.Param;
import org.spoofax.jsglr2.JSGLR2Variants.ParseTableVariant;
import org.spoofax.jsglr2.JSGLR2Variants.ParserVariant;
import org.spoofax.jsglr2.JSGLR2Variants.Variant;
import org.spoofax.jsglr2.parseforest.ParseForestConstruction;
import org.spoofax.jsglr2.parseforest.ParseForestRepresentation;
import org.spoofax.jsglr2.reducing.Reducing;
import org.spoofax.jsglr2.stack.StackRepresentation;
import org.spoofax.jsglr2.stack.collections.ActiveStacksRepresentation;
import org.spoofax.jsglr2.stack.collections.ForActorStacksRepresentation;
import org.spoofax.jsglr2.states.ActionsForCharacterRepresentation;
import org.spoofax.jsglr2.states.ProductionToGotoRepresentation;
import org.spoofax.jsglr2.testset.TestSet;

public abstract class JSGLR2BenchmarkParsingAndImploding extends JSGLR2Benchmark {

    protected JSGLR2BenchmarkParsingAndImploding(TestSet testSet) {
        super(testSet);
    }

    @Param({ "true" }) public boolean implode;

    @Param({ "Separated", "DisjointSorted" }) ActionsForCharacterRepresentation actionsForCharacterRepresentation =
        ActionsForCharacterRepresentation.DisjointSorted;

    @Param({ "ForLoop", "JavaHashMap" }) ProductionToGotoRepresentation productionToGotoRepresentation;

    @Param({ "ArrayList" }) public ActiveStacksRepresentation activeStacksRepresentation;

    @Param({ "ArrayDeque" }) public ForActorStacksRepresentation forActorStacksRepresentation;

    @Param({ "Basic", "Hybrid" }) public ParseForestRepresentation parseForestRepresentation;

    @Param({ "Full", "Optimized" }) public ParseForestConstruction parseForestConstruction;

    @Param({ "Basic", "Hybrid", "HybridElkhound" }) public StackRepresentation stackRepresentation;

    @Param({ "Basic", "Elkhound" }) public Reducing reducing;

    @Override
    protected Variant variant() {
        return new Variant(new ParseTableVariant(actionsForCharacterRepresentation, productionToGotoRepresentation),
            new ParserVariant(activeStacksRepresentation, forActorStacksRepresentation, parseForestRepresentation,
                parseForestConstruction, stackRepresentation, reducing));
    }

    @Override
    protected boolean implode() {
        return implode;
    }

}
