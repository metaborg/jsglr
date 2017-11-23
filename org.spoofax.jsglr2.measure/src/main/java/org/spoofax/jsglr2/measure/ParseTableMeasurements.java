package org.spoofax.jsglr2.measure;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

import org.spoofax.jsglr2.characters.ICharacterClassFactory;
import org.spoofax.jsglr2.parsetable.IStateFactory;
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

        ICharacterClassFactory characterClassFactory = new MeasureCharacterClassFactory();
        IStateFactory stateFactory = new MeasureStateFactory();

        new ParseTableReader(characterClassFactory, stateFactory).read(testSetReader.getParseTableTerm());

        // TODO: write results to CSV

        out.close();
    }

}
