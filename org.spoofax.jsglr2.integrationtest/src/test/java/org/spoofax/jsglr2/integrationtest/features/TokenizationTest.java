package org.spoofax.jsglr2.integrationtest.features;

import static jsglr.shared.IToken.Kind.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.spoofax.jsglr2.integrationtest.BaseTestWithSdf3ParseTables;
import org.spoofax.jsglr2.integrationtest.TokenDescriptor;
import org.spoofax.terms.ParseError;

public class TokenizationTest extends BaseTestWithSdf3ParseTables {

    public TokenizationTest() {
        super("tokenization.sdf3");
    }

    @TestFactory public Stream<DynamicTest> number() throws ParseError {
        return testTokens("1", Arrays.asList(
        //@formatter:off
            new TokenDescriptor("1", TK_NUMBER, 0, 1, 1, "NUMBER", null)
        //@formatter:on
        ));
    }

    @TestFactory public Stream<DynamicTest> identifier() throws ParseError {
        return testTokens("x", Arrays.asList(
        //@formatter:off
            new TokenDescriptor("x", TK_IDENTIFIER, 0, 1, 1, "ID", null)
        //@formatter:on
        ));
    }

    @TestFactory public Stream<DynamicTest> operator() throws ParseError {
        return testTokens("x+x", Arrays.asList(
        //@formatter:off
            new TokenDescriptor("x", TK_IDENTIFIER, 0, 1, 1, "ID", null),
            new TokenDescriptor("+", TK_OPERATOR,   1, 1, 2, "Exp", "AddOperator"),
            new TokenDescriptor("x", TK_IDENTIFIER, 2, 1, 3, "ID", null)
        //@formatter:on
        ));
    }

    @TestFactory public Stream<DynamicTest> ambiguousOperator() throws ParseError {
        List<TokenDescriptor> tokens1 = Arrays.asList(
        //@formatter:off
            new TokenDescriptor("x", TK_IDENTIFIER, 0, 1, 1, "ID", null),
            new TokenDescriptor("+", TK_OPERATOR,   1, 1, 2, "Exp", "AddOperator"),
            new TokenDescriptor("x", TK_IDENTIFIER, 2, 1, 3, "ID", null),
            new TokenDescriptor("+", TK_OPERATOR,   3, 1, 4, "Exp", "AddOperator"),
            new TokenDescriptor("x", TK_IDENTIFIER, 4, 1, 5, "ID", null)
        //@formatter:on
        );
        List<TokenDescriptor> tokens2 = new ArrayList<>(tokens1);
        tokens2.addAll(Arrays.asList(
        //@formatter:off
            new TokenDescriptor("x", TK_IDENTIFIER, 0, 1, 1, "ID", null),
            new TokenDescriptor("+", TK_OPERATOR,   1, 1, 2, "Exp", "AddOperator"),
            new TokenDescriptor("x", TK_IDENTIFIER, 2, 1, 3, "ID", null),
            new TokenDescriptor("+", TK_OPERATOR,   3, 1, 4, "Exp", "AddOperator"),
            new TokenDescriptor("x", TK_IDENTIFIER, 4, 1, 5, "ID", null)
        //@formatter:on
        ));
        return testTokens("x+x+x", tokens1, tokens2);
    }

    @TestFactory public Stream<DynamicTest> operatorWithLayout() throws ParseError {
        return testTokens("x + x", Arrays.asList(
        //@formatter:off
            new TokenDescriptor("x", TK_IDENTIFIER, 0, 1, 1, "ID", null),
            new TokenDescriptor(" ", TK_LAYOUT,     1, 1, 2, "Exp", "AddOperator"),
            new TokenDescriptor("+", TK_OPERATOR,   2, 1, 3, "Exp", "AddOperator"),
            new TokenDescriptor(" ", TK_LAYOUT,     3, 1, 4, "Exp", "AddOperator"),
            new TokenDescriptor("x", TK_IDENTIFIER, 4, 1, 5, "ID", null)
        //@formatter:on
        ));
    }

    @TestFactory public Stream<DynamicTest> operatorWithLayout2() throws ParseError {
        return testTokens(" x + x ", Arrays.asList(
        //@formatter:off
            new TokenDescriptor(" ", TK_LAYOUT,     0, 1, 1, null, "[]"),
            new TokenDescriptor("x", TK_IDENTIFIER, 1, 1, 2, "ID", null),
            new TokenDescriptor(" ", TK_LAYOUT,     2, 1, 3, "Exp", "AddOperator"),
            new TokenDescriptor("+", TK_OPERATOR,   3, 1, 4, "Exp", "AddOperator"),
            new TokenDescriptor(" ", TK_LAYOUT,     4, 1, 5, "Exp", "AddOperator"),
            new TokenDescriptor("x", TK_IDENTIFIER, 5, 1, 6, "ID", null),
            new TokenDescriptor(" ", TK_LAYOUT,     6, 1, 7, null, "[]")
        //@formatter:on
        ));
    }

