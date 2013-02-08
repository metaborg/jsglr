package org.spoofax.jsglr.client;

/*
 * Settings that control the heuristics applied to find a recover branch
 */
public class FineGrainedSetting {

	private int acceptDistanceLines; //number of lines that must be parsed error-free before recovery is accepted
	private int backtrackDistanceLines; //maximum number of (non-empty) lines that is fully backtracked
	private int forwardDistanceLines; //maximum number of (non-empty) lines that is explored at the right context
	private int backtrackDistanceLinesSingleToken; //maximum number of (non-empty) lines that is explored for single token recoveries
	private double backwardFactor; //increase explored left context with x lines at each loop 
	private double forwardFactor; //increase explored right context with x lines at each loop (for example: 0.5 then extend one line after two loops)
	private int maxNumberOfRecoverApplicationsLocal; //branches with more then x recoveries after backtrack offset are cut off in FG mode
	
	private int endOffsetFragment; 	//recovery must stop here and return the constructed recover branches
									//setting for subfragment parsing


	private int timeLimit; //finegrained is stopped in case time limit expired
	
	public int getEndOffsetFragment() {
		return endOffsetFragment;
	}

	public FineGrainedSetting setEndOffsetFragment(int endOffsetFragment) {
		this.endOffsetFragment = endOffsetFragment;
		return this;
	}

	public int getTimeLimit() {
		return timeLimit;
	}

	public FineGrainedSetting setTimeLimit(int timeLimit) {
		this.timeLimit = timeLimit;
		return this;
	}

	public int getAcceptDistanceLines() {
		return acceptDistanceLines;
	}

	public FineGrainedSetting setAcceptDistanceLines(int acceptDistanceLines) {
		this.acceptDistanceLines = acceptDistanceLines;
		return this;
	}

	public int getForwardDistanceLines() {
		return forwardDistanceLines;
	}

	public FineGrainedSetting setForwardDistanceLines(int forwardDistanceLines) {
		this.forwardDistanceLines = forwardDistanceLines;
		return this;
	}
	public int getBacktrackDistanceLines() {
		return backtrackDistanceLines;
	}

	public FineGrainedSetting setBacktrackDistanceLines(int backtrackDistanceLines) {
		this.backtrackDistanceLines = backtrackDistanceLines;
		return this;
	}

	public int getBacktrackDistanceLinesSingleToken() {
		return backtrackDistanceLinesSingleToken;
	}

	public FineGrainedSetting setBacktrackDistanceLinesSingleToken(
			int backtrackDistanceLinesSingleToken) {
		this.backtrackDistanceLinesSingleToken = backtrackDistanceLinesSingleToken;
		return this;
	}

	public double getBackwardFactor() {
		return backwardFactor;
	}

	public FineGrainedSetting setBackwardFactor(double backwardFactor) {
		this.backwardFactor = backwardFactor;
		return this;
	}

	public double getForwardFactor() {
		return forwardFactor;
	}

	public FineGrainedSetting setForwardFactor(double forwardFactor) {
		this.forwardFactor = forwardFactor;
		return this;
	}

	public int getMaxNumberOfRecoverApplicationsLocal() {
		return maxNumberOfRecoverApplicationsLocal;
	}

	public FineGrainedSetting setMaxNumberOfRecoverApplicationsLocal(
			int maxNumberOfRecoverApplicationsLocal) {
		this.maxNumberOfRecoverApplicationsLocal = maxNumberOfRecoverApplicationsLocal;
		return this;
	}

	private FineGrainedSetting() {
		this.setTimeLimit(1000);
		this.setAcceptDistanceLines(5);
		this.setBacktrackDistanceLines(8);
		this.setBacktrackDistanceLinesSingleToken(80);
		this.setBackwardFactor(1);
		this.setForwardDistanceLines(8);
		this.setForwardFactor(0.5);
		this.setMaxNumberOfRecoverApplicationsLocal(5);		
		this.endOffsetFragment = Integer.MAX_VALUE;
	}
	
	/**
	 * Setting that is fine tuned for interactive editing.
	 * Both the left- and the right- context of the failure location are explored
	 * using an expanding search space.
	 * Recovery fails or succeeds within 1 second
	 * @return Standard setting for fine grained recovery in an interactive environment
	 */
	public static FineGrainedSetting createDefaultSetting(){
		FineGrainedSetting fgSetting = new FineGrainedSetting()
			.setTimeLimit(1000)
			.setAcceptDistanceLines(5)
			.setBacktrackDistanceLines(10)
			.setBacktrackDistanceLinesSingleToken(15)
			.setBackwardFactor(1)
			.setForwardDistanceLines(5)
			.setForwardFactor(0.4)
			.setMaxNumberOfRecoverApplicationsLocal(5);		
		fgSetting.checkAssertionsForSettings();
		return fgSetting;
	}

