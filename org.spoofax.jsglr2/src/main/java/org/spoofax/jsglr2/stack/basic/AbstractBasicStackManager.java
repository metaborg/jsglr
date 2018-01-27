package org.spoofax.jsglr2.stack.basic;

import org.metaborg.parsetable.IState;
import org.spoofax.jsglr2.parseforest.AbstractParseForest;
import org.spoofax.jsglr2.parser.Parse;
import org.spoofax.jsglr2.parser.Position;
import org.spoofax.jsglr2.stack.StackLink;
import org.spoofax.jsglr2.stack.StackManager;

public abstract class AbstractBasicStackManager<ParseForest extends AbstractParseForest, StackNode extends AbstractBasicStackNode<ParseForest>>
    extends StackManager<ParseForest, AbstractBasicStackNode<ParseForest>> {

    protected abstract StackNode createStackNode(int stackNumber, IState state, Position position);

    @Override
    public AbstractBasicStackNode<ParseForest>
        createInitialStackNode(Parse<ParseForest, AbstractBasicStackNode<ParseForest>> parse, IState state) {
        AbstractBasicStackNode<ParseForest> newStackNode =
            createStackNode(parse.stackNodeCount++, state, parse.currentPosition());

        parse.observing.notify(observer -> observer.createStackNode(newStackNode));

        return newStackNode;
    }

    @Override
    public AbstractBasicStackNode<ParseForest>
        createStackNode(Parse<ParseForest, AbstractBasicStackNode<ParseForest>> parse, IState state) {
        AbstractBasicStackNode<ParseForest> newStackNode =
            createStackNode(parse.stackNodeCount++, state, parse.currentPosition());

        parse.observing.notify(observer -> observer.createStackNode(newStackNode));

        return newStackNode;
    }

    @Override
    public StackLink<ParseForest, AbstractBasicStackNode<ParseForest>> createStackLink(
        Parse<ParseForest, AbstractBasicStackNode<ParseForest>> parse, AbstractBasicStackNode<ParseForest> from,
        AbstractBasicStackNode<ParseForest> to, ParseForest parseNode) {
        StackLink<ParseForest, AbstractBasicStackNode<ParseForest>> link =
            from.addLink(parse.stackLinkCount++, to, parseNode);

        parse.observing.notify(observer -> observer.createStackLink(link));

        return link;
    }

    @Override
    protected Iterable<StackLink<ParseForest, AbstractBasicStackNode<ParseForest>>>
        stackLinksOut(AbstractBasicStackNode<ParseForest> stack) {
        return stack.getLinks();
    }

}
