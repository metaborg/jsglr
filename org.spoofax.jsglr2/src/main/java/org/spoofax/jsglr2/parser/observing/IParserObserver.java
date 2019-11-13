package org.spoofax.jsglr2.parser.observing;

import java.util.Queue;

import org.metaborg.parsetable.actions.IAction;
import org.metaborg.parsetable.actions.IReduce;
import org.metaborg.parsetable.productions.IProduction;
import org.metaborg.parsetable.states.IState;
import org.spoofax.jsglr2.elkhound.AbstractElkhoundStackNode;
import org.spoofax.jsglr2.parseforest.IDerivation;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parseforest.IParseNode;
import org.spoofax.jsglr2.parser.AbstractParseState;
import org.spoofax.jsglr2.parser.ForShifterElement;
import org.spoofax.jsglr2.parser.result.ParseFailure;
import org.spoofax.jsglr2.parser.result.ParseSuccess;
import org.spoofax.jsglr2.recovery.IBacktrackChoicePoint;
import org.spoofax.jsglr2.stack.IStackNode;
import org.spoofax.jsglr2.stack.StackLink;
import org.spoofax.jsglr2.stack.collections.IForActorStacks;

public interface IParserObserver
//@formatter:off
   <ParseForest extends IParseForest,
    Derivation  extends IDerivation<ParseForest>,
    ParseNode   extends IParseNode<ParseForest, Derivation>,
    StackNode   extends IStackNode,
    ParseState  extends AbstractParseState<?, StackNode>>
//@formatter:on
{

    default void parseStart(ParseState parseState) {}

    default void parseRound(ParseState parseState, Iterable<StackNode> activeStacks) {}

    default void addActiveStack(StackNode stack) {}

    default void addForActorStack(StackNode stack) {}

    default void findActiveStackWithState(IState state) {}

    default void createStackNode(StackNode stack) {}

    default void createStackLink(StackLink<ParseForest, StackNode> link) {}

    default void resetDeterministicDepth(AbstractElkhoundStackNode<ParseForest> stack) {}

    default void rejectStackLink(StackLink<ParseForest, StackNode> link) {}

    default void forActorStacks(IForActorStacks<StackNode> forActorStacks) {}

    default void handleForActorStack(StackNode stack, IForActorStacks<StackNode> forActorStacks) {}

    default void actor(StackNode stack, ParseState parseState, Iterable<IAction> applicableActions) {}

    default void skipRejectedStack(StackNode stack) {}

    default void addForShifter(ForShifterElement<StackNode> forShifterElement) {}

    default void doReductions(ParseState parseState, StackNode stack, IReduce reduce) {}

    default void doLimitedReductions(ParseState parseState, StackNode stack, IReduce reduce,
        StackLink<ParseForest, StackNode> link) {}

    default void reducer(StackNode stack, IReduce reduce, ParseForest[] parseNodes, StackNode activeStackWithGotoState) {}

    default void reducerElkhound(StackNode stack, IReduce reduce, ParseForest[] parseNodes) {}

    default void directLinkFound(ParseState parseState, StackLink<ParseForest, StackNode> directLink) {}

    default void accept(StackNode acceptingStack) {}

    default void createParseNode(ParseNode parseNode, IProduction production) {}

    default void createDerivation(Derivation derivationNode, IProduction production, ParseForest[] parseNodes) {}

    default void createCharacterNode(ParseForest characterNode, int character) {}

    default void addDerivation(ParseNode parseNode, Derivation derivation) {}

    default void shifter(ParseForest termNode, Queue<ForShifterElement<StackNode>> forShifter) {}

    default void recoveryBacktrackChoicePoint(int index, IBacktrackChoicePoint<?, StackNode> choicePoint) {}

    default void startRecovery(ParseState parseState) {}

    default void recoveryIteration(ParseState parseState) {}

    default void endRecovery(ParseState parseState) {}

    default void remark(String remark) {}

    default void success(ParseSuccess<ParseForest> success) {}

    default void failure(ParseFailure<ParseForest> failure) {}

}
