package org.spoofax.jsglr2.benchmark.jsglr2;

import org.openjdk.jmh.annotations.Param;
import org.spoofax.jsglr2.testset.TestSet;

public class JSGLR2Java8GitBenchmarkIncrementalParsing extends JSGLR2BenchmarkIncrementalParsing {

    @Param({ "-1", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17",
        "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35",
        "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49" }) public int i;

    public JSGLR2Java8GitBenchmarkIncrementalParsing() {
        super(TestSet.java8IncrementalGit);
    }

}
