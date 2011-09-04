package org.spoofax.jsglr.client;

import java.util.ArrayList;
import java.util.List;

public class FineGrainedOnRegion {

    private static final int TIME_LIMIT = 500;
	private static final int MAX_NUMBER_OF_RECOVER_BRANCHES = 1000;
	private static final int MAX_NR_OF_EXPLORED_LINES = 75;
	private int acceptRecoveryPosition;
    private int regionEndPosition;
    private ArrayList<BacktrackPosition> choicePoints;
    private static int MAX_BACK_JUMPS=6;
    private SGLR mySGLR;
    private long recoveryStartTime;
    private boolean hasRegionFallBack;
    
    private ParserHistory getHistory() {
        return mySGLR.getHistory();
    }
   
    public void setInfoFGOnly(int tokensSeen, int lastIndex){
        regionEndPosition = Integer.MAX_VALUE;
        acceptRecoveryPosition=tokensSeen + 20;
        for (int i = Math.max(0, lastIndex-MAX_NR_OF_EXPLORED_LINES); i < lastIndex; i++) {
            IndentInfo line= getHistory().getLine(i);
            if(line.getStackNodes()!=null && line.getStackNodes().size()>0){
                BacktrackPosition btPoint=new BacktrackPosition(line.getStackNodes(), line.getTokensSeen());
                btPoint.setIndexHistory(i);
                choicePoints.add(btPoint);
            }            
        }
        hasRegionFallBack = false;
    }
    
    public void setRegionInfo(StructureSkipSuggestion erroneousRegion, int acceptPosition){
        regionEndPosition=erroneousRegion.getEndSkip().getTokensSeen();
        acceptRecoveryPosition=acceptPosition;
        int lastIndex=Math.min(erroneousRegion.getIndexHistoryEnd(), getHistory().getIndexLastLine());
        assert(
        	lastIndex >= 0 &&
        	erroneousRegion.getIndexHistoryStart()>=0 && 
        	erroneousRegion.getIndexHistoryStart()<=erroneousRegion.getIndexHistoryEnd()
        );
        int btStartIndex=Math.max(erroneousRegion.getIndexHistoryStart(), lastIndex-MAX_NR_OF_EXPLORED_LINES);
        for (int i = btStartIndex; i < lastIndex; i++) {
            IndentInfo line= getHistory().getLine(i);
            if (line.getStackNodes() != null && line.getStackNodes().size()>0){
                BacktrackPosition btPoint=new BacktrackPosition(line.getStackNodes(), line.getTokensSeen());
                btPoint.setIndexHistory(i);
                choicePoints.add(btPoint);
            }            
        }
        hasRegionFallBack = true;
    }
    
    public boolean recover() {
        int btIndex=choicePoints.size()-1;
        if(btIndex>=0){
        	recoveryStartTime = System.currentTimeMillis();
        	return recoverFrom(btIndex, new ArrayList<RecoverNode>());
        }
        return false;
    }

	private boolean recoverFrom(int btIndex, ArrayList<RecoverNode> unexplored_branches) {
		if(hasRegionFallBack && System.currentTimeMillis()-recoveryStartTime > TIME_LIMIT)
			return false;
        ArrayList<RecoverNode> rec_Branches=new ArrayList<RecoverNode>();
        if(btIndex>=0){
        	rec_Branches=collectRecoverBranches(btIndex); //collect permissive branches at btIndex line
        	resetSGLR(btIndex);
        }
        else
        	resetSGLR(0);
        rec_Branches.addAll(unexplored_branches);
    	
        ArrayList<RecoverNode> newbranches=recoverParse(rec_Branches, regionEndPosition); //explore and collect

    	if(acceptParse())
        	return true;
        if(choicePoints.size()-1-btIndex > MAX_BACK_JUMPS){
        	if(btIndex>0){
        		//Permissive branches constructed by one recovery are always explored
	        	ArrayList<RecoverNode> rec_Branches_prefix=new ArrayList<RecoverNode>();
	        	rec_Branches_prefix=collectRecoverBranches(0, btIndex);
	        	resetSGLR(0);
	        	ArrayList<RecoverNode> oneRecoverBranches = recoverParse(rec_Branches_prefix, regionEndPosition);
	        	if(!acceptParse()){
	        		resetSGLR(0);
	        		recoverParse(oneRecoverBranches, regionEndPosition);
	        	}
	        	return acceptParse();
        	}
        	return false;
        }
        return recoverFrom(btIndex-1, newbranches);
	}

	private void resetSGLR(int btIndex) {
		BacktrackPosition btrPosition=choicePoints.get(btIndex);    
    	mySGLR.activeStacks.clear(true); //only permissive branches are explored
        getHistory().setTokenIndex(btrPosition.tokensSeen);
	}

