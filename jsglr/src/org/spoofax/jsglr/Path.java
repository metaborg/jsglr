/*
 * Created on 04.des.2005
 *
 * Copyright (c) 2005, Karl Trygve Kalleberg <karltk@ii.uib.no>
 * 
 * Licensed under the GNU Lesser General Public License, v2.1
 */
package org.spoofax.jsglr;

import java.util.LinkedList;
import java.util.List;

import aterm.ATerm;

public class Path {

    public final Path parent;

    public final ATerm label;

    public final Frame frame;

    Path(Path parent, ATerm label, Frame frame) {
        this.parent = parent;
        this.label = label;
        this.frame = frame;
    }

    public Frame getEnd() {
        return frame;
    }

    public final List<ATerm> getATerms() {
        List<ATerm> ret = new LinkedList<ATerm>();
        for (Path n = parent; n != null; n = n.parent) {
            ret.add(n.label);
        }
        return ret;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        boolean first = true;
        sb.append("<");
        for (Path p = this; p != null; p = p.parent) {
            if (!first)
                sb.append(", ");
            sb.append(p.frame.state.stateNumber);
            first = false;
        }
        sb.append(">");
        return sb.toString();
    }

}
