/*
 * Created on 03.des.2005
 *
 * Copyright (c) 2005, Karl Trygve Kalleberg <karltk@ii.uib.no>
 * 
 * Licensed under the GNU Lesser General Public License, v2.1
 */
package org.spoofax.jsglr;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import aterm.ATerm;

public class Main {

    public static void main(String[] args) throws FileNotFoundException, IOException, InvalidParseTableException {
        
        SGLR sglr= new SGLR();
        
        if(args.length < 2) {
            System.out.println("Usage: org.spoofax.jsglr.Main <parsetable.tbl> <inputfile>");
            System.exit(-1);
        }
        sglr.loadParseTable(new FileInputStream(args[0]));
        FileInputStream fis= new FileInputStream(args[1]);
        
        ATerm t= sglr.parse(fis);
        System.out.println(t);
    }
}
