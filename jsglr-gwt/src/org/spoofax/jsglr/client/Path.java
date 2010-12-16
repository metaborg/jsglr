/*
 * Created on 04.des.2005
 *
 * Copyright (c) 2005, Karl Trygve Kalleberg <karltk near strategoxt.org>
 * 
 * Licensed under the GNU Lesser General Public License, v2.1
 */
package org.spoofax.jsglr.client;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Path /*implements Iterable<IParseNode>*/ {

    public final Path parent;

    public final IParseNode label;

    public final Frame frame;

    protected final int length;
    
    public final Link link;

    Path(Path parent, Link link, Frame frame, int length) {
    	this.parent = parent;
        this.link = link;
        if(link != null){
            this.label = link.label;
        } else {
            this.label = null;
        }
        this.frame = frame;
        this.length = length;
    }

    public int getRecoverCount()
    {
        int result = 0;
        if(link != null) {
            result += link.recoverCount;           
        }
        if(parent != null) {
        	// TODO find out relation linktoparent/parent
            result += parent.getRecoverCount();
        }
        return result;        
    }
    
    public int getRecoverCount(int maxCharLength)
    {
        if(parent == null || this.length <= maxCharLength)
            return getRecoverCount();
        return parent.getRecoverCount(maxCharLength);
    }

    public static boolean logNewInstanceCreation = false;

    public static Path valueOf(Path parent, Link ln, Frame frame, int length) {
        return new Path(parent, ln, frame, length);
    }


    public Frame getEnd() {
        return frame;
    }

    public final List<IParseNode> getATerms() {
        ArrayList<IParseNode> ret = new ArrayList<IParseNode>();
        for (Path n = parent; n != null; n = n.parent) {
            ret.add(n.label);
        }
        return ret;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        sb.append("<");
        for (Path p = this; p != null; p = p.parent) {
            if (!first) {
                sb.append(", ");
            }
            sb.append(p.frame.state.stateNumber);
            first = false;
        }
        sb.append(">");
        return sb.toString();
    }

    public int getLength() {
    	return length;
    }

//    private class 
//	@Override
//	public Iterator<IParseNode> iterator() {
//		// TODO Auto-generated method stub
//		return null;
//	}

}

