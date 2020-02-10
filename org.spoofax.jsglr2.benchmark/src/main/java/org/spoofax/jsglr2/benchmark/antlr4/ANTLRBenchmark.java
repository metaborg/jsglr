package org.spoofax.jsglr2.benchmark.antlr4;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.Tree;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.infra.Blackhole;
import org.spoofax.jsglr2.benchmark.BaseBenchmark;
import org.spoofax.jsglr2.benchmark.BenchmarkTestSetReader;
import org.spoofax.jsglr2.testset.TestSet;
import org.spoofax.jsglr2.testset.testinput.StringInput;

public abstract class ANTLRBenchmark<Lexer_ extends Lexer, Parser_ extends Parser>
    extends BaseBenchmark<String, StringInput> {

    ANTLRBenchmark() {
        String[] args = System.getProperty("testSet").split(" ");

        TestSet<String, StringInput> testSet = TestSet.fromArgs(TestSet.parseArgs(args));

        setTestSetReader(new BenchmarkTestSetReader<>(testSet));
    }

    @Benchmark public void benchmark(Blackhole bh) {
        for(StringInput input : inputs)
            bh.consume(action(bh, input));
    }

    protected abstract Lexer_ lexer(CharStream charStream);

    protected abstract Parser_ parser(TokenStream tokens);

    protected abstract Tree result(Parser_ parser);

    protected Object action(Blackhole bh, StringInput input) {
        CharStream charStream = CharStreams.fromString(input.content);
        Lexer_ lexer = lexer(charStream);

        TokenStream tokens = new CommonTokenStream(lexer);
        Parser_ parser = parser(tokens);

        return result(parser);
    }

}
