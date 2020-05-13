package org.spoofax.jsglr2.parser;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.spoofax.jsglr2.messages.Message;
import org.spoofax.jsglr2.messages.SourceRegion;
import org.spoofax.jsglr2.parseforest.IDerivation;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parseforest.IParseNode;
import org.spoofax.jsglr2.parseforest.ParseNodeVisitor;

public class CycleDetector
//@formatter:off
   <ParseForest extends IParseForest,
    Derivation  extends IDerivation<ParseForest>,
    ParseNode   extends IParseNode<ParseForest, Derivation>>
//@formatter:on
    implements ParseNodeVisitor<ParseForest, Derivation, ParseNode> {

    boolean cycleDetected = false;
    Collection<Message> messages;
    Set<ParseNode> spine = new HashSet<>();

    CycleDetector(Collection<Message> messages) {
        this.messages = messages;
    }

    @Override public boolean preVisit(ParseNode parseNode, Position startPosition) {
        if(spine.contains(parseNode)) {
            cycleDetected = true;
            messages.add(Message.error("Cycle in parse forest", new SourceRegion(startPosition)));

            return false;
        } else {
            spine.add(parseNode);

            return true;
        }
    }

    @Override public void postVisit(ParseNode parseNode, Position startPosition, Position endPosition) {
        spine.remove(parseNode);
    }

}