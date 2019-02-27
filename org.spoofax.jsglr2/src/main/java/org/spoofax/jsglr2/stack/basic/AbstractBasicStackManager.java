package org.spoofax.jsglr2.stack.basic;

import org.metaborg.parsetable.IState;
import org.spoofax.jsglr2.parseforest.AbstractParseForest;
import org.spoofax.jsglr2.parser.AbstractParse;
import org.spoofax.jsglr2.parser.Position;
import org.spoofax.jsglr2.stack.StackLink;
import org.spoofax.jsglr2.stack.StackManager;

public abstract class AbstractBasicStackManager<ParseForest extends AbstractParseForest, StackNode extends AbstractBasicStackNode<ParseForest>>
    extends StackManager<ParseForest, AbstractBasicStackNode<ParseForest>> {

    protected abstract StackNode createStackNode(IState state, Position position, boolean isRoot);

    @Override
    public AbstractBasicStackNode<ParseForest>
        createInitialStackNode(AbstractParse<ParseForest, AbstractBasicStackNode<ParseForest>> parse, IState state) {
        AbstractBasicStackNode<ParseForest> newStackNode = createStackNode(state, parse.currentPosition(), true);

        parse.observing.notify(observer -> observer.createStackNode(newStackNode));

        return newStackNode;
    }

    @Override
    public AbstractBasicStackNode<ParseForest>
        createStackNode(AbstractParse<ParseForest, AbstractBasicStackNode<ParseForest>> parse, IState state) {
        AbstractBasicStackNode<ParseForest> newStackNode = createStackNode(state, parse.currentPosition(), false);

        parse.observing.notify(observer -> observer.createStackNode(newStackNode));

        return newStackNode;
    }

    @Override
    public StackLink<ParseForest, AbstractBasicStackNode<ParseForest>> createStackLink(
        AbstractParse<ParseForest, AbstractBasicStackNode<ParseForest>> parse, AbstractBasicStackNode<ParseForest> from,
        AbstractBasicStackNode<ParseForest> to, ParseForest parseNode) {
        StackLink<ParseForest, AbstractBasicStackNode<ParseForest>> link = from.addLink(to, parseNode);

        parse.observing.notify(observer -> observer.createStackLink(link));

        return link;
    }

    @Override
    protected Iterable<StackLink<ParseForest, AbstractBasicStackNode<ParseForest>>>
        stackLinksOut(AbstractBasicStackNode<ParseForest> stack) {
        return stack.getLinks();
    }

}
