package org.spoofax.jsglr2.integrationtest.features;

import static org.spoofax.jsglr.client.imploder.IToken.Kind.TK_NO_TOKEN_KIND;
import static org.spoofax.jsglr.client.imploder.IToken.Kind.TK_OPERATOR;

import java.util.Arrays;
import java.util.stream.Stream;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.spoofax.jsglr2.integrationtest.BaseTestWithSdf3ParseTables;
import org.spoofax.jsglr2.integrationtest.TokenDescriptor;
import org.spoofax.terms.ParseError;

public class KernelStringTest extends BaseTestWithSdf3ParseTables {

    public KernelStringTest() {
        super("kernel-string.sdf3");
    }

    @TestFactory public Stream<DynamicTest> emptyString() throws ParseError {
        return testSuccessByExpansions("\"\"", "[String(\"\")]");
    }

    @TestFactory public Stream<DynamicTest> nonEmptyString() throws ParseError {
        return testSuccessByExpansions("\"Hello World!\"", "[String(\"Hello World!\")]");
    }

    @TestFactory public Stream<DynamicTest> emptyAndNonEmptyStrings() throws ParseError {
        return testSuccessByExpansions("\"\"\n\"Hello World!\"", "[String(\"\"),String(\"Hello World!\")]");
    }

    @TestFactory public Stream<DynamicTest> emptyStringTokens() throws ParseError {
        return testTokens("\"\"", Arrays.asList(
        //@formatter:off
            new TokenDescriptor("\"", TK_OPERATOR,      0, 1, 1, "String", "String"),
            new TokenDescriptor("\"", TK_OPERATOR,      1, 1, 2, "String", "String")
        //@formatter:on
        ), Arrays.asList(
        //@formatter:off
            new TokenDescriptor("\"", TK_OPERATOR,      0, 1, 1, "String", "String"),
            new TokenDescriptor("",   TK_NO_TOKEN_KIND, 1, 1, 2, "InnerString", null),
            new TokenDescriptor("\"", TK_OPERATOR,      1, 1, 2, "String", "String")
        //@formatter:on
        ));
    }


}
