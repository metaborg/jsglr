package org.spoofax.jsglr2.measure;

import java.io.PrintWriter;
import java.util.List;

import org.spoofax.jsglr2.testset.TestSet;
import org.spoofax.jsglr2.testset.testinput.StringInput;

public abstract class Measurements {

    protected TestSet<String, StringInput> testSet;
    protected MeasureTestSetReader<String, StringInput> testSetReader;

    public Measurements(TestSet<String, StringInput> testSet) {
        this.testSet = testSet;
        this.testSetReader = new MeasureTestSetReader<>(testSet);
    }

    protected static void csvLine(PrintWriter out, List<String> cells) {
        out.println(String.join(",", cells));
    }

}
