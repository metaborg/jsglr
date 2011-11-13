package org.spoofax.jsglr.client;

public class FineGrainedSetting {

	private int acceptDistanceLines; //number of lines that must be parsed error-free before recovery is accepted
	private int backtrackDistanceLines; //maximum number of (non-empty) lines that is fully backtracked
	private int backtrackDistanceLinesSingleToken; //maximum number of (non-empty) lines that is explored for single token recoveries
	private double backwardFactor; //increase explored left context with x lines at each loop 
	private double forwardFactor; //increase explored right context with x lines at each loop (for example: 0.5 then extend one line after two loops)
	private int maxNumberOfRecoverApplicationsLocal; //branches with more then x recoveries after backtrack offset are cut off in FG mode
	private int maxNumberOfRecoverApplicationsGlobal; //branches with more then x recoveries are cut off (IS USED FOR ANALYSIS)

	/*
	 * Settings that control the heuristics applied to find a recover branch
	 */
	private int timeLimit; //finegrained is stopped in case time limit expired
	
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

	public int getMaxNumberOfRecoverApplicationsGlobal() {
		return maxNumberOfRecoverApplicationsGlobal;
	}

	public FineGrainedSetting setMaxNumberOfRecoverApplicationsGlobal(
			int maxNumberOfRecoverApplicationsGlobal) {
		this.maxNumberOfRecoverApplicationsGlobal = maxNumberOfRecoverApplicationsGlobal;
		return this;
	}

	private FineGrainedSetting() {
		this.setTimeLimit(1000);
		this.setAcceptDistanceLines(5);
		this.setBacktrackDistanceLines(8);
		this.setBacktrackDistanceLinesSingleToken(80);
		this.setBackwardFactor(1);
		this.setForwardFactor(0.5);
		this.setMaxNumberOfRecoverApplicationsLocal(6);
		this.setMaxNumberOfRecoverApplicationsGlobal(Integer.MAX_VALUE);
		
	}
	
	/**
	 * Setting that is fine tuned for interactive editing.
	 * Both the left- and the right- context of the failure location are explored
	 * using an expanding search space.
	 * Recovery fails or succeeds within 1 second
	 * @return Standard setting for fine grained recovery in an interactive environment
	 */
	public static FineGrainedSetting createDefaultSetting(){
		return new FineGrainedSetting()
			.setTimeLimit(1000)
			.setAcceptDistanceLines(5)
			.setBacktrackDistanceLines(8)
			.setBacktrackDistanceLinesSingleToken(80)
			.setBackwardFactor(1)
			.setForwardFactor(0.5)
			.setMaxNumberOfRecoverApplicationsLocal(6)
			.setMaxNumberOfRecoverApplicationsGlobal(Integer.MAX_VALUE);
	}

	/**
	 * Setting is used for error analysis to find out how many files
	 * can be recovered with a single token insertion/deletion/replacement 
	 * Search heuristic: try all recover-count = 1 branches in the left context
	 * @return Setting that globally searches for a single token recovery
	 */
	public static FineGrainedSetting createSingleTokenSetting(){
		return new FineGrainedSetting()
			.setTimeLimit(2500)
			.setAcceptDistanceLines(15)
			.setBacktrackDistanceLines(0)
			.setBacktrackDistanceLinesSingleToken(500)
			.setBackwardFactor(1)
			.setForwardFactor(0)
			.setMaxNumberOfRecoverApplicationsLocal(1)
			.setMaxNumberOfRecoverApplicationsGlobal(1);
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
		return new FineGrainedSetting()
			.setTimeLimit(4000)
			.setAcceptDistanceLines(5)
			.setBacktrackDistanceLines(20)
			.setBacktrackDistanceLinesSingleToken(0)
			.setBackwardFactor(20)
			.setForwardFactor(20)
			.setMaxNumberOfRecoverApplicationsLocal(10)
			.setMaxNumberOfRecoverApplicationsGlobal(Integer.MAX_VALUE);
	}

	/**
	 * Setting is used for error analysis to find out how well
	 * a local approach performs, only modifications on a single line are explored. 
	 * Search heuristic: try all recover-count = 1, 2, 3, ... branches on the failure (or other) line,
	 * @return Setting that locally searches for a recover branch
	 */
	public static FineGrainedSetting createLocalContextSetting(){
		return new FineGrainedSetting()
			.setTimeLimit(2500)
			.setAcceptDistanceLines(5)
			.setBacktrackDistanceLines(0)
			.setBacktrackDistanceLinesSingleToken(0)
			.setBackwardFactor(0)
			.setForwardFactor(0)
			.setMaxNumberOfRecoverApplicationsLocal(10)
			.setMaxNumberOfRecoverApplicationsGlobal(Integer.MAX_VALUE);
	}

	/**
	 * Setting is used for error analysis to find out how 
	 * well a left context approach performs, only modifications at the (inclusive) 
	 * left of the error location are explored 
	 * Search heuristic: find recover branches at the left of the failure location using an expanding search space
	 * @return Setting that searches for a recover branch by modifying the left context
	 */
	public static FineGrainedSetting createLeftContextSetting(){
		return new FineGrainedSetting()
			.setTimeLimit(2500)
			.setAcceptDistanceLines(5)
			.setBacktrackDistanceLines(20)
			.setBacktrackDistanceLinesSingleToken(80)
			.setBackwardFactor(1)
			.setForwardFactor(0)
			.setMaxNumberOfRecoverApplicationsLocal(6)
			.setMaxNumberOfRecoverApplicationsGlobal(Integer.MAX_VALUE);
	}

	/**
	 * Setting is used for error analysis to find out how 
	 * well a right context approach performs, only modifications at the (inclusive) 
	 * right of the error location are explored 
	 * Search heuristic: find recover branches at the right of the failure location using an expanding search space
	 * @return Setting that searches for a recover branch by modifying the right context
	 */
	public static FineGrainedSetting createRightContextSetting(){
		return new FineGrainedSetting()
			.setTimeLimit(2500)
			.setAcceptDistanceLines(5)
			.setBacktrackDistanceLines(0)
			.setBacktrackDistanceLinesSingleToken(0)
			.setBackwardFactor(0)
			.setForwardFactor(1)
			.setMaxNumberOfRecoverApplicationsLocal(6)
			.setMaxNumberOfRecoverApplicationsGlobal(Integer.MAX_VALUE);
	}
}
