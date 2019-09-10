package org.spoofax.jsglr2.stack;

import org.metaborg.parsetable.states.IState;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parser.AbstractParseState;
import org.spoofax.jsglr2.parser.Parse;
import org.spoofax.jsglr2.parser.Position;

public abstract class StackManager
//@formatter:off
   <ParseForest extends IParseForest,
    StackNode   extends AbstractStackNode<ParseForest, StackNode>,
    ParseState  extends AbstractParseState<ParseForest, StackNode>>
//@formatter:on
    extends AbstractStackManager<ParseForest, StackNode, ParseState> {

    protected abstract StackNode createStackNode(IState state, Position position, boolean isRoot);

    @Override public StackNode createInitialStackNode(Parse<ParseForest, StackNode, ParseState> parse, IState state) {
        StackNode newStackNode = createStackNode(state, parse.state.currentPosition(), true);

        parse.observing.notify(observer -> observer.createStackNode(newStackNode));

        return newStackNode;
    }

    @Override public StackNode createStackNode(Parse<ParseForest, StackNode, ParseState> parse, IState state) {
        StackNode newStackNode = createStackNode(state, parse.state.currentPosition(), false);

        parse.observing.notify(observer -> observer.createStackNode(newStackNode));

        return newStackNode;
    }

    @Override public StackLink<ParseForest, StackNode> createStackLink(Parse<ParseForest, StackNode, ParseState> parse,
        StackNode from, StackNode to, ParseForest parseNode) {
        StackLink<ParseForest, StackNode> link = from.addLink(to, parseNode);

        parse.observing.notify(observer -> observer.createStackLink(link));

        return link;
    }

    @Override protected Iterable<StackLink<ParseForest, StackNode>> stackLinksOut(StackNode stack) {
        return stack.getLinks();
    }

}
