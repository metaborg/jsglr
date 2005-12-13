/*
 * Created on 04.des.2005
 *
 * Copyright (c) 2005, Karl Trygve Kalleberg <karltk@ii.uib.no>
 * 
 * Licensed under the GNU General Public License, v2
 */
package org.spoofax.jsglr;

import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import aterm.ATerm;

public class Frame {

    public final State state;

    private List<Link> steps;

    // FIXME: All frames except the root must have a step with a label
    // that goes to the parent frame. Should we enforce this in this
    // constructor?
    public Frame(State s) {
        state = s;
        steps = new Vector<Link>();
    }

    public boolean allLinksRejected() {

        if (steps.size() == 0)
            return false;

        for (Link s : steps) {
            if (!s.isRejected())
                return false;
        }

        return true;
    }

    public State peek() {
        return state;
    }

    public Path<Link> computePathsToRoot(int arity) {
        if (arity == 0) {
            Path<Link> ret = new Path<Link>();
            Link ln = new Link(this, null);
            ret.add(ln);
            return ret;
        }

        return doComputePathsToRoot(arity);
    }

    public Path<Link> doComputePathsToRoot(int arity) {

        Path<Link> ret = new Path<Link>();

        if (arity == 0) {
            Link ln = new Link(this, null);
            ret.add(ln);
        } else if (steps.size() > 0) {
            for (Link ln : steps) {
                Path<Link> p = ln.parent.doComputePathsToRoot(arity - 1);
                p.add(ln);
                ret.add(p);
            }
        } else {
            Tools.debug("Error: Stack not deep enough for arity");
        }

        return ret;
    }

    public Frame getRoot() {
        // FIXME: I'm iffy about the contract here. The assumption is
        // that the user applies addStep correctly.
        if (steps.size() == 0)
            return this;
        return steps.get(0).parent.getRoot();
    }

    public Link findLink(Frame st0) {
        for (Link s : steps) {
            if (s.parent == st0)
                return s;
        }
        return null;
    }

    public Link addLink(Frame st0, ATerm t) {
        Link s = new Link(st0, t);
        steps.add(s);
        return s;
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
        for (Link s : steps) {
            if (!hasForked) {
                sb.append(", " + s.parent.dumpStack(false));
                hasForked = true;
            } else {
                sb.append("[ " + s.parent.dumpStack(false) + "]");
            }
        }
        return sb.toString();
    }

    public List<Frame> computeFramesAtDepth(int depth) {
        List<Frame> frames = new LinkedList<Frame>();

        if (depth == 0) {
            frames.add(this);
        } else {
            for (Link s : steps) {
                Frame st = s.parent;
                frames.addAll(st.computeFramesAtDepth(depth - 1));
            }
        }
        return frames;
    }

}
