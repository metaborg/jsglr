package org.spoofax.jsglr2.parseforest.basic;

import java.util.List;

import org.spoofax.jsglr2.parseforest.IDerivation;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parseforest.IParseNode;

public interface IBasicParseNode
//@formatter:off
   <ParseForest extends IParseForest,
    Derivation  extends IDerivation<ParseForest>>
//@formatter:on
    extends IParseNode<ParseForest, Derivation>, IBasicParseForest {

    @Override default void addDerivation(Derivation derivation) {
        this.getDerivations().add(derivation);
    }

    @Override List<Derivation> getDerivations();

    @Override default boolean hasDerivations() {
        return !getDerivations().isEmpty();
    }

    @Override default Derivation getFirstDerivation() {
        if(getDerivations().size() < 1)
            throw new UnsupportedOperationException("Cannot get derivation of skipped parse node");

        return getDerivations().get(0);
    }

    @Override default boolean isAmbiguous() {
        return getDerivations().size() > 1;
    }

    @Override default void disambiguate(Derivation derivation) {
        getDerivations().removeIf(alternative -> alternative != derivation);
    }

}
