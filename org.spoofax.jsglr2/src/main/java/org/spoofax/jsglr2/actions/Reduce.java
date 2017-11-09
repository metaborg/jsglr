package org.spoofax.jsglr2.actions;

import org.spoofax.jsglr2.characters.ICharacters;
import org.spoofax.jsglr2.parsetable.IProduction;
import org.spoofax.jsglr2.parsetable.ProductionType;

public class Reduce extends Action implements IReduce {

    private final IProduction production;
    private final ProductionType productionType;
    private final int arity;

    public Reduce(ICharacters characters, IProduction production, ProductionType productionType, int arity) {
        super(characters);

        this.production = production;
        this.productionType = productionType;
        this.arity = arity;

        /*
         * NOTE: according to Eduardo, the type of the action and of the production should match for types REJECT /
         * PREFER / AVOID at least. There seems to be a bug in the parse table generator.
         */
        assert !(productionType == ProductionType.REJECT || productionType == ProductionType.PREFER
            || productionType == ProductionType.AVOID) || productionType == production.productionType();

        assert !(production.productionType() == ProductionType.REJECT
            || production.productionType() == ProductionType.PREFER
            || production.productionType() == ProductionType.AVOID) || production.productionType() == productionType;
    }

    public IProduction production() {
        return production;
    }

    public ProductionType productionType() {
        return productionType;
    }

    public int arity() {
        return arity;
    }

}
