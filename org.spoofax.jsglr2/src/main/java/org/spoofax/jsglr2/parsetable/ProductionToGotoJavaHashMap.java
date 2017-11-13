package org.spoofax.jsglr2.parsetable;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

import org.spoofax.jsglr2.actions.IGoto;

public class ProductionToGotoJavaHashMap implements IProductionToGoto {

    private final Map<Integer, Integer> productionToGoto;

    public ProductionToGotoJavaHashMap(IGoto[] gotos) {
        productionToGoto = new HashMap<>();

        for(IGoto gotoAction : gotos) {
            int gotoState = gotoAction.gotoState();

            IntStream.of(gotoAction.productions()).forEach(productionId -> {
                productionToGoto.put(productionId, gotoState);
            });
        }
    }

    @Override public boolean contains(int production) {
        return productionToGoto.containsKey(production);
    }

    @Override public int get(int production) {
        return productionToGoto.get(production);
    }

}
