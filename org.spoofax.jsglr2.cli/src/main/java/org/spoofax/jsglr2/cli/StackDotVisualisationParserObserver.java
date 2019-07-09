package org.spoofax.jsglr2.cli;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.metaborg.parsetable.IProduction;
import org.spoofax.jsglr2.parseforest.IDerivation;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parser.AbstractParse;
import org.spoofax.jsglr2.stack.IStackNode;
import org.spoofax.jsglr2.stack.StackLink;

class StackDotVisualisationParserObserver
//@formatter:off
   <ParseForest extends IParseForest,
    StackNode   extends IStackNode>
//@formatter:on
    extends DotVisualisationParserObserver<ParseForest, StackNode> {

    private Map<StackNode, Integer> stackNodeRank;
    private int maxStackNodeRank;
    private int[] offsetMaxStackNodeRank;

    public StackDotVisualisationParserObserver(Consumer<String> outputConsumer) {
        super(outputConsumer);
    }

    @Override public void parseStart(AbstractParse<ParseForest, StackNode> parse) {
        super.parseStart(parse);
        stackNodeRank = new HashMap<>();
        maxStackNodeRank = 0;
        offsetMaxStackNodeRank = new int[parse.inputLength + 1];
    }

    private void rankStackNode(StackNode stackNode, int rank) {
        int rankAtOffset;

        if(stackNode.position().offset > 0)
            rankAtOffset = Math.max(rank, offsetMaxStackNodeRank[stackNode.position().offset - 1]);
        else
            rankAtOffset = rank;

        stackNodeRank.put(stackNode, rankAtOffset);
        maxStackNodeRank = Math.max(maxStackNodeRank, rankAtOffset);
        offsetMaxStackNodeRank[stackNode.position().offset] = maxStackNodeRank;
    }

    @Override public void createStackNode(StackNode stack) {
        super.createStackNode(stack);

        if(id(stack) == 0)
            rankStackNode(stack, 0);

        int innerGrid = 3;
        int cellDimension = 10;
        append(stackNodeId(stack)
            + " [label=<<TABLE CELLSPACING=\"0\" CELLPADDING=\"0\" BORDER=\"0\" CELLBORDER=\"0\" FIXEDSIZE=\"TRUE\" WIDTH=\""
            + ((1 + innerGrid) * cellDimension) + "\" HEIGHT=\"" + ((1 + innerGrid) * cellDimension)
            + "\"><TR><TD WIDTH=\"" + cellDimension + "\" HEIGHT=\"" + cellDimension + "\"><FONT POINT-SIZE=\"10\">"
            + id(stack) + "</FONT></TD><TD COLSPAN=\"" + innerGrid + "\" WIDTH=\"" + (innerGrid * cellDimension)
            + "\" HEIGHT=\"" + cellDimension + "\"></TD></TR><TR ROWSPAN=\"" + innerGrid + "\"><TD WIDTH=\""
            + cellDimension + "\" HEIGHT=\"" + (innerGrid * cellDimension) + "\"></TD><TD COLSPAN=\"" + innerGrid
            + "\" WIDTH=\"" + (innerGrid * cellDimension) + "\" HEIGHT=\"" + (innerGrid * cellDimension)
            + "\" BORDER=\"1\" PORT=\"stack\">" + stack.state().id() + "</TD></TR></TABLE>>];");
    }

    @Override public void createStackLink(StackLink<ParseForest, StackNode> link) {
        super.createStackLink(link);

        if(!stackNodeRank.containsKey(link.from))
            rankStackNode(link.from, stackNodeRank.get(link.to) + 1);

        append(stackNodeId(link.to) + ":stack:e -> " + stackNodeId(link.from) + ":stack:w [label=\""
            + id(link.parseForest) + ": " + escape(link.parseForest.descriptor()) + "\"];");
    }

    @Override public void rejectStackLink(StackLink<ParseForest, StackNode> link) {
    }

    @Override public void createParseNode(ParseForest parseNode, IProduction production) {
        super.createParseNode(parseNode, production);
    }

    @Override public void createDerivation(IDerivation<ParseForest> derivation, IProduction production,
        ParseForest[] parseNodes) {
        super.createDerivation(derivation, production, parseNodes);
    }

    @Override public void createCharacterNode(ParseForest characterNode, int character) {
        super.createCharacterNode(characterNode, character);
    }

    @Override public void addDerivation(ParseForest parseNode) {
    }

    void output() {
        String prefix = "digraph {\nrankdir = LR;\nedge [dir=\"back\"];\nnode [shape=plain];\n";

        for(int rank = 0; rank <= maxStackNodeRank; rank++) {
            Collection<StackNode> stackNodesForRank = new ArrayList<>();

            for(Map.Entry<StackNode, Integer> entry : stackNodeRank.entrySet()) {
                if(entry.getValue() == rank)
                    stackNodesForRank.add(entry.getKey());
            }

            append("{rank=same; "
                + stackNodesForRank.stream().map(id -> stackNodeId(id) + ";").collect(Collectors.joining()) + "}");
        }

        outputConsumer.accept(prefix + sb.toString() + "}");
    }

}
