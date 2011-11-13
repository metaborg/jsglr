package org.spoofax.jsglr.client;

import java.util.ArrayList;

import org.spoofax.jsglr.client.SGLR;

/**
 * @author maartje
 *
 */
public class FineGrainedRecovery {
	
	// safety guard: > 1000 recover branches seem to cause performance problems
	private static final int MAX_NUMBER_OF_RECOVER_BRANCHES = 1000;  	
	//minimum number of characters that must be parsed error-free before recovery is accepted 
	//(safety guard against false accepts after some empty lines)
	private static final int ACCEPT_DISTANCE_CHARACTERS = 100; 

	/*
	 * Settings that control the heuristics applied to find a recover branch
	 */
	private final FineGrainedSetting settings;
	
	/*
	 * Properties of the current error
	 */
	private int exploredRegionStartOffset; //recoveries at the left of the region start are not explored
	private int exploredRegionEndOffset; //recoveries at the right of the region end are not explored
	private int lineIndexRecovery; //line index where recovery search starts (parse failure line, region end line, cursor line(?!))
	private int failureOffset; //location where parser failed
	private long recoverStartTime; //start time

	private void checkAssertionsForErrorProperties() {
    	assert(exploredRegionStartOffset < exploredRegionEndOffset);
    	assert(exploredRegionStartOffset < failureOffset);
		assert(lineIndexRecovery <= getHistory().getIndexLastLine());
		assert(getHistory().getLine(lineIndexRecovery).getStackNodes().size() > 0);
		assert(getTokensSeenAtLine(lineIndexRecovery) <= failureOffset);
		assert(failureOffset >= getTokensSeenAtLine(lineIndexRecovery));
	}

	private void checkAssertionsUnexploredBranches(ArrayList<RecoverNode> unexplored_branches, int bwIndex) {
		for (RecoverNode recoverNode : unexplored_branches) {
			assert(exploredRegionStartOffset <= recoverNode.tokensSeen);
			assert(getTokensSeenAtLine(bwIndex) <= recoverNode.tokensSeen);
			assert(recoverNode.tokensSeen <= exploredRegionEndOffset);
		}
	}

	/*
	 * Parser instance
	 */
    private final SGLR mySGLR;
    
	private ParserHistory getHistory() {
		return mySGLR.getHistory();
	}
	
	/**
	 * Correcting recovery technique for SGLR parser
	 * Applies recover productions (which simulate token insertions and deletions) 
	 * using a expanding search space heuristic over an untrusted source fragment  
	 * @param SGLR parser
	 */
	public FineGrainedRecovery(SGLR parser) {
		this(parser, FineGrainedSetting.createDefaultSetting());
	}

	/**
	 * Correcting recovery technique for SGLR.
	 * @param SGLR parser
	 * @param Setting that determines the expanding search space heuristics applied to find a suitable recovery
	 */
	public FineGrainedRecovery(SGLR parser, FineGrainedSetting fgSettings) {
		this.settings = fgSettings;		
		this.mySGLR = parser;
	}
	
    /**
     * Constructs a recover branch for SGLR
     * @param failureOffset location where the parser fails
     * @param recoverIndex line index (from parser history) where recover search starts
     * (typically: region-end index, failure index, or: index of cursor line if nearby and at the left of failure index)
     * @param regionStartOffset restricts the search space to the left
     * @param regionEndOffset restricts the search space to the right
     * @return true iff suitable recover branch is constructed
     */
    public boolean recover(int failureOffset, int recoverIndex, int regionStartOffset, int regionEndOffset){
    	this.exploredRegionStartOffset = regionStartOffset; 
    	this.exploredRegionEndOffset = regionEndOffset;
    	return finegrainedRecover(failureOffset, recoverIndex);
    }

    /**
     * Constructs a recover branch for SGLR
     * @param failureOffset location where the parser fails
     * @param recoverIndex line index (from parser history) where recover search starts
     * (typically: failure index, or: index of cursor line if nearby and at the left of failure index)
     * @return true iff suitable recover branch is constructed
     */
	public boolean recover(int failureOffset, int recoverIndex){
    	this.exploredRegionStartOffset = -1;
    	this.exploredRegionEndOffset = Integer.MAX_VALUE;
    	return finegrainedRecover(failureOffset, recoverIndex);
    }

