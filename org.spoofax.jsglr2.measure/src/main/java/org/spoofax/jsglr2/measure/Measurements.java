package org.spoofax.jsglr2.measure;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.spoofax.jsglr2.measure.ParsingMeasurements.ParsingMeasurement;
import org.spoofax.jsglr2.testset.TestSet;
import org.spoofax.jsglr2.testset.TestSetReader;

public abstract class Measurements {

    protected TestSet testSet;
    protected TestSetReader testSetReader;

    public Measurements(TestSet testSet) {
        this.testSet = testSet;
        this.testSetReader = new MeasureTestsetReader(testSet);
    }

    protected static void csvHeader(PrintWriter out) {
        List<String> cells = new ArrayList<String>();

        for(ParsingMeasurement measurement : ParsingMeasurement.values()) {
            cells.add(measurement.name());
        }

        csvLine(out, cells);
    }

    protected static void csvLine(PrintWriter out, List<String> cells) {
        out.println(String.join(",", cells));
    }

}
