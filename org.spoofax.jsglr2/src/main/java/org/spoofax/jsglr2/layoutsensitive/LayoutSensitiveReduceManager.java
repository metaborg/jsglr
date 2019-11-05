package org.spoofax.jsglr2.layoutsensitive;

import org.metaborg.parsetable.IParseTable;
import org.metaborg.parsetable.actions.IReduce;
import org.metaborg.sdf2table.grammar.LayoutConstraintAttribute;
import org.metaborg.sdf2table.parsetable.ParseTableProduction;
import org.spoofax.jsglr2.parseforest.ParseForestConstruction;
import org.spoofax.jsglr2.parseforest.ParseForestManager;
import org.spoofax.jsglr2.parser.AbstractParseState;
import org.spoofax.jsglr2.parser.ParserVariant;
import org.spoofax.jsglr2.reducing.ReduceManager;
import org.spoofax.jsglr2.reducing.ReduceManagerFactory;
import org.spoofax.jsglr2.stack.AbstractStackManager;
import org.spoofax.jsglr2.stack.IStackNode;

public class LayoutSensitiveReduceManager
//@formatter:off
   <StackNode  extends IStackNode,
    ParseState extends AbstractParseState<ILayoutSensitiveParseForest, StackNode>>
//@formatter:on
    extends
    ReduceManager<ILayoutSensitiveParseForest, ILayoutSensitiveDerivation<ILayoutSensitiveParseForest>, ILayoutSensitiveParseNode<ILayoutSensitiveParseForest, ILayoutSensitiveDerivation<ILayoutSensitiveParseForest>>, StackNode, ParseState> {

    private LayoutSensitiveReduceManager(IParseTable parseTable,
        AbstractStackManager<ILayoutSensitiveParseForest, ILayoutSensitiveDerivation<ILayoutSensitiveParseForest>, ILayoutSensitiveParseNode<ILayoutSensitiveParseForest, ILayoutSensitiveDerivation<ILayoutSensitiveParseForest>>, StackNode, ParseState> stackManager,
        ParseForestManager<ILayoutSensitiveParseForest, ILayoutSensitiveDerivation<ILayoutSensitiveParseForest>, ILayoutSensitiveParseNode<ILayoutSensitiveParseForest, ILayoutSensitiveDerivation<ILayoutSensitiveParseForest>>, StackNode, ParseState> parseForestManager,
        ParseForestConstruction parseForestConstruction) {
        super(parseTable, stackManager, parseForestManager, parseForestConstruction);
    }

    public static
    //@formatter:off
       <StackNode_    extends IStackNode,
        ParseState_   extends AbstractParseState<ILayoutSensitiveParseForest, StackNode_>,
        StackManager_ extends AbstractStackManager<ILayoutSensitiveParseForest, ILayoutSensitiveDerivation<ILayoutSensitiveParseForest>, ILayoutSensitiveParseNode<ILayoutSensitiveParseForest, ILayoutSensitiveDerivation<ILayoutSensitiveParseForest>>, StackNode_, ParseState_>>
    //@formatter:on
    ReduceManagerFactory<ILayoutSensitiveParseForest, ILayoutSensitiveDerivation<ILayoutSensitiveParseForest>, ILayoutSensitiveParseNode<ILayoutSensitiveParseForest, ILayoutSensitiveDerivation<ILayoutSensitiveParseForest>>, StackNode_, ParseState_, StackManager_, LayoutSensitiveReduceManager<StackNode_, ParseState_>>
        factoryLayoutSensitive(ParserVariant parserVariant) {
        return (parseTable, stackManager, parseForestManager) -> new LayoutSensitiveReduceManager<>(parseTable,
            stackManager, parseForestManager, parserVariant.parseForestConstruction);
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
                if(!LayoutConstraintEvaluator.evaluate(lca.getLayoutConstraint(), parseNodes).orElse(true))
                    return true;
            }
        }

        return false;
    }

}
