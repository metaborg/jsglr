package org.spoofax.jsglr2.recovery;

import org.metaborg.parsetable.actions.IAction;
import org.metaborg.parsetable.actions.IReduce;
import org.metaborg.parsetable.characterclasses.CharacterClassFactory;
import org.metaborg.parsetable.productions.IProduction;
import org.metaborg.parsetable.states.IState;
import org.spoofax.jsglr2.elkhound.AbstractElkhoundStackNode;
import org.spoofax.jsglr2.parseforest.IDerivation;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parser.AbstractParseState;
import org.spoofax.jsglr2.parser.ForShifterElement;
import org.spoofax.jsglr2.parser.Parse;
import org.spoofax.jsglr2.parser.observing.IParserObserver;
import org.spoofax.jsglr2.parser.result.ParseFailure;
import org.spoofax.jsglr2.parser.result.ParseSuccess;
import org.spoofax.jsglr2.stack.IStackNode;
import org.spoofax.jsglr2.stack.StackLink;
import org.spoofax.jsglr2.stack.collections.IForActorStacks;

import java.util.Queue;

public class RecoveryParserObserver
//@formatter:off
   <ParseForest extends IParseForest,
    StackNode   extends IStackNode,
    ParseState  extends AbstractParseState<ParseForest, StackNode> & IRecoveryParseState<ParseForest, StackNode>>
//@formatter:on
    implements IParserObserver<ParseForest, StackNode, ParseState> {

    @Override public void parseStart(Parse<ParseForest, StackNode, ParseState> parse) {
        parse.state.initializeBacktrackChoicePoints(parse.state.inputString);
        parse.state.saveBacktrackChoicePoint(parse.state.currentPosition(), parse.state.activeStacks);
    }

    @Override public void parseCharacter(Parse<ParseForest, StackNode, ParseState> parse,
        Iterable<StackNode> activeStacks) {
    }

    @Override public void parseNext(Parse<ParseForest, StackNode, ParseState> parse) {
        if(CharacterClassFactory.isNewLine(parse.state.currentChar))
            parse.state.saveBacktrackChoicePoint(parse.state.currentPosition(), parse.state.activeStacks);

        // TODO: check if recovery mode can be leaved
    }

    @Override public void addActiveStack(StackNode stack) {
    }

    @Override public void addForActorStack(StackNode stack) {
    }

    @Override public void findActiveStackWithState(IState state) {
    }

    @Override public void createStackNode(StackNode stack) {
    }

    @Override public void createStackLink(StackLink<ParseForest, StackNode> link) {
    }

    @Override public void resetDeterministicDepth(AbstractElkhoundStackNode<ParseForest> stack) {
    }

    @Override public void rejectStackLink(StackLink<ParseForest, StackNode> link) {
    }

    @Override public void forActorStacks(IForActorStacks<StackNode> forActorStacks) {
    }

    @Override public void handleForActorStack(StackNode stack, IForActorStacks<StackNode> forActorStacks) {
    }

    @Override public void actor(StackNode stack, Parse<ParseForest, StackNode, ParseState> parse,
        Iterable<IAction> applicableActions) {
    }

    @Override public void skipRejectedStack(StackNode stack) {
    }

    @Override public void addForShifter(ForShifterElement<StackNode> forShifterElement) {
    }

    @Override public void doReductions(Parse<ParseForest, StackNode, ParseState> parse, StackNode stack,
        IReduce reduce) {
    }

    @Override public void doLimitedReductions(Parse<ParseForest, StackNode, ParseState> parse, StackNode stack,
        IReduce reduce, StackLink<ParseForest, StackNode> link) {
    }

    @Override public void reducer(StackNode stack, IReduce reduce, ParseForest[] parseNodes,
        StackNode activeStackWithGotoState) {
    }

    @Override public void reducerElkhound(StackNode stack, IReduce reduce, ParseForest[] parseNodes) {
    }

    @Override public void directLinkFound(Parse<ParseForest, StackNode, ParseState> parse,
        StackLink<ParseForest, StackNode> directLink) {
    }

    @Override public void accept(StackNode acceptingStack) {
    }

    @Override public void createParseNode(ParseForest parseNode, IProduction production) {
    }

    @Override public void createDerivation(IDerivation<ParseForest> derivation, IProduction production,
        ParseForest[] parseNodes) {
    }

    @Override public void createCharacterNode(ParseForest characterNode, int character) {
    }

    @Override public void addDerivation(ParseForest parseNode, IDerivation<ParseForest> derivation) {
    }

    @Override public void shifter(ParseForest termNode, Queue<ForShifterElement<StackNode>> forShifter) {
    }

    @Override public void remark(String remark) {
    }

    @Override public void success(ParseSuccess<ParseForest> success) {
    }

    @Override public void failure(ParseFailure<ParseForest> failure) {
    }

}
