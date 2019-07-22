package org.spoofax.jsglr.client;

import java.io.Serializable;

public interface IKeywordRecognizer extends Serializable {

    boolean isKeyword(String literal);
    
}