	private boolean finegrainedRecover(int failureOffset, int recoverIndex) {
		this.failureOffset = failureOffset;
    	this.lineIndexRecovery = recoverIndex; 
    	this.recoverStartTime = System.currentTimeMillis();
    	checkAssertionsForErrorProperties();
    	return recoverFrom(0, new ArrayList<RecoverNode>());
	}

	private boolean recoverFrom(int loopIndex, ArrayList<RecoverNode> unexplored_branches) {
		int backwardIndexPrev = Math.max(0,lineIndexRecovery - (int)(settings.getBackwardFactor() * (loopIndex - 1)));
		int backwardIndex = Math.max(0,lineIndexRecovery - (int)(settings.getBackwardFactor() * loopIndex));
		int forwardLinesMax = Math.min(settings.getForwardDistanceLines(), (int)(settings.getForwardFactor() * loopIndex));
		assert(0 <= backwardIndex);
		assert(backwardIndex <= lineIndexRecovery);
		assert(backwardIndex <= backwardIndexPrev);
		assert(forwardLinesMax >= 0);		
		assert(forwardLinesMax <= settings.getForwardDistanceLines());		
		unexplored_branches.addAll(getBackwardRecoverCandidates(backwardIndex, backwardIndexPrev));
		checkAssertionsUnexploredBranches(unexplored_branches, backwardIndex);
		resetSGLR(backwardIndex, false);
		ArrayList<RecoverNode> newCandidates = recoverParse(forwardLinesMax, this.exploredRegionEndOffset, unexplored_branches);
		if(!acceptParse()){
			if(timelimitExpired()){
				return false;
			}			
			if(continueBacktracking(backwardIndex)){
				if(newCandidates.size() > MAX_NUMBER_OF_RECOVER_BRANCHES)
					newCandidates = new ArrayList<RecoverNode>(); //too much branches causes performance problems
				recoverFrom(loopIndex + 1, newCandidates);
			}
			else if (continueSingleTokenBacktracking(backwardIndex)){
				recoverFrom(loopIndex + 1, new ArrayList<RecoverNode>());
			}
			else{
				return false;
			}
		}
		return true;
	}

	/**
	 * Collects recover branches between current parser location and fwTokensSeenMax. 
	 * Either from a previously unexplored line,
	 * or from a set of unexplored branches.
	 * Stops in case more then fwLineMax newlines are parsed after the line where recovery started
	 * @param fwLineMax restricts search space to the right: max number of lines that may be explored after recover line
	 * @param fwTokensSeenMax restricts search space to the right, exploration within erroneous region
	 * @param candidates candidate branches that are explored
	 * @return new candidate branches
	 */
	private ArrayList<RecoverNode> recoverParse(int fwLineMax, int fwTokensSeenMax, ArrayList<RecoverNode> candidates) {
		// Backtracking is not combined with exploration because that creates
		// duplicates
		assert (mySGLR.activeStacks.size() == 0 || candidates.size() == 0);

		ArrayList<RecoverNode> newCandidates = new ArrayList<RecoverNode>();
		int curTokIndex;
		int exploredLinesForward = 0;
		do {
			curTokIndex = getHistory().getTokenIndex();
			addCurrentCandidates(candidates, curTokIndex);
			getHistory().readRecoverToken(mySGLR, false);
			if (mySGLR.getCurrentToken() == '\n' && curTokIndex > getTokensSeenAtLine(lineIndexRecovery)){
				exploredLinesForward++;
			}
			// System.out.print((char)mySGLR.currentToken);
			mySGLR.setFineGrainedOnRegion(exploredRegionStartOffset <= curTokIndex);
			mySGLR.doParseStep();
			newCandidates.addAll(collectNewRecoverCandidates(curTokIndex));
			mySGLR.getRecoverStacks().clear();
		} while (
				   exploredLinesForward <= fwLineMax
				&& exploredLinesForward <= settings.getForwardDistanceLines()
				&& getHistory().getTokenIndex() <= exploredRegionEndOffset
				&& getHistory().getTokenIndex() <= fwTokensSeenMax
				&& mySGLR.acceptingStack == null
				&& mySGLR.getCurrentToken() != SGLR.EOF);
		mySGLR.setFineGrainedOnRegion(false);
		return newCandidates;
	}

