package org.spoofax.jsglr2.states;

import java.util.stream.IntStream;

import org.spoofax.jsglr2.actions.IGoto;

import io.usethesource.capsule.BinaryRelation;

public class ProductionToGotoCapsuleBinaryRelationImmutable implements IProductionToGoto {

    private final BinaryRelation.Immutable<Integer, Integer> productionToGoto;

    public ProductionToGotoCapsuleBinaryRelationImmutable(IGoto[] gotos) {
        final BinaryRelation.Transient<Integer, Integer> tmpProductionToGoto = BinaryRelation.Transient.of();

        for(IGoto gotoAction : gotos) {
            int gotoStateId = gotoAction.gotoStateId();

            IntStream.of(gotoAction.productionIds()).forEach(productionId -> {
                tmpProductionToGoto.__put(productionId, gotoStateId);
            });
        }

        productionToGoto = tmpProductionToGoto.freeze();
    }

    @Override public boolean contains(int productionId) {
        return productionToGoto.containsKey(productionId);
    }

    @Override public int get(int productionId) {
        return productionToGoto.get(productionId).findFirst().get();
    }

}
