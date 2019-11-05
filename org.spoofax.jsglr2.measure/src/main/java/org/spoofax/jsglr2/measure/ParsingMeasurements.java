package org.spoofax.jsglr2.measure;

import org.metaborg.parsetable.IParseTable;
import org.metaborg.parsetable.ParseTableReadException;
import org.metaborg.parsetable.ParseTableReader;
import org.spoofax.jsglr2.elkhound.AbstractElkhoundStackNode;
import org.spoofax.jsglr2.parseforest.ParseForestConstruction;
import org.spoofax.jsglr2.parseforest.ParseForestRepresentation;
import org.spoofax.jsglr2.parseforest.hybrid.HybridDerivation;
import org.spoofax.jsglr2.parseforest.hybrid.HybridParseForest;
import org.spoofax.jsglr2.parseforest.hybrid.HybridParseNode;
import org.spoofax.jsglr2.parser.AbstractParseState;
import org.spoofax.jsglr2.parser.IObservableParser;
import org.spoofax.jsglr2.parser.ParseException;
import org.spoofax.jsglr2.parser.ParserVariant;
import org.spoofax.jsglr2.reducing.Reducing;
import org.spoofax.jsglr2.stack.StackRepresentation;
import org.spoofax.jsglr2.stack.collections.ActiveStacksRepresentation;
import org.spoofax.jsglr2.stack.collections.ForActorStacksRepresentation;
import org.spoofax.jsglr2.testset.StringInput;
import org.spoofax.jsglr2.testset.TestSet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ParsingMeasurements extends Measurements {

    public ParsingMeasurements(TestSet testSet) {
        super(testSet);
    }

    public void measure() throws ParseTableReadException, IOException, ParseException {
        System.out.println(" * Parsing");

        IParseTable parseTable = new ParseTableReader().read(testSetReader.getParseTableTerm());

        ParserVariant variantStandard = new ParserVariant(ActiveStacksRepresentation.ArrayList,
            ForActorStacksRepresentation.ArrayDeque, ParseForestRepresentation.Hybrid, ParseForestConstruction.Full,
            StackRepresentation.HybridElkhound, Reducing.Basic, false);
        ParserVariant variantElkhound = new ParserVariant(ActiveStacksRepresentation.ArrayList,
            ForActorStacksRepresentation.ArrayDeque, ParseForestRepresentation.Hybrid, ParseForestConstruction.Full,
            StackRepresentation.HybridElkhound, Reducing.Elkhound, false);
        ParserVariant variantOptimzedParseForest = new ParserVariant(ActiveStacksRepresentation.ArrayList,
            ForActorStacksRepresentation.ArrayDeque, ParseForestRepresentation.Hybrid,
            ParseForestConstruction.Optimized, StackRepresentation.HybridElkhound, Reducing.Basic, false);

        measure(parseTable, variantStandard, "standard");
        measure(parseTable, variantElkhound, "elkhound");
        measure(parseTable, variantOptimzedParseForest, "optimizedParseForest");
    }

    private void measure(IParseTable parseTable, ParserVariant variant, String postfix)
        throws IOException, ParseException {
        PrintWriter out =
            new PrintWriter(JSGLR2Measurements.REPORT_PATH + testSet.name + "_parsing_" + postfix + ".csv");

        csvHeader(out);

        for(MeasureTestSetReader.InputBatch inputBatch : testSetReader.getInputBatches()) {
            MeasureActiveStacksFactory measureActiveStacksFactory = new MeasureActiveStacksFactory();
            MeasureForActorStacksFactory measureForActorStacksFactory = new MeasureForActorStacksFactory();

            @SuppressWarnings("unchecked") IObservableParser<HybridParseForest, HybridDerivation, HybridParseNode, AbstractElkhoundStackNode<HybridParseForest>, AbstractParseState<AbstractElkhoundStackNode<HybridParseForest>>> parser =
                (IObservableParser<HybridParseForest, HybridDerivation, HybridParseNode, AbstractElkhoundStackNode<HybridParseForest>, AbstractParseState<AbstractElkhoundStackNode<HybridParseForest>>>) variant
                    .getParser(parseTable);

            ParserMeasureObserver<HybridParseForest, HybridDerivation, HybridParseNode> measureObserver =
                new ParserMeasureObserver<>();

            parser.observing().attachObserver(measureObserver);

            for(StringInput input : inputBatch.inputs) {
                parser.parseUnsafe(input.content, input.filename, null);
            }

            if(inputBatch.size != -1)
                System.out.println(
                    "   - Size: " + inputBatch.size + ", Characters: " + measureObserver.length + " (" + postfix + ")");
            else
                System.out.println("   - Characters: " + measureObserver.length + " (" + postfix + ")");

            csvResults(out, inputBatch, measureActiveStacksFactory, measureForActorStacksFactory, measureObserver);
        }

        out.close();
    }

    protected static void csvResults(PrintWriter out, MeasureTestSetReader.InputBatch inputBatch,
        MeasureActiveStacksFactory measureActiveStacksFactory,
        MeasureForActorStacksFactory measureForActorStacksFactory,
        ParserMeasureObserver<HybridParseForest, HybridDerivation, HybridParseNode> measureObserver) {
        List<String> cells = new ArrayList<>();

        int parseNodesSingleDerivation = 0;

        List<HybridParseNode> parseNodesContextFree = new ArrayList<>();
        List<HybridParseNode> parseNodesLexical = new ArrayList<>();
        List<HybridParseNode> parseNodesLayout = new ArrayList<>();

        for(HybridParseNode parseNode : measureObserver.parseNodes) {
            int derivationCount = 0;

            for(HybridDerivation derivation : parseNode.getDerivations())
                derivationCount++;

            if(derivationCount == 1)
                parseNodesSingleDerivation++;

            if(parseNode.production.isContextFree())
                parseNodesContextFree.add(parseNode);

            if(!parseNode.production.isLayout() && parseNode.production.isLexical())
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
                case activeStacksAdds:
                    cells.add("" + measureActiveStacksFactory.measureActiveStacks.adds);
                    break;
                case activeStacksMaxSize:
                    cells.add("" + measureActiveStacksFactory.measureActiveStacks.maxSize);
                    break;
                case activeStacksIsSingleChecks:
                    cells.add("" + measureActiveStacksFactory.measureActiveStacks.iSingleChecks);
                    break;
                case activeStacksIsEmptyChecks:
                    cells.add("" + measureActiveStacksFactory.measureActiveStacks.isEmptyChecks);
                    break;
                case activeStacksFindsWithState:
                    cells.add("" + measureActiveStacksFactory.measureActiveStacks.findsWithState);
                    break;
                case activeStacksForLimitedReductions:
                    cells.add("" + measureActiveStacksFactory.measureActiveStacks.forLimitedReductions);
                    break;
                case activeStacksAddAllTo:
                    cells.add("" + measureActiveStacksFactory.measureActiveStacks.addAllTo);
                    break;
                case activeStacksClears:
                    cells.add("" + measureActiveStacksFactory.measureActiveStacks.clears);
                    break;
                case activeStacksIterators:
                    cells.add("" + measureActiveStacksFactory.measureActiveStacks.iterators);
                    break;
                case forActorAdds:
                    cells.add("" + measureForActorStacksFactory.measureForActorStacks.forActorAdds);
                    break;
                case forActorDelayedAdds:
                    cells.add("" + measureForActorStacksFactory.measureForActorStacks.forActorDelayedAdds);
                    break;
                case forActorMaxSize:
                    cells.add("" + measureForActorStacksFactory.measureForActorStacks.forActorMaxSize);
                    break;
                case forActorDelayedMaxSize:
                    cells.add("" + measureForActorStacksFactory.measureForActorStacks.forActorDelayedMaxSize);
                    break;
                case forActorContainsChecks:
                    cells.add("" + measureForActorStacksFactory.measureForActorStacks.containsChecks);
                    break;
                case forActorNonEmptyChecks:
                    cells.add("" + measureForActorStacksFactory.measureForActorStacks.nonEmptyChecks);
                    break;
                case stackNodes:
                    cells.add("" + measureObserver.stackNodes.size());
                    break;
                case stackNodesSingleLink:
                    cells.add("" + measureObserver.stackNodesSingleLink());
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
                case parseNodesSingleDerivation:
                    cells.add("" + parseNodesSingleDerivation);
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

    private static int parseNodesAmbiguous(Collection<HybridParseNode> parseNodes) {
        int parseNodesAmbiguous = 0;

        for(HybridParseNode parseNode : parseNodes) {
            if(parseNode.isAmbiguous())
                parseNodesAmbiguous++;
        }

        return parseNodesAmbiguous;
    }



    public enum ParsingMeasurement {
        size, characters, activeStacksAdds, activeStacksMaxSize, activeStacksIsSingleChecks, activeStacksIsEmptyChecks,
        activeStacksFindsWithState, activeStacksForLimitedReductions, activeStacksAddAllTo, activeStacksClears,
        activeStacksIterators, forActorAdds, forActorDelayedAdds, forActorMaxSize, forActorDelayedMaxSize,
        forActorContainsChecks, forActorNonEmptyChecks, stackNodes, stackNodesSingleLink, stackLinks,
        stackLinksRejected, deterministicDepthResets, parseNodes, parseNodesSingleDerivation, parseNodesAmbiguous,
        parseNodesContextFree, parseNodesContextFreeAmbiguous, parseNodesLexical, parseNodesLexicalAmbiguous,
        parseNodesLayout, parseNodesLayoutAmbiguous, characterNodes, actors, doReductions, doLimitedReductions,
        doReductionsLR, doReductionsDeterministicGLR, doReductionsNonDeterministicGLR, reducers, reducersElkhound
    }

    private static void csvHeader(PrintWriter out) {
        List<String> cells = new ArrayList<>();

        for(ParsingMeasurement measurement : ParsingMeasurement.values()) {
            cells.add(measurement.name());
        }

        csvLine(out, cells);
    }

}
