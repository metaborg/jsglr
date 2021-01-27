package org.spoofax.jsglr2.measure;

import org.spoofax.jsglr2.testset.TestSet;
import org.spoofax.jsglr2.testset.TestSetWithParseTable;
import org.spoofax.jsglr2.testset.testinput.TestInput;

public class Config<ContentType, Input extends TestInput<ContentType>> {

    final Iterable<TestSetWithParseTable<ContentType, Input>> testSets;
    final boolean prefix;

    Config(Iterable<TestSetWithParseTable<ContentType, Input>> testSets, boolean prefix) {
        this.testSets = testSets;
        this.prefix = prefix;
    }

    public String prefix(TestSet<ContentType, Input> testSet) {
        if (prefix)
            return JSGLR2Measurements.REPORT_PATH + "/" + testSet.name + "_";
        else
            return JSGLR2Measurements.REPORT_PATH + "/";
    }

}
