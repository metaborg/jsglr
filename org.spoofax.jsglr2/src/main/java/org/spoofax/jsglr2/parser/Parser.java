package org.spoofax.jsglr2.parser;

import org.metaborg.parsetable.IParseTable;
import org.metaborg.parsetable.actions.IAction;
import org.metaborg.parsetable.actions.IReduce;
import org.metaborg.parsetable.actions.IShift;
import org.metaborg.parsetable.states.IState;
import org.spoofax.jsglr2.parseforest.IDerivation;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parseforest.ParseForestManager;
import org.spoofax.jsglr2.parser.failure.IParseFailureHandler;
import org.spoofax.jsglr2.parser.observing.ParserObserving;
import org.spoofax.jsglr2.parser.result.ParseFailure;
import org.spoofax.jsglr2.parser.result.ParseFailureType;
import org.spoofax.jsglr2.parser.result.ParseResult;
import org.spoofax.jsglr2.parser.result.ParseSuccess;
import org.spoofax.jsglr2.reducing.ReduceManagerFactory;
import org.spoofax.jsglr2.stack.AbstractStackManager;
import org.spoofax.jsglr2.stack.IStackNode;

public class Parser
//@formatter:off
   <ParseForest   extends IParseForest,
    ParseNode     extends ParseForest,
    Derivation    extends IDerivation<ParseForest>,
    StackNode     extends IStackNode,
    ParseState    extends AbstractParseState<ParseForest, StackNode>,
    StackManager  extends AbstractStackManager<ParseForest, StackNode, ParseState>,
    ReduceManager extends org.spoofax.jsglr2.reducing.ReduceManager<ParseForest, ParseNode, Derivation, StackNode, ParseState>>
//@formatter:on
    implements IObservableParser<ParseForest, StackNode, ParseState> {

    protected final ParseStateFactory<ParseForest, StackNode, ParseState> parseStateFactory;
    protected final IParseTable parseTable;
    protected final StackManager stackManager;
    protected final ParseForestManager<ParseForest, ParseNode, Derivation, StackNode, ParseState> parseForestManager;
    public final ReduceManager reduceManager;
    protected final IParseFailureHandler<ParseForest, StackNode, ParseState> failureHandler;
    protected final ParserObserving<ParseForest, StackNode, ParseState> observing;

    public Parser(ParseStateFactory<ParseForest, StackNode, ParseState> parseStateFactory, IParseTable parseTable,
        StackManager stackManager,
        ParseForestManager<ParseForest, ParseNode, Derivation, StackNode, ParseState> parseForestManager,
        ReduceManagerFactory<ParseForest, ParseNode, Derivation, StackNode, ParseState, StackManager, ReduceManager> reduceManagerFactory,
        IParseFailureHandler<ParseForest, StackNode, ParseState> failureHandler) {
        this.parseStateFactory = parseStateFactory;
        this.parseTable = parseTable;
        this.stackManager = stackManager;
        this.parseForestManager = parseForestManager;
        this.reduceManager = reduceManagerFactory.get(parseTable, this.stackManager, parseForestManager);
        this.failureHandler = failureHandler;
        this.observing = new ParserObserving<>();
    }

    @Override public ParseResult<ParseForest> parse(String inputString, String filename, String startSymbol) {
        ParseState parseState = getParseState(inputString, filename);

        observing.notify(observer -> observer.parseStart(parseState));

        StackNode initialStackNode = stackManager.createInitialStackNode(observing, parseTable.getStartState());

        parseState.activeStacks.add(initialStackNode);

        boolean recover = false;

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
        return parseStateFactory.get(inputString, filename, observing);
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
        while(parseState.hasNext() && !parseState.activeStacks.isEmpty()) {
            parseCharacter(parseState);

            if(!parseState.activeStacks.isEmpty())
                parseState.next();
        }
    }

    protected void parseCharacter(ParseState parseState) {
        observing.notify(observer -> observer.parseRound(parseState, parseState.activeStacks));

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
        observing.notify(observer -> observer.actor(stack, parseState, stack.state().getApplicableActions(parseState)));

        for(IAction action : stack.state().getApplicableActions(parseState))
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
                stackManager.createStackLink(observing, parseState, activeStackForState, forShifterElement.stack,
                    characterNode);
            } else {
                StackNode newStack = stackManager.createStackNode(observing, forShifterElement.state);

                stackManager.createStackLink(observing, parseState, newStack, forShifterElement.stack, characterNode);

                parseState.activeStacks.add(newStack);
            }
        }

        parseState.forShifter.clear();
    }

    protected ParseForest getNodeToShift(ParseState parseState) {
        return parseForestManager.createCharacterNode(observing, parseState);
    }

    private void addForShifter(ParseState parseState, StackNode stack, IState shiftState) {
        ForShifterElement<StackNode> forShifterElement = new ForShifterElement<>(stack, shiftState);

        observing.notify(observer -> observer.addForShifter(forShifterElement));

        parseState.forShifter.add(forShifterElement);
    }

    @Override public ParserObserving<ParseForest, StackNode, ParseState> observing() {
        return observing;
    }

}
