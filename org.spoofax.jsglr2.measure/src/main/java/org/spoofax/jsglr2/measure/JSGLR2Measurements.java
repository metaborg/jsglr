package org.spoofax.jsglr2.measure;

import java.io.File;
import java.io.IOException;

import org.spoofax.jsglr2.parser.ParseException;
import org.spoofax.jsglr2.parsetable.ParseTableReadException;
import org.spoofax.jsglr2.testset.TestSet;

public class JSGLR2Measurements {

    public static String REPORT_PATH =
        System.getProperty(String.format("%s.%s", JSGLR2Measurements.class.getCanonicalName(), "reportPath"),
            "~/Desktop/jsglr2reports") + "/measurements/";

    public static void main(String[] args) throws ParseTableReadException, IOException, ParseException {
        if(REPORT_PATH.startsWith("~" + File.separator)) {
            REPORT_PATH = System.getProperty("user.home") + REPORT_PATH.substring(1);
        }

        System.out.println("Starting measurments...\n");

        System.out.println(String.format("%s.%s", JSGLR2Measurements.class.getCanonicalName(), "reportPath"));
        System.out.println(REPORT_PATH);

        new File(REPORT_PATH).mkdirs();

        for(TestSet testSet : TestSet.all) {
            System.out.println(testSet.name);

            new ParseTableMeasurements(testSet).measure();
            new ParsingMeasurements(testSet).measure();
        }

        System.out.println("\nDone");
    }

}
