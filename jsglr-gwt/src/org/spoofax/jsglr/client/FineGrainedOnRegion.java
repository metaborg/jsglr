package org.spoofax.jsglr.client;

import java.util.ArrayList;

import org.spoofax.jsglr.shared.ArrayDeque;

public class FineGrainedOnRegion {
    private static final int MAX_RECOVERIES_PER_LINE = 3;
    private static final int MAX_NR_OF_LINES = 25;
    private int acceptRecoveryPosition;
    private int regionEndPosition;
    private ArrayList<BacktrackPosition> choicePoints;
    private SGLR mySGLR;
    private int maxPerLine;
    
    private ParserHistory getHistory() {
        return mySGLR.getHistory();
    }
   
    public void setRegionInfo(StructureSkipSuggestion erroneousRegion, int acceptPosition){
        regionEndPosition=erroneousRegion.getEndSkip().getTokensSeen();
        acceptRecoveryPosition=acceptPosition;
        int lastIndex=Math.min(erroneousRegion.getIndexHistoryEnd(), getHistory().getIndexLastLine());
        if(lastIndex<0 || erroneousRegion.getIndexHistoryStart()<0 || erroneousRegion.getIndexHistoryStart()>erroneousRegion.getIndexHistoryEnd()){
            System.err.println("Something went wrong with the region index");
            return;
        }            
        for (int i = erroneousRegion.getIndexHistoryStart(); i < lastIndex; i++) {
            IndentInfo line= getHistory().getLine(i);
            if(line.getStackNodes()!=null && line.getStackNodes().size()>0){
                BacktrackPosition btPoint=new BacktrackPosition(line.getStackNodes(), line.getTokensSeen());
                btPoint.setIndexHistory(i);
                choicePoints.add(btPoint);
            }            
        } 
        maxPerLine=MAX_RECOVERIES_PER_LINE;
        if(erroneousRegion.getIndexHistoryEnd()-erroneousRegion.getIndexHistoryStart()==1){
            maxPerLine=2;            
        }
    }
    
    public boolean recover() {
       // System.out.println("FINE GRAINED RECOVERY STARTED");
        mySGLR.setFineGrainedOnRegion(true);
        boolean succeeded=recoverFrom(choicePoints.size()-1, new ArrayList<RecoverNode>());
        mySGLR.setFineGrainedOnRegion(false);
        return succeeded;
    }
    
    private boolean recoverFrom(int indexCP, ArrayList<RecoverNode> candidates) {        
        int loops=choicePoints.size()-1-indexCP;
        if(indexCP<-1*maxPerLine)//first line 3 times explored
            return false;
        if(loops>MAX_NR_OF_LINES)//max nr of lines explored in backtracking
            return false;
        int indexChoichePoints=Math.max(0, indexCP);
        if (indexChoichePoints >= choicePoints.size())
            return false;
        BacktrackPosition btPosition=choicePoints.get(indexChoichePoints);
        mySGLR.activeStacks.clear(true);
        mySGLR.activeStacks.addAll(btPosition.recoverStacks);
        getHistory().deleteLinesFrom(btPosition.getIndexHistory());
        getHistory().setTokenIndex(btPosition.tokensSeen);
        int endPos=regionEndPosition;
        if(indexChoichePoints<choicePoints.size()-maxPerLine)
            endPos=choicePoints.get(indexChoichePoints+maxPerLine).tokensSeen;        
        ArrayList<RecoverNode> newCandidates=recoverParse(candidates, endPos, true);
        if(mySGLR.activeStacks.size()>0 || mySGLR.acceptingStack!=null){
            //if (loops<=MAX_RECOVERIES_PER_LINE) {
                ///*
                ArrayDeque<Frame> stacks = new ArrayDeque<Frame>();
                stacks.addAll(mySGLR.activeStacks);
                mySGLR.setFineGrainedOnRegion(false);
                //extra set of recover stacks
                mySGLR.activeStacks.clear(true);
                mySGLR.activeStacks.addAll(btPosition.recoverStacks);
                getHistory().setTokenIndex(btPosition.tokensSeen);
                recoverParse(newCandidates, endPos, false);
                for (Frame frame : stacks) {
                    mySGLR.addStack(frame);
                }
            //}
            //*/
            return true;
        }
        return recoverFrom(indexCP-1, newCandidates);    
    }

    private ArrayList<RecoverNode> recoverParse(ArrayList<RecoverNode> candidates, int endRecoverSearchPos, boolean keepHistory) {
       // System.out.println("RECOVER PARSE");
        ArrayList<RecoverNode> newCandidates=new ArrayList<RecoverNode>();
        boolean firstRound=false;//true;
        while(getHistory().getTokenIndex()<=acceptRecoveryPosition && mySGLR.acceptingStack==null){
            int curTokIndex=getHistory().getTokenIndex();
            addCurrentCandidates(candidates, curTokIndex);
            getHistory().readRecoverToken(mySGLR, keepHistory);
            mySGLR.doParseStep();
            //char logToken=(char)mySGLR.currentToken;
            //if(logToken==' '){logToken='^';}
            //if(logToken==SGLR.EOF){logToken='$';}
            //System.out.print(logToken);
            if(curTokIndex<=endRecoverSearchPos && !firstRound){
                newCandidates.addAll(collectNewRecoverCandidates(curTokIndex));
                //if(newCandidates.size()>oldSize)
                  //  System.out.println("CANDIDATES: " + (newCandidates.size()-oldSize));
            }
            firstRound=false;
            //if(getHistory().getTokenIndex()==endRecoverSearchPos)
              //  System.out.print("@End Search@");
            mySGLR.clearRecoverStacks();
        }
        //System.out.println("Nr. of candidates found: "+newCandidates.size());
        return newCandidates;
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
        for (RecoverNode recoverNode : candidates) {//TODO: improve efficiency by using a sorted list
            if(tokenPosition==recoverNode.tokensSeen){
                //mySGLR.activeStacks.add(recoverNode.recoverStack);
                mySGLR.addStack(recoverNode.recoverStack);
                //System.out.println("Stack added, new count: "+ mySGLR.activeStacks.size());
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
