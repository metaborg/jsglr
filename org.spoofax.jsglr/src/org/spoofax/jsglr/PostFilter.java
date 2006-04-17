/*
 * Created on 11.apr.2006
 *
 * Copyright (c) 2005, Karl Trygve Kalleberg <karltk@ii.uib.no>
 * 
 * Licensed under the GNU General Public License, v2
 */
package org.spoofax.jsglr;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import aterm.ATerm;

public class PostFilter {

    SGLR parser;
    AmbiguityManager ambiguityManager;
    ParseTable parseTable;
    
    Map<Object, Integer> posTable;
    Map<Object, IParseNode> resolvedTable;
    private boolean[] inputAmbiguityMap;  
    
    PostFilter(SGLR parser) {
        this.parser = parser;
    }
    
    private void initializeFromParser() {
        parseTable = parser.getParseTable();
        ambiguityManager = parser.getAmbiguityManager();
    }

    protected ATerm parseResult(IParseNode root, String sort, int inputLength) {
        
        initializeAmbiguityMaps(inputLength);
        initializeFromParser();
        
        IParseNode t = root;
        AmbiguityManager ambMgr = parser.getAmbiguityManager();
        ParseTable parseTable = parser.getParseTable();
        
        if(sort != null) {
             t = selectOnTopSort();
             if(t == null) {
                 return parseError("Desired top sort not found");
             }
        }
        
        if(parser.isDetectCyclesEnabled()) {
            if(ambMgr.getMaxNumberOfAmbiguities() > 0) {
                if(isCyclicTerm(t))
                    parseError("Term is cyclic");
            }
        }
        
        if(parser.isFilteringEnabled()) {
            t = filterTree(t);
        }
        
        if(t != null) {
            ATerm r = yieldTree(t);
            int ambCount = ambMgr.getAmbiguitiesCount();
            return parseTable.getFactory().parse("parsetree(" + r + "," + ambCount + ")");
        }
        
        return null;
    }
    
    private void initializeAmbiguityMaps(int inputLength) {
        inputAmbiguityMap = new boolean[inputLength];
    }

    private ATerm yieldTree(IParseNode t) {
        return t.toParseTree(parser.getParseTable());
    }

    private IParseNode filterTree(IParseNode t) {
        
        ambiguityManager.resetClustersVisitedCount();
        
        IParseNode newT = filterTree(t, 0, false);
        
        return newT;
    }

    private IParseNode filterTree(IParseNode t, int pos, boolean inAmbiguityCluster) {
        
        List<IParseNode> ambs = null;
        Object key;
        IParseNode newT;
        
        // If APPL
        if(inputAmbiguityMapIsSet(pos)) {
             ambs = getCluster(t, pos);
        } else {
            ambs = getEmptyList();
        }
        
        if(!inAmbiguityCluster && !ambs.isEmpty()) {
            key = createAmbiguityKey(ambs, pos);
            
            newT = resolvedTable.get(key);
            if(newT == null) {
                newT = filterAmbiguities(ambs, pos);
                if(newT != null) {
                    resolvedTable.put(key, newT);
                    posTable.put(key, pos);
                } else {
                    return null;
                }
            } else {
                // FIXME
                pos = posTable.get(key);
            }
            t = newT;
        } else {
            List<IParseNode> args = ((Node)t).getKids();
            List<IParseNode> newArgs = filterTree(args, pos, false);
            
            if(parser.isFilteringEnabled()) {
                if(parser.isRejectFilterEnabled() && parseTable.hasRejects()) {
                    if(hasRejectProd(t))
                        return null;
                }
            }
            
            if(newArgs != null) {
                t = null;//t.replaceArgs(newArgs);
            } else {
                return null;
            }
        }
        
        return null;
    }

    private List<IParseNode> tail(List<IParseNode> list) {
        // FIXME This is ugly and slow!
        List<IParseNode> tail = new LinkedList<IParseNode>();
        tail.addAll(list);
        tail.remove(0); 
        return tail;
    }
    
    private List<IParseNode> filterTree(List<IParseNode> args, int pos, boolean inAmbiguityCluster) {

        List<IParseNode> t = args;
        
        if(!args.isEmpty()) {
            IParseNode arg = args.get(0);
            
            List<IParseNode> tail = tail(args);
            IParseNode newArg = filterTree(arg, pos, false);
            
            List<IParseNode> newTail;
            
            if(tail.isEmpty()) {
                newTail = getEmptyList();
            } else {
                newTail = filterTree(tail, pos, false);
                
                if(newTail == null)
                    return null;
            }
            
            if (newArg != null) {
                if(!arg.equals(newArg) || !tail.equals(newTail)) {
                    newTail.add(0, newArg);
                    t = newTail;
                }
            } else {
                return null;
            }
        }
        
        if(parser.isFilteringEnabled()) {
            List<IParseNode> filtered = new ArrayList<IParseNode>();
            for(IParseNode n : t)
                filtered.add(applyAssociativityPriorityFilter(n));
            return filtered;
        } else {
            return t;
        }
    }

