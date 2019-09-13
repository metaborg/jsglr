package org.spoofax.jsglr2.cli;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.spoofax.jsglr2.parseforest.IParseForest;

import org.spoofax.jsglr2.parser.AbstractParseState;
import org.spoofax.jsglr2.stack.IStackNode;
import org.spoofax.jsglr2.stack.StackLink;

class StackDotVisualisationParserObserver
//@formatter:off
   <ParseForest extends IParseForest,
    StackNode   extends IStackNode,
    ParseState  extends AbstractParseState<ParseForest, StackNode>>
//@formatter:on
    extends DotVisualisationParserObserver<ParseForest, StackNode, ParseState> {

    private Map<StackNode, Integer> stackNodeRank;
    private int maxStackNodeRank;
    private int[] offsetMaxStackNodeRank;

    public StackDotVisualisationParserObserver(Consumer<String> outputConsumer) {
        super(outputConsumer);
    }

    @Override public void parseStart(ParseState parseState) {
        super.parseStart(parseState);
        stackNodeRank = new HashMap<>();
        maxStackNodeRank = 0;
        offsetMaxStackNodeRank = new int[parseState.inputLength + 1];
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

        dotStatement(idNode(stackNodeId(stack), id(stack), "" + stack.state().id()) + ";");
    }

    @Override public void createStackLink(StackLink<ParseForest, StackNode> link) {
        super.createStackLink(link);

        if(!stackNodeRank.containsKey(link.from))
            rankStackNode(link.from, stackNodeRank.get(link.to) + 1);

        dotStatement(stackNodeId(link.to) + ":p:e -> " + stackNodeId(link.from) + ":p:w [label=\""
            + id(link.parseForest) + ": " + escape(link.parseForest.descriptor()) + "\"];");
    }

    String stackNodeId(StackNode stack) {
        return stackNodeId(id(stack));
    }

    String stackNodeId(int id) {
        return "stack_" + id;
    }

    void output() {
        String prefix = "digraph {\nrankdir = LR;\nedge [dir=\"back\"];\n";

        for(int rank = 0; rank <= maxStackNodeRank; rank++) {
            Collection<StackNode> stackNodesForRank = new ArrayList<>();

            for(Map.Entry<StackNode, Integer> entry : stackNodeRank.entrySet()) {
                if(entry.getValue() == rank)
                    stackNodesForRank.add(entry.getKey());
            }

            dotStatement("{rank=same; "
                + stackNodesForRank.stream().map(id -> stackNodeId(id) + ";").collect(Collectors.joining()) + "}");
        }

        outputConsumer.accept(prefix + dotStatements + "}");
    }

}
