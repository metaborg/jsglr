package org.spoofax.jsglr2.parser;

import org.metaborg.characterclasses.CharacterClassFactory;
import org.metaborg.parsetable.IParseTable;
import org.metaborg.parsetable.IState;
import org.metaborg.parsetable.actions.IAction;
import org.metaborg.parsetable.actions.IReduce;
import org.metaborg.parsetable.actions.IShift;
import org.spoofax.jsglr2.parseforest.AbstractParseForest;
import org.spoofax.jsglr2.parseforest.ParseForestManager;
import org.spoofax.jsglr2.parser.observing.ParserObserving;
import org.spoofax.jsglr2.reducing.ReduceManager;
import org.spoofax.jsglr2.stack.AbstractStackNode;
import org.spoofax.jsglr2.stack.StackManager;
import org.spoofax.jsglr2.stack.collections.IActiveStacks;
import org.spoofax.jsglr2.stack.collections.IActiveStacksFactory;
import org.spoofax.jsglr2.stack.collections.IForActorStacks;
import org.spoofax.jsglr2.stack.collections.IForActorStacksFactory;

public class Parser<ParseForest extends AbstractParseForest, ParseNode extends ParseForest, Derivation, StackNode extends AbstractStackNode<ParseForest>>
    implements IParser<ParseForest, StackNode> {

    protected final IParseTable parseTable;
    protected final StackManager<ParseForest, StackNode> stackManager;
    protected final ParseForestManager<ParseForest, ParseNode, Derivation> parseForestManager;
    protected final ReduceManager<ParseForest, ParseNode, Derivation, StackNode> reduceManager;
    private final IActiveStacksFactory activeStacksFactory;
    private final IForActorStacksFactory forActorStacksFactory;
    protected final ParserObserving<ParseForest, StackNode> observing;

    public Parser(IParseTable parseTable, IActiveStacksFactory activeStacksFactory,
        IForActorStacksFactory forActorStacksFactory, StackManager<ParseForest, StackNode> stackManager,
        ParseForestManager<ParseForest, ParseNode, Derivation> parseForestManager,
        ReduceManager<ParseForest, ParseNode, Derivation, StackNode> reduceManager) {
        this.parseTable = parseTable;
        this.activeStacksFactory = activeStacksFactory;
        this.forActorStacksFactory = forActorStacksFactory;
        this.stackManager = stackManager;
        this.parseForestManager = parseForestManager;
        this.reduceManager = reduceManager;
        this.observing = new ParserObserving<>();
    }

    @Override
    public ParseResult<ParseForest, ?> parse(String inputString, String filename, String startSymbol) {
        IActiveStacks<StackNode> activeStacks = activeStacksFactory.get(observing);
        IForActorStacks<StackNode> forActorStacks = forActorStacksFactory.get(observing);

        Parse<ParseForest, StackNode> parse =
            new Parse<ParseForest, StackNode>(inputString, filename, activeStacks, forActorStacks, observing);

        observing.notify(observer -> observer.parseStart(parse));

        StackNode initialStackNode = stackManager.createInitialStackNode(parse, parseTable.getStartState());

        parse.activeStacks.add(initialStackNode);

        try {
            parseLoop(parse);

            ParseResult<ParseForest, ?> result;

            if(parse.acceptingStack != null) {
                ParseForest parseForest =
                    stackManager.findDirectLink(parse.acceptingStack, initialStackNode).parseForest;
                ParseForest parseForestWithStartSymbol =
                    startSymbol != null ? parseForestManager.filterStartSymbol(parseForest, startSymbol) : parseForest;

                if(parseForest != null && parseForestWithStartSymbol == null)
                    throw new ParseException("invalid start symbol");

                ParseSuccess<ParseForest, ?> success = new ParseSuccess<>(parse, parseForestWithStartSymbol);

                observing.notify(observer -> observer.success(success));

                result = success;
            } else {
                ParseFailure<ParseForest, ?> failure = new ParseFailure<>(parse,
                    new ParseException("unknown parse fail (file: " + parse.filename + ", char: " + parse.currentChar
                        + "/'" + CharacterClassFactory.intToString(parse.currentChar) + "', position: "
                        + parse.currentPosition().coordinatesToString() + " [" + parse.currentPosition().offset + "/"
                        + parse.inputLength + "])"));

                observing.notify(observer -> observer.failure(failure));

                result = failure;
            }

            return result;
        } catch(ParseException parseException) {
            ParseFailure<ParseForest, ?> failure = new ParseFailure<>(parse, parseException);

            observing.notify(observer -> observer.failure(failure));

            return failure;
        }
    }

    protected void parseLoop(Parse<ParseForest, StackNode> parse) throws ParseException {
        while(parse.hasNext() && !parse.activeStacks.isEmpty()) {
            parseCharacter(parse);

            parse.next();
        }
    }

    protected void parseCharacter(Parse<ParseForest, StackNode> parse) {
        observing.notify(observer -> observer.parseCharacter(parse, parse.activeStacks));

        parse.activeStacks.addAllTo(parse.forActorStacks);

        observing.notify(observer -> observer.forActorStacks(parse.forActorStacks));

        processForActorStacks(parse);

        shifter(parse);
    }

    protected void processForActorStacks(Parse<ParseForest, StackNode> parse) {
        while(parse.forActorStacks.nonEmpty()) {
            StackNode stack = parse.forActorStacks.remove();

            parse.observing.notify(observer -> observer.handleForActorStack(stack, parse.forActorStacks));

            if(!stack.allLinksRejected())
                actor(stack, parse);
            else
                parse.observing.notify(observer -> observer.skipRejectedStack(stack));
        }
    }

    protected void actor(StackNode stack, Parse<ParseForest, StackNode> parse) {
        observing.notify(observer -> observer.actor(stack, parse, stack.state.getApplicableActions(parse)));

        for(IAction action : stack.state.getApplicableActions(parse))
            actor(stack, parse, action);
    }

    protected void actor(StackNode stack, Parse<ParseForest, StackNode> parse, IAction action) {
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

    protected void shifter(Parse<ParseForest, StackNode> parse) {
        parse.activeStacks.clear();

        ParseForest characterNode = parseForestManager.createCharacterNode(parse);

        observing.notify(observer -> observer.shifter(characterNode, parse.forShifter));

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

        parse.forShifter.clear();
    }

    private void addForShifter(Parse<ParseForest, StackNode> parse, StackNode stack, IState shiftState) {
        ForShifterElement<ParseForest, StackNode> forShifterElement =
            new ForShifterElement<ParseForest, StackNode>(stack, shiftState);

        observing.notify(observer -> observer.addForShifter(forShifterElement));

        parse.forShifter.add(forShifterElement);
    }

    @Override
    public ParserObserving<ParseForest, StackNode> observing() {
        return observing;
    }

}
