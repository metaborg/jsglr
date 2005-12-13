/*
 * Created on 06.des.2005
 *
 * Copyright (c) 2005, Karl Trygve Kalleberg <karltk@ii.uib.no>
 * 
 * Licensed under the GNU Lesser General Public License, v2.1
 */
package org.spoofax.jsglr;

import aterm.ATermAppl;

public class Label {

    public final int labelNumber;
    public final ATermAppl prod;
    
    public Label(int labelNumber, ATermAppl prod) {
        this.labelNumber = labelNumber;
        this.prod = prod;
    }

}
