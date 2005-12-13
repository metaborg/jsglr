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

    public Frame(State s) {
        state = s;
        steps = new Vector<Step>();
    }

    public void clear() {
        // TODO Auto-generated method stub
        
    }

    public boolean allLinksRejected() {
        // TODO Auto-generated method stub
        return false;
    }

    public State peek() {
        // TODO Auto-generated method stub
        return null;
    }

    public List<Path> computePathsToRoot(int arity) {
        // TODO Auto-generated method stub
        return null;
    }

    public Frame getRoot() {
        // TODO Auto-generated method stub
        return null;
    }

    public Step findStep(Frame st0) {
        // TODO Auto-generated method stub
        return null;
    }

    public Step addStep(Frame st0, ATerm t) {
        Step s= new Step(t);
        steps.add(s);
        return s;
    }

    public boolean rejected() {
        // TODO Auto-generated method stub
        return false;
    }

    public List<Path> computePathsToRoot(int arity, Step l) {
        // TODO Auto-generated method stub
        return null;
    }

}
