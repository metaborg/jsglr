package org.spoofax.jsglr;

import java.io.IOException;

//Todo: better use abstract class?
/*
 * Interface for recovery algorithms. 
 * Allows for preserving parse information relevant for recovering (characters, stacks as start point for recovering), 
 * contains method for handling recover productions, method for a recover algorithm
 */
public interface IRecoverAlgorithm {
    
    /*
     * keeps information and reset algorithm variables
     */
    void initialize();
    
    /*
     * Allows for preserving stacks to allow backtracking
     */
    void afterParseStep();
    
    /*
     * Allows for preserving characterstream
     */
    void afterStreamRead(int currToken);
    
    /*
     * If true, then recover productions are not reduced,
     * if false then they are handled like normal productions.
     */
    boolean haltsOnRecoverProduction(Frame st0);
    
    /*
     * Allows to handle a recover production
     */
    void handleRecoverProduction(Frame st0, State s, int length,
            int numberOfAvoids, IParseNode t);
    
    /*
     * Determined when the recover function is called
     */
    boolean meetsRecoverCriteria();
    
    /*
     * Tries to find a recovering.
     * After the recovery has succeeded, normal parsing continues on current parse position  
     */
    void recover() throws IOException;
    
    long getRecoverTime();
}