	private ArrayList<RecoverNode> collectRecoverBranches(int btIndex) {
		resetSGLR(btIndex);
        mySGLR.activeStacks.addAll(choicePoints.get(btIndex).recoverStacks);
        int endPos=btIndex<choicePoints.size()-1 ? choicePoints.get(btIndex+1).tokensSeen-1 : regionEndPosition;
    	ArrayList<RecoverNode> rec1_Branches=recoverParse(new ArrayList<RecoverNode>(), endPos);
		return rec1_Branches;
	}
	
	private ArrayList<RecoverNode> collectRecoverBranches(int btIndex, int btIndex_end) {
		resetSGLR(btIndex);
        mySGLR.activeStacks.addAll(choicePoints.get(btIndex).recoverStacks);
        int endPos=btIndex_end < choicePoints.size() ? choicePoints.get(btIndex_end).tokensSeen-1 : regionEndPosition;
    	ArrayList<RecoverNode> rec1_Branches=recoverParse(new ArrayList<RecoverNode>(), endPos);
		return rec1_Branches;
	}
    
    /** 
     * Explores permissive branches, and collects derived branches with higher recover count
     */
    private ArrayList<RecoverNode> recoverParse(ArrayList<RecoverNode> candidates, int endRecoverSearchPos) {
    	if(candidates.size() > MAX_NUMBER_OF_RECOVER_BRANCHES)
    		candidates = new ArrayList<RecoverNode>(candidates.subList(0, MAX_NUMBER_OF_RECOVER_BRANCHES/2)); //too much stacks causes problems for performance while they probably contain multiple solutions
    	mySGLR.setFineGrainedOnRegion(true);
        ArrayList<RecoverNode> newCandidates=new ArrayList<RecoverNode>();
        int curTokIndex;
        do {
            curTokIndex=getHistory().getTokenIndex();
            addCurrentCandidates(candidates, curTokIndex);
            getHistory().readRecoverToken(mySGLR, false);
            //System.out.print((char)mySGLR.currentToken);
            mySGLR.doParseStep();
            newCandidates.addAll(collectNewRecoverCandidates(curTokIndex));
            mySGLR.getRecoverStacks().clear();
        } while(getHistory().getTokenIndex()<= endRecoverSearchPos && mySGLR.acceptingStack==null && mySGLR.getCurrentToken()!=SGLR.EOF);
    	mySGLR.setFineGrainedOnRegion(false);
        return newCandidates;
    }
    
    /**
     * Permissive branches are accepted if they are still alive at the accepting position
     */
    private boolean acceptParse(){
        while(getHistory().getTokenIndex()<= acceptRecoveryPosition && mySGLR.acceptingStack==null && mySGLR.activeStacks.size()>0){
            getHistory().readRecoverToken(mySGLR, false);
            //System.out.print((char)mySGLR.currentToken);
            mySGLR.doParseStep();
        }
    	return mySGLR.activeStacks.size()>0 || mySGLR.acceptingStack!=null;
    }

    private ArrayList<RecoverNode> collectNewRecoverCandidates(int tokenIndex) {
        ArrayList<RecoverNode> results=new ArrayList<RecoverNode>();
        for (Frame recoverStack : mySGLR.getRecoverStacks()) {
            RecoverNode rn = new RecoverNode(recoverStack, tokenIndex);
            results.add(rn);
        }
        return results;
    }
    
    private void addCurrentCandidates(ArrayList<RecoverNode> candidates, int tokenPosition) {
        for (RecoverNode recoverNode : candidates) {
            if(tokenPosition==recoverNode.tokensSeen){
            	Frame st =mySGLR.findStack(mySGLR.activeStacks, recoverNode.recoverStack.state);
                if(st!=null) {
                	for (Link ln : recoverNode.recoverStack.getAllLinks()) {
                		st.addLink(ln);
					}                	
                }
                else
                	mySGLR.addStack(recoverNode.recoverStack);
            }
        }
    }

    public FineGrainedOnRegion(SGLR parser){
        mySGLR=parser;
        choicePoints=new ArrayList<BacktrackPosition>();
    }

    public boolean parseRemainingTokens() {
        // TODO what if parsing fails here???
        while(!getHistory().hasFinishedRecoverTokens() && mySGLR.activeStacks.size()>0 && mySGLR.acceptingStack==null){        
            getHistory().readRecoverToken(mySGLR, true);
            //System.out.print((char)mySGLR.currentToken);
            mySGLR.doParseStep();            
        }  
        return mySGLR.activeStacks.size()>0 || mySGLR.acceptingStack!=null;        
    }
}
