package org.spoofax.jsglr2.benchmark.jsglr2;

import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.infra.Blackhole;
import org.spoofax.jsglr2.benchmark.BenchmarkTestSetWithParseTableReader;
import org.spoofax.jsglr2.parser.ParseException;
import org.spoofax.jsglr2.testset.TestSet;
import org.spoofax.jsglr2.testset.testinput.IncrementalStringInput;

public class JSGLR2OCamlGitBenchmarkIncrementalParsing extends JSGLR2BenchmarkIncrementalParsing {

    @Param({ "-1", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17",
        "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35",
        "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49" }) public int i;

    public JSGLR2OCamlGitBenchmarkIncrementalParsing() {
        setTestSetReader(new BenchmarkTestSetWithParseTableReader<>(TestSet.ocamlIncrementalGit));
    }

    // TODO Some of the ML files don't parse :shrug:
    @Override protected Object action(Blackhole bh, IncrementalStringInput input) {
        try {
            return super.action(bh, input);
        } catch(ParseException ignored) {
            return null;
        }
    }

}
