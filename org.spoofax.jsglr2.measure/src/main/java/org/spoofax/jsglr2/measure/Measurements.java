package org.spoofax.jsglr2.measure;

import java.io.PrintWriter;
import java.util.List;

import org.spoofax.jsglr2.testset.StringInput;
import org.spoofax.jsglr2.testset.TestSet;

public abstract class Measurements {

    protected TestSet<StringInput> testSet;
    protected MeasureTestSetReader<StringInput> testSetReader;

    public Measurements(TestSet<StringInput> testSet) {
        this.testSet = testSet;
        this.testSetReader = new MeasureTestSetReader<>(testSet);
    }

    protected static void csvLine(PrintWriter out, List<String> cells) {
        out.println(String.join(",", cells));
    }

}
