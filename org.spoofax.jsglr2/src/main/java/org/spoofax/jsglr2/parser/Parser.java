package org.spoofax.jsglr2.parser;

import java.util.ArrayList;
import java.util.List;

import org.metaborg.parsetable.IParseTable;
import org.metaborg.parsetable.actions.IAction;
import org.metaborg.parsetable.actions.IReduce;
import org.metaborg.parsetable.actions.IShift;
import org.metaborg.parsetable.states.IState;
import org.spoofax.jsglr2.JSGLR2Request;
import org.spoofax.jsglr2.inputstack.IInputStack;
import org.spoofax.jsglr2.inputstack.InputStackFactory;
import org.spoofax.jsglr2.messages.Message;
import org.spoofax.jsglr2.parseforest.*;
import org.spoofax.jsglr2.parser.failure.IParseFailureHandler;
import org.spoofax.jsglr2.parser.failure.ParseFailureHandlerFactory;
import org.spoofax.jsglr2.parser.observing.ParserObserving;
import org.spoofax.jsglr2.parser.result.ParseFailure;
import org.spoofax.jsglr2.parser.result.ParseFailureCause;
import org.spoofax.jsglr2.parser.result.ParseResult;
import org.spoofax.jsglr2.parser.result.ParseSuccess;
import org.spoofax.jsglr2.reducing.ReduceManagerFactory;
import org.spoofax.jsglr2.stack.AbstractStackManager;
import org.spoofax.jsglr2.stack.IStackNode;
import org.spoofax.jsglr2.stack.StackManagerFactory;

public class Parser
//@formatter:off
   <ParseForest   extends IParseForest,
    Derivation    extends IDerivation<ParseForest>,
    ParseNode     extends IParseNode<ParseForest, Derivation>,
    StackNode     extends IStackNode,
    InputStack    extends IInputStack,
    ParseState    extends AbstractParseState<InputStack, StackNode>,
    StackManager  extends AbstractStackManager<ParseForest, Derivation, ParseNode, StackNode, ParseState>,
    ReduceManager extends org.spoofax.jsglr2.reducing.ReduceManager<ParseForest, Derivation, ParseNode, StackNode, InputStack, ParseState>>
