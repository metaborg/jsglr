package org.spoofax.jsglr.client;

import java.util.ArrayList;

import org.spoofax.terms.util.PushbackStringIterator;

public class ParserHistory {
    
    private IndentationHandler indentHandler;
    private IndentationHandler recoveryIndentHandler;
    
    private ArrayList<IndentInfo> newLinePoints;
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
        clear();
    }
     
    public void clear(){
        newLinePoints.clear();
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
    public void readRecoverToken(SGLR myParser, boolean keepStacks) { 
        if (hasFinishedRecoverTokens()) {             
            if(myParser.getCurrentToken().getToken()!=SGLR.EOF){                
                if(getIndexLastToken()>=0){
                    myParser.readNextToken();
                    indentHandler.updateIndentation(myParser.getCurrentToken().getToken());
                    recoverTokenCount++;   
                    if (myParser.getCurrentToken().getToken()==SGLR.EOF)
                        keepNewLinePoint(myParser, myParser.getParserLocation(), !keepStacks, indentHandler);
                    else if (indentHandler.lineMarginEnded())
                        keepNewLinePoint(myParser, myParser.getParserLocation()-1, !keepStacks, indentHandler);
                }
            }
        }
        else{
            int ct = readCharAt(tokenIndex, myParser.currentInputStream);
            myParser.setCurrentToken(new TokenOffset(ct, tokenIndex));
            
            
            if (myParser.getReadNonLayout() && myParser.getApplyCompletionProd())
                myParser.setApplyCompletionProd(false);
            else if (!myParser.getApplyCompletionProd() && tokenIndex < myParser.getCursorLocation()) {
                myParser.setReadNonLayout(false);
                myParser.setApplyCompletionProd(true);
            }
            
            if (tokenIndex > myParser.getCursorLocation() && !myParser.isLayout(ct))
                myParser.setReadNonLayout(true);
            
            
            
            if(myParser.getCurrentToken().getToken() == -1) {
            	myParser.setCurrentToken(new TokenOffset(SGLR.EOF, Integer.MAX_VALUE));
    		}
            if(keepStacks)
            	addStackNodesToNewLinePoint(myParser);
        }
        tokenIndex++;        
    }
    
    public boolean hasFinishedRecoverTokens() {
        return tokenIndex >= recoverTokenCount;
    }
    
    public int getTokensSeenStartLine(int tokPosition, PushbackStringIterator chars){
        int tokIndexLine=tokPosition;
        while (readCharAt(tokIndexLine, chars) != '\n' && tokIndexLine>0) {
            tokIndexLine-=1;
        }
        return tokIndexLine;
    }
    
    private int readCharAt(int offset, PushbackStringIterator chars){
    	chars.setOffset(offset);
        return chars.read();
    }

    public void keepTokenAndState(SGLR myParser) {
        indentHandler.updateIndentation(myParser.getCurrentToken().getToken());
        recoverTokenCount++;
        tokenIndex++;
        //assert myParser.tokensSeen == this.getTokenIndex(): "inconsistentcy in token index";
        if (indentHandler.lineMarginEnded() || myParser.getCurrentToken().getToken()==SGLR.EOF || tokenIndex == 1)
            keepNewLinePoint(myParser, myParser.getParserLocation() - 1, false, indentHandler);
        else if (indentHandler.lineMarginEnded() || myParser.getCurrentToken().getToken()==SGLR.EOF || tokenIndex == 1)
            keepNewLinePoint(myParser, myParser.getParserLocation() - 1, false, indentHandler);
    }
    
    public void keepInitialState(SGLR myParser) {        
        IndentInfo newLinePoint= new IndentInfo(0, 0, 0);
        newLinePoint.fillStackNodes(myParser.activeStacks);
        newLinePoints.add(newLinePoint);
    }
    
    private void keepNewLinePoint(SGLR myParser, int tokSeen ,boolean inRecoverMode, IndentationHandler anIndentHandler) {
        int indent = anIndentHandler.getIndentValue();
        IndentInfo newLinePoint= new IndentInfo(myParser.lineNumber, tokSeen, indent);
        newLinePoints.add(newLinePoint);
        //System.out.println(newLinePoints.size()-1+" NEWLINE ("+newLinePoint.getIndentValue()+")"+newLinePoint.getTokensSeen());
        if (!inRecoverMode){
            newLinePoint.fillStackNodes(myParser.activeStacks);           
        }
    }
    
    private void addStackNodesToNewLinePoint(SGLR myParser) {
		// TODO Auto-generated method stub
    	int tokensSeen = myParser.getParserLocation() - 1;
    	for (int i = newLinePoints.size()-1; i >= 0; i--) {
			IndentInfo newLinePoint = newLinePoints.get(i);
			if(newLinePoint.getTokensSeen() == tokensSeen){
	            newLinePoint.fillStackNodes(myParser.activeStacks);
	            return;
	        }
			if(newLinePoint.getTokensSeen() < tokensSeen)
				return;
		}
	}


    public String getFragment(int startTok, int endTok, PushbackStringIterator chars) {
        StringBuilder fragment = new StringBuilder();
        for (int i = startTok; i <= endTok; i++) {
        	int nextChar = readCharAt(i, chars);
            if(i >= recoverTokenCount || nextChar == -1)
                break;
            fragment.append((char)nextChar);
        }        
        return fragment.toString();
    }
    
    public String readLine(int StartTok, PushbackStringIterator chars) {
        StringBuilder fragment = new StringBuilder();
        int pos=StartTok;
        int currentTok=' ';
        while(currentTok!='\n' && currentTok!=SGLR.EOF && pos<recoverTokenCount) {            
            currentTok=readCharAt(pos, chars);
            fragment.append((char)currentTok);
            pos++;
        }        
        return fragment.toString();
    }
    
    public IndentInfo getLine(int index){
        // FIXME: throw an IndexOutOfBoundsException: this is indicates a programmer error
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
    
    /*
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

    }*/

    public int getLineOfTokenPosition(int tokPos) {        
        for (int i = 1; i < newLinePoints.size(); i++) {
            IndentInfo line=newLinePoints.get(i);
            if(line.getTokensSeen()>tokPos)
                return i-1;
        }
        return newLinePoints.size()-1;
    }
    
}
