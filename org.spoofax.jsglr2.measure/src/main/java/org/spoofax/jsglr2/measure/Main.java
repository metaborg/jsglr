package org.spoofax.jsglr2.measure;

import java.io.File;
import java.io.IOException;

import org.spoofax.jsglr2.parser.ParseException;
import org.spoofax.jsglr2.parsetable.ParseTableReadException;
import org.spoofax.jsglr2.testset.TestSet;

public class Main {

    public static String reportPath = "/Users/Jasper/Dropbox/Thesis/r/measurements/";

    public static void main(String[] args) throws ParseTableReadException, IOException, ParseException {
        System.out.println("Starting measurments...\n");

        new File(reportPath).mkdirs();

        for(TestSet testSet : TestSet.all) {
            System.out.println(testSet.name);

            new ParsingMeasurements(testSet).measure();
            new ParseTableMeasurements(testSet).measure();
        }

        System.out.println("\nDone");
    }

}
