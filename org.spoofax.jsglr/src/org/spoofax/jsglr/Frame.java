/*
 * Created on 04.des.2005
 *
 * Copyright (c) 2005, Karl Trygve Kalleberg <karltk@ii.uib.no>
 * 
 * Licensed under the GNU Lesser General Public License, v2.1
 */
package org.spoofax.jsglr;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import java.io.Serializable;

public class Frame implements Serializable {

    static final long serialVersionUID = -4757644376472129935L;
    
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

    public List<Path> computePathsToRoot(int arity) {

        if (arity == 0) {
            List<Path> ret = new LinkedList<Path>();
            ret.add(new Path(null, null, this));
            return ret;
        }

        List<Path> ret = new LinkedList<Path>();
        doComputePathsToRoot(ret, null, arity);
        Collections.reverse(ret);
        return ret;
    }

    private void doComputePathsToRoot(List<Path> collect, Path node, int arity) {

        if (arity == 0) {
            Path n = new Path(node, null, this);
            collect.add(n);
        }

        for (Link ln : steps) {
            Path n = new Path(node, ln.label, this);
            ln.parent.doComputePathsToRoot(collect, n, arity - 1);
        }
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

    public Link addLink(Frame st0, IParseNode n, int length) {
        Link s = new Link(st0, n, length);
        steps.add(s);
        return s;
    }

    public String dumpStack() {
        StringBuffer sb = new StringBuffer();

        sb.append("GSS [\n" + doDumpStack(2) + "\n  ]");
        return sb.toString();
    }

    public String doDumpStack(int indent) {
        StringBuffer sb = new StringBuffer();
        sb.append(" " + state.stateNumber);
        if (steps.size() > 1) {
            sb.append(" ( ");
            for (Link s : steps) {
                sb.append(s.parent.doDumpStack(indent + 1));
                sb.append(" | ");
            }
            sb.append(")");

        } else {
            for (Link s : steps) {
                sb.append(" <" + s.label + "> "
                        + s.parent.state.stateNumber + "\n");
                sb.append(s.parent.doDumpStack(indent));
            }
        }
        return sb.toString();
    }

    public String dumpStackCompact() {
        StringBuffer sb = new StringBuffer();

        sb.append("GSS [" + doDumpStackCompact() + " ]");
        return sb.toString();
    }

    public String doDumpStackCompact() {
        StringBuffer sb = new StringBuffer();
        sb.append(" " + state.stateNumber);
        if (steps.size() > 1) {
            sb.append(" ( ");
            for (Link s : steps) {
                sb.append(s.parent.doDumpStackCompact());
                sb.append(" | ");
            }
            sb.append(")");

        } else {
            for (Link s : steps) {
                sb.append(", " + s.parent.doDumpStackCompact());
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

    public List<Path> computePathsToRoot(int arity, Link l) {

        if (arity == 0) {
            List<Path> ret = new LinkedList<Path>();
            ret.add(new Path(null, null, this));
            return ret;
        }

        List<Path> ret = new LinkedList<Path>();
        doComputePathsToRoot(ret, null, l, false, arity);
        // FIXME: Necessary?
        Collections.reverse(ret);
        return ret;
    }

    private void doComputePathsToRoot(List<Path> collect, Path node, Link l,
            boolean seen, int arity) {

        if (arity == 0 && seen) {
            Path n = new Path(node, null, this);
            collect.add(n);
        }

        for (Link ln : steps) {
            Path n = new Path(node, ln.label, this);
            boolean seenIt = seen || (ln == l);
            ln.parent.doComputePathsToRoot(collect, n, l, seenIt, arity - 1);
        }
    }
}
