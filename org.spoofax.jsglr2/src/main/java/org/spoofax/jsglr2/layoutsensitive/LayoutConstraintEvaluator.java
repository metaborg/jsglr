package org.spoofax.jsglr2.layoutsensitive;

import org.metaborg.sdf2table.grammar.layoutconstraints.*;

public class LayoutConstraintEvaluator<ParseForest extends LayoutSensitiveParseForest> {

    public boolean evaluate(ILayoutConstraint layoutConstraint, ParseForest[] parseNodes) throws Exception {
        boolean noValue = false;

        try {
            if(layoutConstraint instanceof BooleanLayoutConstraint) {
                BooleanLayoutConstraint booleanLayoutConstraint = (BooleanLayoutConstraint) layoutConstraint;

                boolean c1Result = evaluate(booleanLayoutConstraint.getC1(), parseNodes);

                switch(booleanLayoutConstraint.getOp()) {
                    case AND:
                    case OR:
                        boolean c2Result = evaluate(booleanLayoutConstraint.getC2(), parseNodes);

                        switch(booleanLayoutConstraint.getOp()) {
                            case AND:
                                return c1Result && c2Result;
                            case OR:
                                return c1Result || c2Result;
                        }
                    case NOT:
                        return !c1Result;
                }
            }

            if(layoutConstraint instanceof ComparisonLayoutConstraint) {
                ComparisonLayoutConstraint comparisonLayoutConstraint = (ComparisonLayoutConstraint) layoutConstraint;

                int c1Result = evaluateNumeric(comparisonLayoutConstraint.getC1(), parseNodes);
                int c2Result = evaluateNumeric(comparisonLayoutConstraint.getC2(), parseNodes);

                switch(comparisonLayoutConstraint.getOp()) {
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
                }
            }
        } catch(NoValueLayoutException e) {
            noValue = true;
        }

        if(noValue) {
            return true;
        } else {
            throw new Exception("Could not evaluate constraint: " + layoutConstraint);
        }
    }

    private int evaluateNumeric(ILayoutConstraint layoutConstraint, ParseForest[] parseNodes) throws Exception {
        if(layoutConstraint instanceof NumericLayoutConstraint) {
            NumericLayoutConstraint numericLayoutConstraint = (NumericLayoutConstraint) layoutConstraint;

            ParseForest tree = parseNodes[numericLayoutConstraint.getTree()];

            if(tree instanceof LayoutSensitiveParseNode
                && ((LayoutSensitiveParseNode) tree).production().isIgnoreLayoutConstraint()) {
                throw new NoValueLayoutException();
            }

            switch(numericLayoutConstraint.getToken()) {
                case FIRST:
                    if(numericLayoutConstraint.getElem() == ConstraintElement.COL) {
                        return tree.getStartPosition().column;
                    } else {
                        return tree.getStartPosition().line;
                    }
                case LAST:
                    if(numericLayoutConstraint.getElem() == ConstraintElement.COL) {
                        return tree.getEndPosition().column;
                    } else {
                        return tree.getEndPosition().line;
                    }
                case LEFT:
                    if(tree instanceof LayoutSensitiveParseNode) {
                        if(((LayoutSensitiveParseNode) tree).getFirstDerivation().leftPosition == null) {
                            throw new NoValueLayoutException();
                        } else if(numericLayoutConstraint.getElem() == ConstraintElement.COL) {
                            return ((LayoutSensitiveParseNode) tree).getFirstDerivation().leftPosition.column;
                        } else {
                            return ((LayoutSensitiveParseNode) tree).getFirstDerivation().leftPosition.line;
                        }
                    }
                    throw new NoValueLayoutException();
                case RIGHT:
                    // TODO implement this
                    throw new NoValueLayoutException();
            }
        }

        if(layoutConstraint instanceof ArithmeticLayoutConstraint) {
            ArithmeticLayoutConstraint arithmeticLayoutConstraint = (ArithmeticLayoutConstraint) layoutConstraint;

            int c1Result = evaluateNumeric(((ArithmeticLayoutConstraint) layoutConstraint).getC1(), parseNodes);
            int c2Result = evaluateNumeric(((ArithmeticLayoutConstraint) layoutConstraint).getC2(), parseNodes);

            switch(arithmeticLayoutConstraint.getOp()) {
                case ADD:
                    return c1Result + c2Result;
                case DIV:
                    return c1Result / c2Result;
                case MUL:
                    return c1Result * c2Result;
                case SUB:
                    return c1Result - c2Result;
            }
        }

        throw new Exception("Could not evaluate constraint: " + layoutConstraint);
    }

}
