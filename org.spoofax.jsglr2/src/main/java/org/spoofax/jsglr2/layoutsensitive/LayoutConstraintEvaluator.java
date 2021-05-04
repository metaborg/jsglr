package org.spoofax.jsglr2.layoutsensitive;

import java.util.Optional;

import org.metaborg.sdf2table.grammar.layoutconstraints.ArithmeticLayoutConstraint;
import org.metaborg.sdf2table.grammar.layoutconstraints.BooleanLayoutConstraint;
import org.metaborg.sdf2table.grammar.layoutconstraints.ComparisonLayoutConstraint;
import org.metaborg.sdf2table.grammar.layoutconstraints.ILayoutConstraint;
import org.metaborg.sdf2table.grammar.layoutconstraints.ILayoutConstraintExpression;
import org.metaborg.sdf2table.grammar.layoutconstraints.NumericLayoutConstraint;
import org.metaborg.sdf2table.grammar.layoutconstraints.TreeRef;
import org.spoofax.jsglr2.parser.Position;

public class LayoutConstraintEvaluator {
    /**
     * Evaluates a layout constraint attribute on a given parse forest.
     * @param <ParseForest>
     * @param layoutConstraint
     * @param parseNodes
     * @return false when the constraint does not hold, true otherwise
     */
    public static <ParseForest extends ILayoutSensitiveParseForest> boolean
        evaluate(ILayoutConstraint layoutConstraint, ParseForest[] parseNodes) {
        if (layoutConstraint instanceof BooleanLayoutConstraint) {
            return evaluateBoolean((BooleanLayoutConstraint) layoutConstraint, parseNodes);
        }

        if (layoutConstraint instanceof ComparisonLayoutConstraint) {
            return evaluateComparison((ComparisonLayoutConstraint) layoutConstraint, parseNodes);
        }

        throw new IllegalStateException("Could not evaluate constraint: " + layoutConstraint);
    }

    private static <ParseForest extends ILayoutSensitiveParseForest> boolean
        evaluateBoolean(BooleanLayoutConstraint booleanConstraint, ParseForest[] parseNodes) {
        final boolean c1Result = evaluate(booleanConstraint.getC1(), parseNodes);

        switch (booleanConstraint.getOp()) {
            case AND:
                return c1Result && evaluate(booleanConstraint.getC2(), parseNodes);
            case OR:
                return c1Result || evaluate(booleanConstraint.getC2(), parseNodes);
            case NOT:
                return !c1Result;
           default:
               throw new IllegalStateException("Could not evaluate boolean constraint: " + booleanConstraint);
        }
    }

    private static <ParseForest extends ILayoutSensitiveParseForest> boolean
        evaluateComparison(ComparisonLayoutConstraint comparisonConstraint, ParseForest[] parseNodes) {
        final Optional<Integer> c1ResultOpt = evaluateExpression(comparisonConstraint.getC1(), parseNodes);
        final Optional<Integer> c2ResultOpt = evaluateExpression(comparisonConstraint.getC2(), parseNodes);

        if (!c1ResultOpt.isPresent() || !c2ResultOpt.isPresent()) {
            return true;
        } else {
            final int c1Result = c1ResultOpt.get();
            final int c2Result = c2ResultOpt.get();

            switch(comparisonConstraint.getOp()) {
                case EQ:
                    return c1Result == c2Result;
                case GE:
                    return c1Result >= c2Result;
                case GT:
                    return c1Result > c2Result;
                case LE:
                    return c1Result <= c2Result;
                case LT:
                    return c1Result < c2Result;
                default:
                    throw new IllegalStateException("Could not evaluate comparison constraint: " + comparisonConstraint);
            }
        }
    }

    private static <ParseForest extends ILayoutSensitiveParseForest> Optional<Integer>
        evaluateExpression(ILayoutConstraintExpression layoutConstraint, ParseForest[] parseNodes) {
        if (layoutConstraint instanceof NumericLayoutConstraint) {
            final NumericLayoutConstraint numericLayoutConstraint = (NumericLayoutConstraint) layoutConstraint;
            return Optional.of(numericLayoutConstraint.getNum());
        }

        if (layoutConstraint instanceof TreeRef) {
            return evaluateTreeRef((TreeRef) layoutConstraint, parseNodes);
        }

        if (layoutConstraint instanceof ArithmeticLayoutConstraint) {
            return evaluateArithmetic((ArithmeticLayoutConstraint) layoutConstraint, parseNodes);
        }

        throw new IllegalStateException("Could not evaluate constraint expression: " + layoutConstraint);
    }

