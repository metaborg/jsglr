package org.spoofax.jsglr;

import java.io.IOException;
import java.util.ArrayList;

public class ParserHistory {
    
    private final static int MAX_SIZE_NEW_LINE_POINTS = 150;
    private final static int MIN_SIZE_NEW_LINE_POINTS = 50;
    private IndentationHandler indentHandler;
    private IndentationHandler recoveryIndentHandler;
    
    private ArrayList<IndentInfo> newLinePoints;      
    public char[] recoverTokenStream;
    private int recoverTokenCount;
    private int tokenIndex;
    
    public int getTokenIndex() {
        return tokenIndex;
    }
    
    public int getIndexLastToken() {
        return recoverTokenCount-1;
    }

    public void setTokenIndex(int tokenIndex) {
        this.tokenIndex = tokenIndex;
    }
    
    public ParserHistory(){    
        newLinePoints=new ArrayList<IndentInfo>();        
        reset();
    }
     
    private void reset(){
        newLinePoints.clear();
        recoverTokenStream = new char[5000];
        recoverTokenCount = 0;
        tokenIndex=0;
        indentHandler = new IndentationHandler();
        recoveryIndentHandler=new IndentationHandler();
    }
    
    public void resetRecoveryIndentHandler(int indentValue){
        recoveryIndentHandler=new IndentationHandler();
        recoveryIndentHandler.setInLeftMargin(true);
        recoveryIndentHandler.setIndentValue(indentValue);
    }
    /*
     * Set current token of parser based on recover tokens or read from new tokens
     */
    public void readRecoverToken(SGLR myParser, boolean keepRecoveredLines) throws IOException{  
        if (hasFinishedRecoverTokens()) {             
            if(myParser.currentToken!=SGLR.EOF){                
                if(getIndexLastToken()>0 && recoverTokenStream[getIndexLastToken()]!=SGLR.EOF){
                    myParser.readNextToken();
                    indentHandler.updateIndentation(myParser.currentToken);
                    keepToken((char)myParser.currentToken);   
                    if(indentHandler.lineMarginEnded() || myParser.currentToken==SGLR.EOF)
                        keepNewLinePoint(myParser, myParser.tokensSeen-1, true, indentHandler);
                }
            }
        }
        else if(tokenIndex<0 || tokenIndex>recoverTokenCount){
            myParser.currentToken =SGLR.EOF;
            System.err.println("Unexpected token index"+tokenIndex);
        }
        else{
            myParser.currentToken = recoverTokenStream[tokenIndex];
            if(keepRecoveredLines){
                recoveryIndentHandler.updateIndentation(myParser.currentToken);
                if(recoveryIndentHandler.lineMarginEnded() || myParser.currentToken==SGLR.EOF)
                    keepNewLinePoint(myParser, tokenIndex, false, recoveryIndentHandler);
            }    
        }
        tokenIndex++;
        
    }
    
    public boolean hasFinishedRecoverTokens() {
        return tokenIndex >= recoverTokenCount;
    }
    
    public int getTokensSeenStartLine(int tokPosition){
        int tokIndexLine=tokPosition;
        while (recoverTokenStream[tokIndexLine] != '\n' && tokIndexLine>0) {
            tokIndexLine-=1;
        }
        return tokIndexLine;
    }

    public void keepTokenAndState(SGLR myParser) {
        indentHandler.updateIndentation(myParser.currentToken);
        keepToken((char)myParser.currentToken);
        tokenIndex++;
        if(indentHandler.lineMarginEnded() || myParser.currentToken==SGLR.EOF)
            keepNewLinePoint(myParser, myParser.tokensSeen-1, false, indentHandler);
    }
    
    public void keepInitialState(SGLR myParser) {        
        IndentInfo newLinePoint= new IndentInfo(0, 0, 0);
        newLinePoint.fillStackNodes(myParser.activeStacks);
        newLinePoints.add(newLinePoint);
    }

    private void keepToken(char currentToken) {
        if(getIndexLastToken()>0 && recoverTokenStream[getIndexLastToken()]==SGLR.EOF)
            return;
        recoverTokenStream[recoverTokenCount++] = currentToken;         
        if (recoverTokenCount == recoverTokenStream.length) {
            char[] copy = recoverTokenStream;
            recoverTokenStream = new char[recoverTokenStream.length * 2];
            System.arraycopy(copy, 0, recoverTokenStream, 0, copy.length);
        }
    }
    
    private void keepNewLinePoint(SGLR myParser, int tokSeen ,boolean inRecoverMode, IndentationHandler anIndentHandler) {
        int indent = anIndentHandler.getIndentValue();
        IndentInfo newLinePoint= new IndentInfo(myParser.lineNumber, tokSeen, indent);
        newLinePoints.add(newLinePoint);
        //System.out.println(newLinePoints.size()-1+" NEWLINE ("+newLinePoint.getIndentValue()+")"+newLinePoint.getTokensSeen());
        if(!inRecoverMode){
            newLinePoint.fillStackNodes(myParser.activeStacks);           
            if(newLinePoints.size()> MAX_SIZE_NEW_LINE_POINTS)
                removeOldPoints();
        }
    }
    
