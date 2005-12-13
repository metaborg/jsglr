/*
 * Created on 03.des.2005
 *
 * Copyright (c) 2005, Karl Trygve Kalleberg <karltk@ii.uib.no>
 * 
 * Licensed under the GNU General Public License, v2
 */
package org.spoofax.jsglr;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import aterm.ATerm;

public class Main {

    public static void main(String[] args) throws FileNotFoundException, IOException, InvalidParseTableException {
        
        SGLR sglr= new SGLR();
        
        sglr.loadParseTable(new FileInputStream(args[0]));
        FileInputStream fis= new FileInputStream(args[1]);
        
        ATerm t= sglr.parse(fis);
        System.out.println(t);
    }
}
