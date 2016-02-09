package org.spoofax.jsglr.client;

public class RecoveryConnector {
	private SGLR mySGLR;
	private IntegratedRecoverySettings settings;
	private RegionRecovery regionSelector;
	private FineGrainedRecovery fgRegionalRecovery; // used on errorneous region (or on region near failure line)
	private FineGrainedRecovery fgCursorLineRecovery; // intended for recovery near cursor

	public void setFgRegionalRecovery(FineGrainedRecovery fgRegionalRecovery) {
		this.fgRegionalRecovery = fgRegionalRecovery;
	}

	private ParserHistory getHistory() {
		return mySGLR.getHistory();
	}

	public RecoveryConnector(SGLR parser){
		this(parser, IntegratedRecoverySettings.createDefaultSettings(), FineGrainedSetting.createDefaultSetting());
	}
	
	public RecoveryConnector(SGLR parser, IntegratedRecoverySettings settings) {
		this(parser, settings, FineGrainedSetting.createDefaultSetting());
	}

	public int getMaxNumberOfRecoverApplicationsGlobal(){
		return this.settings.getMaxNumberOfRecoverApplicationsGlobal();
	}
	
	public RecoveryConnector(SGLR parser, IntegratedRecoverySettings settings, FineGrainedSetting fgSettings) {
		this.mySGLR = parser;
		this.regionSelector = new RegionRecovery(mySGLR);
		this.settings = settings;
		this.fgCursorLineRecovery = new FineGrainedRecovery(mySGLR, FineGrainedSetting.createCursorLineSetting());		
		this.fgRegionalRecovery = new FineGrainedRecovery(mySGLR, fgSettings);
	}
	
	public void recover() throws InterruptedException {
		mySGLR.getPerformanceMeasuring().startRecovery();
		boolean recoverySucceeded = combinedRecover();
		mySGLR.getPerformanceMeasuring().endRecovery(recoverySucceeded);
	}

	private boolean combinedRecover() throws InterruptedException {
		int failureOffset = mySGLR.getParserLocation();
		int failureLineIndex = getHistory().getLineOfTokenPosition(failureOffset - 1);
		int cursorLineIndex = getHistory().getLineOfTokenPosition(mySGLR.getCursorLocation());

		mySGLR.getPerformanceMeasuring().addFailureLocation(failureOffset);

		if (settings.useFineGrained() && settings.useCursorLocation()) {
			if(tryFineGrainedOnCursorLine(failureOffset, failureLineIndex, cursorLineIndex)){
				//System.out.println("FG on cursor line succeeded!");
				return true;
			}
		}
		boolean skipSucceeded = false;
		if (settings.useRegionSelection() || settings.useRegionRecovery()) {
			skipSucceeded = trySelectErroneousRegion(failureOffset, failureLineIndex, cursorLineIndex);
		}
		if (settings.useFineGrained()) {
			boolean fgSucceeded = tryFineGrainedRecovery(failureOffset, failureLineIndex, skipSucceeded);
			if (parseRemainingTokens(true)) {
				return true;
			} else if (fgSucceeded && mySGLR.getParserLocation() > failureOffset) {
				return combinedRecover();
			}
		}
		if (settings.useRegionRecovery() && skipSucceeded) {
			parseErrorFragmentAsWhiteSpace();
			if (parseRemainingTokens(true))
				return true;
			else if (mySGLR.getParserLocation() > failureOffset)
				return combinedRecover();
		}
		return false;
	}

	private boolean tryFineGrainedOnCursorLine(int failureOffset, int failureLineIndex, int cursorLineIndex) throws InterruptedException {
		if(isLikelyErrorLocation(failureLineIndex, cursorLineIndex)){
			/*			
			int startTok = getHistory().getLine(Math.max(0, cursorLineIndex - 1)).getTokensSeen();		
			int endTok = failureOffset;
			System.out.println(getHistory().getFragment(startTok, endTok, mySGLR.currentInputStream));
			*/
			
			mySGLR.getPerformanceMeasuring().startFGOnCursor();
			boolean fgSucceededOnCursor = fgCursorLineRecovery.recover(failureOffset, cursorLineIndex);
			mySGLR.getPerformanceMeasuring().endFGOnCursor(fgSucceededOnCursor);
			if (fgSucceededOnCursor && parseRemainingTokens(true)) {
				return true;
			}
		}
		//System.out.println("FG on cursor line failed!");
		return false;
	}

