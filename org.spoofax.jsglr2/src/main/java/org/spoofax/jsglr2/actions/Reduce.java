package org.spoofax.jsglr2.actions;

import org.spoofax.jsglr2.parsetable.IProduction;
import org.spoofax.jsglr2.parsetable.ProductionType;

public class Reduce implements IReduce {

    protected final IProduction production;
    private final ProductionType productionType;
    private final int arity;

    public Reduce(IProduction production, ProductionType productionType, int arity) {
        this.production = production;
        this.productionType = productionType;
        this.arity = arity;

        /*
         * NOTE: according to Eduardo, the type of the action and of the production should match for types REJECT /
         * PREFER / AVOID at least. There seems to be a bug in the parse table generator. If this bug is fixed the
         * asserts below should succeed (they are failing now).
         *
         * assert !(productionType == ProductionType.REJECT || productionType == ProductionType.PREFER || productionType
         * == ProductionType.AVOID) || productionType == production.productionType();
         * 
         * assert !(production.productionType() == ProductionType.REJECT || production.productionType() ==
         * ProductionType.PREFER || production.productionType() == ProductionType.AVOID) || production.productionType()
         * == productionType;
         */
    }

    @Override public IProduction production() {
        return production;
    }

    @Override public ProductionType productionType() {
        return productionType;
    }

    @Override public int arity() {
        return arity;
    }

    @Override public String toString() {
        return "REDUCE(" + production.id() + ")";
    }

    @Override public int hashCode() {
        return production.hashCode() ^ productionType.hashCode() ^ arity;
    }

    @Override public boolean equals(Object o) {
        if(this == o) {
            return true;
        }
        if(o == null || getClass() != o.getClass()) {
            return false;
        }

        Reduce that = (Reduce) o;

        return production.equals(that.production) && productionType.equals(that.productionType) && arity == that.arity;
    }

}
