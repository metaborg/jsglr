package org.spoofax.jsglr2.parseforest;

import org.spoofax.jsglr2.parsetable.IProduction;

public interface IDerivation<ParseForest> {

    IProduction production();

    ParseForest[] parseForests();

}
