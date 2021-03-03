package org.spoofax.jsglr2.measure.incremental;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

import org.metaborg.parsetable.productions.IProduction;
import org.spoofax.jsglr2.incremental.IIncrementalParseState;
import org.spoofax.jsglr2.incremental.parseforest.IncrementalDerivation;
import org.spoofax.jsglr2.incremental.parseforest.IncrementalParseForest;
import org.spoofax.jsglr2.incremental.parseforest.IncrementalParseNode;
import org.spoofax.jsglr2.inputstack.incremental.IIncrementalInputStack;
import org.spoofax.jsglr2.parser.AbstractParseState;
import org.spoofax.jsglr2.parser.ForShifterElement;
import org.spoofax.jsglr2.parser.observing.IParserObserver;
import org.spoofax.jsglr2.stack.IStackNode;

public class IncrementalParserMeasureObserver
//@formatter:off
   <StackNode     extends IStackNode,
    ParseState    extends AbstractParseState<IIncrementalInputStack, StackNode> & IIncrementalParseState>
//@formatter:on
    implements
    IParserObserver<IncrementalParseForest, IncrementalDerivation, IncrementalParseNode, StackNode, ParseState> {

    long createChar, createNode, shiftChar, shiftNode;
    Map<IParserObserver.BreakdownReason, Long> breakdown = new HashMap<>();

    @Override public void parseStart(ParseState parse) {
        breakdown.clear();
        createChar = 0;
        createNode = 0;
        shiftChar = 0;
        shiftNode = 0;
    }

    @Override public void createParseNode(IncrementalParseNode parseNode, IProduction production) {
        createNode++;
    }

    @Override public void createCharacterNode(IncrementalParseForest characterNode, int character) {
        createChar++;
    }

    @Override public void shifter(IncrementalParseForest termNode, Queue<ForShifterElement<StackNode>> forShifter) {
        if(termNode instanceof IncrementalParseNode)
            shiftNode++;
        else
            shiftChar++;
    }

    @Override public void breakDown(IIncrementalInputStack inputStack, IParserObserver.BreakdownReason reason) {
        breakdown.merge(reason, 1L, Long::sum);
    }
}
