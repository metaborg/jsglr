/*
 * Created on 04.des.2005
 *
 * Copyright (c) 2005, Karl Trygve Kalleberg <karltk@ii.uib.no>
 * 
 * Licensed under the GNU General Public License, v2
 */
package org.spoofax.jsglr;

import java.util.List;

import aterm.ATerm;

public class Production {

    public final int arity;
    public final int label;
    public final int status;
    
    // FIXME: These should be factored out in a separate constant class.
    public static final int NORMAL = Reduce.NORMAL;
    public static final int PREFER = Reduce.PREFER;
    public static final int AVOID = Reduce.AVOID; 
    public static final int REJECT = Reduce.REJECT;
    
    public Production(int arity, int label, int status) {
        this.arity = arity;
        this.label = label;
        this.status = status;
    }

    // FIXME: This is fugly
    public ATerm apply(List<ATerm> kids, ParseTable pt) {
        Label l = pt.getLabel(label);
        Tools.debug("Applying : " + kids);
        Tools.debug("      to : " + l.prod);
        return l.prod;
    }

}
