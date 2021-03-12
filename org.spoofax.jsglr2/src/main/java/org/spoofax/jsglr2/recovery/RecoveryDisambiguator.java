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
            RecoverCost minRecoveryCost = null;
            Derivation bestRecovery = null;

            Set<ParseNode> spine = new HashSet<>();

            spine.add(parseNode);

            for(Derivation derivation : parseNode.getPreferredAvoidedDerivations()) {
                RecoverCost cost = recoveryCost(0, derivation, parseNode.width(), spine);

                if(bestRecovery == null || RecoverCost.lowerThan(cost, minRecoveryCost)) {
                    minRecoveryCost = cost;
                    bestRecovery = derivation;
                }
            }

            parseNode.disambiguate(bestRecovery);
        }
    }

    private RecoverCost recoveryCost(int offset, Derivation derivation, int width, Set<ParseNode> spine) {
        String constructor = derivation.production().constructor();

        if(constructor != null) {
            if(constructor.equals("INSERTION"))
                return new RecoverCost(1, offset, false);
            else if(constructor.equals("WATER"))
                return new RecoverCost(1 + width, offset, false);
        }

        RecoverCost cost = null;

        for(ParseForest child : derivation.parseForests()) {
            if(child instanceof IParseNode) {
                ParseNode parseNode = (ParseNode) child;

                if(!spine.contains(parseNode)) {
                    spine.add(parseNode);
                    cost = RecoverCost.merge(cost, recoveryCost(offset, parseNode, parseNode.width(), spine));
                    spine.remove(parseNode);
                } else
                    cost = RecoverCost.cycle();
            }

            offset += child.width();
        }

        return cost;
    }

    private RecoverCost recoveryCost(int offset, ParseNode parseNode, int width, Set<ParseNode> spine) {
        RecoverCost cost = null;

        for(Derivation derivation : parseNode.getDerivations()) {
            RecoverCost derivationCost = recoveryCost(offset, derivation, width, spine);

            cost = RecoverCost.merge(cost, derivationCost);
        }

        return cost;
    }

    static class RecoverCost {
        int cost, firstRecoveryOffset;
        boolean containsCycle;

        RecoverCost(int cost, int firstRecoveryOffset, boolean containsCycle) {
            this.cost = cost;
            this.firstRecoveryOffset = firstRecoveryOffset;
            this.containsCycle = containsCycle;
        }

        RecoverCost(RecoverCost first, RecoverCost second) {
            this(first.cost + second.cost, Math.min(first.firstRecoveryOffset, second.firstRecoveryOffset),
                first.containsCycle || second.containsCycle);
        }

        static RecoverCost cycle() {
            return new RecoverCost(0, -1, true);
        }

        static RecoverCost merge(RecoverCost first, RecoverCost second) {
            if(first == null)
                return second;
            else if(second == null)
                return first;
            else
                return new RecoverCost(first, second);
        }

        static boolean lowerThan(RecoverCost first, RecoverCost second) {
            if(second == null)
                return false;
            else if(first == null)
                return true;
            else if(first.containsCycle != second.containsCycle)
                // Avoid recoveries that contain cycles
                return second.containsCycle;
            else
                // Prefer later recoveries over earlier recoveries
                return first.cost < second.cost
                    || (first.cost == second.cost && first.firstRecoveryOffset > second.firstRecoveryOffset);
        }
    }

}
