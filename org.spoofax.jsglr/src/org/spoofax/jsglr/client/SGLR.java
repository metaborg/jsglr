/*
 * Created on 03.des.2005
 * 
 * Copyright (c) 2005, Karl Trygve Kalleberg <karltk near strategoxt.org>
 * 
 * Licensed under the GNU Lesser General Public License, v2.1
 */
package org.spoofax.jsglr.client;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.interpreter.terms.ITermFactory;
import org.spoofax.jsglr.client.indentation.LayoutFilter;
import org.spoofax.jsglr.shared.ArrayDeque;
import org.spoofax.jsglr.shared.BadTokenException;
import org.spoofax.jsglr.shared.SGLRException;
import org.spoofax.jsglr.shared.TokenExpectedException;
import org.spoofax.jsglr.shared.Tools;
import org.spoofax.terms.util.PushbackStringIterator;

public class SGLR {

    private static final boolean ENFORCE_NEWLINE_FILTER = true;
    private static final boolean PARSE_TIME_LAYOUT_FITER = true;

    /**
     * logging variables
     */
    private int layoutFiltering;
    private int enforcedNewlineSkip = 0;

    private RecoveryPerformance performanceMeasuring;

    private final Set<BadTokenException> collectedErrors = new LinkedHashSet<BadTokenException>();

    public static final int EOF = ParseTable.NUM_CHARS;

    static final int TAB_SIZE = 8;

    protected static boolean WORK_AROUND_MULTIPLE_LOOKAHEAD;

    // Performance testing
    private static long parseTime = 0;
    private static int parseCount = 0;

    protected Frame startFrame;

    private long startTime;

    protected volatile boolean asyncAborted;

    protected Frame acceptingStack;
    protected Frame lastAcceptingStack;
    protected int lastAcceptTokenSeen;

    protected final ArrayDeque<Frame> activeStacks;

    private ParseTable parseTable;

    private TokenOffset currentToken;
    private int currentTokenOffset = 0;

    private boolean applyCompletionProd = true;
    private boolean readNonLayout = false;

    private int currentIndentation;

    private int tokensSeen;

    protected int lineNumber;

    protected int columnNumber;

    protected int lastLineNumber;

    protected int lastColumnNumber;

    private int startOffset;

    private final ArrayDeque<ActionState> forShifter;

    private ArrayDeque<Frame> forActor;

    private ArrayDeque<Frame> forActorDelayed;

    private int maxBranches;

    private int maxToken;

    private int maxLine;

    private int maxColumn;

    private int maxTokenNumber;

    private AmbiguityManager ambiguityManager;

    protected Disambiguator disambiguator;

    private int rejectCount;

    private int reductionCount;

    protected PushbackStringIterator currentInputStream;

    private PathListPool pathCache = PathListPool.getInstance();
    private ArrayDeque<Frame> activeStacksWorkQueue = new ArrayDeque<Frame>();
    private ArrayDeque<Frame> recoverStacks;

    private ParserHistory history;

    private RecoveryConnector recoverIntegrator;

    private ITreeBuilder treeBuilder;

    private AbstractParseNode parseTree;

    protected boolean useIntegratedRecovery;

    private boolean triedRecovery = false;

    private boolean isNewCompletionMode;

    /**
     * If true, parser reads as many characters as possible, but succeeds even if not all characters were read.
     */
    private boolean isParseMaxMode;

    private boolean isFineGrainedMode;

    private int fineGrainedStartLocation;

    private int fineGrainedRecoverMax;

    private int cursorLocation;

    private LayoutFilter layoutFilter;

    public int getCursorLocation() {
        return cursorLocation;
    }

    public boolean isSetCursorLocation() {
        return 0 < cursorLocation && cursorLocation != Integer.MAX_VALUE;
    }

    public void setCompletionParse(boolean isCompletionMode, int cursorLocation) {
        this.isNewCompletionMode = isCompletionMode;
        this.cursorLocation = cursorLocation;
    }

    public void setParseMaxMode(boolean isParseMaxMode) {
        this.isParseMaxMode = isParseMaxMode;
    }

    public void setDisambiguatorTimeout(long timeout) {
        this.disambiguator.setTimeout(timeout);
    }

    protected ArrayDeque<Frame> getRecoverStacks() {
        return recoverStacks;
    }

    public Set<BadTokenException> getCollectedErrors() {
        return collectedErrors;
    }

    public int getEnforcedNewlineSkips() {
        return enforcedNewlineSkip;
    }

    public int getLayoutFilteringCount() {
        return layoutFiltering;
    }

    public int getLayoutFilterCallCount() {
        return layoutFilter.getFilterCallCount();
    }

    /**
     * Attempts to set a timeout for parsing. Default implementation throws an {@link UnsupportedOperationException}.
     * 
     * @see org.spoofax.jsglr.io.SGLR#setTimeout(int)
     */
    public void setTimeout(int timeout) {
        throw new UnsupportedOperationException("Use org.spoofax.jsglr.io.SGLR for setting a timeout");
    }

    protected void initParseTimer() {
        // Default does nothing (not supported by GWT)
    }

    @Deprecated public SGLR(ITermFactory pf, ParseTable parseTable) {
        this(new Asfix2TreeBuilder(pf), parseTable);
    }

    @Deprecated public SGLR(ParseTable parseTable) {
        this(new Asfix2TreeBuilder(), parseTable);
    }

    public SGLR(ITreeBuilder treeBuilder, ParseTable parseTable) {
        assert parseTable != null;
        // Init with a new factory for both serialized or BAF instances.
        this.parseTable = parseTable;
        activeStacks = new ArrayDeque<Frame>();
        forActor = new ArrayDeque<Frame>();
        forActorDelayed = new ArrayDeque<Frame>();
        forShifter = new ArrayDeque<ActionState>();
        disambiguator = new Disambiguator();
        useIntegratedRecovery = false;
        setUseStructureRecovery(false);
        history = new ParserHistory();
        setCompletionParse(false, Integer.MAX_VALUE);
        setTreeBuilder(treeBuilder);
        layoutFilter = new LayoutFilter(parseTable, true);
    }

    protected void setFinegrainedRecoverMode(boolean fineGrainedMode) {
        this.isFineGrainedMode = fineGrainedMode;
        recoverStacks = new ArrayDeque<Frame>();
    }

    protected void setFineGrainedStartLocation(int fineGrainedStartLocation) {
        this.fineGrainedStartLocation = fineGrainedStartLocation;
    }

    protected void setFineGrainedRecoverMax(int fineGrainedRecoverMax) {
        this.fineGrainedRecoverMax = fineGrainedRecoverMax;
    }

    public RecoveryPerformance getPerformanceMeasuring() {
        return performanceMeasuring;
    }

    /**
     * @deprecated Use {@link #asyncCancel()} instead.
     */
    @Deprecated public void asyncAbort() {
        asyncCancel();
    }

    /**
     * Aborts an asynchronously running parse job, causing it to throw an exception.
     *
     * (Provides no guarantee that the parser is actually cancelled.)
     */
    public void asyncCancel() {
        asyncAborted = true;
    }

    public void asyncCancelReset() {
        asyncAborted = false;
    }

    @Deprecated public static boolean isDebugging() {
        return Tools.debugging;
    }

    public static boolean isLogging() {
        return Tools.logging;
    }

    /**
     * Initializes the active stacks. At the start of parsing there is only one active stack, and this stack contains
     * the start symbol obtained from the parse table.
     *
     * @return top-level frame of the initial stack
     */
    private Frame initActiveStacks() {
        activeStacks.clear();
        final Frame st0 = newStack(parseTable.getInitialState());
        addStack(st0);
        return st0;
    }

    /**
     * Parses a string. The parser reads as many characters as possible, but succeeds even if not all characters were
     * read.
     * 
     * @param input
     *            The input string.
     * @param filename
     *            The source filename of the string, or null if not available.
     * @param startSymbol
     *            The start symbol to use, or null if any applicable.
     * @throws InterruptedException
     */
    public Object parseMax(String input, String filename, String startSymbol)
        throws BadTokenException, TokenExpectedException, ParseException, SGLRException, InterruptedException {
        setParseMaxMode(true);
        return parse(input, filename, startSymbol).output;
    }

