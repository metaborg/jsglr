package org.spoofax.jsglr2.parseforest.mock;

import java.util.Arrays;
import java.util.Collections;

import org.spoofax.jsglr2.parseforest.IParseForest;

public abstract class MockParseForest implements IParseForest {

    public static MockCharacterNode c(int character) {
        return new MockCharacterNode(character);
    }

    public static MockDerivation d(MockParseForest... parseForests) {
        return new MockDerivation(parseForests);
    }

    public static MockParseNode n(String label, MockDerivation... derivations) {
        return new MockParseNode(label, IParseForest.sumWidth(derivations[0].parseForests()),
            Arrays.asList(derivations));
    }

    public static MockParseNode n(String label, MockParseForest... parseForests) {
        MockDerivation derivation = d(parseForests);

        return new MockParseNode(label, IParseForest.sumWidth(parseForests), Collections.singletonList(derivation));
    }

}
