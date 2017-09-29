package org.spoofax.jsglr2;

import org.spoofax.jsglr2.parser.Parse;

public abstract class JSGLR2Result {
    
    public final Parse parse;
    public final boolean isSuccess;
    
    protected JSGLR2Result(Parse parse, boolean isSuccess) {
        this.parse = parse;
        this.isSuccess = isSuccess;
    }

}