	private boolean trySelectErroneousRegion(int failureOffset, int failureLineIndex, int cursorLineIndex) throws InterruptedException {
		boolean skipSucceeded;
		mySGLR.getPerformanceMeasuring().startCG();
		if(settings.useCursorLocation() && isPossibleErrorLocation(failureLineIndex, cursorLineIndex))
			skipSucceeded = regionSelector.selectErroneousFragment(failureOffset, failureLineIndex, cursorLineIndex); 
		else
			skipSucceeded = regionSelector.selectErroneousFragment(failureOffset, failureLineIndex);
		mySGLR.getPerformanceMeasuring().endCG(skipSucceeded);
		mySGLR.acceptingStack = null;
		mySGLR.activeStacks.clear();
		return skipSucceeded;
	}

	private boolean tryFineGrainedRecovery(int failureOffset, int failureLineIndex, boolean skipSucceeded) throws InterruptedException {
		mySGLR.getPerformanceMeasuring().startFG();
		boolean fgSucceeded = false;
		if (skipSucceeded && settings.useRegionSelection()) {
			StructureSkipSuggestion erroneousRegion = regionSelector.getErroneousRegion();
			fgSucceeded = fgRegionalRecovery.recover(
				failureOffset, 
				Math.min(erroneousRegion.getIndexHistoryEnd(), failureLineIndex), 
				erroneousRegion.getStartSkip().getTokensSeen(), 
				erroneousRegion.getEndSkip().getTokensSeen()
			);
		} else {
			fgSucceeded = fgRegionalRecovery.recover(failureOffset, failureLineIndex);
		}
		mySGLR.getPerformanceMeasuring().endFG(fgSucceeded);
		return fgSucceeded;
	}

	private boolean isLikelyErrorLocation(int failureLineIndex, int cursorLineIndex) {
		return 
			isPossibleErrorLocation(failureLineIndex, cursorLineIndex) && 
			failureLineIndex - cursorLineIndex <= 10;
	}

	private boolean isPossibleErrorLocation(int failureLineIndex, int cursorLineIndex) {
		return mySGLR.isSetCursorLocation() && failureLineIndex >= cursorLineIndex;
	}

	public boolean parseRemainingTokens(boolean keepHistory) throws InterruptedException {
		while ((!getHistory().hasFinishedRecoverTokens())
				&& mySGLR.activeStacks.size() > 0
				&& mySGLR.acceptingStack == null) {
			getHistory().readRecoverToken(mySGLR, keepHistory);
			mySGLR.doParseStep();
		}
		return recoverySucceeded();
	}

	private boolean recoverySucceeded() {
		return (mySGLR.activeStacks.size() > 0 || mySGLR.acceptingStack != null);
	}

	public boolean parseErrorFragmentAsWhiteSpace() throws InterruptedException {
		mySGLR.activeStacks.clear();
		mySGLR.activeStacks.addAll(regionSelector.getStartLineErrorFragment().getStackNodes());
		getHistory().setTokenIndex(regionSelector.getStartPositionErrorFragment());
		getHistory().resetRecoveryIndentHandler(regionSelector.getStartLineErrorFragment().getIndentValue());
		while ((getHistory().getTokenIndex() < regionSelector.getEndPositionErrorFragment())
				&& mySGLR.activeStacks.size() > 0
				&& mySGLR.acceptingStack == null) {
			getHistory().readRecoverToken(mySGLR, false);
			parseAsLayout();
		}
		return recoverySucceeded();
	}

	private void parseAsLayout() throws InterruptedException {
		if (!isLayoutCharacter((char) mySGLR.getCurrentToken().getToken()) && mySGLR.getCurrentToken().getToken() != SGLR.EOF) {
			mySGLR.setCurrentToken(new TokenOffset(' ', mySGLR.getCurrentToken().getOffset()));
		}
		mySGLR.doParseStep();
	}

	public static boolean isLayoutCharacter(char aChar) {
		// TODO: Move this to the parse table class; only it truly can know
		// layout characters
		return aChar == ' ' || aChar == '\t' || aChar == '\n';
	}
}
