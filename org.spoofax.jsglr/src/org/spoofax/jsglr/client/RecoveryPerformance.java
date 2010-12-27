package org.spoofax.jsglr.client;

import java.util.ArrayList;

public class RecoveryPerformance {
    
    private long startParse;
    private long parseTime;
    private long startCG;
    private ArrayList<Integer> CGTimes;
    private long startFG;
    private ArrayList<Integer> FGTimes;
    private long startBP;
    private ArrayList<Integer> BPTimes;
    private long startRecovery;
    private ArrayList<Integer> recoveryTimes;
    
    public RecoveryPerformance(){
        CGTimes=new ArrayList<Integer>();
        FGTimes=new ArrayList<Integer>();
        BPTimes=new ArrayList<Integer>();
        recoveryTimes=new ArrayList<Integer>();
    }
    
    public long getParseTime() {
        return parseTime;
    }

    public ArrayList<Integer> getCGTimes() {
        return CGTimes;
    }

    public ArrayList<Integer> getFGTimes() {
        return FGTimes;
    }

    public ArrayList<Integer> getBPTimes() {
        return BPTimes;
    }

    public ArrayList<Integer> getRecoveryTimes() {
        return recoveryTimes;
    }

    void startParse(){
        startParse=System.currentTimeMillis();
    }

    void endParse(){
        parseTime=System.currentTimeMillis()-startParse;
    }
    
    void startCG(){
        startCG=System.currentTimeMillis();
    }

    void endCG(){
        long CGTime=System.currentTimeMillis()-startCG;
        CGTimes.add((int) CGTime);
    }
    
    void startFG(){
        startFG=System.currentTimeMillis();
    }

    void endFG(){
        long FGTime=System.currentTimeMillis()-startFG;
        FGTimes.add((int)FGTime);
    }
    
    void startBP(){
        startBP=System.currentTimeMillis();
    }

    void endBP(){
        long BPTime=System.currentTimeMillis()-startBP;
        BPTimes.add((int)BPTime);
    }
    
    void startRecovery(){
        startRecovery=System.currentTimeMillis();
    }

    void endRecovery(){
        long recoveryTime=System.currentTimeMillis()-startRecovery;
        recoveryTimes.add((int)recoveryTime);
    }
}
