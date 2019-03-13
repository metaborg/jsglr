package org.spoofax.jsglr2.benchmark.jsglr2;

import org.metaborg.sdf2table.parsetable.query.ActionsForCharacterRepresentation;
import org.metaborg.sdf2table.parsetable.query.ProductionToGotoRepresentation;
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
import org.spoofax.jsglr2.testset.TestSet;

public abstract class JSGLR2BenchmarkParsing extends JSGLR2Benchmark {

    protected JSGLR2BenchmarkParsing(TestSet testSet) {
        super(testSet);
    }

    @Param({ "false" }) public boolean implode;

    @Param({ "DisjointSorted" }) ActionsForCharacterRepresentation actionsForCharacterRepresentation =
        ActionsForCharacterRepresentation.DisjointSorted;

    @Param({ "JavaHashMap" }) ProductionToGotoRepresentation productionToGotoRepresentation;

    @Param({ "ArrayList", "ArrayListHashMap",
        "LinkedHashMap" }) public ActiveStacksRepresentation activeStacksRepresentation;

    @Param({ "ArrayDeque", "LinkedHashMap" }) public ForActorStacksRepresentation forActorStacksRepresentation;

    @Param({ "Basic", "Hybrid" }) public ParseForestRepresentation parseForestRepresentation;

    @Param({ "Full", "Optimized" }) public ParseForestConstruction parseForestConstruction;

    @Param({ "Basic", "Hybrid", "BasicElkhound", "HybridElkhound" }) public StackRepresentation stackRepresentation;

    @Param({ "Basic", "Elkhound" }) public Reducing reducing;

    @Override protected Variant variant() {
        return new Variant(new ParseTableVariant(actionsForCharacterRepresentation, productionToGotoRepresentation),
            new ParserVariant(activeStacksRepresentation, forActorStacksRepresentation, parseForestRepresentation,
                parseForestConstruction, stackRepresentation, reducing));
    }

    @Override protected boolean implode() {
        return implode;
    }

}
