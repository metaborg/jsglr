package org.spoofax.jsglr2.recovery;

import java.util.HashSet;
import java.util.Set;

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

            Set<ParseNode> spine = new HashSet<>();

            spine.add(parseNode);

            for(Derivation derivation : parseNode.getPreferredAvoidedDerivations()) {
                int cost = recoveryCost(derivation, parseNode.width(), spine);

                if(minRecoveryCost == -1 || cost < minRecoveryCost) {
                    minRecoveryCost = cost;
                    bestRecovery = derivation;
                }
            }

            parseNode.disambiguate(bestRecovery);
        }
    }

    private int recoveryCost(Derivation derivation, int width, Set<ParseNode> spine) {
        String constructor = derivation.production().constructor();

        if(constructor != null) {
            if(constructor.equals("INSERTION"))
                return 1;
            else if(constructor.equals("WATER"))
                return 1 + width;
        }

        int cost = 0;

        for(ParseForest child : derivation.parseForests()) {
            if(child instanceof IParseNode) {
                ParseNode parseNode = (ParseNode) child;

                if(!spine.contains(parseNode)) {
                    spine.add(parseNode);
                    cost += recoveryCost(parseNode, parseNode.width(), spine);
                    spine.remove(parseNode);
                }
            }
        }

        return cost;
    }

    private int recoveryCost(ParseNode parseNode, int width, Set<ParseNode> spine) {
        int cost = 0;

        for(Derivation derivation : parseNode.getDerivations())
            cost += recoveryCost(derivation, width, spine);

        return cost;
    }

}
