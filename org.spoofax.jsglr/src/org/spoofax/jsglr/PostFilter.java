/*
 * Created on 11.apr.2006
 *
 * Copyright (c) 2005, Karl Trygve Kalleberg <karltk@ii.uib.no>
 * 
 * Licensed under the GNU General Public License, v2
 */
package org.spoofax.jsglr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import aterm.AFun;
import aterm.ATerm;

public class PostFilter {

    private static final int FILTER_DRAW = 1;

    private static final int FILTER_LEFT_WINS = 2;

    private static final int FILTER_RIGHT_WINS = 3;

    SGLR parser;

    AmbiguityManager ambiguityManager;

    ParseTable parseTable;

    Map<Object, Integer> posTable;

    Map<AmbKey, IParseNode> resolvedTable;

    private AmbiguityMap markMap;

    private int parseTreePosition;

    PostFilter(SGLR parser) {
        this.parser = parser;
        resolvedTable = new HashMap<AmbKey, IParseNode>();
    }

    private void initializeFromParser() {
        parseTable = parser.getParseTable();
        ambiguityManager = parser.getAmbiguityManager();
    }

    protected ATerm parseResult(IParseNode root, String sort, int inputLength) {

        initializeFromParser();

        IParseNode t = root;
        AmbiguityManager ambMgr = parser.getAmbiguityManager();
        ParseTable parseTable = parser.getParseTable();

        if (SGLR.isDebugging()) {
            Tools.debug("pre-select: ", t);
        }

        if (sort != null) {
            t = selectOnTopSort();
            if (t == null) {
                return parseError("Desired top sort not found");
            }
        }

        if (SGLR.isDebugging()) {
            Tools.debug("pre-cycle detect: ", t);
        }

        if (parser.isDetectCyclesEnabled()) {
            if (ambMgr.getMaxNumberOfAmbiguities() > 0) {
                if (isCyclicTerm(t))
                    parseError("Term is cyclic");
            }
        }

        if (SGLR.isDebugging()) {
            Tools.debug("pre-filtering detect: ", t);
        }

        if (parser.isFilteringEnabled()) {
            t = filterTree(t);
        }

        if (SGLR.isDebugging()) {
            Tools.debug("pre-yield: ", t);
        }

        if (t != null) {
            ATerm r = yieldTree(t);
            int ambCount = ambMgr.getAmbiguitiesCount();
            if (SGLR.isDebugging()) {
                Tools.debug("yield: ", r);
            }
            final AFun parseTreeAfun = parseTable.getFactory().makeAFun("parsetree", 2, false);
            return parseTable.getFactory().makeAppl(parseTreeAfun, r,
                                                    parseTable.getFactory().makeInt(ambCount));
        }

        return null;
    }

    private ATerm yieldTree(IParseNode t) {
        return t.toParseTree(parser.getParseTable());
    }

    private IParseNode filterTree(IParseNode t) {
        ambiguityManager.resetClustersVisitedCount();
        return filterTree(t, 0, false);
    }