    private void addCurrentCandidates(ArrayList<RecoverNode> candidates, int tokenPosition) {
        for (RecoverNode recoverNode : candidates) {
            if(tokenPosition==recoverNode.tokensSeen){
            	Frame st =mySGLR.findStack(mySGLR.activeStacks, recoverNode.recoverStack.state);
                if(st != null) {
                	for (Link ln : recoverNode.recoverStack.getAllLinks()) {
                		st.addLink(ln);
					}                	
                }
                else
                	mySGLR.addStack(recoverNode.recoverStack);
            }
        }
    }

	private void resetSGLR(int btIndex, boolean keepStacks) {
    	mySGLR.activeStacks.clear();
		if(keepStacks){
	        mySGLR.activeStacks.addAll(getHistory().getLine(btIndex).getStackNodes());
		}
        getHistory().setTokenIndex(getHistory().getLine(btIndex).getTokensSeen());
	}

    private ArrayList<RecoverNode> collectNewRecoverCandidates(int tokenIndex) {
    	assert(tokenIndex >= exploredRegionStartOffset || mySGLR.getRecoverStacks().isEmpty());
    	assert(tokenIndex <= exploredRegionEndOffset || mySGLR.getRecoverStacks().isEmpty());
        ArrayList<RecoverNode> results=new ArrayList<RecoverNode>();
        for (Frame recoverStack : mySGLR.getRecoverStacks()) {
            RecoverNode rn = new RecoverNode(recoverStack, tokenIndex);
            results.add(rn);
        }
        return results;
    }

	private boolean timelimitExpired() {
		return System.currentTimeMillis() - this.recoverStartTime > settings.getTimeLimit();
	}

	private ArrayList<RecoverNode> getBackwardRecoverCandidates(int bwIndex, int bwIndexPrev) {
		assert(bwIndex <= bwIndexPrev);
		if (bwIndex == bwIndexPrev || preceedsErroneousRegion(bwIndexPrev)){
			return new ArrayList<RecoverNode>();
		}
		resetSGLR(bwIndex, true);
		int fwTokensSeenMax = Integer.MAX_VALUE;
		if(bwIndexPrev <= getHistory().getIndexLastLine()){
			fwTokensSeenMax = getTokensSeenAtLine(bwIndexPrev);
		}
		ArrayList<RecoverNode> newBranches = recoverParse(0, fwTokensSeenMax, new ArrayList<RecoverNode>());
		return newBranches;
	}

	private boolean preceedsErroneousRegion(int lineIndex) {
		return 
			lineIndex <= getHistory().getIndexLastLine() &&
			getTokensSeenAtLine(lineIndex) <= exploredRegionStartOffset;
	}

	private int getTokensSeenAtLine(int lineIndex) {
		return getHistory().getLine(lineIndex).getTokensSeen();
	}

	private boolean continueBacktracking(int backwardIndex) {
		assert(backwardIndex <= lineIndexRecovery);
		return lineIndexRecovery - backwardIndex < settings.getBacktrackDistanceLines();
	}

	private boolean continueSingleTokenBacktracking(int backwardIndex) {
		return lineIndexRecovery - backwardIndex < settings.getBacktrackDistanceLinesSingleToken();
	}
	
	/**
	 * recovery is accepted in case a minimal number of characters and lines
	 * are parsed error free after failure location (and last recovery location).
	 * Or in case accepting stack is constructed
	 * @return true iff suitable recover stack constructed
	 */
	private boolean acceptParse() {
		String parsedFragment = "";
		while (mySGLR.activeStacks.size() > 0 && !acceptRecovery(parsedFragment)) {
			getHistory().readRecoverToken(mySGLR, false);
			if(getHistory().getTokenIndex() > failureOffset){
				parsedFragment += ((char)mySGLR.getCurrentToken());
			}
			// System.out.print((char)mySGLR.currentToken);
			mySGLR.doParseStep();
		}
		return acceptRecovery(parsedFragment);
	}

	/**
	 * Recovery is accepted if:
	 * - Parser has accepting stack at EOF
	 * - sufficiently large fragment is parsed without a recover application 
	 */
	private boolean acceptRecovery(String parsedFragmentSinceLastRecovery){
		if(mySGLR.acceptingStack != null)
			return true;
		return 
			mySGLR.activeStacks.size() > 0
		&&	parsedFragmentSinceLastRecovery.split("\n").length > settings.getAcceptDistanceLines()
		&&	parsedFragmentSinceLastRecovery.length() > ACCEPT_DISTANCE_CHARACTERS
		&&  getHistory().getTokenIndex() > exploredRegionEndOffset;
	}
}
