package org.spoofax.jsglr2.benchmark;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.*;
import org.spoofax.jsglr2.testset.Input;
import org.spoofax.jsglr2.testset.TestSet;
import org.spoofax.jsglr2.testset.TestSetReader;

@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public abstract class BaseBenchmark {

    protected TestSetReader testSetReader;
    protected Iterable<Input> inputs;

    @Param({ "-1" }) public int n; // Can be overwritten if the input has a dynamic size

    protected BaseBenchmark(TestSet testSet) {
        this.testSetReader = new BenchmarkTestsetReader(testSet);
    }

    @Setup public void setupInputs() throws IOException {
        if(n == -1)
            inputs = testSetReader.getInputs();
        else
            inputs = testSetReader.getInputsForSize(n);
    }

}
