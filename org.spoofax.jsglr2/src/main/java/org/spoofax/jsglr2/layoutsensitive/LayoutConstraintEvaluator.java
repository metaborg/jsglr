package org.spoofax.jsglr2.layoutsensitive;

import java.util.Optional;

import org.metaborg.sdf2table.grammar.layoutconstraints.ArithmeticLayoutConstraint;
import org.metaborg.sdf2table.grammar.layoutconstraints.BooleanLayoutConstraint;
import org.metaborg.sdf2table.grammar.layoutconstraints.ComparisonLayoutConstraint;
import org.metaborg.sdf2table.grammar.layoutconstraints.ConstraintElement;
import org.metaborg.sdf2table.grammar.layoutconstraints.ILayoutConstraint;
import org.metaborg.sdf2table.grammar.layoutconstraints.NumericLayoutConstraint;

public class LayoutConstraintEvaluator {

    public static <ParseForest extends ILayoutSensitiveParseForest> Optional<Boolean>
        evaluate(ILayoutConstraint layoutConstraint, ParseForest[] parseNodes) {
        if(layoutConstraint instanceof BooleanLayoutConstraint) {
            BooleanLayoutConstraint booleanLayoutConstraint = (BooleanLayoutConstraint) layoutConstraint;

            Optional<Boolean> c1ResultOpt = evaluate(booleanLayoutConstraint.getC1(), parseNodes);

            boolean c1Result, c2Result;

            if(!c1ResultOpt.isPresent()) {
                c1Result = true;
            } else {
                c1Result = c1ResultOpt.get();
            }

            switch(booleanLayoutConstraint.getOp()) {
                case AND:
                case OR:
                    Optional<Boolean> c2ResultOpt = evaluate(booleanLayoutConstraint.getC2(), parseNodes);

                    if(!c2ResultOpt.isPresent()) {
                        c2Result = true;
                    } else {
                        c2Result = c2ResultOpt.get();
                    }

                    switch(booleanLayoutConstraint.getOp()) {
                        case AND:
                            return Optional.of(c1Result && c2Result);
                        case OR:
                            return Optional.of(c1Result || c2Result);
                    }

                case NOT:
                    return Optional.of(!c1Result);
            }

        }

        if(layoutConstraint instanceof ComparisonLayoutConstraint) {
            ComparisonLayoutConstraint comparisonLayoutConstraint = (ComparisonLayoutConstraint) layoutConstraint;

            Optional<Integer> c1ResultOpt = evaluateNumeric(comparisonLayoutConstraint.getC1(), parseNodes);
            Optional<Integer> c2ResultOpt = evaluateNumeric(comparisonLayoutConstraint.getC2(), parseNodes);

            if(!c1ResultOpt.isPresent() || !c2ResultOpt.isPresent())
                return Optional.empty();
            else {
                int c1Result = c1ResultOpt.get();
                int c2Result = c2ResultOpt.get();

                switch(comparisonLayoutConstraint.getOp()) {
                    case EQ:
                        return Optional.of(c1Result == c2Result);
                    case GE:
                        return Optional.of(c1Result >= c2Result);
                    case GT:
                        return Optional.of(c1Result > c2Result);
                    case LE:
                        return Optional.of(c1Result <= c2Result);
                    case LT:
                        return Optional.of(c1Result < c2Result);
                }
            }
        }

        throw new IllegalStateException("Could not evaluate constraint: " + layoutConstraint);
    }

    private static <ParseForest extends ILayoutSensitiveParseForest> Optional<Integer>
        evaluateNumeric(ILayoutConstraint layoutConstraint, ParseForest[] parseNodes) {
        if(layoutConstraint instanceof NumericLayoutConstraint) {
            NumericLayoutConstraint numericLayoutConstraint = (NumericLayoutConstraint) layoutConstraint;
            
            if (numericLayoutConstraint.getElem() == null && numericLayoutConstraint.getToken() == null) {
                return Optional.of(numericLayoutConstraint.getTree());
            }

            ParseForest tree = parseNodes[numericLayoutConstraint.getTree()];

            if(tree instanceof LayoutSensitiveParseNode
                && ((ILayoutSensitiveParseNode) tree).production().isIgnoreLayoutConstraint()) {
                return Optional.empty();
            }
            
            if(tree.getStartPosition() == tree.getEndPosition()) {
                return Optional.empty();
            }
            
            switch(numericLayoutConstraint.getToken()) {
                case FIRST:
                    if(numericLayoutConstraint.getElem() == ConstraintElement.COL) {
                        return Optional.of(tree.getStartPosition().column);
                    } else {
                        return Optional.of(tree.getStartPosition().line);
                    }
                case LAST:
                    if(numericLayoutConstraint.getElem() == ConstraintElement.COL) {
                        return Optional.of(tree.getEndPosition().column);
                    } else {
                        return Optional.of(tree.getEndPosition().line);
                    }
                case LEFT:
                    if(tree instanceof ILayoutSensitiveParseNode) {
                        ILayoutSensitiveParseNode<ILayoutSensitiveParseForest, ILayoutSensitiveDerivation<ILayoutSensitiveParseForest>> layoutSensitiveParseNode =
                            (ILayoutSensitiveParseNode<ILayoutSensitiveParseForest, ILayoutSensitiveDerivation<ILayoutSensitiveParseForest>>) tree;

                        if(layoutSensitiveParseNode.getLeftPosition() == null) {
                            return Optional.empty();
                        } else if(numericLayoutConstraint.getElem() == ConstraintElement.COL) {
                            return Optional.of(layoutSensitiveParseNode.getLeftPosition().column);
                        } else {
                            return Optional.of(layoutSensitiveParseNode.getLeftPosition().line);
                        }
                    }
                    return Optional.empty();
                case RIGHT:
                    // TODO implement this
                    return Optional.empty();
            }
        }

        if(layoutConstraint instanceof ArithmeticLayoutConstraint) {
            ArithmeticLayoutConstraint arithmeticLayoutConstraint = (ArithmeticLayoutConstraint) layoutConstraint;

            Optional<Integer> c1ResultOpt = evaluateNumeric(arithmeticLayoutConstraint.getC1(), parseNodes);
            Optional<Integer> c2ResultOpt = evaluateNumeric(arithmeticLayoutConstraint.getC2(), parseNodes);

            if(!c1ResultOpt.isPresent() || !c2ResultOpt.isPresent())
                return Optional.empty();
            else {
                int c1Result = c1ResultOpt.get();
                int c2Result = c2ResultOpt.get();

                switch(arithmeticLayoutConstraint.getOp()) {
                    case ADD:
                        return Optional.of(c1Result + c2Result);
                    case DIV:
                        return Optional.of(c1Result / c2Result);
                    case MUL:
                        return Optional.of(c1Result * c2Result);
                    case SUB:
                        return Optional.of(c1Result - c2Result);
                }
            }
        }

        throw new IllegalStateException("Could not evaluate constraint: " + layoutConstraint);
    }

}
