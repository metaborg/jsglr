package org.spoofax.jsglr2.measure;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.metaborg.parsetable.ParseTableReadException;
import org.metaborg.util.iterators.Iterables2;
import org.spoofax.jsglr2.measure.incremental.IncrementalParsingMeasurements;
import org.spoofax.jsglr2.measure.parsetable.ParseTableMeasurements;
import org.spoofax.jsglr2.measure.parsing.ParsingMeasurements;
import org.spoofax.jsglr2.testset.TestSet;
import org.spoofax.jsglr2.testset.TestSetInput;
import org.spoofax.jsglr2.testset.TestSetWithParseTable;
import org.spoofax.jsglr2.testset.testinput.IncrementalStringInput;
import org.spoofax.jsglr2.testset.testinput.StringInput;

public class JSGLR2MeasurementsIncremental {

    public static void main(String[] args) throws ParseTableReadException, IOException {
        if(args.length != 1) {
            throw new IllegalStateException("invalid arguments");
        }

        Config<String[], IncrementalStringInput> config = new Config<>(
            Collections.singleton(TestSet.fromArgsWithParseTableIncremental(TestSet.parseArgs(args[0].split(" ")))),
            false);
        Config<String, StringInput> configBatch = configFromLastVersion(config);

        for(TestSetWithParseTable<String[], IncrementalStringInput> testSet : config.testSets) {
            if(config.prefix)
                System.out.println(testSet.name);

            new ParseTableMeasurements(testSetFromLastVersion(testSet)).measure(configBatch);
            new ParsingMeasurements(testSetFromLastVersion(testSet)).measure(configBatch);
            new IncrementalParsingMeasurements(testSet).measure(config);
        }
    }

    private static Config<String, StringInput> configFromLastVersion(Config<String[], IncrementalStringInput> config) {
        return new Config<>(Iterables2.stream(config.testSets)
            .map(JSGLR2MeasurementsIncremental::testSetFromLastVersion).collect(Collectors.toList()), config.prefix);
    }

    private static TestSetWithParseTable<String, StringInput>
        testSetFromLastVersion(TestSetWithParseTable<String[], IncrementalStringInput> testSet) {
        return new TestSetWithParseTable<>(testSet.name, testSet.parseTable,
            new TestSetInput<String, StringInput>(TestSetInput.Type.MULTIPLE, false) {
                @Override protected StringInput getInput(String filename, String input) {
                    return new StringInput(filename, input);
                }

                @Override public List<StringInput> getInputs() throws IOException {
                    return testSet.input.getInputs().stream()
                        .map(incrementalInput -> getInput(incrementalInput.fileName,
                            incrementalInput.content[incrementalInput.content.length - 1]))
                        .collect(Collectors.toList());
                }
            });
    }

}
