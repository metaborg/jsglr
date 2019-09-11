package org.spoofax.jsglr2.parser.observing;

import java.util.Queue;

import org.metaborg.parsetable.actions.IAction;
import org.metaborg.parsetable.actions.IReduce;
import org.metaborg.parsetable.productions.IProduction;
import org.metaborg.parsetable.states.IState;
import org.spoofax.jsglr2.elkhound.AbstractElkhoundStackNode;
import org.spoofax.jsglr2.parseforest.IDerivation;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parser.Parse;
import org.spoofax.jsglr2.parser.ForShifterElement;
import org.spoofax.jsglr2.parser.AbstractParseState;
import org.spoofax.jsglr2.parser.result.ParseFailure;
import org.spoofax.jsglr2.parser.result.ParseSuccess;
import org.spoofax.jsglr2.stack.IStackNode;
import org.spoofax.jsglr2.stack.StackLink;
import org.spoofax.jsglr2.stack.collections.IForActorStacks;

public interface IParserObserver
//@formatter:off
   <ParseForest extends IParseForest,
    StackNode   extends IStackNode,
    ParseState  extends AbstractParseState<ParseForest, StackNode>>
//@formatter:on
{

    void parseStart(Parse<ParseForest, StackNode, ParseState> parse);

    void parseRound(Parse<ParseForest, StackNode, ParseState> parse, Iterable<StackNode> activeStacks);

    void addActiveStack(StackNode stack);

    void addForActorStack(StackNode stack);

    void findActiveStackWithState(IState state);

    void createStackNode(StackNode stack);

    void createStackLink(StackLink<ParseForest, StackNode> link);

    void resetDeterministicDepth(AbstractElkhoundStackNode<ParseForest> stack);

    void rejectStackLink(StackLink<ParseForest, StackNode> link);

    void forActorStacks(IForActorStacks<StackNode> forActorStacks);

    void handleForActorStack(StackNode stack, IForActorStacks<StackNode> forActorStacks);

    void actor(StackNode stack, Parse<ParseForest, StackNode, ParseState> parse, Iterable<IAction> applicableActions);

    void skipRejectedStack(StackNode stack);

    void addForShifter(ForShifterElement<StackNode> forShifterElement);

    void doReductions(Parse<ParseForest, StackNode, ParseState> parse, StackNode stack, IReduce reduce);

    void doLimitedReductions(Parse<ParseForest, StackNode, ParseState> parse, StackNode stack, IReduce reduce,
        StackLink<ParseForest, StackNode> link);

    void reducer(StackNode stack, IReduce reduce, ParseForest[] parseNodes, StackNode activeStackWithGotoState);

    void reducerElkhound(StackNode stack, IReduce reduce, ParseForest[] parseNodes);

    void directLinkFound(Parse<ParseForest, StackNode, ParseState> parse, StackLink<ParseForest, StackNode> directLink);

    void accept(StackNode acceptingStack);

    void createParseNode(ParseForest parseNode, IProduction production);

    void createDerivation(IDerivation<ParseForest> derivationNode, IProduction production, ParseForest[] parseNodes);

    void createCharacterNode(ParseForest characterNode, int character);

    void addDerivation(ParseForest parseNode, IDerivation<ParseForest> derivation);

    void shifter(ParseForest termNode, Queue<ForShifterElement<StackNode>> forShifter);

    void remark(String remark);

    void success(ParseSuccess<ParseForest> success);

    void failure(ParseFailure<ParseForest> failure);

}
