package org.spoofax.jsglr2.integrationtest.features;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.spoofax.jsglr.client.imploder.IToken;
import org.spoofax.jsglr2.integrationtest.BaseTestWithSdf3ParseTables;
import org.spoofax.jsglr2.integrationtest.TokenDescriptor;
import org.spoofax.terms.ParseError;

import java.util.Arrays;
import java.util.stream.Stream;

public class TokenizationTest extends BaseTestWithSdf3ParseTables {

    public TokenizationTest() {
        super("tokenization.sdf3");
    }

    @TestFactory public Stream<DynamicTest> number() throws ParseError {
        return testTokens("1", Arrays.asList(
        //@formatter:off
            new TokenDescriptor("1", IToken.TK_NUMBER, 0, 1, 1, "NUMBER", null)
        //@formatter:on
        ));
    }

    @TestFactory public Stream<DynamicTest> identifier() throws ParseError {
        return testTokens("x", Arrays.asList(
        //@formatter:off
            new TokenDescriptor("x", IToken.TK_IDENTIFIER, 0, 1, 1, "ID", null)
        //@formatter:on
        ));
    }

    @TestFactory public Stream<DynamicTest> operator() throws ParseError {
        return testTokens("x+x", Arrays.asList(
        //@formatter:off
            new TokenDescriptor("x", IToken.TK_IDENTIFIER, 0, 1, 1, "ID", null),
            new TokenDescriptor("+", IToken.TK_OPERATOR,   1, 1, 2, "Exp", "AddOperator"),
            new TokenDescriptor("x", IToken.TK_IDENTIFIER, 2, 1, 3, "ID", null)
        //@formatter:on
        ));
    }

    @TestFactory public Stream<DynamicTest> operatorWithLayout() throws ParseError {
        return testTokens("x + x", Arrays.asList(
        //@formatter:off
            new TokenDescriptor("x", IToken.TK_IDENTIFIER, 0, 1, 1, "ID", null),
            new TokenDescriptor(" ", IToken.TK_LAYOUT,     1, 1, 2, "Exp", "AddOperator"),
            new TokenDescriptor("+", IToken.TK_OPERATOR,   2, 1, 3, "Exp", "AddOperator"),
            new TokenDescriptor(" ", IToken.TK_LAYOUT,     3, 1, 4, "Exp", "AddOperator"),
            new TokenDescriptor("x", IToken.TK_IDENTIFIER, 4, 1, 5, "ID", null)
        //@formatter:on
        ));
    }

    @TestFactory public Stream<DynamicTest> operatorWithLayout2() throws ParseError {
        return testTokens(" x + x ", Arrays.asList(
        //@formatter:off
            new TokenDescriptor(" ", IToken.TK_LAYOUT,     0, 1, 1, null, null),
            new TokenDescriptor("x", IToken.TK_IDENTIFIER, 1, 1, 2, "ID", null),
            new TokenDescriptor(" ", IToken.TK_LAYOUT,     2, 1, 3, "Exp", "AddOperator"),
            new TokenDescriptor("+", IToken.TK_OPERATOR,   3, 1, 4, "Exp", "AddOperator"),
            new TokenDescriptor(" ", IToken.TK_LAYOUT,     4, 1, 5, "Exp", "AddOperator"),
            new TokenDescriptor("x", IToken.TK_IDENTIFIER, 5, 1, 6, "ID", null),
            new TokenDescriptor(" ", IToken.TK_LAYOUT,     6, 1, 7, null, null)
        //@formatter:on
        ));
    }

    @TestFactory public Stream<DynamicTest> keywordWithLayout() throws ParseError {
        return testTokens("x add x", Arrays.asList(
        //@formatter:off
            new TokenDescriptor("x",   IToken.TK_IDENTIFIER, 0, 1, 1, "ID", null),
            new TokenDescriptor(" ",   IToken.TK_LAYOUT,     1, 1, 2, "Exp", "AddKeyword"),
            new TokenDescriptor("add", IToken.TK_KEYWORD,    2, 1, 3, "Exp", "AddKeyword"),
            new TokenDescriptor(" ",   IToken.TK_LAYOUT,     5, 1, 6, "Exp", "AddKeyword"),
            new TokenDescriptor("x",   IToken.TK_IDENTIFIER, 6, 1, 7, "ID", null)
        //@formatter:on
        ));
    }

    @TestFactory public Stream<DynamicTest> keywordWithNumber() throws ParseError {
        return testTokens("x add2 x", Arrays.asList(
        //@formatter:off
            new TokenDescriptor("x",    IToken.TK_IDENTIFIER, 0, 1, 1, "ID", null),
            new TokenDescriptor(" ",    IToken.TK_LAYOUT,     1, 1, 2, "Exp", "Add2Keyword"),
            new TokenDescriptor("add2", IToken.TK_KEYWORD,    2, 1, 3, "Exp", "Add2Keyword"),
            new TokenDescriptor(" ",    IToken.TK_LAYOUT,     6, 1, 7, "Exp", "Add2Keyword"),
            new TokenDescriptor("x",    IToken.TK_IDENTIFIER, 7, 1, 8, "ID", null)
        //@formatter:on
        ));
    }

    @TestFactory public Stream<DynamicTest> multipleLines() throws ParseError {
        return testTokens("x;\nx", Arrays.asList(
        //@formatter:off
            new TokenDescriptor("x",  IToken.TK_IDENTIFIER, 0, 1, 1, "ID", null),
            new TokenDescriptor(";",  IToken.TK_OPERATOR,   1, 1, 2, null, null),
            new TokenDescriptor("\n", IToken.TK_LAYOUT,     2, 1, 3, null, null),
            new TokenDescriptor("x",  IToken.TK_IDENTIFIER, 3, 2, 1, "ID", null)
        //@formatter:on
        ));
    }

    @TestFactory public Stream<DynamicTest> multipleLinesNewlineEnd() throws ParseError {
        return testTokens("x;\nx\n", Arrays.asList(
        //@formatter:off
            new TokenDescriptor("x",  IToken.TK_IDENTIFIER, 0, 1, 1, "ID", null),
            new TokenDescriptor(";",  IToken.TK_OPERATOR,   1, 1, 2, null, null),
            new TokenDescriptor("\n", IToken.TK_LAYOUT,     2, 1, 3, null, null),
            new TokenDescriptor("x",  IToken.TK_IDENTIFIER, 3, 2, 1, "ID", null),
            new TokenDescriptor("\n", IToken.TK_LAYOUT,     4, 2, 2, null, null)
        //@formatter:on
        ));
    }

}
