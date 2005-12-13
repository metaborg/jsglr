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
        
        List<ATerm> labels = new Vector<ATerm>(steps.size());
        
        for(Step s : steps)
            labels.add(s.label);
        
        return labels;
    }

    public void addStep(Step s) {
        steps.add(s);
    }

}
