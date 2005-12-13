/*
 * Created on 04.des.2005
 *
 * Copyright (c) 2005, Karl Trygve Kalleberg <karltk@ii.uib.no>
 * 
 * Licensed under the GNU General Public License, v2
 */
package org.spoofax.jsglr;

import java.util.List;
import java.util.Vector;

import aterm.ATerm;

public class Frame {

    public final State state;

    private List<Step> steps;

    // FIXME: All frames except the root must have a step with a label
    // that goes to the parent frame. Should we enforce this in this
    // constructor?
    public Frame(State s) {
        state = s;
        steps = new Vector<Step>();
    }

    public boolean allLinksRejected() {
        
        if(steps.size() == 0)
            return false;
        
        for (Step s : steps) {
            if (!s.isRejected())
                return false;
        }
        
        return true;
    }

    public State peek() {
        return state;
    }

    public List<Path> computePathsToRoot(int arity) {

        List<Path> ret = new Vector<Path>();

        if (arity == 0 || steps.size() == 0) {
            Path p = new Path();
            // FIXME: WOW! this is bad
            p.addStep(new Step(this, null));
            ret.add(p);
        } else {
            for (Step s : steps) {
                List<Path> paths = s.destination.computePathsToRoot(arity - 1);
                for (Path p : paths) {
                    p.addStep(s);
                }
                ret.addAll(paths);
            }
        }
        return ret;
    }

    public Frame getRoot() {
        // FIXME: I'm iffy about the contract here. The assumption is
        // that the user applies addStep correctly.
        if (steps.size() == 0)
            return this;
        return steps.get(0).destination.getRoot();
    }

    public Step findStep(Frame st0) {
        for(Step s : steps) {
            if(s.destination == st0)
                return s;
        }
        return null;
    }

    public Step addStep(Frame st0, ATerm t) {
        Step s = new Step(st0, t);
        steps.add(s);
        return s;
    }

    public List<Path> computePathsToRoot(int arity, Step l) {
        // FIXME: I think l can only occur in the first step of the path.
        //        but this must be verified
        
        List<Path> paths = l.destination.computePathsToRoot(arity - 1);
        for(Path p : paths)
            p.addStep(l);
        
        return paths;
    }

    public String dumpStack() {
        StringBuffer sb = new StringBuffer();
        
        sb.append("GSS [" + dumpStack(false) + " ]");
        return sb.toString();
    }

    public String dumpStack(boolean f) {
        StringBuffer sb = new StringBuffer();
        boolean hasForked = false;
        sb.append(" " + state.stateNumber);
        for(Step s : steps) {
            if(!hasForked) {
                sb.append(", " + s.destination.dumpStack(false));
                hasForked = true;
            } else {
                sb.append("[ " + s.destination.dumpStack(false) + "]");
            }
        }
        return sb.toString();
    }

    public void dropPaths(List<Path> paths) {
        // FIXME: Over estimated number of new Steps
        // FIXME: This is probably not even remotely correct in the face of GSS "joins"
        List<Step> newSteps = new Vector<Step>(paths.size());
        
        for(Path p : paths) {
            Step s = p.getRoot();
            if(s != null)
            newSteps.add(s);
        }
        steps = newSteps;
    }

}
