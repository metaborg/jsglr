package org.spoofax.jsglr2.measure.parsing;

import org.metaborg.parsetable.actions.IReduce;
import org.spoofax.jsglr2.elkhound.AbstractElkhoundStackNode;
import org.spoofax.jsglr2.parseforest.IDerivation;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parseforest.IParseNode;
import org.spoofax.jsglr2.parser.AbstractParseState;
import org.spoofax.jsglr2.stack.StackLink;

class ElkhoundParserMeasureObserver
//@formatter:off
   <ParseForest extends IParseForest,
    Derivation  extends IDerivation<ParseForest>,
    ParseNode   extends IParseNode<ParseForest, Derivation>>
//@formatter:on
    extends
    ParserMeasureObserver<ParseForest, Derivation, ParseNode, AbstractElkhoundStackNode<ParseForest>, AbstractParseState<?, AbstractElkhoundStackNode<ParseForest>>> {

    @Override long stackNodeLinkCount(AbstractElkhoundStackNode<ParseForest> stackNode) {
        long linksOutCount = 0;

        for(StackLink<?, ?> link : stackNode.getLinks())
            linksOutCount++;

        return linksOutCount;
    }

    @Override public void doReductions(AbstractParseState<?, AbstractElkhoundStackNode<ParseForest>> parseState,
        AbstractElkhoundStackNode<ParseForest> stack, IReduce reduce) {
        doReductions++;

        if(stack.deterministicDepth >= reduce.arity()) {
            if(parseState.activeStacks.isSingle())
                doReductionsLR++;
            else
                doReductionsDeterministicGLR++;
        } else {
            doReductionsNonDeterministicGLR++;
        }
    }

}
