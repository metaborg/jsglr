/*
 * Created on 04.des.2005
 *
 * Copyright (c) 2005, Karl Trygve Kalleberg <karltk@ii.uib.no>
 * 
 * Licensed under the GNU General Public License, v2
 */
package org.spoofax.jsglr;

public class Tools {

    public static void debug(String s) {
        System.out.println(s);
    }

    public static void debug(int stateNumber) {
        debug("" + stateNumber);
    }

    public static void debug(Object o) {
        if (o == null)
            debug(null);
        else
            debug(o.toString());
    }

    public static void logger(String string) {
        System.out.println(string);
    }
}
