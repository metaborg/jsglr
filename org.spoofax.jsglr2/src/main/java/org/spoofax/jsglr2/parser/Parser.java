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
    ParseState    extends IParseState<ParseForest, StackNode>,
    Parse         extends AbstractParse<ParseForest, StackNode, ParseState>,
    StackManager  extends AbstractStackManager<ParseForest, StackNode, ParseState, Parse>,
    ReduceManager extends org.spoofax.jsglr2.reducing.ReduceManager<ParseForest, ParseNode, Derivation, StackNode, ParseState, Parse>>
//@formatter:on
    implements IObservableParser<ParseForest, StackNode, ParseState> {

    protected final ParseFactory<ParseForest, StackNode, ParseState, Parse> parseFactory;
    protected final IParseTable parseTable;
    protected final StackManager stackManager;
    protected final ParseForestManager<ParseForest, ParseNode, Derivation, Parse> parseForestManager;
    protected final ReduceManager reduceManager;
    protected final IParseFailureHandler<ParseForest, StackNode, ParseState, Parse> failureHandler;
    protected final ParserObserving<ParseForest, StackNode, ParseState> observing;

    public Parser(ParseFactory<ParseForest, StackNode, ParseState, Parse> parseFactory, IParseTable parseTable,
        StackManager stackManager, ParseForestManager<ParseForest, ParseNode, Derivation, Parse> parseForestManager,
        ReduceManagerFactory<ParseForest, ParseNode, Derivation, StackNode, ParseState, Parse, StackManager, ReduceManager> reduceManagerFactory,
        IParseFailureHandler<ParseForest, StackNode, ParseState, Parse> failureHandler,
        ParserObserving<ParseForest, StackNode, ParseState> observing) {
        this.parseFactory = parseFactory;
        this.parseTable = parseTable;
        this.stackManager = stackManager;
        this.parseForestManager = parseForestManager;
        this.reduceManager = reduceManagerFactory.get(parseTable, this.stackManager, parseForestManager);
        this.failureHandler = failureHandler;
        this.observing = observing;
    }

    @Override public ParseResult<ParseForest> parse(String inputString, String filename, String startSymbol) {
        Parse parse = getParse(inputString, filename);

        observing.notify(observer -> observer.parseStart(parse));

        StackNode initialStackNode = stackManager.createInitialStackNode(parse, parseTable.getStartState());

        parse.activeStacks.add(initialStackNode);

        parseLoop(parse);

        if(parse.acceptingStack != null) {
            ParseForest parseForest = stackManager.findDirectLink(parse.acceptingStack, initialStackNode).parseForest;

            ParseForest parseForestWithStartSymbol = startSymbol != null
                ? parseForestManager.filterStartSymbol(parseForest, startSymbol, parse) : parseForest;

            if(parseForest != null && parseForestWithStartSymbol == null)
                return failure(parse, ParseFailureType.InvalidStartSymbol);
            else
                return success(parse, parseForestWithStartSymbol);
        } else
            return failure(parse, failureHandler.failureType(parse));
    }

    protected Parse getParse(String inputString, String filename) {
        return parseFactory.get(inputString, filename, observing);
    }

    protected ParseSuccess<ParseForest> success(Parse parse, ParseForest parseForest) {
        ParseSuccess<ParseForest> success = new ParseSuccess<>(parse, parseForest);

        observing.notify(observer -> observer.success(success));

        return success;
    }

    protected ParseFailure<ParseForest> failure(Parse parse, ParseFailureType failureType) {
        ParseFailure<ParseForest> failure = new ParseFailure<>(parse, failureType);

        observing.notify(observer -> observer.failure(failure));

        return failure;
    }

    protected void parseLoop(Parse parse) {
        while(parse.hasNext() && !parse.activeStacks.isEmpty()) {
            parseCharacter(parse);

            if(!parse.activeStacks.isEmpty())
                parse.next();
        }

        if(parse.acceptingStack == null)
            failureHandler.onFailure(parse);
    }

    protected void parseCharacter(Parse parse) {
        observing.notify(observer -> observer.parseCharacter(parse, parse.activeStacks));

        parse.activeStacks.addAllTo(parse.forActorStacks);

        observing.notify(observer -> observer.forActorStacks(parse.forActorStacks));

        processForActorStacks(parse);

        shifter(parse);
    }

    protected void processForActorStacks(Parse parse) {
        while(parse.forActorStacks.nonEmpty()) {
            StackNode stack = parse.forActorStacks.remove();

            parse.observing.notify(observer -> observer.handleForActorStack(stack, parse.forActorStacks));

            if(!stack.allLinksRejected())
                actor(stack, parse);
            else
                parse.observing.notify(observer -> observer.skipRejectedStack(stack));
        }
    }

    protected void actor(StackNode stack, Parse parse) {
        observing.notify(observer -> observer.actor(stack, parse, stack.state().getApplicableActions(parse)));

        for(IAction action : stack.state().getApplicableActions(parse))
            actor(stack, parse, action);
    }

    protected void actor(StackNode stack, Parse parse, IAction action) {
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
                parse.acceptingStack = stack;

                observing.notify(observer -> observer.accept(stack));

                break;
        }
    }

    protected void shifter(Parse parse) {
        parse.activeStacks.clear();

        ParseForest characterNode = getNodeToShift(parse);

        observing.notify(observer -> observer.shifter(characterNode, parse.forShifter));

        for(ForShifterElement<StackNode> forShifterElement : parse.forShifter) {
            StackNode activeStackForState = parse.activeStacks.findWithState(forShifterElement.state);

            if(activeStackForState != null) {
                stackManager.createStackLink(parse, activeStackForState, forShifterElement.stack, characterNode);
            } else {
                StackNode newStack = stackManager.createStackNode(parse, forShifterElement.state);

                stackManager.createStackLink(parse, newStack, forShifterElement.stack, characterNode);

                parse.activeStacks.add(newStack);
            }
        }

        parse.forShifter.clear();
    }

    protected ParseForest getNodeToShift(Parse parse) {
        return parseForestManager.createCharacterNode(parse);
    }

    private void addForShifter(Parse parse, StackNode stack, IState shiftState) {
        ForShifterElement<StackNode> forShifterElement = new ForShifterElement<>(stack, shiftState);

        observing.notify(observer -> observer.addForShifter(forShifterElement));

        parse.forShifter.add(forShifterElement);
    }

    @Override public ParserObserving<ParseForest, StackNode, ParseState> observing() {
        return observing;
    }

}