//@formatter:on
    implements IObservableParser<ParseForest, Derivation, ParseNode, StackNode, ParseState> {

    protected final ParserObserving<ParseForest, Derivation, ParseNode, StackNode, ParseState> observing;
    protected final InputStackFactory<InputStack> inputStackFactory;
    protected final ParseStateFactory<ParseForest, Derivation, ParseNode, InputStack, StackNode, ParseState> parseStateFactory;
    protected final IParseTable parseTable;
    protected final StackManager stackManager;
    protected final ParseForestManager<ParseForest, Derivation, ParseNode, StackNode, ParseState> parseForestManager;
    public final ReduceManager reduceManager;
    protected final IParseFailureHandler<ParseForest, StackNode, ParseState> failureHandler;
    protected final IParseReporter<ParseForest, Derivation, ParseNode, StackNode, InputStack, ParseState> reporter;

    public Parser(InputStackFactory<InputStack> inputStackFactory,
        ParseStateFactory<ParseForest, Derivation, ParseNode, InputStack, StackNode, ParseState> parseStateFactory,
        IParseTable parseTable,
        StackManagerFactory<ParseForest, Derivation, ParseNode, StackNode, ParseState, StackManager> stackManagerFactory,
        ParseForestManagerFactory<ParseForest, Derivation, ParseNode, StackNode, ParseState> parseForestManagerFactory,
        Disambiguator<ParseForest, Derivation, ParseNode, StackNode, ParseState> disambiguator,
        ReduceManagerFactory<ParseForest, Derivation, ParseNode, StackNode, InputStack, ParseState, StackManager, ReduceManager> reduceManagerFactory,
        ParseFailureHandlerFactory<ParseForest, Derivation, ParseNode, StackNode, ParseState> failureHandlerFactory,
        ParseReporterFactory<ParseForest, Derivation, ParseNode, StackNode, InputStack, ParseState> reporterFactory) {
        this.inputStackFactory = inputStackFactory;
        this.observing = new ParserObserving<>();
        this.parseStateFactory = parseStateFactory;
        this.parseTable = parseTable;
        this.stackManager = stackManagerFactory.get(observing);
        this.parseForestManager = parseForestManagerFactory.get(observing, disambiguator);
        this.reduceManager = reduceManagerFactory.get(parseTable, stackManager, parseForestManager);
        this.failureHandler = failureHandlerFactory.get(observing);
        this.reporter = reporterFactory.get(parseForestManager);
    }

    @Override public ParseResult<ParseForest> parse(JSGLR2Request request, String previousInput,
        ParseForest previousResult) {
        ParseState parseState = getParseState(request, previousInput, previousResult);

        observing.notify(observer -> observer.parseStart(parseState));

        StackNode initialStackNode = stackManager.createInitialStackNode(parseTable.getStartState());

        parseState.activeStacks.add(initialStackNode);

        boolean recover;

        try {
            do {
                parseLoop(parseState);

                if(parseState.acceptingStack == null)
                    recover = failureHandler.onFailure(parseState);
                else
                    recover = false;
            } while(recover);

            if(parseState.acceptingStack != null) {
                ParseForest parseForest =
                    stackManager.findDirectLink(parseState.acceptingStack, initialStackNode).parseForest;

                ParseForest parseForestWithStartSymbol = request.startSymbol != null
                    ? parseForestManager.filterStartSymbol(parseForest, request.startSymbol, parseState) : parseForest;

                if(parseForest != null && parseForestWithStartSymbol == null)
                    return failure(parseState, new ParseFailureCause(ParseFailureCause.Type.InvalidStartSymbol));
                else
                    return complete(parseState, parseForestWithStartSymbol);
            } else
                return failure(parseState, failureHandler.failureCause(parseState));
        } catch(ParseException e) {
            return failure(parseState, e.cause);
        }
    }

    @Override public void visit(ParseSuccess<?> success, ParseNodeVisitor<?, ?, ?> visitor) {
        parseForestManager.visit(success.parseState.request, (ParseForest) success.parseResult,
            (ParseNodeVisitor<ParseForest, Derivation, ParseNode>) visitor);
    }

    protected ParseState getParseState(JSGLR2Request request, String previousInput, ParseForest previousResult) {
        return parseStateFactory.get(request, inputStackFactory.get(request.input), observing);
    }

    protected ParseResult<ParseForest> complete(ParseState parseState, ParseForest parseForest) {
        List<Message> messages = new ArrayList<>();
        CycleDetector<ParseForest, Derivation, ParseNode> cycleDetector = new CycleDetector<>(messages);

        parseForestManager.visit(parseState.request, parseForest, cycleDetector);

        if(cycleDetector.cycleDetected()) {
            return failure(new ParseFailure<>(parseState, cycleDetector.failureCause));
        } else {
            reporter.report(parseState, parseForest, messages);

            // Generate errors for non-assoc or non-nested productions that are used associatively
            parseForestManager.visit(parseState.request, parseForest, new NonAssocDetector<>(messages));

            if(parseState.request.reportAmbiguities) {
                // Generate warnings for ambiguous parse nodes
                parseForestManager.visit(parseState.request, parseForest,
                    new AmbiguityDetector<>(parseState.inputStack.inputString(), messages));
            }

            ParseSuccess<ParseForest> success = new ParseSuccess<>(parseState, parseForest, messages);

            observing.notify(observer -> observer.success(success));

            return success;
        }
    }

    protected ParseFailure<ParseForest> failure(ParseState parseState, ParseFailureCause failureCause) {
        return failure(new ParseFailure<>(parseState, failureCause));
    }

    protected ParseFailure<ParseForest> failure(ParseFailure<ParseForest> failure) {
        observing.notify(observer -> observer.failure(failure));

        return failure;
    }

    protected void parseLoop(ParseState parseState) throws ParseException {
        while(parseState.inputStack.hasNext() && !parseState.activeStacks.isEmpty()) {
            parseCharacter(parseState);
            parseState.inputStack.consumed();

            if(!parseState.activeStacks.isEmpty())
                parseState.inputStack.next();
        }
    }

    protected void parseCharacter(ParseState parseState) throws ParseException {
        parseState.nextParseRound(observing);

        parseState.activeStacks.addAllTo(parseState.forActorStacks);

        observing.notify(observer -> observer.forActorStacks(parseState.forActorStacks));

        processForActorStacks(parseState);

        shifter(parseState);
    }

    protected void processForActorStacks(ParseState parseState) {
        while(parseState.forActorStacks.nonEmpty()) {
            StackNode stack = parseState.forActorStacks.remove();

            observing.notify(observer -> observer.handleForActorStack(stack, parseState.forActorStacks));

            if(!stack.allLinksRejected())
                actor(stack, parseState);
            else
                observing.notify(observer -> observer.skipRejectedStack(stack));
        }
    }

    protected void actor(StackNode stack, ParseState parseState) {
        observing.notify(observer -> observer.actor(stack, parseState,
            stack.state().getApplicableActions(parseState.inputStack, parseState.mode)));

        for(IAction action : stack.state().getApplicableActions(parseState.inputStack, parseState.mode))
            actor(stack, parseState, action);
    }

    protected void actor(StackNode stack, ParseState parseState, IAction action) {
        switch(action.actionType()) {
            case SHIFT:
                IShift shiftAction = (IShift) action;
                IState shiftState = parseTable.getState(shiftAction.shiftStateId());

                addForShifter(parseState, stack, shiftState);

                break;
            case REDUCE:
            case REDUCE_LOOKAHEAD: // Lookahead is checked while retrieving applicable actions from the state
                IReduce reduceAction = (IReduce) action;

                reduceManager.doReductions(observing, parseState, stack, reduceAction);

                break;
            case ACCEPT:
                parseState.acceptingStack = stack;

                observing.notify(observer -> observer.accept(stack));

                break;
        }
    }

    protected void shifter(ParseState parseState) {
        parseState.activeStacks.clear();

        ParseForest characterNode = getNodeToShift(parseState);

        observing.notify(observer -> observer.shifter(characterNode, parseState.forShifter));

        for(ForShifterElement<StackNode> forShifterElement : parseState.forShifter) {
            StackNode gotoStack = parseState.activeStacks.findWithState(forShifterElement.state);

            if(gotoStack != null) {
                stackManager.createStackLink(parseState, gotoStack, forShifterElement.stack, characterNode);
            } else {
                gotoStack = stackManager.createStackNode(forShifterElement.state);

                stackManager.createStackLink(parseState, gotoStack, forShifterElement.stack, characterNode);

                parseState.activeStacks.add(gotoStack);
            }

            StackNode finalGotoStack = gotoStack;
            observing.notify(observer -> observer.shift(parseState, forShifterElement.stack, finalGotoStack));
        }

        parseState.forShifter.clear();
    }

    protected ParseForest getNodeToShift(ParseState parseState) {
        return parseForestManager.createCharacterNode(parseState);
    }

    protected void addForShifter(ParseState parseState, StackNode stack, IState shiftState) {
        ForShifterElement<StackNode> forShifterElement = new ForShifterElement<>(stack, shiftState);

        observing.notify(observer -> observer.addForShifter(forShifterElement));

        parseState.forShifter.add(forShifterElement);
    }

    @Override public ParserObserving<ParseForest, Derivation, ParseNode, StackNode, ParseState> observing() {
        return observing;
    }

}
