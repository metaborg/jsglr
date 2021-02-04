package org.spoofax.jsglr2.measure.incremental;

import static org.spoofax.jsglr2.measure.incremental.IncrementalParsingMeasurement.*;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.metaborg.parsetable.IParseTable;
import org.metaborg.parsetable.ParseTableReadException;
import org.metaborg.parsetable.ParseTableReader;
import org.spoofax.jsglr2.JSGLR2Request;
import org.spoofax.jsglr2.incremental.IIncrementalParseState;
import org.spoofax.jsglr2.incremental.parseforest.IncrementalDerivation;
import org.spoofax.jsglr2.incremental.parseforest.IncrementalParseForest;
import org.spoofax.jsglr2.incremental.parseforest.IncrementalParseNode;
import org.spoofax.jsglr2.incremental.parseforest.IncrementalSkippedNode;
import org.spoofax.jsglr2.inputstack.incremental.IIncrementalInputStack;
import org.spoofax.jsglr2.measure.CSV;
import org.spoofax.jsglr2.measure.Config;
import org.spoofax.jsglr2.measure.Measurements;
import org.spoofax.jsglr2.measure.parsing.ParsingMeasurements;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parseforest.IParseNode;
import org.spoofax.jsglr2.parser.AbstractParseState;
import org.spoofax.jsglr2.parser.IObservableParser;
import org.spoofax.jsglr2.parser.ParserVariant;
import org.spoofax.jsglr2.parser.observing.IParserObserver;
import org.spoofax.jsglr2.parser.result.ParseSuccess;
import org.spoofax.jsglr2.stack.IStackNode;
import org.spoofax.jsglr2.testset.TestSetWithParseTable;
import org.spoofax.jsglr2.testset.testinput.IncrementalStringInput;

public class IncrementalParsingMeasurements extends Measurements<String[], IncrementalStringInput> {

    public IncrementalParsingMeasurements(TestSetWithParseTable<String[], IncrementalStringInput> testSet) {
        super(testSet);
    }

    @Override public void measure(Config<String[], IncrementalStringInput> config)
        throws ParseTableReadException, IOException {
        CSV<IncrementalParsingMeasurement> output = new CSV<>(IncrementalParsingMeasurement.values());

        IParseTable parseTable = new ParseTableReader().read(testSetReader.getParseTableTerm());

        output.addRows(
            measure(ParsingMeasurements.variantIncremental, parseTable, new IncrementalParserMeasureObserver<>()));

        output.write(config.prefix(testSet) + "parsing-incremental.csv");
    }

    private
//@formatter:off
   <StackNode  extends IStackNode,
    ParseState extends AbstractParseState<IIncrementalInputStack, StackNode> & IIncrementalParseState>
//@formatter:on
    List<Map<IncrementalParsingMeasurement, String>> measure(ParserVariant variant, IParseTable parseTable,
        IncrementalParserMeasureObserver<StackNode, ParseState> measureObserver) throws IOException {
        return testSetReader.getInputBatches().flatMap(inputBatch -> {
            @SuppressWarnings("unchecked") IObservableParser<IncrementalParseForest, IncrementalDerivation, IncrementalParseNode, StackNode, ParseState> parser =
                (IObservableParser<IncrementalParseForest, IncrementalDerivation, IncrementalParseNode, StackNode, ParseState>) variant
                    .getParser(parseTable);

            parser.observing().attachObserver(measureObserver);

            int versionCount = inputBatch.inputs.iterator().next().content.length;

            List<Map<IncrementalParsingMeasurement, Long>> output = new ArrayList<>();
            for(int i = 0; i < versionCount; i++) {
                output.add(new HashMap<>());
            }

            for(IncrementalStringInput input : inputBatch.inputs) {
                String previousInput = null;
                IncrementalParseForest previousResult = null;
                for(int i = 0; i < input.content.length; i++) {
                    String content = input.content[i];
                    if(content == null)
                        continue;
                    IncrementalParseForest result;
                    try {
                        result = ((ParseSuccess<IncrementalParseForest>) parser.parse(
                            new JSGLR2Request(content, input.fileName, null), previousInput,
                            previousResult)).parseResult;
                    } catch(Exception e) {
                        throw new IllegalStateException("Parsing failed with variant " + variant.name() + ": "
                            + e.getClass().getSimpleName() + ": " + e.getMessage());
                    }
                    output.set(i, addToOutput(output.get(i), previousResult, result, measureObserver));
                    previousInput = content;
                    previousResult = result;
                }
            }

            return IntStream.range(0, versionCount).mapToObj(i -> {
                Map<IncrementalParsingMeasurement, String> stringMap = output.get(i).entrySet().stream()
                    .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().toString()));
                stringMap.put(IncrementalParsingMeasurement.version, Integer.toString(i));
                return stringMap;
            });
        }).collect(Collectors.toList());
    }

    private
