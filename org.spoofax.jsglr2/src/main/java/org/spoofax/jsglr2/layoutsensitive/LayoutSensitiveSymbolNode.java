package org.spoofax.jsglr2.layoutsensitive;

import java.util.ArrayList;
import java.util.List;

import org.metaborg.parsetable.IProduction;
import org.spoofax.jsglr2.parseforest.basic.BasicParseForest;
import org.spoofax.jsglr2.parser.Parse;
import org.spoofax.jsglr2.parser.Position;

public class LayoutSensitiveSymbolNode extends BasicParseForest {

    public final IProduction production;
    private final List<LayoutSensitiveRuleNode> derivations;

    public LayoutSensitiveSymbolNode(int nodeNumber, Parse<?, ?> parse, Position startPosition, Position endPosition,
        IProduction production) {
        super(nodeNumber, parse, startPosition, endPosition);
        this.production = production;
        this.derivations = new ArrayList<LayoutSensitiveRuleNode>();
    }

    public void addDerivation(LayoutSensitiveRuleNode derivation) {
        this.derivations.add(derivation);
    }

    public IProduction getProduction() {
        return production;
    }

    public List<LayoutSensitiveRuleNode> getDerivations() {
        return derivations;
    }

    public List<LayoutSensitiveRuleNode> getPreferredAvoidedDerivations() {
        if(derivations.size() <= 1)
            return derivations;
        else {
            List<LayoutSensitiveRuleNode> preferred = null, avoided = null, other = null;

            for(LayoutSensitiveRuleNode derivation : derivations) {
                switch(derivation.productionType) {
                    case PREFER:
                        if(preferred == null)
                            preferred = new ArrayList<LayoutSensitiveRuleNode>();

                        preferred.add(derivation);
                        break;
                    case AVOID:
                        if(avoided == null)
                            avoided = new ArrayList<LayoutSensitiveRuleNode>();

                        avoided.add(derivation);
                        break;
                    default:
                        if(other == null)
                            other = new ArrayList<LayoutSensitiveRuleNode>();

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

    public LayoutSensitiveRuleNode getOnlyDerivation() {
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
