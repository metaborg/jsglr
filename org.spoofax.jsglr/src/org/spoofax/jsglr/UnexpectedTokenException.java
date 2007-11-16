package org.spoofax.jsglr;

public class UnexpectedTokenException extends SGLRException {
    private static final long serialVersionUID = -2050581505177108272L;

    private final int token, offset, lineNumber, columnNumber;

    public int getOffset() {
        return offset;
    }

    public int getColumnNumber() {
        return columnNumber;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public int getToken() {
        return token;
    }

    public UnexpectedTokenException(int token, int offset, int lineNumber, int columnNumber) {
        super("Character '" + (char) token + "' unexpected at line " +
              lineNumber + ", column " + columnNumber);

        this.token = token;
        this.offset = offset;
        this.lineNumber = lineNumber;
        this.columnNumber = columnNumber;
    }
}
