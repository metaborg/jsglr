package org.spoofax.jsglr2.parser.observing;

import java.util.HashMap;
import java.util.Queue;
import java.util.function.Consumer;

import org.metaborg.parsetable.actions.IAction;
import org.metaborg.parsetable.actions.IReduce;
import org.metaborg.parsetable.characterclasses.CharacterClassFactory;
import org.metaborg.parsetable.productions.IProduction;
import org.spoofax.jsglr2.JSGLR2Logging;
import org.spoofax.jsglr2.JSGLR2Request;
import org.spoofax.jsglr2.elkhound.AbstractElkhoundStackNode;
import org.spoofax.jsglr2.incremental.parseforest.IncrementalParseForest;
import org.spoofax.jsglr2.inputstack.incremental.IIncrementalInputStack;
import org.spoofax.jsglr2.parseforest.IDerivation;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parseforest.IParseNode;
import org.spoofax.jsglr2.parser.AbstractParseState;
import org.spoofax.jsglr2.parser.ForShifterElement;
import org.spoofax.jsglr2.parser.result.ParseFailure;
import org.spoofax.jsglr2.parser.result.ParseSuccess;
import org.spoofax.jsglr2.recovery.IRecoveryParseState;
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
    final private JSGLR2Logging[] scopes;

    final private HashMap<JSGLR2Request, Long> startTimes;

    public LogParserObserver(Consumer<String> logger, JSGLR2Logging... scopes) {
        this.logger = logger;
        this.scopes = scopes;
        this.startTimes = new HashMap<>();
    }

    public LogParserObserver(Consumer<String> logger) {
        this(logger, JSGLR2Logging.All);
    }

    public LogParserObserver(JSGLR2Logging... scopes) {
        this(System.out::println, scopes);
    }

    @Override public void parseStart(ParseState parseState) {
        super.parseStart(parseState);

        log("Starting parse" + (parseState.request.isCompletion()
            ? (" for completion at offset " + parseState.request.completionCursorOffset.get()) : ""),
            JSGLR2Logging.Parsing, JSGLR2Logging.Minimal);

        startTimes.put(parseState.request, System.currentTimeMillis());
    }

    @Override public void parseRound(ParseState parseState, Iterable<StackNode> activeStacks) {
        if(parseState.inputStack instanceof IIncrementalInputStack) {
            IncrementalParseForest node = ((IIncrementalInputStack) parseState.inputStack).getNode();
            @SuppressWarnings("unchecked") int id = id((ParseForest) node);
            String activeStacksText = " (active stacks: " + stackQueueToString(activeStacks) + ")";
            String message = node.isTerminal()
                ? "Process character node " + id + " " + node.toString() + activeStacksText
                : "Process parse node " + id + activeStacksText + ":\n  " + node.toString().replaceAll("\n", "\n  ");
            log(message, JSGLR2Logging.Parsing);
        } else
            log("Parse character '" + characterToString(parseState.inputStack.getChar()) + "' (active stacks: "
                + stackQueueToString(activeStacks) + ")", JSGLR2Logging.Parsing);
    }

    @Override public void createStackNode(StackNode stack) {
        super.createStackNode(stack);

        log("    Create stack " + stackNodeString(stack), JSGLR2Logging.Parsing);
    }

    @Override public void createStackLink(StackLink<ParseForest, StackNode> link) {
        super.createStackLink(link);

        log("    Create link " + stackNodeString(link.to) + " <-- " + id(link) + " --- " + stackNodeString(link.from),
            JSGLR2Logging.Parsing);
    }

    @Override public void resetDeterministicDepth(AbstractElkhoundStackNode<ParseForest> stack) {
        log("    Reset deterministic depth for stack " + stackNodeString((StackNode) stack), JSGLR2Logging.Parsing);
    }

    @Override public void rejectStackLink(StackLink<ParseForest, StackNode> link) {
        log("Reject link " + id(link), JSGLR2Logging.Parsing);
    }

    @Override public void forActorStacks(IForActorStacks<StackNode> forActorStacks) {
        log("For actor stacks: " + stackQueueToString(forActorStacks), JSGLR2Logging.Parsing);
    }

    @Override public void actor(StackNode stack, ParseState parseState, Iterable<IAction> applicableActions) {
        log("  Actor for stack " + stackNodeString(stack) + " (applicable actions: "
            + applicableActionsToString(applicableActions) + ")", JSGLR2Logging.Parsing);
    }

    @Override public void skipRejectedStack(StackNode stack) {
        log("    Skipping stack " + stackNodeString(stack) + " since all links to it are rejected",
            JSGLR2Logging.Parsing);
    }

    @Override public void breakDown(IIncrementalInputStack inputStack, BreakdownReason reason) {
        @SuppressWarnings("unchecked") ParseForest forest = (ParseForest) inputStack.getNode();
        log("  Breaking down parse node " + id(forest) + ", reason: " + reason.message, JSGLR2Logging.Parsing);
        @SuppressWarnings("unchecked") ParseNode node = (ParseNode) forest;
        for(Derivation derivation : node.getDerivations()) {
            for(ParseForest parseForest : derivation.parseForests()) {
                id(parseForest); // Register the children of the broken-down parse node
            }
        }
    }

    @Override public void addForShifter(ForShifterElement<StackNode> forShifterElement) {
        log("    Add for shifter " + forShifterElementToString(forShifterElement), JSGLR2Logging.Parsing);
    }

    @Override public void doLimitedReductions(ParseState parseState, StackNode stack, IReduce reduce,
        StackLink<ParseForest, StackNode> link) {
    }

    @Override public void reducer(ParseState parseState, StackNode activeStack, StackNode originStack, IReduce reduce,
        ParseForest[] parseNodes, StackNode gotoStack) {
        log("    Reduce by production [" + reduce.production().descriptor() + "] (" + reduce.productionType().toString()
            + ") with parse nodes " + parseForestsToString(parseNodes) + ", target stack: "
            + stackNodeString(gotoStack), JSGLR2Logging.Parsing,
            conditional(JSGLR2Logging.Recovery, reduce.production().isRecovery()));
    }

    @Override public void reducerElkhound(StackNode stack, IReduce reduce, ParseForest[] parseNodes) {
        log("    Reduce (Elkhound) by production [" + reduce.production().descriptor() + "] ("
            + reduce.productionType().toString() + ") with parse nodes " + parseForestsToString(parseNodes),
            JSGLR2Logging.Parsing, conditional(JSGLR2Logging.Recovery, reduce.production().isRecovery()));
    }

    @Override public void directLinkFound(ParseState parseState, StackLink<ParseForest, StackNode> directLink) {
        log("    Direct link " + (directLink != null ? id(directLink) : "not") + " found", JSGLR2Logging.Parsing);
    }

    @Override public void accept(StackNode acceptingStack) {
        log("    Accept stack " + stackNodeString(acceptingStack), JSGLR2Logging.Parsing);
    }

    @Override public void createParseNode(ParseNode parseNode, IProduction production) {
        super.createParseNode(parseNode, production);

        log("    Create parse node " + id((ParseForest) parseNode) + " for production "
            + (production == null ? null : production.id()), JSGLR2Logging.Parsing);
    }

    @Override public void createDerivation(Derivation derivation, IProduction production, ParseForest[] parseNodes) {
        super.createDerivation(derivation, production, parseNodes);

        log("    Create derivation with parse nodes " + parseForestsToString(parseNodes), JSGLR2Logging.Parsing);
    }

    @Override public void createCharacterNode(ParseForest characterNode, int character) {
        super.createCharacterNode(characterNode, character);

        log("    Create character node " + id(characterNode) + " for character '" + characterToString(character) + "'",
            JSGLR2Logging.Parsing);
    }

    @Override public void addDerivation(ParseNode parseNode, Derivation derivation) {
        log("    Add derivation " + id(derivation) + " to parse node " + id((ParseForest) parseNode),
            JSGLR2Logging.Parsing);
    }

    @Override public void shifter(ParseForest termNode, Queue<ForShifterElement<StackNode>> forShifter) {
        log("    Shifter for elements " + forShifterQueueToString(forShifter) + " with character node " + id(termNode),
            JSGLR2Logging.Parsing);
    }

    @Override public void startRecovery(ParseState parseState) {
        log("    Recovery started at offset " + parseState.inputStack.offset(), JSGLR2Logging.Recovery);
    }

    @Override public void recoveryIteration(ParseState parseState) {
        log("    Recovery iteration " + ((IRecoveryParseState) parseState).recoveryJob().iteration,
            JSGLR2Logging.Recovery);
    }

    @Override public void endRecovery(ParseState parseState) {
        log("    Recovery ended at offset " + parseState.inputStack.offset(), JSGLR2Logging.Recovery);
    }

    @Override public void remark(String remark) {
        log(remark, JSGLR2Logging.All);
    }

    @Override public void success(ParseSuccess<ParseForest> success) {
        log("Parsing succeeded " + timing(success.parseState.request), JSGLR2Logging.Parsing, JSGLR2Logging.Minimal);
    }

    @Override public void failure(ParseFailure<ParseForest> failure) {
        log("Parsing failed " + timing(failure.parseState.request), JSGLR2Logging.Parsing, JSGLR2Logging.Minimal);
    }

    private String timing(JSGLR2Request request) {
        return "(in " + (System.currentTimeMillis() - startTimes.get(request)) + "ms)";
    }

    private String characterToString(int character) {
        return CharacterClassFactory.intToString(character).replace("\n", "\\n").replace("\r", "\\r").replace("\t",
            "\\t");
    }

    private JSGLR2Logging conditional(JSGLR2Logging scope, boolean condition) {
        if(condition)
            return scope;
        else
            return JSGLR2Logging.None;
    }

    private void log(String message, JSGLR2Logging... eventJSGLR2Loggings) {
        for(JSGLR2Logging scope : scopes) {
            if(scope == JSGLR2Logging.All) {
                logger.accept(message);
                return;
            }

            for(JSGLR2Logging eventJSGLR2Logging : eventJSGLR2Loggings) {
                if(scope == eventJSGLR2Logging || eventJSGLR2Logging == JSGLR2Logging.Minimal) {
                    logger.accept(message);
                    return;
                }
            }
        }
    }

}
