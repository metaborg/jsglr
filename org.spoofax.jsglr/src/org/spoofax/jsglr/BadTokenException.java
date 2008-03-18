package org.spoofax.jsglr;

/**
 * Exception thrown when a specific token was unexpected by the parser.
 * 
 * @author Lennart Kats <L.C.L.Kats add tudelft.nl>
 */
public class BadTokenException extends SGLRException {
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
    
    public boolean isEOFToken() {
        return token == SGLR.EOF;
    }
    
    public String getMessage() {
        return getShortMessage() + " at line " + lineNumber + ", column " + columnNumber;
    }
    
    public String getShortMessage() {
        if (isEOFToken())
            return "Unexpected end of file";
        else
            return "Syntax near unexpected character '" + (char) token + "'";
    }

    public BadTokenException(int token, int offset, int lineNumber, int columnNumber) {
        this.token = token;
        this.offset = offset;
        this.lineNumber = lineNumber;
        this.columnNumber = columnNumber;
    }
}
