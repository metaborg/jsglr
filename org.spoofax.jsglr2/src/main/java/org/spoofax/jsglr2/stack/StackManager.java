package org.spoofax.jsglr2.stack;

import org.metaborg.parsetable.states.IState;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parser.AbstractParseState;
import org.spoofax.jsglr2.parser.observing.ParserObserving;

public abstract class StackManager
//@formatter:off
   <ParseForest extends IParseForest,
    StackNode   extends AbstractStackNode<ParseForest, StackNode>,
    ParseState  extends AbstractParseState<ParseForest, StackNode>>
//@formatter:on
    extends AbstractStackManager<ParseForest, StackNode, ParseState> {

    public StackManager(ParserObserving<ParseForest, StackNode, ParseState> observing) {
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
        StackNode to, ParseForest parseNode) {
        StackLink<ParseForest, StackNode> link = from.addLink(to, parseNode);

        observing.notify(observer -> observer.createStackLink(link));

        return link;
    }

    @Override protected Iterable<StackLink<ParseForest, StackNode>> stackLinksOut(StackNode stack) {
        return stack.getLinks();
    }

}
