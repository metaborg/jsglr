package org.spoofax.jsglr2.measure;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.spoofax.jsglr2.JSGLR2Variants;
import org.spoofax.jsglr2.JSGLR2Variants.ParseForestConstruction;
import org.spoofax.jsglr2.JSGLR2Variants.ParseForestRepresentation;
import org.spoofax.jsglr2.JSGLR2Variants.Reducing;
import org.spoofax.jsglr2.JSGLR2Variants.StackRepresentation;
import org.spoofax.jsglr2.parseforest.hybrid.Derivation;
import org.spoofax.jsglr2.parseforest.hybrid.HybridParseForest;
import org.spoofax.jsglr2.parseforest.hybrid.ParseNode;
import org.spoofax.jsglr2.parser.ParseException;
import org.spoofax.jsglr2.parser.Parser;
import org.spoofax.jsglr2.parsetable.IParseTable;
import org.spoofax.jsglr2.parsetable.ParseTableReadException;
import org.spoofax.jsglr2.parsetable.ParseTableReader;
import org.spoofax.jsglr2.stack.elkhound.AbstractElkhoundStackNode;
import org.spoofax.jsglr2.testset.Input;
import org.spoofax.jsglr2.testset.TestSet;
import org.spoofax.jsglr2.testset.TestSetReader;

public class ParsingMeasurements extends Measurements {

    public ParsingMeasurements(TestSet testSet) {
        super(testSet);
    }

    public void measure() throws ParseTableReadException, IOException, ParseException {
        System.out.println(" * Parsing");

        IParseTable parseTable = new ParseTableReader().read(testSetReader.getParseTableTerm());

        JSGLR2Variants.Variant variantElkhound = new JSGLR2Variants.Variant(ParseForestRepresentation.Hybrid,
            ParseForestConstruction.Full, StackRepresentation.BasicElkhound, Reducing.Elkhound);
        JSGLR2Variants.Variant variantOptimzedParseForest = new JSGLR2Variants.Variant(ParseForestRepresentation.Hybrid,
            ParseForestConstruction.Optimized, StackRepresentation.BasicElkhound, Reducing.Elkhound);

        measure(parseTable, variantElkhound, "elkhound");
        measure(parseTable, variantOptimzedParseForest, "optimizedParseForest");
    }

    private void measure(IParseTable parseTable, JSGLR2Variants.Variant variant, String postfix)
        throws ParseTableReadException, IOException, ParseException {
        PrintWriter out =
            new PrintWriter(JSGLR2Measurements.REPORT_PATH + testSet.name + "_parsing_" + postfix + ".csv");

        csvHeader(out);

        for(TestSetReader.InputBatch inputBatch : testSetReader.getInputBatches()) {
            @SuppressWarnings("unchecked") Parser<AbstractElkhoundStackNode<HybridParseForest>, HybridParseForest, ParseNode, Derivation> parser =
                (Parser<AbstractElkhoundStackNode<HybridParseForest>, HybridParseForest, ParseNode, Derivation>) JSGLR2Variants
                    .getParser(parseTable, variant);

            ParserMeasureObserver<HybridParseForest> measureObserver = new ParserMeasureObserver<HybridParseForest>();

            parser.attachObserver(measureObserver);

            for(Input input : inputBatch.inputs) {
                parser.parseUnsafe(input.content, input.filename, null);
            }

            if(inputBatch.size != -1)
                System.out.println(
                    "   - Size: " + inputBatch.size + ", Characters: " + measureObserver.length + " (" + postfix + ")");
            else
                System.out.println("   - Characters: " + measureObserver.length + " (" + postfix + ")");

            csvResults(out, inputBatch, measureObserver);
        }

        out.close();
    }

