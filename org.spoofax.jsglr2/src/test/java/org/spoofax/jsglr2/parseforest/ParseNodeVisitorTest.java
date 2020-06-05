package org.spoofax.jsglr2.parseforest;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.spoofax.jsglr2.parseforest.mock.MockParseForest.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.spoofax.jsglr2.JSGLR2Request;
import org.spoofax.jsglr2.parseforest.mock.MockCharacterNode;
import org.spoofax.jsglr2.parseforest.mock.MockDerivation;
import org.spoofax.jsglr2.parseforest.mock.MockParseForest;
import org.spoofax.jsglr2.parseforest.mock.MockParseNode;
import org.spoofax.jsglr2.parser.Position;

public class ParseNodeVisitorTest {

    ParseNodeVisiting<MockParseForest, MockDerivation, MockParseNode> mockParseNodeVisiting =
        new ParseNodeVisiting<MockParseForest, MockDerivation, MockParseNode>() {};

    private void visit(MockParseForest parseForest, String yield,
        ParseNodeVisitor<MockParseForest, MockDerivation, MockParseNode> visitor) {
        JSGLR2Request request = new JSGLR2Request(yield);

        mockParseNodeVisiting.visit(request, parseForest, visitor);
    }

    private Iterable<Visit> trace(MockParseForest parseForest, String yield) {
        List<Visit> visits = new ArrayList<>();

        visit(parseForest, yield, new ParseNodeVisitor<MockParseForest, MockDerivation, MockParseNode>() {
            @Override public boolean preVisit(MockParseNode parseNode, Position startPosition) {
                visits.add(new PreVisit(parseNode.label, startPosition));

                return true;
            }

            @Override public void postVisit(MockParseNode parseNode, Position startPosition, Position endPosition) {
                visits.add(new PostVisit(parseNode.label, startPosition, endPosition));
            }
        });

        return visits;
    }

    static abstract class Visit {
        String label;
        Position startPosition;

        Visit(String label, Position startPosition) {
            this.label = label;
            this.startPosition = startPosition;
        }
    }

    static class PreVisit extends Visit {

        PreVisit(String label, Position startPosition) {
            super(label, startPosition);
        }

        PreVisit(String label, int startOffset, int startLine, int startColumn) {
            this(label, new Position(startOffset, startLine, startColumn));
        }

        @Override public boolean equals(Object o) {
            if(this == o)
                return true;
            if(o == null || getClass() != o.getClass())
                return false;
            PreVisit visit = (PreVisit) o;
            return label.equals(visit.label) && startPosition.equals(visit.startPosition);
        }

        @Override public String toString() {
            return label + ": " + startPosition;
        }
    }

    static class PostVisit extends Visit {
        Position endPosition;

        PostVisit(String label, Position startPosition, Position endPosition) {
            super(label, startPosition);
            this.endPosition = endPosition;
        }

        PostVisit(String label, int startOffset, int startLine, int startColumn, int endOffset, int endLine,
            int endColumn) {
            this(label, new Position(startOffset, startLine, startColumn), new Position(endOffset, endLine, endColumn));
        }

        @Override public boolean equals(Object o) {
            if(this == o)
                return true;
            if(o == null || getClass() != o.getClass())
                return false;
            PostVisit visit = (PostVisit) o;
            return label.equals(visit.label) && startPosition.equals(visit.startPosition)
                && endPosition.equals(visit.endPosition);
        }

        @Override public String toString() {
            return label + ": " + startPosition + "/" + endPosition;
        }
    }

    void testByTrace(MockParseForest parseForest, String yield, Iterable<Visit> expectedVisits) {
        assertIterableEquals(expectedVisits, trace(parseForest, yield), "Visits don't match");
    }

    @Test void testSingle() {
        MockParseForest parseForest = n("x", c('x'));

        testByTrace(parseForest, "x", Arrays.asList(
        //@formatter:off
            new  PreVisit("x", 0, 1, 1),
            new PostVisit("x", 0, 1, 1, 1, 1, 2)
        //@formatter:on
        ));
    }

    @Test void testMultiple() {
        MockParseForest parseForest = n("+", c('x'), c('+'), c('y'));

        testByTrace(parseForest, "x+y", Arrays.asList(
        //@formatter:off
            new  PreVisit("+", 0, 1, 1),
            new PostVisit("+", 0, 1, 1, 3, 1, 4)
        //@formatter:on
        ));
    }

