/*
 * Created on 03.des.2005
 *
 * Copyright (c) 2005, Karl Trygve Kalleberg <karltk@ii.uib.no>
 * 
 * Licensed under the GNU General Public License, v2
 */
package org.spoofax.jsglr;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Stack;
import java.util.Vector;

import sun.security.krb5.internal.ac;

import aterm.ATerm;
import aterm.pure.PureFactory;

public class SGLR {

    // FIXME: Should probably be put elsewhere
    private static final int EOF = 256;

    private PureFactory factory;

    private Frame acceptingStack;

    private List<Frame> activeStacks;

    private ParseTable parseTable;

    private Stack<ActionState> forShifter;

    private int currentToken;

    private Stack<Frame> forActor;

    private Stack<Frame> forActorDelayed;

    SGLR() {
        basicInit(null);
    }

    private void basicInit(PureFactory pf) {
        factory = pf;
        if (factory == null)
            factory = new PureFactory();
        activeStacks = new Vector<Frame>();
    }

    public SGLR(InputStream is) throws IOException, FatalException,
            InvalidParseTableException {
        basicInit(null);
        loadParseTable(is);
    }

    public SGLR(PureFactory pf, InputStream is) throws IOException,
            FatalException, InvalidParseTableException {
        basicInit(pf);
        loadParseTable(is);
    }

    public void loadParseTable(InputStream r) throws IOException,
            FatalException, InvalidParseTableException {
        Tools.debug("loadParseTable()");

        ATerm pt = factory.readFromFile(r);

        parseTable = new ParseTable(pt);

        Tools.debug(" # states         : " + parseTable.getStateCount());
        Tools.debug(" # productions    : " + parseTable.getProductionCount());
        Tools.debug(" # actions        : " + parseTable.getActionCount());
        Tools.debug(" # gotos          : " + parseTable.getGotoEntries());

        Tools.debug(" " + (parseTable.hasRejects() ? "Includes" : "Excludes")
                + " rejects");
        Tools.debug(" "
                + (parseTable.hasPriorities() ? "Includes" : "Excludes")
                + " priorities");
        Tools.debug(" " + (parseTable.hasPrefers() ? "Includes" : "Excludes")
                + " prefer actions");
        Tools.debug(" " + (parseTable.hasAvoids() ? "Includes" : "Excludes")
                + " avoid actions");
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
        Frame st0 = new Frame(parseTable.getInitialState());
        activeStacks.add(st0);
        return st0;
    }

    public ATerm parse(FileInputStream fis) throws IOException {

        Tools.debug("parse() - " + dumpActiveStacks());

        acceptingStack = null;
        Frame st0 = initActiveStacks();

        do {
            currentToken = getNextToken(fis);
            parseCharacter();
            shifter();
        } while (currentToken != SGLR.EOF && activeStacks.size() > 0);

        if (acceptingStack == null)
            return null;

        Step s = acceptingStack.findStep(st0);

        if (s != null)
            return s.label;

        return null;
    }

    private void shifter() {
        Tools.debug("shifter()");

        Tools.debug(" token   : " + currentToken);
        Tools.debug(" parsers : " + forShifter.size());

        activeStacks.clear();

        ATerm t = makeTerm(currentToken);

        while (forShifter.size() > 0) {
            ActionState as = forShifter.pop();

            State s = as.s;
            Frame st0 = as.st;

            Frame st1 = findStack(activeStacks, s);
            if (st1 != null) {
                st1.addStep(st0, t);
            } else {
                st1 = new Frame(as.s);
                st1.addStep(st0, t);
                activeStacks.add(st1);
            }
        }

    }

    private ATerm makeTerm(int token) {
        // FIXME: Is this correct?
        return factory.makeInt(token);
    }

    private void parseCharacter() {
        Tools.debug("parseCharacter() - " + dumpActiveStacks());

        forActor = computeStackOfStacks(activeStacks);
        forActorDelayed = new Stack<Frame>();
        forShifter = new Stack<ActionState>();

        while (forActor.size() > 0 || forActorDelayed.size() > 0) {
            if (forActor.size() == 0) {
                forActor.add(forActorDelayed.pop());
            }
            while (forActor.size() > 0) {
                Frame st = forActor.pop();
                if (!st.allLinksRejected())
                    actor(st);
            }
        }
    }

    private void actor(Frame st) {
        Tools.debug("actor() - " + dumpActiveStacks());

        State s = st.peek();

        Tools.debug(" state   : " + s.stateNumber);
        Tools.debug(" token   : " + currentToken);

        List<ActionItem> actionItems = s.getActionItems(currentToken);

        Tools.debug(" actions : " + actionItems);

        for (ActionItem ai : actionItems) {
            if (ai instanceof Shift) {
                Shift sh = (Shift) ai;
                forShifter.push(new ActionState(st, parseTable
                        .getState(sh.nextState)));
            } else if (ai instanceof Reduce) {
                Reduce red = (Reduce) ai;
                doReductions(st, red.production);
            } else if (ai instanceof Accept) {
                acceptingStack = st;
            }
        }
    }

