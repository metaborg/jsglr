package org.spoofax.jsglr;

import java.io.IOException;
import java.util.ArrayList;

public class SimpleRecovering implements IRecoverAlgorithm {

    protected ArrayList<RecoverNode> recoverNodes;
    private ArrayList<Integer> recoverTokens; //parsed characters
    private int recoveryIndex; //for Reading recovery tokens
    private SGLR myParser;
    
    private long recoverTime;
    
    public SimpleRecovering(SGLR parser)
    {
        recoverNodes = new ArrayList<RecoverNode>();
        recoverTokens = new ArrayList<Integer>();
        recoveryIndex= 0;
        myParser=parser;
        recoverTime = 0; 
    }
    
    public void afterParseStep() {
        // TODO Auto-generated method stub
        
    }

    public void afterStreamRead(int currToken) {
        recoverTokens.add(currToken);
        
    }

    public boolean haltsOnRecoverProduction(Frame st0) {
        // TODO Auto-generated method stub
        return true;
    }

    public void handleRecoverProduction(Frame st0, State s, int length,
            int numberOfAvoids, IParseNode t) {
        Frame st1;
        Link nl;
        st1 = myParser.newStack(s);            
        nl = st1.addLink(st0, t, length);
        nl.avoidCount = numberOfAvoids;
        addRecoverNode(st1, st0);        
    }
    
    private void addRecoverNode(Frame st, Frame parent)
    {
        RecoverNode rn = new RecoverNode(st, myParser.tokensSeen, myParser.lineNumber, myParser.columnNumber, parent);
        recoverNodes.add(rn);   
        if(recoverNodes.size()>120) //removing early inserted positions improves non-error parsing performance
        {             
            ArrayList<RecoverNode> cleanedPositions = new ArrayList<RecoverNode>();
            cleanedPositions.addAll(recoverNodes.subList(70, 120));                
            recoverNodes=cleanedPositions;                
        }
    }


    public void initialize() {
        // TODO Auto-generated method stub
        recoverTime = 0;
    }

    public boolean meetsRecoverCriteria() {
        return myParser.acceptingStack == null && this.recoverNodes.size()>0;
    }

    public void recover() throws IOException {
        
        long startTime = System.currentTimeMillis();
        
        //determine start point of recovery
        int startIndex=Math.max(0, recoverNodes.size()-1);
        for (int i = 0; i < recoverNodes.size(); i++) {
            if(myParser.tokensSeen - recoverNodes.get(i).tokensSeen < 500){
                startIndex = i;
                break;
            }
        }    
        
        //set all variables for recovery 
        ArrayList<RecoverNode> rnNodes = new ArrayList<RecoverNode>();            
        rnNodes.addAll(recoverNodes.subList(startIndex, recoverNodes.size()-1)); //recover criteria says > 0        
        recoverNodes.clear(); //Needed for collecting new unused recoverings              
        RecoverNode rn;
        if (rnNodes.size()==0) {
            return;
        }   
        rn = rnNodes.remove(0);
        int recoveryLength = myParser.tokensSeen - rn.tokensSeen;
        recoveryIndex = recoverTokens.size()-1-recoveryLength;
        setCurrentToken();
        setParserFields(rn);        
        activateRecoverFrames(rnNodes);
        myParser.doParseStep(); //first recover step
        //do recovering till original position is reached
        while(recoveryIndex < recoverTokens.size()) 
        {
            setCurrentToken();
            myParser.updateParserFields(myParser.currentToken);
            activateRecoverFrames(rnNodes);
            myParser.doParseStep();                    
        }
        
        recoverTime += System.currentTimeMillis() - startTime;
    }
    
    public long getRecoverTime() {
        return recoverTime;
    }
    
    private void setParserFields(RecoverNode rn) {
        myParser.tokensSeen = rn.tokensSeen;
        myParser.columnNumber = rn.columnNumber;
        myParser.lineNumber = rn.lineNumber;
    }
    
    private void setCurrentToken() {
        myParser.currentToken = recoverTokens.get(recoveryIndex).intValue();
        recoveryIndex ++;        
    }
    /*
     * Add recover nodes meeting the current position in stream
     */
    private void activateRecoverFrames(ArrayList<RecoverNode> rnNodes) {
        boolean inspectR=true; //try adding recover nodes for this token position
        while (rnNodes.size() > 0 && inspectR) {
            if (myParser.tokensSeen == rnNodes.get(0).tokensSeen) {
                RecoverNode recNode = rnNodes.remove(0);
                //addRecoverState(recNode);
                myParser.activeStacks.add(recNode.recoverStack);
            }
            else
                inspectR =false; //list is sorted
        }
    }
/*
    private void addRecoverState(RecoverNode recNode) {
        Frame st1 = myParser.findStack(myParser.activeStacks, recNode.recoverStack.state);
        if(st1==null)
            myParser.activeStacks.add(recNode.recoverStack);
        else {
            Link nl = st1.findDirectLink(recNode.parentStack);
            Link oldLink = recNode.recoverStack.findDirectLink(recNode.parentStack);

            if (nl == null) { //no need to handle ambiguity, just skip!?
                nl = st1.addLink(recNode.parentStack, oldLink.label, oldLink.length);
                nl.avoidCount = oldLink.avoidCount;
            }
        }
    }*/
    private String logRecoverTokens()
    {
        String result = "LINE "+myParser.lineNumber +": ";
        for (int i = 0; i < 50; i++) {
            if (recoverTokens.size()-50+i > 0) {
                int tok = recoverTokens.get(recoverTokens.size()-50+i);               
                char tokChar = (char)tok;
                result+=tokChar;
            }
        }
        /*
        for (int tok : recoverTokens) {
            char tokChar = (char)tok;
            result+=tokChar;
        }*/
        return result;
    }
    
}
