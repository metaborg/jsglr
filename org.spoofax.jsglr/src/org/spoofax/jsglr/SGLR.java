/*
 * Created on 03.des.2005
 *
 * Copyright (c) 2005, Karl Trygve Kalleberg <karltk near strategoxt.org>
 * 
 * Licensed under the GNU Lesser General Public License, v2.1
 */
package org.spoofax.jsglr;

import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CancellationException;

import org.spoofax.ArrayDeque;

import aterm.ATerm;
import aterm.ATermFactory;
import aterm.pure.PureFactory;

public class SGLR {              

    // FIXME: Should probably be put elsewhere
    static final int EOF = 256;
    
    private static final int TAB_SIZE = 8;

    protected static boolean WORK_AROUND_MULTIPLE_LOOKAHEAD;
    
    //Performance testing
    private static long parseTime=0;
    private static int parseCount=0;
        
    //handles avoid and recover productions in an efficient way
    private IRecoverAlgorithm recoverHandler;
        
    private Frame startFrame; 
    
    private long startTime;
    
    private Timer abortTimer;
    
    private volatile boolean asyncAborted;
    
    private ATermFactory factory;

    protected Frame acceptingStack;

    protected ArrayDeque<Frame> activeStacks;

    private ParseTable parseTable;

    protected int currentToken;

    protected int tokensSeen;

    protected int lineNumber;

    protected int columnNumber;

    private ArrayDeque<ActionState> forShifter;

    private ArrayDeque<Frame> forActor;

    private ArrayDeque<Frame> forActorDelayed;

    private int maxBranches;

    private int maxToken;

    private int maxLine;

    private int maxColumn;

    private int maxTokenNumber;

    private AmbiguityManager ambiguityManager;

    private boolean detectCycles;

    private boolean filter;

    private Disambiguator postFilter;

    private boolean rejectFilter;

    private boolean associtivityFilter;

    private boolean priorityFilter;

    private boolean injectionCountFilter;

    private int rejectCount;

    private int reductionCount;

    private PushbackInputStream currentInputStream;

    SGLR() {
        basicInit(null);
    }

    public SGLR(final ATermFactory pf, ParseTable parseTable) {
        assert pf != null;
        assert parseTable != null;

        // Init with a new factory for both serialized or BAF instances.
        basicInit(pf);

        this.parseTable = parseTable;
        recoverHandler = new BackTrackRecovery2(this);
        //recoverHandler = new SimpleRecovering(this);
        //recoverHandler = new NoRecovery();
    }
    
    @Deprecated
    /**
     * @deprecated Use setRecoverHandler() instead.
     */
    public void withBacktracking(boolean withBT) {
        if (withBT) {
            recoverHandler = new BackTrackRecovery2(this);
        } else {
            recoverHandler = new NoRecovery();
        }
    }
    
    /**
     * Aborts an asynchronously running parse job, causing it to throw an exception.     * 
     * (Provides no guarantee that the parser is actually cancelled.)
     */
    public void asyncAbort() {
        asyncAborted = true;
    }
    
    public void setRecoverHandler(IRecoverAlgorithm recoverHandler) {
        this.recoverHandler = recoverHandler;
    }
    
    public IRecoverAlgorithm getRecoverHandler() {
        return recoverHandler;
    }

    public static void forceGC() {        
        System.gc();
        try {
            Thread.sleep(200); // Allows gc to do its work.
        } catch (InterruptedException e) {
            throw new Error(e);
        }
    }

    private void basicInit(ATermFactory factory) {
        detectCycles = true;
        this.factory = factory;
        if (factory == null)
            factory = new PureFactory();
        activeStacks = new ArrayDeque<Frame>();     
        forActor = new ArrayDeque<Frame>();
        forActorDelayed = new ArrayDeque<Frame>();
        forShifter = new ArrayDeque<ActionState>();

        // FIXME This is *wrong*
        ambiguityManager = new AmbiguityManager(10000);

        // FIXME filter flags should probably be moved to PostFilter class
        filter = true;
        rejectFilter = true;
        injectionCountFilter = true;
        priorityFilter = true;
        associtivityFilter = true;

        postFilter = new Disambiguator(this);
    }

    public static boolean isDebugging() {
        return Tools.debugging;
    }

