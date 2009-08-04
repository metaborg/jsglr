/*
 * Created on 3. aug.. 2006
 *
 * Copyright (c) 2005, Karl Trygve Kalleberg <karltk near strategoxt.org>
 * 
 * Licensed under the GNU General Public License, v2
 */
package org.spoofax.jsglr;

public class SGLRException extends Exception {

    private static final long serialVersionUID = -8467572969588110480L;

    public SGLRException() {}
    
    public SGLRException(String message) {
        super(message);
    }
}
