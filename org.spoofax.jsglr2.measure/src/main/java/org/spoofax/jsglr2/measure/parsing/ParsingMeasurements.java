package org.spoofax.jsglr2.measure.parsing;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.metaborg.parsetable.IParseTable;
import org.metaborg.parsetable.ParseTableReadException;
import org.metaborg.parsetable.ParseTableReader;
import org.spoofax.jsglr2.measure.JSGLR2Measurements;
import org.spoofax.jsglr2.measure.MeasureTestSetReader;
import org.spoofax.jsglr2.measure.Measurements;
import org.spoofax.jsglr2.parseforest.*;
import org.spoofax.jsglr2.parser.AbstractParseState;
import org.spoofax.jsglr2.parser.IObservableParser;
import org.spoofax.jsglr2.parser.ParserVariant;
import org.spoofax.jsglr2.reducing.Reducing;
import org.spoofax.jsglr2.stack.IStackNode;
import org.spoofax.jsglr2.stack.StackRepresentation;
import org.spoofax.jsglr2.stack.collections.ActiveStacksRepresentation;
import org.spoofax.jsglr2.stack.collections.ForActorStacksRepresentation;
import org.spoofax.jsglr2.testset.TestSet;
import org.spoofax.jsglr2.testset.testinput.StringInput;

public class ParsingMeasurements extends Measurements {

    public ParsingMeasurements(TestSet<String, StringInput> testSet) {
        super(testSet);
    }

    public void measure() throws ParseTableReadException, IOException {
        System.out.println(" * Parsing");

        IParseTable parseTable = new ParseTableReader().read(testSetReader.getParseTableTerm());

        ParserVariant variantStandard =
        //@formatter:off
            new ParserVariant(
                ActiveStacksRepresentation.ArrayList,
                ForActorStacksRepresentation.ArrayDeque,
                ParseForestRepresentation.Hybrid,
                ParseForestConstruction.Full,
                StackRepresentation.Hybrid,
                Reducing.Basic,
                false
            );
            //@formatter:on

        ParserVariant variantElkhound =
        //@formatter:off
            new ParserVariant(
                ActiveStacksRepresentation.ArrayList,
                ForActorStacksRepresentation.ArrayDeque,
                ParseForestRepresentation.Hybrid,
                ParseForestConstruction.Full,
                StackRepresentation.HybridElkhound,
                Reducing.Elkhound,
                false
            );
        //@formatter:on

        measure(variantStandard, parseTable, new StandardParserMeasureObserver<>(), "standard");
        measure(variantElkhound, parseTable, new ElkhoundParserMeasureObserver<>(), "elkhound");
    }

    private
//@formatter:off
   <ParseForest extends IParseForest,
    Derivation  extends IDerivation<ParseForest>,
    ParseNode   extends IParseNode<ParseForest, Derivation>,
    StackNode   extends IStackNode,
    ParseState  extends AbstractParseState<?, StackNode>>
//@formatter:on
    void measure(ParserVariant variant, IParseTable parseTable,
        ParserMeasureObserver<ParseForest, Derivation, ParseNode, StackNode, ParseState> measureObserver,
        String postfix) throws IOException {
        PrintWriter out =
            new PrintWriter(JSGLR2Measurements.REPORT_PATH + testSet.name + "_parsing_" + postfix + ".csv");

        csvHeader(out);

        testSetReader.getInputBatches().forEach(inputBatch -> {
            MeasureActiveStacksFactory measureActiveStacksFactory = new MeasureActiveStacksFactory();
            MeasureForActorStacksFactory measureForActorStacksFactory = new MeasureForActorStacksFactory();

            @SuppressWarnings("unchecked") IObservableParser<ParseForest, Derivation, ParseNode, StackNode, ParseState> parser =
                (IObservableParser<ParseForest, Derivation, ParseNode, StackNode, ParseState>) variant
                    .getParser(parseTable, measureActiveStacksFactory, measureForActorStacksFactory);

            parser.observing().attachObserver(measureObserver);

            for(StringInput input : inputBatch.inputs) {
                parser.parse(input.content, input.filename, null);
            }

            if(inputBatch.size != -1)
                System.out.println(
                    "   - Size: " + inputBatch.size + ", Characters: " + measureObserver.length + " (" + postfix + ")");
            else
                System.out.println("   - Characters: " + measureObserver.length + " (" + postfix + ")");

            csvResults(out, inputBatch, measureActiveStacksFactory, measureForActorStacksFactory, measureObserver);
        });

        out.close();
    }

    protected
//@formatter:off
   <ParseForest extends IParseForest,
    Derivation  extends IDerivation<ParseForest>,
    ParseNode   extends IParseNode<ParseForest, Derivation>,
    StackNode   extends IStackNode,
    ParseState  extends AbstractParseState<?, StackNode>>
//@formatter:on
    void csvResults(PrintWriter out, MeasureTestSetReader.InputBatch inputBatch,
        MeasureActiveStacksFactory measureActiveStacksFactory,
        MeasureForActorStacksFactory measureForActorStacksFactory,
        ParserMeasureObserver<ParseForest, Derivation, ParseNode, StackNode, ParseState> measureObserver) {
        List<String> cells = new ArrayList<>();

        int parseNodesSingleDerivation = 0;

        List<IParseNode> parseNodesContextFree = new ArrayList<>();
        List<IParseNode> parseNodesLexical = new ArrayList<>();
        List<IParseNode> parseNodesLayout = new ArrayList<>();

        for(IParseNode parseNode : measureObserver.parseNodes) {
            int derivationCount = 0;

            for(Object derivation : parseNode.getDerivations())
                derivationCount++;

            if(derivationCount == 1)
                parseNodesSingleDerivation++;

            if(parseNode.production().isContextFree())
                parseNodesContextFree.add(parseNode);

            if(!parseNode.production().isLayout() && parseNode.production().isLexical())
                parseNodesLexical.add(parseNode);

            if(parseNode.production().isLayout())
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

    private static int parseNodesAmbiguous(Collection<IParseNode> parseNodes) {
        int parseNodesAmbiguous = 0;

        for(IParseNode parseNode : parseNodes) {
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
