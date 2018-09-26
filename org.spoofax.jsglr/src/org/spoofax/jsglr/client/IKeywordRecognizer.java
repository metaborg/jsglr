package org.spoofax.jsglr.client;

import java.io.Serializable;

public interface IKeywordRecognizer extends Serializable {

    public boolean isKeyword(String literal);
    
}
