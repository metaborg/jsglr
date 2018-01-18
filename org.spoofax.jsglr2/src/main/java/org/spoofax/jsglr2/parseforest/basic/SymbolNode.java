package org.spoofax.jsglr2.parseforest.basic;

import java.util.ArrayList;
import java.util.List;

import org.metaborg.parsetable.IProduction;
import org.spoofax.jsglr2.parser.Parse;
import org.spoofax.jsglr2.parser.Position;

public class SymbolNode extends BasicParseForest {

    public final IProduction production;
    private final List<RuleNode> derivations;

    public SymbolNode(int nodeNumber, Parse<?, ?> parse, Position startPosition, Position endPosition,
        IProduction production) {
        super(nodeNumber, parse, startPosition, endPosition);
        this.production = production;
        this.derivations = new ArrayList<RuleNode>();
    }

    public void addDerivation(RuleNode derivation) {
        this.derivations.add(derivation);
    }

    public List<RuleNode> getDerivations() {
        return derivations;
    }

    public List<RuleNode> getPreferredAvoidedDerivations() {
        if(derivations.size() <= 1)
            return derivations;
        else {
            List<RuleNode> preferred = null, avoided = null, other = null;

            for(RuleNode derivation : derivations) {
                switch(derivation.productionType) {
                    case PREFER:
                        if(preferred == null)
                            preferred = new ArrayList<RuleNode>();

                        preferred.add(derivation);
                        break;
                    case AVOID:
                        if(avoided == null)
                            avoided = new ArrayList<RuleNode>();

                        avoided.add(derivation);
                        break;
                    default:
                        if(other == null)
                            other = new ArrayList<RuleNode>();

                        other.add(derivation);
                }
            }

            if(preferred != null && !preferred.isEmpty())
                return preferred;
            else if(other != null && !other.isEmpty())
                return other;
            else
                return avoided;
        }
    }

    public RuleNode getOnlyDerivation() {
        return derivations.get(0);
    }

    public boolean isAmbiguous() {
        return derivations.size() > 1;
    }

    @Override
    public String descriptor() {
        return production.descriptor();
    }

}
