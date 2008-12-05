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
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import aterm.ATerm;
import aterm.ATermFactory;
import aterm.pure.PureFactory;

public class SGLR {

    // FIXME: Should probably be put elsewhere
    static final int EOF = 256;
    
    private static final int TAB_SIZE = 8;

    protected static boolean WORK_AROUND_MULTIPLE_LOOKAHEAD;
    
    private ATermFactory factory;

    private Frame acceptingStack;

    private LinkedList<Frame> activeStacks;

    private ParseTable parseTable;

    private int currentToken;

    private int tokensSeen;

    private int lineNumber;

    private int columnNumber;

    private Queue<ActionState> forShifter;

    private LinkedList<Frame> forActor;

    private LinkedList<Frame> forActorDelayed;

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
        activeStacks = new LinkedList<Frame>();

        forActor = new LinkedList<Frame>();
        forActorDelayed = new LinkedList<Frame>();
        forShifter = new LinkedList<ActionState>();

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
    
    public ATerm parse(InputStream fis, String startSymbol)  throws IOException, TokenExpectedException, BadTokenException, SGLRException {
        if(Tools.tracing) {
            TRACE("SG_Parse() - ");
        }

        if (Tools.debugging) {
            Tools.debug("parse() - ", dumpActiveStacks());
        }

        long start = System.currentTimeMillis();

        tokensSeen = 0;
        columnNumber = 0;
        lineNumber = 1;

        currentInputStream = new PushbackInputStream(fis, 1024);
        
        acceptingStack = null;
        Frame st0 = initActiveStacks();
        Frame singlePreviousStack;

        do {
            if (isLogging()) {
                Tools.logger("Current token (#", tokensSeen, "): ", charify(currentToken));
            }
            
            currentToken = getNextToken();

            singlePreviousStack = activeStacks.size() == 1
                                ? activeStacks.get(0)
                                : null;

            parseCharacter();
            shifter();
            
        } while (currentToken != SGLR.EOF && activeStacks.size() > 0);

        if (isLogging()) {
            Tools.logger("Number of lines: ", lineNumber);
            Tools.logger("Maximum ", maxBranches, " parse branches reached at token ",
                         charify(maxToken), ", line ", maxLine, ", column ", maxColumn,
                         " (token #", maxTokenNumber, ")");

            long elapsed = System.currentTimeMillis() - start;
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

        Link s = acceptingStack.findDirectLink(st0);

        if (s == null)
            throw new ParseException("Accepting stack has no link");

        if (isDebugging()) {
            Tools.debug("internal parse tree:\n", s.label);
        }

        if(Tools.tracing) {
            TRACE("SG_ - internal tree: " + s.label);
        }
        
        return postFilter.applyFilters(s.label, startSymbol, tokensSeen);
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
        if(Tools.tracing) {
            TRACE("SG_Shifter() - ");
            TRACE_ActiveStacks();
        }
        
        if (Tools.logging) {
            Tools.logger("#", tokensSeen, ": shifting ", forShifter.size(), " parser(s) -- token ",
                         charify(currentToken), ", line ", lineNumber, ", column ", columnNumber);
        }

        if (Tools.debugging) {
            Tools.debug("shifter() - " + dumpActiveStacks());

            Tools.debug(" token   : " + currentToken);
            Tools.debug(" parsers : " + forShifter.size());
        }
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
        if(Tools.tracing) {
            TRACE("SG_DiscardShiftPairs() - ");
            TRACE_ActiveStacks();
        }
    }

    private void addStack(Frame st1) {
        if(Tools.tracing) {
            TRACE("SG_AddStack() - " + st1.state.stateNumber);
        }
        activeStacks.addFirst(st1);
    }

    private String charify(int currentToken) {
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

    private void parseCharacter() throws IOException {
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

        LinkedList<Frame> actives = new LinkedList<Frame>();
        actives.addAll(activeStacks); // FIXME avoid garbage
        
        clearForActorDelayed(false);
        clearForShifter(false);

        while (actives.size() > 0 || forActor.size() > 0) {
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

            if (!st.allLinksRejected()) {
                actor(st);
            }
            
            if(actives.size() == 0 && forActor.size() == 0) {
                if(Tools.tracing) {
                    TRACE("SG_ - both empty");
                }
                forActor = forActorDelayed;
                forActorDelayed = new LinkedList<Frame>(); // FIXME: avoid garbage
            }
                
        }
    }

    private void actor(Frame st) throws IOException {
        if(Tools.tracing) {
            TRACE("SG_Actor() - " + st.state.stateNumber);
            TRACE_ActiveStacks();
        }
        
        if (Tools.debugging) {
            Tools.debug("actor() - ", dumpActiveStacks());
        }

        State s = st.peek();

        if (Tools.debugging) {
            Tools.debug(" state   : ", s.stateNumber);
            Tools.debug(" token   : ", currentToken);
        }

        List<ActionItem> actionItems = s.getActionItems(currentToken);

        if (Tools.debugging) {
            Tools.debug(" actions : ", actionItems);
        }
        
        if(Tools.tracing) {
            TRACE("SG_ - actions: " + actionItems.size());
        }
        
        for (ActionItem ai : actionItems) {
        	switch(ai.type) {
        	case ActionItem.SHIFT: {
        		Shift sh = (Shift) ai;
        		addShiftPair(new ActionState(st, parseTable.getState(sh.nextState)));
        		statsRecordParsers();
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
        if(Tools.tracing) {
            TRACE("SG_DoReductions() - " + st.state.stateNumber);
        }

        if (Tools.debugging) {
            Tools.debug("doReductions() - " + dumpActiveStacks());

            Tools.debug(" state : " + st.peek().stateNumber);
            Tools.debug(" token : " + currentToken);
            Tools.debug(" label : " + prod.label);
            Tools.debug(" arity : " + prod.arity);
            Tools.debug(" stack : " + st.dumpStack());
        }

        List<Path> paths = st.findAllPaths(prod.arity);

        final int pathsCount = paths.size();
        if (Tools.debugging) {
            Tools.debug(" paths : " + pathsCount);
        }

        for (int i = 0; i < pathsCount; i++) {
            Path path = paths.get(i);

            List<IParseNode> kids = path.getATerms();

            if (Tools.debugging) {
                Tools.debug(" path: ", path);
                Tools.debug(" kids: ", kids);
            }

            Frame st0 = path.getEnd();

            if (Tools.debugging) {
                Tools.debug(st0.state);
            }

            State next = parseTable.go(st0.peek(), prod.label);

            if (Tools.logging) {
                Tools.logger("Goto(", st0.peek().stateNumber, ",", prod.label + ") == ",
                             next.stateNumber);
            }

            reducer(st0, next, prod, kids, path.getLength());
        }

        clearPath(paths);

        if (Tools.debugging) {
            Tools.debug("<doReductions() - " + dumpActiveStacks());
        }
        
        if(Tools.tracing) {
            TRACE("SG_ - doreductions done");
        }
    }

    private void clearPath(List<Path> paths) {
        if(Tools.tracing) {
            SGLR.TRACE("SG_ClearPath() - " + paths.size());
        }
        paths.clear();
    }

    private void reducer(Frame st0, State s, Production prod, List<IParseNode> kids, int length) throws IOException {
        if(Tools.tracing) {
            TRACE("SG_Reducer() - " + s.stateNumber + ", " + length + ", " + prod.label);
            TRACE_ActiveStacks();
        }

        if (Tools.logging) {
            Tools.logger("Reducing; state ", s.stateNumber, ", token: ", charify(currentToken),
                         ", production: ", prod.label);
        }

        if (Tools.debugging) {
            Tools.debug("reducer() - ", dumpActiveStacks());

            Tools.debug(" state      : ", s.stateNumber);
            Tools.debug(" token      : ", charify(currentToken) + " (" + currentToken + ")");
            Tools.debug(" production : ", prod.label);
        }

        increaseReductionCount();

        IParseNode t = prod.apply(kids);

        Frame st1 = findStack(activeStacks, s);
        if (st1 == null) {
            /* Found no existing stack with for state s; make new stack */
            st1 = newStack(s);
            Link nl = st1.addLink(st0, t, length);
            addStack(st1);
            if(Tools.tracing) {
                TRACE("SG_AddStack() - " + st1.state.stateNumber);
            }
            forActorDelayed.addFirst(st1);

            if (prod.isReject()) {
                if (Tools.logging) {
                    Tools.logger("Reject [new]");
                }
                nl.reject();
                increaseRejectCount();
            }
        } else {
            /* A stack with state s exists; check for ambiguities */
            Link nl = st1.findDirectLink(st0);

            if (nl != null) {
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

                if (prod.isReject()) {
                    nl.reject();
                }

                nl.addAmbiguity(t, tokensSeen);
                ambiguityManager.increaseAmbiguityCalls();

            } else {
                nl = st1.addLink(st0, t, length);
                if (Tools.debugging) {
                    Tools.debug(" added link ", nl, " from ", st1.state.stateNumber, " to ",
                                st0.state.stateNumber);
                }

                if (prod.isReject())
                    nl.reject();

                if(Tools.tracing) {
                    TRACE_ActiveStacks();
                }
                
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
                        continue;

                    for (ActionItem ai : st2.peek().getActionItems(currentToken)) {
                    	switch(ai.type) {
                    	case ActionItem.REDUCE: {
                    		Reduce red = (Reduce) ai;
                    		doLimitedReductions(st2, red.production, nl);
                    		if(Tools.tracing) {
                    			TRACE("SG_ - back in reducer ");
                    			TRACE_ActiveStacks();
                    		}
                    		break;
                    	}
                    	case ActionItem.REDUCE_LOOKAHEAD: {
                    		ReduceLookahead red = (ReduceLookahead) ai;
                    		if(checkLookahead(red)) {
                    			doLimitedReductions(st2, red.production, nl);
                    			if(Tools.tracing) {
                    				TRACE("SG_ - back in reducer ");
                    				TRACE_ActiveStacks();
                    			}
                    		}
                    		break;
                    	}
                    	}
                    }

                }
            }
        }
        if(Tools.tracing) {
            TRACE_ActiveStacks();
            TRACE("SG_ - reducer done");
        }
    }

    private void TRACE_ActiveStacks() {
        TRACE("SG_ - active stacks: " + activeStacks.size());
        TRACE("SG_ - for_actor stacks: " + forActor.size());
        TRACE("SG_ - for_actor_delayed stacks: " + forActorDelayed.size());
    }

    private boolean inReduceStacks(Queue<Frame> q, Frame frame) {
        if(Tools.tracing) {
            TRACE("SG_InReduceStacks() - " + frame.state.stateNumber);
        }
        return q.contains(frame);
    }

    private Frame newStack(State s) {
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

    private Frame findStack(List<Frame> stacks, State s) {
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

    private void doLimitedReductions(Frame st, Production prod, Link l) throws IOException {
        if(Tools.tracing) {
            TRACE("SG_DoLimitedReductions() - " + st.state.stateNumber + ", " + l.parent.state.stateNumber);
        }

        if (Tools.debugging) {
            Tools.debug("doLimitedReductions() - ", dumpActiveStacks());

            Tools.debug(" state : ", st.peek().stateNumber);
            Tools.debug(" token : ", currentToken);
            Tools.debug(" label : ", prod.label);
            Tools.debug(" arity : ", prod.arity);
            Tools.debug(" stack : ", st.dumpStack());
        }

        List<Path> paths = st.findLimitedPaths(prod.arity, l);

        if (Tools.debugging) {
            Tools.debug(paths);
        }

        final int pathsCount = paths.size();
        for (int i = 0; i < pathsCount; i++) {
            Path path = paths.get(i);

            List<IParseNode> kids = path.getATerms();

            if (Tools.debugging) {
                Tools.debug(path);
            }

            Frame st0 = path.getEnd();

            if (Tools.debugging) {
                Tools.debug(st0.state);
            }
            State next = parseTable.go(st0.peek(), prod.label);

            if (Tools.logging) {
                Tools.logger("Goto(", st0.peek().stateNumber, ",", prod.label, ") == ",
                             next.stateNumber);
            }

            reducer(st0, next, prod, kids, path.getLength());
        }
        clearPath(paths);
    }

    private int getNextToken() throws IOException {
        if(Tools.tracing) {
            TRACE("SG_NextToken() - ");
        }

        int t = currentInputStream.read();

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
            return SGLR.EOF;
        default:
            columnNumber++;
        }

        return t;
    }

    static int num = 0;

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
}