    private IParseNode filterTree(IParseNode t, int pos, boolean inAmbiguityCluster) {

        if (SGLR.isDebugging()) {
            Tools.debug("filterTree() - " + t.getClass());
        }

        List<IParseNode> ambs = null;
        AmbKey key;
        IParseNode newT;

        // If APPL
        if (ambiguityManager.isInputAmbiguousAt(pos)) {
            ambs = getCluster(t, pos);
        } else {
            ambs = getEmptyList();
        }

        if (!inAmbiguityCluster && (ambs != null && !ambs.isEmpty())) {

            key = new AmbKey(new Amb(ambs), pos);
            newT = resolvedTable.get(key);
            if (newT == null) {
                newT = filterAmbiguities(ambs, pos);
                if (newT != null) {
                    resolvedTable.put(key, newT);
                    posTable.put(key, pos);
                } else {
                    return null;
                }
            } else {
                parseTreePosition = posTable.get(key);
            }
            t = newT;
        } else if (t instanceof ParseNode) {
            ParseNode node = (ParseNode) t;
            List<IParseNode> args = node.getKids();
            List<IParseNode> newArgs = filterTree(args, pos, false);

            if (parser.isFilteringEnabled()) {
                if (parser.isRejectFilterEnabled() && parseTable.hasRejects()) {
                    if (hasRejectProd(t))
                        return null;
                }
            }

            if (newArgs != null) {
                t = new ParseNode(node.label, newArgs);
            } else {
                return null;
            }
        } else {
            return t;
        }

        if (parser.isFilteringEnabled()) {
            return applyAssociativityPriorityFilter(t);
        } else {
            return t;
        }
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

        if (!args.isEmpty()) {
            IParseNode arg = args.get(0);

            List<IParseNode> tail = tail(args);
            IParseNode newArg = filterTree(arg, pos, false);

            List<IParseNode> newTail;

            if (tail.isEmpty()) {
                newTail = getEmptyList();
            } else {
                newTail = filterTree(tail, pos, false);

                if (newTail == null)
                    return null;
            }

            if (newArg != null) {
                if (!arg.equals(newArg) || !tail.equals(newTail)) {
                    newTail.add(0, newArg);
                    t = newTail;
                }
            } else {
                return null;
            }
        }

        if (parser.isFilteringEnabled()) {
            List<IParseNode> filtered = new ArrayList<IParseNode>();
            for (IParseNode n : t)
                filtered.add(applyAssociativityPriorityFilter(n));
            return filtered;
        } else {
            return t;
        }
    }

    private IParseNode applyAssociativityPriorityFilter(IParseNode t) {

        IParseNode r = t;

        if (t instanceof Amb) {
            Label prodLabel = getProductionLabel(t);

            if (parser.isAssociativityFilterEnabled()) {
                if (prodLabel.isLeftAssociative()) {
                    r = applyLeftAssociativeFilter(t, prodLabel);
                } else if (prodLabel.isRightAssociative()) {
                    throw new NotImplementedException();
                    // r = applyRightAssociativeFilter(t, prodLabel);
                }
            }

            if (r != null && parser.isPriorityFilterEnabled()) {
                if (lookupGtrPriority(prodLabel) != null) {
                    return applyPriorityFilter(r, prodLabel);
                }
            }
        }

        return r;
    }

    private IParseNode applyPriorityFilter(IParseNode r, Label prodLabel) {

        List<IParseNode> newAmbiguities = new ArrayList<IParseNode>();
        List<IParseNode> alternatives = ((Amb) r).getAlternatives();
        int l0 = prodLabel.labelNumber;

        for (IParseNode alt : alternatives) {
            IParseNode injection = jumpOverInjections(alt);

            if (injection instanceof Amb) {

            } else {
                int l1 = ((ParseNode) injection).label;
                if (hasGreaterPriority(l0, l1)) {
                    return null;
                }
            }
        }

        return null;
    }

