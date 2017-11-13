package org.spoofax.jsglr2.parsetable;

import java.util.stream.IntStream;

import org.spoofax.jsglr2.actions.IGoto;

import io.usethesource.capsule.BinaryRelation;

public class ProductionToGoto implements IProductionToGoto {

    final BinaryRelation.Immutable<Integer, Integer> productionToGoto;

    public ProductionToGoto(IGoto[] gotos) {
        final BinaryRelation.Transient<Integer, Integer> tmpProductionToGoto = BinaryRelation.Transient.of();

        for(IGoto gotoAction : gotos) {
            int gotoState = gotoAction.gotoState();

            IntStream.of(gotoAction.productions()).forEach(productionId -> {
                tmpProductionToGoto.__put(productionId, gotoState);
            });
        }

        productionToGoto = tmpProductionToGoto.freeze();
    }

    @Override public boolean contains(int production) {
        return productionToGoto.containsKey(production);
    }

    @Override public int get(int production) {
        return productionToGoto.get(production).findFirst().get();
    }

}
