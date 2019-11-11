package org.spoofax.jsglr2.parser;

import org.metaborg.parsetable.IParseTable;
import org.metaborg.parsetable.actions.IAction;
import org.metaborg.parsetable.actions.IReduce;
import org.metaborg.parsetable.actions.IShift;
import org.metaborg.parsetable.states.IState;
import org.spoofax.jsglr2.inputstack.IInputStack;
import org.spoofax.jsglr2.inputstack.InputStackFactory;
import org.spoofax.jsglr2.parseforest.*;
import org.spoofax.jsglr2.parser.failure.IParseFailureHandler;
import org.spoofax.jsglr2.parser.failure.ParseFailureHandlerFactory;
import org.spoofax.jsglr2.parser.observing.ParserObserving;
import org.spoofax.jsglr2.parser.result.ParseFailure;
import org.spoofax.jsglr2.parser.result.ParseFailureType;
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
    ReduceManager extends org.spoofax.jsglr2.reducing.ReduceManager<ParseForest, Derivation, ParseNode, StackNode, ParseState>>
//@formatter:on
    implements IObservableParser<ParseForest, Derivation, ParseNode, StackNode, ParseState> {

    protected final ParserObserving<ParseForest, Derivation, ParseNode, StackNode, ParseState> observing;
    protected final InputStackFactory<InputStack> inputStackFactory;
    protected final ParseStateFactory<ParseForest, Derivation, ParseNode, StackNode, InputStack, ParseState> parseStateFactory;
    protected final IParseTable parseTable;
    protected final StackManager stackManager;
    protected final ParseForestManager<ParseForest, Derivation, ParseNode, StackNode, ParseState> parseForestManager;
    public final ReduceManager reduceManager;
    protected final IParseFailureHandler<ParseForest, StackNode, ParseState> failureHandler;

    public Parser(InputStackFactory<InputStack> inputStackFactory,
        ParseStateFactory<ParseForest, Derivation, ParseNode, StackNode, InputStack, ParseState> parseStateFactory,
        IParseTable parseTable,
        StackManagerFactory<ParseForest, Derivation, ParseNode, StackNode, ParseState, StackManager> stackManagerFactory,
        ParseForestManagerFactory<ParseForest, Derivation, ParseNode, StackNode, ParseState> parseForestManagerFactory,
        ReduceManagerFactory<ParseForest, Derivation, ParseNode, StackNode, ParseState, StackManager, ReduceManager> reduceManagerFactory,
        ParseFailureHandlerFactory<ParseForest, Derivation, ParseNode, StackNode, ParseState> failureHandlerFactory) {
        this.inputStackFactory = inputStackFactory;
        this.observing = new ParserObserving<>();
        this.parseStateFactory = parseStateFactory;
        this.parseTable = parseTable;
        this.stackManager = stackManagerFactory.get(observing);
        this.parseForestManager = parseForestManagerFactory.get(observing);
        this.reduceManager = reduceManagerFactory.get(parseTable, stackManager, parseForestManager);
        this.failureHandler = failureHandlerFactory.get(observing);
    }

    @Override public ParseResult<ParseForest> parse(String inputString, String filename, String startSymbol) {
        ParseState parseState = getParseState(inputString, filename);

        observing.notify(observer -> observer.parseStart(parseState));

        StackNode initialStackNode = stackManager.createInitialStackNode(parseTable.getStartState());

        parseState.activeStacks.add(initialStackNode);

        boolean recover;

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

            ParseForest parseForestWithStartSymbol = startSymbol != null
                ? parseForestManager.filterStartSymbol(parseForest, startSymbol, parseState) : parseForest;

            if(parseForest != null && parseForestWithStartSymbol == null)
                return failure(parseState, ParseFailureType.InvalidStartSymbol);
            else
                return success(parseState, parseForestWithStartSymbol);
        } else
            return failure(parseState, failureHandler.failureType(parseState));
    }

    protected ParseState getParseState(String inputString, String filename) {
        return parseStateFactory.get(inputStackFactory.get(inputString, filename), observing);
    }

    protected ParseSuccess<ParseForest> success(ParseState parseState, ParseForest parseForest) {
        ParseSuccess<ParseForest> success = new ParseSuccess<>(parseState, parseForest);

        observing.notify(observer -> observer.success(success));

        return success;
    }

    protected ParseFailure<ParseForest> failure(ParseState parseState, ParseFailureType failureType) {
        ParseFailure<ParseForest> failure = new ParseFailure<>(parseState, failureType);

        observing.notify(observer -> observer.failure(failure));

        return failure;
    }

    protected void parseLoop(ParseState parseState) {
        while(parseState.inputStack.hasNext() && !parseState.activeStacks.isEmpty()) {
            parseCharacter(parseState);

            if(!parseState.activeStacks.isEmpty())
                parseState.inputStack.next();
        }
    }

    protected void parseCharacter(ParseState parseState) {
        observing.notify(observer -> observer.parseRound(parseState, parseState.activeStacks, observing));

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
        observing.notify(
            observer -> observer.actor(stack, parseState, stack.state().getApplicableActions(parseState.inputStack)));

        for(IAction action : stack.state().getApplicableActions(parseState.inputStack))
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
            StackNode activeStackForState = parseState.activeStacks.findWithState(forShifterElement.state);

            if(activeStackForState != null) {
                stackManager.createStackLink(parseState, activeStackForState, forShifterElement.stack, characterNode);
            } else {
                StackNode newStack = stackManager.createStackNode(forShifterElement.state);

                stackManager.createStackLink(parseState, newStack, forShifterElement.stack, characterNode);

                parseState.activeStacks.add(newStack);
            }
        }

        parseState.forShifter.clear();
    }

    protected ParseForest getNodeToShift(ParseState parseState) {
        return parseForestManager.createCharacterNode(parseState);
    }

    private void addForShifter(ParseState parseState, StackNode stack, IState shiftState) {
        ForShifterElement<StackNode> forShifterElement = new ForShifterElement<>(stack, shiftState);

        observing.notify(observer -> observer.addForShifter(forShifterElement));

        parseState.forShifter.add(forShifterElement);
    }

    @Override public ParserObserving<ParseForest, Derivation, ParseNode, StackNode, ParseState> observing() {
        return observing;
    }

}
