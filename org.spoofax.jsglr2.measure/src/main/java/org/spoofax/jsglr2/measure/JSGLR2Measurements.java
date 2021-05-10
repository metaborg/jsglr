package org.spoofax.jsglr2.measure;

import java.io.File;
import java.io.IOException;
import java.util.Collections;

import org.metaborg.parsetable.ParseTableReadException;
import org.spoofax.jsglr2.measure.parsetable.ParseTableMeasurements;
import org.spoofax.jsglr2.measure.parsing.ParsingMeasurements;
import org.spoofax.jsglr2.testset.TestSet;
import org.spoofax.jsglr2.testset.TestSetWithParseTable;
import org.spoofax.jsglr2.testset.testinput.StringInput;

public class JSGLR2Measurements {

    static final String REPORT_PATH;
    static {
        String reportPath = System.getProperty("reportPath", "~/jsglr2measurements");
        if(reportPath.startsWith("~" + File.separator)) {
            reportPath = System.getProperty("user.home") + reportPath.substring(1);
        }
        REPORT_PATH = reportPath;

        new File(REPORT_PATH).mkdirs();
    }

    public static void main(String[] args) throws ParseTableReadException, IOException {
        Config<String, StringInput> config = getConfig(args);

        for(TestSetWithParseTable<String, StringInput> testSet : config.testSets) {
            if(config.prefix)
                System.out.println(testSet.name);

            new ParseTableMeasurements(testSet).measure(config);
            new ParsingMeasurements(testSet).measure(config);
        }
    }

    private static Config<String, StringInput> getConfig(String[] args) {
        if(args.length == 0)
            return new Config<>(TestSet.all, true);
        else if(args.length == 1)
            return new Config<>(
                Collections.singleton(TestSet.fromArgsWithParseTable(TestSet.parseArgs(args[0].split(" ")))), false);

        throw new IllegalStateException("invalid arguments");
    }

}
