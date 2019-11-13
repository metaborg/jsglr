package org.spoofax.jsglr2.measure;

import java.io.IOException;

import org.metaborg.parsetable.ParseTableReadException;
import org.spoofax.jsglr2.testset.TestSet;
import org.spoofax.jsglr2.testset.testinput.StringInput;

public abstract class Measurements {

    protected TestSet<String, StringInput> testSet;
    protected MeasureTestSetReader<String, StringInput> testSetReader;

    public Measurements(TestSet<String, StringInput> testSet) {
        this.testSet = testSet;
        this.testSetReader = new MeasureTestSetReader<>(testSet);
    }

    protected abstract void measure(JSGLR2Measurements.Config config) throws ParseTableReadException, IOException;

}