    private void removeOldPoints() {        
        int firstPointIndex = nrOfLines()-MIN_SIZE_NEW_LINE_POINTS;
        ArrayList<IndentInfo> shrinkedList = new ArrayList<IndentInfo>();
        shrinkedList.ensureCapacity(newLinePoints.size());
        shrinkedList.addAll(newLinePoints.subList(firstPointIndex, newLinePoints.size()-1));
        newLinePoints = shrinkedList;
    }

    public String getFragment(int startTok, int endTok) {
        String fragment="";
        for (int i = startTok; i <= endTok; i++) {
            if(i >= recoverTokenCount)
                break;
            fragment+= recoverTokenStream[i];
        }        
        return fragment;
    }
    
    public String getFragment(StructureSkipSuggestion skip) {
        if(skip.getEndSkip().getTokensSeen() < skip.getStartSkip().getTokensSeen()){
            System.err.println("Startskip > endskip");
            //System.err.println(getFragment(skip.getEndSkip().getTokensSeen(), skip.getEndSkip().getTokensSeen()));
            return "--Wrong Fragment --";
        }
        String fragment="";
        for (int i = skip.getStartSkip().getTokensSeen(); i <= skip.getEndSkip().getTokensSeen()-1; i++) {
            if(i >= recoverTokenCount)
                break;
            fragment+= recoverTokenStream[i];
        }  
        String correctedFragment=fragment.substring(skip.getAdditionalTokens().length);
        return correctedFragment;
    }
    
    public String readLine(int StartTok) {
        String fragment="";
        int pos=StartTok;
        char currentTok=' ';
        while(currentTok!='\n' && currentTok!=SGLR.EOF && pos<recoverTokenCount) {            
            currentTok=recoverTokenStream[pos];
            fragment+= currentTok;
            pos++;
        }        
        return fragment;
    }
    
    private int nrOfLines(){
        return newLinePoints.size();
    }
    
    public IndentInfo getLine(int index){
        if(index < 0 || index > getIndexLastLine())
            return null;
        return newLinePoints.get(index);
    }
    
    public IndentInfo getLastLine(){
        return newLinePoints.get(newLinePoints.size()-1);
    }
    
    public int getIndexLastLine(){
        return newLinePoints.size()-1;
    }
    
    public ArrayList<IndentInfo> getLinesFromTo(int startIndex, int endLocation) {
        int indexLine = startIndex;
        ArrayList<IndentInfo> result=new ArrayList<IndentInfo>();
        IndentInfo firstLine = newLinePoints.get(indexLine);
        while(indexLine < newLinePoints.size()){
             firstLine = newLinePoints.get(indexLine);
             if(firstLine.getTokensSeen() < endLocation){
                 result.add(firstLine);
                 indexLine++;
             }
             else{
                 indexLine=newLinePoints.size();
             }
        }
        return result;
    }

    public void deleteLinesFrom(int startIndexErrorFragment) {
        if(startIndexErrorFragment>=0 && startIndexErrorFragment<newLinePoints.size()-1){
            ArrayList<IndentInfo> shrinkedList=new ArrayList<IndentInfo>();
            shrinkedList.addAll(newLinePoints.subList(0, startIndexErrorFragment));
            newLinePoints=shrinkedList;
        }
        else if (startIndexErrorFragment > newLinePoints.size()-1){
            System.err.println("StartIndex Error Fragment: "+startIndexErrorFragment);
            System.err.println("Numeber Of Lines in History: : "+newLinePoints.size());
            System.err.println("Unexpected index of history new-line-points");            
        }
    }
    
    public void logHistory(){       
       for (int i = 0; i < newLinePoints.size()-1; i++) {
           IndentInfo currLine=newLinePoints.get(i);
           IndentInfo nextLine=newLinePoints.get(i+1);
           String stackDescription="";
           for (Frame node : currLine.getStackNodes()) {
               stackDescription+=node.state.stateNumber+";";
           }
           System.out.print("("+i+")"+"["+currLine.getIndentValue()+"]"+"{"+stackDescription+"}"+getFragment(currLine.getTokensSeen(), nextLine.getTokensSeen()-1));
       }
       IndentInfo currLine=newLinePoints.get(newLinePoints.size()-1);
       System.out.print("("+(newLinePoints.size()-1)+")"+"["+currLine.getIndentValue()+"]"+getFragment(currLine.getTokensSeen(), getIndexLastToken()-1));

    }

    public int getLineOfTokenPosition(int tokPos) {        
        for (int i = 1; i < newLinePoints.size(); i++) {
            IndentInfo line=newLinePoints.get(i);
            if(line.getTokensSeen()>tokPos)
                return i-1;
        }
        return newLinePoints.size()-1;
    }
    
}
