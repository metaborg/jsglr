package org.spoofax.jsglr2.parseforest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.metaborg.parsetable.IProduction;

public interface IParseNode
//@formatter:off
   <ParseForest extends AbstractParseForest,
    Derivation  extends IDerivation<ParseForest>>
//@formatter:on
    extends IParseForest {

    IProduction production();

    void addDerivation(Derivation derivation);

    Iterable<Derivation> getDerivations();

    default List<Derivation> getPreferredAvoidedDerivations() {
        if(!isAmbiguous())
            return Collections.singletonList(getFirstDerivation());
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

    Derivation getFirstDerivation();

    boolean isAmbiguous();

    default int width() {
        return getFirstDerivation().width();
    }

    default String descriptor() {
        return production().descriptor();
    }

}
