package org.spoofax.jsglr2.cli;

public final class WrappedException extends Exception {

    public WrappedException(String message, Exception exception) {
        super(message, exception);
    }

    public WrappedException(String message) {
        super(message);
    }

}