    @Test void testNested() {
        MockParseForest parseForest = n("z", n("y", c('x')));

        testByTrace(parseForest, "x", Arrays.asList(
        //@formatter:off
            new  PreVisit("z", 0, 1, 1),
            new  PreVisit("y", 0, 1, 1),
            new PostVisit("y", 0, 1, 1, 1, 1, 2),
            new PostVisit("z", 0, 1, 1, 1, 1, 2)
        //@formatter:on
        ));
    }

    @Test void testNestedMultiple() {
        MockParseForest parseForest = n("+", n("x", c('x')), c('+'), n("y", c('y')));

        testByTrace(parseForest, "x+y", Arrays.asList(
        //@formatter:off
            new  PreVisit("+", 0, 1, 1),
            new  PreVisit("x", 0, 1, 1),
            new PostVisit("x", 0, 1, 1, 1, 1, 2),
            new  PreVisit("y", 2, 1, 3),
            new PostVisit("y", 2, 1, 3, 3, 1, 4),
            new PostVisit("+", 0, 1, 1, 3, 1, 4)
        //@formatter:on
        ));
    }

    @Test void testAmbiguous() {
        MockParseForest parseForest = n("+", d(n("x1", c('x'))), d(n("x2", c('x'))));

        testByTrace(parseForest, "x", Arrays.asList(
        //@formatter:off
            new  PreVisit("+", 0, 1, 1),
            new  PreVisit("x2", 0, 1, 1),
            new PostVisit("x2", 0, 1, 1, 1, 1, 2),
            new  PreVisit("x1", 0, 1, 1),
            new PostVisit("x1", 0, 1, 1, 1, 1, 2),
            new PostVisit("+", 0, 1, 1, 1, 1, 2)
        //@formatter:on
        ));
    }

    @Test void testAmbiguousExpression() {
        MockParseForest x = n("x", c('x'));
        MockCharacterNode plus = c('+');
        MockParseForest y = n("y", c('y'));
        MockCharacterNode times = c('*');
        MockParseForest z = n("z", c('z'));

        MockParseNode x_plus_y = n("x+y", x, plus, y);
        MockParseNode y_times_z = n("y*z", y, times, z);

        MockDerivation x_plus__y_times_z = d(x, plus, y_times_z); // x + (y * z)
        MockDerivation x_plus_y__times_z = d(x_plus_y, times, z); // (x + y) * z

        MockParseForest parseForest = n("amb", x_plus_y__times_z, x_plus__y_times_z);

        testByTrace(parseForest, "x+y*z", Arrays.asList(
        //@formatter:off
            new  PreVisit("amb", 0, 1, 1),

            // x + (y * z)
            new  PreVisit("x",   0, 1, 1),
            new PostVisit("x",   0, 1, 1, 1, 1, 2),
            new  PreVisit("y*z", 2, 1, 3),
            new  PreVisit("y",   2, 1, 3),
            new PostVisit("y",   2, 1, 3, 3, 1, 4),
            new  PreVisit("z",   4, 1, 5),
            new PostVisit("z",   4, 1, 5, 5, 1, 6),
            new PostVisit("y*z", 2, 1, 3, 5, 1, 6),

            // (x + y) * z
            new  PreVisit("x+y", 0, 1, 1),
            new  PreVisit("x",   0, 1, 1),
            new PostVisit("x",   0, 1, 1, 1, 1, 2),
            new  PreVisit("y",   2, 1, 3),
            new PostVisit("y",   2, 1, 3, 3, 1, 4),
            new PostVisit("x+y", 0, 1, 1, 3, 1, 4),
            new  PreVisit("z",   4, 1, 5),
            new PostVisit("z",   4, 1, 5, 5, 1, 6),

            new PostVisit("amb", 0, 1, 1, 5, 1, 6)
        //@formatter:on
        ));
    }

    @Test void testSkipped() {
        MockParseForest parseForest = n("x", 3);

        testByTrace(parseForest, "xxx", Arrays.asList(
        //@formatter:off
            new  PreVisit("x", 0, 1, 1),
            new PostVisit("x", 0, 1, 1, 3, 1, 4)
        //@formatter:on
        ));
    }

    @Test void testMultipleSkipped() {
        MockParseForest parseForest = n("x", n("y", 3), n("z", 3));

        testByTrace(parseForest, "yyyzzz", Arrays.asList(
        //@formatter:off
            new  PreVisit("x", 0, 1, 1),
            new  PreVisit("y", 0, 1, 1),
            new PostVisit("y", 0, 1, 1, 3, 1, 4),
            new  PreVisit("z", 3, 1, 4),
            new PostVisit("z", 3, 1, 4, 6, 1, 7),
            new PostVisit("x", 0, 1, 1, 6, 1, 7)
        //@formatter:on
        ));
    }

}
