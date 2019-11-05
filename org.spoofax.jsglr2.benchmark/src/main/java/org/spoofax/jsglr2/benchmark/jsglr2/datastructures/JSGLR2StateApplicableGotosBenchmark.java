package org.spoofax.jsglr2.benchmark.jsglr2.datastructures;

import org.metaborg.parsetable.IParseTable;
import org.metaborg.parsetable.ParseTableReadException;
import org.metaborg.parsetable.ParseTableReader;
import org.metaborg.parsetable.actions.IReduce;
import org.metaborg.parsetable.query.ActionsForCharacterRepresentation;
import org.metaborg.parsetable.query.ProductionToGotoRepresentation;
import org.metaborg.parsetable.states.IState;
import org.metaborg.parsetable.states.IStateFactory;
import org.metaborg.parsetable.states.StateFactory;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.infra.Blackhole;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr2.benchmark.BenchmarkParserObserver;
import org.spoofax.jsglr2.parseforest.basic.*;
import org.spoofax.jsglr2.parser.AbstractParseState;
import org.spoofax.jsglr2.stack.basic.BasicStackNode;
import org.spoofax.jsglr2.testset.TestSet;

import java.util.ArrayList;
import java.util.List;

public abstract class JSGLR2StateApplicableGotosBenchmark extends JSGLR2DataStructureBenchmark {

    GotoObserver gotoObserver;

    protected JSGLR2StateApplicableGotosBenchmark(TestSet testSet) {
        super(testSet);
    }

    @Param public ProductionToGotoRepresentation productionToGotoRepresentation;

    @Override public void postParserSetup() {
        gotoObserver = new GotoObserver();

        parser.observing().attachObserver(gotoObserver);
    }

    @Override protected IParseTable readParseTable(IStrategoTerm parseTableTerm) throws ParseTableReadException {
        IStateFactory stateFactory =
            new StateFactory(ActionsForCharacterRepresentation.standard(), productionToGotoRepresentation);

        return new ParseTableReader(stateFactory).read(parseTableTerm);
    }

    class GotoLookup {

        final IState state;
        final int productionId;

        protected GotoLookup(IState state, int productionId) {
            this.state = state;
            this.productionId = productionId;
        }

        public int execute() {
            return state.getGotoId(productionId);
        }

    }

    class GotoObserver extends
        BenchmarkParserObserver<IBasicParseForest, IBasicDerivation<IBasicParseForest>, IBasicParseNode<IBasicParseForest, IBasicDerivation<IBasicParseForest>>, BasicStackNode<IBasicParseForest>, AbstractParseState<BasicStackNode<IBasicParseForest>>> {

        public List<GotoLookup> gotoLookups = new ArrayList<>();

        @Override public void reducer(BasicStackNode<IBasicParseForest> stack, IReduce reduce,
            IBasicParseForest[] parseNodes, BasicStackNode<IBasicParseForest> activeStackWithGotoState) {
            gotoLookups.add(new GotoLookup(stack.state, reduce.production().id()));
        }

    }

    @Benchmark public void benchmark(Blackhole bh) {
        for(GotoLookup gotoLookup : gotoObserver.gotoLookups)
            bh.consume(gotoLookup.execute());
    }

}