    // FIXME: Second argument should be Action, not Production?
    private void doReductions(Frame st, Production prod) {
        Tools.debug("doReductions() - " + dumpActiveStacks());

        Tools.debug(" state : " + st.peek().stateNumber);
        Tools.debug(" token : " + currentToken);
        Tools.debug(" arity : " + prod.arity);
        Tools.debug(" stack : " + st.dumpStack());
        
        if(prod.arity > 1){
            int x = 0;
        }
        
        List<Path> paths = st.computePathsToRoot(prod.arity);
        Tools.debug(paths);
        
        for (Path path : paths) {
            List<ATerm> kids = path.collectTerms();

            Tools.debug(path);

            Frame st0 = path.getStep().destination;

            Tools.debug(st0.state);
            
            State next = parseTable.go(st0.peek(), prod.label);

            Tools.debug(" next  : goto(" + st0.peek().stateNumber + ","
                    + prod.label + ") = " + next.stateNumber);

            reducer(st0, next, prod, kids);
        }

        activeStacks.remove(st);
        Tools.debug("<doReductions() - " + dumpActiveStacks());

    }

    private void popStack(Frame st, int arity) {
        Tools.debug("popStack(" + arity + ") - " + st.dumpStack());

        if (arity > 0) {
            List<Frame> frames = st.computeFramesAtDepth(arity);

            Tools.debug(frames);
            activeStacks.addAll(frames);
        }
        activeStacks.remove(st);

    }

    private void reducer(Frame st0, State s, Production prod, List<ATerm> kids) {
        Tools.debug("reducer() - " + dumpActiveStacks());

        Tools.debug(" state      : " + s.stateNumber);
        Tools.debug(" token      : " + currentToken);
        Tools.debug(" production : " + prod.label);

        ATerm t = prod.apply(kids, parseTable);

        Frame st1 = findStack(activeStacks, s);
        if (st1 != null) {
            Step nl = st1.findStep(st0);

            if (nl != null) {
                nl.addAmbiguity(t);

                if (prod.status == Production.REJECT)
                    nl.reject();

            } else {
                nl = st1.addStep(st0, t);

                if (prod.status == Production.REJECT)
                    nl.reject();

                for (Frame st2 : activeStacks) {
                    if (st2.allLinksRejected())
                        continue;
                    if (forActor.contains(st2))
                        continue;
                    if (forActorDelayed.contains(st2))
                        continue;

                    for (ActionItem ai : st2.peek()
                            .getActionItems(currentToken)) {
                        if (ai instanceof Reduce) {
                            Reduce red = (Reduce) ai;
                            doLimitedReductions(st2, red.production, nl);
                        }
                    }

                }
            }
        } else {
            st1 = new Frame(s);
            Step nl = st1.addStep(st0, t);
            activeStacks.add(st1);
            if (st1.peek().rejectable()) {
                forActorDelayed.push(st1);
            } else {
                forActor.clear();
                forActor.add(st1);
                forActor.addAll(forActorDelayed);
            }

            if (prod.status == Production.REJECT)
                nl.reject();
        }
    }

    private Frame findStack(List<Frame> stacks, State s) {
        // FIXME: Should we recurse into the stacks, too?
        Tools.debug("findStack() - " + dumpActiveStacks());
        Tools.debug(" looking for " + s.stateNumber);
        for (Frame st : stacks)
            if (st.state.stateNumber == s.stateNumber) {
                return st;
            }
        return null;
    }

    private void doLimitedReductions(Frame st, Production prod, Step l) {
        Tools.debug("doLimitedReductions()");

        List<Path> paths = st.computePathsToRoot(prod.arity, l);
        Frame st0 = st.getRoot();

        for (Path path : paths) {
            List<ATerm> kids = path.collectTerms();
            reducer(st0, parseTable.go(st0.peek(), prod.label), prod, kids);
        }
    }

    private Stack<Frame> computeStackOfStacks(List<Frame> st) {
        Stack<Frame> ret = new Stack<Frame>();
        for (Frame s : st)
            ret.push(s);
        return ret;
    }

    private int getNextToken(FileInputStream fis) throws IOException {
        int t = fis.read();

        Tools.debug("getNextToken() - " + t);

        // FIXME: Is 256 the EOF?
        return t == -1 ? SGLR.EOF : t;
    }

    private String dumpActiveStacks() {
        StringBuffer sb = new StringBuffer();
        if (activeStacks == null) {
            sb.append(" GSS unitialized");
        } else {
            for (Frame f : activeStacks) {
                sb.append(f.dumpStack());
            }
        }
        return sb.toString();
    }
}
