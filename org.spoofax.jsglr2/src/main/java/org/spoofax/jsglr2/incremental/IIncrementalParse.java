package org.spoofax.jsglr2.incremental;

import org.spoofax.jsglr2.incremental.lookaheadstack.ILookaheadStack;

public interface IIncrementalParse {

    ILookaheadStack lookahead();

    boolean isMultipleStates();

    void setMultipleStates(boolean multipleStates);

}
