package org.spoofax.jsglr2.stack.basic;

import org.metaborg.parsetable.states.IState;
import org.spoofax.jsglr2.parseforest.IDerivation;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parseforest.IParseNode;
import org.spoofax.jsglr2.parser.AbstractParseState;
import org.spoofax.jsglr2.parser.observing.ParserObserving;
import org.spoofax.jsglr2.stack.StackManager;
import org.spoofax.jsglr2.stack.StackManagerFactory;

public class BasicStackManager
//@formatter:off
   <ParseForest extends IParseForest,
    Derivation  extends IDerivation<ParseForest>,
    ParseNode   extends IParseNode<ParseForest, Derivation>,
    ParseState  extends AbstractParseState<BasicStackNode<ParseForest>>>
//@formatter:on
    extends StackManager<ParseForest, Derivation, ParseNode, BasicStackNode<ParseForest>, ParseState> {

    public BasicStackManager(
        ParserObserving<ParseForest, Derivation, ParseNode, BasicStackNode<ParseForest>, ParseState> observing) {
        super(observing);
    }

    public static
//@formatter:off
   <ParseForest_  extends IParseForest,
    Derivation_   extends IDerivation<ParseForest_>,
    ParseNode_    extends IParseNode<ParseForest_, Derivation_>,
    ParseState_   extends AbstractParseState<BasicStackNode<ParseForest_>>>
//@formatter:on
    StackManagerFactory<ParseForest_, Derivation_, ParseNode_, BasicStackNode<ParseForest_>, ParseState_, BasicStackManager<ParseForest_, Derivation_, ParseNode_, ParseState_>>
        factory() {
        return observing -> new BasicStackManager<>(observing);
    }

    @Override protected BasicStackNode<ParseForest> createStackNode(IState state, boolean isRoot) {
        return new BasicStackNode<>(state);
    }

}
