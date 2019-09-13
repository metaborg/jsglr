package org.spoofax.jsglr2.elkhound;

import org.metaborg.parsetable.states.IState;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parseforest.ParseForestManager;
import org.spoofax.jsglr2.parser.AbstractParseState;
import org.spoofax.jsglr2.parser.observing.ParserObserving;
import org.spoofax.jsglr2.stack.AbstractStackManager;
import org.spoofax.jsglr2.stack.StackLink;

public abstract class ElkhoundStackManager
//@formatter:off
   <ParseForest       extends IParseForest,
    ElkhoundStackNode extends AbstractElkhoundStackNode<ParseForest>,
    ParseState        extends AbstractParseState<ParseForest, ElkhoundStackNode>>
//@formatter:on
    extends AbstractStackManager<ParseForest, ElkhoundStackNode, ParseState> {

    protected abstract ElkhoundStackNode createStackNode(IState state, boolean isRoot);

    @Override public ElkhoundStackNode
        createInitialStackNode(ParserObserving<ParseForest, ElkhoundStackNode, ParseState> observing, IState state) {
        ElkhoundStackNode newStackNode = createStackNode(state, true);

        observing.notify(observer -> observer.createStackNode(newStackNode));

        return newStackNode;
    }

    @Override public ElkhoundStackNode
        createStackNode(ParserObserving<ParseForest, ElkhoundStackNode, ParseState> observing, IState state) {
        ElkhoundStackNode newStackNode = createStackNode(state, false);

        observing.notify(observer -> observer.createStackNode(newStackNode));

        return newStackNode;
    }

    @Override public StackLink<ParseForest, ElkhoundStackNode> createStackLink(
        ParserObserving<ParseForest, ElkhoundStackNode, ParseState> observing, ParseState parseState,
        ElkhoundStackNode from, ElkhoundStackNode to, ParseForest parseNode) {
        StackLink<ParseForest, ElkhoundStackNode> link = addStackLink(observing, parseState, from, to, parseNode);

        observing.notify(observer -> observer.createStackLink(link));

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
        ParserObserving<ParseForest, ElkhoundStackNode, ParseState> observing, ParseState parseState,
        ElkhoundStackNode from, ElkhoundStackNode to, ParseForest parseNode);

    protected abstract StackLink<ParseForest, ElkhoundStackNode> getOnlyLink(ElkhoundStackNode stack);

}