    private static <ParseForest extends ILayoutSensitiveParseForest> Optional<Integer>
        evaluateTreeRef(TreeRef treeRefConstraint, ParseForest[] parseNodes) {
        final ParseForest tree = parseNodes[treeRefConstraint.getTree()];

        if (tree instanceof LayoutSensitiveParseNode
            && ((ILayoutSensitiveParseNode) tree).production().isIgnoreLayoutConstraint()) {
            return Optional.empty();
        }

        if (tree.getStartPosition() == tree.getEndPosition()) {
            return Optional.empty();
        }

        Position pos;
        switch (treeRefConstraint.getToken()) {
            case FIRST:
                pos = tree.getStartPosition();
                break;
            case LAST:
                pos = tree.getEndPosition();
                break;
            case LEFT:
                if (tree instanceof ILayoutSensitiveParseNode) {
                    ILayoutSensitiveParseNode<ILayoutSensitiveParseForest, ILayoutSensitiveDerivation<ILayoutSensitiveParseForest>> layoutSensitiveParseNode =
                        (ILayoutSensitiveParseNode<ILayoutSensitiveParseForest, ILayoutSensitiveDerivation<ILayoutSensitiveParseForest>>) tree;

                    if (layoutSensitiveParseNode.getLeftPosition() == null) {
                        return Optional.empty();
                    } else {
                        pos = layoutSensitiveParseNode.getLeftPosition();
                        break;
                    }
                } else {
                    return Optional.empty();
                }
            case RIGHT:
                if (tree instanceof ILayoutSensitiveParseNode) {
                    ILayoutSensitiveParseNode<ILayoutSensitiveParseForest, ILayoutSensitiveDerivation<ILayoutSensitiveParseForest>> layoutSensitiveParseNode =
                        (ILayoutSensitiveParseNode<ILayoutSensitiveParseForest, ILayoutSensitiveDerivation<ILayoutSensitiveParseForest>>) tree;

                    if (layoutSensitiveParseNode.getRightPosition() == null) {
                        return Optional.empty();
                    } else {
                        pos = layoutSensitiveParseNode.getRightPosition();
                        break;
                    }
                } else {
                    return Optional.empty();
                }
            default:
                throw new IllegalStateException("Could not evaluate TreeRef token: " + treeRefConstraint.getToken());
        }

        switch (treeRefConstraint.getElem()) {
            case COL:
                return Optional.of(pos.column);
            case LINE:
                return Optional.of(pos.line);
            default:
                throw new IllegalStateException("Could not evaluate TreeRef element: " + treeRefConstraint.getElem());
        }
    }

    private static <ParseForest extends ILayoutSensitiveParseForest> Optional<Integer>
        evaluateArithmetic(ArithmeticLayoutConstraint arithmeticConstraint, ParseForest[] parseNodes) {
        final Optional<Integer> c1ResultOpt = evaluateExpression(arithmeticConstraint.getC1(), parseNodes);
        final Optional<Integer> c2ResultOpt = evaluateExpression(arithmeticConstraint.getC2(), parseNodes);

        if (!c1ResultOpt.isPresent() || !c2ResultOpt.isPresent()) {
            return Optional.empty();
        } else {
            final int c1Result = c1ResultOpt.get();
            final int c2Result = c2ResultOpt.get();

            switch (arithmeticConstraint.getOp()) {
                case ADD:
                    return Optional.of(c1Result + c2Result);
                case DIV:
                    return Optional.of(c1Result / c2Result);
                case MUL:
                    return Optional.of(c1Result * c2Result);
                case SUB:
                    return Optional.of(c1Result - c2Result);
                default:
                    throw new IllegalStateException("Could not evaluate arithmetic constraint: " + arithmeticConstraint);
            }
        }
    }
}
