package org.spoofax.jsglr2.benchmark.jsglr2;

import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;
import org.spoofax.jsglr2.testset.TestSet;

import java.util.concurrent.TimeUnit;

public class JSGLR2Java8Benchmark extends JSGLR2Benchmark {

    public JSGLR2Java8Benchmark() {
        super(TestSet.java8);
    }

    public static void main(String[] args) throws RunnerException {
        System.out.println(JSGLR2Java8CharacterClassBenchmark.class.getSimpleName());

        /*
            @Param({"false", "true"})
            public boolean implode;

            @Param({"Null", "Basic", "Hybrid"})
            public JSGLR2Variants.ParseForestRepresentation parseForestRepresentation;

            @Param({"Full", "Optimized"})
            public JSGLR2Variants.ParseForestConstruction parseForestConstruction;

            @Param({"Basic", "Hybrid", "BasicElkhound", "HybridElkhound"})
            public JSGLR2Variants.StackRepresentation stackRepresentation;

            @Param({"Basic", "Elkhound"})
            public JSGLR2Variants.Reducing reducing;
         */

        // @formatter:off
        Options opt = new OptionsBuilder()
                .include(".*" + JSGLR2Java8Benchmark.class.getSimpleName() + ".(.*)")
                .timeUnit(TimeUnit.NANOSECONDS)
                .mode(Mode.AverageTime)
                .warmupIterations(10)
                .warmupTime(TimeValue.seconds(1))
                .measurementIterations(10)
                .param("implode", "false")
                .param("parseForestRepresentation", "Hybrid")
                .param("parseForestConstruction", "Optimized")
                .param("stackRepresentation", "HybridElkhound")
                .param("reducing", "Elkhound")
                .forks(0)
                .build();
        // @formatter:on

        new Runner(opt).run();
    }

}
