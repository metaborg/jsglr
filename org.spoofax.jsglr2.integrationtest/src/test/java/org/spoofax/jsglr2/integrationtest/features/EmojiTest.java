package org.spoofax.jsglr2.integrationtest.features;

import java.util.stream.Stream;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.spoofax.jsglr2.integrationtest.BaseTestWithSdf3ParseTables;
import org.spoofax.terms.ParseError;

public class EmojiTest extends BaseTestWithSdf3ParseTables {

    public EmojiTest() {
        super("emoji.sdf3");
    }

    @TestFactory public Stream<DynamicTest> testCorrectEmoji() throws ParseError {
        // 😄😇😹🙄😃😀
        return testSuccessByExpansions("\uD83D\uDE04\uD83D\uDE07\uD83D\uDE39\uD83D\uDE44\uD83D\uDE03\uD83D\uDE00",
            "\"\uD83D\uDE04\uD83D\uDE07\uD83D\uDE39\uD83D\uDE44\uD83D\uDE03\uD83D\uDE00\"");
    }

    @TestFactory public Stream<DynamicTest> testWrongEmoji() throws ParseError {
        // 🎉🚀✨
        return Stream.of(testParseFailure("\uD83C\uDF89"), testParseFailure("\uD83D\uDE80"), testParseFailure("\u2728"))
            .flatMap(stream -> stream);
    }

    @TestFactory public Stream<DynamicTest> testSingleSurrogate() throws ParseError {
        // This is the first UTF-16 surrogate character for the correct emoji.
        // As this is not a complete code point, it should fail.
        return testParseFailure("\uD83D");
    }

    @TestFactory public Stream<DynamicTest> testLetters() throws ParseError {
        return testParseFailure("Hello World!");
    }

}
