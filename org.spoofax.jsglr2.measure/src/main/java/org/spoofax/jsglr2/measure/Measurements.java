package org.spoofax.jsglr2.measure;

import java.io.IOException;

import org.metaborg.parsetable.ParseTableReadException;
import org.spoofax.jsglr2.testset.TestSetWithParseTable;
import org.spoofax.jsglr2.testset.testinput.TestInput;

public abstract class Measurements<ContentType, Input extends TestInput<ContentType>> {

    protected TestSetWithParseTable<ContentType, Input> testSet;
    protected MeasureTestSetWithParseTableReader<ContentType, Input> testSetReader;

    public Measurements(TestSetWithParseTable<ContentType, Input> testSet) {
        this.testSet = testSet;
        this.testSetReader = new MeasureTestSetWithParseTableReader<>(testSet);
    }

    protected abstract void measure(Config<ContentType, Input> config) throws ParseTableReadException, IOException;

}
