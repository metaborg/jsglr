package org.spoofax.jsglr2.elkhound;

import org.metaborg.parsetable.states.IState;
import org.spoofax.jsglr2.parseforest.IDerivation;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parseforest.IParseNode;
import org.spoofax.jsglr2.parser.AbstractParseState;
import org.spoofax.jsglr2.parser.observing.ParserObserving;
import org.spoofax.jsglr2.stack.StackLink;
import org.spoofax.jsglr2.stack.StackManagerFactory;

public class HybridElkhoundStackManager
//@formatter:off
   <ParseForest extends IParseForest,
    Derivation  extends IDerivation<ParseForest>,
    ParseNode   extends IParseNode<ParseForest, Derivation>,
    ParseState  extends AbstractParseState<?, HybridElkhoundStackNode<ParseForest, Derivation, ParseNode>>>
//@formatter:on
    extends
    ElkhoundStackManager<ParseForest, Derivation, ParseNode, HybridElkhoundStackNode<ParseForest, Derivation, ParseNode>, ParseState> {

    public HybridElkhoundStackManager(
        ParserObserving<ParseForest, Derivation, ParseNode, HybridElkhoundStackNode<ParseForest, Derivation, ParseNode>, ParseState> observing) {
        super(observing);
    }

    public static
//@formatter:off
   <ParseForest_  extends IParseForest,
    Derivation_   extends IDerivation<ParseForest_>,
    ParseNode_    extends IParseNode<ParseForest_, Derivation_>,
    ParseState_   extends AbstractParseState<?, HybridElkhoundStackNode<ParseForest_, Derivation_, ParseNode_>>>
//@formatter:on
    StackManagerFactory<ParseForest_, Derivation_, ParseNode_, HybridElkhoundStackNode<ParseForest_, Derivation_, ParseNode_>, ParseState_, HybridElkhoundStackManager<ParseForest_, Derivation_, ParseNode_, ParseState_>>
        factory() {
        return observing -> new HybridElkhoundStackManager<>(observing);
    }

    @Override protected HybridElkhoundStackNode<ParseForest, Derivation, ParseNode> createStackNode(IState state,
        boolean isRoot) {
        return new HybridElkhoundStackNode<>(state, isRoot);
    }

    @Override protected StackLink<ParseForest, HybridElkhoundStackNode<ParseForest, Derivation, ParseNode>>
        addStackLink(ParseState parseState, HybridElkhoundStackNode<ParseForest, Derivation, ParseNode> from,
            HybridElkhoundStackNode<ParseForest, Derivation, ParseNode> to, ParseForest parseNode) {
        return from.addLink(observing, to, parseNode, parseState.activeStacks);
    }

    @Override protected StackLink<ParseForest, HybridElkhoundStackNode<ParseForest, Derivation, ParseNode>>
        getOnlyLink(HybridElkhoundStackNode<ParseForest, Derivation, ParseNode> stack) {
        return stack.getOnlyLink();
    }

    @Override protected Iterable<StackLink<ParseForest, HybridElkhoundStackNode<ParseForest, Derivation, ParseNode>>>
        stackLinksOut(HybridElkhoundStackNode<ParseForest, Derivation, ParseNode> stack) {
        return stack.getLinks();
    }

}
