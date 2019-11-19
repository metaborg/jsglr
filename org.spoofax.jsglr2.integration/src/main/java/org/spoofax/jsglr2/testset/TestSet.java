package org.spoofax.jsglr2.testset;

import java.util.*;

import org.spoofax.jsglr2.testset.testinput.IncrementalStringInput;
import org.spoofax.jsglr2.testset.testinput.StringInput;
import org.spoofax.jsglr2.testset.testinput.TestInput;
import org.spoofax.jsglr2.testset.testinput.TestSetIncrementalGitInput;

public class TestSet<ContentType, Input extends TestInput<ContentType>> {

    public final String name;
    public final TestSetInput<ContentType, Input> input;

    public TestSet(String name, TestSetInput<ContentType, Input> input) {
        this.name = name;
        this.input = input;
    }

    public static Map<String, String> parseArgs(String[] args) {
        Map<String, String> parsedArgs = new HashMap<>();

        for(String arg : args) {
            String[] splitted = arg.split("=");

            if(splitted.length == 2) {
                String key = splitted[0];
                String value = splitted[1];

                parsedArgs.put(key, value);
            } else
                throw new IllegalArgumentException("invalid argument: " + arg);
        }

        return parsedArgs;
    }

    public static TestSet<String, StringInput> fromArgs(Map<String, String> args) {
        String language = args.get("language");

        if(language != null) {
            TestSetInput<String, StringInput> input = TestSetInput.fromArgs(args);

            return new TestSet<>(language, input);
        }

        throw new IllegalStateException("invalid arguments");
    }

    public static TestSetWithParseTable<String, StringInput> fromArgsWithParseTable(Map<String, String> args) {
        String language = args.get("language");
        String parseTablePath = args.get("parseTablePath");

        if(language != null & parseTablePath != null) {
            TestSetParseTableFromATerm parseTable = new TestSetParseTableFromATerm(parseTablePath, false);

            TestSetInput<String, StringInput> input = TestSetInput.fromArgs(args);

            return new TestSetWithParseTable<>(language, parseTable, input);
        }

        throw new IllegalStateException("invalid arguments");
    }

    // --- ARTIFICIAL BENCHMARKS ---

    public static TestSetWithParseTable<String, StringInput> lexical = new TestSetWithParseTable<>("lexical",
        new TestSetParseTableFromSDF3("lexical-id"),
        new TestSetSizedInput.StringInputSet(n -> String.join("", Collections.nCopies(n, "a")), 10000, 50000, 100000));


    public static TestSetWithParseTable<String, StringInput> sumAmbiguous =
        new TestSetWithParseTable<>("sumAmbiguous", new TestSetParseTableFromSDF3("sum-ambiguous"),
            new TestSetSizedInput.StringInputSet(n -> String.join("+", Collections.nCopies(n, "x")), 20, 40, 60, 80));

    public static TestSetWithParseTable<String, StringInput> sumNonAmbiguous =
        new TestSetWithParseTable<>("sumNonAmbiguous", new TestSetParseTableFromSDF3("sum-nonambiguous"),
            new TestSetSizedInput.StringInputSet(n -> String.join("+", Collections.nCopies(n, "x")), 4000, 8000, 16000,
                32000, 64000));

    public static TestSetWithParseTable<String[], IncrementalStringInput> sumNonAmbiguousIncremental =
        new TestSetWithParseTable<>("sumNonAmbiguousIncremental", new TestSetParseTableFromSDF3("sum-nonambiguous"),
            new TestSetSizedInput.IncrementalStringInputSet(
                n -> new String[] { String.join("+", Collections.nCopies(n, "x")),
                    String.join("+", Collections.nCopies(n + 1, "x")), String.join("+", Collections.nCopies(n, "x")) },
                4000, 8000, 16000, 32000, 64000));

    // --- LANGUAGE BENCHMARKS ---

    public static TestSetWithParseTable<String, StringInput> greenMarl =
        new TestSetWithParseTable<>("greenmarl", new TestSetParseTableFromATerm("GreenMarl", true),
            new TestSetSingleInput.StringInputSet("GreenMarl/infomap.gm", true));


    private static final String JAVA_8_BENCHMARK_INPUT_PATH_STRING =
        System.getProperty(TestSet.class.getCanonicalName() + ".javaInputPath",
            "/Users/Jasper/git/spoofax-releng/mb-rep/org.spoofax.terms");

    private static final TestSetParseTableFromATerm JAVA_8_PARSE_TABLE =
        new TestSetParseTableFromATerm("Java8_SLR", true);

    public static TestSetWithParseTable<String, StringInput> java8 = new TestSetWithParseTable<>("java",
        JAVA_8_PARSE_TABLE, new TestSetMultipleInputs.StringInputSet(JAVA_8_BENCHMARK_INPUT_PATH_STRING, "java"));

    public static TestSetWithParseTable<String, StringInput> java8Unrolled =
        new TestSetWithParseTable<>("javaUnrolled", new TestSetParseTableFromATerm("Java8_unrolled", true),
            new TestSetMultipleInputs.StringInputSet(JAVA_8_BENCHMARK_INPUT_PATH_STRING, "java"));

    public static TestSetWithParseTable<String[], IncrementalStringInput> java8Incremental =
        new TestSetWithParseTable<>("java8Incremental", JAVA_8_PARSE_TABLE,
            new TestSetIncrementalInput("Java/AnyKeyboardViewBase.java/", true));

    public static final TestSetWithParseTable<String[], IncrementalStringInput> java8IncrementalGit =
        new TestSetWithParseTable<>("java8Incremental", JAVA_8_PARSE_TABLE,
            new TestSetIncrementalGitInput("/home/maarten/git/tmp/mb-rep", "java", 50));


    public static final TestSetWithParseTable<String[], IncrementalStringInput> ocamlIncrementalGit =
        new TestSetWithParseTable<>("OCaml-incremental-git", new TestSetParseTableFromATerm("OCaml", true),
            new TestSetIncrementalGitInput("/home/maarten/git/tmp/google-drive-ocamlfuse", "ml", 50));


    private static final String WEBDSL_BENCHMARK_INPUT_PATH_STRING = System.getProperty(
        TestSet.class.getCanonicalName() + ".webDSLInputPath", "/Users/Jasper/Desktop/jsglr2benchmarks/webdsl");

    public static TestSetWithParseTable<String, StringInput> webDSL =
        new TestSetWithParseTable<>("webdsl", new TestSetParseTableFromATerm("WebDSL", true),
            new TestSetMultipleInputs.StringInputSet(WEBDSL_BENCHMARK_INPUT_PATH_STRING, "app"));

    public static List<TestSetWithParseTable<String, StringInput>> all =
        Arrays.asList(lexical, sumAmbiguous, sumNonAmbiguous, greenMarl, java8, java8Unrolled, webDSL);

}
