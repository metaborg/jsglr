package org.spoofax.jsglr2.parsetable;

public interface IProductionToGoto {

    // Whether a goto for the given production id is present
    boolean contains(int production);

    // Get the goto state id for the given production id
    int get(int production);

}
