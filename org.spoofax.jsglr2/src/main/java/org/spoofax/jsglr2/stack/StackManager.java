package org.spoofax.jsglr2.stack;

import org.metaborg.parsetable.states.IState;
import org.spoofax.jsglr2.parseforest.IDerivation;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parseforest.IParseNode;
import org.spoofax.jsglr2.parser.AbstractParseState;
import org.spoofax.jsglr2.parser.observing.ParserObserving;

public abstract class StackManager
//@formatter:off
   <ParseForest extends IParseForest,
    Derivation  extends IDerivation<ParseForest>,
    ParseNode   extends IParseNode<ParseForest, Derivation>,
    StackNode   extends AbstractStackNode<ParseForest, StackNode>,
    ParseState  extends AbstractParseState<ParseForest, StackNode>>
//@formatter:on
    extends AbstractStackManager<ParseForest, Derivation, ParseNode, StackNode, ParseState> {

    public StackManager(ParserObserving<ParseForest, Derivation, ParseNode, StackNode, ParseState> observing) {
        super(observing);
    }

    protected abstract StackNode createStackNode(IState state, boolean isRoot);

    @Override public StackNode createInitialStackNode(IState state) {
        StackNode newStackNode = createStackNode(state, true);

        observing.notify(observer -> observer.createStackNode(newStackNode));

        return newStackNode;
    }

    @Override public StackNode createStackNode(IState state) {
        StackNode newStackNode = createStackNode(state, false);

        observing.notify(observer -> observer.createStackNode(newStackNode));

        return newStackNode;
    }

    @Override public StackLink<ParseForest, StackNode> createStackLink(ParseState parseState, StackNode from,
        StackNode to, ParseForest parseForest) {
        StackLink<ParseForest, StackNode> link = from.addLink(to, parseForest);

        observing.notify(observer -> observer.createStackLink(link));

        return link;
    }

    @Override protected Iterable<StackLink<ParseForest, StackNode>> stackLinksOut(StackNode stack) {
        return stack.getLinks();
    }

}
