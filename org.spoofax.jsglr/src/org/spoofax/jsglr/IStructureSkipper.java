package org.spoofax.jsglr;

import java.io.IOException;
import java.util.ArrayList;

public interface IStructureSkipper {

    ParserHistory getHistory();
    
    public abstract void setFailureIndex(int failureIndex);

    public abstract void clear();

    public abstract StructureSkipSuggestion getErroneousPrefix()
            throws IOException;

    public abstract ArrayList<StructureSkipSuggestion> getCurrentSkipSuggestions()
            throws IOException;

    public abstract ArrayList<StructureSkipSuggestion> getPreviousSkipSuggestions()
            throws IOException;

    public abstract ArrayList<StructureSkipSuggestion> getPriorSkipSuggestions()
            throws IOException;

    public abstract ArrayList<StructureSkipSuggestion> getParentSkipSuggestions()
            throws IOException;

    public abstract ArrayList<StructureSkipSuggestion> getSibblingForwardSuggestions()
            throws IOException;

    public abstract ArrayList<StructureSkipSuggestion> getSibblingBackwardSuggestions()
            throws IOException;

    public abstract ArrayList<StructureSkipSuggestion> getSibblingSurroundingSuggestions()
            throws IOException;

    public abstract ArrayList<StructureSkipSuggestion> getZoomOnPreviousSuggestions(
            StructureSkipSuggestion prevRegion) throws IOException;

    public abstract ArrayList<StructureSkipSuggestion> getPickErroneousChild(
            StructureSkipSuggestion prevRegion) throws IOException;

}