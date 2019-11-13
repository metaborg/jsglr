package org.spoofax.jsglr2.measure;

import org.metaborg.parsetable.ParseTableReadException;
import org.spoofax.jsglr2.parser.ParseException;
import org.spoofax.jsglr2.testset.TestSet;
import org.spoofax.jsglr2.testset.TestSetMultipleInputs;
import org.spoofax.jsglr2.testset.TestSetParseTableFromATerm;

import java.io.File;
import java.io.IOException;
import java.util.Collections;

public class JSGLR2Measurements {

    public static String REPORT_PATH =
        System.getProperty(String.format("%s.%s", JSGLR2Measurements.class.getCanonicalName(), "reportPath"),
            "~/Desktop/jsglr2reports") + "/measurements/";

    public static void main(String[] arg) throws ParseTableReadException, IOException, ParseException {
        if(REPORT_PATH.startsWith("~" + File.separator)) {
            REPORT_PATH = System.getProperty("user.home") + REPORT_PATH.substring(1);
        }

        Iterable<TestSet> testSets;

        if (arg.length == 0)
            testSets = TestSet.all;
        else if (arg.length == 1 && arg[0].split(" ").length == 4) {
            String[] args = arg[0].split(" ");

            String language = args[0];
            String extension = args[1];
            String parseTablePath = args[2];
            String sourcesPath = args[3];

            testSets = Collections.singleton(new TestSet(
                language,
                new TestSetParseTableFromATerm(parseTablePath, false),
                new TestSetMultipleInputs(sourcesPath, extension)
            ));
        } else
            throw new IllegalStateException("invalid arguments");

        System.out.println("Starting measurements...\n");

        new File(REPORT_PATH).mkdirs();

        for(TestSet testSet : testSets) {
            System.out.println(testSet.name);

            new ParseTableMeasurements(testSet).measure();
            new ParsingMeasurements(testSet).measure();
        }

        System.out.println("\nDone");
    }

}
