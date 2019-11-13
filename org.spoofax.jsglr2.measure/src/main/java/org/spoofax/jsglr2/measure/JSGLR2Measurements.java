package org.spoofax.jsglr2.measure;

import java.io.File;
import java.io.IOException;
import java.util.Collections;

import org.metaborg.parsetable.ParseTableReadException;
import org.spoofax.jsglr2.measure.parsetable.ParseTableMeasurements;
import org.spoofax.jsglr2.measure.parsing.ParsingMeasurements;
import org.spoofax.jsglr2.parser.ParseException;
import org.spoofax.jsglr2.testset.TestSet;
import org.spoofax.jsglr2.testset.TestSetMultipleInputs;
import org.spoofax.jsglr2.testset.TestSetParseTableFromATerm;
import org.spoofax.jsglr2.testset.testinput.StringInput;

public class JSGLR2Measurements {

    private static String REPORT_PATH = System.getProperty("reportPath", "~/jsglr2measurements");

    public static void main(String[] args) throws ParseTableReadException, IOException, ParseException {
        if(REPORT_PATH.startsWith("~" + File.separator)) {
            REPORT_PATH = System.getProperty("user.home") + REPORT_PATH.substring(1);
        }

        new File(REPORT_PATH).mkdirs();

        Config config = getConfig(args);

        for(TestSet<String, StringInput> testSet : config.testSets) {
            if(config.prefix)
                System.out.println(testSet.name);

            new ParseTableMeasurements(testSet).measure(config);
            new ParsingMeasurements(testSet).measure(config);
        }
    }

    private static Config getConfig(String[] arg) {
        if(arg.length == 0)
            return new Config(TestSet.all, true);
        else if(arg.length == 1) {
            String[] args = arg[0].split(" ");

            if(args.length == 4) {
                String language = args[0];
                String extension = args[1];
                String parseTablePath = args[2];
                String sourcesPath = args[3];

                TestSetParseTableFromATerm parseTable = new TestSetParseTableFromATerm(parseTablePath, false);
                TestSetMultipleInputs<StringInput> input =
                    new TestSetMultipleInputs.StringInputSet(sourcesPath, extension);

                TestSet<String, StringInput> testSet = new TestSet<>(language, parseTable, input);

                return new Config(Collections.singleton(testSet), false);
            }
        }

        throw new IllegalStateException("invalid arguments");
    }

    public static class Config {

        final Iterable<TestSet<String, StringInput>> testSets;
        final boolean prefix;

        Config(Iterable<TestSet<String, StringInput>> testSets, boolean prefix) {
            this.testSets = testSets;
            this.prefix = prefix;
        }

        public String prefix(TestSet testSet) {
            if(prefix)
                return JSGLR2Measurements.REPORT_PATH + "/" + testSet.name + "_";
            else
                return JSGLR2Measurements.REPORT_PATH + "/";
        }

    }

}
