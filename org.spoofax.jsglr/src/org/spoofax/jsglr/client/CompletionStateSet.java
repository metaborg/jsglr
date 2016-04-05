package org.spoofax.jsglr.client;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

import org.spoofax.jsglr.shared.Tools;

public class CompletionStateSet implements Serializable {
    static final long serialVersionUID = 3383369639779986307L;

    private final Set<State> states = new LinkedHashSet<State>();

    private int traceCallCount = 0;
    private State last;


    public Iterable<State> states() {
        return states;
    }

    public State last() {
        return last;
    }

    public boolean add(State e) {
        if(Tools.debuggingCompletion) {
            COMPLETIONS_TRACE("SG_CompletionTRACE() - adding state " + e.stateNumber);
        }
        last = e;
        return states.add(e);
    }

    public boolean replace(State state) {
        if(Tools.debuggingCompletion) {
            COMPLETIONS_TRACE("SG_CompletionStates() - replacing state " + last.stateNumber + " by "
                + state.stateNumber);
        }
        states.remove(last);
        return add(state);
    }

    public boolean addAll(CompletionStateSet cs) {
        last = cs.last;
        return states.addAll(cs.states);
    }

    public void clear() {
        last = null;
        states.clear();
    }

    @Override public String toString() {
        final StringBuffer sb = new StringBuffer();
        sb.append("[ ");
        for(State s : states) {
            sb.append(s.stateNumber + " ");
        }
        sb.append("]");

        return sb.toString();
    }

    private void COMPLETIONS_TRACE(String string) {
        System.out.println("[" + traceCallCount + "] " + string + "\n");
        traceCallCount++;
    }
}
