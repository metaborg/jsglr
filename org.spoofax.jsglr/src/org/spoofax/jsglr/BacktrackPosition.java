package org.spoofax.jsglr;

import java.util.ArrayList;

import org.spoofax.ArrayDeque;

public class BacktrackPosition {
    public final ArrayDeque<Frame> recoverStacks;
    public final int tokensSeen;
    public final int lineNumber;
    public final int columnNumber;
    public final ArrayList<RecoverNode> recoverNodes; 
    public boolean isVisited;
    
    public BacktrackPosition( ArrayDeque<Frame> activeStacks, int tokSeen, int line, int col)
    {
        recoverStacks = new ArrayDeque<Frame>(activeStacks);
        this.tokensSeen = tokSeen;
        lineNumber = line;
        columnNumber = col;
        recoverNodes=new ArrayList<RecoverNode>();
    }

}
