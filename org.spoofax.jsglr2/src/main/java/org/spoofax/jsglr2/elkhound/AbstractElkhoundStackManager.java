package org.spoofax.jsglr2.elkhound;

import org.metaborg.parsetable.IState;
import org.spoofax.jsglr2.parseforest.AbstractParseForest;
import org.spoofax.jsglr2.parseforest.ParseForestManager;
import org.spoofax.jsglr2.parser.Parse;
import org.spoofax.jsglr2.parser.Position;
import org.spoofax.jsglr2.stack.StackLink;
import org.spoofax.jsglr2.stack.StackManager;

public abstract class AbstractElkhoundStackManager<ParseForest extends AbstractParseForest, ElkhoundStackNode extends AbstractElkhoundStackNode<ParseForest>>
    extends StackManager<ParseForest, ElkhoundStackNode> {

    @Override
    public ElkhoundStackNode createInitialStackNode(Parse<ParseForest, ElkhoundStackNode> parse, IState state) {
        ElkhoundStackNode newStackNode = createStackNode(parse.stackNodeCount++, state, parse.currentPosition(), true);

        parse.observing.notify(observer -> observer.createStackNode(newStackNode));

        return newStackNode;
    }

    @Override
    public ElkhoundStackNode createStackNode(Parse<ParseForest, ElkhoundStackNode> parse, IState state) {
        ElkhoundStackNode newStackNode = createStackNode(parse.stackNodeCount++, state, parse.currentPosition(), false);

        parse.observing.notify(observer -> observer.createStackNode(newStackNode));

        return newStackNode;
    }

    @Override
    public StackLink<ParseForest, ElkhoundStackNode> createStackLink(Parse<ParseForest, ElkhoundStackNode> parse,
        ElkhoundStackNode from, ElkhoundStackNode to, ParseForest parseNode) {
        StackLink<ParseForest, ElkhoundStackNode> link = addStackLink(parse, from, to, parseNode);

        parse.observing.notify(observer -> observer.createStackLink(link));

        return link;
    }

    public DeterministicStackPath<ParseForest, ElkhoundStackNode> findDeterministicPathOfLength(
        ParseForestManager<ParseForest, ?, ?> parseForestManager, ElkhoundStackNode stack, int length) {
        ElkhoundStackNode lastStackNode = stack;
        ElkhoundStackNode currentStackNode = stack;

        ParseForest[] parseForests = parseForestManager.parseForestsArray(length);

        for(int i = length - 1; i >= 0; i--) {
            StackLink<ParseForest, ElkhoundStackNode> link = getOnlyLink(currentStackNode);

            if(parseForests != null) {
                parseForests[i] = link.parseForest;
            }

            if(i == 0)
                lastStackNode = link.to;
            else
                currentStackNode = link.to;
        }

        return new DeterministicStackPath<ParseForest, ElkhoundStackNode>(parseForests, lastStackNode);
    }

    protected abstract ElkhoundStackNode createStackNode(int stackNumber, IState state, Position position,
        boolean isRoot);

    protected abstract StackLink<ParseForest, ElkhoundStackNode> addStackLink(
        Parse<ParseForest, ElkhoundStackNode> parse, ElkhoundStackNode from, ElkhoundStackNode to,
        ParseForest parseNode);

    protected abstract StackLink<ParseForest, ElkhoundStackNode> getOnlyLink(ElkhoundStackNode stack);

}
