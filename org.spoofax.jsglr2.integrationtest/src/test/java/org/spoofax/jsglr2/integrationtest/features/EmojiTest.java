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
        return testSuccessByExpansions("😄😇😹🙄😃😀", "\"😄😇😹🙄😃😀\"");
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

}
