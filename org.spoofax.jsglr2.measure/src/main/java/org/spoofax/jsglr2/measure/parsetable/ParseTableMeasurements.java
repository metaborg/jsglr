package org.spoofax.jsglr2.measure.parsetable;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.metaborg.parsetable.ParseTableReadException;
import org.metaborg.parsetable.ParseTableReader;
import org.metaborg.parsetable.actions.ActionsFactory;
import org.metaborg.parsetable.actions.IActionsFactory;
import org.spoofax.jsglr2.measure.CSV;
import org.spoofax.jsglr2.measure.JSGLR2Measurements;
import org.spoofax.jsglr2.measure.Measurements;
import org.spoofax.jsglr2.testset.TestSet;
import org.spoofax.jsglr2.testset.testinput.StringInput;

public class ParseTableMeasurements extends Measurements {

    public ParseTableMeasurements(TestSet<String, StringInput> testSet) {
        super(testSet);
    }

    @Override public void measure(JSGLR2Measurements.Config config) throws FileNotFoundException, ParseTableReadException {
        CSV<ParseTableMeasurement> output = new CSV<>(ParseTableMeasurement.values());

        MeasureCharacterClassFactory characterClassFactory = new MeasureCharacterClassFactory();
        IActionsFactory actionsFactory = new ActionsFactory();
        MeasureStateFactory stateFactory = new MeasureStateFactory();

        new ParseTableReader(characterClassFactory, actionsFactory, stateFactory)
            .read(testSetReader.getParseTableTerm());

        output.addRow(toOutput(characterClassFactory, stateFactory));

        output.write(config.prefix(testSet) + "parsetable.csv");
    }

    private static Map<ParseTableMeasurement, String> toOutput(MeasureCharacterClassFactory characterClassFactory,
        MeasureStateFactory stateFactory) {
        return Arrays.stream(ParseTableMeasurement.values())
            .collect(Collectors.toMap(Function.identity(), measurement -> {
                switch(measurement) {
                    case states:
                        return "" + stateFactory.statesCount;
                    case characterClasses:
                        return "" + characterClassFactory.characterClassesCount;
                    case characterClassesUnique:
                        return "" + characterClassFactory.characterClassesUnique.size();
                    case characterClassesOptimizedUnique:
                        return "" + characterClassFactory.characterClassesOptimizedUnique.size();
                    case statesDisjointSortableCharacterClasses:
                        return "" + stateFactory.statesDisjointSortableCharacterClassesCount;
                    case gotos:
                        return "" + stateFactory.gotosCount;
                    case gotosPerStateMax:
                        return "" + stateFactory.gotosPerStateMax;
                    case actionGroups:
                        return "" + stateFactory.actionGroupsCount;
                    case actionDisjointSortedRanges:
                        return "" + stateFactory.actionDisjointSortedRangesCount;
                    case actions:
                        return "" + stateFactory.actionsCount;
                    case actionGroupsPerStateMax:
                        return "" + stateFactory.actionGroupsPerStateMax;
                    case actionDisjointSortedRangesPerStateMax:
                        return "" + stateFactory.actionDisjointSortedRangesPerStateMax;
                    case actionsPerStateMax:
                        return "" + stateFactory.actionsPerStateMax;
                    case actionsPerGroupMax:
                        return "" + stateFactory.actionsPerGroupMax;
                    case actionsPerDisjointSortedRangeMax:
                        return "" + stateFactory.actionsPerDisjointSortedRangeMax;
                    default:
                        return "";
                }
            }));
    }

}
