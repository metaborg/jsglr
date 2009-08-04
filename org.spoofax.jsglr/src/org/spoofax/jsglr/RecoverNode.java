package org.spoofax.jsglr;

public class RecoverNode {
    public Frame recoverStack;
    public int tokensSeen;
    public int lineNumber;
    public int columnNumber;
    public Frame parentStack;
    
    public RecoverNode(Frame st, int tokSeen, int line, int col, Frame parent)
    {
        recoverStack =st;
        this.tokensSeen = tokSeen;
        lineNumber = line;
        columnNumber = col;
        parentStack=parent;
    }

}
