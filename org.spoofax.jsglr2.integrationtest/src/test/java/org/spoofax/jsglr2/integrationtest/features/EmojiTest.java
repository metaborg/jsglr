package org.spoofax.jsglr2.integrationtest.features;

import static org.spoofax.jsglr.client.imploder.IToken.Kind.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Stream;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.spoofax.jsglr2.integrationtest.BaseTestWithSdf3ParseTables;
import org.spoofax.jsglr2.integrationtest.TokenDescriptor;
import org.spoofax.terms.ParseError;

public class EmojiTest extends BaseTestWithSdf3ParseTables {

    public EmojiTest() {
        super("emoji.sdf3");
    }

    @TestFactory public Stream<DynamicTest> testCorrectEmoji() throws ParseError {
        return testSuccessByExpansions("😄😇😹🙄😃😀", "[Var(\"😄😇😹🙄😃😀\")]");
    }

    @TestFactory public Stream<DynamicTest> testWrongEmoji() throws ParseError {
        // Note that the last emoji is a single-character emoji, i.e. "\u2728"
        return Stream.of(testParseFailure("🎉"), testParseFailure("🚀"), testParseFailure("✨"))
            .flatMap(stream -> stream);
    }

    @TestFactory public Stream<DynamicTest> testSingleSurrogate() throws ParseError {
        // This is the first UTF-16 surrogate character for the correct emoji (full encoding would be "\uD83D\uDE00").
        // As this is not a complete code point, it should fail.
        return testParseFailure("\uD83D");
    }

    @TestFactory public Stream<DynamicTest> testLetters() throws ParseError {
        return testParseFailure("Hello World!");
    }

    @TestFactory public Stream<DynamicTest> tokenizationTestSingle() throws ParseError {
        return testTokens("😇😹😄",
            Collections.singletonList(new TokenDescriptor("😇😹😄", TK_IDENTIFIER, 0, 1, 1, "ID", null)));
    }

    @TestFactory public Stream<DynamicTest> tokenizationTestAdd() throws ParseError {
        return testTokens("😇 ➕ 😄",
            Arrays.asList(new TokenDescriptor("😇", TK_IDENTIFIER, 0, 1, 1, "ID", null),
                new TokenDescriptor(" ", TK_LAYOUT, 2, 1, 2, "Exp", "Add"),
                new TokenDescriptor("➕", TK_IDENTIFIER, 3, 1, 3, "PLUS", null),
                new TokenDescriptor(" ", TK_LAYOUT, 4, 1, 4, "Exp", "Add"),
                new TokenDescriptor("😄", TK_IDENTIFIER, 5, 1, 5, "ID", null)));
    }

    @TestFactory public Stream<DynamicTest> tokenizationTestMultilineWithEmpty() throws ParseError {
        return testTokens("😇 \n\n 😄",
            Arrays.asList(new TokenDescriptor("😇", TK_IDENTIFIER, 0, 1, 1, "ID", null),
                new TokenDescriptor(" ", TK_LAYOUT, 2, 1, 2, null, "[]"),
                new TokenDescriptor("\n", TK_KEYWORD, 3, 1, 3, null, "[]"),
                new TokenDescriptor("", TK_NO_TOKEN_KIND, 4, 2, 1, "Exp", "Empty"),
                new TokenDescriptor("\n", TK_KEYWORD, 4, 2, 1, null, "[]"),
                new TokenDescriptor(" ", TK_LAYOUT, 5, 3, 1, null, "[]"),
                new TokenDescriptor("😄", TK_IDENTIFIER, 6, 3, 2, "ID", null)));
    }

    @TestFactory public Stream<DynamicTest> testEmojiIncremental() throws ParseError {
        return testIncrementalSuccessByExpansions(new String[] { "😇 ➕ 😄😄", "😇😇  ➕ 😄" },
            new String[] { "[Add(Var(\"😇\"),\"➕\",Var(\"😄😄\"))]", "[Add(Var(\"😇😇\"),\"➕\",Var(\"😄\"))]" });
    }

}
