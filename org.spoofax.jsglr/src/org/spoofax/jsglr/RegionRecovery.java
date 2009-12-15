package org.spoofax.jsglr;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Supports error recovery by selecting the region containing the error.
 * This region can be discarded (parsed as layout) or can be inspected by a refined recover method
 */
public class RegionRecovery {

    private SGLR myParser;
    IStructureSkipper regionSelector;
    IStructureSkipper newRegionSelector;
    private StructureSkipSuggestion erroneousRegion;
    private boolean hasFoundErroneousRegion;
    private int errorDetectionLocation;
    private static int NR_OF_LINES_TILL_SUCCESS=3;
    private boolean useDebugMode;
    
    /**
     * Says whether an erroneous region is found
     * @return
     */
    public boolean hasFoundErroneousRegion() {
        return hasFoundErroneousRegion;
    }
    
    /**
     * Prints information about the selected regions to the console
     */
    public void setUseDebugMode(boolean useDebugMode) {
        this.useDebugMode = useDebugMode;
    }

    /**
     * Used for testing, accepts a recovery only after end of file is reached
     */
    public void setEndOfFileSuccessMode(){
        NR_OF_LINES_TILL_SUCCESS=Integer.MAX_VALUE;
    }
    
    /**
     * Supports error recovery by selecting the region containing the error
     */
    public RegionRecovery(SGLR sglr){
        myParser=sglr;
        regionSelector = new StructureSkipper(sglr);
        newRegionSelector=new NewStructureSkipper(sglr);
    }
    
    private ParserHistory getHistory() {
        return myParser.getHistory();
    }
    
    /** *
     *  Returns info about the parser configuration at the start of the erroneous region 
     */
    public IndentInfo getStartSkipPosition() {
        return erroneousRegion.getStartSkip();
    }
    
    /**
     * returns the location of the first non-erroneous character
     */
    public int getEndSkipPosition() {
        return erroneousRegion.getEndSkip().getTokensSeen();
    }

    /**
     *  Returns error fragment including the left margin (needed for bridge-parsing)
     */
    public String getErrorFragment() {
        int tokIndexLine=getHistory().getTokensSeenStartLine(getStartSkipPosition().getTokensSeen());
        return getHistory().getFragment(tokIndexLine, getEndSkipPosition()-1);
    }
    
    public String getErrorFragmentPlusSeparator() {
        int tokIndexLine=erroneousRegion.getStartSkip().getTokensSeen()+erroneousRegion.getAdditionalTokens().length; //getHistory().getTokensSeenStartLine(getStartSkipPosition().getTokensSeen());
        return getHistory().getFragment(tokIndexLine, getEndSkipPosition()-1);
    }

    /**
     * Returns location where erroneous region starts, including left margin
     */
    public int getErrorFragmentStartPosition() {
        int tokIndexLine=getHistory().getTokensSeenStartLine(getStartSkipPosition().getTokensSeen());
        return tokIndexLine;
    }

    public ArrayList<IndentInfo> getSkippedLines() {        
        return getHistory().getLinesFromTo(erroneousRegion.getIndexHistoryStart(), getEndSkipPosition());
    }      

    /**
     * Selects erroneous region based on layout 
     */
    public boolean selectErroneousFragment() throws IOException { 
        newRegionSelector=new NewStructureSkipper(myParser);
        regionSelector.clear();
        regionSelector.setFailureIndex(getHistory().getIndexLastLine());
        newRegionSelector.setFailureIndex(getHistory().getIndexLastLine());
        errorDetectionLocation=getHistory().getIndexLastToken();
        hasFoundErroneousRegion=false;         
        //ArrayList<StructureSkipSuggestion> prevRegions=regionSelector.getPreviousSkipSuggestions();
        ArrayList<StructureSkipSuggestion> prevRegions=newRegionSelector.getPreviousSkipSuggestions();
        logRecoverInfo("PREVIOUS REGION");        
        if(trySetErroneousRegion(prevRegions)){
            ArrayList<StructureSkipSuggestion> decomposedRegions=newRegionSelector.getZoomOnPreviousSuggestions(erroneousRegion);
            boolean findSmallerPart= trySetErroneousRegion(decomposedRegions);
            if(findSmallerPart){
               // ArrayList<StructureSkipSuggestion> childRegions=regionSelector.getPickErroneousChild(erroneousRegion);
               // trySetErroneousRegion(childRegions);
            }
            return true;
        }
        //ArrayList<StructureSkipSuggestion> currentRegions=regionSelector.getCurrentSkipSuggestions();
        ArrayList<StructureSkipSuggestion> currentRegions=newRegionSelector.getCurrentSkipSuggestions();
        logRecoverInfo("CURRENT REGION");
        if(trySetErroneousRegion(currentRegions)){            
            return true;
        }
        logRecoverInfo("PRIOR REGIONS");
        ArrayList<StructureSkipSuggestion> priorRegions=newRegionSelector.getPriorSkipSuggestions();
        if(trySetErroneousRegion(priorRegions)){
            ArrayList<StructureSkipSuggestion> decomposedRegions=newRegionSelector.getZoomOnPreviousSuggestions(erroneousRegion);
            boolean findSmallerPart=trySetErroneousRegion(decomposedRegions);
            if(findSmallerPart){
                ArrayList<StructureSkipSuggestion> childRegions=newRegionSelector.getPickErroneousChild(erroneousRegion);
                trySetErroneousRegion(childRegions);
            }
            return true;
        }
        logRecoverInfo("FW-SIB REGIONS");
        ArrayList<StructureSkipSuggestion> siblingForWardRegions=newRegionSelector.getSibblingForwardSuggestions();
        if(trySetErroneousRegion(siblingForWardRegions)){            
            return true;
        }
        logRecoverInfo("BW-SIB REGIONS");
        ArrayList<StructureSkipSuggestion> siblingBackWardRegions=newRegionSelector.getSibblingBackwardSuggestions();
        if(trySetErroneousRegion(siblingBackWardRegions)){            
            return true;
        }
        logRecoverInfo("SURROUNDING-SIB REGIONS");
        //System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
        ArrayList<StructureSkipSuggestion> siblingSurroundingRegions=newRegionSelector.getSibblingSurroundingSuggestions();
        if(trySetErroneousRegion(siblingSurroundingRegions)){            
            return true;
        }
        logRecoverInfo("PARENT REGION");
        ArrayList<StructureSkipSuggestion> parentRegion=newRegionSelector.getParentSkipSuggestions();
        if(trySetErroneousRegion(parentRegion)){            
            return true;
        }        
        else {
            logRecoverInfo("PREFIX");
            erroneousRegion=regionSelector.getErroneousPrefix();
            logRecoverInfo(getHistory().getFragment(erroneousRegion));
            ArrayList<StructureSkipSuggestion> decomposedRegions=regionSelector.getZoomOnPreviousSuggestions(erroneousRegion);
            boolean findSmallerPart=trySetErroneousRegion(decomposedRegions);
            if(findSmallerPart){
                ArrayList<StructureSkipSuggestion> childRegions=regionSelector.getPickErroneousChild(erroneousRegion);
                trySetErroneousRegion(childRegions);
            }
            return findSmallerPart; //false;
        }
    }

