package org.spoofax.jsglr.client;

import org.spoofax.jsglr.shared.ArrayDeque;
import org.spoofax.jsglr.shared.BadTokenException;
import org.spoofax.jsglr.shared.SGLRException;
import org.spoofax.jsglr.shared.TokenExpectedException;
import org.spoofax.jsglr.shared.Tools;

//TODO: keep recovered lines (Testcase: two separated errors)
public class RecoveryConnector {
    private SGLR mySGLR;
    private IRecoveryParser recoveryParser;
    private RegionRecovery skipRecovery;
    private boolean active;
    private boolean useBridgeParser;
    private IRecoveryResult bpResult;
    private boolean useFineGrained;
    
    
    public void setUseBridgeParser(boolean useBridgeParser) {
        this.useBridgeParser = useBridgeParser;
    }

    public RecoveryConnector(SGLR parser, IRecoveryParser recoveryParser){
        useFineGrained=true;
        active=false;
        mySGLR=parser;        
        skipRecovery = new RegionRecovery(mySGLR); 
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
    
    public void recover() {
        //long startSkip=System.currentTimeMillis();
        //System.err.print("***************** Recover");
        doRecoverSteps();
        //long durationSkip=System.currentTimeMillis()-startSkip;
        //System.err.print(" Recovertime: "+durationSkip);
    }

    private void doRecoverSteps() {
        active=true;
        boolean skipSucceeded = skipRecovery.selectErroneousFragment(); //decides whether whitespace parse makes sense
        mySGLR.acceptingStack=null;
        long startSkip=System.currentTimeMillis();
        String errorFragment = skipRecovery.getErrorFragmentWithLeftMargin();
        long durationSkip=System.currentTimeMillis()-startSkip;
        Tools.debug("Skip time: "+ durationSkip);
        //System.err.print("Skip time: "+ durationSkip+ "  ");
        Tools.debug(errorFragment);
        //System.err.print(errorFragment);
        mySGLR.activeStacks.clear();
        //BRIDGE REPAIR
        if(useBridgeParser){            
            boolean succeeded = tryBridgeRepair(errorFragment);
            if(succeeded){
                Tools.debug("Bridge Repair Succeeded");
                //System.err.print("************** BP-Succeeded");
                return;
            }
            Tools.debug("Bridge Repair Failed");
        }
        //System.out.println("USE FG? "+useFineGrained);
        //FINEGRAINED REPAIR 
        if(useFineGrained){            
            long startFineGrained=System.currentTimeMillis();        
            tryFineGrainedRepair();       
            long durationFG=System.currentTimeMillis()-startFineGrained;
            Tools.debug("Fine-Grained time: "+ durationFG);
        }
        //System.out.println("HISTORY AFTER FINE-GRAINED");
        //getHistory().logHistory();
        //System.err.print("Fine-Grained time: "+ durationFG);
        //Tools.debug("Disambiguations: " +  RecoverDisambiguator.testCount);
        if(recoverySucceeded()){
            Tools.debug("Fine-Grained Repair Succeeded");
            //System.err.print("**************** FG-succeeded");
            ArrayDeque<Frame> fgStacks=new ArrayDeque<Frame>();
            fgStacks.addAll(mySGLR.activeStacks);
            if (skipSucceeded) { 
                
                boolean whiteSpaceRecovery=parseErrorFragmentAsWhiteSpace(false);
                if(whiteSpaceRecovery)
                    whiteSpaceRecovery=parseRemainingTokens(false);
                if(whiteSpaceRecovery){
                    for (Frame frame : mySGLR.activeStacks) {
                        for (Link l : frame.getAllLinks()) {
                            l.recoverCount = 5;
                        }
                    }                    
                }
               // ArrayDeque<Frame> wsStacks=mySGLR.activeStacks;
                
                //mySGLR.activeStacks.clear();
                //whiteSpaceParse();
                //whiteSpaceParse(errorFragment); 
                for (Frame frame : fgStacks) {
                    mySGLR.addStack(frame);
                } 
                /*
                for (Frame frame : wsStacks) {
                    mySGLR.addStack(frame);
                } */  
            }
            //System.out.println("HISTORY AFTER FG + COLLECTING WS-STACKS");
            //getHistory().logHistory();
            return;
        }
        Tools.debug("FineGrained Repair Failed");
        //WHITESPACE REPAIR
        if (skipSucceeded) { 
            getHistory().deleteLinesFrom(skipRecovery.getStartIndexErrorFragment());//TODO: integrate with FG and BP
            getHistory().resetRecoveryIndentHandler(skipRecovery.getStartLineErrorFragment().getIndentValue());
            boolean whiteSpaceRecovery=parseErrorFragmentAsWhiteSpace(false);//true
            //System.err.println("MMM");
            //getHistory().logHistory();
            if(whiteSpaceRecovery){
                parseRemainingTokens(true);
                //System.err.println("MMM");
                //getHistory().logHistory();
            }
            //whiteSpaceParse();
            //whiteSpaceParse(errorFragment); 
            if(recoverySucceeded()){
                Tools.debug("WhiteSpace Repair Succeeded");
                //System.err.print("************* WS-succeeded");
            }
            else{
                Tools.debug("WhiteSpace Repair unexpectly fails");
                recover();
                //System.err.print("*************** WS-Fails unexpected");
            }/*
            if(!parseRemainingTokens())
                recover();*/
        }
        //FORCE PREFIX ACCEPT
        /*else {            
            EofRecovery eofR = new EofRecovery(mySGLR);
            eofR.enforceAccept(getHistory().getBigReducePoint().getStackNodes());
            if(recoverySucceeded()){
                Tools.debug("Enforcing Accepting Stack - Succeeded");
                //System.err.print("******************* AS-succeeded");
            }
            else{
                Tools.debug("Enforcing Accepting Stack - Failed"); 
              //System.err.print("******************* AS-Failed");
            }
        }*/
        active = false;
    }
    
    private boolean recoverySucceeded() {
        boolean hasSucceeded = (mySGLR.activeStacks.size()>0 || mySGLR.acceptingStack!=null);
        /*
        if(hasSucceeded){           
            ArrayList<IndentInfo> recoverNewLinePoints = new ArrayList<IndentInfo>();            
            IndentInfo currentStatus = new IndentInfo(mySGLR.lineNumber, getHistory().getTokenIndex(), mySGLR.getIndentHandler().getIndentValue());
            recoverNewLinePoints.add(currentStatus);
            getHistory().addRecoverLines(recoverNewLinePoints);
        }
        */
        return hasSucceeded;
    }

    private void whiteSpaceParse() {
        String errorFragment=skipRecovery.getErrorFragment();
        mySGLR.activeStacks.addAll(skipRecovery.getStartLineErrorFragment().getStackNodes());            
        tryParsing(errorFragment, true);
        parseRemainingTokens(true);
    }

    private void tryFineGrainedRepair() {
        FineGrainedOnRegion fgRepair=new FineGrainedOnRegion(mySGLR);        
        fgRepair.setRegionInfo(skipRecovery.getErroneousRegion(), skipRecovery.getAcceptPosition());
        fgRepair.recover();
        fgRepair.parseRemainingTokens();
        /*if(!fgRepair.parseRemainingTokens())
            recover();*/
    }
    /*
    private void tryFineGrainedRepair() throws IOException {
        FineGrainedRepair fineGrained=new FineGrainedRepair(mySGLR);   
        fineGrained.setBpSuggestions(getBPSuggestions());
        fineGrained.findRecoverBranch(skipRecovery.getSkippedLines(), skipRecovery.getEndPositionErrorFragment());        
    }*/

    private boolean tryBridgeRepair(String errorFragment) {
        String repairedFragment = repairBridges(errorFragment);
        mySGLR.activeStacks.addAll(skipRecovery.getStartLineErrorFragment().getStackNodes());   
        tryParsing(repairedFragment, false);      
        parseRemainingTokens(true);
        return recoverySucceeded();
    }

    private String repairBridges(String errorFragment) {
        try {            
            bpResult = null;
            bpResult = recoveryParser.recover(errorFragment);
            return bpResult.getResult();
        } catch (TokenExpectedException e) {
            e.printStackTrace();
        } catch (BadTokenException e) {
            e.printStackTrace();
        } catch (SGLRException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "/*" + errorFragment + "*/";
    }
    
    private void tryParsing(String fragment, boolean asLayout) {
        // Skip any leading whitespace, since we already parsed up to that point
        int indexFragment = findFirstNonLayoutToken(fragment);      
        //System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
        while(indexFragment<fragment.length() && mySGLR.activeStacks.size()>0) {                        
            mySGLR.currentToken=fragment.charAt(indexFragment);
            //System.out.print((char)mySGLR.currentToken);
            //Tools.debug((char)mySGLR.currentToken);
            indexFragment++;
            
            if(!asLayout)
                mySGLR.doParseStep();
            else
                parseAsLayout();
        }       
    }
    
    public boolean parseErrorFragmentAsWhiteSpace(boolean keepLines) {
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
    
    public boolean parseRemainingTokens(boolean keepHistory) {
        //System.out.println("------------- REMAINING CHARACTERS --------------- ");
        getHistory().setTokenIndex(skipRecovery.getEndPositionErrorFragment());        
        while(!getHistory().hasFinishedRecoverTokens() && mySGLR.activeStacks.size()>0 && mySGLR.acceptingStack==null){        
            getHistory().readRecoverToken(mySGLR, keepHistory);
            //System.out.print((char)mySGLR.currentToken);
            mySGLR.doParseStep();            
        }  
        return mySGLR.activeStacks.size()>0 || mySGLR.acceptingStack!=null;
    }

    
    
    private void parseAsLayout() {
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

    public boolean isActive() {        
        return active;
    }

    public void setUseFineGrained(boolean useFG) {
        useFineGrained=useFG;        
    }

}
