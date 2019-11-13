package org.spoofax.jsglr2.measure.parsing;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.metaborg.parsetable.IParseTable;
import org.metaborg.parsetable.ParseTableReadException;
import org.metaborg.parsetable.ParseTableReader;
import org.spoofax.jsglr2.measure.CSV;
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
        CSV<ParsingMeasurement> output = new CSV<>(ParsingMeasurement.values());

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

            output.addRow(toOutput(inputBatch, measureActiveStacksFactory, measureForActorStacksFactory, measureObserver));
        });

        output.write(JSGLR2Measurements.REPORT_PATH + testSet.name + "_parsing_" + postfix + ".csv");
    }

    private
//@formatter:off
   <ParseForest extends IParseForest,
    Derivation  extends IDerivation<ParseForest>,
    ParseNode   extends IParseNode<ParseForest, Derivation>,
    StackNode   extends IStackNode,
    ParseState  extends AbstractParseState<?, StackNode>>
//@formatter:on
    Map<ParsingMeasurement, String> toOutput(MeasureTestSetReader.InputBatch inputBatch,
        MeasureActiveStacksFactory measureActiveStacksFactory,
        MeasureForActorStacksFactory measureForActorStacksFactory,
        ParserMeasureObserver<ParseForest, Derivation, ParseNode, StackNode, ParseState> measureObserver) {
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

        int finalParseNodesSingleDerivation = parseNodesSingleDerivation;

        return Arrays.stream(ParsingMeasurement.values()).collect(Collectors.toMap(Function.identity(), measurement -> {
            switch(measurement) {
                case size:
                    return "" + inputBatch.size;
                case characters:
                    return "" + measureObserver.length;
                case activeStacksAdds:
                    return "" + measureActiveStacksFactory.measureActiveStacks.adds;
                case activeStacksMaxSize:
                    return "" + measureActiveStacksFactory.measureActiveStacks.maxSize;
                case activeStacksIsSingleChecks:
                    return "" + measureActiveStacksFactory.measureActiveStacks.iSingleChecks;
                case activeStacksIsEmptyChecks:
                    return "" + measureActiveStacksFactory.measureActiveStacks.isEmptyChecks;
                case activeStacksFindsWithState:
                    return "" + measureActiveStacksFactory.measureActiveStacks.findsWithState;
                case activeStacksForLimitedReductions:
                    return "" + measureActiveStacksFactory.measureActiveStacks.forLimitedReductions;
                case activeStacksAddAllTo:
                    return "" + measureActiveStacksFactory.measureActiveStacks.addAllTo;
                case activeStacksClears:
                    return "" + measureActiveStacksFactory.measureActiveStacks.clears;
                case activeStacksIterators:
                    return "" + measureActiveStacksFactory.measureActiveStacks.iterators;
                case forActorAdds:
                    return "" + measureForActorStacksFactory.measureForActorStacks.forActorAdds;
                case forActorDelayedAdds:
                    return "" + measureForActorStacksFactory.measureForActorStacks.forActorDelayedAdds;
                case forActorMaxSize:
                    return "" + measureForActorStacksFactory.measureForActorStacks.forActorMaxSize;
                case forActorDelayedMaxSize:
                    return "" + measureForActorStacksFactory.measureForActorStacks.forActorDelayedMaxSize;
                case forActorContainsChecks:
                    return "" + measureForActorStacksFactory.measureForActorStacks.containsChecks;
                case forActorNonEmptyChecks:
                    return "" + measureForActorStacksFactory.measureForActorStacks.nonEmptyChecks;
                case stackNodes:
                    return "" + measureObserver.stackNodes.size();
                case stackNodesSingleLink:
                    return "" + measureObserver.stackNodesSingleLink();
                case stackLinks:
                    return "" + measureObserver.stackLinks.size();
                case stackLinksRejected:
                    return "" + measureObserver.stackLinksRejected.size();
                case deterministicDepthResets:
                    return "" + measureObserver.deterministicDepthResets;
                case parseNodes:
                    return "" + measureObserver.parseNodes.size();
                case parseNodesSingleDerivation:
                    return "" + finalParseNodesSingleDerivation;
                case parseNodesAmbiguous:
                    return "" + parseNodesAmbiguous(measureObserver.parseNodes);
                case parseNodesContextFree:
                    return "" + parseNodesContextFree.size();
                case parseNodesContextFreeAmbiguous:
                    return "" + parseNodesAmbiguous(parseNodesContextFree);
                case parseNodesLexical:
                    return "" + parseNodesLexical.size();
                case parseNodesLexicalAmbiguous:
                    return "" + parseNodesAmbiguous(parseNodesLexical);
                case parseNodesLayout:
                    return "" + parseNodesLayout.size();
                case parseNodesLayoutAmbiguous:
                    return "" + parseNodesAmbiguous(parseNodesLayout);
                case characterNodes:
                    return "" + measureObserver.characterNodes.size();
                case actors:
                    return "" + measureObserver.actors.size();
                case doReductions:
                    return "" + measureObserver.doReductions;
                case doLimitedReductions:
                    return "" + measureObserver.doLimitedReductions;
                case doReductionsLR:
                    return "" + measureObserver.doReductionsLR;
                case doReductionsDeterministicGLR:
                    return "" + measureObserver.doReductionsDeterministicGLR;
                case doReductionsNonDeterministicGLR:
                    return "" + measureObserver.doReductionsNonDeterministicGLR;
                case reducers:
                    return "" + measureObserver.reducers.size();
                case reducersElkhound:
                    return "" + measureObserver.reducersElkhound.size();
                default:
                    return "";
            }
        }));
    }

    private static int parseNodesAmbiguous(Collection<IParseNode> parseNodes) {
        int parseNodesAmbiguous = 0;

        for(IParseNode parseNode : parseNodes) {
            if(parseNode.isAmbiguous())
                parseNodesAmbiguous++;
        }

        return parseNodesAmbiguous;
    }

}
