/*
 * Created on 04.des.2005
 *
 * Copyright (c) 2005, Karl Trygve Kalleberg <karltk@ii.uib.no>
 * 
 * Licensed under the GNU General Public License, v2
 */
package org.spoofax.jsglr;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import aterm.ATerm;
import aterm.ATermAppl;
import aterm.ATermList;

public class ParseTable {

    // FIXME: This should be an array
    private HashMap<Integer, State> states;
    private int startState;
    private List<Label> labels;
    private List<Priority> priorities;
    
    public ParseTable(ATerm pt) throws FatalException, InvalidParseTableException {
        parse(pt);
    }

    private boolean parse(ATerm pt) throws FatalException, InvalidParseTableException {
        int version = Term.intAt(pt, 0);
        startState = Term.intAt(pt, 1);
        ATermList labelsTerm = Term.listAt(pt, 2);
        ATermAppl statesTerm = Term.applAt(pt, 3);
        ATermAppl prioritiesTerm = Term.applAt(pt, 4);

        if (version != 4) {
            return false;
        }

        labels = parseLabels(labelsTerm);
        states = parseStates(statesTerm);
        priorities = parsePriorities(prioritiesTerm);
        

        return true;
    }

    private List<Priority> parsePriorities(ATermAppl prioritiesTerm) throws FatalException {
        
        List<Priority> ret = new Vector<Priority>(prioritiesTerm.getChildCount());
        
        ATermList prods = Term.listAt(prioritiesTerm, 0);
        
        for(int i=0;i<prods.getChildCount();i++) {
            ATermAppl a = Term.applAt(prods, i);
            int left = Term.intAt(a, 0);
            int right = Term.intAt(a, 1);
            if(a.getName().equals("left-prio")) {
                ret.add(new Priority(Priority.LEFT, left, right));
            } else if(a.getName().equals("right-prio")) {
                ret.add(new Priority(Priority.RIGHT, left, right));
            } else if(a.getName().equals("non-assoc")) {
                ret.add(new Priority(Priority.NONASSOC, left, right));
            } else if(a.getName().equals("gtr-prio")) {
                ret.add(new Priority(Priority.GTR, left, right));
            } else {
                throw new FatalException("Unknown priority");
            }
        }
        return ret;
    }

    private List<Label> parseLabels(ATermList labelsTerm) {

        List<Label> ret = new Vector<Label>(labelsTerm.getChildCount());
        
        for(int i=0;i<labelsTerm.getChildCount();i++) {
            ATermAppl a = Term.applAt(labelsTerm, i);
            ATermAppl prod = Term.applAt(a, 0);
            int labelNumber = Term.intAt(a, 1);
            
            ret.add(new Label(labelNumber, prod));
        }
        
        return ret;
    }

    private HashMap<Integer, State> parseStates(ATermAppl statesTerm) throws InvalidParseTableException {

        ATermList states = Term.listAt(statesTerm, 0);
        HashMap<Integer, State> ret = new HashMap<Integer, State>();

        for (int i = 0; i < states.getLength(); i++) {
            ATermAppl stateRec = Term.applAt(states, i);
            int stateNumber = Term.intAt(stateRec, 0);
            List<Goto> gotos = parseGotos(Term.listAt(stateRec, 1));
            List<Action> actions = parseActions(Term.listAt(stateRec, 2));
            
            ret.put(stateNumber, new State(stateNumber, gotos, actions));

        }

        return ret;
    }

    private List<Action> parseActions(ATermList actionList) throws InvalidParseTableException {
        
        List<Action> ret = new Vector<Action>(actionList.getChildCount());

        for (int i = 0; i < actionList.getChildCount(); i++) {
            ATermAppl action = Term.applAt(actionList, i);
            List<Range> ranges = parseRanges(Term.listAt(action, 0));
            List<ActionItem> items = parseActionItems(Term
                    .listAt(action, 1));
            ret.add(new Action(ranges, items));
        }
        return ret;
    }

