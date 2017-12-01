package org.spoofax.jsglr2.stack.elkhound;

import org.spoofax.jsglr2.parseforest.AbstractParseForest;
import org.spoofax.jsglr2.parseforest.ParseForestManager;
import org.spoofax.jsglr2.parser.Parse;
import org.spoofax.jsglr2.parser.Position;
import org.spoofax.jsglr2.stack.StackLink;
import org.spoofax.jsglr2.stack.StackManager;
import org.spoofax.jsglr2.states.IState;

public abstract class AbstractElkhoundStackManager<ParseForest extends AbstractParseForest, StackNode extends AbstractElkhoundStackNode<ParseForest>>
    extends StackManager<ParseForest, AbstractElkhoundStackNode<ParseForest>> {

    protected abstract StackNode createStackNode(int stackNumber, IState state, Position position,
        int deterministicDepth);

    @Override
    public AbstractElkhoundStackNode<ParseForest>
        createInitialStackNode(Parse<ParseForest, AbstractElkhoundStackNode<ParseForest>> parse, IState state) {
        AbstractElkhoundStackNode<ParseForest> newStackNode =
            createStackNode(parse.stackNodeCount++, state, parse.currentPosition(), 1);

        parse.notify(observer -> observer.createStackNode(newStackNode));

        return newStackNode;
    }

    @Override
    public AbstractElkhoundStackNode<ParseForest>
        createStackNode(Parse<ParseForest, AbstractElkhoundStackNode<ParseForest>> parse, IState state) {
        AbstractElkhoundStackNode<ParseForest> newStackNode =
            createStackNode(parse.stackNodeCount++, state, parse.currentPosition(), 0);

        parse.notify(observer -> observer.createStackNode(newStackNode));

        return newStackNode;
    }

    @Override
    public StackLink<ParseForest, AbstractElkhoundStackNode<ParseForest>> createStackLink(
        Parse<ParseForest, AbstractElkhoundStackNode<ParseForest>> parse, AbstractElkhoundStackNode<ParseForest> from,
        AbstractElkhoundStackNode<ParseForest> to, ParseForest parseNode) {
        StackLink<ParseForest, AbstractElkhoundStackNode<ParseForest>> link =
            from.addOutLink(parse.stackLinkCount++, to, parseNode, parse);

        parse.notify(observer -> observer.createStackLink(link));

        return link;
    }

    public DeterministicStackPath<ParseForest, AbstractElkhoundStackNode<ParseForest>> findDeterministicPathOfLength(
        ParseForestManager<ParseForest, ?, ?> parseForestManager, AbstractElkhoundStackNode<ParseForest> stack,
        int length) {
        AbstractElkhoundStackNode<ParseForest> lastStackNode = stack;
        AbstractElkhoundStackNode<ParseForest> currentStackNode = stack;

        ParseForest[] parseForests = parseForestManager.parseForestsArray(length);

        for(int i = length - 1; i >= 0; i--) {
            StackLink<ParseForest, AbstractElkhoundStackNode<ParseForest>> link = currentStackNode.getOnlyLinkOut();

            if(parseForests != null) {
                parseForests[i] = link.parseForest;
            }

            if(i == 0)
                lastStackNode = link.to;
            else
                currentStackNode = link.to;
        }

        return new DeterministicStackPath<ParseForest, AbstractElkhoundStackNode<ParseForest>>(parseForests,
            lastStackNode);
    }

    @Override
    protected Iterable<StackLink<ParseForest, AbstractElkhoundStackNode<ParseForest>>>
        stackLinksOut(AbstractElkhoundStackNode<ParseForest> stack) {
        return stack.getLinksOut();
    }

}
