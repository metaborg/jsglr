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

    protected final ParseFactory<ParseForest, StackNode, ParseState> parseFactory;
    protected final IParseTable parseTable;
    protected final StackManager stackManager;
    protected final ParseForestManager<ParseForest, ParseNode, Derivation, StackNode, ParseState> parseForestManager;
    protected final ReduceManager reduceManager;
    protected final IParseFailureHandler<ParseForest, StackNode, ParseState> failureHandler;
    protected final ParserObserving<ParseForest, StackNode, ParseState> observing;

    public Parser(ParseFactory<ParseForest, StackNode, ParseState> parseFactory, IParseTable parseTable,
        StackManager stackManager,
        ParseForestManager<ParseForest, ParseNode, Derivation, StackNode, ParseState> parseForestManager,
        ReduceManagerFactory<ParseForest, ParseNode, Derivation, StackNode, ParseState, StackManager, ReduceManager> reduceManagerFactory,
        IParseFailureHandler<ParseForest, StackNode, ParseState> failureHandler) {
        this.parseFactory = parseFactory;
        this.parseTable = parseTable;
        this.stackManager = stackManager;
        this.parseForestManager = parseForestManager;
        this.reduceManager = reduceManagerFactory.get(parseTable, this.stackManager, parseForestManager);
        this.failureHandler = failureHandler;
        this.observing = new ParserObserving<>();
    }

    @Override public ParseResult<ParseForest> parse(String inputString, String filename, String startSymbol) {
        Parse<ParseForest, StackNode, ParseState> parse = getParse(inputString, filename);

        observing.notify(observer -> observer.parseStart(parse));

        StackNode initialStackNode = stackManager.createInitialStackNode(parse, parseTable.getStartState());

        parse.state.activeStacks.add(initialStackNode);

        parseLoop(parse);

        if(parse.state.acceptingStack != null) {
            ParseForest parseForest =
                stackManager.findDirectLink(parse.state.acceptingStack, initialStackNode).parseForest;

            ParseForest parseForestWithStartSymbol = startSymbol != null
                ? parseForestManager.filterStartSymbol(parseForest, startSymbol, parse) : parseForest;

            if(parseForest != null && parseForestWithStartSymbol == null)
                return failure(parse, ParseFailureType.InvalidStartSymbol);
            else
                return success(parse, parseForestWithStartSymbol);
        } else
            return failure(parse, failureHandler.failureType(parse));
    }

    protected Parse<ParseForest, StackNode, ParseState> getParse(String inputString, String filename) {
        return parseFactory.get(inputString, filename, observing);
    }

    protected ParseSuccess<ParseForest> success(Parse<ParseForest, StackNode, ParseState> parse,
        ParseForest parseForest) {
        ParseSuccess<ParseForest> success = new ParseSuccess<>(parse, parseForest);

        observing.notify(observer -> observer.success(success));

        return success;
    }

    protected ParseFailure<ParseForest> failure(Parse<ParseForest, StackNode, ParseState> parse,
        ParseFailureType failureType) {
        ParseFailure<ParseForest> failure = new ParseFailure<>(parse, failureType);

        observing.notify(observer -> observer.failure(failure));

        return failure;
    }

    protected void parseLoop(Parse<ParseForest, StackNode, ParseState> parse) {
        while(parse.hasNext() && !parse.state.activeStacks.isEmpty()) {
            parseCharacter(parse);

            if(!parse.state.activeStacks.isEmpty())
                parse.next();
        }

        if(parse.state.acceptingStack == null) {
            boolean recover = failureHandler.onFailure(parse);

            if(recover)
                parseLoop(parse);
        }
    }

    protected void parseCharacter(Parse<ParseForest, StackNode, ParseState> parse) {
        observing.notify(observer -> observer.parseRound(parse, parse.state.activeStacks));

        parse.state.activeStacks.addAllTo(parse.state.forActorStacks);

        observing.notify(observer -> observer.forActorStacks(parse.state.forActorStacks));

        processForActorStacks(parse);

        shifter(parse);
    }

    protected void processForActorStacks(Parse<ParseForest, StackNode, ParseState> parse) {
        while(parse.state.forActorStacks.nonEmpty()) {
            StackNode stack = parse.state.forActorStacks.remove();

            parse.observing.notify(observer -> observer.handleForActorStack(stack, parse.state.forActorStacks));

            if(!stack.allLinksRejected())
                actor(stack, parse);
            else
                parse.observing.notify(observer -> observer.skipRejectedStack(stack));
        }
    }

    protected void actor(StackNode stack, Parse<ParseForest, StackNode, ParseState> parse) {
        observing.notify(observer -> observer.actor(stack, parse, stack.state().getApplicableActions(parse.state)));

        for(IAction action : stack.state().getApplicableActions(parse.state))
            actor(stack, parse, action);
    }

    protected void actor(StackNode stack, Parse<ParseForest, StackNode, ParseState> parse, IAction action) {
        switch(action.actionType()) {
            case SHIFT:
                IShift shiftAction = (IShift) action;
                IState shiftState = parseTable.getState(shiftAction.shiftStateId());

                addForShifter(parse, stack, shiftState);

                break;
            case REDUCE:
            case REDUCE_LOOKAHEAD: // Lookahead is checked while retrieving applicable actions from the state
                IReduce reduceAction = (IReduce) action;

                reduceManager.doReductions(parse, stack, reduceAction);

                break;
            case ACCEPT:
                parse.state.acceptingStack = stack;

                observing.notify(observer -> observer.accept(stack));

                break;
        }
    }

    protected void shifter(Parse<ParseForest, StackNode, ParseState> parse) {
        parse.state.activeStacks.clear();

        ParseForest characterNode = getNodeToShift(parse);

        observing.notify(observer -> observer.shifter(characterNode, parse.state.forShifter));

        for(ForShifterElement<StackNode> forShifterElement : parse.state.forShifter) {
            StackNode activeStackForState = parse.state.activeStacks.findWithState(forShifterElement.state);

            if(activeStackForState != null) {
                stackManager.createStackLink(parse, activeStackForState, forShifterElement.stack, characterNode);
            } else {
                StackNode newStack = stackManager.createStackNode(parse, forShifterElement.state);

                stackManager.createStackLink(parse, newStack, forShifterElement.stack, characterNode);

                parse.state.activeStacks.add(newStack);
            }
        }

        parse.state.forShifter.clear();
    }

    protected ParseForest getNodeToShift(Parse<ParseForest, StackNode, ParseState> parse) {
        return parseForestManager.createCharacterNode(parse);
    }

    private void addForShifter(Parse<ParseForest, StackNode, ParseState> parse, StackNode stack, IState shiftState) {
        ForShifterElement<StackNode> forShifterElement = new ForShifterElement<>(stack, shiftState);

        observing.notify(observer -> observer.addForShifter(forShifterElement));

        parse.state.forShifter.add(forShifterElement);
    }

    @Override public ParserObserving<ParseForest, StackNode, ParseState> observing() {
        return observing;
    }

}
