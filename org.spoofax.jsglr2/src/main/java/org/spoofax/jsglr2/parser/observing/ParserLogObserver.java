package org.spoofax.jsglr2.parser.observing;

import java.util.Queue;
import java.util.logging.Logger;

import org.metaborg.characterclasses.CharacterClassFactory;
import org.metaborg.parsetable.IProduction;
import org.metaborg.parsetable.IState;
import org.metaborg.parsetable.actions.IAction;
import org.metaborg.parsetable.actions.IReduce;
import org.metaborg.parsetable.characterclasses.ICharacterClass;
import org.spoofax.jsglr2.elkhound.AbstractElkhoundStackNode;
import org.spoofax.jsglr2.parseforest.AbstractParseForest;
import org.spoofax.jsglr2.parser.ForShifterElement;
import org.spoofax.jsglr2.parser.Parse;
import org.spoofax.jsglr2.parser.ParseFailure;
import org.spoofax.jsglr2.parser.ParseSuccess;
import org.spoofax.jsglr2.stack.AbstractStackNode;
import org.spoofax.jsglr2.stack.StackLink;
import org.spoofax.jsglr2.stack.collections.IForActorStacks;

public class ParserLogObserver<ParseForest extends AbstractParseForest, StackNode extends AbstractStackNode<ParseForest>>
    implements IParserObserver<ParseForest, StackNode> {

    @Override
    public void parseStart(Parse<ParseForest, StackNode> parse) {
        log("\n  ---  Starting parse for input '" + parse.inputString + "'  ---\n");
    }

    @Override
    public void parseCharacter(Parse<ParseForest, StackNode> parse, Iterable<StackNode> activeStacks) {
        log("Parse character '" + CharacterClassFactory.intToString(parse.currentChar) + "' (active stacks: "
            + stackQueueToString(activeStacks) + ")");
    }

    @Override
    public void addActiveStack(StackNode stack) {
    }

    @Override
    public void addForActorStack(StackNode stack) {
    }

    @Override
    public void findActiveStackWithState(IState state) {
    }

    @Override
    public void createStackNode(StackNode stack) {
        log("Create new stack with number " + stack.stackNumber + " for state " + stack.state.id());
    }

    @Override
    public void createStackLink(StackLink<ParseForest, StackNode> link) {
        log("Create link " + link.linkNumber + " from stack " + link.from.stackNumber + " to stack "
            + link.to.stackNumber + " with parse node "
            + (link.parseForest != null ? link.parseForest.nodeNumber : "null"));
    }

    @Override
    public void resetDeterministicDepth(AbstractElkhoundStackNode<ParseForest> stack) {
        log("Reset deterministic depth for stack " + stack.stackNumber);
    }

    @Override
    public void rejectStackLink(StackLink<ParseForest, StackNode> link) {
        log("Reject link " + link.linkNumber);
    }

    @Override
    public void forActorStacks(IForActorStacks<StackNode> forActorStacks) {
        log("For actor stacks: " + forActorStacks);
    }

    @Override
    public void handleForActorStack(StackNode stack, IForActorStacks<StackNode> forActorStacks) {
    }

    @Override
    public void actor(StackNode stack, Parse<ParseForest, StackNode> parse, Iterable<IAction> applicableActions) {
        log("Actor for stack " + stack.stackNumber + " (applicable actions: "
            + applicableActionsToString(applicableActions) + ")");
    }

    @Override
    public void skipRejectedStack(StackNode stack) {
        log("Skipping stack " + stack.stackNumber + " since all links to it are rejected");
    }

    @Override
    public void addForShifter(ForShifterElement<ParseForest, StackNode> forShifterElement) {
        log("Add for shifter " + forShifterElementToString(forShifterElement));
    }

    @Override
    public void doReductions(Parse<ParseForest, StackNode> parse, StackNode stack, IReduce reduce) {
    }

    @Override
    public void doLimitedReductions(Parse<ParseForest, StackNode> parse, StackNode stack, IReduce reduce,
        StackLink<ParseForest, StackNode> link) {
    }

    @Override
    public void reducer(StackNode stack, IReduce reduce, ParseForest[] parseNodes, StackNode activeStackWithGotoState) {
        log("Reduce by prodution " + reduce.production().id() + " (" + reduce.productionType().toString()
            + ") with parse nodes " + parseForestListToString(parseNodes) + ", using existing stack: "
            + (activeStackWithGotoState != null ? activeStackWithGotoState.stackNumber : "no"));
    }

    @Override
    public void reducerElkhound(StackNode stack, IReduce reduce, ParseForest[] parseNodes) {
        log("Reduce (Elkhound) by prodution " + reduce.production().id() + " (" + reduce.productionType().toString()
            + ") with parse nodes " + parseForestListToString(parseNodes));
    }

    @Override
    public void directLinkFound(Parse<ParseForest, StackNode> parse, StackLink<ParseForest, StackNode> directLink) {
        log("Direct link " + (directLink != null ? directLink.linkNumber : "not") + " found");
    }

    @Override
    public void accept(StackNode acceptingStack) {
        log("Accept stack " + acceptingStack.stackNumber);
    }

    @Override
    public void createParseNode(ParseForest parseNode, IProduction production) {
        log("Create parse node " + parseNode.nodeNumber + " for production " + production.id());
    }

    @Override
    public void createDerivation(int nodeNumber, IProduction production, ParseForest[] parseNodes) {
        log("Create derivation with parse nodes " + parseForestListToString(parseNodes));
    }

    @Override
    public void createCharacterNode(ParseForest characterNode, int character) {
        log("Create character node " + characterNode.nodeNumber + " for character '"
            + CharacterClassFactory.intToString(character) + "'");
    }

    @Override
    public void addDerivation(ParseForest parseNode) {
        log("Add derivation to parse node '" + parseNode.nodeNumber);
    }

    @Override
    public void shifter(ParseForest termNode, Queue<ForShifterElement<ParseForest, StackNode>> forShifter) {
        log("Shifter for elements " + forShifterQueueToString(forShifter) + " with character node "
            + termNode.nodeNumber);
    }

    @Override
    public void remark(String remark) {
        log(remark);
    }

    @Override
    public void success(ParseSuccess<ParseForest, ?> success) {
        log("Parsing succeeded. Result: " + success.parseResult.toString());
    }

    @Override
    public void failure(ParseFailure<ParseForest, ?> failure) {
        log("Parsing failed");
    }

    private void log(String message) {
        Logger.getGlobal().info(message);
    }

}
