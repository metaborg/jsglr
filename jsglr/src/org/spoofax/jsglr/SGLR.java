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

import aterm.ATerm;
import aterm.pure.PureFactory;

public class SGLR {

    private PureFactory factory;

    private Frame acceptingStack;

    private List<Frame> activeStacks;

    private ParseTable parseTable;

    private List<ActionState> forShifter;

    private int currentToken;

    private List<Frame> forActor;

    private Stack<Frame> forActorDelayed;

    SGLR() {
        basicInit();
    }

    private void basicInit() {
        factory = new PureFactory();
        activeStacks = new Vector<Frame>();
    }

    public SGLR(InputStream r) throws IOException, FatalException {
        basicInit();
        loadParseTable(r);
    }
    
    public void loadParseTable(InputStream r) throws IOException, FatalException {
        ATerm pt = factory.readFromFile(r);

        parseTable = new ParseTable(pt);
    }

    private boolean computeParseTable(ATerm pt) {
        // TODO Auto-generated method stub
        return false;
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

        acceptingStack = null;
        Frame st0 = initActiveStacks();

        do {
            currentToken = getNextToken(fis);
            parseCharacter();
            shifter();
        } while (currentToken != -1 && activeStacks.size() > 0);

        // FIXME: How can this return multiple alternatives?
        return acceptingStack.findStep(st0).label;
    }

    private void shifter() {
        activeStacks.clear();

        ATerm t = makeTerm(currentToken);

        for (ActionState as : forShifter) {
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

    private ATerm makeTerm(int currentToken2) {
        // TODO Auto-generated method stub
        return null;
    }

    private void parseCharacter() {
        forActor = getListOfStacks(activeStacks);
        forActorDelayed = new Stack<Frame>();
        forShifter = new Vector<ActionState>();
        while (forActor.size() > 0 || forActorDelayed.size() > 0) {
            if (forActor.size() == 0) {
                forActor.add(forActorDelayed.pop());
            }
            for (Frame st : forActor) {
                if (!st.allLinksRejected())
                    actor(st);
            }
        }
    }

    private void actor(Frame st) {
        State s = st.peek();
        for (ActionItem ai : s.getActionItems(currentToken)) {
            if(ai instanceof Shift) {
                forShifter.add(new ActionState(st, s));
            } else if(ai instanceof Reduce) {
                Reduce red = (Reduce) ai;
                doReductions(st, red.production);
            } else if(ai instanceof Accept) {
                acceptingStack = st;
            }
        }
    }

    private void doReductions(Frame st, Production prod) {
        Frame st0 = st.getRoot();

        for (Path path : st.computePathsToRoot(prod.arity)) {
            List<ATerm> kids = path.collectTerms();
            reducer(st0, st0.peek().go(prod), prod, kids);
        }

    }

    private void reducer(Frame st0, State s, Production prod, List<ATerm> kids) {
        ATerm t = prod.apply(kids);

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
                    if (st2.rejected())
                        continue;
                    if (forActor.contains(st2))
                        continue;
                    if (forActorDelayed.contains(st2))
                        continue;

                    for (ActionItem ai : st2.peek().getActionItems(currentToken)) {
                        if (ai instanceof Reduce) {
                            Reduce red = (Reduce) ai;
                            doLimitedReductions(st2, red.production, nl);
                        }
                    }

                }
            }
        } else {
            st1 = new Frame(s);
        }
    }

    private Frame findStack(List<Frame> stacks, State s) {
        for (Frame st : stacks)
            if (st.state.equals(s))
                return st;
        return null;
    }

    private void doLimitedReductions(Frame st, Production prod, Step l) {
        List<Path> paths = st.computePathsToRoot(prod.arity, l);
        Frame st0 = st.getRoot();
        for (Path path : paths) {
            List<ATerm> kids = path.collectTerms();
            // FIXME: Not correct, where do we go to?
            reducer(st0, st0.peek().go(prod), prod, kids);
        }
    }

    private List<Frame> getListOfStacks(List<Frame> st) {
        List<Frame> ret = new Vector<Frame>();
        for (Frame s : st)
            ret.add(s);
        return ret;
    }

    private int getNextToken(FileInputStream fis) throws IOException {
        return fis.read();
    }

}
