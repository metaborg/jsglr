/*
 * Created on 04.des.2005
 *
 * Copyright (c) 2005, Karl Trygve Kalleberg <karltk@ii.uib.no>
 * 
 * Licensed under the GNU Lesser General Public License, v2.1
 */
package org.spoofax.jsglr;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.BufferedOutputStream;
import java.io.OutputStream;

public class Tools {
    
    private static OutputStream fos;
    private static String outfile = null;

    static {
    	initOutput();
    }
    
    public static void setOutput(String d) {
        outfile = d;
        initOutput();
    }

    private static void initOutput() {
        if(fos == null) {
            try {
                if(outfile == null)
                    outfile = ".jsglr-log";
                fos = new BufferedOutputStream(new FileOutputStream(outfile));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
    
    public static void debug(Object ...s) {
        // FIXME Copy debug() from org.spoofax.interpreter
        for(Object o : s) {
            System.err.print(o);
        }
        System.err.println("");
    }

    public static void logger(Object ...s) {
        try {
            for(Object o : s)
                fos.write(o.toString().getBytes());
            fos.write("\n".getBytes());
            fos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static boolean debugging = false;
    static boolean logging = false;
    static boolean tracing = false;
 
    public static void setTracing(boolean enableTracing) {
        tracing = enableTracing;
    }
    
    public static void setDebug(boolean enableDebug) {
        debugging = enableDebug;
    }

    public static void setLogging(boolean enableLogging) {
        logging = enableLogging;
        setOutput(".jsglr-log");
    }
}
