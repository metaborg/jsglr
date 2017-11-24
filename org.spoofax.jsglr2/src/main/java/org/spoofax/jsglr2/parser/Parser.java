package org.spoofax.jsglr2.parser;

import java.util.ArrayList;
import java.util.List;

import org.spoofax.jsglr2.JSGLR2Variants.ParseForestConstruction;
import org.spoofax.jsglr2.actions.IAction;
import org.spoofax.jsglr2.actions.IReduce;
import org.spoofax.jsglr2.actions.IReduceLookahead;
import org.spoofax.jsglr2.actions.IShift;
import org.spoofax.jsglr2.characters.ICharacterClass;
import org.spoofax.jsglr2.parseforest.AbstractParseForest;
import org.spoofax.jsglr2.parseforest.ParseForestManager;
import org.spoofax.jsglr2.parsetable.IParseTable;
import org.spoofax.jsglr2.reducing.ReduceManager;
import org.spoofax.jsglr2.stack.AbstractStackNode;
import org.spoofax.jsglr2.stack.StackManager;
import org.spoofax.jsglr2.states.IState;

public class Parser<StackNode extends AbstractStackNode<ParseForest>, ParseForest extends AbstractParseForest, ParseNode extends ParseForest, Derivation>
    implements IParser<StackNode, ParseForest> {

    private final IParseTable parseTable;
    private final StackManager<StackNode, ParseForest> stackManager;
    private final ParseForestManager<ParseForest, ParseNode, Derivation> parseForestManager;
    private final ReduceManager<StackNode, ParseForest, ParseNode, Derivation> reducer;
    private final List<IParserObserver<StackNode, ParseForest>> observers;

    public Parser(IParseTable parseTable, StackManager<StackNode, ParseForest> stackManager,
        ParseForestManager<ParseForest, ParseNode, Derivation> parseForestManager) {
        this.parseTable = parseTable;
        this.stackManager = stackManager;
        this.parseForestManager = parseForestManager;
        this.reducer = new ReduceManager<StackNode, ParseForest, ParseNode, Derivation>(parseTable, stackManager,
            parseForestManager, ParseForestConstruction.Full);
        this.observers = new ArrayList<IParserObserver<StackNode, ParseForest>>();
    }

    public Parser(IParseTable parseTable, StackManager<StackNode, ParseForest> stackManager,
        ParseForestManager<ParseForest, ParseNode, Derivation> parseForestManager,
        ReduceManager<StackNode, ParseForest, ParseNode, Derivation> reducer) {
        this.parseTable = parseTable;
        this.stackManager = stackManager;
        this.parseForestManager = parseForestManager;
        this.reducer = reducer;
        this.observers = new ArrayList<IParserObserver<StackNode, ParseForest>>();
    }

    @Override public ParseResult<StackNode, ParseForest, ?> parse(String inputString, String filename,
        String startSymbol) {
        Parse<StackNode, ParseForest> parse = new Parse<StackNode, ParseForest>(inputString, filename, observers);

        notify(observer -> observer.parseStart(parse));

        StackNode initialStackNode = stackManager.createInitialStackNode(parse, parseTable.getStartState());

        parse.activeStacks.add(initialStackNode);

        try {
            while(parse.hasNext() && !parse.activeStacks.isEmpty()) {
                parseCharacter(parse, parse.currentChar);

                parse.next();
            }

            ParseResult<StackNode, ParseForest, ?> result;

            if(parse.acceptingStack != null) {
                ParseForest parseForest =
                    stackManager.findDirectLink(parse.acceptingStack, initialStackNode).parseForest;
                ParseForest parseForestWithStartSymbol =
                    startSymbol != null ? parseForestManager.filterStartSymbol(parseForest, startSymbol) : parseForest;

                if(parseForest != null && parseForestWithStartSymbol == null)
                    throw new ParseException("invalid start symbol");

                ParseSuccess<StackNode, ParseForest, ?> success = new ParseSuccess(parse, parseForestWithStartSymbol);

                notify(observer -> observer.success(success));

                result = success;
            } else {
                ParseFailure<StackNode, ParseForest, ?> failure = new ParseFailure(parse,
                    new ParseException("unknown parse fail (file: " + parse.filename + ", char: " + parse.currentChar
                        + "/'" + ICharacterClass.intToString(parse.currentChar) + "', position: "
                        + parse.currentPosition().coordinatesToString() + " [" + parse.currentPosition().offset + "/"
                        + parse.inputLength + "])"));

                notify(observer -> observer.failure(failure));

                result = failure;
            }

            return result;
        } catch(ParseException parseException) {
            ParseFailure<StackNode, ParseForest, ?> failure = new ParseFailure(parse, parseException);

            notify(observer -> observer.failure(failure));

            return failure;
        }
    }

    private void parseCharacter(Parse<StackNode, ParseForest> parse, int character) {
        notify(observer -> observer.parseCharacter(parse, parse.activeStacks));

        parse.activeStacks.addAllTo(parse.forActorStacks);

        parse.forShifter.clear();

        notify(observer -> observer.forActorStacks(parse.forActorStacks));

        while(parse.forActorStacks.nonEmpty()) {
            StackNode stack = parse.forActorStacks.remove();

            notify(observer -> observer.handleForActorStack(stack, parse.forActorStacks));

            if(!stack.allOutLinksRejected())
                actor(stack, parse, character);
            else
                notify(observer -> observer.skipRejectedStack(stack));
        }

        shifter(parse);
    }

    private void actor(StackNode stack, Parse<StackNode, ParseForest> parse, int character) {
        notify(observer -> observer.actor(stack, parse, stack.state.getActions(character)));

        // Use for loop here rather than IState::applicableActions since it is faster
        for(IAction action : stack.state.getActions(character)) {
            switch(action.actionType()) {
                case SHIFT:
                    IShift shiftAction = (IShift) action;
                    IState shiftState = parseTable.getState(shiftAction.shiftStateId());

                    addForShifter(parse, stack, shiftState);

                    break;
                case REDUCE:
                    IReduce reduceAction = (IReduce) action;

                    reducer.doReductions(parse, stack, reduceAction);

                    break;
                case REDUCE_LOOKAHEAD:
                    IReduceLookahead reduceLookaheadAction = (IReduceLookahead) action;

                    if(reduceLookaheadAction.allowsLookahead(parse)) {
                        reducer.doReductions(parse, stack, reduceLookaheadAction);
                    }

                    break;
                case ACCEPT:
                    parse.acceptingStack = stack;

                    notify(observer -> observer.accept(stack));

                    break;
            }
        }
    }

    private void shifter(Parse<StackNode, ParseForest> parse) {
        parse.activeStacks.clear();

        ParseForest characterNode = parseForestManager.createCharacterNode(parse);

        notify(observer -> observer.shifter(characterNode, parse.forShifter));

        for(ForShifterElement<StackNode, ParseForest> forShifterElement : parse.forShifter) {
            StackNode activeStackForState = parse.activeStacks.findWithState(forShifterElement.state);

            if(activeStackForState != null) {
                stackManager.createStackLink(parse, activeStackForState, forShifterElement.stack, characterNode);
            } else {
                StackNode newStack = stackManager.createStackNode(parse, forShifterElement.state);

                stackManager.createStackLink(parse, newStack, forShifterElement.stack, characterNode);

                parse.activeStacks.add(newStack);
            }
        }
    }

    private void addForShifter(Parse<StackNode, ParseForest> parse, StackNode stack, IState shiftState) {
        ForShifterElement<StackNode, ParseForest> forShifterElement =
            new ForShifterElement<StackNode, ParseForest>(stack, shiftState);

        notify(observer -> observer.addForShifter(forShifterElement));

        parse.forShifter.add(forShifterElement);
    }

    @Override public void attachObserver(IParserObserver<StackNode, ParseForest> observer) {
        observers.add(observer);
    }

    private void notify(IParserNotification<StackNode, ParseForest> notification) {
        for(IParserObserver<StackNode, ParseForest> observer : observers)
            notification.notify(observer);
    }

}
