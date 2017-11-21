package org.spoofax.jsglr2.parser;

import java.util.Queue;
import java.util.logging.Logger;

import org.spoofax.jsglr2.actions.IAction;
import org.spoofax.jsglr2.actions.IReduce;
import org.spoofax.jsglr2.characters.ICharacters;
import org.spoofax.jsglr2.parseforest.AbstractParseForest;
import org.spoofax.jsglr2.parsetable.IProduction;
import org.spoofax.jsglr2.parsetable.IState;
import org.spoofax.jsglr2.stack.AbstractStackNode;
import org.spoofax.jsglr2.stack.StackLink;
import org.spoofax.jsglr2.stack.elkhound.AbstractElkhoundStackNode;

public class ParserLogObserver<StackNode extends AbstractStackNode<ParseForest>, ParseForest extends AbstractParseForest>
    implements IParserObserver<StackNode, ParseForest> {

    @Override public void parseStart(Parse<StackNode, ParseForest> parse) {
        log("\n  ---  Starting parse for input '" + parse.inputString + "'  ---\n");
    }

    @Override public void parseCharacter(int character, Iterable<StackNode> activeStacks) {
        log("Parse character '" + ICharacters.intToString(character) + "' (active stacks: "
            + stackQueueToString(activeStacks) + ")");
    }

    @Override public void addActiveStack(StackNode stack) {
    }

    @Override public void findActiveStackWithState(IState state) {
    }

    @Override public void createStackNode(StackNode stack) {
        log("Create new stack with number " + stack.stackNumber + " for state " + stack.state.stateNumber());
    }

    @Override public void createStackLink(StackLink<StackNode, ParseForest> link) {
        log("Create link " + link.linkNumber + " from stack " + link.from.stackNumber + " to stack "
            + link.to.stackNumber + " with parse node " + link.parseForest.nodeNumber);
    }

    @Override public void resetDeterministicDepth(AbstractElkhoundStackNode<ParseForest> stack) {
        log("Reset deterministic depth for stack " + stack.stackNumber);
    }

    @Override public void rejectStackLink(StackLink<StackNode, ParseForest> link) {
        log("Reject link " + link.linkNumber);
    }

    @Override public void forActorStacks(IForActorStacks<StackNode> forActorStacks) {
        log("For actor stacks: " + forActorStacks);
    }

    @Override public void actor(StackNode stack, Parse<StackNode, ParseForest> parse,
        Iterable<IAction> applicableActions) {
        log("Actor for stack " + stack.stackNumber + " (applicable actions: "
            + applicableActionsToString(applicableActions) + ")");
    }

    @Override public void skipRejectedStack(StackNode stack) {
        log("Skipping stack " + stack.stackNumber + " since all links to it are rejected");
    }

    @Override public void addForShifter(ForShifterElement<StackNode, ParseForest> forShifterElement) {
        log("Add for shifter " + forShifterElementToString(forShifterElement));
    }

    @Override public void doReductions(Parse<StackNode, ParseForest> parse, StackNode stack, IReduce reduce) {
    }

    @Override public void doLimitedReductions(Parse<StackNode, ParseForest> parse, StackNode stack, IReduce reduce,
        StackLink<StackNode, ParseForest> link) {
    }

    @Override public void reducer(StackNode stack, IReduce reduce, ParseForest[] parseNodes,
        StackNode activeStackWithGotoState) {
        log("Reduce by prodution " + reduce.production().productionNumber() + " (" + reduce.productionType().toString()
            + ") with parse nodes " + parseForestListToString(parseNodes) + ", using existing stack: "
            + (activeStackWithGotoState != null ? activeStackWithGotoState.stackNumber : "no"));
    }

    @Override public void reducerElkhound(StackNode stack, IReduce reduce, ParseForest[] parseNodes) {
        log("Reduce (Elkhound) by prodution " + reduce.production().productionNumber() + " ("
            + reduce.productionType().toString() + ") with parse nodes " + parseForestListToString(parseNodes));
    }

    @Override public void directLinkFound(StackLink<StackNode, ParseForest> directLink) {
        log("Direct link " + (directLink != null ? directLink.linkNumber : "not") + " found");
    }

    @Override public void accept(StackNode acceptingStack) {
        log("Accept stack " + acceptingStack.stackNumber);
    }

    @Override public void createParseNode(ParseForest parseNode, IProduction production) {
        log("Create parse node " + parseNode.nodeNumber + " for production " + production.productionNumber());
    }

    @Override public void createDerivation(int nodeNumber, IProduction production, ParseForest[] parseNodes) {
        log("Create derivation with parse nodes " + parseForestListToString(parseNodes));
    }

    @Override public void createCharacterNode(ParseForest characterNode, int character) {
        log("Create character node " + characterNode.nodeNumber + " for character '"
            + ICharacters.intToString(character) + "'");
    }

    @Override public void addDerivation(ParseForest parseNode) {
        log("Add derivation to parse node '" + parseNode.nodeNumber);
    }

    @Override public void shifter(ParseForest termNode, Queue<ForShifterElement<StackNode, ParseForest>> forShifter) {
        log("Shifter for elements " + forShifterQueueToString(forShifter) + " with character node "
            + termNode.nodeNumber);
    }

    @Override public void remark(String remark) {
        log(remark);
    }

    @Override public void success(ParseSuccess<StackNode, ParseForest, ?> success) {
        log("Parsing succeeded. Result: " + success.parseResult.toString());
    }

    @Override public void failure(ParseFailure<StackNode, ParseForest, ?> failure) {
        log("Parsing failed");
    }

    private void log(String message) {
        Logger.getGlobal().info(message);
    }

}