//@formatter:off
   <StackNode  extends IStackNode,
    ParseState extends AbstractParseState<IIncrementalInputStack, StackNode> & IIncrementalParseState>
//@formatter:on
    Map<IncrementalParsingMeasurement, Long> addToOutput(Map<IncrementalParsingMeasurement, Long> output,
        IncrementalParseForest previousResult, IncrementalParseForest result,
        IncrementalParserMeasureObserver<StackNode, ParseState> measureObserver) {

        calculateReuse(previousResult, result).forEach((k, v) -> output.merge(k, v, Long::sum));

        Arrays.stream(IncrementalParsingMeasurement.values())
            .collect(Collectors.toMap(Function.identity(), measurement -> {
                switch(measurement) {
                    case createCharacterNode:
                        return measureObserver.createChar;
                    case createParseNode:
                        return measureObserver.createNode;
                    case shiftCharacterNode:
                        return measureObserver.shiftChar;
                    case shiftParseNode:
                        return measureObserver.shiftNode;
                    case breakDowns:
                        return measureObserver.breakdown.values().stream().mapToLong(l -> l).sum();
                    case breakDownNoActions:
                        return measureObserver.breakdown.getOrDefault(IParserObserver.BreakdownReason.NO_ACTIONS, 0L);
                    case breakDownNonDeterministic:
                        return measureObserver.breakdown.getOrDefault(IParserObserver.BreakdownReason.NON_DETERMINISTIC,
                            0L);
                    case breakDownWrongState:
                        return measureObserver.breakdown.getOrDefault(IParserObserver.BreakdownReason.WRONG_STATE, 0L);
                    default:
                        return -1L;
                }
            })).forEach((k, v) -> {
                if(v >= 0)
                    output.merge(k, v, Long::sum);
            });
        return output;
    }

    private static Map<IncrementalParsingMeasurement, Long> calculateReuse(IParseForest parse1, IParseForest parse2) {
        long nodes = 0, leaves = 0, ambs = 0, nondets = 0, reusedNodes = 0, reusedLeaves = 0, rebuilt = 0;
        Set<IParseForest> nodeSet = new HashSet<>(), leafSet = new HashSet<>();

        if(parse1 != null) {
            Stack<IParseForest> todo1 = new Stack<>();
            todo1.add(parse1);
            while(!todo1.isEmpty()) {
                IParseForest t1 = todo1.pop();
                if(t1 instanceof IParseNode) {
                    nodeSet.add(t1);
                    if(!(t1 instanceof IncrementalSkippedNode)) {
                        IParseForest[] sub1 = ((IParseNode<?, ?>) t1).getFirstDerivation().parseForests();
                        for(int i = sub1.length - 1; i >= 0; i--) {
                            todo1.add(sub1[i]);
                        }
                    }
                } else {
                    leafSet.add(t1);
                }
            }
        }

        Map<IParseForest, IParseNode<?, ?>> parents = new HashMap<>();
        Stack<IParseForest> todo2 = new Stack<>();
        todo2.add(parse2);
        while(!todo2.isEmpty()) {
            IParseForest t2 = todo2.pop();
            if(t2 instanceof IParseNode) {
                nodes++;
                if(nodeSet.contains(t2))
                    reusedNodes++;
                IParseNode<?, ?> t2Node = (IParseNode<?, ?>) t2;
                if(t2Node.isAmbiguous())
                    ambs++;
                if(!((IncrementalParseForest) t2Node).isReusable())
                    nondets++;
                if(!(t2 instanceof IncrementalSkippedNode)) {
                    IParseForest[] sub2 = t2Node.getFirstDerivation().parseForests();
                    for(int i = sub2.length - 1; i >= 0; i--) {
                        parents.put(sub2[i], t2Node);
                        todo2.add(sub2[i]);
                    }
                    IParseNode<?, ?> parent = t2Node;
                    while(!nodeSet.contains(parent) && parent.getFirstDerivation().parseForests().length > 0
                        && Arrays.stream(parent.getFirstDerivation().parseForests()).allMatch(nodeSet::contains)) {
                        rebuilt++;
                        nodeSet.add(parent);
                        parent = parents.get(parent);
                    }
                }
            } else {
                leaves++;
                if(leafSet.contains(t2))
                    reusedLeaves++;
            }
        }

        Map<IncrementalParsingMeasurement, Long> output = new HashMap<>();
        output.put(parseNodesAmbiguous, ambs);
        output.put(parseNodesNonDeterministic, nondets);
        output.put(parseNodes, nodes);
        output.put(parseNodesReused, reusedNodes);
        output.put(parseNodesRebuilt, rebuilt);
        output.put(characterNodes, leaves);
        output.put(characterNodesReused, reusedLeaves);
        return output;
    }

}
