package org.spoofax.jsglr2.layoutsensitive;

import org.metaborg.parsetable.IParseTable;
import org.metaborg.parsetable.IProduction;
import org.metaborg.parsetable.actions.IReduce;
import org.metaborg.sdf2table.grammar.LayoutConstraintNewAttribute;
import org.metaborg.sdf2table.grammar.LayoutConstraintType;
import org.metaborg.sdf2table.parsetable.ParseTableProduction;
import org.spoofax.jsglr2.parseforest.AbstractParseForest;
import org.spoofax.jsglr2.parseforest.ParseForestConstruction;
import org.spoofax.jsglr2.parseforest.ParseForestManager;
import org.spoofax.jsglr2.parser.Parse;
import org.spoofax.jsglr2.reducing.ReduceManager;
import org.spoofax.jsglr2.stack.AbstractStackNode;
import org.spoofax.jsglr2.stack.StackLink;
import org.spoofax.jsglr2.stack.StackManager;
import org.spoofax.jsglr2.stack.paths.StackPath;

public class LayoutSensitiveReduceManager<ParseForest extends AbstractParseForest, ParseNode extends ParseForest, Derivation, StackNode extends AbstractStackNode<ParseForest>>
    extends ReduceManager<ParseForest, ParseNode, Derivation, StackNode> {


    public LayoutSensitiveReduceManager(IParseTable parseTable, StackManager<ParseForest, StackNode> stackManager,
        ParseForestManager<ParseForest, ParseNode, Derivation> parseForestManager,
        ParseForestConstruction parseForestConstruction) {
        super(parseTable, stackManager, parseForestManager, parseForestConstruction);
    }

    @Override protected void doReductionsHelper(Parse<ParseForest, StackNode> parse, StackNode stack, IReduce reduce,
        StackLink<ParseForest, StackNode> throughLink) {
        for(StackPath<ParseForest, StackNode> path : stackManager.findAllPathsOfLength(stack, reduce.arity())) {
            if(throughLink == null || path.contains(throughLink)) {
                StackNode pathBegin = path.head();
                ParseForest[] parseNodes = stackManager.getParseForests(parseForestManager, path);

                boolean skipReduce = false;

                IProduction prod = reduce.production();
                if(prod instanceof ParseTableProduction) {
                    if(!((ParseTableProduction) prod).getLayoutConstraints().isEmpty()) {
                        for(LayoutConstraintNewAttribute lc : ((ParseTableProduction) prod).getLayoutConstraints()) {
                            if(!lc.isNoLayoutConstraint() && evaluateLayoutConstraint(lc, parseNodes) == false) {
                                skipReduce = true;
                                break;
                            }
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

    private boolean evaluateLayoutConstraint(LayoutConstraintNewAttribute lc, ParseForest[] parseNodes) {
        int exp1, exp2;
        // FIXME line and column of the parent might not be the line and column of the first node
        // similarly, end line and end column of the parent might not be the line and column of the last node
                
        int idxExp1 = lc.getIndexExp1() == -1 ? 0 : lc.getIndexExp1();
        
        // if the production used to construct the tree ignores layout constraints, the constraint involving the tree should be ignored
        if(parseNodes[idxExp1] instanceof LayoutSensitiveSymbolNode) {
            if(((LayoutSensitiveSymbolNode) parseNodes[idxExp1]).getProduction().isIgnoreLayoutConstraint()) {
                return true;
            }
        }

        switch(lc.getTypeExp1()) {
            default:
            case Line:
                exp1 = parseNodes[idxExp1].startPosition.line;
                break;
            case Column:
                exp1 = parseNodes[idxExp1].startPosition.column;
                break;
            case EndLine:
                exp1 = parseNodes[parseNodes.length-1].endPosition.line;
                break;
            case EndColumn:
                exp1 = parseNodes[parseNodes.length-1].endPosition.column;
                break;
        }

        int idxExp2 = lc.getIndexExp2() == -1 ? 0 : lc.getIndexExp2();
        
        if(parseNodes[idxExp2] instanceof LayoutSensitiveSymbolNode) {
            if(((LayoutSensitiveSymbolNode) parseNodes[idxExp2]).getProduction().isIgnoreLayoutConstraint()) {
                return true;
            }
        }

        switch(lc.getTypeExp2()) {
            default:
            case Line:
                exp2 = parseNodes[idxExp2].startPosition.line;
                break;
            case Column:
                exp2 = parseNodes[idxExp2].startPosition.column;
                break;
            case EndLine:
                exp2 = parseNodes[parseNodes.length-1].endPosition.line;
                break;
            case EndColumn:
                exp2 = parseNodes[parseNodes.length-1].endPosition.column;
                break;
        }

        switch(lc.getOperator()) {
            case "<":
                return exp1 < exp2;
            case "<=":
                return exp1 <= exp2;
            case ">":
                return exp1 > exp2;
            case ">=":
                return exp1 >= exp2;
            default:
            case "==":
                return exp1 == exp2;
        }
    }

}
