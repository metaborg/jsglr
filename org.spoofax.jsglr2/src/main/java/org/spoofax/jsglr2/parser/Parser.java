package org.spoofax.jsglr2.parser;

import java.util.ArrayList;
import java.util.List;

import org.spoofax.jsglr2.JSGLR2Variants.ParseForestConstruction;
import org.spoofax.jsglr2.actions.IAction;
import org.spoofax.jsglr2.actions.IReduce;
import org.spoofax.jsglr2.actions.IReduceLookahead;
import org.spoofax.jsglr2.actions.IShift;
import org.spoofax.jsglr2.characterclasses.ICharacterClass;
import org.spoofax.jsglr2.parseforest.AbstractParseForest;
import org.spoofax.jsglr2.parseforest.ParseForestManager;
import org.spoofax.jsglr2.parsetable.IParseTable;
import org.spoofax.jsglr2.reducing.ReduceManager;
import org.spoofax.jsglr2.stack.AbstractStackNode;
import org.spoofax.jsglr2.stack.StackManager;
import org.spoofax.jsglr2.states.IState;

public class Parser<ParseForest extends AbstractParseForest, ParseNode extends ParseForest, Derivation, StackNode extends AbstractStackNode<ParseForest>>
    implements IParser<ParseForest, StackNode> {

    private final IParseTable parseTable;
    private final StackManager<ParseForest, StackNode> stackManager;
    private final ParseForestManager<ParseForest, ParseNode, Derivation> parseForestManager;
    private final ReduceManager<ParseForest, ParseNode, Derivation, StackNode> reducer;
    private final List<IParserObserver<ParseForest, StackNode>> observers;

    public Parser(IParseTable parseTable, StackManager<ParseForest, StackNode> stackManager,
        ParseForestManager<ParseForest, ParseNode, Derivation> parseForestManager) {
        this.parseTable = parseTable;
        this.stackManager = stackManager;
        this.parseForestManager = parseForestManager;
        this.reducer = new ReduceManager<ParseForest, ParseNode, Derivation, StackNode>(parseTable, stackManager,
            parseForestManager, ParseForestConstruction.Full);
        this.observers = new ArrayList<IParserObserver<ParseForest, StackNode>>();
    }

    public Parser(IParseTable parseTable, StackManager<ParseForest, StackNode> stackManager,
        ParseForestManager<ParseForest, ParseNode, Derivation> parseForestManager,
        ReduceManager<ParseForest, ParseNode, Derivation, StackNode> reducer) {
        this.parseTable = parseTable;
        this.stackManager = stackManager;
        this.parseForestManager = parseForestManager;
        this.reducer = reducer;
        this.observers = new ArrayList<IParserObserver<ParseForest, StackNode>>();
    }

    @Override
    public ParseResult<ParseForest, StackNode, ?> parse(String inputString, String filename, String startSymbol) {
        Parse<ParseForest, StackNode> parse = new Parse<ParseForest, StackNode>(inputString, filename, observers);

        notify(observer -> observer.parseStart(parse));

        StackNode initialStackNode = stackManager.createInitialStackNode(parse, parseTable.getStartState());

        parse.activeStacks.add(initialStackNode);

        try {
            while(parse.hasNext() && !parse.activeStacks.isEmpty()) {
                parseCharacter(parse, parse.currentChar);

                parse.next();
            }

            ParseResult<ParseForest, StackNode, ?> result;

            if(parse.acceptingStack != null) {
                ParseForest parseForest =
                    stackManager.findDirectLink(parse.acceptingStack, initialStackNode).parseForest;
                ParseForest parseForestWithStartSymbol =
                    startSymbol != null ? parseForestManager.filterStartSymbol(parseForest, startSymbol) : parseForest;

                if(parseForest != null && parseForestWithStartSymbol == null)
                    throw new ParseException("invalid start symbol");

                ParseSuccess<ParseForest, StackNode, ?> success = new ParseSuccess<>(parse, parseForestWithStartSymbol);

                notify(observer -> observer.success(success));

                result = success;
            } else {
                ParseFailure<ParseForest, StackNode, ?> failure = new ParseFailure<>(parse,
                    new ParseException("unknown parse fail (file: " + parse.filename + ", char: " + parse.currentChar
                        + "/'" + ICharacterClass.intToString(parse.currentChar) + "', position: "
                        + parse.currentPosition().coordinatesToString() + " [" + parse.currentPosition().offset + "/"
                        + parse.inputLength + "])"));

                notify(observer -> observer.failure(failure));

                result = failure;
            }

            return result;
        } catch(ParseException parseException) {
            ParseFailure<ParseForest, StackNode, ?> failure = new ParseFailure<>(parse, parseException);

            notify(observer -> observer.failure(failure));

            return failure;
        }
    }

    private void parseCharacter(Parse<ParseForest, StackNode> parse, int character) {
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

    private void actor(StackNode stack, Parse<ParseForest, StackNode> parse, int character) {
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

    private void shifter(Parse<ParseForest, StackNode> parse) {
        parse.activeStacks.clear();

        ParseForest characterNode = parseForestManager.createCharacterNode(parse);

        notify(observer -> observer.shifter(characterNode, parse.forShifter));

        for(ForShifterElement<ParseForest, StackNode> forShifterElement : parse.forShifter) {
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

    private void addForShifter(Parse<ParseForest, StackNode> parse, StackNode stack, IState shiftState) {
        ForShifterElement<ParseForest, StackNode> forShifterElement =
            new ForShifterElement<ParseForest, StackNode>(stack, shiftState);

        notify(observer -> observer.addForShifter(forShifterElement));

        parse.forShifter.add(forShifterElement);
    }

    @Override
    public void attachObserver(IParserObserver<ParseForest, StackNode> observer) {
        observers.add(observer);
    }

    private void notify(IParserNotification<ParseForest, StackNode> notification) {
        for(IParserObserver<ParseForest, StackNode> observer : observers)
            notification.notify(observer);
    }

}
