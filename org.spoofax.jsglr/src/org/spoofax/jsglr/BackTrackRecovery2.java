package org.spoofax.jsglr;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

import org.spoofax.ArrayDeque;

// TODO: Start all Javadoc comments with a /** token

/*
 * Assumption: some recover productions are implemented in the syntax definition
 * Examples: -> } {avoid}, [a-zA-Z]+ -> LAYOUT {avoid}
 * 
 * Algorithm: 
 * - During parsing: keep a set with parser states for recovering, ignore recover productions
 * - Recovering: Reset parser on last inserted parser state, collect and process recover-productions, continue by backtracking on parser states
 * - Fine tuning: The criteria for inserting recover positions influences quality and performance. 
 */
public class BackTrackRecovery2 implements IRecoverAlgorithm {

    //recover parameters
    private final static int NEW_LINE_INTERVAL =1; //Interval decides when to add a recover position
    private final static int NEW_LINE_CHAR=10;
    private final static int STREAM_INTERVAL=15; //Interval for inserting recover positions. Remark: big interval increases quality but may increase recover time
    private final static int POSITION_CLEANUP=120; // for reason of performance it pays off to keeps the list with recover positions short (big list increases non-error parse time)
    private final static int MAX_LOOPS=50; //protection against infinity
    
    
    private int newLineCount; //Alternative approach for inserting recoverPositions based on newline tokens 
            
    private ArrayList<BacktrackPosition> backtrackPositions; //List to collect parser states. Positions also keeps a collection of recover nodes
    private int positionIndex;//indicates the position from where recovering takes place
    private int addNodeIndex; //indicates on backtrack position the collected recovernodes has to be added

    // TODO: recoverTokenStream seems to be common in all recovery implementations; move to supertype?
    // TODO: recoverTokenStream should never be an ArrayList of boxed integers, like in the other implementations
    private char[] recoverTokenStream; //parsed characters
    private int recoverTokenCount;
    private int tokenIndex; //for Reading recovery tokens
    
    private boolean inRecoverMode; //Recover mode means that recover productions are processed and the resulting stacknodes are preserved in recoverNodes 
    private final SGLR myParser; //parser    
    
    private int nrOfLoops; //number of unsuccessful recover-loops during the current recovery
    private boolean exceededMax;//recovery is stopped because of too many loops    
    
    private long recoverTime;
    
    /**
     * Recover algorithm based on backtracking
     */
    public BackTrackRecovery2(SGLR parser)
    {
        myParser = parser;
        inRecoverMode = false;
        
        //for adding backtrack positions        
        newLineCount =0;
        
        //backtrack positions + indices
        positionIndex =-1;
        addNodeIndex =-1;
        
        backtrackPositions = new ArrayList<BacktrackPosition>();
        //backtrackPositions.ensureCapacity(positionsCleanup +1);
        
        
        //reading tokens during recovery
        tokenIndex=-1;
        recoverTokenStream = new char[200];       
       
        //Protection against infinite looping        
        nrOfLoops=0;        
        exceededMax =false;
        
        recoverTime = 0;
    }
    
    /**
     * Keep nodes during recovery
     * @see org.spoofax.jsglr.IRecoverAlgorithm#handleRecoverProduction(org.spoofax.jsglr.Frame, org.spoofax.jsglr.State, int, int, org.spoofax.jsglr.IParseNode)
     */
    public void handleRecoverProduction(Frame st0, State s, int length,
            int numberOfAvoids, IParseNode t) {        
        if (inRecoverMode) {
            Frame st1;
            Link nl;                  
            st1 = myParser.newStack(s);            
            nl = st1.addLink(st0, t, length);
            nl.avoidCount = numberOfAvoids;
            addRecoverNode(st1, st0); 
        }
    } 
        
    /**
     * Recover node stacks are collected in handleRecoverProductions and processed as normal stacks in second round  (non-Javadoc)
     * @see org.spoofax.jsglr.IRecoverAlgorithm#haltsOnRecoverProduction(org.spoofax.jsglr.Frame)
     */
    public boolean haltsOnRecoverProduction(Frame st0) {
        return true;
    }
    
    /**
     * Keeps node in accessory backtrack position
     */
    private void addRecoverNode(Frame st, Frame parent)
    {
        RecoverNode rn = new RecoverNode(st, myParser.tokensSeen, myParser.lineNumber, myParser.columnNumber, parent);
        backtrackPositions.get(addNodeIndex).recoverNodes.add(rn);        
    }
    
    /**
     * @see org.spoofax.jsglr.IRecoverAlgorithm#afterStreamRead(int)
     */
    public void afterStreamRead(int currToken) {
        recoverTokenStream[recoverTokenCount++] = (char) currToken;         
        if (recoverTokenCount == recoverTokenStream.length) {
            char[] copy = recoverTokenStream;
            recoverTokenStream = new char[recoverTokenStream.length * 2];
            System.arraycopy(copy, 0, recoverTokenStream, 0, copy.length);
        }
    }
    
