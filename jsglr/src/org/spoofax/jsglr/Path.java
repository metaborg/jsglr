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

public class Path {

    private List<Step> steps;
    
    public Path() {
        steps = new Vector<Step>();
    }
    
    public List<ATerm> collectTerms() {
        
        List<ATerm> ret = new Vector<ATerm>(steps.size());
        
        for(Step s : steps)
            ret.add(s.label);
        
        return ret;
    }

    public void addStep(Step s) {
        steps.add(s);
    }

    public Step getRoot() {
        if(steps.size() < 1) 
            return null;
        return steps.get(steps.size()-1);
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("< ");
        for(Step s : steps) {
            sb.append( s.destination.state.stateNumber + ", ");
        }   
        sb.append(">");
        return sb.toString();
    }
}
