package org.spoofax.jsglr2.measure;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.spoofax.jsglr2.parsetable.ParseTableReadException;
import org.spoofax.jsglr2.parsetable.ParseTableReader;
import org.spoofax.jsglr2.testset.TestSet;

public class ParseTableMeasurements extends Measurements {

    public ParseTableMeasurements(TestSet testSet) {
        super(testSet);
    }

    public void measure() throws FileNotFoundException, ParseTableReadException {
        System.out.println(" * Parse table");

        PrintWriter out = new PrintWriter(JSGLR2Measurements.REPORT_PATH + testSet.name + "_parsetable.csv");

        csvHeader(out);

        MeasureCharacterClassFactory characterClassFactory = new MeasureCharacterClassFactory();
        MeasureStateFactory stateFactory = new MeasureStateFactory();

        new ParseTableReader(characterClassFactory, stateFactory).read(testSetReader.getParseTableTerm());

        csvResults(out, characterClassFactory, stateFactory);

        out.close();
    }

    protected static void csvResults(PrintWriter out, MeasureCharacterClassFactory characterClassFactory,
        MeasureStateFactory stateFactory) {
        List<String> cells = new ArrayList<String>();

        for(ParseTableMeasurement measurement : ParseTableMeasurement.values()) {
            switch(measurement) {
                case states:
                    cells.add("" + stateFactory.statesCount);
                    break;
                case characterClasses:
                    cells.add("" + characterClassFactory.characterClassesCount);
                    break;
                case characterClassesUnique:
                    cells.add("" + characterClassFactory.characterClassesUnique.size());
                    break;
                case characterClassesOptimizedUnique:
                    cells.add("" + characterClassFactory.characterClassesOptimizedUnique.size());
                    break;
                case gotos:
                    cells.add("" + stateFactory.gotosCount);
                    break;
                case gotosPerStateMax:
                    cells.add("" + stateFactory.gotosMaxPerState);
                    break;
                case actions:
                    cells.add("" + stateFactory.actionsCount);
                    break;
                case actionsPerStateMax:
                    cells.add("" + stateFactory.actionsMaxPerState);
                    break;
                default:
                    break;
            }
        }

        csvLine(out, cells);
    }

    public enum ParseTableMeasurement {
        states, characterClasses, characterClassesUnique, characterClassesOptimizedUnique, gotos, gotosPerStateMax,
        actions, actionsPerStateMax
    }

    private static void csvHeader(PrintWriter out) {
        List<String> cells = new ArrayList<String>();

        for(ParseTableMeasurement measurement : ParseTableMeasurement.values()) {
            cells.add(measurement.name());
        }

        csvLine(out, cells);
    }

}
