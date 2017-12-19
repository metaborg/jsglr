package org.spoofax.jsglr2.testset;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TestSet {

    public final String name;
    public final TestSetParseTable parseTable;
    public final TestSetInput input;

    public TestSet(String name, TestSetParseTable parseTable, TestSetInput input) {
        this.name = name;
        this.parseTable = parseTable;
        this.input = input;
    }

    public static TestSet lexical =
        new TestSet("lexical", new TestSetParseTableFromGrammarDef("lexical-id"), new TestSetSizedInput(n -> {
            return String.join("", Collections.nCopies(n, "a"));
        }, 10000, 50000, 100000));

    public static TestSet sumAmbiguous =
        new TestSet("sumAmbiguous", new TestSetParseTableFromGrammarDef("sum-ambiguous"), new TestSetSizedInput(n -> {
            return String.join("+", Collections.nCopies(n, "x"));
        }, 20, 40, 60, 80));

    public static TestSet sumNonAmbiguous = new TestSet("sumNonAmbiguous",
        new TestSetParseTableFromGrammarDef("sum-nonambiguous"), new TestSetSizedInput(n -> {
            return String.join("+", Collections.nCopies(n, "x"));
        }, 4000, 8000, 16000, 32000, 64000));

    private static final String JAVA_8_BENCHMARK_INPUT_PATH_STRING =
        System.getProperty(String.format("%s.%s", TestSet.class.getCanonicalName(), "javaInputPath"),
            "/Users/Jasper/git/spoofax-releng/mb-rep/org.spoofax.terms");

    public static TestSet java8 = new TestSet("java", new TestSetParseTableFromATerm("Java8"),
        new TestSetMultipleInputs(JAVA_8_BENCHMARK_INPUT_PATH_STRING, "java"));

    public static TestSet java8unrolled = new TestSet("javaUnrolled", new TestSetParseTableFromATerm("Java8_unrolled"),
        new TestSetMultipleInputs(JAVA_8_BENCHMARK_INPUT_PATH_STRING, "java"));

    public static TestSet greenMarl = new TestSet("greenmarl", new TestSetParseTableFromATerm("GreenMarl"),
        new TestSetSingleInput("GreenMarl/infomap.gm"));

    private static final String WEBDSL_BENCHMARK_INPUT_PATH_STRING =
        System.getProperty(String.format("%s.%s", TestSet.class.getCanonicalName(), "webDSLInputPath"),
            "/Users/Jasper/Desktop/jsglr2benchmarks/webdsl");

    public static TestSet webDSL = new TestSet("webdsl", new TestSetParseTableFromATerm("WebDSL"),
        new TestSetMultipleInputs(WEBDSL_BENCHMARK_INPUT_PATH_STRING, "app"));

    public static List<TestSet> all =
        Arrays.asList(lexical, sumAmbiguous, sumNonAmbiguous, java8, java8unrolled, greenMarl, webDSL);

}
