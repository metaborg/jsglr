package org.spoofax.jsglr;

import java.io.IOException;

/*
 * No recovery is implemented 
 */
public class NoRecovery implements IRecoverAlgorithm {

    public void handleRecoverProduction(Frame st0, State s, int length,
            int numberOfAvoids, IParseNode t) {       
        
    }

    public void afterStreamRead(int currToken) {
        
    }

    public void afterParseStep() {
       
    }

    public void initialize() {
        
    }

    public void recover() throws IOException {
        
    }
    
    public long getRecoverTime() {
        return 0;
    }

    public boolean meetsRecoverCriteria() {        
        return false;
    }
    
    public boolean haltsOnRecoverProduction(Frame st0) {
        return false;
    }

}
