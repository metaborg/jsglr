/*
 * Created on 04.des.2005
 *
 * Copyright (c) 2005, Karl Trygve Kalleberg <karltk@ii.uib.no>
 * 
 * Licensed under the GNU Lesser General Public License, v2.1
 */
package org.spoofax.jsglr;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import com.sun.jmx.trace.Trace;

public class Frame implements Serializable {

    static final long serialVersionUID = -4757644376472129935L;

    public final State state;

    // Using a Vector and regular iteration takes parsing of file-test from 1100ms (min) to 2020ms (max)!
    private Link[] steps;
    private int stepsCount;

    // FIXME: All frames except the root must have a step with a label
    // that goes to the parent frame. Should we enforce this in this
    // constructor?
    public Frame(State s) {
        state = s;
        steps = new Link[80]; //todo: what is the max size?
        stepsCount = 0;
    }

    public boolean allLinksRejected() {
        if(Tools.tracing) {
            SGLR.TRACE("SG_Rejected() - " + state.stateNumber);
        }
        
        if (stepsCount == 0)
            return false;

        for (int i = 0; i < stepsCount; i++) {
            if (!steps[i].isRejected())
                return false;
        }

        return true;
    }

    public State peek() {
        return state;
    }

    public List<Path> findAllPaths(int arity) {
        LinkedList<Path> ret = new LinkedList<Path>();
        doComputePathsToRoot(ret, null, arity, 0);
        return ret;
    }

    private void doComputePathsToRoot(LinkedList<Path> collect, Path node, int arity, int length) {
        if(Tools.tracing) {
            SGLR.TRACE("SG_FindAllPaths() - " + arity + ", " + length);
        }

        if (arity == 0) {
            Path n = Path.valueOf(node, null, this, length);
            if(Tools.tracing) {
                SGLR.TRACE("SG_NewPath() - " + state.stateNumber + ", " + n.length);
            }
            collect.addFirst(n);
        } else { 
            for (int i = 1; i <= stepsCount; i++) {
                Link ln = steps[stepsCount - i];
                
                Path n = Path.valueOf(node, ln.label, this, ln.getLength());
                ln.parent.doComputePathsToRoot(collect, n, arity - 1, length + ln.getLength());
            }
        }
    }

    public Frame getRoot() {
        // FIXME: I'm not happy about the contract here. The assumption is
        // that the holder of the Frame class applies addStep correctly.
        if (stepsCount == 0)
            return this;
        return steps[0].parent.getRoot();
    }

    public Link findDirectLink(Frame st0) {
        if(Tools.tracing) {
            SGLR.TRACE("SG_FindDirectLink() - [" + state.stateNumber + ", " + st0.state.stateNumber + "]");
        }
        
        for (int i = 0; i < stepsCount; i++) {
            if (steps[i].parent == st0)
                return steps[i];
        }
        
        return null;
    }

    public Link addLink(Frame st0, IParseNode n, int length) {
        if(Tools.tracing) {
            SGLR.TRACE("SG_AddLink() - " + state.stateNumber + ", " + st0.state.stateNumber + ", " + length);
        }
        return steps[stepsCount++] = new Link(st0, n, length); 
    }

    public String dumpStack() {
        StringBuffer sb = new StringBuffer();

        sb.append("GSS [\n").append(doDumpStack(2)).append("\n  ]");
        return sb.toString();
    }

    public String doDumpStack(int indent) {
        StringBuffer sb = new StringBuffer();
        sb.append(" ").append(state.stateNumber);
        if (stepsCount > 1) {
            sb.append(" ( ");
            for (Link s : steps) {
                if(s == null) {
                    break;
                }
                sb.append(s.parent.doDumpStack(indent + 1));
                sb.append(" | ");
            }
            sb.append(")");

        } else {
            for (Link s : steps) {
                if(s == null) {
                    break;
                }
                sb.append(" <").append(s.label).append("> ").append(s.parent.state.stateNumber).append("\n");
                sb.append(s.parent.doDumpStack(indent));
            }
        }
        return sb.toString();
    }

    public String dumpStackCompact() {
        StringBuffer sb = new StringBuffer();

        sb.append("GSS [").append(doDumpStackCompact()).append(" ]");
        return sb.toString();
    }

    public String doDumpStackCompact() {
        StringBuffer sb = new StringBuffer();
        sb.append(" ").append(state.stateNumber);
        if (stepsCount > 1) {
            sb.append(" ( ");
            for (Link s : steps) {
                if(s == null) {
                    break;
                }
                sb.append(s.parent.doDumpStackCompact());
                sb.append(" | ");
            }
            sb.append(")");

        } else {
            for (Link s : steps) {
                if(s == null) {
                    break;
                }
                sb.append(", ").append(s.parent.doDumpStackCompact());
            }
        }
        return sb.toString();
    }

    public List<Path> findLimitedPaths(int arity, Link l) {
        if(Tools.tracing) {
            SGLR.TRACE("SG_FindLimitedPaths() - " + arity + ", " + l.getLength() + ", " + l.parent.state.stateNumber);
            TRACE_DumpLinks(steps);
        }
        LinkedList<Path> ret = new LinkedList<Path>();
        if(findLink(arity, l)) { 
            doComputePathsToRoot(ret, null, l, false, arity, 0);
        } 
        return ret;
    }

    private void TRACE_DumpLinks(Link[] st) {
        for(int i = 0; i < stepsCount; i++) {
            Link l = st[stepsCount - i - 1];
            if(Tools.tracing) {
                SGLR.TRACE("SG_ - dump link: " + l.parent.state.stateNumber);
            }
        }
    }

    private boolean findLink(int arity, Link l0) {
        if(Tools.tracing) {
            SGLR.TRACE("SG_FindLink() - " + arity);
            SGLR.TRACE("SG_ - links: " + stepsCount);
        }
        
        if(arity > 0) {
            for(int i = 0; i < stepsCount; i++) {
                Link l1 = steps[stepsCount - i - 1];
                if(l0 == l1) {
                    if(Tools.tracing) {
                        SGLR.TRACE("SG_ - l0 == l1");
                    }
                    return true;
                } else if (l1.parent.findLink(arity - 1, l0)) {
                    if(Tools.tracing) {
                        SGLR.TRACE("SG_ - findlink");
                    }
                    return true;
                }
            }
        }
        return false;
    }

    private void doComputePathsToRoot(LinkedList<Path> collect, Path node, Link l,
      boolean seen, int arity, int length) {
        if(Tools.tracing) {
            SGLR.TRACE("SG_FindPaths() - " + arity);
        }

        if (arity == 0 && seen) {
            Path n = Path.valueOf(node, null, this, length);
            if(Tools.tracing) {
                SGLR.TRACE("SG_NewPath() - " + state.stateNumber + ", " + n.length);
            }
            collect.addFirst(n);
        } else if(arity > 0) {
            for (int i = 0; i < stepsCount; i++) {
                Link ln = steps[stepsCount - i - 1];
                boolean seenIt = seen || (ln == l);
                Path n = Path.valueOf(node, ln.label, this, ln.getLength());
                ln.parent.doComputePathsToRoot(collect, n, l, seenIt, arity - 1, length + ln.getLength());
            }
        }
    }

    public void clear() {

        if (this.steps != null) {
            for (int i = 0; i < stepsCount; i++) {
                steps[i].clear();
                steps[i] = null;
            }
            this.steps = null;
            this.stepsCount = 0;
        }
    }

}
