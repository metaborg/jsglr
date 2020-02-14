package org.spoofax.jsglr2.integrationtest.features;

import java.util.stream.Stream;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.spoofax.jsglr2.integrationtest.BaseTestWithSdf3ParseTables;
import org.spoofax.terms.ParseError;

public class UnicodeTest extends BaseTestWithSdf3ParseTables {

    public UnicodeTest() {
        super("unicode.sdf3");
    }

    @TestFactory public Stream<DynamicTest> testASCII() throws ParseError {
        return testSuccessByExpansions("Hello World!", "\"Hello World!\"");
    }

    @TestFactory public Stream<DynamicTest> testAccents() throws ParseError {
        return testSuccessByExpansions("¡Hēłļø Wóŗ£đ!", "\"¡Hēłļø Wóŗ£đ!\"");
    }

    @TestFactory public Stream<DynamicTest> testGreek() throws ParseError {
        return testSuccessByExpansions("Γεια σου κόσμο", "\"Γεια σου κόσμο\"");
    }

    @TestFactory public Stream<DynamicTest> testEmoji() throws ParseError {
        // 👋🌍😄🎉
        return testSuccessByExpansions("\uD83D\uDC4B\uD83C\uDF0D\uD83D\uDE04\uD83C\uDF89",
            "\"\uD83D\uDC4B\uD83C\uDF0D\uD83D\uDE04\uD83C\uDF89\"");
    }

}
