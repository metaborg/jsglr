package org.spoofax.jsglr2.stack;

import org.metaborg.parsetable.IState;
import org.spoofax.jsglr2.parseforest.AbstractParseForest;
import org.spoofax.jsglr2.parser.AbstractParse;
import org.spoofax.jsglr2.parser.Position;

public abstract class StackManager
//@formatter:off
   <ParseForest extends AbstractParseForest,
    StackNode extends org.spoofax.jsglr2.stack.StackNode<ParseForest>>
//@formatter:on
    extends AbstractStackManager<ParseForest, org.spoofax.jsglr2.stack.StackNode<ParseForest>> {

    protected abstract StackNode createStackNode(IState state, Position position, boolean isRoot);

    @Override public org.spoofax.jsglr2.stack.StackNode<ParseForest> createInitialStackNode(
        AbstractParse<ParseForest, org.spoofax.jsglr2.stack.StackNode<ParseForest>> parse, IState state) {
        org.spoofax.jsglr2.stack.StackNode<ParseForest> newStackNode =
            createStackNode(state, parse.currentPosition(), true);

        parse.observing.notify(observer -> observer.createStackNode(newStackNode));

        return newStackNode;
    }

    @Override public org.spoofax.jsglr2.stack.StackNode<ParseForest> createStackNode(
        AbstractParse<ParseForest, org.spoofax.jsglr2.stack.StackNode<ParseForest>> parse, IState state) {
        org.spoofax.jsglr2.stack.StackNode<ParseForest> newStackNode =
            createStackNode(state, parse.currentPosition(), false);

        parse.observing.notify(observer -> observer.createStackNode(newStackNode));

        return newStackNode;
    }

    @Override public StackLink<ParseForest, org.spoofax.jsglr2.stack.StackNode<ParseForest>> createStackLink(
        AbstractParse<ParseForest, org.spoofax.jsglr2.stack.StackNode<ParseForest>> parse,
        org.spoofax.jsglr2.stack.StackNode<ParseForest> from, org.spoofax.jsglr2.stack.StackNode<ParseForest> to,
        ParseForest parseNode) {
        StackLink<ParseForest, org.spoofax.jsglr2.stack.StackNode<ParseForest>> link = from.addLink(to, parseNode);

        parse.observing.notify(observer -> observer.createStackLink(link));

        return link;
    }

    @Override protected Iterable<StackLink<ParseForest, org.spoofax.jsglr2.stack.StackNode<ParseForest>>>
        stackLinksOut(org.spoofax.jsglr2.stack.StackNode<ParseForest> stack) {
        return stack.getLinks();
    }

}
