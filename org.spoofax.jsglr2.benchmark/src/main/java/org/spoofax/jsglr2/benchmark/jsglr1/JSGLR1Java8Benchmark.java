package org.spoofax.jsglr2.benchmark.jsglr1;

import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;
import org.spoofax.jsglr2.testset.TestSet;

import java.util.concurrent.TimeUnit;

public class JSGLR1Java8Benchmark extends JSGLR1Benchmark {
    
    public JSGLR1Java8Benchmark() {
        super(TestSet.java8);
    }

    public static void main(String[] args) throws RunnerException {
        /*
            @Param({"false", "true"})
            public boolean implode;
         */

        // @formatter:off
        Options opt = new OptionsBuilder()
                .include(".*" + JSGLR1Java8Benchmark.class.getSimpleName() + ".(.*)")
                .timeUnit(TimeUnit.NANOSECONDS)
                .mode(Mode.AverageTime)
                .warmupIterations(10)
                .warmupTime(TimeValue.seconds(1))
                .measurementIterations(10)
                //.param("implode", "false")
                .forks(0)
                .build();
        // @formatter:on

        new Runner(opt).run();
    }

}
