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

}
