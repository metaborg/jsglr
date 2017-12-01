package org.spoofax.jsglr2.states;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

import org.spoofax.jsglr2.actions.IGoto;

public class ProductionToGotoJavaHashMap implements IProductionToGoto {

    private final Map<Integer, Integer> productionToGoto;

    public ProductionToGotoJavaHashMap(IGoto[] gotos) {
        productionToGoto = new HashMap<>();

        for(IGoto gotoAction : gotos) {
            int gotoStateId = gotoAction.gotoStateId();

            IntStream.of(gotoAction.productionIds()).forEach(productionId -> {
                productionToGoto.put(productionId, gotoStateId);
            });
        }
    }

    @Override
    public boolean contains(int productionId) {
        return productionToGoto.containsKey(productionId);
    }

    @Override
    public int get(int productionId) {
        return productionToGoto.get(productionId);
    }

}
