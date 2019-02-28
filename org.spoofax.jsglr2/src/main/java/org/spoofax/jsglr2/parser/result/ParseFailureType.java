package org.spoofax.jsglr2.parser.result;

public enum ParseFailureType {

    Unknown("unknown parsing failure"),
    InvalidStartSymbol("invalid start symbol");
    
    public final String message;

    ParseFailureType(String message) {
        this.message = message;
    }

}