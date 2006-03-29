/*
 * Created on 04.des.2005
 *
 * Copyright (c) 2005, Karl Trygve Kalleberg <karltk@ii.uib.no>
 * 
 * Licensed under the GNU Lesser General Public License, v2.1
 */
package org.spoofax.jsglr;

import java.util.List;

import aterm.ATerm;
import aterm.AFun;
import aterm.ATermFactory;
import aterm.ATermList;

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
        AFun fun;
        ATermFactory pf = pt.getFactory();

        // FIXME: These AFuns are constants, but require a live
        // factory to work.
        switch (status) {
        case NORMAL:
            fun = pf.makeAFun("regular", 2, false);
            break;
        case REJECT:
            fun = pf.makeAFun("reject", 2, false);
            break;
        case PREFER:
            fun = pf.makeAFun("prefer", 2, false);
            break;
        case AVOID:
            fun = pf.makeAFun("avoid", 2, false);
            break;
        default:
            fun = null;
        }

        AFun prod = pf.makeAFun("aprod", 1, false);
        ATerm prodLabel = pf.makeAppl(prod, pf.makeInt(label));

        ATermList l = Term.makeList(pf, kids);
        ATerm t = pf.makeAppl(fun, prodLabel, l);
        
        return t;
    }

    boolean isReject() {
        return status == REJECT;
    }
    
    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Production))
            return false;
        Production o = (Production)obj;
        return arity == o.arity && label == o.label && status == o.status;
    }
}
