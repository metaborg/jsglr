package org.spoofax.jsglr2.measure;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.metaborg.parsetable.ParseTableReadException;
import org.metaborg.parsetable.ParseTableReader;
import org.metaborg.parsetable.actions.ActionsFactory;
import org.metaborg.parsetable.actions.IActionsFactory;
import org.spoofax.jsglr2.testset.StringInput;
import org.spoofax.jsglr2.testset.TestSet;

public class ParseTableMeasurements extends Measurements {

    public ParseTableMeasurements(TestSet<StringInput> testSet) {
        super(testSet);
    }

    public void measure() throws FileNotFoundException, ParseTableReadException {
        System.out.println(" * Parse table");

        PrintWriter out = new PrintWriter(JSGLR2Measurements.REPORT_PATH + testSet.name + "_parsetable.csv");

        csvHeader(out);

        MeasureCharacterClassFactory characterClassFactory = new MeasureCharacterClassFactory();
        IActionsFactory actionsFactory = new ActionsFactory();
        MeasureStateFactory stateFactory = new MeasureStateFactory();

        new ParseTableReader(characterClassFactory, actionsFactory, stateFactory)
            .read(testSetReader.getParseTableTerm());

        csvResults(out, characterClassFactory, stateFactory);

        out.close();
    }

    protected static void csvResults(PrintWriter out, MeasureCharacterClassFactory characterClassFactory,
        MeasureStateFactory stateFactory) {
        List<String> cells = new ArrayList<>();

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
                case statesDisjointSortableCharacterClasses:
                    cells.add("" + stateFactory.statesDisjointSortableCharacterClassesCount);
                    break;
                case gotos:
                    cells.add("" + stateFactory.gotosCount);
                    break;
                case gotosPerStateMax:
                    cells.add("" + stateFactory.gotosPerStateMax);
                    break;
                case actionGroups:
                    cells.add("" + stateFactory.actionGroupsCount);
                    break;
                case actionDisjointSortedRanges:
                    cells.add("" + stateFactory.actionDisjointSortedRangesCount);
                    break;
                case actions:
                    cells.add("" + stateFactory.actionsCount);
                    break;
                case actionGroupsPerStateMax:
                    cells.add("" + stateFactory.actionGroupsPerStateMax);
                    break;
                case actionDisjointSortedRangesPerStateMax:
                    cells.add("" + stateFactory.actionDisjointSortedRangesPerStateMax);
                    break;
                case actionsPerStateMax:
                    cells.add("" + stateFactory.actionsPerStateMax);
                    break;
                case actionsPerGroupMax:
                    cells.add("" + stateFactory.actionsPerGroupMax);
                    break;
                case actionsPerDisjointSortedRangeMax:
                    cells.add("" + stateFactory.actionsPerDisjointSortedRangeMax);
                    break;
                default:
                    break;
            }
        }

        csvLine(out, cells);
    }

    public enum ParseTableMeasurement {
        states, characterClasses, characterClassesUnique, characterClassesOptimizedUnique,
        statesDisjointSortableCharacterClasses, gotos, gotosPerStateMax, actionGroups, actionDisjointSortedRanges,
        actions, actionGroupsPerStateMax, actionDisjointSortedRangesPerStateMax, actionsPerStateMax, actionsPerGroupMax,
        actionsPerDisjointSortedRangeMax
    }

    private static void csvHeader(PrintWriter out) {
        List<String> cells = new ArrayList<>();

        for(ParseTableMeasurement measurement : ParseTableMeasurement.values()) {
            cells.add(measurement.name());
        }

        csvLine(out, cells);
    }

}
