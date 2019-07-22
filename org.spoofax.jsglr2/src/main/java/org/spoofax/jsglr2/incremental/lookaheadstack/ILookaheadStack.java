package org.spoofax.jsglr2.incremental.lookaheadstack;

import org.metaborg.parsetable.query.IActionQuery;
import org.spoofax.jsglr2.incremental.parseforest.IncrementalParseForest;

// TODO the name LookaheadStack is a bit misleading. Rename to something like MixedInputStream?
public interface ILookaheadStack extends IActionQuery {
    void leftBreakdown();

    void popLookahead();

    IncrementalParseForest get();
}
