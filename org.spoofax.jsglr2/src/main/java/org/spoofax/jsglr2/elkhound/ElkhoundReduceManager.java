package org.spoofax.jsglr2.elkhound;

import org.metaborg.parsetable.IParseTable;
import org.metaborg.parsetable.actions.IReduce;
import org.metaborg.parsetable.states.IState;
import org.spoofax.jsglr2.inputstack.IInputStack;
import org.spoofax.jsglr2.parseforest.IDerivation;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parseforest.IParseNode;
import org.spoofax.jsglr2.parseforest.ParseForestManager;
import org.spoofax.jsglr2.parser.AbstractParseState;
import org.spoofax.jsglr2.parser.observing.ParserObserving;
import org.spoofax.jsglr2.reducing.ReduceManager;
import org.spoofax.jsglr2.reducing.ReduceManagerFactory;
import org.spoofax.jsglr2.reducing.ReducerFactory;
import org.spoofax.jsglr2.stack.StackLink;
import org.spoofax.jsglr2.stack.paths.StackPath;

public class ElkhoundReduceManager
//@formatter:off
   <ParseForest       extends IParseForest,
    Derivation        extends IDerivation<ParseForest>,
    ParseNode         extends IParseNode<ParseForest, Derivation>,
    ElkhoundStackNode extends AbstractElkhoundStackNode<ParseForest>,
    InputStack        extends IInputStack,
    ParseState        extends AbstractParseState<InputStack, ElkhoundStackNode>>
//@formatter:on
    extends ReduceManager<ParseForest, Derivation, ParseNode, ElkhoundStackNode, InputStack, ParseState> {

    protected final ElkhoundStackManager<ParseForest, Derivation, ParseNode, ElkhoundStackNode, ParseState> stackManager;

    public ElkhoundReduceManager(IParseTable parseTable,
        ElkhoundStackManager<ParseForest, Derivation, ParseNode, ElkhoundStackNode, ParseState> stackManager,
        ParseForestManager<ParseForest, Derivation, ParseNode, ElkhoundStackNode, ParseState> parseForestManager,
        ReducerFactory<ParseForest, Derivation, ParseNode, ElkhoundStackNode, InputStack, ParseState> reducerFactory) {
        super(parseTable, stackManager, parseForestManager, reducerFactory);

        this.stackManager = stackManager;
    }

    public static
    //@formatter:off
       <ParseForest_  extends IParseForest,
        Derivation_   extends IDerivation<ParseForest_>,
        ParseNode_    extends IParseNode<ParseForest_, Derivation_>,
        StackNode_    extends AbstractElkhoundStackNode<ParseForest_>,
        InputStack_   extends IInputStack,
        ParseState_   extends AbstractParseState<InputStack_, StackNode_>,
        StackManager_ extends ElkhoundStackManager<ParseForest_, Derivation_, ParseNode_, StackNode_, ParseState_>>
    //@formatter:on
    ReduceManagerFactory<ParseForest_, Derivation_, ParseNode_, StackNode_, InputStack_, ParseState_, StackManager_, ElkhoundReduceManager<ParseForest_, Derivation_, ParseNode_, StackNode_, InputStack_, ParseState_>>
        factoryElkhound(
            ReducerFactory<ParseForest_, Derivation_, ParseNode_, StackNode_, InputStack_, ParseState_> reducerFactory) {
        return (parseTable, stackManager, parseForestManager) -> new ElkhoundReduceManager<>(parseTable, stackManager,
            parseForestManager, reducerFactory);
    }

    @Override protected void doReductionsHelper(
        ParserObserving<ParseForest, Derivation, ParseNode, ElkhoundStackNode, ParseState> observing,
        ParseState parseState, ElkhoundStackNode activeStack, IReduce reduce,
        StackLink<ParseForest, ElkhoundStackNode> throughLink) {
        if(activeStack.deterministicDepth >= reduce.arity()) {
            DeterministicStackPath<ParseForest, ElkhoundStackNode> deterministicPath =
                stackManager.findDeterministicPathOfLength(parseForestManager, activeStack, reduce.arity());

            if(throughLink == null || deterministicPath.contains(throughLink)) {
                ElkhoundStackNode originStack = deterministicPath.head();

                ParseForest[] parseNodes = deterministicPath.parseForests;

                if(!ignoreReducePath(originStack, reduce, parseNodes)) {
                    if(parseState.activeStacks.isEmpty())
                        // Do LR if there are no other active stacks (the stack on which the current reduction is
                        // applied is removed from the activeStacks collection in ElkhoundParser)
                        reducerElkhound(observing, parseState, activeStack, originStack, reduce, parseNodes);
                    else
                        // Benefit from faster path retrieval, but still do regular (S)GLR reducing since there are
                        // other active stacks
                        reducer(observing, parseState, activeStack, originStack, reduce, parseNodes);
                }
            }
        } else {
            // Fall back to regular (S)GLR
            for(StackPath<ParseForest, ElkhoundStackNode> path : stackManager.findAllPathsOfLength(activeStack,
                reduce.arity()))
                if(throughLink == null || path.contains(throughLink)) {
                    ElkhoundStackNode originStack = path.head();

                    ParseForest[] parseNodes = stackManager.getParseForests(parseForestManager, path);

                    if(!ignoreReducePath(originStack, reduce, parseNodes))
                        reducer(observing, parseState, activeStack, originStack, reduce, parseNodes);
                }
        }
    }

    private void reducerElkhound(
        ParserObserving<ParseForest, Derivation, ParseNode, ElkhoundStackNode, ParseState> observing,
        ParseState parseState, ElkhoundStackNode activeStack, ElkhoundStackNode originStack, IReduce reduce,
        ParseForest[] parseForests) {
        int gotoId = originStack.state.getGotoId(reduce.production().id());
        IState gotoState = parseTable.getState(gotoId);

        observing.notify(observer -> observer.reducerElkhound(originStack, reduce, parseForests));

        ElkhoundStackNode gotoStack =
            reducer.reducerNoExistingStack(observing, parseState, reduce, originStack, gotoState, parseForests);

        parseState.activeStacks.add(gotoStack);
        // We are doing LR and the new active stack is the only one, thus no need to add it to forActorStacks here for
        // further processing

        observing.notify(
            observer -> observer.reducer(parseState, activeStack, originStack, reduce, parseForests, gotoStack));
    }

}
