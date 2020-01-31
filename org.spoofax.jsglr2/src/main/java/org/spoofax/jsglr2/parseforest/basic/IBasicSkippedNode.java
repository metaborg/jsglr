package org.spoofax.jsglr2.parseforest.basic;

import java.util.Collections;
import java.util.List;

import org.spoofax.jsglr2.parseforest.IDerivation;
import org.spoofax.jsglr2.parseforest.IParseForest;

public interface IBasicSkippedNode
//@formatter:off
   <ParseForest extends IParseForest,
    Derivation  extends IDerivation<ParseForest>>
//@formatter:on
    extends IBasicParseNode<ParseForest, Derivation> {

    @Override default void addDerivation(Derivation derivation) {
    }

    @Override default List<Derivation> getDerivations() {
        return Collections.emptyList();
    }

    @Override default Derivation getFirstDerivation() {
        throw new UnsupportedOperationException("Cannot get derivation of skipped parse node");
    }

    @Override default boolean isAmbiguous() {
        return false;
    }

    @Override int width();
}
