package org.spoofax.jsglr2.benchmark.treesitter;

import java.util.Map;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;
import org.spoofax.jsglr2.benchmark.BaseBenchmark;
import org.spoofax.jsglr2.benchmark.BenchmarkTestSetReader;
import org.spoofax.jsglr2.testset.TestSet;
import org.spoofax.jsglr2.testset.testinput.StringInput;

import com.github.mpsijm.javatreesitter.java.TreeSitterJavaLibrary;

@State(Scope.Benchmark)
public class TreeSitterBenchmark extends BaseBenchmark<String, StringInput> {

    private TreeSitterParser parser;

    public TreeSitterBenchmark() {
        Map<String, String> args = TestSet.parseArgs(System.getProperty("testSet").split(" "));

        if(!args.getOrDefault("language", "").equals("java")) {
            throw new IllegalStateException("TreeSitter currently only supports Java");
        }

        TestSet<String, StringInput> testSet = TestSet.fromArgs(args);

        setTestSetReader(new BenchmarkTestSetReader<>(testSet));
    }

    @Setup public void setup() {
        parser = new TreeSitterParser(TreeSitterParser.SupportedLanguage.JAVA);
    }

    @Benchmark public void benchmark(Blackhole bh) {
        for(StringInput input : inputs)
            bh.consume(action(bh, input));
    }

    protected Object action(Blackhole bh, StringInput input) {
        return parser.parse(input.content);
    }

}
