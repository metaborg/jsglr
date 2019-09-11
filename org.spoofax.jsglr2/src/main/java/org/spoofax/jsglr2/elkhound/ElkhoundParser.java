package org.spoofax.jsglr2.elkhound;

import org.metaborg.parsetable.IParseTable;
import org.metaborg.parsetable.actions.IAction;
import org.metaborg.parsetable.actions.IReduce;
import org.metaborg.parsetable.actions.IShift;
import org.metaborg.parsetable.states.IState;
import org.spoofax.jsglr2.parseforest.IDerivation;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parseforest.ParseForestManager;
import org.spoofax.jsglr2.parser.AbstractParseState;
import org.spoofax.jsglr2.parser.Parse;
import org.spoofax.jsglr2.parser.ParseFactory;
import org.spoofax.jsglr2.parser.Parser;
import org.spoofax.jsglr2.parser.failure.IParseFailureHandler;
import org.spoofax.jsglr2.reducing.ReduceManagerFactory;
import org.spoofax.jsglr2.stack.AbstractStackManager;

import java.util.Iterator;

public class ElkhoundParser
//@formatter:off
   <ParseForest       extends IParseForest,
    ParseNode         extends ParseForest,
    Derivation        extends IDerivation<ParseForest>,
    ElkhoundStackNode extends AbstractElkhoundStackNode<ParseForest>,
    ParseState        extends AbstractParseState<ParseForest, ElkhoundStackNode>,
    StackManager      extends AbstractStackManager<ParseForest, ElkhoundStackNode, ParseState>,
    ReduceManager     extends org.spoofax.jsglr2.reducing.ReduceManager<
                                  ParseForest, ParseNode, Derivation, ElkhoundStackNode, ParseState>>
//@formatter:on
    extends Parser<ParseForest, ParseNode, Derivation, ElkhoundStackNode, ParseState, StackManager, ReduceManager> {

    public ElkhoundParser(ParseFactory<ParseForest, ElkhoundStackNode, ParseState> parseFactory, IParseTable parseTable,
        StackManager stackManager,
        ParseForestManager<ParseForest, ParseNode, Derivation, ElkhoundStackNode, ParseState> parseForestManager,
        ReduceManagerFactory<ParseForest, ParseNode, Derivation, ElkhoundStackNode, ParseState, StackManager, ReduceManager> elkhoundReduceManagerFactory,
        IParseFailureHandler<ParseForest, ElkhoundStackNode, ParseState> failureHandler) {
        super(parseFactory, parseTable, stackManager, parseForestManager, elkhoundReduceManagerFactory, failureHandler);
    }

    @Override protected void parseLoop(Parse<ParseForest, ElkhoundStackNode, ParseState> parse) {
        while(parse.hasNext() && !parse.state.activeStacks.isEmpty()) {
            if(parse.state.activeStacks.isSingle()) {
                observing.notify(observer -> observer.parseRound(parse, parse.state.activeStacks));

                ElkhoundStackNode singleActiveStack = parse.state.activeStacks.getSingle();

                if(!singleActiveStack.allLinksRejected()) {
                    Iterator<IAction> actionsIterator =
                        singleActiveStack.state.getApplicableActions(parse.state).iterator();

                    if(actionsIterator.hasNext()) {
                        IAction firstAction = actionsIterator.next();

                        if(!actionsIterator.hasNext()) {
                            parse.state.activeStacks.clear();

                            switch(firstAction.actionType()) {
                                case SHIFT:
                                    // A single shift action applicable on a single active stack, we can thus directly
                                    // shift and go the the next character
                                    IShift shiftAction = (IShift) firstAction;
                                    IState shiftState = parseTable.getState(shiftAction.shiftStateId());

                                    ElkhoundStackNode newStack = stackManager.createStackNode(parse, shiftState);
                                    ParseForest characterNode = parseForestManager.createCharacterNode(parse);

                                    stackManager.createStackLink(parse, newStack, singleActiveStack, characterNode);

                                    parse.state.activeStacks.add(newStack);

                                    parse.next();
                                    break;
                                case REDUCE:
                                case REDUCE_LOOKAHEAD:
                                    IReduce reduceAction = (IReduce) firstAction;

                                    reduceManager.doReductions(parse, singleActiveStack, reduceAction);

                                    // If stacks are added to forActorStacks, the reduction was not LR (deterministic
                                    // depth not big enough, so there is reduced over multiple paths), we thus partly
                                    // fall back to (S)GLR by processing the forActorStacks collection and calling
                                    // shifter afterwards, before going to the next character
                                    if(parse.state.forActorStacks.nonEmpty()) {
                                        parse.state.activeStacks.add(singleActiveStack);

                                        processForActorStacks(parse);
                                        shifter(parse);
                                        parse.next();
                                    }

                                    break;
                                case ACCEPT:
                                    parse.state.acceptingStack = singleActiveStack;

                                    observing.notify(observer -> observer.accept(singleActiveStack));

                                    return;
                            }
                        } else {
                            actor(singleActiveStack, parse, firstAction);

                            while(actionsIterator.hasNext())
                                actor(singleActiveStack, parse, actionsIterator.next());

                            // The forActorStacks collection could be filled with actions from the reductions that are
                            // applied, so we need continue like regular (S)GLR, by processing the active stacks,
                            // performing shifts and then proceed to the next character
                            processForActorStacks(parse);
                            shifter(parse);
                            parse.next();
                        }
                    } else {
                        // The single active stack that was left has no applicable actions, thus parsing fails
                        parse.state.activeStacks.clear();
                        return;
                    }
                } else {
                    parse.observing.notify(observer -> observer.skipRejectedStack(singleActiveStack));

                    // The single active stack that was left is rejected, thus parsing fails
                    parse.state.activeStacks.clear();
                    return;
                }
            } else {
                // Fall back to regular (S)GLR when multiple stacks are active
                parseCharacter(parse);

                parse.next();
            }
        }
    }

}
