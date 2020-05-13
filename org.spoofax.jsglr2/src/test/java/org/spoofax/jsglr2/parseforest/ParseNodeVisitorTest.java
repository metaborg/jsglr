package org.spoofax.jsglr2.parseforest;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.spoofax.jsglr2.parseforest.mock.MockParseForest.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.spoofax.jsglr2.JSGLR2Request;
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

        visit(parseForest, yield, (parseNode, startPosition, endPosition) -> {
            visits.add(new Visit(parseNode.label, startPosition, endPosition));
        });

        return visits;
    }

    static class Visit {
        String label;
        Position startPosition;
        Position endPosition;

        Visit(String label, Position startPosition, Position endPosition) {
            this.label = label;
            this.startPosition = startPosition;
            this.endPosition = endPosition;
        }

        Visit(String label, int startOffset, int startLine, int startColumn, int endOffset, int endLine,
            int endColumn) {
            this(label, new Position(startOffset, startLine, startColumn), new Position(endOffset, endLine, endColumn));
        }

        @Override public boolean equals(Object o) {
            if(this == o)
                return true;
            if(o == null || getClass() != o.getClass())
                return false;
            Visit visit = (Visit) o;
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
            new Visit("x", 0, 1, 1, 1, 1, 2)
        //@formatter:on
        ));
    }

    @Test void testMultiple() {
        MockParseForest parseForest = n("+", c('x'), c('+'), c('y'));

        testByTrace(parseForest, "x+y", Arrays.asList(
        //@formatter:off
            new Visit("+", 0, 1, 1, 3, 1, 4)
        //@formatter:on
        ));
    }

    @Test void testNested() {
        MockParseForest parseForest = n("z", n("y", c('x')));

        testByTrace(parseForest, "x", Arrays.asList(
        //@formatter:off
            new Visit("y", 0, 1, 1, 1, 1, 2),
            new Visit("z", 0, 1, 1, 1, 1, 2)
        //@formatter:on
        ));
    }

    @Test void testNestedMultiple() {
        MockParseForest parseForest = n("+", n("x", c('x')), c('+'), n("y", c('y')));

        testByTrace(parseForest, "x+y", Arrays.asList(
        //@formatter:off
            new Visit("x", 0, 1, 1, 1, 1, 2),
            new Visit("y", 2, 1, 3, 3, 1, 4),
            new Visit("+", 0, 1, 1, 3, 1, 4)
        //@formatter:on
        ));
    }

    @Test void testAmbiguous() {
        MockParseForest parseForest = n("+", d(n("x1", c('x'))), d(n("x2", c('x'))));

        testByTrace(parseForest, "x", Arrays.asList(
        //@formatter:off
            new Visit("x1", 0, 1, 1, 1, 1, 2),
            new Visit("x2", 0, 1, 1, 1, 1, 2),
            new Visit("+", 0, 1, 1, 1, 1, 2)
        //@formatter:on
        ));
    }

    @Test void testSkipped() {
        MockParseForest parseForest = n("x", 3);

        testByTrace(parseForest, "xxx", Arrays.asList(
        //@formatter:off
            new Visit("x", 0, 1, 1, 3, 1, 4)
        //@formatter:on
        ));
    }

    @Test void testMultipleSkipped() {
        MockParseForest parseForest = n("x", n("y", 3), n("z", 3));

        testByTrace(parseForest, "yyyzzz", Arrays.asList(
        //@formatter:off
            new Visit("y", 0, 1, 1, 3, 1, 4),
            new Visit("z", 3, 1, 4, 6, 1, 7),
            new Visit("x", 0, 1, 1, 6, 1, 7)
        //@formatter:on
        ));
    }

}
