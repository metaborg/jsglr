/*
 * Created on 04.des.2005
 *
 * Copyright (c) 2005, Karl Trygve Kalleberg <karltk@ii.uib.no>
 * 
 * Licensed under the GNU General Public License, v2
 */
package org.spoofax.jsglr;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Tools {

    private static FileOutputStream fos;
    private static String outfile = null;
    
    public static void setOutput(String d) {
        outfile = d;
        fos = null;
    }
    
    private static void initOutput() {
        if(fos == null) {
            try {
                if(outfile == null)
                    outfile = ".jsglr-log";
                fos = new FileOutputStream(outfile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
    
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

    public static void logger(String s) {
        initOutput();
        try {
            fos.write((s + "\n").getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.err.println(s);
    }
}
