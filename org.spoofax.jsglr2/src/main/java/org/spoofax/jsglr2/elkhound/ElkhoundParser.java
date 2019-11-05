package org.spoofax.jsglr2.elkhound;

import org.metaborg.parsetable.IParseTable;
import org.metaborg.parsetable.actions.IAction;
import org.metaborg.parsetable.actions.IReduce;
import org.metaborg.parsetable.actions.IShift;
import org.metaborg.parsetable.states.IState;
import org.spoofax.jsglr2.parseforest.IDerivation;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parseforest.IParseNode;
import org.spoofax.jsglr2.parseforest.ParseForestManagerFactory;
import org.spoofax.jsglr2.parser.AbstractParseState;
import org.spoofax.jsglr2.parser.ParseStateFactory;
import org.spoofax.jsglr2.parser.Parser;
import org.spoofax.jsglr2.parser.failure.ParseFailureHandlerFactory;
import org.spoofax.jsglr2.reducing.ReduceManagerFactory;
import org.spoofax.jsglr2.stack.AbstractStackManager;
import org.spoofax.jsglr2.stack.StackManagerFactory;

import java.util.Iterator;

public class ElkhoundParser
//@formatter:off
   <ParseForest       extends IParseForest,
    Derivation        extends IDerivation<ParseForest>,
    ParseNode         extends IParseNode<ParseForest, Derivation>,
    ElkhoundStackNode extends AbstractElkhoundStackNode<ParseForest>,
    ParseState        extends AbstractParseState<ElkhoundStackNode>,
    StackManager      extends AbstractStackManager<ParseForest, Derivation, ParseNode, ElkhoundStackNode, ParseState>,
    ReduceManager     extends org.spoofax.jsglr2.reducing.ReduceManager<ParseForest, Derivation, ParseNode, ElkhoundStackNode, ParseState>>
//@formatter:on
    extends Parser<ParseForest, Derivation, ParseNode, ElkhoundStackNode, ParseState, StackManager, ReduceManager> {

    public ElkhoundParser(
        ParseStateFactory<ParseForest, Derivation, ParseNode, ElkhoundStackNode, ParseState> parseStateFactory,
        IParseTable parseTable,
        StackManagerFactory<ParseForest, Derivation, ParseNode, ElkhoundStackNode, ParseState, StackManager> stackManagerFactory,
        ParseForestManagerFactory<ParseForest, Derivation, ParseNode, ElkhoundStackNode, ParseState> parseForestManagerFactory,
        ReduceManagerFactory<ParseForest, Derivation, ParseNode, ElkhoundStackNode, ParseState, StackManager, ReduceManager> elkhoundReduceManagerFactory,
        ParseFailureHandlerFactory<ParseForest, Derivation, ParseNode, ElkhoundStackNode, ParseState> failureHandlerFactory) {
        super(parseStateFactory, parseTable, stackManagerFactory, parseForestManagerFactory,
            elkhoundReduceManagerFactory, failureHandlerFactory);
    }

    @Override protected void parseLoop(ParseState parseState) {
        boolean nextRound = true;

        while(parseState.hasNext() && !parseState.activeStacks.isEmpty()) {
            if(parseState.activeStacks.isSingle()) {
                if(nextRound)
                    observing.notify(observer -> observer.parseRound(parseState, parseState.activeStacks, observing));

                ElkhoundStackNode singleActiveStack = parseState.activeStacks.getSingle();

                if(!singleActiveStack.allLinksRejected()) {
                    Iterator<IAction> actionsIterator =
                        singleActiveStack.state.getApplicableActions(parseState).iterator();

                    if(actionsIterator.hasNext()) {
                        IAction firstAction = actionsIterator.next();

                        if(!actionsIterator.hasNext()) {
                            parseState.activeStacks.clear();

                            switch(firstAction.actionType()) {
                                case SHIFT:
                                    // A single shift action applicable on a single active stack, we can thus directly
                                    // shift and go the the next character
                                    IShift shiftAction = (IShift) firstAction;
                                    IState shiftState = parseTable.getState(shiftAction.shiftStateId());

                                    ElkhoundStackNode newStack = stackManager.createStackNode(shiftState);
                                    ParseForest characterNode = parseForestManager.createCharacterNode(parseState);

                                    stackManager.createStackLink(parseState, newStack, singleActiveStack,
                                        characterNode);

                                    parseState.activeStacks.add(newStack);

                                    parseState.next();

                                    nextRound = true;

                                    break;
                                case REDUCE:
                                case REDUCE_LOOKAHEAD:
                                    IReduce reduceAction = (IReduce) firstAction;

                                    reduceManager.doReductions(observing, parseState, singleActiveStack, reduceAction);

                                    // If stacks are added to forActorStacks, the reduction was not LR (deterministic
                                    // depth not big enough, so there is reduced over multiple paths), we thus partly
                                    // fall back to (S)GLR by processing the forActorStacks collection and calling
                                    // shifter afterwards, before going to the next character
                                    if(parseState.forActorStacks.nonEmpty()) {
                                        parseState.activeStacks.add(singleActiveStack);

                                        processForActorStacks(parseState);
                                        shifter(parseState);
                                        parseState.next();

                                        nextRound = true;
                                    } else
                                        nextRound = false;

                                    break;
                                case ACCEPT:
                                    parseState.acceptingStack = singleActiveStack;

                                    observing.notify(observer -> observer.accept(singleActiveStack));

                                    return;
                            }
                        } else {
                            actor(singleActiveStack, parseState, firstAction);

                            while(actionsIterator.hasNext())
                                actor(singleActiveStack, parseState, actionsIterator.next());

                            // The forActorStacks collection could be filled with actions from the reductions that are
                            // applied, so we need continue like regular (S)GLR, by processing the active stacks,
                            // performing shifts and then proceed to the next character
                            processForActorStacks(parseState);
                            shifter(parseState);
                            parseState.next();

                            nextRound = true;
                        }
                    } else {
                        // The single active stack that was left has no applicable actions, thus parsing fails
                        parseState.activeStacks.clear();
                        return;
                    }
                } else {
                    observing.notify(observer -> observer.skipRejectedStack(singleActiveStack));

                    // The single active stack that was left is rejected, thus parsing fails
                    parseState.activeStacks.clear();
                    return;
                }
            } else {
                // Fall back to regular (S)GLR when multiple stacks are active
                parseCharacter(parseState);

                parseState.next();

                nextRound = true;
            }
        }
    }

}
