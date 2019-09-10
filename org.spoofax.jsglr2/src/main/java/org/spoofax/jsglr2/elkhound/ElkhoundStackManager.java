package org.spoofax.jsglr2.elkhound;

import org.metaborg.parsetable.states.IState;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parseforest.ParseForestManager;
import org.spoofax.jsglr2.parser.AbstractParseState;
import org.spoofax.jsglr2.parser.Parse;
import org.spoofax.jsglr2.parser.Position;
import org.spoofax.jsglr2.stack.AbstractStackManager;
import org.spoofax.jsglr2.stack.StackLink;

public abstract class ElkhoundStackManager
//@formatter:off
   <ParseForest       extends IParseForest,
    ElkhoundStackNode extends AbstractElkhoundStackNode<ParseForest>,
    ParseState        extends AbstractParseState<ParseForest, ElkhoundStackNode>>
//@formatter:on
    extends AbstractStackManager<ParseForest, ElkhoundStackNode, ParseState> {

    protected abstract ElkhoundStackNode createStackNode(IState state, Position position, boolean isRoot);

    @Override public ElkhoundStackNode createInitialStackNode(Parse<ParseForest, ElkhoundStackNode, ParseState> parse,
        IState state) {
        ElkhoundStackNode newStackNode = createStackNode(state, parse.state.currentPosition(), true);

        parse.observing.notify(observer -> observer.createStackNode(newStackNode));

        return newStackNode;
    }

    @Override public ElkhoundStackNode createStackNode(Parse<ParseForest, ElkhoundStackNode, ParseState> parse,
        IState state) {
        ElkhoundStackNode newStackNode = createStackNode(state, parse.state.currentPosition(), false);

        parse.observing.notify(observer -> observer.createStackNode(newStackNode));

        return newStackNode;
    }

    @Override public StackLink<ParseForest, ElkhoundStackNode> createStackLink(
        Parse<ParseForest, ElkhoundStackNode, ParseState> parse, ElkhoundStackNode from, ElkhoundStackNode to,
        ParseForest parseNode) {
        StackLink<ParseForest, ElkhoundStackNode> link = addStackLink(parse, from, to, parseNode);

        parse.observing.notify(observer -> observer.createStackLink(link));

        return link;
    }

    public DeterministicStackPath<ParseForest, ElkhoundStackNode> findDeterministicPathOfLength(
        ParseForestManager<ParseForest, ?, ?, ?, ?> parseForestManager, ElkhoundStackNode stack, int length) {
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

        return new DeterministicStackPath<>(parseForests, lastStackNode);
    }

    protected abstract StackLink<ParseForest, ElkhoundStackNode> addStackLink(
        Parse<ParseForest, ElkhoundStackNode, ParseState> parse, ElkhoundStackNode from, ElkhoundStackNode to,
        ParseForest parseNode);

    protected abstract StackLink<ParseForest, ElkhoundStackNode> getOnlyLink(ElkhoundStackNode stack);

}
