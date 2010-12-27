/*
 * Created on 04.des.2005
 *
 * Copyright (c) 2005, Karl Trygve Kalleberg <karltk near strategoxt.org>
 *
 * Licensed under the GNU Lesser General Public License, v2.1
 */
package org.spoofax.jsglr.shared;

import org.spoofax.jsglr.client.Measures;
import org.spoofax.jsglr.shared.terms.ATerm;
import org.spoofax.jsglr.shared.terms.ATermAppl;
import org.spoofax.jsglr.shared.terms.ATermInt;
import org.spoofax.jsglr.shared.terms.ATermString;


public class Tools {

    public static boolean debugging = false;
    public static boolean logging = false;
    public static boolean tracing = false;
    public static boolean measuring = false;

    private static Measures measures;

    public static void debug(Object ...s) {
    	if(debugging)
    		System.err.println(s);
    }

    public static void logger(Object ...s) {
    	if(logging)
    		System.out.println(s);
    }

    public static void setTracing(boolean enableTracing) {
        tracing = enableTracing;
    }

    public static void setDebug(boolean enableDebug) {
        debugging = enableDebug;
    }

    public static void setLogging(boolean enableLogging) {
        logging = enableLogging;
    }

    // Measuring

    public static void setMeasuring(boolean enableMeasuring) {
        measuring = enableMeasuring;
    }

    public static void setMeasures(Measures m) {
        measures = m;
    }

    public static Measures getMeasures() {
        return measures;
    }
    
    // Terms

    public static ATermAppl applAt(ATerm pt, int i) {
        return termAt(pt, i);
    }
    
    public static String asJavaString(ATerm s) {
    	return ((ATermString) s).getString();
    }

    @SuppressWarnings("unchecked") // casting is inherently unsafe, but doesn't warrant a warning here
    public static<T extends ATerm> T termAt(ATerm t, int i) {
        return (T) t.getChildAt(i);
    }
    
    public static ATermAppl asAppl(ATerm t) {
        return (ATermAppl) t;
    }

    public static boolean isAppl(ATerm t) {
        return t.getType() == ATerm.APPL;
    }
    
    public static int intAt(ATerm pt, int i) {
        return ((ATermInt) pt.getChildAt(i)).getInt();
    }


}