    /*
     * (non-Javadoc)
     * @see org.spoofax.jsglr.IRecoverAlgorithm#afterParseStep()
     */
    public void afterParseStep() {
        if(keepStackCriteria()){ 
            keepBackTrackPoint();
            if(backtrackPositions.size()>POSITION_CLEANUP) //removing early inserted positions improves non-error parsing performance
            {
                int third = POSITION_CLEANUP /3;
                ArrayList<BacktrackPosition> cleanedPositions = new ArrayList<BacktrackPosition>();
                cleanedPositions.add(backtrackPositions.get(0));
                cleanedPositions.add(backtrackPositions.get(third));
                cleanedPositions.addAll(backtrackPositions.subList(2*third, POSITION_CLEANUP));                
                backtrackPositions=cleanedPositions;                
            }
        }      
        
    }
      
    /*
     * Keeps active stacks and current parser state for reason of recovery
     */
    private void keepBackTrackPoint() {
        BacktrackPosition recPos=new BacktrackPosition(myParser.activeStacks, myParser.tokensSeen, myParser.lineNumber, myParser.columnNumber);                     
        backtrackPositions.add(recPos);
    }
    
    private boolean keepStackCriteria() { //Todo: experiment with criteria        
        return checkNewLineCount();
        //return checkStreamPosition();         
    }

    private boolean checkStreamPosition() {
        return myParser.tokensSeen % STREAM_INTERVAL == 0;
    }

    private boolean checkNewLineCount() {
        if(newLineCount>NEW_LINE_INTERVAL)
        {
            newLineCount=0;
            return true;
        }
        if(myParser.currentToken == NEW_LINE_CHAR)
        {
            newLineCount++;
            
        }
        return false;
    }

    public void recover() throws IOException {
        
        long startTime = System.currentTimeMillis();
        
        /*
        Tools.debug("----------------");
        Tools.debug("Recovery: line = " + myParser.lineNumber);
        int startFrameCount = Frame.framesCreated;
        int startLinkCount = Link.linksCreated;
        long startRecover=System.currentTimeMillis();
        */
        inRecoverMode=true;
        //recovering starts with the first (backwards) unexplored backtrack position
        for (int i = backtrackPositions.size()-1; i > -1; i--) {
            if(backtrackPositions.get(i).isVisited ==false){
                positionIndex =i;
                break;
            }
        }
        localRecover();
        inRecoverMode=false;
        //nrOfLoops=0;
        /*
        long endRecover=System.currentTimeMillis();
        Tools.debug("Recovery: time =" + (endRecover - startRecover));        
        Tools.debug("Recovery: nodes =" + (Frame.framesCreated - startFrameCount));
        Tools.debug("Recovery: links =" + (Link.linksCreated - startLinkCount));
        Tools.debug("Recovery: loops =" + nrOfLoops);
        Tools.debug("Recovery: active-stacks =" + myParser.activeStacks.size());
        Tools.debug("----------------");
        */
        inRecoverMode=false;
        nrOfLoops=0;
        
        recoverTime += System.currentTimeMillis() - startTime;
    }
    
    public long getRecoverTime() {
        return recoverTime;
    }

    

    public boolean meetsRecoverCriteria() {
        return myParser.acceptingStack == null && exceededMax ==false;
    }
    
    /*
     * Sets parser variables on recover position and tries recovering. 
     * After failure, continues with previous recover position
     */
    private void localRecover() throws IOException {   
        BacktrackPosition rp=backtrackPositions.get(positionIndex);
        //collect recover nodes if this is not already done
        if (rp.isVisited ==false) {
            addNodeIndex=positionIndex;
            collectNodesForPosition();
            rp.isVisited =true;
        }
        //set parser on current recover position
        myParser.activeStacks.clear();
        setTokenIndex(rp);
        setParserFields(rp);        
        boolean hasRecovered = tryRecover();//recover with all recover nodes from current recover position to last recover position
        if(hasRecovered) { //parsing continued
            //inRecoverMode=false;
            //nrOfLoops=0;
            //Idea: these stack nodes must be available for recovery after nearby error
            for (Frame actStack : myParser.activeStacks) {
                //needed to keep stack accessable for next recovery
                //Bugfix: +1 because node comes after shifter
                RecoverNode rn = new RecoverNode(actStack, myParser.tokensSeen+1, myParser.lineNumber, myParser.columnNumber, null);
                backtrackPositions.get(addNodeIndex).recoverNodes.add(rn);               
            }
        }
        else { 
            int newIndex = Math.max(0, positionIndex -1); //backtrack idea
            nrOfLoops+=1;
            if(nrOfLoops>MAX_LOOPS)
            {
                exceededMax =true;
            }
            if(exceededMax == false){
                positionIndex = newIndex;
                localRecover();//retry recovering with all new collected recover nodes plus recover nodes one position earlier
            }
        }        
        
    }

