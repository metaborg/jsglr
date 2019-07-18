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
            new TokenDescriptor("1", IToken.TK_NUMBER, 0, 1, 1)
        //@formatter:on
        ));
    }

    @Test public void identifier() throws ParseError {
        testTokens("x", Arrays.asList(
        //@formatter:off
            new TokenDescriptor("x", IToken.TK_IDENTIFIER, 0, 1, 1)
        //@formatter:on
        ));
    }

    @Test public void operator() throws ParseError {
        testTokens("x+x", Arrays.asList(
        //@formatter:off
            new TokenDescriptor("x", IToken.TK_IDENTIFIER, 0, 1, 1),
            new TokenDescriptor("+", IToken.TK_OPERATOR,   1, 1, 2),
            new TokenDescriptor("x", IToken.TK_IDENTIFIER, 2, 1, 3)
        //@formatter:on
        ));
    }

    @Test public void operatorWithLayout() throws ParseError {
        testTokens("x + x", Arrays.asList(
        //@formatter:off
            new TokenDescriptor("x", IToken.TK_IDENTIFIER, 0, 1, 1),
            new TokenDescriptor(" ", IToken.TK_LAYOUT,     1, 1, 2),
            new TokenDescriptor("+", IToken.TK_OPERATOR,   2, 1, 3),
            new TokenDescriptor(" ", IToken.TK_LAYOUT,     3, 1, 4),
            new TokenDescriptor("x", IToken.TK_IDENTIFIER, 4, 1, 5)
        //@formatter:on
        ));
    }

    @Test public void operatorWithLayout2() throws ParseError {
        testTokens(" x + x ", Arrays.asList(
        //@formatter:off
            new TokenDescriptor(" ", IToken.TK_LAYOUT,     0, 1, 1),
            new TokenDescriptor("x", IToken.TK_IDENTIFIER, 1, 1, 2),
            new TokenDescriptor(" ", IToken.TK_LAYOUT,     2, 1, 3),
            new TokenDescriptor("+", IToken.TK_OPERATOR,   3, 1, 4),
            new TokenDescriptor(" ", IToken.TK_LAYOUT,     4, 1, 5),
            new TokenDescriptor("x", IToken.TK_IDENTIFIER, 5, 1, 6),
            new TokenDescriptor(" ", IToken.TK_LAYOUT,     6, 1, 7)
        //@formatter:on
        ));
    }

    @Test public void keywordWithLayout() throws ParseError {
        testTokens("x add x", Arrays.asList(
        //@formatter:off
            new TokenDescriptor("x",   IToken.TK_IDENTIFIER, 0, 1, 1),
            new TokenDescriptor(" ",   IToken.TK_LAYOUT,     1, 1, 2),
            new TokenDescriptor("add", IToken.TK_KEYWORD,    2, 1, 3),
            new TokenDescriptor(" ",   IToken.TK_LAYOUT,     5, 1, 6),
            new TokenDescriptor("x",   IToken.TK_IDENTIFIER, 6, 1, 7)
        //@formatter:on
        ));
    }

    @Test public void multipleLines() throws ParseError {
        testTokens("x;\nx", Arrays.asList(
        //@formatter:off
            new TokenDescriptor("x",  IToken.TK_IDENTIFIER, 0, 1, 1),
            new TokenDescriptor(";",  IToken.TK_OPERATOR,   1, 1, 2),
            new TokenDescriptor("\n", IToken.TK_LAYOUT,     2, 1, 3),
            new TokenDescriptor("x",  IToken.TK_IDENTIFIER, 3, 2, 1)
        //@formatter:on
        ));
    }

    @Test public void multipleLinesNewlineEnd() throws ParseError {
        testTokens("x;\nx\n", Arrays.asList(
        //@formatter:off
            new TokenDescriptor("x",  IToken.TK_IDENTIFIER, 0, 1, 1),
            new TokenDescriptor(";",  IToken.TK_OPERATOR,   1, 1, 2),
            new TokenDescriptor("\n", IToken.TK_LAYOUT,     2, 1, 3),
            new TokenDescriptor("x",  IToken.TK_IDENTIFIER, 3, 2, 1),
            new TokenDescriptor("\n", IToken.TK_LAYOUT,     4, 2, 2)
        //@formatter:on
        ));
    }

}