    private IParseNode jumpOverInjections(IParseNode alt) {

        if (!(alt instanceof Amb)) {
            int prod = ((ParseNode) alt).label;
            while (isUserDefinedLabel(prod)) {
                List<IParseNode> kids = ((ParseNode) alt).getKids();
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

    /**
     * Returns true if the first production has higher priority than the second.
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
        List<IParseNode> alternatives = ((Amb) t).getAlternatives();
        IParseNode last = alternatives.get(alternatives.size() - 1);

        if (last instanceof Amb) {
            List<IParseNode> rest = new ArrayList<IParseNode>();
            rest.addAll(alternatives);
            rest.remove(rest.size() - 1);

            List<IParseNode> ambs = ((Amb) last).getAlternatives();
            for (IParseNode amb : ambs) {
                Label other = parseTable.getLabel(((ParseNode) amb).getLabel());
                if (!prodLabel.equals(other)) {
                    newAmbiguities.add(amb);
                }
            }

            if (!newAmbiguities.isEmpty()) {
                if (newAmbiguities.size() > 1) {
                    last = new Amb(newAmbiguities);
                } else {
                    last = newAmbiguities.get(0);
                }
                rest.add(last);
                return new Amb(rest);
            } else {
                return null;
            }
        } else if (last instanceof ParseNode) {
            Label other = parseTable.getLabel(((ParseNode) last).getLabel());
            if (prodLabel.equals(other)) {
                return null;
            }
        }
        return null;
    }

    private Label getProductionLabel(IParseNode t) {
        if (t instanceof ParseNode) {
            return parseTable.getLabel(((ParseNode) t).getLabel());
        } else if (t instanceof ParseProductionNode) {
            return parseTable.getLabel(((ParseProductionNode) t).getProduction());
        }
        return null;
    }

    private boolean hasRejectProd(IParseNode t) {
        return t instanceof ParseReject;
    }

    private IParseNode filterAmbiguities(List<IParseNode> ambs, int pos) {

        int savedPos = parseTreePosition;

        if (parser.isRejectFilterEnabled() && parseTable.hasRejects()) {
            for (IParseNode amb : ambs) {
                if (amb instanceof Amb) {
                    filterTree(amb, pos, true);
                    return null;
                }
            }
        }

        List<IParseNode> newAmbiguities = new LinkedList<IParseNode>();

        for (IParseNode amb : ambs) {
            parseTreePosition = savedPos;
            IParseNode t = filterTree(amb, pos, true);
            if (t != null)
                newAmbiguities.add(t);
        }

        if (parser.isFilteringEnabled()) {
            // FIXME ??
            if (newAmbiguities.size() > 1) {
                List<IParseNode> oldAmbiguities = new LinkedList<IParseNode>();
                oldAmbiguities.addAll(newAmbiguities);
                for (IParseNode amb : oldAmbiguities) {
                    if (newAmbiguities.remove(amb)) {
                        newAmbiguities = filterAmbiguityList(newAmbiguities, amb);
                    }
                }
            }
        }

        if (newAmbiguities.isEmpty())
            return null;

        if (newAmbiguities.size() == 1)
            return newAmbiguities.get(0);

        return new Amb(newAmbiguities);
    }

    private List<IParseNode> filterAmbiguityList(List<IParseNode> ambiguities, IParseNode t) {

        boolean keepT = true;
        List<IParseNode> r = new LinkedList<IParseNode>();

        if (ambiguities.isEmpty()) {
            r.add(t);
            return r;
        }

        for (IParseNode amb : ambiguities) {

            switch (filter(t, amb)) {
            case FILTER_DRAW:
                r.add(amb);
                break;
            case FILTER_RIGHT_WINS:
                r.add(amb);
                keepT = false;
            }
        }

        if (keepT) {
            r.add(t);
        }

        return r;
    }

    private int filter(IParseNode left, IParseNode right) {

        if (left.equals(right)) {
            return FILTER_LEFT_WINS;
        }

        // FIXME priority filter == preferences?
        if (parser.isPriorityFilterEnabled() && parseTable.hasPriorities()) {
            int r = filterOnIndirectPrefers(left, right);
            if (r != FILTER_DRAW)
                return r;
        }
        if(parser.isPriorityFilterEnabled() && parseTable.hasPriorities()) {
            int r = filterOnPreferCount(left, right);
            if(r != FILTER_DRAW)
                return r;
        }
        
        if(parser.isInjectionCountFilterEnabled()) {
            int r = filterOnInjectionCount(left, right);
            if(r != FILTER_DRAW)
                return r;
        }

        return FILTER_DRAW;
    }

    private int filterOnInjectionCount(IParseNode left, IParseNode right) {
        throw new NotImplementedException();
    }

    private int filterOnPreferCount(IParseNode left, IParseNode right) {
        throw new NotImplementedException();
    }

    private int filterOnIndirectPrefers(IParseNode left, IParseNode right) {

        if(left instanceof Amb || right instanceof Amb)
            return FILTER_DRAW;
        
        if(!left.equals(right))
            return filterOnDirectPrefers(left, right);
        
        ParseNode l = (ParseNode)left;
        ParseNode r = (ParseNode)right;
        
        int leftLabel = l.label;
        int rightLabel = r.label;
        
        List<IParseNode> leftArgs = l.getKids();
        List<IParseNode> rightArgs = l.getKids();
        
        int diffs = computeDistinctArguments(leftArgs, rightArgs);
        
        if(diffs == 1) {
            for(int i = 0; i < leftArgs.size(); i++) {
                IParseNode leftArg = leftArgs.get(i);
                IParseNode rightArg = rightArgs.get(i);
                
                if(!leftArg.equals(rightArg)) {
                    return filterOnIndirectPrefers(leftArg, rightArg);
                }
            }
                
        }
        return 0;
    }

    private int filterOnDirectPrefers(IParseNode left, IParseNode right) {
        
        if(isLeftMoreEager(left, right))
            return FILTER_LEFT_WINS;
        if(isLeftMoreEager(right, left))
            return FILTER_RIGHT_WINS;
        
        return FILTER_DRAW;
    }

    private boolean isLeftMoreEager(IParseNode left, IParseNode right) {
        
        if(isMoreEager(left, right))
            return true;
        
        IParseNode newLeft = jumpOverInjectionsModuloEagerness(left);
        IParseNode newRight = jumpOverInjectionsModuloEagerness(right);
        
        if(newLeft instanceof ParseNode && newRight instanceof ParseNode)
            return isMoreEager(left, right);
        
        return false;
    }

    private IParseNode jumpOverInjectionsModuloEagerness(IParseNode t) {

        int prodType = getProductionType(t);

        if(t instanceof ParseNode 
                && prodType != ProductionAttributes.PREFER 
                && prodType != ProductionAttributes.AVOID) {
                
            Label prod = getLabel(t);
            ParseNode n = (ParseNode)t;

            while(prod.isInjection()) {
                IParseNode x = (ParseNode) n.getKids().get(0);

                int prodTypeX = getProductionType(x);

                if(x instanceof ParseNode 
                        && prodTypeX != ProductionAttributes.PREFER
                        && prodTypeX != ProductionAttributes.AVOID) {
                        prod = getLabel(x);
                } else {
                    return n;
                }
            }
        }
        return t;
    }

    private Label getLabel(IParseNode t) {
        if(t instanceof ParseNode) {
            ParseNode n = (ParseNode)t;
            return parseTable.getLabel(n.label);
        } else if(t instanceof ParseProductionNode) {
            ParseProductionNode n = (ParseProductionNode)t;
            return parseTable.getLabel(n.prod);
        }
        return null;
    }

    private int getProductionType(IParseNode t) {
        return getLabel(t).getAttributes().type;
    }

    private boolean isMoreEager(IParseNode left, IParseNode right) {
        int leftLabel = ((ParseNode)left).getLabel();
        int rightLabel = ((ParseNode)right).getLabel();
        
        Label leftProd = parseTable.getLabel(leftLabel);
        Label rightProd = parseTable.getLabel(rightLabel);
        
        if(leftProd.isMoreEager(rightProd))
            return true;
        
        return false;
    }

    private int computeDistinctArguments(List<IParseNode> leftArgs, List<IParseNode> rightArgs) {
        int r = 0;
        for(int i=0;i<leftArgs.size();i++) {
            if(!leftArgs.equals(rightArgs))
                r++;
        }
        return r;
    }

    private List<IParseNode> getEmptyList() {
        return new ArrayList<IParseNode>(0);
    }

    private List<IParseNode> getCluster(IParseNode t, int pos) {
        int idx = ambiguityManager.getClusterIndex(t, pos);
        Amb amb = idx == -1 ? null : ambiguityManager.getClusterOnIndex(idx);
        return amb == null ? null : amb.getAlternatives();
    }

    private boolean isCyclicTerm(IParseNode t) {

        ambiguityManager.dumpIndexTable();

        List<IParseNode> cycles = computeCyclicTerm(t);

        return cycles != null && cycles.size() > 0;
    }

    private List<IParseNode> computeCyclicTerm(IParseNode t) {
        PositionMap visited = new PositionMap(ambiguityManager.getMaxNumberOfAmbiguities());

        ambiguityManager.resetAmbiguityCount();
        initializeMarks();
        parseTreePosition = 0;

        return computeCyclicTerm(t, false, visited);
    }

    private List<IParseNode> computeCyclicTerm(IParseNode t, boolean inAmbiguityCluster,
            PositionMap visited) {

        if (SGLR.isDebugging()) {
            Tools.debug("computeCyclicTerm()");
            Tools.debug(" - t : " + t);
        }

        if (t instanceof ParseProductionNode) {
            parseTreePosition++;
            if (SGLR.isDebugging()) {
                Tools.debug(" bumping");
            }
            return null;
        } else if (t instanceof ParseNode) {
            Amb ambiguities = null;
            List<IParseNode> cycle = null;
            int clusterIndex;
            ParseNode n = (ParseNode) t;

            if (inAmbiguityCluster) {
                cycle = computeCyclicTerm(n.getKids(), false, visited);
            } else {
                if (ambiguityManager.isInputAmbiguousAt(parseTreePosition)) {
                    ambiguityManager.increaseAmbiguityCount();
                    clusterIndex = ambiguityManager.getClusterIndex(t, parseTreePosition);
                    if (SGLR.isDebugging()) {
                        Tools.debug(" - clusterIndex : ", clusterIndex);
                    }
                    if (markMap.isMarked(clusterIndex)) {
                        return new ArrayList<IParseNode>();
                    }
                    ambiguities = ambiguityManager.getClusterOnIndex(clusterIndex);
                } else {
                    clusterIndex = -1;
                }

                if (ambiguities == null) {
                    cycle = computeCyclicTerm(((ParseNode) t).getKids(), false, visited);
                } else {
                    int length = visited.getValue(clusterIndex);
                    int savePos = parseTreePosition;

                    if (length == -1) {
                        markMap.mark(clusterIndex);
                        cycle = computeCyclicTermInAmbiguityCluster(ambiguities, visited);
                        visited.put(clusterIndex, parseTreePosition - savePos);
                        markMap.unmark(clusterIndex);
                    } else {
                        parseTreePosition += length;
                    }
                }
            }
            return cycle;
        } else {
            throw new FatalException();
        }
    }

    private List<IParseNode> computeCyclicTermInAmbiguityCluster(Amb ambiguities,
            PositionMap visited) {

        int savedPos = parseTreePosition;

        for (IParseNode n : ambiguities.getAlternatives()) {
            parseTreePosition = savedPos;
            List<IParseNode> cycle = computeCyclicTerm(n, true, visited);
            if (cycle != null)
                return cycle;
        }
        return null;
    }

    private List<IParseNode> computeCyclicTerm(List<IParseNode> kids, boolean b, PositionMap visited) {

        for (IParseNode kid : kids) {
            List<IParseNode> cycle = computeCyclicTerm(kid, false, visited);
            if (cycle != null)
                return cycle;
        }
        return null;
    }

    private void initializeMarks() {
        markMap = new AmbiguityMap(1024);
    }

    private ATerm parseError(String msg) {
        System.err.println("Parse error: " + msg);
        return null;
    }

    private IParseNode selectOnTopSort() {
        throw new NotImplementedException();
    }

}
