package org.spoofax.jsglr2.cli;

import java.util.Queue;
import java.util.function.Consumer;

import org.metaborg.parsetable.characterclasses.CharacterClassFactory;
import org.metaborg.parsetable.productions.IProduction;
import org.metaborg.parsetable.actions.IAction;
import org.metaborg.parsetable.actions.IReduce;
import org.spoofax.jsglr2.elkhound.AbstractElkhoundStackNode;
import org.spoofax.jsglr2.parseforest.IDerivation;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parser.AbstractParse;
import org.spoofax.jsglr2.parser.ForShifterElement;
import org.spoofax.jsglr2.parser.observing.ParserObserver;
import org.spoofax.jsglr2.parser.result.ParseFailure;
import org.spoofax.jsglr2.parser.result.ParseSuccess;
import org.spoofax.jsglr2.stack.IStackNode;
import org.spoofax.jsglr2.stack.StackLink;
import org.spoofax.jsglr2.stack.collections.IForActorStacks;

public class LogParserObserver
//@formatter:off
   <ParseForest extends IParseForest,
    StackNode   extends IStackNode>
//@formatter:on
    extends ParserObserver<ParseForest, StackNode> {

    final private Consumer<String> logger;

    LogParserObserver(Consumer<String> logger) {
        this.logger = logger;
    }

    @Override public void parseStart(AbstractParse<ParseForest, StackNode> parse) {
        super.parseStart(parse);
        log("\nStarting parse for input '" + parse.inputString + "'");
    }

    @Override public void parseCharacter(AbstractParse<ParseForest, StackNode> parse,
        Iterable<StackNode> activeStacks) {
        log("\nParse character '" + CharacterClassFactory.intToString(parse.currentChar) + "' (active stacks: "
            + stackQueueToString(activeStacks) + ")\n");
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

    @Override public void actor(StackNode stack, AbstractParse<ParseForest, StackNode> parse,
        Iterable<IAction> applicableActions) {
        log("  Actor for stack " + stackNodeString(stack) + " (applicable actions: "
            + applicableActionsToString(applicableActions) + ")");
    }

    @Override public void skipRejectedStack(StackNode stack) {
        log("    Skipping stack " + stackNodeString(stack) + " since all links to it are rejected");
    }

    @Override public void addForShifter(ForShifterElement<StackNode> forShifterElement) {
        log("    Add for shifter " + forShifterElementToString(forShifterElement));
    }

    @Override public void doLimitedReductions(AbstractParse<ParseForest, StackNode> parse, StackNode stack,
        IReduce reduce, StackLink<ParseForest, StackNode> link) {
    }

    @Override public void reducer(StackNode stack, IReduce reduce, ParseForest[] parseNodes,
        StackNode activeStackWithGotoState) {
        log("    Reduce by production " + reduce.production().id() + " (" + reduce.productionType().toString()
            + ") with parse nodes " + parseForestsToString(parseNodes) + ", using existing stack: "
            + (activeStackWithGotoState != null ? stackNodeString(activeStackWithGotoState) : "no"));
    }

    @Override public void reducerElkhound(StackNode stack, IReduce reduce, ParseForest[] parseNodes) {
        log("    Reduce (Elkhound) by production " + reduce.production().id() + " ("
            + reduce.productionType().toString() + ") with parse nodes " + parseForestsToString(parseNodes));
    }

    @Override public void directLinkFound(AbstractParse<ParseForest, StackNode> parse,
        StackLink<ParseForest, StackNode> directLink) {
        log("    Direct link " + (directLink != null ? id(directLink) : "not") + " found");
    }

    @Override public void accept(StackNode acceptingStack) {
        log("    Accept stack " + stackNodeString(acceptingStack));
    }

    @Override public void createParseNode(ParseForest parseNode, IProduction production) {
        super.createParseNode(parseNode, production);

        log("    Create parse node " + id(parseNode) + " for production "
            + (production == null ? null : production.id()));
    }

    @Override public void createDerivation(IDerivation<ParseForest> derivation, IProduction production,
        ParseForest[] parseNodes) {
        super.createDerivation(derivation, production, parseNodes);

        log("    Create derivation with parse nodes " + parseForestsToString(parseNodes));
    }

    @Override public void createCharacterNode(ParseForest characterNode, int character) {
        super.createCharacterNode(characterNode, character);

        log("    Create character node " + id(characterNode) + " for character '"
            + CharacterClassFactory.intToString(character) + "'");
    }

    @Override public void addDerivation(ParseForest parseNode, IDerivation<ParseForest> derivation) {
        log("    Add derivation " + id(derivation) + " to parse node " + id(parseNode));
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
