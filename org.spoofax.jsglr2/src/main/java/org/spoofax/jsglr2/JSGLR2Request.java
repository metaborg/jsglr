package org.spoofax.jsglr2;

import java.util.Optional;

public class JSGLR2Request {

    public final String input;
    public final String fileName;
    public final String startSymbol;
    public final Optional<Integer> completionCursorOffset;

    public JSGLR2Request(String input) {
        this(input, "");
    }

    public JSGLR2Request(String input, String fileName) {
        this(input, fileName, null);
    }

    public JSGLR2Request(String input, String fileName, String startSymbol) {
        this(input, fileName, startSymbol, Optional.empty());
    }

    public JSGLR2Request(String input, String fileName, String startSymbol, Optional<Integer> completionCursorOffset) {
        this.input = input;
        this.fileName = fileName;
        this.startSymbol = startSymbol;
        this.completionCursorOffset = completionCursorOffset;
    }

    public boolean isCompletion() {
        return completionCursorOffset.isPresent();
    }

}
