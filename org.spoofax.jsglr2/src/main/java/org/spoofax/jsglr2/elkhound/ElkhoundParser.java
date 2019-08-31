package org.spoofax.jsglr2.elkhound;

import java.util.Iterator;

import org.metaborg.parsetable.IParseTable;
import org.metaborg.parsetable.actions.IAction;
import org.metaborg.parsetable.actions.IReduce;
import org.metaborg.parsetable.actions.IShift;
import org.metaborg.parsetable.states.IState;
import org.spoofax.jsglr2.parseforest.IDerivation;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parseforest.ParseForestManager;
import org.spoofax.jsglr2.parser.AbstractParse;
import org.spoofax.jsglr2.parser.ParseFactory;
import org.spoofax.jsglr2.parser.Parser;
import org.spoofax.jsglr2.parser.failure.IParseFailureHandler;
import org.spoofax.jsglr2.parser.observing.ParserObserving;
import org.spoofax.jsglr2.reducing.ReduceManagerFactory;
import org.spoofax.jsglr2.stack.AbstractStackManager;

public class ElkhoundParser
//@formatter:off
   <ParseForest       extends IParseForest,
    ParseNode         extends ParseForest,
    Derivation        extends IDerivation<ParseForest>,
    ElkhoundStackNode extends AbstractElkhoundStackNode<ParseForest>,
    Parse             extends AbstractParse<ParseForest, ElkhoundStackNode>,
    StackManager      extends AbstractStackManager<ParseForest, ElkhoundStackNode, Parse>,
    ReduceManager     extends org.spoofax.jsglr2.reducing.ReduceManager<
                                  ParseForest, ParseNode, Derivation, ElkhoundStackNode, Parse>>
//@formatter:on
    extends Parser<ParseForest, ParseNode, Derivation, ElkhoundStackNode, Parse, StackManager, ReduceManager> {

    public ElkhoundParser(ParseFactory<ParseForest, ElkhoundStackNode, Parse> parseFactory, IParseTable parseTable,
        StackManager stackManager, ParseForestManager<ParseForest, ParseNode, Derivation, Parse> parseForestManager,
        ReduceManagerFactory<ParseForest, ParseNode, Derivation, ElkhoundStackNode, Parse, StackManager, ReduceManager> elkhoundReduceManagerFactory,
        IParseFailureHandler failureHandler, ParserObserving observing) {
        super(parseFactory, parseTable, stackManager, parseForestManager, elkhoundReduceManagerFactory, failureHandler,
            observing);
    }

    @Override protected void parseLoop(Parse parse) {
        while(parse.hasNext() && !parse.activeStacks.isEmpty()) {
            if(parse.activeStacks.isSingle()) {
                ElkhoundStackNode singleActiveStack = parse.activeStacks.getSingle();

                if(!singleActiveStack.allLinksRejected()) {
                    Iterator<IAction> actionsIterator = singleActiveStack.state.getApplicableActions(parse).iterator();

                    if(actionsIterator.hasNext()) {
                        IAction firstAction = actionsIterator.next();

                        if(!actionsIterator.hasNext()) {
                            parse.activeStacks.clear();

                            switch(firstAction.actionType()) {
                                case SHIFT:
                                    // A single shift action applicable on a single active stack, we can thus directly
                                    // shift and go the the next character
                                    IShift shiftAction = (IShift) firstAction;
                                    IState shiftState = parseTable.getState(shiftAction.shiftStateId());

                                    ElkhoundStackNode newStack = stackManager.createStackNode(parse, shiftState);
                                    ParseForest characterNode = parseForestManager.createCharacterNode(parse);

                                    stackManager.createStackLink(parse, newStack, singleActiveStack, characterNode);

                                    parse.activeStacks.add(newStack);

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
                                    if(parse.forActorStacks.nonEmpty()) {
                                        parse.activeStacks.add(singleActiveStack);

                                        processForActorStacks(parse);
                                        shifter(parse);
                                        parse.next();
                                    }

                                    break;
                                case ACCEPT:
                                    parse.acceptingStack = singleActiveStack;

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
                        parse.activeStacks.clear();
                        return;
                    }
                } else {
                    parse.observing.notify(observer -> observer.skipRejectedStack(singleActiveStack));

                    // The single active stack that was left is rejected, thus parsing fails
                    parse.activeStacks.clear();
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
