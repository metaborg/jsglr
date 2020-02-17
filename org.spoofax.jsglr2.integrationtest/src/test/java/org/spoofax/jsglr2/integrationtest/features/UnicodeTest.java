package org.spoofax.jsglr2.integrationtest.features;

import java.util.stream.Stream;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.spoofax.jsglr2.integrationtest.BaseTestWithSdf3ParseTablesWithJSGLR1;
import org.spoofax.terms.ParseError;

public class UnicodeTest extends BaseTestWithSdf3ParseTablesWithJSGLR1 {

    public UnicodeTest() {
        super("unicode.sdf3");
    }

    @TestFactory public Stream<DynamicTest> testASCII1() throws ParseError {
        return testSuccessByJSGLR1("Hello World!");
    }

    @TestFactory public Stream<DynamicTest> testAccents1() throws ParseError {
        return testSuccessByJSGLR1("¡Hēłļø Wóŗ£đ!");
    }

    @TestFactory public Stream<DynamicTest> testGreek1() throws ParseError {
        return testSuccessByJSGLR1("Γεια σου κόσμο");
    }

    @TestFactory public Stream<DynamicTest> testEmoji1() throws ParseError {
        return testSuccessByJSGLR1("👋🌍😄🎉");
    }

    @TestFactory public Stream<DynamicTest> testASCII2() throws ParseError {
        return testSuccessByExpansions("Hello World!", "Id(\"Hello World!\")");
    }

    @TestFactory public Stream<DynamicTest> testAccents2() throws ParseError {
        return testSuccessByExpansions("¡Hēłļø Wóŗ£đ!", "Id(\"¡Hēłļø Wóŗ£đ!\")");
    }

    @TestFactory public Stream<DynamicTest> testGreek2() throws ParseError {
        return testSuccessByExpansions("Γεια σου κόσμο", "Id(\"Γεια σου κόσμο\")");
    }

    @TestFactory public Stream<DynamicTest> testEmoji2() throws ParseError {
        return testSuccessByExpansions("👋🌍😄🎉", "Id(\"👋🌍😄🎉\")");
    }

}
