package org.spoofax.jsglr2.integrationtest.grammars;

import java.util.Arrays;

import org.junit.Test;
import org.spoofax.jsglr.client.imploder.IToken;
import org.spoofax.jsglr2.integrationtest.BaseTestWithSdf3ParseTables;
import org.spoofax.jsglr2.integrationtest.TokenDescriptor;
import org.spoofax.terms.ParseError;

public class TokenizationTest extends BaseTestWithSdf3ParseTables {

    public TokenizationTest() {
        super("tokenization.sdf3");
    }

    @Test public void number() throws ParseError {
        testTokens("1", Arrays.asList(
        //@formatter:off
            new TokenDescriptor("1", IToken.TK_NUMBER, 0, 1, 1, "NUMBER", null)
        //@formatter:on
        ));
    }

    @Test public void identifier() throws ParseError {
        testTokens("x", Arrays.asList(
        //@formatter:off
            new TokenDescriptor("x", IToken.TK_IDENTIFIER, 0, 1, 1, "ID", null)
        //@formatter:on
        ));
    }

    @Test public void operator() throws ParseError {
        testTokens("x+x", Arrays.asList(
        //@formatter:off
            new TokenDescriptor("x", IToken.TK_IDENTIFIER, 0, 1, 1, "ID", null),
            new TokenDescriptor("+", IToken.TK_OPERATOR,   1, 1, 2, "Exp", "AddOperator"),
            new TokenDescriptor("x", IToken.TK_IDENTIFIER, 2, 1, 3, "ID", null)
        //@formatter:on
        ));
    }

    @Test public void operatorWithLayout() throws ParseError {
        testTokens("x + x", Arrays.asList(
        //@formatter:off
            new TokenDescriptor("x", IToken.TK_IDENTIFIER, 0, 1, 1, "ID", null),
            new TokenDescriptor(" ", IToken.TK_LAYOUT,     1, 1, 2, "Exp", "AddOperator"),
            new TokenDescriptor("+", IToken.TK_OPERATOR,   2, 1, 3, "Exp", "AddOperator"),
            new TokenDescriptor(" ", IToken.TK_LAYOUT,     3, 1, 4, "Exp", "AddOperator"),
            new TokenDescriptor("x", IToken.TK_IDENTIFIER, 4, 1, 5, "ID", null)
        //@formatter:on
        ));
    }

    @Test public void operatorWithLayout2() throws ParseError {
        testTokens(" x + x ", Arrays.asList(
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

    @Test public void keywordWithLayout() throws ParseError {
        testTokens("x add x", Arrays.asList(
        //@formatter:off
            new TokenDescriptor("x",   IToken.TK_IDENTIFIER, 0, 1, 1, "ID", null),
            new TokenDescriptor(" ",   IToken.TK_LAYOUT,     1, 1, 2, "Exp", "AddKeyword"),
            new TokenDescriptor("add", IToken.TK_KEYWORD,    2, 1, 3, "Exp", "AddKeyword"),
            new TokenDescriptor(" ",   IToken.TK_LAYOUT,     5, 1, 6, "Exp", "AddKeyword"),
            new TokenDescriptor("x",   IToken.TK_IDENTIFIER, 6, 1, 7, "ID", null)
        //@formatter:on
        ));
    }

    @Test public void keywordWithNumber() throws ParseError {
        testTokens("x add2 x", Arrays.asList(
        //@formatter:off
            new TokenDescriptor("x",    IToken.TK_IDENTIFIER, 0, 1, 1, "ID", null),
            new TokenDescriptor(" ",    IToken.TK_LAYOUT,     1, 1, 2, "Exp", "Add2Keyword"),
            new TokenDescriptor("add2", IToken.TK_KEYWORD,    2, 1, 3, "Exp", "Add2Keyword"),
            new TokenDescriptor(" ",    IToken.TK_LAYOUT,     6, 1, 7, "Exp", "Add2Keyword"),
            new TokenDescriptor("x",    IToken.TK_IDENTIFIER, 7, 1, 8, "ID", null)
        //@formatter:on
        ));
    }

    @Test public void multipleLines() throws ParseError {
        testTokens("x;\nx", Arrays.asList(
        //@formatter:off
            new TokenDescriptor("x",  IToken.TK_IDENTIFIER, 0, 1, 1, "ID", null),
            new TokenDescriptor(";",  IToken.TK_OPERATOR,   1, 1, 2, null, null),
            new TokenDescriptor("\n", IToken.TK_LAYOUT,     2, 1, 3, null, null),
            new TokenDescriptor("x",  IToken.TK_IDENTIFIER, 3, 2, 1, "ID", null)
        //@formatter:on
        ));
    }

    @Test public void multipleLinesNewlineEnd() throws ParseError {
        testTokens("x;\nx\n", Arrays.asList(
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
