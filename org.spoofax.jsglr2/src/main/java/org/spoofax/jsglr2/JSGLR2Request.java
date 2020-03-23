package org.spoofax.jsglr2;

public class JSGLR2Request {

    public final String input;
    public final String fileName;
    public final String startSymbol;

    public JSGLR2Request(String input) {
        this.input = input;
        this.fileName = "";
        this.startSymbol = null;
    }

    public JSGLR2Request(String input, String fileName) {
        this.input = input;
        this.fileName = fileName;
        this.startSymbol = null;
    }

    public JSGLR2Request(String input, String fileName, String startSymbol) {
        this.input = input;
        this.fileName = fileName;
        this.startSymbol = startSymbol;
    }

}
