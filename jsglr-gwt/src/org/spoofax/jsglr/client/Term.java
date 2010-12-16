/*
 * Created on 04.des.2005
 *
 * Copyright (c) 2005, Karl Trygve Kalleberg <karltk near strategoxt.org>
 *
 * Licensed under the GNU Lesser General Public License, v2.1
 */
package org.spoofax.jsglr.client;

import java.util.List;

import org.spoofax.jsglr.shared.terms.ATerm;
import org.spoofax.jsglr.shared.terms.ATermAppl;
import org.spoofax.jsglr.shared.terms.ATermFactory;
import org.spoofax.jsglr.shared.terms.ATermInt;
import org.spoofax.jsglr.shared.terms.ATermList;

public class Term {

    public static ATermInt asInt(ATerm t) {
        return (ATermInt) t;
    }


    public static ATermAppl asAppl(ATerm t) {
        return (ATermAppl) t;
    }

    public static int toInt(ATermInt t) {
        return t.getInt();
    }

    public static int toInt(ATerm t) {
        return ((ATermInt)t).getInt();
    }

    public static int intAt(ATerm pt, int i) {
        return asInt(pt.getChildAt(i)).getInt();
    }

    public static ATermList listAt(ATerm pt, int i) {
        return termAt(pt, i);
    }

    public static ATermAppl applAt(ATerm pt, int i) {
        return termAt(pt, i);
    }

    @SuppressWarnings("unchecked") // casting is inherently unsafe, but doesn't warrant a warning here
    public static<T extends ATerm> T termAt(ATerm t, int i) {
        return (T) t.getChildAt(i);
    }

    public static boolean isInt(ATerm t) {
        return t.getType() == ATerm.INT;
    }

//    public static ATermList makeList(ATermFactory f, List<ATerm> kids) {
//    	return f.makeList(kids);
//    }

    public static boolean isAppl(ATerm t) {
        return t.getType() == ATerm.APPL;
    }

}