    private void collectNodesForPosition()
            throws IOException {
        BacktrackPosition rp = backtrackPositions.get(addNodeIndex);                    
        setTokenIndex(rp);
        myParser.activeStacks.addAll(rp.recoverStacks);  
        setParserFields(rp);
        int endInterval;
        if(addNodeIndex == backtrackPositions.size()-1)
        {
            endInterval = recoverTokenCount;
        }
        else
        {
            endInterval = backtrackPositions.get(addNodeIndex +1).tokensSeen;
        }
        collectRecoverNodes(endInterval);
        
    }

    private void setTokenIndex(BacktrackPosition rp) {        
        tokenIndex = rp.tokensSeen;       
    }    
    
    /*
     * Collects recover nodes for recoverPosition 
     */
    private void collectRecoverNodes(int endInterval) throws IOException {           
        //collect till end position is reached
        while(tokenIndex < endInterval) 
        {
            setCurrentToken();
            myParser.updateParserFields(myParser.currentToken);             
            myParser.doParseStep();                    
        }        
    }
    
    /*
     * Stacks resulting from recover productions are collected in the first round
     * This stacks are added to the active stacks in the next round of recover-parsing.
     * Remark on quality-performance trade-off: a recovering with two recover-productions near [add stack criteria]
     * the error position may be preferred above one recover production on an earlier position 
     */
    private boolean tryRecover() throws IOException {
        ArrayDeque<RecoverNode> rnNodes = new ArrayDeque<RecoverNode>();
        addNodeIndex=positionIndex;
        //collect all relevant recover nodes
        int i=positionIndex;        
        while(i < backtrackPositions.size())
        {
            rnNodes.addAll(backtrackPositions.get(i).recoverNodes);
            backtrackPositions.get(i).recoverNodes.clear(); //list will contain newly produced recover nodes             
            i++;
        }         
        //do recovering till original position is reached
        while(tokenIndex < recoverTokenCount) 
        {
            //nodes are added to position covering the corresponding stream interval
            if (addNodeIndex<backtrackPositions.size()-1) {
                if (tokenIndex == backtrackPositions.get(addNodeIndex + 1).tokensSeen){
                    addNodeIndex++;                    
                }
            }
            setCurrentToken();
            myParser.updateParserFields(myParser.currentToken); 
            activateRecoverFrames(rnNodes); //recover nodes for current position are added to active stacks
            myParser.doParseStep();                    
        }
        return myParser.activeStacks.size() > 0 || myParser.acceptingStack !=null;
    }
    
    private void setParserFields(BacktrackPosition rp) {
        myParser.tokensSeen = rp.tokensSeen;
        myParser.columnNumber = rp.columnNumber;
        myParser.lineNumber = rp.lineNumber;
    }
    
    private void setCurrentToken() {
        myParser.currentToken = recoverTokenStream[tokenIndex];
        tokenIndex++;        
    }
    
    /*
     * Add recover nodes meeting the current position in stream
     */
    private void activateRecoverFrames(ArrayDeque<RecoverNode> rnNodes) {
        boolean inspectR=true; //try adding recover nodes for this token position
        while (rnNodes.size() > 0 && inspectR) {
            if (myParser.tokensSeen == rnNodes.peek().tokensSeen) {
                RecoverNode recNode = rnNodes.removeFirst();
                addRecoverState(recNode);
            }
            else
                inspectR =false; //list is sorted
        }
    }
    
    //a link or a new state is added to include recover stack in the GSS stack object
    private void addRecoverState(RecoverNode recNode) {
        Frame st1 = myParser.findStack(myParser.activeStacks, recNode.recoverStack.state);
        if(st1==null || recNode.parentStack ==null)
            myParser.activeStacks.add(recNode.recoverStack);
        else {
            Link nl = st1.findDirectLink(recNode.parentStack);
            Link oldLink = recNode.recoverStack.findDirectLink(recNode.parentStack);

            if (nl == null) { //no need to handle ambiguity, just skip!?
                nl = st1.addLink(recNode.parentStack, oldLink.label, oldLink.length);
                nl.avoidCount = oldLink.avoidCount;
            }
        }
    }

    public void initialize() {//TODO: clear, init, constructor
        backtrackPositions.clear(); 
        recoverTokenCount = 0;
        inRecoverMode=false;
        tokenIndex=-1;
        newLineCount =0;
        nrOfLoops=0;
        exceededMax=false;    
        recoverTime = 0;
        keepBackTrackPoint(); // keep the initial stack as backtrack point
    }
    ///*
  //Debug help
    private String logRecoverTokens()
    {
        String result = "LINE "+myParser.lineNumber +": ";
        for (int i = 0; i < 50; i++) {
            if (recoverTokenCount - 50 + i > 0) {
                int tok = recoverTokenStream[recoverTokenCount - 50 + i];               
                char tokChar = (char)tok;
                result += tokChar;
            }
        }       
        return result;
    } 
    
    //Debug help
    private String logRecoverInfo()
    {
      
        String result = ""; //todo: info uit current recoverpos
        for (int i = 0; i < 50; i++) {
            if (tokenIndex - 50 + i>0) {
                int tok = recoverTokenStream[tokenIndex - 50 + i];
                char tokChar = (char) tok;
                result += tokChar;
            }
        }        
        return result;
    } //*/
}
