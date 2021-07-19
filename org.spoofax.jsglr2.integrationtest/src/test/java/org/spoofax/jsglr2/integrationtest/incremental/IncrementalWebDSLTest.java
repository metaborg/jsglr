package org.spoofax.jsglr2.integrationtest.incremental;

import java.io.IOException;
import java.util.stream.Stream;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.spoofax.jsglr2.integrationtest.BaseTestWithParseTableFromTerm;
import org.spoofax.terms.ParseError;

public class IncrementalWebDSLTest extends BaseTestWithParseTableFromTerm {

    public IncrementalWebDSLTest() throws Exception {
        setupParseTable("WebDSL");
    }

    @TestFactory public Stream<DynamicTest> testSampleProgramByJSGLR1() throws ParseError, IOException {
        String sampleProgram = getFileAsString("WebDSL/built-in.app");

        return testParseSuccess(sampleProgram);
    }

    @TestFactory public Stream<DynamicTest> testIncrementalMathApp() throws IOException {
        return testIncrementalSuccessByBatch(getFileAsString("WebDSL/math.app/0.in"),
            getFileAsString("WebDSL/math.app/1.in"), getFileAsString("WebDSL/math.app/2.in"));
    }

    // See the lexical SDF definition for numbers (both the old WebDSL in SDF2 and the new WebDSL-Statix in SDF3):
    // https://github.com/webdsl/webdsl/blob/master/src/org/webdsl/dsl/syntax/WebDSL-Lexical.sdf#L18-L31
    // https://github.com/webdsl/webdsl-statix/blob/master/webdslstatix/syntax/WebDSL-Lexical.sdf3#L32-L42
    // Specifically, the rules `Float = "-"? FloatDigits ExponentPart? [fFdD]?` and `Int = "-"? [0-9]+`.
    // The `FloatDigits` changes from `0.0` to `0`, which is still valid at this point.
    // The empty `ExponentPart?` and `[fFdD]?` must not be reused, and therefore we delete any null-yield trees
    // at position `currentUpdate.deletedEnd` in `InlinedEagerIncrementalInputStack.checkUpdate`.
    @TestFactory public Stream<DynamicTest> testIncrementalFloatToInt() {
        return testIncrementalSuccessByBatch(
            "module elib/elib-utils/math\nfunction sum(fs: List<Float>): Float {\n  var s := 0.0;\n}",
            "module elib/elib-utils/math\nfunction sum(ns: List<Int>): Int {\n  var s := 0;\n}");
    }

}