    @TestFactory public Stream<DynamicTest> keywordWithLayout() throws ParseError {
        return testTokens("x add x", Arrays.asList(
        //@formatter:off
            new TokenDescriptor("x",   TK_IDENTIFIER, 0, 1, 1, "ID", null),
            new TokenDescriptor(" ",   TK_LAYOUT,     1, 1, 2, "Exp", "AddKeyword"),
            new TokenDescriptor("add", TK_KEYWORD,    2, 1, 3, "Exp", "AddKeyword"),
            new TokenDescriptor(" ",   TK_LAYOUT,     5, 1, 6, "Exp", "AddKeyword"),
            new TokenDescriptor("x",   TK_IDENTIFIER, 6, 1, 7, "ID", null)
        //@formatter:on
        ));
    }

    @TestFactory public Stream<DynamicTest> keywordWithNumber() throws ParseError {
        return testTokens("x add2 x", Arrays.asList(
        //@formatter:off
            new TokenDescriptor("x",    TK_IDENTIFIER, 0, 1, 1, "ID", null),
            new TokenDescriptor(" ",    TK_LAYOUT,     1, 1, 2, "Exp", "Add2Keyword"),
            new TokenDescriptor("add2", TK_KEYWORD,    2, 1, 3, "Exp", "Add2Keyword"),
            new TokenDescriptor(" ",    TK_LAYOUT,     6, 1, 7, "Exp", "Add2Keyword"),
            new TokenDescriptor("x",    TK_IDENTIFIER, 7, 1, 8, "ID", null)
        //@formatter:on
        ));
    }

    @TestFactory public Stream<DynamicTest> emptyList() throws ParseError {
        return testTokens("[]", Arrays.asList(
        //@formatter:off
            new TokenDescriptor("[", TK_OPERATOR,      0, 1, 1, "List", "List"),
            new TokenDescriptor("]", TK_OPERATOR,      1, 1, 2, "List", "List")
        //@formatter:on
        ), Arrays.asList( // The AST of this input is "[List([])]"
        //@formatter:off
            new TokenDescriptor("[", TK_OPERATOR,      0, 1, 1, "List", "List"),
            new TokenDescriptor("",  TK_NO_TOKEN_KIND, 1, 1, 2, null, "[]"), // belonging to AST "[]"
            new TokenDescriptor("]", TK_OPERATOR,      1, 1, 2, "List", "List")
        //@formatter:on
        ));
    }

    @TestFactory public Stream<DynamicTest> singletonList() throws ParseError {
        return testTokens("[x]", Arrays.asList(
        //@formatter:off
            new TokenDescriptor("[", TK_OPERATOR,    0, 1, 1, "List", "List"),
            new TokenDescriptor("x", TK_IDENTIFIER,  1, 1, 2, "ID", null),
            new TokenDescriptor("]", TK_OPERATOR,    2, 1, 3, "List", "List")
        //@formatter:on
        ));
    }

    @TestFactory public Stream<DynamicTest> longList() throws ParseError {
        return testTokens("[x,x,x]", Arrays.asList(
        //@formatter:off
            new TokenDescriptor("[", TK_OPERATOR,   0, 1, 1, "List", "List"),
            new TokenDescriptor("x", TK_IDENTIFIER, 1, 1, 2, "ID", null),
            new TokenDescriptor(",", TK_OPERATOR,   2, 1, 3, null, "[]"),
            new TokenDescriptor("x", TK_IDENTIFIER, 3, 1, 4, "ID", null),
            new TokenDescriptor(",", TK_OPERATOR,   4, 1, 5, null, "[]"),
            new TokenDescriptor("x", TK_IDENTIFIER, 5, 1, 6, "ID", null),
            new TokenDescriptor("]", TK_OPERATOR,   6, 1, 7, "List", "List")
        //@formatter:on
        ));
    }

    @TestFactory public Stream<DynamicTest> multipleLines() throws ParseError {
        return testTokens("x;\nx", Arrays.asList(
        //@formatter:off
            new TokenDescriptor("x",  TK_IDENTIFIER, 0, 1, 1, "ID", null),
            new TokenDescriptor(";",  TK_OPERATOR,   1, 1, 2, null, "[]"),
            new TokenDescriptor("\n", TK_LAYOUT,     2, 1, 3, null, "[]"),
            new TokenDescriptor("x",  TK_IDENTIFIER, 3, 2, 1, "ID", null)
        //@formatter:on
        ));
    }

    @TestFactory public Stream<DynamicTest> multipleLinesNewlineEnd() throws ParseError {
        return testTokens("x;\nx\n", Arrays.asList(
        //@formatter:off
            new TokenDescriptor("x",  TK_IDENTIFIER, 0, 1, 1, "ID", null),
            new TokenDescriptor(";",  TK_OPERATOR,   1, 1, 2, null, "[]"),
            new TokenDescriptor("\n", TK_LAYOUT,     2, 1, 3, null, "[]"),
            new TokenDescriptor("x",  TK_IDENTIFIER, 3, 2, 1, "ID", null),
            new TokenDescriptor("\n", TK_LAYOUT,     4, 2, 2, null, "[]")
        //@formatter:on
        ));
    }

}