    private IParseNode applyAssociativityPriorityFilter(IParseNode t) {
        
        IParseNode r = t;

        if(t instanceof Amb) {
            Label prodLabel = getProductionLabel(t);
            
            if(parser.isAssociativityFilterEnabled()) {
                if(prodLabel.isLeftAssociative()) {
                    r = applyLeftAssociativeFilter(t, prodLabel); 
                } else if(prodLabel.isRightAssociative()) {
                    throw new NotImplementedException();
                    //r = applyRightAssociativeFilter(t, prodLabel);
                }
            }
            
            if(r != null && parser.isPriorityFilterEnabled()) {
                if(lookupGtrPriority(prodLabel) != null) {
                    return applyPriorityFilter(r, prodLabel);
                }
            }
        }
        
        return r;
    }
    
    private IParseNode applyPriorityFilter(IParseNode r, Label prodLabel) {
    
        List<IParseNode> newAmbiguities = new ArrayList<IParseNode>();
        List<IParseNode> alternatives = ((Amb)r).getAlternatives();
        int l0 = prodLabel.labelNumber;
        
        for(IParseNode alt : alternatives) {
            IParseNode injection = jumpOverInjections(alt);
            
            if(injection instanceof Amb) {
                
            } else {
                int l1 = ((Node)injection).label;
                if(hasGreaterPriority(l0, l1)) {
                    return null;
                }
            }
        }
        
        
        return null;
    }

    private IParseNode jumpOverInjections(IParseNode alt) {
       
        if(!(alt instanceof Amb)) {
            int prod = ((Node)alt).label;
            while(isUserDefinedLabel(prod)) {
                List<IParseNode> kids = ((Node)alt).getKids();
                throw new NotImplementedException();
            }
        } else {
            return alt;
        }
        
        return null;
    }

    private boolean isUserDefinedLabel(int prod) {
        return parseTable.lookupInjection(prod) != null;
    }

    /** Returns true if the first production has higher priority than the
     * second.
     * 
     * @param l0
     * @param l1
     * @return true if production l0 has greater priority than l1 
     */
    private boolean hasGreaterPriority(int l0, int l1) {
        throw new NotImplementedException();
    }

    private List lookupGtrPriority(Label prodLabel) {
        return parseTable.getPriorities(prodLabel);
    }

    private IParseNode applyLeftAssociativeFilter(IParseNode t, Label prodLabel) {
        
        List<IParseNode> newAmbiguities = new ArrayList<IParseNode>();
        List<IParseNode> alternatives = ((Amb)t).getAlternatives();
        IParseNode last = alternatives.get(alternatives.size() - 1);
        
        if(last instanceof Amb) {
            List<IParseNode> rest = new ArrayList<IParseNode>();
            rest.addAll(alternatives);
            rest.remove(rest.size() - 1);
            
            List<IParseNode> ambs = ((Amb)last).getAlternatives();
            for(IParseNode amb : ambs) {
                Label other = parseTable.getLabel(((Node)amb).getLabel());
                if(!prodLabel.equals(other)) {
                    newAmbiguities.add(amb);
                }
            }
            
            if(!newAmbiguities.isEmpty()) {
                if(newAmbiguities.size() > 1) { 
                    last = new Amb(newAmbiguities);
                } else {
                    last = newAmbiguities.get(0);
                }
                rest.add(last);
                return new Amb(rest);
            } else {
                return null;
            }
        } else if(last instanceof Node) {
            Label other = parseTable.getLabel(((Node)last).getLabel());
            if(prodLabel.equals(other)) {
                return null;
            }
        }
        return null;
    }

    private Label getProductionLabel(IParseNode t) {
        if(t instanceof Node) {
            return parseTable.getLabel(((Node)t).getLabel());
        } else if (t instanceof ParseProductionNode) {
            return parseTable.getLabel(((ParseProductionNode)t).getProduction());
        }
        return null;
    }

    private boolean hasRejectProd(IParseNode t) {
        return t instanceof ParseReject;
    }

    private IParseNode filterAmbiguities(List<IParseNode> ambs, int pos) {
        throw new NotImplementedException();
    }

    private Object createAmbiguityKey(List<IParseNode> ambs, int pos) {
        throw new NotImplementedException();
    }

    private List<IParseNode> getEmptyList() {
        return new ArrayList<IParseNode>(0);
    }

    private List<IParseNode> getCluster(IParseNode t, int pos) {
        throw new NotImplementedException();
    }

    private boolean inputAmbiguityMapIsSet(int pos) {
        return inputAmbiguityMap[pos];
    }

    private boolean isCyclicTerm(IParseNode t) {
        throw new NotImplementedException();
    }

    private ATerm parseError(String msg) {
        System.err.println("Parse error: " + msg);
        return null;
    }

    private IParseNode selectOnTopSort() {
        throw new NotImplementedException();
    }

}
