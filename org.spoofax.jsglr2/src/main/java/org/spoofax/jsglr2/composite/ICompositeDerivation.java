package org.spoofax.jsglr2.composite;

import org.spoofax.jsglr2.datadependent.IDataDependentDerivation;
import org.spoofax.jsglr2.layoutsensitive.ILayoutSensitiveDerivation;
import org.spoofax.jsglr2.parseforest.basic.IBasicDerivation;

public interface ICompositeDerivation
//@formatter:off
   <ParseForest extends ICompositeParseForest>
    extends
        IBasicDerivation<ParseForest>,
        IDataDependentDerivation<ParseForest>,
        ILayoutSensitiveDerivation<ParseForest>
//@formatter:on
{
}
