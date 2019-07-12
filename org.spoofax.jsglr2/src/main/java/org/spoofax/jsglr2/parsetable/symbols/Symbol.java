package org.spoofax.jsglr2.parsetable.symbols;

abstract class Symbol implements ISymbol {

    SortCardinality cardinality;

    Symbol(SortCardinality cardinality) {
        this.cardinality = cardinality;
    }

    @Override public SortCardinality cardinality() {
        return cardinality;
    }

}
