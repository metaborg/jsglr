package org.spoofax.jsglr2.cli;

final class WrappedException extends Exception {

    final String message;
    final Exception exception;

    WrappedException(String message, Exception exception) {
        this.message = message;
        this.exception = exception;
    }

    WrappedException(String message) {
        this.message = message;
        this.exception = null;
    }

}