    private boolean trySetErroneousRegion(ArrayList<StructureSkipSuggestion> regions) throws IOException {
        StructureSkipSuggestion aSkip=new StructureSkipSuggestion();
        int indexSkips=0;
        myParser.acceptingStack=null; 
        myParser.activeStacks.clear(); //undo success
        while (indexSkips < regions.size() && !successCriterion()) {
            aSkip = regions.get(indexSkips);            
            testRegion(aSkip);
            indexSkips++;            
        }
        hasFoundErroneousRegion=successCriterion();
        if(hasFoundErroneousRegion){
            erroneousRegion=aSkip;   
            logRecoverInfo("Erroneous region set ");
        }
        return hasFoundErroneousRegion;
    }

    private void testRegion(StructureSkipSuggestion aSkip) throws IOException {
       // System.out.println("MMMMMMMMMMM");
        //System.out.println(getInputFragment(aSkip));
        logRecoverInfoBlock(getInputFragment(aSkip));           
        IndentInfo endPos=aSkip.getEndSkip();
        getHistory().setTokenIndex(endPos.getTokensSeen());
        myParser.activeStacks.clear();
        myParser.acceptingStack=null;
        myParser.activeStacks.addAll(endPos.getStackNodes());        
        for (char aChar : aSkip.getAdditionalTokens()) {
            myParser.currentToken=aChar;
           // System.out.print((char)aChar);
            myParser.doParseStep();
        }
        if(aSkip.getAdditionalTokens().length>0){
            //System.out.println("$$ skip additional characters $$");
            aSkip.getStartSkip().fillStackNodes(myParser.activeStacks);
            aSkip.getStartSkip().setTokensSeen(aSkip.getStartSkip().getTokensSeen() + aSkip.getAdditionalTokens().length);
            aSkip.setAdditionalTokens(new char[0]);
        }
        int nrOfParsedLines=0;
        logRecoverInfo("CONTINUE PARSING: ");
        //System.out.println("CONTINUE PARSING: ");
        IndentationHandler indentHandler = new IndentationHandler();
        indentHandler.setInLeftMargin(false);
        while((myParser.activeStacks.size() > 0 && nrOfParsedLines<NR_OF_LINES_TILL_SUCCESS) || !getHistory().hasFinishedRecoverTokens()) {                       
            getHistory().readRecoverToken(myParser); 
            indentHandler.updateIndentation(myParser.currentToken);
           // System.out.print((char)myParser.currentToken);
            logRecoverInfo((char)myParser.currentToken);            
            myParser.doParseStep();
            if(getHistory().getTokenIndex()>errorDetectionLocation && indentHandler.lineMarginEnded())
                nrOfParsedLines++;
        }
        return;
    }

    public String getInputFragment(StructureSkipSuggestion aSkip) {
        return getHistory().getFragment(aSkip.getStartSkip().getTokensSeen(), aSkip.getEndSkip().getTokensSeen()-1);
    }

    private boolean successCriterion() {
        return myParser.activeStacks.size() > 0 || myParser.acceptingStack!=null;
    }
    
    private void logRecoverInfo(Object s) {
        if(useDebugMode){
            System.err.println(s);
        }
    } 
    
    private void logRecoverInfoBlock(Object s) {
        if(useDebugMode){
            System.err.println("------------------------");
            System.err.println("");
            System.err.println(s);            
            System.err.println("");
            System.err.println("------------------------");
        }
    } 

}
