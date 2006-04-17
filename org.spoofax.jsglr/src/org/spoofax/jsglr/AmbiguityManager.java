/*
 * Created on 01.apr.2006
 *
 * Copyright (c) 2005, Karl Trygve Kalleberg <karltk@ii.uib.no>
 * 
 * Licensed under the GNU General Public License, v2
 */
package org.spoofax.jsglr;

import java.util.HashMap;
import java.util.Map;

public class AmbiguityManager {

    private int injectionFilterSucceededCount;
    private int eagernessFilterSucceededCount;
    private int injectionCallsCount;
    private int eagernessGreaterCallsCount;
    private int preferAndAvoidCallsCount;
    private int ambiguityCallsCount;
    private int maxNumberOfAmbiguitiesCount;
    private int ambiguitiesCount;
    private int clustersVisitedCount;
    
    private Map<AmbKey,Integer> indexTable;
    private Map<Integer, Amb> clusterTable;

    AmbiguityManager() {
        indexTable = new HashMap<AmbKey, Integer>();
        clusterTable = new HashMap<Integer, Amb>();
    }
    
    void createAmbiguityCluster(IParseNode existing, IParseNode newNode, int pos) {
        
        increaseAmbiguityCalls();
        
        Amb newAmbiguities = null;
        
        int idx = getIndex(existing, pos);
        if(idx == -1) {
            idx = increaseMaxAmbiguityCount();
            addIndex(existing, pos, idx);
            newAmbiguities = new Amb(existing, newNode);
        } else {
            Amb oldAmbiguities = getClusterOnIndex(idx);
            if(oldAmbiguities.hasAmbiguity(newNode))
                return;
            newAmbiguities = new Amb(oldAmbiguities, newNode);
        }
        
        addIndex(newNode, pos, idx);
        updateCluster(idx, newAmbiguities);
        //setAmbiguityMapSet(pos);
    }

    private Amb getClusterOnIndex(int idx) {
        return clusterTable.get(idx);
    }

    private void updateCluster(int idx, Amb cluster) {
        clusterTable.put(idx, cluster);
    }

    private void addIndex(IParseNode existing, int pos, int idx) {
        indexTable.put(new AmbKey(existing, pos), idx);
    }

    private int getIndex(IParseNode existing, int pos) {
        Integer i = indexTable.get(new AmbKey(existing, pos));
        if(i == null)
            return -1;
        return i.intValue();
    }

    private int increaseMaxAmbiguityCount() {
        return maxNumberOfAmbiguitiesCount++;
    }

    private int increaseAmbiguityCalls() {
        return ambiguityCallsCount++;
    }

    public int getAmbiguityCallsCount() {
        return ambiguityCallsCount;
    }
    
    public int getAmbiguitiesCount() {
        return ambiguitiesCount;
    }

    public int getMaxNumberOfAmbiguities() {
        return maxNumberOfAmbiguitiesCount;
    }

    public void resetClustersVisitedCount() {
        clustersVisitedCount = 0;
    }

}