    private List<ActionItem> parseActionItems(ATermList items) {
        List<ActionItem> ret = new Vector<ActionItem>(items.getChildCount());
        for(int i=0;i<items.getChildCount();i++) {
            ActionItem item = null;
            ATermAppl a = Term.applAt(items, i);
            if(a.getName().equals("reduce")) {
                int productionArity = Term.intAt(a, 0);
                int label = Term.intAt(a, 1);
                int status = Term.intAt(a, 2);
                item = new Reduce(productionArity, label, status);
            } else if(a.getName().equals("accept")) {
                item = new Accept();
            } else if(a.getName().equals("shift")) {
                int nextState = Term.intAt(a, 0); 
                item = new Shift(nextState);
            }
            ret.add(item);
        }
        return ret;
    }

    private List<Goto> parseGotos(ATermList gotos) throws InvalidParseTableException {

        List<Goto> ret = new Vector<Goto>(gotos.getChildCount());

        for (int i = 0; i < gotos.getChildCount(); i++) {
            ATermAppl go = Term.applAt(gotos, i);
            ATermList rangeList = Term.listAt(go, 0);
            int newStateNumber = Term.intAt(go, 1);
            List<Range> ranges = parseRanges(rangeList);
            List<Integer> productionLabels = parseProductionLabels(rangeList);
            ret.add(new Goto(ranges, productionLabels, newStateNumber));
        }

        return ret;
    }

    private List<Integer> parseProductionLabels(ATermList ranges) {

        // FIXME: Allocates too much memory
        List<Integer> ret = new Vector<Integer>(ranges.getChildCount());

        for(int i=0;i<ranges.getChildCount();i++) {
            ATerm t = Term.termAt(ranges, i);
            if(Term.isInt(t)) {
                int j = Term.toInt(t);
                if(j > 256) {
                    ret.add(new Integer(j));
                }
            }
        }
        return ret;
    }

    private List<Range> parseRanges(ATermList ranges) throws InvalidParseTableException {

        // FIXME: Allocates too much memory
        List<Range> ret = new Vector<Range>(ranges.getChildCount());

        for (int i = 0; i < ranges.getChildCount(); i++) {
            ATerm t = Term.termAt(ranges, i);
            if (Term.isInt(t)) {
                int j = Term.toInt(t);
                // Anything > 256 is a label, anything below is a char
                // FIXME: Should it be <= ?
                if(j <= 256) 
                    ret.add(new Range(j));
            } else {
                int low = Term.intAt(t, 0);
                int hi = Term.intAt(t, 1);
                ret.add(new Range(low, hi));
            }
        }
        return ret;
    }

    public State getInitialState() {
        return states.get(startState);
    }

    public State go(State s, int label) {
        Tools.debug("goto(" + s.stateNumber + "," + label + ")");
        State n = states.get(s.go(label));
        Tools.debug("goto(" + s.stateNumber + "," + label + ") = " + n.stateNumber);
        return n;
    }

    // FIXME: Why can't this.labels just be an array and label the index? 
    public Label getLabel(int label) {
        for(Label l : labels) {
            if (l.labelNumber == label)
                return l;
        }
        return null;
    }

    public State getState(int s) {
        return states.get(s);
    }

    public int getStateCount() {
        return states.values().size();
    }

    public int getProductionCount() {
        // FIXME: What are labels really?
        return labels.size();
    }

    public int getActionEntryCount() {
        int total = 0;
        for(State s : states.values()) {
            total += s.getActionItemCount();
        }
        return total;
    }

    public int getGotoEntries() {
        int total = 0;
        for(State s : states.values()) {
            total += s.getGotoCount();
        }
        return total;
    }

    public int getActionCount() {
        int total = 0;
        for(State s : states.values()) {
            total += s.getActionCount();
        }
        return total;
    }

    public boolean hasRejects() {
        
        for(State s : states.values())
            if(s.rejectable())
                return true;
        
        return false;
    }

    public boolean hasPriorities() {
        if(priorities.size() > 0)
            return true;
        return false;
    }
    
    public boolean hasPrefers() {
        for(State s : states.values())
            if(s.hasPrefer())
                return true;
        return false;
    }

    public boolean hasAvoids() {
        for(State s : states.values())
            if(s.hasAvoid())
                return true;
        return false;
    }
        
}
