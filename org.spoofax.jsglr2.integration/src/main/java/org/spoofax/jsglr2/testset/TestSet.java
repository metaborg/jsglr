package org.spoofax.jsglr2.testset;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.spoofax.jsglr2.testset.testinput.IncrementalStringInput;
import org.spoofax.jsglr2.testset.testinput.StringInput;
import org.spoofax.jsglr2.testset.testinput.TestInput;
import org.spoofax.jsglr2.testset.testinput.TestSetIncrementalGitInput;

public class TestSet<ContentType, Input extends TestInput<ContentType>> {

    public final String name;
    public final TestSetParseTable parseTable;
    public final TestSetInput<ContentType, Input> input;

    public TestSet(String name, TestSetParseTable parseTable, TestSetInput<ContentType, Input> input) {
        this.name = name;
        this.parseTable = parseTable;
        this.input = input;
    }

    public static TestSet<String, StringInput> fromArgs(String[] args) {
        if(args.length == 5) {
            String language = args[0];
            String extension = args[1];
            String parseTablePath = args[2];
            String sourcePath = args[3];
            String type = args[4];

            TestSetParseTableFromATerm parseTable = new TestSetParseTableFromATerm(parseTablePath, false);

            TestSetInput<String, StringInput> input;

            if ("multiple".equals(type)) {
                input = new TestSetMultipleInputs.StringInputSet(sourcePath, extension);

                return new TestSet<>(language, parseTable, input);
            } else if ("single".equals(type)) {
                input = new TestSetSingleInput.StringInputSet(sourcePath);

                return new TestSet<>(language, parseTable, input);
            }
        }

        throw new IllegalStateException("invalid arguments");
    }

    // --- ARTIFICIAL BENCHMARKS ---

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

    public static TestSet<String[], IncrementalStringInput> sumNonAmbiguousIncremental =
        new TestSet<>("sumNonAmbiguousIncremental", new TestSetParseTableFromSDF3("sum-nonambiguous"),
            new TestSetSizedInput.IncrementalStringInputSet(
                n -> new String[] { String.join("+", Collections.nCopies(n, "x")),
                    String.join("+", Collections.nCopies(n + 1, "x")), String.join("+", Collections.nCopies(n, "x")) },
                4000, 8000, 16000, 32000, 64000));

    // --- LANGUAGE BENCHMARKS ---

    public static TestSet<String, StringInput> greenMarl =
        new TestSet<>("greenmarl", new TestSetParseTableFromATerm("GreenMarl", true),
            new TestSetSingleInput.StringInputSet("GreenMarl/infomap.gm"));


    private static final String JAVA_8_BENCHMARK_INPUT_PATH_STRING =
        System.getProperty(TestSet.class.getCanonicalName() + ".javaInputPath",
            "/Users/Jasper/git/spoofax-releng/mb-rep/org.spoofax.terms");

    private static final TestSetParseTableFromATerm JAVA_8_PARSE_TABLE =
        new TestSetParseTableFromATerm("Java8_SLR", true);

    public static TestSet<String, StringInput> java8 = new TestSet<>("java", JAVA_8_PARSE_TABLE,
        new TestSetMultipleInputs.StringInputSet(JAVA_8_BENCHMARK_INPUT_PATH_STRING, "java"));

    public static TestSet<String, StringInput> java8Unrolled =
        new TestSet<>("javaUnrolled", new TestSetParseTableFromATerm("Java8_unrolled", true),
            new TestSetMultipleInputs.StringInputSet(JAVA_8_BENCHMARK_INPUT_PATH_STRING, "java"));

    public static TestSet<String[], IncrementalStringInput> java8Incremental = new TestSet<>("java8Incremental",
        JAVA_8_PARSE_TABLE, new TestSetIncrementalInput("Java/AnyKeyboardViewBase.java/"));

    public static final TestSet<String[], IncrementalStringInput> java8IncrementalGit =
        new TestSet<>("java8Incremental", JAVA_8_PARSE_TABLE,
            new TestSetIncrementalGitInput("/home/maarten/git/tmp/mb-rep", "java", 50));


    public static final TestSet<String[], IncrementalStringInput> ocamlIncrementalGit =
        new TestSet<>("OCaml-incremental-git", new TestSetParseTableFromATerm("OCaml", true),
            new TestSetIncrementalGitInput("/home/maarten/git/tmp/google-drive-ocamlfuse", "ml", 50));


    private static final String WEBDSL_BENCHMARK_INPUT_PATH_STRING = System.getProperty(
        TestSet.class.getCanonicalName() + ".webDSLInputPath", "/Users/Jasper/Desktop/jsglr2benchmarks/webdsl");

    public static TestSet<String, StringInput> webDSL =
        new TestSet<>("webdsl", new TestSetParseTableFromATerm("WebDSL", true),
            new TestSetMultipleInputs.StringInputSet(WEBDSL_BENCHMARK_INPUT_PATH_STRING, "app"));

    public static List<TestSet<String, StringInput>> all =
        Arrays.asList(lexical, sumAmbiguous, sumNonAmbiguous, greenMarl, java8, java8Unrolled, webDSL);

}
