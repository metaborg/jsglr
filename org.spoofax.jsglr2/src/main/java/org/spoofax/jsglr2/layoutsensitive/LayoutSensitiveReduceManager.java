package org.spoofax.jsglr2.layoutsensitive;

import org.metaborg.parsetable.IParseTable;
import org.metaborg.parsetable.actions.IReduce;
import org.metaborg.sdf2table.grammar.LayoutConstraintAttribute;
import org.metaborg.sdf2table.parsetable.ParseTableProduction;
import org.spoofax.jsglr2.inputstack.IInputStack;
import org.spoofax.jsglr2.parseforest.ParseForestManager;
import org.spoofax.jsglr2.parser.AbstractParseState;
import org.spoofax.jsglr2.reducing.ReduceManager;
import org.spoofax.jsglr2.reducing.ReduceManagerFactory;
import org.spoofax.jsglr2.reducing.ReducerFactory;
import org.spoofax.jsglr2.stack.AbstractStackManager;
import org.spoofax.jsglr2.stack.IStackNode;

public class LayoutSensitiveReduceManager
//@formatter:off
   <StackNode  extends IStackNode,
    InputStack extends IInputStack,
    ParseState extends AbstractParseState<InputStack, StackNode>>
//@formatter:on
    extends
    ReduceManager<ILayoutSensitiveParseForest, ILayoutSensitiveDerivation<ILayoutSensitiveParseForest>, ILayoutSensitiveParseNode<ILayoutSensitiveParseForest, ILayoutSensitiveDerivation<ILayoutSensitiveParseForest>>, StackNode, InputStack, ParseState> {

    private LayoutSensitiveReduceManager(IParseTable parseTable,
        AbstractStackManager<ILayoutSensitiveParseForest, ILayoutSensitiveDerivation<ILayoutSensitiveParseForest>, ILayoutSensitiveParseNode<ILayoutSensitiveParseForest, ILayoutSensitiveDerivation<ILayoutSensitiveParseForest>>, StackNode, ParseState> stackManager,
        ParseForestManager<ILayoutSensitiveParseForest, ILayoutSensitiveDerivation<ILayoutSensitiveParseForest>, ILayoutSensitiveParseNode<ILayoutSensitiveParseForest, ILayoutSensitiveDerivation<ILayoutSensitiveParseForest>>, StackNode, ParseState> parseForestManager,
        ReducerFactory<ILayoutSensitiveParseForest, ILayoutSensitiveDerivation<ILayoutSensitiveParseForest>, ILayoutSensitiveParseNode<ILayoutSensitiveParseForest, ILayoutSensitiveDerivation<ILayoutSensitiveParseForest>>, StackNode, InputStack, ParseState> reducerFactory) {
        super(parseTable, stackManager, parseForestManager, reducerFactory);
    }

    public static
    //@formatter:off
       <StackNode_    extends IStackNode,
        InputStack_   extends IInputStack,
        ParseState_   extends AbstractParseState<InputStack_, StackNode_>,
        StackManager_ extends AbstractStackManager<ILayoutSensitiveParseForest, ILayoutSensitiveDerivation<ILayoutSensitiveParseForest>, ILayoutSensitiveParseNode<ILayoutSensitiveParseForest, ILayoutSensitiveDerivation<ILayoutSensitiveParseForest>>, StackNode_, ParseState_>>
    //@formatter:on
    ReduceManagerFactory<ILayoutSensitiveParseForest, ILayoutSensitiveDerivation<ILayoutSensitiveParseForest>, ILayoutSensitiveParseNode<ILayoutSensitiveParseForest, ILayoutSensitiveDerivation<ILayoutSensitiveParseForest>>, StackNode_, InputStack_, ParseState_, StackManager_, LayoutSensitiveReduceManager<StackNode_, InputStack_, ParseState_>>
        factoryLayoutSensitive(
            ReducerFactory<ILayoutSensitiveParseForest, ILayoutSensitiveDerivation<ILayoutSensitiveParseForest>, ILayoutSensitiveParseNode<ILayoutSensitiveParseForest, ILayoutSensitiveDerivation<ILayoutSensitiveParseForest>>, StackNode_, InputStack_, ParseState_> reducerFactory) {
        return (parseTable, stackManager, parseForestManager) -> new LayoutSensitiveReduceManager<>(parseTable,
            stackManager, parseForestManager, reducerFactory);
    }

    @Override protected boolean ignoreReducePath(StackNode pathBegin, IReduce reduce,
        ILayoutSensitiveParseForest[] parseNodes) {
        return ignoreByLayoutConstraint(reduce, parseNodes);
    }

    public static boolean ignoreByLayoutConstraint(IReduce reduce, ILayoutSensitiveParseForest[] parseNodes) {
        if(reduce.production() instanceof ParseTableProduction) {
            ParseTableProduction sdf2tableProduction = (ParseTableProduction) reduce.production();

            for(LayoutConstraintAttribute lca : sdf2tableProduction.getLayoutConstraints()) {
                // Skip the reduction if the constraint evaluates to false
                if (!lca.ignoreLayout() && !LayoutConstraintEvaluator.evaluate(lca.getLayoutConstraint(), parseNodes)) {
                    return true;
                }
            }
        }

        return false;
    }

}
