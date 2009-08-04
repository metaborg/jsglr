package org.spoofax.jsglr;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Assumption: a coarse-grained recover syntax is defined as grammar
 * Example: ~[\ \t\12\r\n]+ -> WATER  {avoid}, WATER    ClassBodyDecStar -> ClassBodyDecStar, ...
 * Algorithm: 
 * - During parsing: keep a set with parser states for recovering, ignore recover productions
 * - Recovering: Reset parser on last inserted parser state, collect and process recover-productions, continue by backtracking on parser states
 * - Fine tuning: The criteria for inserting recover positions influences quality and performance.
 * @author maartje
 *
 */
public class CoarseGrainedRecovery implements IRecoverAlgorithm {

    private final static int MAX_RECOVER_NODES = 40; //performance + protection against infinity
    
    protected ArrayList<RecoverNode> recoverNodes;
    private ArrayList<Integer> charList; //parsed characters
    private int charIndex; //index for reading characters    
    private SGLR myParser;    
    private long recoverTime;
    
    public CoarseGrainedRecovery(SGLR parser)
    {
        recoverNodes = new ArrayList<RecoverNode>();
        charList = new ArrayList<Integer>();
        charIndex= 0;
        myParser=parser;
        recoverTime = 0; 
    }
    
    public void afterParseStep() {
        // TODO Auto-generated method stub
        
    }

    public void afterStreamRead(int currToken) {
        charList.add(currToken);
        
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
        if(recoverNodes.size()>MAX_RECOVER_NODES) //removing early inserted positions improves non-error parsing performance
        { 
            int halfMax = MAX_RECOVER_NODES/2;
            ArrayList<RecoverNode> cleanedPositions = new ArrayList<RecoverNode>();
            cleanedPositions.addAll(recoverNodes.subList(halfMax, recoverNodes.size()-1));                
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
        resetOnLastRecoverNode();
        tryRecover();        
        recoverTime += System.currentTimeMillis() - startTime;
    }

    private void resetOnLastRecoverNode() throws IOException {
        RecoverNode rn = recoverNodes.remove(recoverNodes.size()-1);        
        charIndex = rn.tokensSeen-1;
        setCurrentToken();
        setParserFields(rn);        
        myParser.activeStacks.add(rn.recoverStack);        
        while(recoverNodes.size()>0 && recoverNodes.get(recoverNodes.size()-1).tokensSeen==rn.tokensSeen)
        {
            RecoverNode rnLast = recoverNodes.remove(recoverNodes.size()-1); 
            myParser.activeStacks.add(rnLast.recoverStack); 
        }        
        myParser.doParseStep(); //first recover step
        Tools.debug("Recover Start: "+(char)myParser.currentToken);
    }

    private void tryRecover() throws IOException {
        //do recovering till original position is reached
        while(charIndex < charList.size() && myParser.activeStacks.size()> 0) 
        {            
            setCurrentToken();
            myParser.updateParserFields(myParser.currentToken);            
            myParser.doParseStep();                    
            Tools.debug("Recover: "+(char)myParser.currentToken);
        }        
        if(myParser.activeStacks.size()==0 && meetsRecoverCriteria()){
            resetOnLastRecoverNode(); //backtracks to unexplored recover node
            tryRecover();
        }
        
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
        if(charIndex > charList.size()-1)
            myParser.currentToken = 0;
        myParser.currentToken = charList.get(charIndex).intValue();
        charIndex ++;        
    }
    
    private String logRecoverTokens()
    {
        String result = "LINE "+myParser.lineNumber +": ";
        for (int i = 0; i < 50; i++) {
            if (charList.size()-50+i > 0) {
                int tok = charList.get(charList.size()-50+i);               
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
