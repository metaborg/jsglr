package org.spoofax.jsglr2.cli.output.dot;

import java.util.*;
import java.util.stream.Collectors;

import org.spoofax.jsglr2.parseforest.ICharacterNode;
import org.spoofax.jsglr2.parseforest.IDerivation;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parseforest.IParseNode;
import org.spoofax.jsglr2.parser.AbstractParseState;
import org.spoofax.jsglr2.stack.IStackNode;
import org.spoofax.jsglr2.stack.StackLink;

class StackDotVisualisationParserObserver
//@formatter:off
   <ParseForest extends IParseForest,
    Derivation  extends IDerivation<ParseForest>,
    ParseNode   extends IParseNode<ParseForest, Derivation>,
    StackNode   extends IStackNode,
    ParseState  extends AbstractParseState<?, StackNode>>
//@formatter:on
    extends DotVisualisationParserObserver<ParseForest, Derivation, ParseNode, StackNode, ParseState> {

    private Map<StackNode, Integer> stackNodeRank;
    private int maxStackNodeRank;
    private int currentOffset;
    private int[] offsetMaxStackNodeRank;
    private Map<StackNode, Integer> stackNodeOffset;
    private List<StackNode> stackNodes;
    private List<StackLink<ParseForest, StackNode>> stackLinks;
    private StackNode acceptingStack;

    @Override public void parseStart(ParseState parseState) {
        super.parseStart(parseState);
        stackNodeRank = new HashMap<>();
        currentOffset = 0;
        maxStackNodeRank = 0;
        offsetMaxStackNodeRank = new int[parseState.inputStack.inputString().length() + 1];
        stackNodeOffset = new HashMap<>();
        stackNodes = new ArrayList<>();
        stackLinks = new ArrayList<>();
        acceptingStack = null;
    }

    @Override public void parseRound(ParseState parseState, Iterable<StackNode> activeStacks) {
        currentOffset = parseState.inputStack.offset();
    }

    private void rankStackNode(StackNode stackNode, int rank) {
        int rankAtOffset;

        if(stackNodeOffset.get(stackNode) > 0)
            rankAtOffset = Math.max(rank, offsetMaxStackNodeRank[stackNodeOffset.get(stackNode) - 1]);
        else
            rankAtOffset = rank;

        stackNodeRank.put(stackNode, rankAtOffset);
        maxStackNodeRank = Math.max(maxStackNodeRank, rankAtOffset);
        offsetMaxStackNodeRank[stackNodeOffset.get(stackNode)] = maxStackNodeRank;
    }

    @Override public void createStackNode(StackNode stack) {
        super.createStackNode(stack);

        stackNodeOffset.put(stack, currentOffset);

        if(id(stack) == 0)
            rankStackNode(stack, 0);

        stackNodes.add(stack);
    }

    @Override public void createStackLink(StackLink<ParseForest, StackNode> link) {
        super.createStackLink(link);

        if(!stackNodeRank.containsKey(link.from))
            rankStackNode(link.from, stackNodeRank.get(link.to) + 1);

        stackLinks.add(link);
    }

    @Override public void accept(StackNode acceptingStack) {
        this.acceptingStack = acceptingStack;
    }

    private String stackNodeId(StackNode stack) {
        return stackNodeId(id(stack));
    }

    private String stackNodeId(int id) {
        return "stack_" + id;
    }

    public String output() {
        String prefix = "digraph {\nrankdir = LR;\nedge [dir=\"back\"];\n";

        for(StackNode stack : stackNodes)
            dotStatement(idNode(stackNodeId(stack), id(stack), "" + stack.state().id(),
                acceptingStack == stack ? "green" : "gray") + ";");

        for(StackLink<ParseForest, StackNode> link : stackLinks) {
            String derivations = "";

            if(link.parseForest instanceof ICharacterNode)
                derivations = link.parseForest.descriptor();
            else {
                ParseNode parseNode = (ParseNode) link.parseForest;

                for(Derivation derivation : parseNode.getDerivations()) {
                    if(!"".equals(derivations))
                        derivations += ",";

                    derivations += derivation.production().lhs().descriptor()
                        + (derivation.production().constructor() != null ? "." + derivation.production().constructor()
                            : "")
                        + "[" + Arrays.stream(derivation.parseForests()).map(this::id).map(Objects::toString)
                            .collect(Collectors.joining(","))
                        + "]";
                }
            }

            dotStatement(stackNodeId(link.to) + ":p:e -> " + stackNodeId(link.from) + ":p:w [label=\""
                + id(link.parseForest) + ": " + escape(derivations) + "\"" + (link.isRejected() ? ",style=dotted" : "")
                + (link.from == acceptingStack ? ",color=green" : "") + "];");
        }

        for(int rank = 0; rank <= maxStackNodeRank; rank++) {
            Collection<StackNode> stackNodesForRank = new ArrayList<>();

            for(Map.Entry<StackNode, Integer> entry : stackNodeRank.entrySet()) {
                if(entry.getValue() == rank)
                    stackNodesForRank.add(entry.getKey());
            }

            dotStatement("{rank=same; "
                + stackNodesForRank.stream().map(id -> stackNodeId(id) + ";").collect(Collectors.joining()) + "}");
        }

        return prefix + dotStatements + "}";
    }

}
