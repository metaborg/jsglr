package org.spoofax.jsglr2.testset;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.spoofax.jsglr2.testset.testinput.StringInput;
import org.spoofax.jsglr2.testset.testinput.TestInput;

public class TestSet<ContentType, Input extends TestInput<ContentType>> {

    public final String name;
    public final TestSetParseTable parseTable;
    public final TestSetInput<ContentType, Input> input;

    private TestSet(String name, TestSetParseTable parseTable, TestSetInput<ContentType, Input> input) {
        this.name = name;
        this.parseTable = parseTable;
        this.input = input;
    }

    public static TestSet<String, StringInput> lexical = new TestSet<>("lexical",
        new TestSetParseTableFromSDF3("lexical-id"),
        new TestSetSizedInput.StringInputSet(n -> String.join("", Collections.nCopies(n, "a")), 10000, 50000, 100000));


    public static TestSet<String, StringInput> sumAmbiguous =
        new TestSet<>("sumAmbiguous", new TestSetParseTableFromSDF3("sum-ambiguous"),
            new TestSetSizedInput.StringInputSet(n -> String.join("+", Collections.nCopies(n, "x")), 20, 40, 60, 80));

    public static TestSet<String, StringInput> sumNonAmbiguous =
        new TestSet<>("sumNonAmbiguous", new TestSetParseTableFromSDF3("sum-nonambiguous"),
            new TestSetSizedInput.StringInputSet(n -> String.join("+", Collections.nCopies(n, "x")), 4000, 8000, 16000,
                32000, 64000));


    private static final String JAVA_8_BENCHMARK_INPUT_PATH_STRING =
        System.getProperty(TestSet.class.getCanonicalName() + ".javaInputPath",
            "/Users/Jasper/git/spoofax-releng/mb-rep/org.spoofax.terms");

    public static TestSet<String, StringInput> java8 = new TestSet<>("java", new TestSetParseTableFromATerm("Java8"),
        new TestSetMultipleInputs.StringInputSet(JAVA_8_BENCHMARK_INPUT_PATH_STRING, "java"));

    public static TestSet<String, StringInput> java8unrolled =
        new TestSet<>("javaUnrolled", new TestSetParseTableFromATerm("Java8_unrolled"),
            new TestSetMultipleInputs.StringInputSet(JAVA_8_BENCHMARK_INPUT_PATH_STRING, "java"));


    public static TestSet<String, StringInput> greenMarl = new TestSet<>("greenmarl",
        new TestSetParseTableFromATerm("GreenMarl"), new TestSetSingleInput.StringInputSet("GreenMarl/infomap.gm"));


    private static final String WEBDSL_BENCHMARK_INPUT_PATH_STRING = System.getProperty(
        TestSet.class.getCanonicalName() + ".webDSLInputPath", "/Users/Jasper/Desktop/jsglr2benchmarks/webdsl");

    public static TestSet<String, StringInput> webDSL =
        new TestSet<>("webdsl", new TestSetParseTableFromATerm("WebDSL"),
            new TestSetMultipleInputs.StringInputSet(WEBDSL_BENCHMARK_INPUT_PATH_STRING, "app"));


    public static List<TestSet<String, StringInput>> all =
        Arrays.asList(lexical, sumAmbiguous, sumNonAmbiguous, java8, java8unrolled, greenMarl, webDSL);

}
