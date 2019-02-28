package org.spoofax.jsglr2.parser.observing;

import java.util.Queue;

import org.metaborg.parsetable.IProduction;
import org.metaborg.parsetable.IState;
import org.metaborg.parsetable.actions.IAction;
import org.metaborg.parsetable.actions.IReduce;
import org.spoofax.jsglr2.elkhound.ElkhoundStackNode;
import org.spoofax.jsglr2.parseforest.AbstractParseForest;
import org.spoofax.jsglr2.parser.AbstractParse;
import org.spoofax.jsglr2.parser.ForShifterElement;
import org.spoofax.jsglr2.parser.result.ParseFailure;
import org.spoofax.jsglr2.parser.result.ParseSuccess;
import org.spoofax.jsglr2.stack.AbstractStackNode;
import org.spoofax.jsglr2.stack.StackLink;
import org.spoofax.jsglr2.stack.collections.IForActorStacks;

public interface IParserObserver
//@formatter:off
   <ParseForest extends AbstractParseForest,
    StackNode   extends AbstractStackNode<ParseForest>>
//@formatter:on
{

    void parseStart(AbstractParse<ParseForest, StackNode> parse);

    void parseCharacter(AbstractParse<ParseForest, StackNode> parse, Iterable<StackNode> activeStacks);

    void addActiveStack(StackNode stack);

    void addForActorStack(StackNode stack);

    void findActiveStackWithState(IState state);

    void createStackNode(StackNode stack);

    void createStackLink(StackLink<ParseForest, StackNode> link);

    void resetDeterministicDepth(ElkhoundStackNode<ParseForest> stack);

    void rejectStackLink(StackLink<ParseForest, StackNode> link);

    void forActorStacks(IForActorStacks<StackNode> forActorStacks);

    void handleForActorStack(StackNode stack, IForActorStacks<StackNode> forActorStacks);

    void actor(StackNode stack, AbstractParse<ParseForest, StackNode> parse, Iterable<IAction> applicableActions);

    void skipRejectedStack(StackNode stack);

    void addForShifter(ForShifterElement<ParseForest, StackNode> forShifterElement);

    void doReductions(AbstractParse<ParseForest, StackNode> parse, StackNode stack, IReduce reduce);

    void doLimitedReductions(AbstractParse<ParseForest, StackNode> parse, StackNode stack, IReduce reduce,
        StackLink<ParseForest, StackNode> link);

    void reducer(StackNode stack, IReduce reduce, ParseForest[] parseNodes, StackNode activeStackWithGotoState);

    void reducerElkhound(StackNode stack, IReduce reduce, ParseForest[] parseNodes);

    void directLinkFound(AbstractParse<ParseForest, StackNode> parse, StackLink<ParseForest, StackNode> directLink);

    void accept(StackNode acceptingStack);

    void createParseNode(ParseForest parseNode, IProduction production);

    void createDerivation(int nodeNumber, IProduction production, ParseForest[] parseNodes);

    void createCharacterNode(ParseForest characterNode, int character);

    void addDerivation(ParseForest parseNode);

    void shifter(ParseForest termNode, Queue<ForShifterElement<ParseForest, StackNode>> forShifter);

    void remark(String remark);

    void success(ParseSuccess<ParseForest, ?> success);

    void failure(ParseFailure<ParseForest, ?> failure);

}
