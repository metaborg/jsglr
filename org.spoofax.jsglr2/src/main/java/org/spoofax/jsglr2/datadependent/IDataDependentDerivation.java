package org.spoofax.jsglr2.datadependent;

import org.spoofax.jsglr2.parseforest.IDerivation;

public interface IDataDependentDerivation
//@formatter:off
   <ParseForest extends IDataDependentParseForest>
//@formatter:on
    extends IDerivation<ParseForest>, IDataDependentParseForest {

    long getContextBitmap();

}
