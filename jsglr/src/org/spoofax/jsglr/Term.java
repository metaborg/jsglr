/*
 * Created on 04.des.2005
 *
 * Copyright (c) 2005, Karl Trygve Kalleberg <karltk@ii.uib.no>
 * 
 * Licensed under the GNU General Public License, v2
 */
package org.spoofax.jsglr;

import java.util.List;

import jjtraveler.Visitable;
import aterm.ATerm;
import aterm.ATermAppl;
import aterm.ATermFactory;
import aterm.ATermInt;
import aterm.ATermList;

public class Term {

    public static ATermInt asInt(ATerm t) {
        return (ATermInt) t;
    }

    public static ATermInt asInt(Visitable v) {
        return (ATermInt) v;
    }
    
    public static ATermAppl asAppl(ATerm t) {
        return (ATermAppl) t;
    }

    public static ATermAppl asAppl(Visitable v) {
        return (ATermAppl) v;
    }
    
    public static int toInt(ATermInt t) {
        return t.getInt();
    }

    public static int toInt(ATerm t) {
        return ((ATermInt)t).getInt();
    }
    
    public static int intAt(ATerm pt, int i) {
        return asInt(asAppl(pt).getChildAt(i)).getInt();
    }

    public static ATermList listAt(ATerm pt, int i) {
        return asList(asAppl(pt).getChildAt(i));
    }

    private static ATermList asList(Visitable v) {
         return (ATermList) v;
    }

    public static ATermAppl applAt(ATerm pt, int i) {
        return asAppl(asAppl(pt).getChildAt(i));
    }

    public static ATermAppl applAt(ATermList pt, int i) {
        return asAppl(pt.getChildAt(i));
    }

    public static ATerm termAt(ATermList t, int i) {
        return (ATerm) t.getChildAt(i);
    }

    public static boolean isInt(ATerm t) {
        return t.getType() == ATerm.INT;
    }

    public static ATermList makeList(ATermFactory f, List<ATerm> kids) {
        ATermList ret = f.makeList();
        // FIXME: Slowest insertion method
        for(ATerm t : kids)
            ret = ret.append(t);
        return ret;
    }

}
