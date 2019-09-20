package org.spoofax.jsglr2.datadependent;

import org.spoofax.jsglr2.parseforest.basic.IBasicDerivation;

public interface IDataDependentDerivation
//@formatter:off
   <ParseForest extends IDataDependentParseForest>
//@formatter:on
    extends IBasicDerivation<ParseForest> {

    long getContextBitmap();

}
