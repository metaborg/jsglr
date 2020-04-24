package org.spoofax.jsglr2.incremental;

public interface IIncrementalParseState {

    boolean newParseNodesAreReusable();

    void setMultipleStates(boolean multipleStates);

}
