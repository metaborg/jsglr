package org.spoofax.jsglr2.stack;

import org.metaborg.parsetable.states.IState;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parser.AbstractParse;
import org.spoofax.jsglr2.parser.Position;

public abstract class StackManager
//@formatter:off
   <ParseForest extends IParseForest,
    StackNode   extends AbstractStackNode<ParseForest, StackNode>,
    Parse       extends AbstractParse<ParseForest, StackNode>>
//@formatter:on
    extends AbstractStackManager<ParseForest, StackNode, Parse> {

    protected abstract StackNode createStackNode(IState state, Position position, boolean isRoot);

    @Override public StackNode createInitialStackNode(Parse parse, IState state) {
        StackNode newStackNode = createStackNode(state, parse.currentPosition(), true);

        parse.observing.notify(observer -> observer.createStackNode(newStackNode));

        return newStackNode;
    }

    @Override public StackNode createStackNode(Parse parse, IState state) {
        StackNode newStackNode = createStackNode(state, parse.currentPosition(), false);

        parse.observing.notify(observer -> observer.createStackNode(newStackNode));

        return newStackNode;
    }

    @Override public StackLink<ParseForest, StackNode> createStackLink(Parse parse, StackNode from, StackNode to,
        ParseForest parseNode) {
        StackLink<ParseForest, StackNode> link = from.addLink(to, parseNode);

        parse.observing.notify(observer -> observer.createStackLink(link));

        return link;
    }

    @Override protected Iterable<StackLink<ParseForest, StackNode>> stackLinksOut(StackNode stack) {
        return stack.getLinks();
    }

}
