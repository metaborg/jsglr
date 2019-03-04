package org.spoofax.jsglr2.parseforest.basic;

import java.util.List;

import org.spoofax.jsglr2.parseforest.AbstractParseForest;
import org.spoofax.jsglr2.parseforest.IDerivation;
import org.spoofax.jsglr2.parseforest.IParseNode;

public interface IBasicParseNode
//@formatter:off
   <ParseForest extends AbstractParseForest,
    Derivation  extends IDerivation<ParseForest>>
//@formatter:on
    extends IParseNode<ParseForest, Derivation> {

    @Override default void addDerivation(Derivation derivation) {
        this.getDerivations().add(derivation);
    }

    @Override List<Derivation> getDerivations();

    @Override default Derivation getOnlyDerivation() {
        return getDerivations().get(0);
    }

    @Override default boolean isAmbiguous() {
        return getDerivations().size() > 1;
    }

}
