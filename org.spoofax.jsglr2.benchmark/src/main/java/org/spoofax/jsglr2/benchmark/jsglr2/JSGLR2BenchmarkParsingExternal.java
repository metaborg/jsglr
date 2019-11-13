package org.spoofax.jsglr2.benchmark.jsglr2;

import org.openjdk.jmh.infra.Blackhole;
import org.spoofax.jsglr2.JSGLR2Variant;
import org.spoofax.jsglr2.benchmark.BenchmarkTestSetReader;
import org.spoofax.jsglr2.integration.IntegrationVariant;
import org.spoofax.jsglr2.integration.ParseTableVariant;
import org.spoofax.jsglr2.parser.ParseException;
import org.spoofax.jsglr2.testset.TestSet;
import org.spoofax.jsglr2.testset.testinput.StringInput;

public class JSGLR2BenchmarkParsingExternal extends JSGLR2Benchmark<String, StringInput> {

    public JSGLR2BenchmarkParsingExternal() {
        String[] args = System.getProperty("language").split(" ");

        TestSet<String, StringInput> testSet = TestSet.fromArgs(args);

        this.testSetReader = new BenchmarkTestSetReader<>(testSet);
    }

    @Override protected IntegrationVariant variant() {
        return new IntegrationVariant(new ParseTableVariant(), JSGLR2Variant.Preset.standard.variant);
    }

    @Override protected boolean implode() {
        return false;
    }

    @Override protected Object action(Blackhole bh, StringInput input) throws ParseException {
        return jsglr2.parser.parseUnsafe(input.content, input.filename, null);
    }

}
