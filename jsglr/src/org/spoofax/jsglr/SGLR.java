/*
 * Created on 03.des.2005
 *
 * Copyright (c) 2005, Karl Trygve Kalleberg <karltk@ii.uib.no>
 * 
 * Licensed under the GNU General Public License, v2
 */
package org.spoofax.jsglr;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import aterm.ATerm;
import aterm.pure.PureFactory;

public class SGLR {

    private PureFactory factory;
    
    SGLR() {
        factory= new PureFactory();
    }
    
    public boolean loadParseTable(InputStream r) throws IOException {
        ATerm pt= factory.readFromFile(r);
        
        return computeParseTable(pt);
    }

    private boolean computeParseTable(ATerm pt) {
        // TODO Auto-generated method stub
        return false;
    }

    public ATerm parse(FileInputStream fis) {
        // TODO Auto-generated method stub
        return null;
    }
}
