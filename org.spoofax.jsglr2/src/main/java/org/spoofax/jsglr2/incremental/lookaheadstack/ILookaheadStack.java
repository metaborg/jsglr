package org.spoofax.jsglr2.incremental.lookaheadstack;

import org.metaborg.parsetable.query.IActionQuery;
import org.spoofax.jsglr2.incremental.parseforest.IncrementalParseForest;

// TODO the name LookaheadStack is a bit misleading. Rename to something like MixedInputStream?
public interface ILookaheadStack extends IActionQuery, Cloneable {
    void leftBreakdown();

    void popLookahead();

    IncrementalParseForest get();

    // TODO add support for recovery. Resetting should push the characters between new and old position on the stack.
    // This is an optimization because the parse nodes in the the part of the input being recovered need to be broken
    // down anyway.
    // void resetPosition(int position);
}
