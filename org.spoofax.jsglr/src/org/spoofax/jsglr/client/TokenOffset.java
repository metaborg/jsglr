package org.spoofax.jsglr.client;

public class TokenOffset {
    
    private int token;
    private int offset;
    private char tokenChar;
    
    public TokenOffset() {
        token = -1;
        offset = -1;
        tokenChar = (char)-1;        
    }
    
    public TokenOffset(int token, int offset) {
        this.token = token;
        this.offset = offset;
        tokenChar = (char)token;
    }
    
    public int getToken() {
        return token;
    }
    public void setToken(int token) {
        this.token = token;
        this.tokenChar=(char) token;
    }
    public int getOffset() {
        return offset;
    }
    public void setOffset(int offset) {
        this.offset = offset;

    }
    
    @Override public String toString() {
        // TODO Auto-generated method stub
        return "token: " + token + "\ntokenchar: " + (char)token + "\noffset: " + offset;
    }
    
    

}
