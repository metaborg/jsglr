package org.spoofax.jsglr2.parser.observing;

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

import java.util.Queue;

public interface IParserObserver
//@formatter:off
   <ParseForest extends IParseForest,
    Derivation  extends IDerivation<ParseForest>,
    ParseNode   extends IParseNode<ParseForest, Derivation>,
    StackNode   extends IStackNode,
    ParseState  extends AbstractParseState<StackNode>>
//@formatter:on
{

    void parseStart(ParseState parseState);

    void parseRound(ParseState parseState, Iterable<StackNode> activeStacks,
        ParserObserving<ParseForest, Derivation, ParseNode, StackNode, ParseState> observing);

    void addActiveStack(StackNode stack);

    void addForActorStack(StackNode stack);

    void findActiveStackWithState(IState state);

    void createStackNode(StackNode stack);

    void createStackLink(StackLink<ParseForest, StackNode> link);

    void resetDeterministicDepth(AbstractElkhoundStackNode<ParseForest> stack);

    void rejectStackLink(StackLink<ParseForest, StackNode> link);

    void forActorStacks(IForActorStacks<StackNode> forActorStacks);

    void handleForActorStack(StackNode stack, IForActorStacks<StackNode> forActorStacks);

    void actor(StackNode stack, ParseState parseState, Iterable<IAction> applicableActions);

    void skipRejectedStack(StackNode stack);

    void addForShifter(ForShifterElement<StackNode> forShifterElement);

    void doReductions(ParseState parseState, StackNode stack, IReduce reduce);

    void doLimitedReductions(ParseState parseState, StackNode stack, IReduce reduce,
        StackLink<ParseForest, StackNode> link);

    void reducer(StackNode stack, IReduce reduce, ParseForest[] parseNodes, StackNode activeStackWithGotoState);

    void reducerElkhound(StackNode stack, IReduce reduce, ParseForest[] parseNodes);

    void directLinkFound(ParseState parseState, StackLink<ParseForest, StackNode> directLink);

    void accept(StackNode acceptingStack);

    void createParseNode(ParseNode parseNode, IProduction production);

    void createDerivation(Derivation derivationNode, IProduction production, ParseForest[] parseNodes);

    void createCharacterNode(ParseForest characterNode, int character);

    void addDerivation(ParseNode parseNode, Derivation derivation);

    void shifter(ParseForest termNode, Queue<ForShifterElement<StackNode>> forShifter);

    void recoveryBacktrackChoicePoint(int index, IBacktrackChoicePoint<StackNode> choicePoint);

    void startRecovery(ParseState parseState);

    void recoveryIteration(ParseState parseState);

    void endRecovery(ParseState parseState);

    void remark(String remark);

    void success(ParseSuccess<ParseForest> success);

    void failure(ParseFailure<ParseForest> failure);

}
