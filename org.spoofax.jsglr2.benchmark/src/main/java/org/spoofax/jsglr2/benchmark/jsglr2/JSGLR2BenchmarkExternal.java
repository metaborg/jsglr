package org.spoofax.jsglr2.benchmark.jsglr2;

import org.metaborg.parsetable.ParseTableVariant;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.infra.Blackhole;
import org.spoofax.jsglr2.JSGLR2Variant;
import org.spoofax.jsglr2.benchmark.BenchmarkTestSetWithParseTableReader;
import org.spoofax.jsglr2.integration.IntegrationVariant;
import org.spoofax.jsglr2.parser.ParseException;
import org.spoofax.jsglr2.testset.TestSet;
import org.spoofax.jsglr2.testset.TestSetWithParseTable;
import org.spoofax.jsglr2.testset.testinput.StringInput;

public class JSGLR2BenchmarkExternal extends JSGLR2Benchmark<String, StringInput> {

    public JSGLR2BenchmarkExternal() {
        String[] args = System.getProperty("testSet").split(" ");

        TestSetWithParseTable<String, StringInput> testSet = TestSet.fromArgsWithParseTable(TestSet.parseArgs(args));

        setTestSetReader(new BenchmarkTestSetWithParseTableReader<>(testSet));
    }

    @Param({ "standard", "elkhound", "recovery", "incremental", "recoveryElkhound", "recoveryIncremental" }) JSGLR2Variant.Preset variant;

    @Param({ "false", "true" }) public boolean implode;

    @Override protected IntegrationVariant variant() {
        return new IntegrationVariant(new ParseTableVariant(), variant.variant);
    }

    @Override protected boolean implode() {
        return implode;
    }

    @Override protected Object action(Blackhole bh, StringInput input) throws ParseException {
        if(implode)
            return jsglr2.parseUnsafe(input.content);
        else
            return jsglr2.parser.parseUnsafe(input.content);
    }

}
