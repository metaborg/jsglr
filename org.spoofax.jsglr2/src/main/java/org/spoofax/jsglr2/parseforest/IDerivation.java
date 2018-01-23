package org.spoofax.jsglr2.parseforest;

import org.metaborg.parsetable.IProduction;

public interface IDerivation<ParseForest> {

    IProduction production();

    ParseForest[] parseForests();

}