    /**
     * Parses the right context of a given offset and collects the largest reductions over that offset Remark: This
     * method is used to interpret the local context around the cursor location for semantic completions
     * 
     * @param prefixStackStructure
     *            Active stacks at the start offset
     * @param startOffset
     *            Offset where the parsing start, also the offset that must be reduced
     * @param endOffset
     *            Offset where the parsing stops.
     * @param inputFragment
     *            Input string
     * @return Set of sub terms that surround the start offset location
     */
    public Set<IStrategoTerm> findLongestLeftContextReductions(ArrayDeque<Frame> prefixStackStructure, int startOffset,
        int endOffset, String inputFragment) throws InterruptedException {

        Set<AbstractParseNode> maxPrefixReductions = new java.util.HashSet<AbstractParseNode>();
        int maxLength = 0;
        int maxPrefixReductionOffset = -1;
        initParseVariables(inputFragment, null);
        if(prefixStackStructure != null) {
            activeStacks.clear();
            acceptingStack = null;
            activeStacks.addAll(prefixStackStructure);
        }
        this.currentInputStream.setOffset(startOffset);
        this.tokensSeen = startOffset;
        getHistory().setTokenIndex(startOffset);
        do {
            readNextToken();
            parseCharacter(); // applies reductions on active stack structure and fills forshifter

            for(int j = 0; j < forShifter.size(); j++) {
                final ActionState as = forShifter.get(j);
                if(!parseTable.hasRejects() || !as.st.allLinksRejected()) {
                    Frame fr = as.st;
                    for(int i = 0; i < fr.stepsCount; i++) {
                        Link lnk = fr.steps[i];
                        int length = lnk.getLength();
                        AbstractParseNode parseNode = lnk.label;
                        if(length > currentInputStream.getOffset() - startOffset) {
                            if(length > maxLength) {
                                maxLength = length;
                                maxPrefixReductionOffset = tokensSeen;
                                maxPrefixReductions.clear();
                                maxPrefixReductions.add(parseNode);
                            } else if(length == maxLength && tokensSeen == maxPrefixReductionOffset) {
                                maxPrefixReductions.add(parseNode);
                            }
                        }
                    }
                }
            }
            shifter(); // renewes active stacks with states in forshifter
        } while(tokensSeen < endOffset && activeStacks.size() > 0);
        if(acceptingStack != null) {
            for(int i = 0; i < acceptingStack.stepsCount; i++) {
                Link lnk = acceptingStack.steps[i];
                int length = lnk.getLength();
                AbstractParseNode parseNode = lnk.label;
                if(length > currentInputStream.getOffset() - startOffset) {
                    if(length > maxLength) {
                        maxLength = length;
                        maxPrefixReductionOffset = tokensSeen;
                        maxPrefixReductions.clear();
                        maxPrefixReductions.add(parseNode);
                    } else if(length == maxLength && tokensSeen == maxPrefixReductionOffset) {
                        maxPrefixReductions.add(parseNode);
                    }
                }
            }
        }
        Set<IStrategoTerm> result = new java.util.HashSet<IStrategoTerm>();
        for(AbstractParseNode node : maxPrefixReductions) {
            int startReductionOffset = maxPrefixReductionOffset - maxLength - 1;
            assert startReductionOffset <= startOffset;
            if(!(getTreeBuilder() instanceof NullTreeBuilder)) {
                try {
                    getTreeBuilder().reset(startReductionOffset);
                    IStrategoTerm candidate =
                        ((IStrategoTerm) disambiguator.applyFilters(this, node, null, startReductionOffset,
                            tokensSeen));
                    if(candidate != null)
                        result.add(candidate);
                } catch(FilterException e) {
                    e.printStackTrace();
                } catch(SGLRException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    // only FG recovery,
    // 1) this ensure that recovery does not touch characters after endoffset
    // 2) this ensures real parser state that represents the text without "skip fragments"

    /**
     * Constructs the stack structure for an endoffset from a start offset Remark: This method is used for checking the
     * "valid prefix" criterion of syntactic completion templates
     * 
     * @param stackStructure
     *            Active stacks at the start offset
     * @param startOffset
     *            Offset where the parsing starts
     * @param endOffset
     *            Offset where the parsing stops.
     * @param inputFragment
     *            Input string
     * @param useRecovery
     *            Use fine-grained recovery to reconstruct the stack structure
     * @return Parser stacks at the end offset
     */
    public ArrayDeque<Frame> parseInputPart(ArrayDeque<Frame> stackStructure, int startOffset, int endOffset,
        String inputFragment, boolean useRecovery) throws InterruptedException {

        if(startOffset == endOffset)
            return stackStructure;
        assert startOffset < endOffset;

        // set the parser configuration
        initParseVariables(inputFragment, null);
        this.currentInputStream.setOffset(startOffset);
        this.tokensSeen = startOffset;
        getHistory().setTokenIndex(startOffset);
        FineGrainedRecovery fgRecovery = null;
        if(useRecovery) {
            FineGrainedSetting fgSettings = FineGrainedSetting.createDefaultSetting();
            fgSettings.setEndOffsetFragment(endOffset);
            fgRecovery = new FineGrainedRecovery(this, fgSettings);
        }
        if(stackStructure != null) {
            activeStacks.clear();
            acceptingStack = null;
            activeStacks.addAll(stackStructure);
        }
        return parseInputPart(endOffset, fgRecovery, useRecovery);
    }

    private ArrayDeque<Frame> parseInputPart(int endParseOffset, FineGrainedRecovery fgRecovery, boolean useRecovery)
        throws InterruptedException {
        try {
            do {
                assert getHistory().getTokenIndex() == this.currentInputStream.getOffset();
                assert this.currentInputStream.getOffset() <= endParseOffset;
                // System.out.print(((char)this.currentToken));
                readNextToken();
                history.keepTokenAndState(this);
                doParseStep();
                if(this.currentInputStream.getOffset() == endParseOffset && activeStacks.size() > 0) {
                    ArrayDeque<Frame> stackNodes = new ArrayDeque<Frame>();
                    stackNodes.addAll(activeStacks);
                    return stackNodes;
                }
            } while(getCurrentToken().getToken() != SGLR.EOF && activeStacks.size() > 0);
            if(useRecovery && activeStacks.isEmpty() && acceptingStack == null) { // TODO: specialized FG recover
                                                                                  // support
                int failureOffset = getParserLocation();
                int failureLineIndex = getHistory().getLineOfTokenPosition(failureOffset - 1);
                fgRecovery.recover(failureOffset, failureLineIndex);
                if(acceptingStack == null && activeStacks.size() > 0
                    && this.currentInputStream.getOffset() != endParseOffset) {
                    return parseInputPart(endParseOffset, fgRecovery, useRecovery);
                }
            }
            ArrayDeque<Frame> stackNodes = new ArrayDeque<Frame>();
            stackNodes.addAll(activeStacks);
            if(acceptingStack != null)
                stackNodes.add(acceptingStack);
            return stackNodes;
        } finally {
            activeStacks.clear();
            activeStacksWorkQueue.clear();
            forShifter.clear();
            history.clear();
            if(recoverStacks != null)
                recoverStacks.clear();
        }
    }

    /**
     * Parses a string and constructs a new tree using the tree builder.
     * 
     * @param input
     *            The input string.
     * @param filename
     *            The source filename of the string, or null if not available.
     * @param startSymbol
     *            The start symbol to use, or null if any applicable.
     * @throws InterruptedException
     */
    public SGLRParseResult parse(String input, String filename, String startSymbol)
        throws BadTokenException, TokenExpectedException, ParseException, SGLRException, InterruptedException {
        logBeforeParsing();
        initParseVariables(input, filename);
        startTime = System.currentTimeMillis();
        initParseTimer();
        getPerformanceMeasuring().startParse();
        final SGLRParseResult result = sglrParse(startSymbol);
        return result;
    }

    private SGLRParseResult sglrParse(String startSymbol)
        throws BadTokenException, TokenExpectedException, ParseException, SGLRException, InterruptedException {
        if(isParseMaxMode)
            disambiguator.initializeFromParser(this);

        try {
            // main parse loop, repeats until stuck or EOF is encountered
            do {
                if(Thread.currentThread().isInterrupted())
                    throw new InterruptedException();

                readNextToken();
                history.keepTokenAndState(this);
                logBeforeNextParseStep();
                doParseStep();

                if(isParseMaxMode) {
                    Frame accept = checkImmediateAcceptance(startSymbol);
                    if(accept != null) {
                        lastAcceptingStack = accept;
                        lastAcceptTokenSeen = tokensSeen;
                    }
                }
            } while(currentToken.getToken() != SGLR.EOF && activeStacks.size() > 0);

            if(acceptingStack != null) {
                lastAcceptingStack = acceptingStack;
                lastAcceptTokenSeen = tokensSeen;
            } else if(isParseMaxMode) {
                acceptingStack = lastAcceptingStack;
            }

            if(acceptingStack == null) {
                // the parsing failed
                collectedErrors.add(createBadTokenException());
            }

            if(useIntegratedRecovery && acceptingStack == null) {
                recoverIntegrator.recover();
                // signal that normal parsing failed, but we tried recovery
                triedRecovery = true;
                if(acceptingStack == null && activeStacks.size() > 0) {
                    return sglrParse(startSymbol);
                }
            }
            getPerformanceMeasuring().endParse(acceptingStack != null);
        } catch(final TaskCancellationException e) {
            throw new ParseTimeoutException(this, currentToken.getToken(), tokensSeen - 1, lineNumber, columnNumber,
                collectedErrors);
        } finally {
            activeStacks.clear();
            activeStacksWorkQueue.clear();
            forShifter.clear();
            history.clear();
            if(recoverStacks != null)
                recoverStacks.clear();
        }

        logAfterParsing();

        if(acceptingStack == null) {
            // both parsing and recovery (if attempted) failed
            final BadTokenException bad = createBadTokenException();
            if(collectedErrors.isEmpty()) {
                throw bad;
            } else {
                collectedErrors.add(bad);
                throw new MultiBadTokenException(this, collectedErrors);
            }
        }

        final Link s = acceptingStack.findDirectLink(startFrame);

        if(s == null) {
            // I don't think this can happen normally, but recovery might cause this
            throw new ParseException(this, "Accepting stack has no link");
        }
        // System.out.println(s.recoverCount);
        assert (s.recoverCount <= s.recoverWeight);

        performanceMeasuring.setRecoverCount(s.recoverCount);

        logParseResult(s);
        // System.out.println("recoveries: " + s.recoverCount);
        Tools.debug("recoveries: ", s.recoverCount);
        // Tools.debug(s.label.toParseTree(parseTable));

        Object result;
        if(getTreeBuilder() instanceof NullTreeBuilder) {
            result = null;
        } else {
            this.parseTree = s.label;
            result = disambiguator.applyFilters(this, s.label, startSymbol, tokensSeen);

            if(isParseMaxMode)
                result = new Object[] { result, lastAcceptTokenSeen };
        }

        // If we don't intend to return an AST, recovery is meaningless.
        if(triedRecovery && result == null) {
            // we tried recovery, so normal parsing had failed
            switch(collectedErrors.size()) {
                case 0:
                    throw new ParseException(this,
                        "Parsing failed without indicating an error, please notify Spoofax developers.");
                case 1:
                    throw collectedErrors.iterator().next();
                default:
                    throw new MultiBadTokenException(this, collectedErrors);
            }
        } else {
            // either parsing succeeded, or recovery succeeded and the caller cares about an AST
            return new SGLRParseResult(result);
        }
    }

    void readNextToken() {
        // System.out.print("(" + (currentToken.getOffset()) + ")=" + (char) currentToken.getToken());
        logCurrentToken();
        setCurrentToken(getNextToken());
    }

    protected void setCurrentToken(TokenOffset tok) {
        if(currentToken.getToken() == -1)
            currentIndentation = 0;
        else
            switch(currentToken.getToken()) {
                case '\n':
                    currentIndentation = 0;
                    break;
                case '\t':
                    currentIndentation = (currentIndentation / TAB_SIZE + 1) * TAB_SIZE;
                    break;
                case -1:
                    break;
                default:
                    currentIndentation++;
            }

        this.currentToken.setToken(tok.getToken());
        this.currentToken.setOffset(tok.getOffset());
    }

    protected TokenOffset getCurrentToken() {
        return this.currentToken;
    }

    protected void doParseStep() throws InterruptedException {
        parseCharacter(); // applies reductions on active stack structure and fills forshifter
        shifter(); // renewes active stacks with states in forshifter
    }

    private void initParseVariables(String input, String filename) {
        forActor.clear();
        forActorDelayed.clear();
        forShifter.clear();
        history.clear();
        startFrame = initActiveStacks();
        currentToken = new TokenOffset();
        currentIndentation = 0;
        tokensSeen = 0;
        currentInputStream = new PushbackStringIterator(input);
        acceptingStack = null;
        collectedErrors.clear();
        history = new ParserHistory();
        performanceMeasuring = new RecoveryPerformance();
        getTreeBuilder().initializeInput(input, filename);
        PooledPathList.resetPerformanceCounters();
        pathCache.resetPerformanceCounters();
        ambiguityManager = new AmbiguityManager(input.length());
        parseTree = null;
        enforcedNewlineSkip = 0;
        layoutFiltering = 0;
        if(getTreeBuilder().getTokenizer() != null) {
            // Make sure we use the same starting offsets as the tokenizer, if any
            // (crucial for parsing fragments at a time)
            lineNumber = getTreeBuilder().getTokenizer().getEndLine();
            columnNumber = getTreeBuilder().getTokenizer().getEndColumn();
            startOffset = getTreeBuilder().getTokenizer().getStartOffset();
        } else {
            lineNumber = 1;
            columnNumber = 0;
        }
    }

    private BadTokenException createBadTokenException() {

        final Frame singlePreviousStack = activeStacks.size() == 1 ? activeStacks.get(0) : null;

        if(singlePreviousStack != null) {
            Action action = singlePreviousStack.peek().getSingularAction();

            if(action != null && action.getActionItems().length == 1) {
                final StringBuilder expected = new StringBuilder();

                do {
                    final int token = action.getSingularRange();
                    if(token == -1) {
                        break;
                    }
                    expected.append((char) token);

                    final ActionItem[] items = action.getActionItems();

                    if(!(items.length == 1 && items[0].type == ActionItem.SHIFT)) {
                        break;
                    }

                    final Shift shift = (Shift) items[0];
                    action = parseTable.getState(shift.nextState).getSingularAction();

                } while(action != null);

                if(expected.length() > 0) {
                    return new TokenExpectedException(this, expected.toString(), currentToken.getToken(),
                        tokensSeen + startOffset - 1, lineNumber, columnNumber);
                }
            }
        }

        return new BadTokenException(this, currentToken.getToken(), tokensSeen + startOffset - 1, lineNumber,
            columnNumber);
    }

    private void shifter() {
        logBeforeShifter();
        activeStacks.clear();
        final AbstractParseNode prod =
            new ParseProductionNode(currentToken.getToken(), lastLineNumber, lastColumnNumber);

        // System.out.println("current token offset " + currentTokenOffset);
        // System.out.println("shifted token " + (char)currentToken + " or (int) " + currentToken);

        while(forShifter.size() > 0) {
            final ActionState as = forShifter.remove();
            if(!parseTable.hasRejects() || !as.st.allLinksRejected()) {
                Frame st1 = findStack(activeStacks, as.s);
                if(st1 == null) {
                    st1 = newStack(as.s);
                    addStack(st1);
                }
                st1.addLink(as.st, prod, 1, lastLineNumber, lastColumnNumber);

            } else {
                if(Tools.tracing) {
                    TRACE("SG_SkippingReject() - skipping reject stack with state " + as.st.state.stateNumber);
                }
                if(Tools.logging) {
                    Tools.logger("Shifter: skipping rejected stack with state ", as.st.state.stateNumber);
                }
            }
        }
        logAfterShifter();
    }

    protected void addStack(Frame st1) {
        if(Tools.tracing) {
            TRACE("SG_AddStack() - actives += " + st1.state.stateNumber);
        }
        activeStacks.addFirst(st1);
    }

    private void parseCharacter() throws InterruptedException {
        logBeforeParseCharacter();

        activeStacksWorkQueue.clear();
        activeStacksWorkQueue.addAll(activeStacks);

        forActorDelayed.clear();
        forShifter.clear();

        while(activeStacksWorkQueue.size() > 0 || forActor.size() > 0) {
            if(Thread.currentThread().isInterrupted())
                throw new InterruptedException();

            final Frame st = pickStackNodeFromActivesOrForActor(activeStacksWorkQueue);

            if(!st.allLinksRejected()) {
                actor(st);
            }

            if(activeStacksWorkQueue.size() == 0 && forActor.size() == 0) {
                fillForActorWithDelayedFrames(); // Fills foractor, clears foractor delayed
            }
        }
    }

    private void fillForActorWithDelayedFrames() {
        if(Tools.tracing) {
            TRACE("SG_ - both empty");
        }
        final ArrayDeque<Frame> empty = forActor;
        forActor = forActorDelayed;
        empty.clear();
        forActorDelayed = empty;
    }

    private Frame pickStackNodeFromActivesOrForActor(ArrayDeque<Frame> actives) {
        Frame st;
        if(actives.size() > 0) {
            if(Tools.tracing) {
                TRACE("SG_ - took active");
            }
            st = actives.remove();
        } else {
            if(Tools.tracing) {
                TRACE("SG_ - took foractor");
            }
            st = forActor.remove();
        }
        return st;
    }

    private void actor(Frame st) throws InterruptedException {

        final State s = st.peek();
        logBeforeActor(st, s);

        for(final Action action : s.getActions()) {
            if(action.accepts(currentToken.getToken())) {
                for(final ActionItem ai : action.getActionItems()) {
                    switch(ai.type) {
                        case ActionItem.SHIFT: {
                            final Shift sh = (Shift) ai;
                            final ActionState actState = new ActionState(st, parseTable.getState(sh.nextState));
                            actState.currentToken = currentToken.getToken();
                            addShiftPair(actState); // Adds StackNode to forshifter
                            statsRecordParsers(); // sets some values un current parse state
                            break;
                        }
                        case ActionItem.REDUCE: {
                            final Reduce red = (Reduce) ai;
                            doReductions(st, red.production);
                            break;
                        }
                        case ActionItem.REDUCE_LOOKAHEAD: {
                            final ReduceLookahead red = (ReduceLookahead) ai;
                            if(checkLookahead(red)) {
                                if(Tools.tracing) {
                                    TRACE("SG_ - ok");
                                }
                                doReductions(st, red.production);
                            }
                            break;
                        }
                        case ActionItem.ACCEPT: {
                            if(!st.allLinksRejected()) {
                                acceptingStack = st;
                                if(Tools.logging) {
                                    Tools.logger("Reached the accept state");
                                }
                            }
                            break;
                        }
                        default:
                            throw new IllegalStateException("Unknown action type: " + ai.type);
                    }
                }
            } else if(reducingNewCompletionProd(action)) {
                for(final ActionItem ai : action.getActionItems()) {
                    switch(ai.type) {
                        case ActionItem.REDUCE: {
                            final Reduce red = (Reduce) ai;
                            if(red.production.isNewCompletionProduction()) {
                                doReductions(st, red.production);
                            }
                            break;
                        }
                        case ActionItem.REDUCE_LOOKAHEAD: {
                            break;
                        }
                        case ActionItem.ACCEPT: {
                            break;
                        }
                        case ActionItem.SHIFT: {
                            break;
                        }
                        default:
                            throw new IllegalStateException("Unknown action type: " + ai.type);
                    }

                }

            }
        }

        if(Tools.tracing) {
            TRACE("SG_ - actor done");
        }
    }

    private boolean reducingNewCompletionProd(Action action) {
        if(currentToken.getOffset() >= cursorLocation && applyCompletionProd) {
            return true;
        }
        return false;
    }

    private boolean checkLookahead(ReduceLookahead red) {
        return doCheckLookahead(red, red.getCharRanges());
    }

    private boolean doCheckLookahead(ReduceLookahead red, RangeList[] charClass) {
        if(Tools.tracing) {
            TRACE("SG_CheckLookAhead() - ");
        }

        if(charClass.length == 0)
            return true;

        boolean permit = false;
        int offset = -1;
        int[] readChars = new int[charClass.length];

        int i;
        for(i = 0; i < charClass.length; i++) {
            int c = currentInputStream.read();
            offset++;
            readChars[offset] = c;

            // EOF
            if(c == -1) {
                permit = true;
                break;
            }

            if(!charClass[i].within(c)) {
                permit = true;
                break;
            }
        }

        for(int j = offset; j >= 0; j--) {
            int c = readChars[j];

            /*
             * WORKAROUND: The PushbackStringIterator usually reads the character at its position and then increments
             * its position. However, if the end of the String is found it will return -1 and NOT update the position.
             * Unreading simply decrements the position. Therefore, unreading a -1 will decrement the position even
             * though during reading it was never incremented. This results in reading (and doing a parseStep for) the
             * last character twice if during that parse step a lookahead reduction is performed.
             *
             * We can 'fix' the behavior inside the PushbackStringIterator, but I don't know who else (if anyone)
             * depends on this tricky behavior. Therefore I 'fixed' it here by not unreading if we got a -1.
             */
            if(c != -1) {
                currentInputStream.unread(c);
            }
        }

        return permit;
    }

    private void addShiftPair(ActionState state) {
        if(Tools.tracing) {
            TRACE("SG_AddShiftPair() - " + state.st.state.stateNumber + "->" + state.s.stateNumber);
        }
        forShifter.add(state);
    }

    private void statsRecordParsers() {
        if(forShifter.size() > maxBranches) {
            maxBranches = forShifter.size();
            maxToken = currentToken.getToken();
            maxColumn = columnNumber;
            maxLine = lineNumber;
            maxTokenNumber = tokensSeen;
        }
    }

    private void doReductions(Frame st, Production prod) throws InterruptedException {

        if(Thread.currentThread().isInterrupted())
            throw new InterruptedException();

        // if(recoverModeOk(st, prod)) {
        // return;
        // }

        // if in completion mode and prod is new completion and not a recovery, return
        if(isNewCompletionMode) {
            if(isFineGrainedMode) {
                if(prod.isNewCompletionProduction() && !prod.isRecoverProduction())
                    return;
            } else {
                if(prod.isRecoverProduction() && !prod.isNewCompletionProduction())
                    return;
            }

        } else {
            if(isFineGrainedMode) {
                if(prod.isNewCompletionProduction() && !prod.isRecoverProduction())
                    return;
            } else {
                if(prod.isRecoverProduction() || prod.isNewCompletionProduction())
                    return;
            }
        }

        PooledPathList paths = pathCache.create();

        try {
            st.findAllPaths(paths, prod.arity);
            logBeforeDoReductions(st, prod, paths.size());
            reduceAllPaths(prod, paths);
            logAfterDoReductions();
        } finally {
            pathCache.endCreate(paths);
        }
    }

    private void doLimitedReductions(Frame st, Production prod, Link l) throws InterruptedException { // Todo: Look add
                                                                                                      // sharing code
                                                                                                      // with
                                                                                                      // doReductions

        // if(recoverModeOk(st, prod)) {
        // return;
        // }

        if(isNewCompletionMode) {
            if(isFineGrainedMode) {
                if(prod.isNewCompletionProduction() && !prod.isRecoverProduction())
                    return;
            } else {
                if(prod.isRecoverProduction() && !prod.isNewCompletionProduction())
                    return;
            }

        } else {
            if(isFineGrainedMode) {
                if(prod.isNewCompletionProduction() && !prod.isRecoverProduction())
                    return;
            } else {
                if(prod.isRecoverProduction() || prod.isNewCompletionProduction())
                    return;
            }
        }

        PooledPathList limitedPool = pathCache.create();
        try {
            st.findLimitedPaths(limitedPool, prod.arity, l); // find paths containing the link
            logBeforeLimitedReductions(st, prod, l, limitedPool);
            reduceAllPaths(prod, limitedPool);
        } finally {
            pathCache.endCreate(limitedPool);
        }
    }

    private void reduceAllPaths(Production prod, PooledPathList paths) throws InterruptedException {

        for(int i = 0; i < paths.size(); i++) {
            final Path path = paths.get(i);
            final AbstractParseNode[] kids = path.getParseNodes();
            final Frame st0 = path.getEnd();
            final State next = parseTable.go(st0.state, prod.label);
            logReductionPath(prod, path, st0, next);

            if(PARSE_TIME_LAYOUT_FITER && !layoutFilter.hasValidLayout(prod.label, kids)) {
                layoutFiltering++;
                continue;
            } else if(PARSE_TIME_LAYOUT_FITER)
                layoutFiltering += layoutFilter.getDisambiguationCount();

            if(ENFORCE_NEWLINE_FILTER && parseTable.getLabel(prod.label).getAttributes().isNewlineEnforced()) {
                boolean hasNewline = false;
                for(int j = kids.length - 1; j >= 0; j--) {
                    int status = kids[j].getLayoutStatus();

                    if(status == AbstractParseNode.NEWLINE_LAYOUT) {
                        hasNewline = true;
                        break;
                    }
                    if(status == AbstractParseNode.OTHER_LAYOUT) {
                        hasNewline = false;
                        break;
                    }
                }

                if(!hasNewline) {
                    enforcedNewlineSkip++;
                    continue;
                }
            }

            if(checkMaxRecoverCount(prod, path))
                if(prod.isRecoverProduction())
                    if(!isFineGrainedMode && prod.isNewCompletionProduction())
                        reducer(st0, next, prod, kids, path);
                    else {
                        // still apply placeholder insertion in recovery mode
                        if(prod.isNewCompletionProduction()) {
                            // only apply new completion productions before reading non-layout character after cursor
                            // position
                            if(currentToken.getOffset() >= cursorLocation && applyCompletionProd
                                && isNewCompletionMode) {
                                reducer(st0, next, prod, kids, path);
                            } else {
                                reducerRecoverProduction(st0, next, prod, kids, path);
                            }
                        } else {
                            reducerRecoverProduction(st0, next, prod, kids, path);
                        }

                    }

                else
                    reducer(st0, next, prod, kids, path);

        }

        if(asyncAborted) {
            // Rethrown as ParseTimeoutException in SGLR.sglrParse()
            throw new TaskCancellationException("Long-running parse job aborted");
        }
    }

    private boolean checkMaxRecoverCount(Production prod, final Path path) {
        return checkRecoverCountLocal(prod, path) && checkRecoverCountGlobal(prod, path);
    }

    private boolean checkRecoverCountLocal(Production prod, final Path path) {
        return !isFineGrainedMode || calcRecoverCount(prod, path) <= fineGrainedRecoverMax
            || getHistory().getTokenIndex() - path.getLength() < fineGrainedStartLocation; // large reduction
    }

    private boolean checkRecoverCountGlobal(Production prod, final Path path) {
        return calcRecoverCount(prod, path) <= this.recoverIntegrator.getMaxNumberOfRecoverApplicationsGlobal();
    }

    private void reducer(Frame st0, State s, Production prod, AbstractParseNode[] kids, Path path)
        throws InterruptedException {
        // assert (!prod.isRecoverProduction());
        logBeforeReducer(s, prod, path.getLength());
        increaseReductionCount();

        // final boolean illegalLayout = !layoutFilter.hasValidLayout(prod, kids, parseTable);
        final int length = path.getLength();
        final int numberOfRecoveries = calcRecoverCount(prod, path);
        int numberOfCompleted = path.getCompletedCount();
        int insertionNodes = countInsertionNodes(kids, false);
        int placeholderNodes = countInsertionNodes(kids, true);
        int layoutTerms = countLayoutTerms(kids);
        int emptyTerms = countEmtpyTerms(kids);
        boolean proposalNode = false;
        boolean nestedProposalNode = false;
        boolean proposalSinglePlaceholder = false;

        if(numberOfCompleted > 1)
            return;

        if(kids.length > 0) {
            // disallowing nested completion using only placeholders and another completion node in a left recursive
            // production
            if((kids[0].containsProposal() || kids[0].containsProposal())
                && insertionNodes == (kids.length - layoutTerms - emptyTerms - 1)) {

                AbstractParseNode leftNode = getFirstNonAmbNode(kids[0]);
                if(checkRecursiveProd(leftNode.getLabel(), prod.label)) {
                    return;
                }
            }

            int last = kids.length - 1;
            // disallowing nested completion using only placeholders and another completion node in a right recursive
            // production
            if((kids[last].containsProposal() || kids[last].containsProposal())
                && insertionNodes == (kids.length - layoutTerms - emptyTerms - 1)) {

                AbstractParseNode rightNode = getFirstNonAmbNode(kids[last]);
                if(checkRecursiveProd(rightNode.getLabel(), prod.label)) {
                    return;
                }
            }
        }

        if(!isFineGrainedMode && isNewCompletionMode) {

            if(prod.isNewCompletionProduction()) {
                if(!(currentToken.getOffset() >= cursorLocation && applyCompletionProd)) {
                    return;
                }
            }

            // if prod is not a new-completion prod and has placeholders
            if(!prod.isNewCompletionProduction() && insertionNodes > 0) {
                if(!checkPlaceholderRequirements(kids)) // if the placeholders a valid then the node is completed
                    return;
                else {
                    // tag the node as completed (necessary to find which completion rule has been triggered)
                    if(numberOfCompleted == 0) {
                        if(insertionNodes == 1 && placeholderNodes == 1)
                            proposalSinglePlaceholder = true;
                        proposalNode = true;
                    } else if(numberOfCompleted > 0) {
                        // tag the node as nested completed (node that has placeholders and a completion node)
                        nestedProposalNode = true;
                    }
                }
            }
        }

        if(proposalNode || nestedProposalNode)
            numberOfCompleted = 1;

        final AbstractParseNode t =
            prod.apply(kids, path.getParentCount() > 0 ? path.getParent().getLink().getLine() : lineNumber,
                path.getParentCount() > 0 ? path.getParent().getLink().getColumn() : columnNumber,
                parseTable.getLabel(prod.label).isLayout(),
                parseTable.getLabel(prod.label).getAttributes().isIgnoreLayout(), proposalNode, nestedProposalNode,
                proposalSinglePlaceholder);

        if(numberOfCompleted == 1) {
            t.setContaintsProposal(true);
        }

        final int recoverWeight = calcRecoverWeight(prod, path);
        final Frame st1 = findStack(activeStacks, s);

        if(st1 == null) {
            // Found no existing stack with for state s; make new stack
            addNewStack(st0, s, prod, length, numberOfRecoveries, numberOfCompleted, insertionNodes, recoverWeight, t);
        } else {
            /* A stack with state s exists; check for ambiguities */
            Link nl = st1.findDirectLink(st0);

            if(nl != null) {
                logAmbiguity(st0, prod, st1, nl);

                if(prod.isRejectProduction()) {
                    nl.reject();
                }
                if(recoverWeight == 0 && nl.recoverWeight == 0 || nl.isRejected()) {

                    // creating ambiguity when having two placeholders
                    if(prod.isNewCompletionProduction() && nl.length == 1 && nl.placeholderCount == 1) {
                        createAmbNode(t, nl);
                    }

                    if(!prod.isNewCompletionProduction()) {
                        // creating ambiguity when there is no completion involved
                        if(!nl.hasCompletedLabel && numberOfCompleted == 0) {
                            createAmbNode(t, nl);
                        }

                        // if there is a completion node in the link, but there's a way to parse without placeholders
                        if(nl.hasCompletedLabel && numberOfCompleted == 0) {
                            nl.label = t;
                            nl.recoverCount = numberOfRecoveries;
                            nl.recoverWeight = recoverWeight;
                            nl.hasCompletedLabel = (numberOfCompleted == 1);
                            nl.placeholderCount = insertionNodes;
                        }

                        // making two possibilities for completions ambiguous
                        if(nl.hasCompletedLabel && numberOfCompleted > 0) {
                            createAmbNode(t, nl);
                        }
                    }

                } else if(recoverWeight < nl.recoverWeight) {
                    nl.label = t;
                    nl.recoverCount = numberOfRecoveries;
                    nl.recoverWeight = recoverWeight;
                    nl.hasCompletedLabel = (numberOfCompleted == 1);
                    nl.placeholderCount = insertionNodes;

                    actorOnActiveStacksOverNewLink(nl);
                } else if(recoverWeight == nl.recoverWeight) {
                    nl.label = t;
                }
            } else {
                nl = st1.addLink(st0, t, length, t.getLine(), t.getColumn());
                nl.recoverWeight = recoverWeight;
                nl.recoverCount = numberOfRecoveries;
                nl.hasCompletedLabel = (numberOfCompleted == 1);
                nl.placeholderCount = insertionNodes;

                if(prod.isRejectProduction()) {
                    nl.reject();
                    increaseRejectCount();
                }
                logAddedLink(st0, st1, nl);
                actorOnActiveStacksOverNewLink(nl);
            }
        }
        if(Tools.tracing) {
            TRACE_ActiveStacks();
            TRACE("SG_ - reducer done");
        }
    }

    private AbstractParseNode getFirstNonAmbNode(AbstractParseNode abstractParseNode) {
        if(abstractParseNode.isAmbNode())
            return getFirstNonAmbNode(abstractParseNode.getChildren()[0]);
        return abstractParseNode;
    }

    private boolean checkRecursiveProd(int label, int label2) {
        IStrategoTerm lhsSort = parseTable.getProduction(label).getSubterm(1);
        IStrategoTerm lhsSort2 = parseTable.getProduction(label2).getSubterm(1);

        if(lhsSort.equals(lhsSort2)) {
            return true;
        }
        return false;
    }

    private int countInsertionNodes(AbstractParseNode[] kids, boolean countOnlyPlaceholders) {
        int insertions = 0;

        for(int i = 0; i < kids.length; i++) {
            boolean isAmbInsertion = isAmbInsertion(kids[i], countOnlyPlaceholders);
            if(kids[i].isPlaceholderInsertionNode() || isAmbInsertion) {
                insertions++;
            }
            if(!countOnlyPlaceholders && kids[i].isLiteralCompletionNode()) {
                insertions++;
            }

        }

        return insertions;
    }

    private int countEmtpyTerms(AbstractParseNode[] kids) {
        int emptyTerms = 0;

        for(int i = 0; i < kids.length; i++) {
            if(kids[i].isEmpty() && !kids[i].isLayout() && !kids[i].isProposal() && !kids[i].isNestedProposal()
                && !kids[i].isPlaceholderInsertionNode() && !kids[i].isLiteralCompletionNode()) {
                emptyTerms++;
            }
        }

        return emptyTerms;
    }

    private int countLayoutTerms(AbstractParseNode[] kids) {
        int layoutTerms = 0;

        for(int i = 0; i < kids.length; i++) {
            if(kids[i].isLayout()) {
                layoutTerms++;
            }
        }

        return layoutTerms;
    }

    boolean isLayout(int token) {
        char tokenChar = (char) token;

        if(tokenChar == ' ' || tokenChar == '\n' || tokenChar == '\t')
            return true;

        return false;
    }

    /*
     * Applying a production with placeholder elements (path.getCompletionCount() > 0) has to go with the following
     * requirements: - Placeholders should be adjacent; - Placeholders should only be inserted in completion mode; -
     * Regular Productions with only placeholders should not be reduced - Regular Productions with placeholders should
     * be reduced only if the current token is after the cursorLocation
     */
    private boolean checkPlaceholderRequirements(AbstractParseNode[] kids) {

        // Placeholders can only be inserted at the cursor position;
        // After all tokens before cursorLocation have been read
        if(currentToken.getOffset() < cursorLocation)
            return false;

        // Shouldn't be only placeholders and
        // Placeholders should be adjacent;
        if(!onlyAdjacentPlaceholders(kids))
            return false;

        return true;
    }

    private boolean onlyAdjacentPlaceholders(AbstractParseNode[] kids) {

        boolean seenPlaceholder = false;
        boolean changedTerm = false;
        boolean onlyPlaceholders = true;

        for(int i = 0; i < kids.length; i++) {
            boolean isAmbPlaceholder = isAmbInsertion(kids[i], false);
            if(kids[i].isPlaceholderInsertionNode() || kids[i].isLiteralCompletionNode() || isAmbPlaceholder) {// ||
                                                                                                               // kids[i].isProposal()
                                                                                                               // ||
                                                                                                               // kids[i].isNestedProposal())
                                                                                                               // {
                if(seenPlaceholder && changedTerm)
                    return false;
                seenPlaceholder = true;
            } else if(!kids[i].isLayout() && !isEmptyNode(kids[i])) {
                onlyPlaceholders = false;
                if(seenPlaceholder)
                    changedTerm = true;
            }
        }

        if(onlyPlaceholders)
            return false;

        return true;
    }

    private boolean isEmptyNode(AbstractParseNode abstractParseNode) {
        // check whether it's a node which the list of kids is empty (nullable node)
        if(abstractParseNode.getChildren().length != 0) {
            return false;
        }
        return true;
    }

    private boolean isAmbInsertion(AbstractParseNode abstractParseNode, boolean countOnlyPlaceholders) {
        boolean isAmbInsertion = false;
        if(abstractParseNode.isAmbNode()) {
            AbstractParseNode[] ambNodes = abstractParseNode.getChildren();
            for(int i = 0; i < ambNodes.length; i++) {
                if(ambNodes[i].isPlaceholderInsertionNode()) {
                    isAmbInsertion = true;
                    break;
                }
                if(!countOnlyPlaceholders && ambNodes[i].isLiteralCompletionNode()) {
                    isAmbInsertion = true;
                    break;
                }
                if(ambNodes[i].isAmbNode()) {
                    isAmbInsertion = isAmbInsertion(ambNodes[i], countOnlyPlaceholders);
                    if(isAmbInsertion)
                        break;
                }
            }
        }

        return isAmbInsertion;
    }

    private void reducerRecoverProduction(Frame st0, State s, Production prod, AbstractParseNode[] kids, Path path) {
        assert (prod.isRecoverProduction());
        final int length = path.getLength();
        final int numberOfRecoveries = calcRecoverCount(prod, path);
        final int recoverWeight = calcRecoverWeight(prod, path);
        final AbstractParseNode t =
            prod.apply(kids, lineNumber, columnNumber, parseTable.getLabel(prod.label).isLayout(),
                parseTable.getLabel(prod.label).getAttributes().isIgnoreLayout(), false, false, false);

        // final Object treeTest = getTreeBuilder().buildTree(t);

        final Frame stActive = findStack(activeStacks, s);
        if(stActive != null) {
            Link lnActive = stActive.findDirectLink(st0);
            if(lnActive != null) {
                return;
                // TODO: ambiguity
            }
        }
        final Frame stRecover = findStack(recoverStacks, s);
        if(stRecover != null) {
            Link nlRecover = stRecover.findDirectLink(st0);
            if(nlRecover != null) {
                return;
                // TODO: ambiguity
            }
            nlRecover = stRecover.addLink(st0, t, length, t.getLine(), t.getColumn());
            nlRecover.recoverCount = numberOfRecoveries;
            nlRecover.recoverWeight = recoverWeight;
            return;
        }
        addNewRecoverStack(st0, s, prod, length, numberOfRecoveries, recoverWeight, t);
    }

    private void createAmbNode(AbstractParseNode t, Link nl) {
        nl.addAmbiguity(t, tokensSeen);
        ambiguityManager.increaseAmbiguityCalls();
    }

    /**
     * Found no existing stack with for state s; make new stack
     */
    private Link addNewStack(Frame st0, State s, Production prod, int length, int numberOfRecoveries,
        int numberOfCompleted, int numberOfPlaceholders, int recoverWeight, AbstractParseNode t) {

        final Frame st1 = newStack(s);

        final Link nl = st1.addLink(st0, t, length, t.getLine(), t.getColumn());

        nl.recoverCount = numberOfRecoveries;
        nl.recoverWeight = recoverWeight;
        nl.hasCompletedLabel = (numberOfCompleted == 1);
        nl.placeholderCount = numberOfPlaceholders;
        addStack(st1);
        forActorDelayed.addFirst(st1);

        if(Tools.tracing) {
            TRACE("SG_AddStack() - for-actor-delayed: stack " + st1.state.stateNumber);
        }

        if(prod.isRejectProduction()) {
            if(Tools.logging) {
                Tools.logger("Reject [new]");
            }
            nl.reject();
            increaseRejectCount();
        }

        return nl;
    }

    /**
     * Found no existing stack with for state s; make new stack
     */
    private void addNewRecoverStack(Frame st0, State s, Production prod, int length, int numberOfRecoveries,
        int recoverWeight, AbstractParseNode t) {
        if(!(isFineGrainedMode && !prod.isRejectProduction())) {
            return;
        }
        final Frame st1 = newStack(s);
        final Link nl = st1.addLink(st0, t, length, t.getLine(), t.getColumn());
        nl.recoverCount = numberOfRecoveries;
        nl.recoverWeight = recoverWeight;
        recoverStacks.addFirst(st1);
    }

    private void actorOnActiveStacksOverNewLink(Link nl) throws InterruptedException {
        // Note: ActiveStacks can be modified inside doLimitedReductions
        // new elements may be inserted at the beginning
        final int sz = activeStacks.size();
        for(int i = 0; i < sz; i++) {
            // for(Frame st2 : activeStacks) {
            if(Tools.tracing) {
                TRACE("SG_ activeStack - ");
            }
            final int pos = activeStacks.size() - sz + i;
            final Frame st2 = activeStacks.get(pos);
            if(st2.allLinksRejected() || inReduceStacks(forActor, st2) || inReduceStacks(forActorDelayed, st2)) {
                continue; // stacknode will find reduction in regular process
            }

            for(final Action action : st2.peek().getActions()) {
                if(action.accepts(currentToken.getToken())) {
                    for(final ActionItem ai : action.getActionItems()) {
                        switch(ai.type) {
                            case ActionItem.REDUCE:
                                final Reduce red = (Reduce) ai;
                                doLimitedReductions(st2, red.production, nl);
                                break;
                            case ActionItem.REDUCE_LOOKAHEAD:
                                final ReduceLookahead red2 = (ReduceLookahead) ai;
                                if(checkLookahead(red2)) {
                                    doLimitedReductions(st2, red2.production, nl);
                                }
                                break;
                        }
                    }
                }
            }
        }
    }

    private int calcRecoverCount(Production prod, Path path) {
        int result = path.getRecoverCount();

        if(isNewCompletionMode && !isFineGrainedMode && prod.isNewCompletionProduction()) {
            return result;
        }

        if(prod.isRecoverProduction() || prod.isCompletionProduction()) {
            result += 1;
        }
        return result;
    }

    private int calcRecoverWeight(Production prod, Path path) {
        int result = path.getRecoverWeight();

        if(isNewCompletionMode && !isFineGrainedMode && prod.isNewCompletionProduction()) {
            return result;
        }

        if(prod.isRecoverProduction() || prod.isCompletionProduction()) {
            result += 1;
            if(path.getLength() > 0 && !prod.isCompletionProduction())
                result += 1; // Hack: insertion rules (length 0) should be preferred above water rules.
        }
        return result;
    }

    int count = 0;
    int count2 = 0;

    // private class LongestMatchKey {
    // private AbstractParseNode n1, n2;
    // public LongestMatchKey(AbstractParseNode n1, AbstractParseNode n2) { this.n1 = n1; this.n2 = n2; }
    // @Override public int hashCode() { return (9 << n1.hashCode()) + n2.hashCode(); }
    // @Override public boolean equals(Object o) {
    // return o instanceof LongestMatchKey && ((LongestMatchKey) o).n1 == n1 && ((LongestMatchKey) o).n2 == n2;
    // }
    // }
    // private Map<LongestMatchKey, Integer> longestMatchCache = new HashMap<LongestMatchKey, Integer>();
    //
    // @SuppressWarnings("null")
    // private AbstractParseNode filterLongestMatch(AbstractParseNode t1, AbstractParseNode t2) {
    // if (t1.isParseRejectNode() || t2.isParseRejectNode())
    // return null;
    //
    // System.out.println(t1.toString());
    // System.out.println(t2.toString());
    //
    // Stack<AbstractParseNode[]> stack = new Stack<AbstractParseNode[]>();
    // stack.push(new AbstractParseNode[] {t1, t2});
    //
    // LinkedList<LongestMatchKey> done = new LinkedList<LongestMatchKey>();
    //
    // AbstractParseNode res = null;
    //
    // while (!stack.isEmpty()) {
    // AbstractParseNode[] ns = stack.pop();
    // AbstractParseNode n1 = ns[0];
    // AbstractParseNode n2 = ns[1];
    //
    // if (n1.equals(n2))
    // continue;
    //
    // LongestMatchKey key = new LongestMatchKey(n1, n2);
    // Integer prevRes = longestMatchCache.get(key);
    // if (prevRes != null) {
    // AbstractParseNode newres = prevRes == -1 ? null : (prevRes == 0 ? t1 : t2);
    // if (res != null && res != newres) {
    // res = null;
    // break;
    // }
    // if (newres == null)
    // continue;
    //
    // res = newres;
    // break;
    // }
    //
    // Label l1 = n1.isAmbNode() ? null : parseTable.getLabel(n1.getLabel());
    // Label l2 = n2.isAmbNode() ? null : parseTable.getLabel(n2.getLabel());
    //
    // if (n1.isAmbNode() || n2.isAmbNode()) {
    // AbstractParseNode[] n1Array = n1.isAmbNode() ? n1.getChildren() : new AbstractParseNode[] {n1};
    // AbstractParseNode[] n2Array = n2.isAmbNode() ? n2.getChildren() : new AbstractParseNode[] {n2};
    //
    // for (int i = 0; i < n1Array.length; i++)
    // for (int j = 0; j < n2Array.length; j++) {
    // if (!n1Array[i].isParseRejectNode() && !n2Array[j].isParseRejectNode())
    // stack.push(new AbstractParseNode[] {n1Array[i], n2Array[j]});
    // }
    // continue;
    // }
    //
    // else if (l1 != null && l1.getAttributes().isLongestMatch() &&
    // (l2 == null || !l2.getAttributes().isLongestMatch())) {
    // if (res == t2) {
    // res = null;
    // break;
    // }
    // res = t1;
    // break;
    // }
    //
    // else if (l2 != null && l2.getAttributes().isLongestMatch() &&
    // (l1 == null || !l1.getAttributes().isLongestMatch())) {
    // if (res == t1) {
    // res = null;
    // break;
    // }
    // res = t2;
    // break;
    // }
    //
    // else if (n1.getLabel() != n2.getLabel())
    // continue;
    //
    // else if (l1 != null && l2 != null &&
    // l1.equals(l2) && l1.getAttributes().isLongestMatch()) {
    // int[] end1 = n1.getEnd();
    // int[] end2 = n2.getEnd();
    // if (end1[0] > end2[0] || end1[0] == end2[0] && end1[1] > end2[1]) {
    // if (res == t2) {
    // res = null;
    // break;
    // }
    // res = t1;
    // break;
    // }
    // else if (end2[0] > end1[0] || end2[0] == end1[0] && end2[1] > end1[1]) {
    // if (res == t1) {
    // res = null;
    // break;
    // }
    // res = t2;
    // break;
    // }
    // }
    //
    // done.add(key);
    //
    // for (int i = n1.getChildren().length - 1; i >= 0; i--)
    // stack.push(new AbstractParseNode[] {n1.getChildren()[i], n2.getChildren()[i]});
    // }
    //
    // int val = res == t1 ? 0 : (res == t2 ? 1 : -1);
    // for (LongestMatchKey key : done)
    // longestMatchCache.put(key, val);
    //
    // return res;
    // }

    private boolean inReduceStacks(Queue<Frame> q, Frame frame) {
        if(Tools.tracing) {
            TRACE("SG_InReduceStacks() - " + frame.state.stateNumber);
        }
        return q.contains(frame);
    }

    protected Frame newStack(State s) {
        if(Tools.tracing) {
            TRACE("SG_NewStack() - " + s.stateNumber);
        }
        return new Frame(s);
    }

    private void increaseReductionCount() {
        reductionCount++;
    }

    protected void increaseRejectCount() {
        rejectCount++;
    }

    protected int getRejectCount() {
        return rejectCount;
    }

    Frame findStack(ArrayDeque<Frame> stacks, State s) {
        int desiredState = s.stateNumber;
        if(Tools.tracing) {
            TRACE("SG_FindStack() - state " + desiredState);
        }

        // We need only check the top frames of the active stacks.
        if(Tools.debugging) {
            Tools.debug("findStack() - ", dumpActiveStacks());
            Tools.debug(" looking for ", desiredState);
        }

        final int size = stacks.size();
        for(int i = 0; i < size; i++) {
            Frame stack = stacks.get(i);
            if(stack.state.stateNumber == desiredState) {
                if(Tools.tracing) {
                    TRACE("SG_ - found stack");
                }
                return stack;
            }
        }
        if(Tools.tracing) {
            TRACE("SG_ - stack not found");
        }
        return null;
    }

    private TokenOffset getNextToken() {
        final int ch = currentInputStream.read();

        final TokenOffset to = new TokenOffset(ch, currentTokenOffset);

        if(applyCompletionProd && readNonLayout)
            setApplyCompletionProd(false);

        if(currentTokenOffset >= cursorLocation && !isLayout(ch))
            readNonLayout = true;

        if(Tools.tracing) {
            TRACE("SG_NextToken() - " + ch);
        }

        updateLineAndColumnInfo(ch);

        if(ch == -1) {
            return new TokenOffset(SGLR.EOF, Integer.MAX_VALUE);
        }

        currentTokenOffset++;
        return to;
    }

    protected void updateLineAndColumnInfo(int ch) {
        tokensSeen++;

        if(Tools.debugging) {
            Tools.debug("getNextToken() - ", ch, "(", (char) ch, ")");
        }

        lastLineNumber = lineNumber;
        lastColumnNumber = columnNumber;

        switch(ch) {
            case '\n':
                lineNumber++;
                columnNumber = 0;
                break;
            case '\t':
                columnNumber = (columnNumber / TAB_SIZE + 1) * TAB_SIZE;
                break;
            case -1:
                break;
            default:
                columnNumber++;
        }
    }

    public ParseTable getParseTable() {
        return parseTable;
    }

    public void setTreeBuilder(ITreeBuilder treeBuilder) {
        this.treeBuilder = treeBuilder;
        parseTable.initializeTreeBuilder(treeBuilder);
    }

    public ITreeBuilder getTreeBuilder() {
        return treeBuilder;
    }

    AmbiguityManager getAmbiguityManager() {
        return ambiguityManager;
    }
    
    public long getAmbiguitiesCount(){
        return disambiguator.getAmbiguityCount();
    }

    public Disambiguator getDisambiguator() {
        return disambiguator;
    }

    public void setDisambiguator(Disambiguator disambiguator) {
        this.disambiguator = disambiguator;
    }

    @Deprecated public ITermFactory getFactory() {
        return parseTable.getFactory();
    }

    protected int getReductionCount() {
        return reductionCount;
    }

    protected int getRejectionCount() {
        return rejectCount;
    }

    // //////////////////////////////////////////////////// Log functions
    // ///////////////////////////////////////////////////////////////////////////////

    // TODO: cleanup, this doesn't belong here!

    private static int traceCallCount = 0;

    static void TRACE(String string) {
        System.out.println("[" + traceCallCount + "] " + string + "\n");
        traceCallCount++;
    }

    private String dumpActiveStacks() {
        final StringBuffer sb = new StringBuffer();
        boolean first = true;
        if(activeStacks == null) {
            sb.append(" GSS unitialized");
        } else {
            sb.append("{").append(activeStacks.size()).append("} ");
            for(final Frame f : activeStacks) {
                if(!first) {
                    sb.append(", ");
                }
                sb.append(f.dumpStack());
                first = false;
            }
        }
        return sb.toString();
    }

    private void logParseResult(Link s) {
        if(Tools.debugging) {
            Tools.debug("internal parse tree:\n", s.label);
        }

        if(Tools.tracing) {
            TRACE("SG_ - internal tree: " + s.label);
        }

        if(Tools.measuring) {
            final Measures m = new Measures();
            // Tools.debug("Time (ms): " + (System.currentTimeMillis()-startTime));
            m.setTime(System.currentTimeMillis() - startTime);
            // Tools.debug("Red.: " + reductionCount);
            m.setReductionCount(reductionCount);
            // Tools.debug("Nodes: " + Frame.framesCreated);
            m.setFramesCreated(Frame.framesCreated);
            // Tools.debug("Links: " + Link.linksCreated);
            m.setLinkedCreated(Link.linksCreated);
            // Tools.debug("avoids: " + s.avoidCount);
            m.setAvoidCount(s.recoverCount);
            // Tools.debug("Total Time: " + parseTime);
            m.setParseTime(parseTime);
            // Tools.debug("Total Count: " + parseCount);
            Measures.setParseCount(++parseCount);
            // Tools.debug("Average Time: " + (int)parseTime / parseCount);
            m.setAverageParseTime((int) parseTime / parseCount);
            m.setRecoverTime(-1);
            Tools.setMeasures(m);
        }
    }

    private void logBeforeParsing() {
        if(Tools.tracing) {
            TRACE("SG_Parse() - ");
        }

        if(Tools.debugging) {
            Tools.debug("parse() - ", dumpActiveStacks());
        }
    }

    private void logAfterParsing() throws BadTokenException, TokenExpectedException {
        if(isLogging()) {
            Tools.logger("Number of lines: ", lineNumber);
            Tools.logger("Maximum ", maxBranches, " parse branches reached at token ", logCharify(maxToken), ", line ",
                maxLine, ", column ", maxColumn, " (token #", maxTokenNumber, ")");

            final long elapsed = System.currentTimeMillis() - startTime;
            Tools.logger("Parse time: " + elapsed / 1000.0f + "s");
        }

        if(Tools.debugging) {
            Tools.debug("Parsing complete: all tokens read");
        }

        if(Tools.debugging) {
            Tools.debug("Accepting stack exists");
        }
    }

    private void logCurrentToken() {
        if(isLogging()) {
            Tools.logger("Current token (#", tokensSeen, "): ", logCharify(currentToken.getToken()));
        }
    }

    private void logAfterShifter() {
        if(Tools.tracing) {
            TRACE("SG_AfterShift() - ");
            TRACE_ActiveStacks();
        }
    }

    private void logBeforeShifter() {
        if(Tools.tracing) {
            TRACE("SG_Shifter() - ");
            TRACE_ActiveStacks();
        }

        if(Tools.logging) {
            Tools.logger("#", tokensSeen, ": shifting ", forShifter.size(), " parser(s) -- token ",
                logCharify(currentToken.getToken()), ", line ", lineNumber, ", column ", columnNumber);
        }

        if(Tools.debugging) {
            Tools.debug("shifter() - " + dumpActiveStacks());

            Tools.debug(" token   : " + currentToken);
            Tools.debug(" parsers : " + forShifter.size());
        }
    }

    private void logBeforeParseCharacter() {
        if(Tools.tracing) {
            TRACE("SG_ParseToken() - " + this.currentToken);
            TRACE(" # active stacks : " + activeStacks.size());
        }

        if(Tools.debugging) {
            Tools.debug("parseCharacter() - " + dumpActiveStacks());
            Tools.debug(" # active stacks : " + activeStacks.size());
        }

        /* forActor = */// computeStackOfStacks(activeStacks);

        if(Tools.debugging) {
            Tools.debug(" # for actor     : " + forActor.size());
        }
    }

    private void logBeforeNextParseStep() {
        if(Tools.tracing) {
            TRACE("SG_NextParseStep() - " + dumpActiveStacks());
            TRACE(" # active stacks : " + activeStacks.size());
        }
    }

    private String logCharify(int currentToken) {
        switch(currentToken) {
            case 32:
                return "\\32";
            case SGLR.EOF:
                return "EOF";
            case '\n':
                return "\\n";
            case 0:
                return "\\0";
            default:
                return "" + (char) currentToken;
        }
    }

    @SuppressWarnings("all") private void logBeforeActor(Frame st, State s) {
        List<ActionItem> actionItems = null;

        if(Tools.debugging || Tools.tracing) {
            actionItems = s.getActionItems(currentToken.getToken());
        }

        if(Tools.tracing) {
            TRACE("SG_Actor() - State " + st.state.stateNumber);
            TRACE_ActiveStacks();
        }

        if(Tools.debugging) {
            Tools.debug("actor() - ", dumpActiveStacks());
        }

        if(Tools.debugging) {
            Tools.debug(" state   : ", s.stateNumber);
            Tools.debug(" token   : ", currentToken);
        }

        if(Tools.debugging) {
            Tools.debug(" actions : ", actionItems);
        }

        if(Tools.tracing) {
            TRACE("SG_ - #actions: " + actionItems.size());
        }
    }

    private void logAfterDoReductions() {
        if(Tools.debugging) {
            Tools.debug("<doReductions() - " + dumpActiveStacks());
        }

        if(Tools.tracing) {
            TRACE("SG_ - doreductions done");
        }
    }

    private void logReductionPath(Production prod, Path path, Frame st0, State next) {
        if(Tools.debugging) {
            Tools.debug(" path: ", path);
            Tools.debug(st0.state);
        }

        if(Tools.logging) {
            Tools.logger("Goto(", st0.peek().stateNumber, ",", prod.label + ") == ", next.stateNumber);
        }
    }

    private void logBeforeDoReductions(Frame st, Production prod, final int pathsCount) {
        if(Tools.tracing) {
            TRACE("SG_DoReductions() - state " + st.state.stateNumber + ", #paths= " + pathsCount);
        }

        if(Tools.debugging) {
            Tools.debug("doReductions() - " + dumpActiveStacks());
            logReductionInfo(st, prod);
            Tools.debug(" paths : " + pathsCount);
        }
    }

    private void logBeforeLimitedReductions(Frame st, Production prod, Link l, PooledPathList paths) {
        if(Tools.tracing) {
            TRACE("SG_ - back in reducer ");
            TRACE_ActiveStacks();
            TRACE("SG_DoLimitedReductions() - " + st.state.stateNumber + ", " + l.parent.state.stateNumber);
        }

        if(Tools.debugging) {
            Tools.debug("doLimitedReductions() - ", dumpActiveStacks());
            logReductionInfo(st, prod);
            Tools.debug(Arrays.asList(paths));
        }
    }

    private void logReductionInfo(Frame st, Production prod) {
        Tools.debug(" state : ", st.peek().stateNumber);
        Tools.debug(" token : ", currentToken);
        Tools.debug(" label : ", prod.label);
        Tools.debug(" arity : ", prod.arity);
        Tools.debug(" stack : ", st.dumpStack());
    }

    private void logAddedLink(Frame st0, Frame st1, Link nl) {
        if(Tools.debugging) {
            Tools.debug(" added link ", nl, " from ", st1.state.stateNumber, " to ", st0.state.stateNumber);
        }

        if(Tools.tracing) {
            TRACE_ActiveStacks();
        }
    }

    private void logBeforeReducer(State s, Production prod, int length) {
        if(Tools.tracing) {
            TRACE("SG_Reducer() - next state " + s.stateNumber + ", length " + length + ", applying production "
                + prod.label);
            TRACE_ActiveStacks();
        }

        if(Tools.logging) {
            Tools.logger("Reducing; state ", s.stateNumber, ", token: ", logCharify(currentToken.getToken()),
                ", production: ", prod.label);
        }

        if(Tools.debugging) {
            Tools.debug("reducer() - ", dumpActiveStacks());

            Tools.debug(" state      : ", s.stateNumber);
            Tools.debug(" token      : ", logCharify(currentToken.getToken()) + " (" + currentToken + ")");
            Tools.debug(" production : ", prod.label);
        }
    }

    private void TRACE_ActiveStacks() {
        TRACE("SG_ - #active stacks: " + activeStacks.size());
        TRACE("SG_ - #for_actor stacks: " + forActor.size());
        TRACE("SG_ - #for_actor_delayed stacks: " + forActorDelayed.size());
    }

    private void logAmbiguity(Frame st0, Production prod, Frame st1, Link nl) {
        if(Tools.logging) {
            Tools.logger("Ambiguity: direct link ", st0.state.stateNumber, " -> ", st1.state.stateNumber, " ",
                (prod.isRejectProduction() ? "{reject}" : ""));
            if(nl.label.isParseNode()) {
                Tools.logger("nl is ", nl.isRejected() ? "{reject}" : "", " for ", ((ParseNode) nl.label).getLabel());
            }
        }

        if(Tools.debugging) {
            Tools.debug("createAmbiguityCluster - ", tokensSeen - nl.getLength() - 1, "/", nl.getLength());
        }
    }

    public AbstractParseNode getParseTree() {
        return parseTree;
    }

    private Frame checkImmediateAcceptance(String startSymbol) throws InterruptedException {
        if(acceptingStack == null) {
            int tmpToken = currentToken.getToken();
            int tmpTokenOffset = currentToken.getOffset();
            ArrayDeque<Frame> tmpActiveStacks = new ArrayDeque<Frame>(activeStacks);
            ArrayDeque<Frame> tmpForActor = new ArrayDeque<Frame>(forActor);

            currentToken.setToken(256);
            currentToken.setOffset(Integer.MAX_VALUE);
            ; // EOF

            try {
                parseCharacter();
            } finally {
                currentToken.setToken(tmpToken);
                currentToken.setOffset(tmpTokenOffset);
                activeStacks.clear();
                activeStacks.addAll(tmpActiveStacks);
                forActor.clear();
                forActor.addAll(tmpForActor);
            }
        }

        // if we now have an accepting stack
        if(acceptingStack != null) {
            Object node = null;
            try {
                node = disambiguator.applyTopSortFilter(startSymbol, acceptingStack.findDirectLink(startFrame).label);
            } catch(SGLRException e) {
                // ignore here
            }
            if(node == null)
                acceptingStack = null;
        }

        Frame result = acceptingStack;
        acceptingStack = null;
        return result;
    }

    public boolean getApplyCompletionProd() {
        // TODO Auto-generated method stub
        return this.applyCompletionProd;
    }

    public boolean getReadNonLayout() {
        // TODO Auto-generated method stub
        return this.readNonLayout;
    }

    public void setUseStructureRecovery(boolean useRecovery) {
        this.useIntegratedRecovery = useRecovery;
        this.recoverIntegrator = new RecoveryConnector(this);
    }

    public void setUseStructureRecovery(boolean useRecovery, IntegratedRecoverySettings settings,
        FineGrainedSetting fgSettings) {
        this.useIntegratedRecovery = useRecovery;
        this.recoverIntegrator = new RecoveryConnector(this, settings, fgSettings);
    }

    public boolean isNewCompletionMode() {
        return isNewCompletionMode;
    }

    public void setNewCompletionMode(boolean isNewCompletionMode) {
        this.isNewCompletionMode = isNewCompletionMode;
    }

    public ParserHistory getHistory() {
        return history;
    }

    public int getParserLocation() {
        return this.getHistory().getTokenIndex(); // should also work in recover mode
    }

    public void setReadNonLayout(boolean readNonLayout) {
        this.readNonLayout = readNonLayout;
    }

    public void setApplyCompletionProd(boolean applyCompletionProd) {
        this.applyCompletionProd = applyCompletionProd;
    }

}
