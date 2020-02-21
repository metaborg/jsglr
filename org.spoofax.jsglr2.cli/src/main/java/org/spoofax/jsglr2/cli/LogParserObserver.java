package org.spoofax.jsglr2.cli;

import java.util.Queue;
import java.util.function.Consumer;

import org.metaborg.parsetable.actions.IAction;
import org.metaborg.parsetable.actions.IReduce;
import org.metaborg.parsetable.characterclasses.CharacterClassFactory;
import org.metaborg.parsetable.productions.IProduction;
import org.spoofax.jsglr2.elkhound.AbstractElkhoundStackNode;
import org.spoofax.jsglr2.parseforest.IDerivation;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parseforest.IParseNode;
import org.spoofax.jsglr2.parser.AbstractParseState;
import org.spoofax.jsglr2.parser.ForShifterElement;
import org.spoofax.jsglr2.parser.observing.RegisteringParserObserver;
import org.spoofax.jsglr2.parser.result.ParseFailure;
import org.spoofax.jsglr2.parser.result.ParseSuccess;
import org.spoofax.jsglr2.stack.IStackNode;
import org.spoofax.jsglr2.stack.StackLink;
import org.spoofax.jsglr2.stack.collections.IForActorStacks;

public class LogParserObserver
//@formatter:off
   <ParseForest extends IParseForest,
    Derivation  extends IDerivation<ParseForest>,
    ParseNode   extends IParseNode<ParseForest, Derivation>,
    StackNode   extends IStackNode,
    ParseState  extends AbstractParseState<?, StackNode>>
//@formatter:on
    extends RegisteringParserObserver<ParseForest, Derivation, ParseNode, StackNode, ParseState> {

    final private Consumer<String> logger;

    LogParserObserver(Consumer<String> logger) {
        this.logger = logger;
    }

    @Override public void parseStart(ParseState parseState) {
        super.parseStart(parseState);
        log("\nStarting parse for input '" + parseState.inputStack.inputString() + "'");
    }

    @Override public void parseRound(ParseState parseState, Iterable<StackNode> activeStacks) {
        log("\nParse character '" + CharacterClassFactory.intToString(parseState.inputStack.getChar())
            + "' (active stacks: " + stackQueueToString(activeStacks) + ")\n");
    }

    @Override public void createStackNode(StackNode stack) {
        super.createStackNode(stack);

        log("    Create stack " + stackNodeString(stack));
    }

    @Override public void createStackLink(StackLink<ParseForest, StackNode> link) {
        super.createStackLink(link);

        log("    Create link " + stackNodeString(link.to) + " <-- " + id(link) + " --- " + stackNodeString(link.from));
    }

    @Override public void resetDeterministicDepth(AbstractElkhoundStackNode<ParseForest> stack) {
        log("    Reset deterministic depth for stack " + stackNodeString((StackNode) stack));
    }

    @Override public void rejectStackLink(StackLink<ParseForest, StackNode> link) {
        log("Reject link " + id(link));
    }

    @Override public void forActorStacks(IForActorStacks<StackNode> forActorStacks) {
        log("For actor stacks: " + stackQueueToString(forActorStacks));
    }

    @Override public void actor(StackNode stack, ParseState parseState, Iterable<IAction> applicableActions) {
        log("  Actor for stack " + stackNodeString(stack) + " (applicable actions: "
            + applicableActionsToString(applicableActions) + ")");
    }

    @Override public void skipRejectedStack(StackNode stack) {
        log("    Skipping stack " + stackNodeString(stack) + " since all links to it are rejected");
    }

    @Override public void addForShifter(ForShifterElement<StackNode> forShifterElement) {
        log("    Add for shifter " + forShifterElementToString(forShifterElement));
    }

    @Override public void doLimitedReductions(ParseState parseState, StackNode stack, IReduce reduce,
        StackLink<ParseForest, StackNode> link) {
    }

    @Override public void reducer(ParseState parseState, StackNode stack, IReduce reduce, ParseForest[] parseNodes,
        StackNode targetStack) {
        log("    Reduce by production " + reduce.production().id() + " (" + reduce.productionType().toString()
            + ") with parse nodes " + parseForestsToString(parseNodes) + ", target stack: "
            + stackNodeString(targetStack));
    }

    @Override public void reducerElkhound(StackNode stack, IReduce reduce, ParseForest[] parseNodes) {
        log("    Reduce (Elkhound) by production " + reduce.production().id() + " ("
            + reduce.productionType().toString() + ") with parse nodes " + parseForestsToString(parseNodes));
    }

    @Override public void directLinkFound(ParseState parseState, StackLink<ParseForest, StackNode> directLink) {
        log("    Direct link " + (directLink != null ? id(directLink) : "not") + " found");
    }

    @Override public void accept(StackNode acceptingStack) {
        log("    Accept stack " + stackNodeString(acceptingStack));
    }

    @Override public void createParseNode(ParseNode parseNode, IProduction production) {
        super.createParseNode(parseNode, production);

        log("    Create parse node " + id((ParseForest) parseNode) + " for production "
            + (production == null ? null : production.id()));
    }

    @Override public void createDerivation(Derivation derivation, IProduction production, ParseForest[] parseNodes) {
        super.createDerivation(derivation, production, parseNodes);

        log("    Create derivation with parse nodes " + parseForestsToString(parseNodes));
    }

    @Override public void createCharacterNode(ParseForest characterNode, int character) {
        super.createCharacterNode(characterNode, character);

        log("    Create character node " + id(characterNode) + " for character '"
            + CharacterClassFactory.intToString(character) + "'");
    }

    @Override public void addDerivation(ParseNode parseNode, Derivation derivation) {
        log("    Add derivation " + id(derivation) + " to parse node " + id((ParseForest) parseNode));
    }

    @Override public void shifter(ParseForest termNode, Queue<ForShifterElement<StackNode>> forShifter) {
        log("    Shifter for elements " + forShifterQueueToString(forShifter) + " with character node " + id(termNode));
    }

    @Override public void remark(String remark) {
        log(remark);
    }

    @Override public void success(ParseSuccess<ParseForest> success) {
        log("Parsing succeeded. Result: " + success.parseResult.toString());
    }

    @Override public void failure(ParseFailure<ParseForest> failure) {
        log("Parsing failed");
    }

    private void log(String message) {
        logger.accept(message);
    }

}
