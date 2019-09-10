package org.spoofax.jsglr2.incremental;

import org.spoofax.jsglr2.incremental.lookaheadstack.ILookaheadStack;
import org.spoofax.jsglr2.incremental.parseforest.IncrementalParseForest;

public interface IIncrementalParseState {

    void initParse(IncrementalParseForest updatedTree, String inputString);

    ILookaheadStack lookahead();

    boolean isMultipleStates();

    void setMultipleStates(boolean multipleStates);

}
