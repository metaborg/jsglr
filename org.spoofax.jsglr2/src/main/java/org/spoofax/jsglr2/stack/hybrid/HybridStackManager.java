package org.spoofax.jsglr2.stack.hybrid;

import org.metaborg.parsetable.states.IState;
import org.spoofax.jsglr2.parseforest.IDerivation;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parseforest.IParseNode;
import org.spoofax.jsglr2.parser.AbstractParseState;
import org.spoofax.jsglr2.parser.observing.ParserObserving;
import org.spoofax.jsglr2.stack.StackManager;
import org.spoofax.jsglr2.stack.StackManagerFactory;

public class HybridStackManager
//@formatter:off
   <ParseForest extends IParseForest,
    Derivation  extends IDerivation<ParseForest>,
    ParseNode   extends IParseNode<ParseForest, Derivation>,
    ParseState  extends AbstractParseState<ParseForest, HybridStackNode<ParseForest>>>
//@formatter:on
    extends StackManager<ParseForest, Derivation, ParseNode, HybridStackNode<ParseForest>, ParseState> {

    public HybridStackManager(
        ParserObserving<ParseForest, Derivation, ParseNode, HybridStackNode<ParseForest>, ParseState> observing) {
        super(observing);
    }

    public static
//@formatter:off
   <ParseForest_  extends IParseForest,
    Derivation_   extends IDerivation<ParseForest_>,
    ParseNode_    extends IParseNode<ParseForest_, Derivation_>,
    ParseState_   extends AbstractParseState<ParseForest_, HybridStackNode<ParseForest_>>>
//@formatter:on
    StackManagerFactory<ParseForest_, Derivation_, ParseNode_, HybridStackNode<ParseForest_>, ParseState_, HybridStackManager<ParseForest_, Derivation_, ParseNode_, ParseState_>>
        factory() {
        return observing -> new HybridStackManager<>(observing);
    }

    @Override protected HybridStackNode<ParseForest> createStackNode(IState state, boolean isRoot) {
        return new HybridStackNode<>(state);
    }

}
