package org.spoofax.jsglr;

import java.io.IOException;

import org.spoofax.ArrayDeque;

//TODO: keep recovered lines (Testcase: two separated errors)
public class RecoveryConnector {
    private SGLR mySGLR;
    private IRecoveryParser recoveryParser;
    private RegionRecovery skipRecovery;
    private boolean useBridgeParser;
    private boolean useFineGrained;
    
    
    public void setUseBridgeParser(boolean useBridgeParser) {
        this.useBridgeParser = useBridgeParser;
    }

    public RecoveryConnector(SGLR parser, IRecoveryParser recoveryParser){
        mySGLR=parser;        
        skipRecovery = new RegionRecovery(mySGLR); 
        useFineGrained=true;
        if(recoveryParser!=null){
            this.recoveryParser = recoveryParser;
            useBridgeParser=true;
        }
        else
            useBridgeParser=false;
        
    }    
    
    private ParserHistory getHistory() {
        return mySGLR.getHistory();
    }

    public void recover() throws IOException {
        boolean skipSucceeded = skipRecovery.selectErroneousFragment(); //decides whether whitespace parse makes sense
        mySGLR.acceptingStack=null;
        mySGLR.activeStacks.clear();
        //BRIDGE REPAIR
        if(useBridgeParser){       
            String errorFragment = skipRecovery.getErrorFragmentWithLeftMargin();
            boolean succeeded = tryBridgeRepair(errorFragment);
            if(succeeded){
                return;
            }
        }
        //FINEGRAINED REPAIR 
        if(useFineGrained){            
            if(tryFineGrainedRepair()){ //FG succeeded  
                addSkipOption(skipSucceeded);
                return;
            }
        }
        //WHITESPACE REPAIR
        if (skipSucceeded) { 
            getHistory().deleteLinesFrom(skipRecovery.getStartIndexErrorFragment());//TODO: integrate with FG and BP
            getHistory().resetRecoveryIndentHandler(skipRecovery.getStartLineErrorFragment().getIndentValue());
            parseErrorFragmentAsWhiteSpace(false);
            parseRemainingTokens(true);
        }
    }

    private void addSkipOption(boolean skipSucceeded)
            throws IOException {
        ArrayDeque<Frame> fgStacks=new ArrayDeque<Frame>();
        fgStacks.addAll(mySGLR.activeStacks);
        if(skipSucceeded && parseErrorFragmentAsWhiteSpace(false) && parseRemainingTokens(false)){
            for (Frame frame : mySGLR.activeStacks) {
                for (Link l : frame.getAllLinks()) {
                    l.recoverCount = 5;
                }
            }                        
            for (Frame frame : fgStacks) {
                mySGLR.addStack(frame);
            } 
        }
    }
    
    private boolean recoverySucceeded() {
        return (mySGLR.activeStacks.size()>0 || mySGLR.acceptingStack!=null);
    }

    private boolean tryFineGrainedRepair() throws IOException {
        FineGrainedOnRegion fgRepair=new FineGrainedOnRegion(mySGLR);        
        fgRepair.setRegionInfo(skipRecovery.getErroneousRegion(), skipRecovery.getAcceptPosition());
        fgRepair.recover();
        fgRepair.parseRemainingTokens();
        return recoverySucceeded();
    }

    private boolean tryBridgeRepair(String errorFragment) throws IOException {
        String repairedFragment = repairBridges(errorFragment);
        mySGLR.activeStacks.addAll(skipRecovery.getStartLineErrorFragment().getStackNodes());   
        tryParsing(repairedFragment, false);      
        return parseRemainingTokens(true);
    }

    private String repairBridges(String errorFragment) {        
        try {            
            IRecoveryResult bpResult = null;
            bpResult = recoveryParser.recover(errorFragment);
            return bpResult.getResult();
        } catch (TokenExpectedException e) {
            e.printStackTrace();
        } catch (BadTokenException e) {
            e.printStackTrace();
        } catch (SGLRException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  errorFragment;
    }
    
    private void tryParsing(String fragment, boolean asLayout) throws IOException{
        // Skip any leading whitespace, since we already parsed up to that point
        int indexFragment = findFirstNonLayoutToken(fragment);
        while(indexFragment<fragment.length() && mySGLR.activeStacks.size()>0) {                        
            mySGLR.currentToken=fragment.charAt(indexFragment);
            indexFragment++;
            if(!asLayout)
                mySGLR.doParseStep();
            else
                parseAsLayout();
        }       
    }
    
    public boolean parseErrorFragmentAsWhiteSpace(boolean keepLines) throws IOException{
        //System.out.println("---------- Start WhiteSpace Parsing ----------");
        mySGLR.activeStacks.clear();
        mySGLR.activeStacks.addAll(skipRecovery.getStartLineErrorFragment().getStackNodes());
        getHistory().setTokenIndex(skipRecovery.getStartPositionErrorFragment());
        while((getHistory().getTokenIndex()<skipRecovery.getEndPositionErrorFragment()) && mySGLR.activeStacks.size()>0 && mySGLR.acceptingStack==null){        
            getHistory().readRecoverToken(mySGLR, keepLines);
            //System.out.print((char)mySGLR.currentToken);
            parseAsLayout();           
        }
        //System.out.println("----------- End WhiteSpace Parsing ---------");
        return recoverySucceeded();
    }
    
    public boolean parseRemainingTokens(boolean keepHistory) throws IOException{
        //System.out.println("------------- REMAINING CHARACTERS --------------- ");
        getHistory().setTokenIndex(skipRecovery.getEndPositionErrorFragment());        
        while(!getHistory().hasFinishedRecoverTokens() && mySGLR.activeStacks.size()>0 && mySGLR.acceptingStack==null){        
            getHistory().readRecoverToken(mySGLR, keepHistory);
            //System.out.print((char)mySGLR.currentToken);
            mySGLR.doParseStep();            
        }  
        return recoverySucceeded();
    }

    
    
    private void parseAsLayout() throws IOException {
        if(isLayoutCharacter((char)mySGLR.currentToken) || mySGLR.currentToken==SGLR.EOF)
            mySGLR.doParseStep();
        else{
            mySGLR.currentToken=' ';
            mySGLR.doParseStep();            
        }
    }
    
    public static boolean isLayoutCharacter(char aChar) {
        // TODO: Move this to the parse table class; only it truly can now layout characters
        return aChar==' ' || aChar == '\t' || aChar=='\n';
    }

    private int findFirstNonLayoutToken(String repairedFragment) {
        int indexFragment=0;
        while(indexFragment<repairedFragment.length()-1 && isLayoutCharacter(repairedFragment.charAt(indexFragment)))
            indexFragment++;
        return indexFragment;
    }

    public void setUseFineGrained(boolean useFG) {
        useFineGrained=useFG;        
    }
    
    /*
    private Map<Integer, char[]> getBPSuggestions(){
        Map<Integer, char[]> bpSuggestions = getBridges();
        int startPos = skipRecovery.getStartPositionErrorFragment_InclLeftMargin();
        
        Map<Integer, char[]> bpSuggestAbsolute = new HashMap<Integer, char[]>();
        for (Integer aKey : bpSuggestions.keySet()) {
            Integer newKey=new Integer(startPos+aKey.intValue());
            char[] newValue=bpSuggestions.get(aKey);
            bpSuggestAbsolute.put(newKey, newValue);
        }
        return bpSuggestAbsolute;
    }

    private Map<Integer, char[]> getBridges() {
        IRecoveryResult bpResult;
        if (bpResult != null) {
            return bpResult.getSuggestions();
        }
        return new HashMap<Integer, char[]>();
    }
    */

}
