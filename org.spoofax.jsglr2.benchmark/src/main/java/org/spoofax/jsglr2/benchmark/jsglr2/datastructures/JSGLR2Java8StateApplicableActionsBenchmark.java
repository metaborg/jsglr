package org.spoofax.jsglr2.benchmark.jsglr2.datastructures;

import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;
import org.spoofax.jsglr2.testset.TestSet;

public class JSGLR2Java8StateApplicableActionsBenchmark extends JSGLR2StateApplicableActionsBenchmark {

    public JSGLR2Java8StateApplicableActionsBenchmark() {
        super(TestSet.java8);
    }

    public static void main(String[] args) throws RunnerException {
        System.out.println(JSGLR2Java8StateApplicableActionsBenchmark.class.getSimpleName());

        // @formatter:off
		Options opt = new OptionsBuilder()
				.include(".*" + JSGLR2Java8StateApplicableActionsBenchmark.class.getSimpleName() + ".(.*)")
				.timeUnit(TimeUnit.NANOSECONDS)
				.mode(Mode.AverageTime)
				.warmupIterations(10)
				.warmupTime(TimeValue.seconds(1))
				.measurementIterations(10)
				.forks(3)
				.build();
		// @formatter:on

        new Runner(opt).run();
    }

}