	/**
	 * Setting is used for error analysis to find out how well
	 * a local approach performs, only modifications on a single line are explored. 
	 * Search heuristic: try all recover-count = 1, 2, 3, ... branches on the failure (or other) line,
	 * @return Setting that locally searches for a recover branch
	 */
	public static FineGrainedSetting createCursorLineSetting(){
		FineGrainedSetting fgSetting = new FineGrainedSetting()
			.setTimeLimit(250)
			.setAcceptDistanceLines(5)
			.setBacktrackDistanceLines(1)
			.setBacktrackDistanceLinesSingleToken(1)
			.setBackwardFactor(1)
			.setForwardDistanceLines(1)
			.setForwardFactor(1)
			.setMaxNumberOfRecoverApplicationsLocal(2);
		fgSetting.checkAssertionsForSettings();
		return fgSetting;
	}

	/**
	 * Setting is used for error analysis to find out how many files
	 * can be recovered with a single token insertion/deletion/replacement 
	 * Search heuristic: try all recover-count = 1 branches in the left context
	 * @return Setting that globally searches for a single token recovery
	 */
	public static FineGrainedSetting createSingleTokenSetting(){
		FineGrainedSetting fgSetting = new FineGrainedSetting()
			.setTimeLimit(2000)
			.setAcceptDistanceLines(5)
			.setBacktrackDistanceLines(0)
			.setBacktrackDistanceLinesSingleToken(25)
			.setBackwardFactor(2)
			.setForwardDistanceLines(0)
			.setForwardFactor(0)
			.setMaxNumberOfRecoverApplicationsLocal(1);
		fgSetting.checkAssertionsForSettings();
		return fgSetting;
	}

	/**
	 * Setting is used for error analysis, to find out
	 * how many recover actions are required to recover from an error.
	 * Search heuristic: regionally try all recover-count = 1 branches,
	 * then continue with recover-count = 2, and so on. 
	 * @return Setting that globally searches for a recover branch 
	 * by modifying the left and right context
	 */
	public static FineGrainedSetting createMultipleTokensSetting(){
		FineGrainedSetting fgSetting = new FineGrainedSetting()
			.setTimeLimit(2000)
			.setAcceptDistanceLines(5)
			.setBacktrackDistanceLines(20)
			.setBacktrackDistanceLinesSingleToken(20)
			.setBackwardFactor(4)
			.setForwardDistanceLines(20)
			.setForwardFactor(4)
			.setMaxNumberOfRecoverApplicationsLocal(8);
		fgSetting.checkAssertionsForSettings();
		return fgSetting;
	}

	/**
	 * Setting is used for error analysis to find out how well
	 * a local approach performs, only modifications on a single line are explored. 
	 * Search heuristic: try all recover-count = 1, 2, 3, ... branches on the failure (or other) line,
	 * @return Setting that locally searches for a recover branch
	 */
	public static FineGrainedSetting createLocalContextSetting(){
		FineGrainedSetting fgSetting = new FineGrainedSetting()
			.setTimeLimit(2500)
			.setAcceptDistanceLines(5)
			.setBacktrackDistanceLines(0)
			.setBacktrackDistanceLinesSingleToken(0)
			.setBackwardFactor(0)
			.setForwardDistanceLines(0)
			.setForwardFactor(0)
			.setMaxNumberOfRecoverApplicationsLocal(10);
		fgSetting.checkAssertionsForSettings();
		return fgSetting;
	}

	/**
	 * Setting is used for error analysis to find out how 
	 * well a left context approach performs, only modifications at the (inclusive) 
	 * left of the error location are explored 
	 * Search heuristic: find recover branches at the left of the failure location using an expanding search space
	 * @return Setting that searches for a recover branch by modifying the left context
	 */
	public static FineGrainedSetting createLeftContextSetting(){
		FineGrainedSetting fgSetting = new FineGrainedSetting()
			.setTimeLimit(2500)
			.setAcceptDistanceLines(5)
			.setBacktrackDistanceLines(20)
			.setBacktrackDistanceLinesSingleToken(80)
			.setBackwardFactor(1)
			.setForwardDistanceLines(0)
			.setForwardFactor(0)
			.setMaxNumberOfRecoverApplicationsLocal(6);
		fgSetting.checkAssertionsForSettings();
		return fgSetting;
	}

	/**
	 * Setting is used for error analysis to find out how 
	 * well a right context approach performs, only modifications at the (inclusive) 
	 * right of the error location are explored 
	 * Search heuristic: find recover branches at the right of the failure location using an expanding search space
	 * @return Setting that searches for a recover branch by modifying the right context
	 */
	public static FineGrainedSetting createRightContextSetting(){
		FineGrainedSetting fgSetting = new FineGrainedSetting()
			.setTimeLimit(2500)
			.setAcceptDistanceLines(5)
			.setBacktrackDistanceLines(0)
			.setBacktrackDistanceLinesSingleToken(0)
			.setBackwardFactor(0)
			.setForwardDistanceLines(Integer.MAX_VALUE)
			.setForwardFactor(1)
			.setMaxNumberOfRecoverApplicationsLocal(6);
		fgSetting.checkAssertionsForSettings();
		return fgSetting;
	}
	
	private void checkAssertionsForSettings() {
    	assert(timeLimit > 0);
    	assert(acceptDistanceLines > 0);
    	assert(backtrackDistanceLines <= backtrackDistanceLinesSingleToken);
    	assert(backwardFactor >= 0);
    	assert(forwardFactor >= 0);
    	assert(maxNumberOfRecoverApplicationsLocal >= 1);
    	assert(maxNumberOfRecoverApplicationsLocal <= 10);
	}
}
