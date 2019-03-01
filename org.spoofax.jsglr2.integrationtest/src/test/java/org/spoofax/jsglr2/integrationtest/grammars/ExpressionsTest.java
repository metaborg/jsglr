package org.spoofax.jsglr2.integrationtest.grammars;

import java.util.Arrays;

import org.junit.Test;
import org.spoofax.jsglr.client.imploder.IToken;
import org.spoofax.jsglr2.integrationtest.BaseTestWithSdf3ParseTables;
import org.spoofax.jsglr2.integrationtest.TokenDescriptor;
import org.spoofax.terms.ParseError;

public class ExpressionsTest extends BaseTestWithSdf3ParseTables {

    public ExpressionsTest() {
        super("expressions.sdf3");
    }

    @Test public void number() throws ParseError {
        testTokens("1", Arrays.asList(
        //@formatter:off
            new TokenDescriptor("1", IToken.TK_NUMBER, 1, 1, 1, 1)
        //@formatter:on
        ));
    }

    @Test public void identifier() throws ParseError {
        testTokens("x", Arrays.asList(
        //@formatter:off
            new TokenDescriptor("x", IToken.TK_IDENTIFIER, 1, 1, 1, 1)
        //@formatter:on
        ));
    }

    @Test public void operator() throws ParseError {
        testTokens("x+x", Arrays.asList(
        //@formatter:off
            new TokenDescriptor("x", IToken.TK_IDENTIFIER, 1, 1, 1, 1),
            new TokenDescriptor("+", IToken.TK_OPERATOR,   1, 2, 1, 2),
            new TokenDescriptor("x", IToken.TK_IDENTIFIER, 1, 3, 1, 3)
        //@formatter:on
        ));
    }

    @Test public void operatorWithLayout() throws ParseError {
        testTokens("x + x", Arrays.asList(
        //@formatter:off
            new TokenDescriptor("x", IToken.TK_IDENTIFIER, 1, 1, 1, 1),
            new TokenDescriptor(" ", IToken.TK_LAYOUT,     1, 2, 1, 2),
            new TokenDescriptor("+", IToken.TK_OPERATOR,   1, 3, 1, 3),
            new TokenDescriptor(" ", IToken.TK_LAYOUT,     1, 4, 1, 4),
            new TokenDescriptor("x", IToken.TK_IDENTIFIER, 1, 5, 1, 5)
        //@formatter:on
        ));
    }

    @Test public void operatorWithLayout2() throws ParseError {
        testTokens(" x + x ", Arrays.asList(
        //@formatter:off
            new TokenDescriptor(" ", IToken.TK_LAYOUT,     1, 1, 1, 1),
            new TokenDescriptor("x", IToken.TK_IDENTIFIER, 1, 2, 1, 2),
            new TokenDescriptor(" ", IToken.TK_LAYOUT,     1, 3, 1, 3),
            new TokenDescriptor("+", IToken.TK_OPERATOR,   1, 4, 1, 4),
            new TokenDescriptor(" ", IToken.TK_LAYOUT,     1, 5, 1, 5),
            new TokenDescriptor("x", IToken.TK_IDENTIFIER, 1, 6, 1, 6),
            new TokenDescriptor(" ", IToken.TK_LAYOUT,     1, 7, 1, 7)
        //@formatter:on
        ));
    }

}