    protected static void csvResults(PrintWriter out, TestSetReader.InputBatch inputBatch,
        ParserMeasureObserver<HybridParseForest> measureObserver) {
        List<String> cells = new ArrayList<String>();

        List<ParseNode> parseNodesContextFree = new ArrayList<ParseNode>();
        List<ParseNode> parseNodesLexical = new ArrayList<ParseNode>();
        List<ParseNode> parseNodesLayout = new ArrayList<ParseNode>();

        for(ParseNode parseNode : measureObserver.parseNodes) {
            if(parseNode.production.isContextFree())
                parseNodesContextFree.add(parseNode);

            if(!parseNode.production.isLayout()
                && (parseNode.production.isLexical() || parseNode.production.isLexicalRhs()))
                parseNodesLexical.add(parseNode);

            if(parseNode.production.isLayout())
                parseNodesLayout.add(parseNode);
        }

        for(ParsingMeasurement measurement : ParsingMeasurement.values()) {
            switch(measurement) {
                case size:
                    cells.add("" + inputBatch.size);
                    break;
                case characters:
                    cells.add("" + measureObserver.length);
                    break;
                case stackNodes:
                    cells.add("" + measureObserver.stackNodes.size());
                    break;
                case stackLinks:
                    cells.add("" + measureObserver.stackLinks.size());
                    break;
                case stackLinksRejected:
                    cells.add("" + measureObserver.stackLinksRejected.size());
                    break;
                case deterministicDepthResets:
                    cells.add("" + measureObserver.deterministicDepthResets);
                    break;
                case parseNodes:
                    cells.add("" + measureObserver.parseNodes.size());
                    break;
                case parseNodesAmbiguous:
                    cells.add("" + parseNodesAmbiguous(measureObserver.parseNodes));
                    break;
                case parseNodesContextFree:
                    cells.add("" + parseNodesContextFree.size());
                    break;
                case parseNodesContextFreeAmbiguous:
                    cells.add("" + parseNodesAmbiguous(parseNodesContextFree));
                    break;
                case parseNodesLexical:
                    cells.add("" + parseNodesLexical.size());
                    break;
                case parseNodesLexicalAmbiguous:
                    cells.add("" + parseNodesAmbiguous(parseNodesLexical));
                    break;
                case parseNodesLayout:
                    cells.add("" + parseNodesLayout.size());
                    break;
                case parseNodesLayoutAmbiguous:
                    cells.add("" + parseNodesAmbiguous(parseNodesLayout));
                    break;
                case characterNodes:
                    cells.add("" + measureObserver.characterNodes.size());
                    break;
                case actors:
                    cells.add("" + measureObserver.actors.size());
                    break;
                case doReductions:
                    cells.add("" + measureObserver.doReductions);
                    break;
                case doLimitedReductions:
                    cells.add("" + measureObserver.doLimitedReductions);
                    break;
                case doReductionsLR:
                    cells.add("" + measureObserver.doReductionsLR);
                    break;
                case doReductionsDeterministicGLR:
                    cells.add("" + measureObserver.doReductionsDeterministicGLR);
                    break;
                case doReductionsNonDeterministicGLR:
                    cells.add("" + measureObserver.doReductionsNonDeterministicGLR);
                    break;
                case reducers:
                    cells.add("" + measureObserver.reducers.size());
                    break;
                case reducersElkhound:
                    cells.add("" + measureObserver.reducersElkhound.size());
                    break;
                default:
                    break;
            }
        }

        csvLine(out, cells);
    }

    private static int parseNodesAmbiguous(Collection<ParseNode> parseNodes) {
        int parseNodesAmbiguous = 0;

        for(ParseNode parseNode : parseNodes) {
            if(parseNode.isAmbiguous())
                parseNodesAmbiguous++;
        }

        return parseNodesAmbiguous;
    }

    public enum ParsingMeasurement {
        size, characters, stackNodes, stackLinks, stackLinksRejected, deterministicDepthResets, parseNodes,
        parseNodesAmbiguous, parseNodesContextFree, parseNodesContextFreeAmbiguous, parseNodesLexical,
        parseNodesLexicalAmbiguous, parseNodesLayout, parseNodesLayoutAmbiguous, characterNodes, actors, doReductions,
        doLimitedReductions, doReductionsLR, doReductionsDeterministicGLR, doReductionsNonDeterministicGLR, reducers,
        reducersElkhound
    }

}
