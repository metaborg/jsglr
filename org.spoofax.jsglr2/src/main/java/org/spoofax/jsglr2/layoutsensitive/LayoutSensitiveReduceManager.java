package org.spoofax.jsglr2.layoutsensitive;

import org.metaborg.parsetable.IParseTable;
import org.metaborg.parsetable.actions.IReduce;
import org.metaborg.sdf2table.grammar.LayoutConstraintAttribute;
import org.metaborg.sdf2table.parsetable.ParseTableProduction;
import org.spoofax.jsglr2.parseforest.IDerivation;
import org.spoofax.jsglr2.parseforest.ParseForestConstruction;
import org.spoofax.jsglr2.parseforest.ParseForestManager;
import org.spoofax.jsglr2.parser.AbstractParse;
import org.spoofax.jsglr2.parser.IParseState;
import org.spoofax.jsglr2.reducing.ReduceManager;
import org.spoofax.jsglr2.stack.AbstractStackManager;
import org.spoofax.jsglr2.stack.IStackNode;
import org.spoofax.jsglr2.stack.StackLink;
import org.spoofax.jsglr2.stack.paths.StackPath;

public class LayoutSensitiveReduceManager
//@formatter:off
   <ParseForest extends LayoutSensitiveParseForest,
    ParseNode   extends ParseForest,
    Derivation  extends IDerivation<ParseForest>,
    StackNode   extends IStackNode,
    ParseState  extends IParseState<ParseForest, StackNode>,
    Parse       extends AbstractParse<ParseForest, StackNode, ParseState>>
//@formatter:on
    extends ReduceManager<ParseForest, ParseNode, Derivation, StackNode, ParseState, Parse> {

    private LayoutConstraintEvaluator<ParseForest> lce = new LayoutConstraintEvaluator<>();

    public LayoutSensitiveReduceManager(IParseTable parseTable,
        AbstractStackManager<ParseForest, StackNode, ParseState, Parse> stackManager,
        ParseForestManager<ParseForest, ParseNode, Derivation, Parse> parseForestManager,
        ParseForestConstruction parseForestConstruction) {
        super(parseTable, stackManager, parseForestManager, parseForestConstruction);
    }

    @Override protected void doReductionsHelper(Parse parse, StackNode stack, IReduce reduce,
        StackLink<ParseForest, StackNode> throughLink) {
        for(StackPath<ParseForest, StackNode> path : stackManager.findAllPathsOfLength(stack, reduce.arity())) {
            if(throughLink == null || path.contains(throughLink)) {
                StackNode pathBegin = path.head();
                ParseForest[] parseNodes = stackManager.getParseForests(parseForestManager, path);

                boolean skipReduce = false;

                if(reduce.production() instanceof ParseTableProduction) {
                    ParseTableProduction sdf2tableProduction = (ParseTableProduction) reduce.production();

                    for(LayoutConstraintAttribute lca : sdf2tableProduction.getLayoutConstraints()) {
                        // Skip the reduction if the constraint evaluates to false or if it is not present
                        if(!lce.evaluate(lca.getLayoutConstraint(), parseNodes).orElse(false)) {
                            skipReduce = true;
                            break;
                        }
                    }
                }

                if(skipReduce) {
                    continue;
                }

                reducer(parse, pathBegin, reduce, parseNodes);
            }
        }
    }

}
