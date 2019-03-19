package org.spoofax.jsglr2.incremental.lookaheadstack;

import org.metaborg.parsetable.IActionQuery;
import org.spoofax.jsglr2.incremental.parseforest.IncrementalParseForest;

public interface ILookaheadStack extends IActionQuery {
    void leftBreakdown();

    void popLookahead();

    IncrementalParseForest get();
}
