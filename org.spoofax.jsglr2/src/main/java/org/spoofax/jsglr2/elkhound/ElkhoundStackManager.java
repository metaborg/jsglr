package org.spoofax.jsglr2.elkhound;

import org.metaborg.parsetable.states.IState;
import org.spoofax.jsglr2.parseforest.IDerivation;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parseforest.IParseNode;
import org.spoofax.jsglr2.parseforest.ParseForestManager;
import org.spoofax.jsglr2.parser.AbstractParseState;
import org.spoofax.jsglr2.parser.observing.ParserObserving;
import org.spoofax.jsglr2.stack.AbstractStackManager;
import org.spoofax.jsglr2.stack.StackLink;

public abstract class ElkhoundStackManager
//@formatter:off
   <ParseForest       extends IParseForest,
    Derivation        extends IDerivation<ParseForest>,
    ParseNode         extends IParseNode<ParseForest, Derivation>,
    ElkhoundStackNode extends AbstractElkhoundStackNode<ParseForest>,
    ParseState        extends AbstractParseState<?, ElkhoundStackNode>>
//@formatter:on
    extends AbstractStackManager<ParseForest, Derivation, ParseNode, ElkhoundStackNode, ParseState> {

    public ElkhoundStackManager(
        ParserObserving<ParseForest, Derivation, ParseNode, ElkhoundStackNode, ParseState> observing) {
        super(observing);
    }

    protected abstract ElkhoundStackNode createStackNode(IState state, boolean isRoot);

    @Override public ElkhoundStackNode createInitialStackNode(IState state) {
        ElkhoundStackNode newStackNode = createStackNode(state, true);

        observing.notify(observer -> observer.createStackNode(newStackNode));

        return newStackNode;
    }

    @Override public ElkhoundStackNode createStackNode(IState state) {
        ElkhoundStackNode newStackNode = createStackNode(state, false);

        observing.notify(observer -> observer.createStackNode(newStackNode));

        return newStackNode;
    }

    @Override public StackLink<ParseForest, ElkhoundStackNode> createStackLink(ParseState parseState,
        ElkhoundStackNode from, ElkhoundStackNode to, ParseForest parseNode) {
        StackLink<ParseForest, ElkhoundStackNode> link = addStackLink(parseState, from, to, parseNode);

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

    protected abstract StackLink<ParseForest, ElkhoundStackNode> addStackLink(ParseState parseState,
        ElkhoundStackNode from, ElkhoundStackNode to, ParseForest parseNode);

    protected abstract StackLink<ParseForest, ElkhoundStackNode> getOnlyLink(ElkhoundStackNode stack);

}
