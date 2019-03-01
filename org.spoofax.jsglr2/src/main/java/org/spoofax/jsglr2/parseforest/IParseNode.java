package org.spoofax.jsglr2.parseforest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public interface IParseNode<ParseForest extends AbstractParseForest, Derivation extends IDerivation<ParseForest>> {

    void addDerivation(Derivation derivation);

    Iterable<Derivation> getDerivations();

    default List<Derivation> getPreferredAvoidedDerivations() {
        if(!isAmbiguous())
            return Collections.singletonList(getOnlyDerivation());
        else {
            List<Derivation> preferred = null, avoided = null, other = null;

            for(Derivation derivation : getDerivations()) {
                switch(derivation.productionType()) {
                    case PREFER:
                        if(preferred == null)
                            preferred = new ArrayList<>();

                        preferred.add(derivation);
                        break;
                    case AVOID:
                        if(avoided == null)
                            avoided = new ArrayList<>();

                        avoided.add(derivation);
                        break;
                    default:
                        if(other == null)
                            other = new ArrayList<>();

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

    Derivation getOnlyDerivation();

    boolean isAmbiguous();
}
