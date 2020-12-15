package org.spoofax.jsglr2.benchmark.treesitter;

import java.util.HashMap;
import java.util.Map;

import org.bridj.Pointer;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.spoofax.jsglr2.benchmark.BaseBenchmark;
import org.spoofax.jsglr2.benchmark.BenchmarkTestSetWithParseTableReader;
import org.spoofax.jsglr2.testset.TestSet;
import org.spoofax.jsglr2.testset.TestSetWithParseTable;
import org.spoofax.jsglr2.testset.testinput.IncrementalStringInput;

import com.github.mpsijm.javatreesitter.JavaTreeSitterLibrary;
import com.github.mpsijm.javatreesitter.java.TreeSitterJavaLibrary;

@State(Scope.Benchmark)
public class TreeSitterBenchmarkIncremental extends BaseBenchmark<String[], IncrementalStringInput> {

    private TreeSitterParser parser;

    @Param({ "Incremental", "IncrementalNoCache" }) public ParserType parserType;

    @Param({ "-1" }) public int i;

    public TreeSitterBenchmarkIncremental() {
        Map<String, String> args = TestSet.parseArgs(System.getProperty("testSet").split(" "));

        if(!args.getOrDefault("language", "").equals("java")) {
            throw new IllegalStateException("TreeSitter currently only supports Java");
        }

        TestSetWithParseTable<String[], IncrementalStringInput> testSet =
            TestSet.fromArgsWithParseTableIncremental(args);

        setTestSetReader(new BenchmarkTestSetWithParseTableReader<>(testSet));
    }

    public enum ParserType {
        Incremental(true), IncrementalNoCache(false);

        boolean setupCache;

        ParserType(boolean setupCache) {
            this.setupCache = setupCache;
        }
    }

    Map<IncrementalStringInput, String> prevString = new HashMap<>();
    Map<IncrementalStringInput, Pointer<JavaTreeSitterLibrary.TSTree>> prevTree = new HashMap<>();

    protected boolean shouldSetupCache() {
        return parserType.setupCache && i > 0;
    }

    @Setup public void setup() {
        parser = new TreeSitterParser(TreeSitterJavaLibrary.tree_sitter_java());

        if(shouldSetupCache()) {
            // Hack: the original intent of the IncrementalBenchmark was to read in all files upon setup.
            // However, for the external benchmark, only two versions of these files are read.
            // Therefore, we have to overwrite the `setupCache` function, but copy/pasting is bad,
            // so we just pretend that the iteration number (`i`) is temporarily something different. O:)
            int oldI = i;
            i = Math.min(i, 1);

            for(IncrementalStringInput input : inputs) {
                String content = input.content[i - 1];
                if(content == null)
                    continue;
                prevString.put(input, content);
                prevTree.put(input, parser.parse(content));
            }

            i = oldI;
        }
    }

    @Benchmark public void benchmark(Blackhole bh) {
        for(IncrementalStringInput input : inputs)
            bh.consume(action(bh, input));
    }

    protected Object action(Blackhole bh, IncrementalStringInput input) {
        if(i >= 0) {
            String s = input.content[i > 0 ? 1 : 0];
            if(s == null)
                return null;
            return parser.parse(s, prevString.get(input), prevTree.get(input));
        }

        String previousInput = null;
        Pointer<JavaTreeSitterLibrary.TSTree> previousResult = null;

        // if (i == -1)
        for(String content : input.content) {
            if(content == null)
                continue;
            bh.consume(previousResult = parser.parse(content, previousInput, previousResult));
            previousInput = content;
        }
        return null;
    }
}
