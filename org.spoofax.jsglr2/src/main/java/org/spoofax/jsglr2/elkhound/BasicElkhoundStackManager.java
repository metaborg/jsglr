package org.spoofax.jsglr2.elkhound;

import org.metaborg.parsetable.states.IState;
import org.spoofax.jsglr2.parseforest.IDerivation;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parseforest.IParseNode;
import org.spoofax.jsglr2.parser.AbstractParseState;
import org.spoofax.jsglr2.parser.observing.ParserObserving;
import org.spoofax.jsglr2.stack.StackLink;
import org.spoofax.jsglr2.stack.StackManagerFactory;

public class BasicElkhoundStackManager
//@formatter:off
   <ParseForest extends IParseForest,
    Derivation  extends IDerivation<ParseForest>,
    ParseNode   extends IParseNode<ParseForest, Derivation>,
    ParseState  extends AbstractParseState<?, BasicElkhoundStackNode<ParseForest, Derivation, ParseNode>>>
//@formatter:on
    extends
    ElkhoundStackManager<ParseForest, Derivation, ParseNode, BasicElkhoundStackNode<ParseForest, Derivation, ParseNode>, ParseState> {

    public BasicElkhoundStackManager(
        ParserObserving<ParseForest, Derivation, ParseNode, BasicElkhoundStackNode<ParseForest, Derivation, ParseNode>, ParseState> observing) {
        super(observing);
    }

    public static
//@formatter:off
   <ParseForest_  extends IParseForest,
    Derivation_   extends IDerivation<ParseForest_>,
    ParseNode_    extends IParseNode<ParseForest_, Derivation_>,
    ParseState_   extends AbstractParseState<?, BasicElkhoundStackNode<ParseForest_, Derivation_, ParseNode_>>>
//@formatter:on
    StackManagerFactory<ParseForest_, Derivation_, ParseNode_, BasicElkhoundStackNode<ParseForest_, Derivation_, ParseNode_>, ParseState_, BasicElkhoundStackManager<ParseForest_, Derivation_, ParseNode_, ParseState_>>
        factory() {
        return observing -> new BasicElkhoundStackManager<>(observing);
    }

    @Override protected BasicElkhoundStackNode<ParseForest, Derivation, ParseNode> createStackNode(IState state,
        boolean isRoot) {
        return new BasicElkhoundStackNode<>(state, isRoot);
    }

    @Override protected StackLink<ParseForest, BasicElkhoundStackNode<ParseForest, Derivation, ParseNode>> addStackLink(
        ParseState parseState, BasicElkhoundStackNode<ParseForest, Derivation, ParseNode> from,
        BasicElkhoundStackNode<ParseForest, Derivation, ParseNode> to, ParseForest parseNode) {
        return from.addLink(observing, to, parseNode, parseState.activeStacks);
    }

    @Override protected StackLink<ParseForest, BasicElkhoundStackNode<ParseForest, Derivation, ParseNode>>
        getOnlyLink(BasicElkhoundStackNode<ParseForest, Derivation, ParseNode> stack) {
        return stack.getOnlyLink();
    }

    @Override protected Iterable<StackLink<ParseForest, BasicElkhoundStackNode<ParseForest, Derivation, ParseNode>>>
        stackLinksOut(BasicElkhoundStackNode<ParseForest, Derivation, ParseNode> stack) {
        return stack.getLinks();
    }

}
