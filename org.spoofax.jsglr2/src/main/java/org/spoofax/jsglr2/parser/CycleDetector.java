package org.spoofax.jsglr2.parser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.spoofax.jsglr2.messages.Message;
import org.spoofax.jsglr2.parseforest.IDerivation;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parseforest.IParseNode;
import org.spoofax.jsglr2.parseforest.ParseNodeVisitor;
import org.spoofax.jsglr2.parser.result.ParseFailureCause;

public class CycleDetector
//@formatter:off
   <ParseForest extends IParseForest,
    Derivation  extends IDerivation<ParseForest>,
    ParseNode   extends IParseNode<ParseForest, Derivation>>
//@formatter:on
    implements ParseNodeVisitor<ParseForest, Derivation, ParseNode> {

    Collection<Message> messages;
    List<ParseNode> spine = new ArrayList<>();
    ParseFailureCause failureCause = null;

    CycleDetector(Collection<Message> messages) {
        this.messages = messages;
    }

    @Override public boolean preVisit(ParseNode parseNode, Position startPosition) {
        if(spine.contains(parseNode)) {
            failureCause =
                new ParseFailureCause(ParseFailureCause.Type.Cycle, startPosition, cycleDescription(parseNode));

            messages.add(failureCause.toMessage());

            return false;
        } else {
            spine.add(parseNode);

            return parseNode.production().isContextFree();
        }
    }

    public boolean cycleDetected() {
        return failureCause != null;
    }

    private String cycleDescription(ParseNode parseNode) {
        int cycleStartIndex = spine.size() - 1;

        while(spine.get(cycleStartIndex) != parseNode)
            cycleStartIndex--;

        List<ParseNode> cycle = spine.subList(cycleStartIndex, spine.size());

        return cycle.stream().map(ParseNode::descriptor).collect(Collectors.joining(" -> "));
    }

    @Override public void postVisit(ParseNode parseNode, Position startPosition, Position endPosition) {
        spine.remove(parseNode);
    }

}