    public static boolean isLogging() {
        return Tools.logging;
    }

    /**
     * Initializes the active stacks. At the start of parsing there is only one
     * active stack, and this stack contains the start symbol obtained from the
     * parse table.
     * 
     * @return the initial stack
     */
    private Frame initActiveStacks() {
        activeStacks.clear();
        Frame st0 = newStack(parseTable.getInitialState());
        addStack(st0);
        return st0;
    }
    
    public ATerm parse(InputStream fis)  throws IOException, SGLRException {        
        return parse(fis, null);
    }   
    
    public ATerm parse(InputStream fis, String startSymbol) throws IOException,
            BadTokenException, TokenExpectedException, ParseException,
            SGLRException {
        logBeforeParsing();        
        initParseVariables(fis);
        recoverHandler.initialize();
        startTime = System.currentTimeMillis();
        
        initParseTimer();
        
        //long beginParse = System.currentTimeMillis(); //MJ:testing
        return sglrParse(startSymbol);
    }
    
    private void initParseTimer() {
        asyncAborted = false;
        if (Tools.timeout > 0) {
            if (abortTimer != null)
                abortTimer.cancel();
            abortTimer = new Timer();
            abortTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    asyncAbort();
                }
            }, Tools.timeout
            );
        }
    }

    private ATerm sglrParse(String startSymbol)
            throws IOException, BadTokenException, TokenExpectedException,
            ParseException, SGLRException {        
        
        try {
            do {
                readNextToken(); 
                recoverHandler.afterStreamRead(currentToken);
                doParseStep();
                recoverHandler.afterParseStep();
                            
            } while (currentToken != SGLR.EOF && activeStacks.size() > 0);
            
            if(recoverHandler.meetsRecoverCriteria()){
                recoverHandler.recover();                                
                return sglrParse(startSymbol);
            }
        } catch (CancellationException e) {
            throw new ParseTimeoutException(currentToken, tokensSeen - 1, lineNumber,
                    columnNumber);
        }
            
        //MJ:testing
        long parseInterval = System.currentTimeMillis() - startTime;
        parseTime +=parseInterval;
        parseCount +=1;
        
        logAfterParsing();    
        
        Link s = acceptingStack.findDirectLink(startFrame);

        if (s == null)
            throw new ParseException("Accepting stack has no link");

        logParseResult(s);        
        return postFilter.applyFilters(s.label, startSymbol, tokensSeen);
    }

    private void readNextToken() throws IOException {
        logCurrentToken();            
        currentToken = getNextToken();    
    }

    protected void doParseStep() throws IOException {               
        parseCharacter(); //applies reductions on active stack structure and fills forshifter                      
        shifter(); //renewes active stacks with states in forshifter
    }    

    private void initParseVariables(InputStream fis) {        
        startFrame = initActiveStacks();
        tokensSeen = 0;
        columnNumber = 0;
        lineNumber = 1;        
        currentInputStream = new PushbackInputStream(fis, 1024);
        acceptingStack = null;
    }    

    private void reportInvalidToken(Frame singlePreviousStack)
            throws BadTokenException, TokenExpectedException {
        if (singlePreviousStack != null) {
            Action action = singlePreviousStack.peek().getSingularAction();
            
            if (action != null && action.getActionItems().length == 1) {
                StringBuilder expected = new StringBuilder();
                
                do {
                    int token = action.getSingularRange();
                    if (token == -1) break;
                    expected.append((char) token);
                    
                    ActionItem[] items = action.getActionItems();
                    
                    if (!(items.length == 1 && items[0].type == ActionItem.SHIFT))
                        break;
                    
                    Shift shift = (Shift) items[0];
                    action = parseTable.getState(shift.nextState).getSingularAction();
                                        
                } while (action != null);

                if (expected.length() > 0)
                    throw new TokenExpectedException(expected.toString(), currentToken,
                                                     tokensSeen - 1, lineNumber, columnNumber);
            }
        }
        
        throw new BadTokenException(currentToken, tokensSeen - 1, lineNumber,
                                           columnNumber);
    }

    private void shifter() {
        logBeforeShifter();
        clearActiveStacks(false);

        IParseNode prod = parseTable.lookupProduction(currentToken);

        while (forShifter.size() > 0) {
            ActionState as = forShifter.remove();

            if (!parseTable.hasRejects() || !as.st.allLinksRejected()) {
                Frame st1 = findStack(activeStacks, as.s);
                if (st1 == null) {
                    st1 = newStack(as.s);
                    addStack(st1);
                }
                st1.addLink(as.st, prod, 1);
            } else {
                if (Tools.logging) {
                    Tools.logger("Shifter: skipping rejected stack with state ",
                                 as.st.state.stateNumber);
                }
            }
        }
        logAfterShifter();
    }    

    private void addStack(Frame st1) {
        if(Tools.tracing) {
            TRACE("SG_AddStack() - " + st1.state.stateNumber);
        }
        activeStacks.addFirst(st1);
    }    

    private void parseCharacter() throws IOException {
        logBeforeParseCharacter();

        ArrayDeque<Frame> actives = new ArrayDeque<Frame>();
        actives.addAll(activeStacks); // FIXME avoid garbage        
        clearForActorDelayed(false);
        clearForShifter(false);
        while (actives.size() > 0 || forActor.size() > 0) {
            Frame st;
            st = pickStackNodeFromActivesOrForActor(actives);
            if (!st.allLinksRejected()) {
                actor(st);
            }
            
            if(actives.size() == 0 && forActor.size() == 0) {
                fillForActorWithDelayedFrames(); //Fills foractor, clears foractor delayed
            }
                
        }
    }

    private void fillForActorWithDelayedFrames() {
        if(Tools.tracing) {
            TRACE("SG_ - both empty");
        }
        forActor = forActorDelayed;
        forActorDelayed = new ArrayDeque<Frame>(); // FIXME: avoid garbage
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

    private void actor(Frame st) throws IOException {
        State s = st.peek();
        logBeforeActor(st, s);        
        for (Action action : s.getActions()) {
            if (action.accepts(currentToken)) {
                for (ActionItem ai : action.getActionItems()) {
                    switch (ai.type) {
                        case ActionItem.SHIFT: {
                            Shift sh = (Shift) ai;
                            addShiftPair(new ActionState(st, parseTable.getState(sh.nextState))); //Adds StackNode to forshifter
                            statsRecordParsers(); //sets some values un current parse state
                            break;
                        }
                        case ActionItem.REDUCE: {
                            Reduce red = (Reduce) ai;
                            doReductions(st, red.production);
                            break;
                        }
                        case ActionItem.REDUCE_LOOKAHEAD: {
                            ReduceLookahead red = (ReduceLookahead) ai;
                            if(checkLookahead(red)) {
                                if(Tools.tracing) {
                                    TRACE("SG_ - ok");
                                }
                                doReductions(st, red.production);
                            }
                            break;
                        }
                        case ActionItem.ACCEPT: {
                            if (!st.allLinksRejected()) {
                                acceptingStack = st;
                                if (Tools.logging) {
                                    Tools.logger("Reached the accept state");
                                }
                            }
                            break;
                        }
                        default:
                            throw new NotImplementedException();
                     }
                }
            }
        }
        
        if(Tools.tracing) {
            TRACE("SG_ - actor done");
        }
    }    

    private boolean checkLookahead(ReduceLookahead red) throws IOException {
        return doCheckLookahead(red, red.getCharClasses(), 0);
    }
    
    private boolean doCheckLookahead(ReduceLookahead red, Range[] charClass, int pos) throws IOException {
        if(Tools.tracing) {
            TRACE("SG_CheckLookAhead() - ");
        }
        
        int c = currentInputStream.read();
        
        // EOF
        if(c == -1) 
            return true;
        
        boolean permit = true;
        
        if(pos < charClass.length)
            permit = charClass[pos].within(c) ? false : doCheckLookahead(red, charClass, pos + 1);

        currentInputStream.unread(c);
        return permit;
    }

    private void addShiftPair(ActionState state) {
        if(Tools.tracing) {
            TRACE("SG_AddShiftPair() - " + state.s.stateNumber);
        }
        forShifter.add(state);
    }

    private void statsRecordParsers() {
        if (forShifter.size() > maxBranches) {
            maxBranches = forShifter.size();
            maxToken = currentToken;
            maxColumn = columnNumber;
            maxLine = lineNumber;
            maxTokenNumber = tokensSeen;
        }
    }

    private void doReductions(Frame st, Production prod) throws IOException {
        List<Path> paths = st.findAllPaths(prod.arity);         
        logBeforeDoReductions(st, prod, paths.size());
        reduceAllPaths(prod, paths);
        logAfterDoReductions();
    }
    
    private void doLimitedReductions(Frame st, Production prod, Link l) throws IOException { //Todo: Look add sharing code with doReductions
        List<Path> paths = st.findLimitedPaths(prod.arity, l); //find paths containing the link         
        logBeforeLimitedReductions(st, prod, l, paths);        
        reduceAllPaths(prod, paths);
    }

    private void reduceAllPaths(Production prod, List<Path> paths)
            throws IOException {
        
        for (int i = paths.size() - 1; i >= 0; i--) {
            Path path = paths.get(i);
            List<IParseNode> kids = path.getATerms();
            Frame st0 = path.getEnd();
            State next = parseTable.go(st0.peek(), prod.label);
            logReductionPath(prod, path, kids, st0, next);
            reducer(st0, next, prod, kids, path);
        }
        clearPath(paths);
        
        if (asyncAborted) {
            // Rethrown as ParseTimeoutException in SGLR.sglrParse()
            throw new CancellationException("Long-running parse job aborted");
        }
    }

    
    private void clearPath(List<Path> paths) {
        if(Tools.tracing) {
            SGLR.TRACE("SG_ClearPath() - " + paths.size());
        }
        paths.clear();
    }

    private void reducer(Frame st0, State s, Production prod, List<IParseNode> kids, Path path) throws IOException {
        int length = path.getLength();        
        int numberOfAvoids = calcAvoidCount(prod, path);         
        IParseNode t = prod.apply(kids);
        Frame st1; 
        Link nl;
        if(prod.status == Reduce.AVOID)
        {
            recoverHandler.handleRecoverProduction(st0, s, length, numberOfAvoids, t);
            if(recoverHandler.haltsOnRecoverProduction(st0)){
                return;
            }
        }        
        logBeforeReducer(s, prod, length);
        increaseReductionCount();        
        st1 = findStack(activeStacks, s);
        if (st1 == null) {
            addNewStack(st0, s, prod, length, numberOfAvoids, t);            
        } else {
            /* A stack with state s exists; check for ambiguities */
            nl = st1.findDirectLink(st0);

            if (nl != null) {
                logAmbiguity(st0, prod, st1, nl);       
                handleAmbiguity(prod, length, numberOfAvoids, t, st1, nl);

            } else {
                nl = st1.addLink(st0, t, length);
                nl.avoidCount = numberOfAvoids;
                if (prod.isReject())
                    nl.reject();                
                logAddedLink(st0, st1, nl);              
                actorOnActiveStacksOverNewLink(nl);
            }
        }
        if(Tools.tracing) {
            TRACE_ActiveStacks();
            TRACE("SG_ - reducer done");
        }
    }

    private void handleAmbiguity(Production prod, int length,
            int numberOfAvoids, IParseNode t, Frame st1, Link nl) {
        ///*
        //Todo: avoid / recover different implementation
        if(!prod.isReject() && lessAvoidsInLink(numberOfAvoids, nl))//??: what about this rejected attribute
        {                                        
            //nl=new Link(st0, t, length); //choose the best path
            nl.avoidCount = numberOfAvoids;
            nl.label = t;
            nl.length = length;    
            forActorDelayed.addFirst(st1); //The avoidcount for reductions based on this node must be recalculated                                    
        }
        /*
        else {
            //ambiguity without avoid (recover) productions
            if(numberOfAvoids == 0){
                nl.addAmbiguity(t, tokensSeen);
                ambiguityManager.increaseAmbiguityCalls();
            }
        }
        */ 
        //Ambiguity filter gives problems
        //*/
        ///////////////MJ: Old Code. Question: ambiguity, one production rejected, one not??
        //if (prod.isReject()) {
        //    nl.reject();
        //}
        //nl.addAmbiguity(t, tokensSeen);
        //ambiguityManager.increaseAmbiguityCalls();
    }

    boolean lessAvoidsInLink(int numberOfAvoids,
            Link nl) {
        return (numberOfAvoids < nl.avoidCount || nl.isRejected());
    }

    private void addNewStack(Frame st0, State s, Production prod, int length,
            int numberOfAvoids, IParseNode t) {
        Frame st1;
        Link nl;
        /* Found no existing stack with for state s; make new stack */
        st1 = newStack(s);            
        nl = st1.addLink(st0, t, length);
        nl.avoidCount = numberOfAvoids;   
        addStack(st1);            
        forActorDelayed.addFirst(st1);
        if(Tools.tracing) {
            TRACE("SG_AddStack() - " + st1.state.stateNumber);
        }
        if (prod.isReject()) {
            if (Tools.logging) {
                Tools.logger("Reject [new]");
            }
            nl.reject();
            increaseRejectCount();
        }
    }  

    private void actorOnActiveStacksOverNewLink(Link nl) throws IOException {
        // Note: ActiveStacks can be modified inside doLimitedReductions
        // new elements may be inserted at the beginning
        final int sz = activeStacks.size();
        for (int i = 0; i < sz; i++) {
//                for(Frame st2 : activeStacks) {
            if(Tools.tracing) {
                TRACE("SG_ activeStack - ");
            }
            int pos = activeStacks.size() - sz + i;
            Frame st2 = activeStacks.get(pos);
            boolean b0 = st2.allLinksRejected();
            boolean b1 = inReduceStacks(forActor, st2);
            boolean b2 = inReduceStacks(forActorDelayed, st2);
            if (b0 || b1 || b2)
                continue; //stacknode will find reduction in regular process

            for (Action action : st2.peek().getActions()) {
                if (action.accepts(currentToken)) {
                    for (ActionItem ai : action.getActionItems()) {                  
                        switch(ai.type) {
                            case ActionItem.REDUCE:
                                Reduce red = (Reduce) ai;
                                doLimitedReductions(st2, red.production, nl);                           
                                break;
                            case ActionItem.REDUCE_LOOKAHEAD:
                                ReduceLookahead red2 = (ReduceLookahead) ai;                         
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

    private int calcAvoidCount(Production prod, Path path) {
        int numberOfAvoids = path.getAvoidCount();
        if(prod.status == Reduce.AVOID)
        {
            numberOfAvoids +=1;
        }
        return numberOfAvoids;
    }
    
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
        if(Tools.tracing) {
            TRACE("SG_FindStack() - " + s.stateNumber);
        }

        // We need only check the top frames of the active stacks.
        if (Tools.debugging) {
            Tools.debug("findStack() - ", dumpActiveStacks());
            Tools.debug(" looking for ", s.stateNumber);
        }

        final int size = stacks.size();
        for (int i = 0; i < size; i++) {
            if (stacks.get(i).state.stateNumber == s.stateNumber) {
                if(Tools.tracing) {
                    TRACE("SG_ - found stack");
                }
                return stacks.get(i);
            }
        }
        if(Tools.tracing) {
            TRACE("SG_ - stack not found");
        }
        return null;
    }     
   

    private int getNextToken() throws IOException {
        if(Tools.tracing) {
            TRACE("SG_NextToken() - ");
        }

        int t = currentInputStream.read();
        updateParserFields(t);
        if(t==-1)
            return SGLR.EOF;
        return t;
    }

    protected void updateParserFields(int t) {
        tokensSeen++;

        if (Tools.debugging) {
            Tools.debug("getNextToken() - ", t, "(", (char) t, ")");
        }

        switch (t) {
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

    static int num = 0;
    
    public void setCycleDetect(boolean detectCycles) {
        this.detectCycles = detectCycles;
    }

    public boolean isDetectCyclesEnabled() {
        return filter && detectCycles;
    }

    public void setFilter(boolean filter) {
        this.filter = filter;
    }

    public void clear() {
        if (this.acceptingStack != null) {
            this.acceptingStack.clear();
        }

        clearActiveStacks(true);
        clearForActorDelayed(true);
        clearForActor(true);
        clearForShifter(true);

        this.parseTable = null;
        this.factory = null;
        this.ambiguityManager = null; // todo clear
    }

    private void clearForShifter(boolean all) {
        if (all) {
            for (ActionState as : forShifter) {
                as.clear(all);
            }
        }
        this.forShifter.clear();
    }

    private void clearForActor(boolean all) {
        if (all) {
            for (Frame frame : forActor) {
                frame.clear();
            }
        }
        forActor.clear();
    }

    private void clearForActorDelayed(boolean all) {
        if (all) {
            for (Frame frame : forActorDelayed) {
                frame.clear();
            }
        }
        forActorDelayed.clear();
    }

    private void clearActiveStacks(boolean all) {
        if (all) {
            for (Frame frame : activeStacks) {
                frame.clear();
            }
        }
        activeStacks.clear();
    }

    public boolean isFilteringEnabled() {
        return filter;
    }

    ParseTable getParseTable() {
        return parseTable;
    }

    AmbiguityManager getAmbiguityManager() {
        return ambiguityManager;
    }

    public boolean isRejectFilterEnabled() {
        return filter && rejectFilter;
    }

    public boolean isAssociativityFilterEnabled() {
        return filter && associtivityFilter;
    }

    public boolean isPriorityFilterEnabled() {
        return filter && priorityFilter;
    }

    public ATermFactory getFactory() {
        return factory;
    }

    public boolean isInjectionCountFilterEnabled() {
        return filter && injectionCountFilter;
    }

    public int getReductionCount() {
        return reductionCount;
    }

    public int getRejectionCount() {
        return rejectCount;
    }
    
    public static void setWorkAroundMultipleLookahead(boolean value) {
        WORK_AROUND_MULTIPLE_LOOKAHEAD = value;
    }   
       
    
    ////////////////////////////////////////////////////// Log functions ///////////////////////////////////////////////////////////////////////////////
    
    static void TRACE(String string) {
        System.err.println("[" + num + "] " + string);
        num++;
    }

    private String dumpActiveStacks() {
        StringBuffer sb = new StringBuffer();
        boolean first = true;
        if (activeStacks == null) {
            sb.append(" GSS unitialized");
        } else {
            sb.append("{").append(activeStacks.size()).append("} ");
            for (Frame f : activeStacks) {
                if (!first)
                    sb.append(", ");
                sb.append(f.dumpStack());
                first = false;
            }
        }
        return sb.toString();
    }

    
    private void logParseResult(Link s) {
        if (isDebugging()) {
            Tools.debug("internal parse tree:\n", s.label);
        }

        if(Tools.tracing) {
            TRACE("SG_ - internal tree: " + s.label);
        }       
                
        if (Tools.measuring) {
            Measures m = new Measures();
            //Tools.debug("Time (ms): " + (System.currentTimeMillis()-startTime));
            m.setTime(System.currentTimeMillis() - startTime);
            //Tools.debug("Red.: " + reductionCount);
            m.setReductionCount(reductionCount);
            //Tools.debug("Nodes: " + Frame.framesCreated);
            m.setFramesCreated(Frame.framesCreated);
            //Tools.debug("Links: " + Link.linksCreated);
            m.setLinkedCreated(Link.linksCreated);
            //Tools.debug("avoids: " + s.avoidCount);
            m.setAvoidCount(s.avoidCount);
            //Tools.debug("Total Time: " + parseTime);
            m.setParseTime(parseTime);
            //Tools.debug("Total Count: " + parseCount);
            m.setParseCount(parseCount);
            //Tools.debug("Average Time: " + (int)parseTime / parseCount);
            m.setAverageParseTime((int)parseTime / parseCount);
            m.setRecoverTime(recoverHandler.getRecoverTime());
            Tools.setMeasures(m);
        }
    }
    

    private void logBeforeParsing() {
        if(Tools.tracing) {
            TRACE("SG_Parse() - ");
        }

        if (Tools.debugging) {
            Tools.debug("parse() - ", dumpActiveStacks());
        }
    }
    
    private void logAfterParsing()
            throws BadTokenException, TokenExpectedException {
        Frame singlePreviousStack;
        singlePreviousStack = activeStacks.size() == 1
        ? activeStacks.get(0)
        : null;
        if (isLogging()) {
            Tools.logger("Number of lines: ", lineNumber);
            Tools.logger("Maximum ", maxBranches, " parse branches reached at token ",
                         logCharify(maxToken), ", line ", maxLine, ", column ", maxColumn,
                         " (token #", maxTokenNumber, ")");

            long elapsed = System.currentTimeMillis() - startTime;
            Tools.logger("Parse time: " + elapsed / 1000.0f + "s");
        }

        if (isDebugging()) {
            Tools.debug("Parsing complete: all tokens read");
        }

        if (acceptingStack == null) {
            reportInvalidToken(singlePreviousStack);
        }

        if (isDebugging()) {
            Tools.debug("Accepting stack exists");
        }
    }

    private void logCurrentToken() {
        if (isLogging()) {
            Tools.logger("Current token (#", tokensSeen, "): ", logCharify(currentToken));
        }
    }
    
    private void logAfterShifter() {
        if(Tools.tracing) {
            TRACE("SG_DiscardShiftPairs() - ");
            TRACE_ActiveStacks();
        }
    }

    private void logBeforeShifter() {
        if(Tools.tracing) {
            TRACE("SG_Shifter() - ");
            TRACE_ActiveStacks();
        }
        
        if (Tools.logging) {
            Tools.logger("#", tokensSeen, ": shifting ", forShifter.size(), " parser(s) -- token ",
                         logCharify(currentToken), ", line ", lineNumber, ", column ", columnNumber);
        }

        if (Tools.debugging) {
            Tools.debug("shifter() - " + dumpActiveStacks());

            Tools.debug(" token   : " + currentToken);
            Tools.debug(" parsers : " + forShifter.size());
        }
    }
    
    private void logBeforeParseCharacter() {
        if(Tools.tracing) {
            TRACE("SG_ParseToken() - ");
        }

        if (Tools.debugging) {
            Tools.debug("parseCharacter() - " + dumpActiveStacks());
            Tools.debug(" # active stacks : " + activeStacks.size());
        }

        /* forActor = *///computeStackOfStacks(activeStacks);

        if (Tools.debugging) {
            Tools.debug(" # for actor     : " + forActor.size());
        }
    }
    
    private String logCharify(int currentToken) {
        switch (currentToken) {
        case 32:
            return "\\32";
        case 256:
            return "EOF";
        case '\n':
            return "\\n";
        case 0:
            return "\\0";
        default:
            return "" + (char) currentToken;
        }
    }
    
    private void logBeforeActor(Frame st, State s) {
        List<ActionItem> actionItems = null;
        
        if (Tools.debugging || Tools.tracing) {
            actionItems = s.getActionItems(currentToken);
        }
        
        if(Tools.tracing) {
            TRACE("SG_Actor() - " + st.state.stateNumber);
            TRACE_ActiveStacks();
        }
        
        if (Tools.debugging) {
            Tools.debug("actor() - ", dumpActiveStacks());
        }       

        if (Tools.debugging) {
            Tools.debug(" state   : ", s.stateNumber);
            Tools.debug(" token   : ", currentToken);
        }        

        if (Tools.debugging) {
            Tools.debug(" actions : ", actionItems);
        }
        
        if(Tools.tracing) {
            TRACE("SG_ - actions: " + actionItems.size());
        }
    }
    
    private void logAfterDoReductions() {
        if (Tools.debugging) {
            Tools.debug("<doReductions() - " + dumpActiveStacks());
        }
        
        if(Tools.tracing) {
            TRACE("SG_ - doreductions done");
        }
    }

    private void logReductionPath(Production prod, Path path,
            List<IParseNode> kids, Frame st0, State next) {
        if (Tools.debugging) {
            Tools.debug(" path: ", path);
            Tools.debug(" kids: ", kids);        
            Tools.debug(st0.state);
        }            

        if (Tools.logging) {
            Tools.logger("Goto(", st0.peek().stateNumber, ",", prod.label + ") == ",
                         next.stateNumber);
        }
    }   
    

    private void logBeforeDoReductions(Frame st, Production prod,
            final int pathsCount) {
        if(Tools.tracing) {
            TRACE("SG_DoReductions() - " + st.state.stateNumber);
        }

        if (Tools.debugging) {
            Tools.debug("doReductions() - " + dumpActiveStacks());
            logReductionInfo(st, prod);       
            Tools.debug(" paths : " + pathsCount);
        }
    }
    
    private void logBeforeLimitedReductions(Frame st, Production prod, Link l,
            List<Path> paths) {
        if(Tools.tracing) {
            TRACE("SG_ - back in reducer ");
            TRACE_ActiveStacks();
            TRACE("SG_DoLimitedReductions() - " + st.state.stateNumber + ", " + l.parent.state.stateNumber);
        }

        if (Tools.debugging) {
            Tools.debug("doLimitedReductions() - ", dumpActiveStacks());
            logReductionInfo(st, prod);
            List<?> reversePaths = (List<?>) ((ArrayList<?>) paths).clone();
            Collections.reverse(reversePaths);
            Tools.debug(reversePaths);
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
        if (Tools.debugging) {
            Tools.debug(" added link ", nl, " from ", st1.state.stateNumber, " to ",
                        st0.state.stateNumber);
        }               

        if(Tools.tracing) {
            TRACE_ActiveStacks();
        }
    }
    
    private void logBeforeReducer(State s, Production prod, int length) {
        if(Tools.tracing) {
            TRACE("SG_Reducer() - " + s.stateNumber + ", " + length + ", " + prod.label);
            TRACE_ActiveStacks();
        }

        if (Tools.logging) {
            Tools.logger("Reducing; state ", s.stateNumber, ", token: ", logCharify(currentToken),
                         ", production: ", prod.label);
        }

        if (Tools.debugging) {
            Tools.debug("reducer() - ", dumpActiveStacks());

            Tools.debug(" state      : ", s.stateNumber);
            Tools.debug(" token      : ", logCharify(currentToken) + " (" + currentToken + ")");
            Tools.debug(" production : ", prod.label);
        }
    }

    private void TRACE_ActiveStacks() {
        TRACE("SG_ - active stacks: " + activeStacks.size());
        TRACE("SG_ - for_actor stacks: " + forActor.size());
        TRACE("SG_ - for_actor_delayed stacks: " + forActorDelayed.size());
    }

   
    private void logAmbiguity(Frame st0, Production prod, Frame st1, Link nl) {
        if (Tools.logging) {
            Tools.logger("Ambiguity: direct link ", st0.state.stateNumber, " -> ",
                         st1.state.stateNumber, " ", (prod.isReject() ? "{reject}" : ""));
            if (nl.label instanceof ParseNode) {
                Tools.logger("nl is ", nl.isRejected() ? "{reject}" : "", " for ",
                             ((ParseNode) nl.label).label);
            }
        }

        if (Tools.debugging) {
            Tools.debug("createAmbiguityCluster - ", tokensSeen - nl.getLength() - 1, "/",
                        nl.getLength());
        }
    }    
    //-------------------------------------------------- mj: debug and recovery ------------------------
        
    //Used for debugging
    private String mjInfo() {
        String result = "";
        result += "CURR TOKEN: " + (char)currentToken;
        result += " ACTIVESTACKS: ";
        for (Frame f : activeStacks) {
            result += f.state.stateNumber;
            if(f.minAvoidValue() > 0)
                result += "$"+f.minAvoidValue() + "$";
            result += "; ";            
        }
        result += " FORACTOR: ";
        for (Frame f : forActor) {
            result += f.state.stateNumber;
            result += "; ";
        }
        result += " FORACTOR_DELAYED: ";
        for (Frame f : forActorDelayed) {
            result += f.state.stateNumber;
            result += "; ";
        }
        result += " FORSHIFTER: ";
        for (ActionState as : forShifter) {
            result += "{ ";
            result += as.st.state.stateNumber;
            result+=",";
            result += as.s.stateNumber;            
            result += "} ; ";
        }
        return result;        
    } 
    //debug stack visualisation
    private void watchActiveStacknodes() {
        //mj debug
        for (Frame actNode : activeStacks) {
            String[] testMJ = actNode.getStackRepresentation(true);
            int i = testMJ.length;
        }
    }
    
    private String[] viewStackObject(boolean avoidFiltered){
        List<String> stackPaths = new ArrayList<String>();
        for (Frame actNode : activeStacks) {
            List<String> testMJ = actNode.getStackPaths("", avoidFiltered);
            stackPaths.addAll(testMJ);
        }
        return stackPaths.toArray(new String[stackPaths.size()]);
    }  
    
    private String[] viewStackObject()
    {
        return viewStackObject(false);
    }
    
    private String[] viewFilteredStackObject()
    {
        return viewStackObject(true);
    }
    
    private void mjTesting() {        
        Tools.debug((char)currentToken); 
    }
    
}
