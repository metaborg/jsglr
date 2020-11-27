package org.spoofax.jsglr2.recovery;

import org.spoofax.jsglr2.parseforest.Disambiguator;
import org.spoofax.jsglr2.parseforest.IDerivation;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parseforest.IParseNode;
import org.spoofax.jsglr2.parser.AbstractParseState;
import org.spoofax.jsglr2.stack.IStackNode;

public class RecoveryDisambiguator
//@formatter:off
   <ParseForest extends IParseForest,
    Derivation  extends IDerivation<ParseForest>,
    ParseNode   extends IParseNode<ParseForest, Derivation>,
    StackNode   extends IStackNode,
    ParseState  extends AbstractParseState<?, StackNode> & IRecoveryParseState<?, StackNode, ?>>
//@formatter:on
    implements Disambiguator<ParseForest, Derivation, ParseNode, StackNode, ParseState> {

    @Override public void disambiguate(ParseState parseState, ParseNode parseNode) {
        if(parseNode.isAmbiguous() && parseState.isRecovering()) {
            int minRecoveryCost = -1;
            Derivation bestRecovery = null;

            for(Derivation derivation : parseNode.getPreferredAvoidedDerivations()) {
                int cost = recoveryCost(derivation);

                if(minRecoveryCost == -1 || cost < minRecoveryCost) {
                    minRecoveryCost = cost;
                    bestRecovery = derivation;
                }
            }

            parseNode.disambiguate(bestRecovery);
        }
    }

    private int recoveryCost(Derivation derivation) {
        int cost = 0;

        if(derivation.production().isRecovery()) {
            String constructor = derivation.production().constructor();

            if(constructor != null && constructor.equals("INSERTION"))
                cost += 1; // Insertion
            else if(constructor == null)
                cost += 2; // Water
            else
                throw new IllegalStateException("invalid recovery");
        }

        for(ParseForest child : derivation.parseForests()) {
            if(child instanceof IParseNode)
                cost += recoveryCost((ParseNode) child);
        }

        return cost;
    }

    private int recoveryCost(ParseNode parseNode) {
        int cost = 0;

        for(Derivation derivation : parseNode.getDerivations())
            cost += recoveryCost(derivation);

        return cost;
    }

}
