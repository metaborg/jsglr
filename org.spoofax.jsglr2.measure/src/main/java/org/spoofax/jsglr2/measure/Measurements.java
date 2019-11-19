package org.spoofax.jsglr2.measure;

import java.io.IOException;

import org.metaborg.parsetable.ParseTableReadException;
import org.spoofax.jsglr2.testset.TestSetWithParseTable;
import org.spoofax.jsglr2.testset.testinput.StringInput;

public abstract class Measurements {

    protected TestSetWithParseTable<String, StringInput> testSet;
    protected MeasureTestSetWithParseTableReader<String, StringInput> testSetReader;

    public Measurements(TestSetWithParseTable<String, StringInput> testSet) {
        this.testSet = testSet;
        this.testSetReader = new MeasureTestSetWithParseTableReader<>(testSet);
    }

    protected abstract void measure(JSGLR2Measurements.Config config) throws ParseTableReadException, IOException;

}
