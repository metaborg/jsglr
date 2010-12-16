/*
 * Created on 11.apr.2006
 *
 * Copyright (c) 2005, Karl Trygve Kalleberg <karltk near strategoxt.org>
 *
 * Licensed under the GNU General Public License, v2
 */
package org.spoofax.jsglr.client;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.spoofax.jsglr.shared.SGLRException;
import org.spoofax.jsglr.shared.Tools;
import org.spoofax.jsglr.shared.terms.AFun;
import org.spoofax.jsglr.shared.terms.ATerm;
import org.spoofax.jsglr.shared.terms.ParseError;

/**
 * @author Karl Trygve Kalleberg <karltk near strategoxt.org>
 * @author Lennart Kats <lennart add lclnet.nl>
 */
public class Disambiguator {

    private static final int FILTER_DRAW = 1;

    private static final int FILTER_LEFT_WINS = 2;

    private static final int FILTER_RIGHT_WINS = 3;

    private boolean filterAny;

    private boolean filterCycles;

    private boolean filterDirectPreference;

    private boolean filterPreferenceCount;

    private boolean filterInjectionCount;

    private boolean filterTopSort;

    private boolean filterReject;

    private boolean filterAssociativity;

    private boolean filterPriorities;

    private boolean filterStrict;

    // Current parser state

    private AmbiguityManager ambiguityManager;

    private SGLR parser;

    private ParseTable parseTable;

    // private Map<AmbKey, IParseNode> resolvedTable = new HashMap<AmbKey, IParseNode>();

    /**
     * Sets whether any filter should be applied at all (excluding the top sort filter).
     */
    public final void setFilterAny(boolean filterAny) {
        this.filterAny = filterAny;
    }

    public final void setFilterDirectPreference(boolean filterDirectPreference) {
        this.filterDirectPreference = filterDirectPreference;
    }

    public boolean getFilterDirectPreference() {
        return filterDirectPreference;
    }

    /**
     * For preference count filtering, see {@link #setFilterPreferenceCount(boolean)}.
     */
    @Deprecated
    public final void setFilterIndirectPreference(boolean filterIndirectPreference) {
        throw new UnsupportedOperationException();
    }

    /**
     * For preference count filtering, see {@link #getFilterPreferenceCount()}.
     */
    @Deprecated
    public boolean getFilterIndirectPreference() {
        throw new UnsupportedOperationException();
    }

    public final void setFilterInjectionCount(boolean filterInjectionCount) {
        this.filterInjectionCount = filterInjectionCount;
    }

    public boolean getFilterInjectionCount() {
        return filterInjectionCount;
    }

    public final void setFilterPreferenceCount(boolean filterPreferenceCount) {
        this.filterPreferenceCount = filterPreferenceCount;
    }

    public boolean getFilterPreferenceCount() {
        return filterPreferenceCount;
    }

    public final void setFilterTopSort(boolean filterTopSort) {
        this.filterTopSort = filterTopSort;
    }

    public boolean getFilterTopSort() {
        return filterTopSort;
    }

    public void setFilterCycles(boolean filterCycles) {
        this.filterCycles = filterCycles;
    }

    public boolean isFilterCycles() {
        return filterCycles;
    }

    public void setFilterAssociativity(boolean filterAssociativity) {
        this.filterAssociativity = filterAssociativity;
    }

    public boolean getFilterAssociativity() {
        return filterAssociativity;
    }

    public void setFilterPriorities(boolean filterPriorities) {
        this.filterPriorities = filterPriorities;
    }

    public boolean getFilterPriorities() {
        return filterPriorities;
    }

    /**
     * Sets whether to enable strict filtering, triggering a
     * FilterException when the priorities filter encounters
     * an unfiltered ambiguity.
     */
    public void setFilterStrict(boolean filterStrict) {
        this.filterStrict = filterStrict;
    }

    public boolean getFilterStrict() {
        return filterStrict;
    }

    public final void setHeuristicFilters(boolean heuristicFilters) {
        setFilterPreferenceCount(heuristicFilters);
        setFilterInjectionCount(heuristicFilters);
    }

    public void setFilterReject(boolean filterReject) {
        this.filterReject = filterReject;
    }

    public boolean getFilterReject() {
        return filterReject;
    }

    public final void setDefaultFilters() {
        filterAny = true;
        filterCycles = false; // TODO: filterCycles; enable by default
        filterDirectPreference = true;
        filterPreferenceCount = false;
        filterInjectionCount = false;
        filterTopSort = true;
        filterReject = true;
        filterAssociativity = true;
        filterPriorities = true;
        filterStrict = false; // TODO: disable filterStrict hack
    }

    public Disambiguator() {
        setDefaultFilters();
    }

    public ATerm applyFilters(SGLR parser, AbstractParseNode root, String sort, int inputLength) throws SGLRException, FilterException {
        try {
            if(SGLR.isDebugging()) {
                Tools.debug("applyFilters()");
            }

            initializeFromParser(parser);

            AbstractParseNode t = root;

            t = applyTopSortFilter(sort, t);

            if (filterAny) {
                t = applyCycleDetectFilter(t);

                // SG_FilterTree
                ambiguityManager.resetClustersVisitedCount();
                t = filterTree(t, false);
            }

            // TODO: Move convertToATerm to SGLR.java and support IStrategoTerms
            ATerm result = convertToATerm(t);
            assert Term.asAppl(result).getAFun().getName().equals("parsetree");
            return result;
        } catch (RuntimeException e) {
            throw new FilterException(parser, "Runtime exception when applying filters", e);
        } finally {
            initializeFromParser(null);
        }
    }

    private void initializeFromParser(SGLR parser) {
        if (parser == null) {
            this.parser = null;
            parseTable = null;
            ambiguityManager = null;
        } else {
            this.parser = parser;
            parseTable = parser.getParseTable();
            ambiguityManager = parser.getAmbiguityManager();
        }
    }

    private void logStatus() {
        Tools.logger("Number of rejects: ", parser.getRejectCount());
        Tools.logger("Number of reductions: ", parser.getReductionCount());
        Tools.logger("Number of ambiguities: ", ambiguityManager.getMaxNumberOfAmbiguities());
        Tools.logger("Number of calls to Amb: ", ambiguityManager.getAmbiguityCallsCount());
        Tools.logger("Count Eagerness Comparisons: ", ambiguityManager.getEagernessComparisonCount(), " / ", ambiguityManager.getEagernessSucceededCount());
        Tools.logger("Number of Injection Counts: ", ambiguityManager.getInjectionCount());
    }

    private ATerm yieldTree(AbstractParseNode t) {
        return t.toParseTree(parser.getParseTable());
    }

    private ATerm convertToATerm(AbstractParseNode t) {

        if (SGLR.isDebugging()) {
            Tools.debug("convertToATerm: ", t);
        }

        ambiguityManager.resetAmbiguityCount();
        ATerm r = yieldTree(t);

        logStatus();

        int ambCount = ambiguityManager.getAmbiguitiesCount();
        if (SGLR.isDebugging()) {
            Tools.debug("yield: ", r);
        }
        final AFun parseTreeAfun = parseTable.getFactory().makeAFun("parsetree", 2, false);
        return parseTable.getFactory().makeAppl(parseTreeAfun, r,
                                                parseTable.getFactory().makeInt(ambCount));
    }

    private AbstractParseNode applyCycleDetectFilter(AbstractParseNode t) throws FilterException {

        if (SGLR.isDebugging()) {
            Tools.debug("applyCycleDetectFilter() - ", t);
        }

        if (filterCycles) {
            if (ambiguityManager.getMaxNumberOfAmbiguities() > 0) {
                if (isCyclicTerm(t)) {
                    throw new FilterException(parser, "Term is cyclic");
                }
            }
        }

        return t;
    }

    private ATerm getProduction(AbstractParseNode t) {
        if (t instanceof ParseNode) {
            return parseTable.getProduction(((ParseNode) t).getLabel());
        } else {
            return parseTable.getProduction(((ParseProductionNode) t).getProduction());
        }
    }

    private AbstractParseNode applyTopSortFilter(String sort, AbstractParseNode t) throws SGLRException {

        if (SGLR.isDebugging()) {
            Tools.debug("applyTopSortFilter() - ", t);
        }

        if (sort != null && filterTopSort) {
            t = selectOnTopSort(t, sort);
            if (t == null)
                throw new StartSymbolException(parser, "Desired start symbol not found: " + sort);
        }

        return t;
    }

    private boolean matchProdOnTopSort(ATerm prod, String sort) throws FilterException {
        try {
            sort = sort.replaceAll("\"", "");
            return prod.match("prod([cf(opt(layout)),cf(sort(\"" + sort + "\")),cf(opt(layout))], sort(\"<START>\"),no-attrs)") != null
                || prod.match("prod([cf(sort(\"" + sort + "\"))], sort(\"<START>\"),no-attrs)") != null
                || prod.match("prod([lex(sort(\"" + sort + "\"))], sort(\"<START>\"),no-attrs)") != null
                || prod.match("prod([sort(\"" + sort + "\")], sort(\"<START>\"),no-attrs)") != null;
        } catch (ParseError e) {
            throw new FilterException(parser, "Could not select desired top sort: " + sort, e);
        }
    }

    private AbstractParseNode selectOnTopSort(AbstractParseNode t, String sort) throws FilterException {
        List<AbstractParseNode> results = new ArrayList<AbstractParseNode>();

        if (t instanceof Amb) {
            addTopSortAlternatives(t, sort, results);

            switch (results.size()) {
                case 0: return null;
                case 1: return results.get(0);
                default: return new Amb(results);
            }
        } else {
            ATerm prod = getProduction(t);
            return matchProdOnTopSort(prod, sort) ? t : null;
        }
      }

    private void addTopSortAlternatives(AbstractParseNode t, String sort, List<AbstractParseNode> results) throws FilterException {
        List<AbstractParseNode> alternatives = ((Amb) t).getAlternatives();
        for (int i = 0, max = alternatives.size(); i < max; i++) {
            AbstractParseNode amb = alternatives.get(i);
            if (amb instanceof Amb) {
                addTopSortAlternatives(amb, sort, results);
            } else {
                ATerm prod = getProduction(amb);
                if (matchProdOnTopSort(prod, sort))
                    results.add(amb);
            }
        }
    }

    private AbstractParseNode filterTree(AbstractParseNode t, boolean inAmbiguityCluster) throws FilterException {
        // SG_FilterTreeRecursive
        if (SGLR.isDebugging()) {
            Tools.debug("filterTree(node)    - ", t);
        }

        if (t instanceof Amb) {
            if (!inAmbiguityCluster) {
                // (some cycle stuff should be done here)
                List<AbstractParseNode> ambs = ((Amb)t).getAlternatives();
                t = filterAmbiguities(ambs);
            } else {
            	// FIXME: hasRejectProd(Amb) can never succeed?
                if (filterReject && parseTable.hasRejects() && hasRejectProd(t)) {
                    return null;
                }
                List<AbstractParseNode> ambs = ((Amb) t).getAlternatives();
                return filterAmbiguities(ambs);

            }
        } else if(t instanceof ParseNode) {
            ParseNode node = (ParseNode) t;
            List<AbstractParseNode> args = node.getKids();
            List<AbstractParseNode> newArgs = filterTree(args, false);

            if (filterReject && parseTable.hasRejects()) {
                if (hasRejectProd(t) && !parser.useIntegratedRecovery)
                    throw new FilterException(parser, "Unexpected reject annotation in " + yieldTree(t));
            }

            t = new ParseNode(node.label, newArgs);
        } else if(t instanceof ParseProductionNode) {
            // leaf node -- do thing (cannot be any ambiguities here)
            return t;
        } else {
            throw new FatalException();
        }

        if (filterAssociativity) {
            return applyAssociativityPriorityFilter(t);
        } else {
            return t;
        }
    }

    private List<AbstractParseNode> filterTree(List<AbstractParseNode> args, boolean inAmbiguityCluster) throws FilterException {

        if(SGLR.isDebugging()) {
            Tools.debug("filterTree(<nodes>) - ", args);
        }

        List<AbstractParseNode> newArgs = new ArrayList<AbstractParseNode>();
        // boolean changed = false;

        for (int i = 0, max = args.size(); i < max; i++) {
            AbstractParseNode n = args.get(i);
            AbstractParseNode filtered = filterTree(n, false);

            // changed = !filtered.equals(n) || changed;
            newArgs.add(filtered);
        }

        // FIXME Shouldn't we do some filtering here?
        // if (!changed) {
        //     Tools.debug("Dropping: ", args);
        //     newArgs = getEmptyList();
        // }

        if (filterAny) {
            List<AbstractParseNode> filtered = new ArrayList<AbstractParseNode>();
            for (int i = 0, max = newArgs.size(); i < max; i++) {
                AbstractParseNode n = newArgs.get(i);
                filtered.add(applyAssociativityPriorityFilter(n));
            }
            return filtered;
        } else {
            return newArgs;
        }
    }

    private AbstractParseNode applyAssociativityPriorityFilter(AbstractParseNode t) throws FilterException {
        // SG_Associativity_Priority_Filter(pt, t)
        // - ok

        if(SGLR.isDebugging()) {
            Tools.debug("applyAssociativityPriorityFilter() - ", t);
        }

        AbstractParseNode r = t;

        if (t instanceof ParseNode) {
            Label prodLabel = getProductionLabel(t);
            ParseNode n = (ParseNode) t;

            if (filterAssociativity) {
                if (prodLabel.isLeftAssociative()) {
                    r = applyLeftAssociativeFilter(n, prodLabel);
                } else if (prodLabel.isRightAssociative()) {
                    r = applyRightAssociativeFilter(n, prodLabel);
                }

            }

            if (filterPriorities && parseTable.hasPriorities()) {
                if(Tools.debugging) {
                    Tools.debug(" - about to look up : ",  prodLabel.labelNumber);
                }

                if (!lookupGtrPriority(prodLabel).isEmpty()) {
                    if(Tools.debugging) {
                        Tools.debug(" - found");
                    }
                    if (r instanceof Amb) // FIXME is this correct?
                        return r;
                    return applyPriorityFilter((ParseNode) r, prodLabel);
                }
                if(Tools.debugging) {
                    Tools.debug(" - not found");
                }
            }
        }

        return r;
    }

    private AbstractParseNode applyRightAssociativeFilter(ParseNode t, Label prodLabel) throws FilterException {
        // SG_Right_Associativity_Filter(t, prodl)
        // - almost ok

        if(SGLR.isDebugging()) {
            Tools.debug("applyRightAssociativeFilter() - ", t);
        }

        List<AbstractParseNode> newAmbiguities = new ArrayList<AbstractParseNode>();
        List<AbstractParseNode> kids = t.getKids();
        AbstractParseNode firstKid = kids.get(0);

        if(firstKid instanceof Amb) {

            List<AbstractParseNode> ambs = ((Amb)firstKid).getAlternatives();
            List<AbstractParseNode> restKids = kids.subList(1, t.kids.length - 1);

            for (int i = 0, max = ambs.size(); i < max; i++) {
                AbstractParseNode amb = ambs.get(i);
                if(((ParseNode)amb).getLabel() != prodLabel.labelNumber) {
                    newAmbiguities.add(amb);
                }
            }

            // FIXME is this correct?
            if(!newAmbiguities.isEmpty()) {
                if(newAmbiguities.size() > 1)
                    firstKid = new Amb(newAmbiguities);
                else
                    firstKid = newAmbiguities.get(0);
                restKids.add(firstKid);
            } else {
                throw new FilterException(parser);
            }

            // FIXME is this correct?
            return new ParseNode(t.label, restKids);

        } else if(firstKid instanceof ParseNode) {
            if(((ParseNode)firstKid).getLabel() == prodLabel.labelNumber)
                throw new FilterException(parser);
        }
        return t;
    }

    private AbstractParseNode applyPriorityFilter(ParseNode t, Label prodLabel) throws FilterException {
        // SG_Priority_Filter

        if(SGLR.isDebugging()) {
            Tools.debug("applyPriorityFilter() - ", t);
        }

        List<AbstractParseNode> newAmbiguities = new ArrayList<AbstractParseNode>();
        List<AbstractParseNode> kids = t.getKids();
        List<AbstractParseNode> newKids = new ArrayList<AbstractParseNode>();

        int l0 = prodLabel.labelNumber;
        int kidnumber = 0;

        for (int i = 0, max = kids.size(); i < max; i++) {
            final AbstractParseNode kid = kids.get(i);
            AbstractParseNode newKid = kid;
            final AbstractParseNode injection = jumpOverInjections(kid);

            if (injection instanceof Amb) {
                List<AbstractParseNode> ambs = ((Amb) injection).getAlternatives();

                newAmbiguities.clear();
                for (int j = 0, jmax = ambs.size(); j < jmax; j++) {
                    AbstractParseNode amb = ambs.get(j);
                    AbstractParseNode injAmb = jumpOverInjections(amb);

                    if (injAmb instanceof ParseNode) {
                        Label label = getProductionLabel(t);
                        if(hasGreaterPriority(l0, label.labelNumber, kidnumber)) {
                            newAmbiguities.add(amb);
                        }
                    }
                }

                if(!newAmbiguities.isEmpty()) {
                    AbstractParseNode n = null;
                    if(newAmbiguities.size() > 1) {
                        n = new Amb(newAmbiguities);
                    } else {
                        n = newAmbiguities.get(0);
                    }
                    newKid = replaceUnderInjections(kid, injection, n);
                } else {
                    // fishy: another filter might be borked
                    if (filterStrict) {
                        throw new FilterException(parser);
                    } else {
                        // TODO: log or whatever?
                        return t;
                    }
                }
            } else if (injection instanceof ParseNode) {
                int l1 = ((ParseNode) injection).label;
                if (hasGreaterPriority(l0, l1, kidnumber)) {
                    throw new FilterException(parser);
                }
            }

            newKids.add(newKid);
            kidnumber++;
        }

        return new ParseNode(t.label, newKids);
    }

    private AbstractParseNode replaceUnderInjections(AbstractParseNode alt, AbstractParseNode injection, AbstractParseNode n) {
        // SG_Replace_Under_Injections
        // - not ok

        throw new NotImplementedException();
        /*
        if (ATisEqual(t, injT)) {
           return newTree;
        } else {
          ATermList sons = (ATermList)ATgetArgument((ATerm) t, 1);
          tree newSon = SG_Replace_Under_Injections((tree)ATgetFirst(sons),
                                                    injT, newTree);
          return ATsetArgument((ATermAppl)t, (ATerm)ATmakeList1((ATerm)newSon), 1);
        }
        */
    }

    private AbstractParseNode jumpOverInjections(AbstractParseNode t) {

        if(SGLR.isDebugging()) {
            Tools.debug("jumpOverInjections() - ", t);
        }

        if (t instanceof ParseNode) {
            int prod = ((ParseNode) t).label;
            ParseNode n = (ParseNode)t;
            while (isUserDefinedLabel(prod)) {
                List<AbstractParseNode> kids = n.getKids();
                AbstractParseNode x = kids.get(0);
                if(x instanceof ParseNode) {
                    n = (ParseNode)x;
                    prod = n.label;
                } else {
                    return x;
                }
            }
        }

        return t;
    }

    // TODO: shouldn't this be called isInjection?

    private boolean isUserDefinedLabel(int prod) {
        Label l = parseTable.lookupInjection(prod);
        if(l == null)
            return false;
        return l.isInjection();
    }

    private boolean hasGreaterPriority(int l0, int l1, int arg) {
        List<Priority> prios = lookupGtrPriority(parseTable.getLabel(l0));

        for (int i = 0, size = prios.size(); i < size; i++) {
            Priority p = prios.get(i);
        	if (l1 == p.right)
        		if (p.arg == -1 || p.arg == arg) {
        			return true;
        	}
        }
        return false;
    }

    private List<Priority> lookupGtrPriority(Label prodLabel) {
        return parseTable.getPriorities(prodLabel);
    }

    private AbstractParseNode applyLeftAssociativeFilter(ParseNode t, Label prodLabel) throws FilterException {
        // SG_Right_Associativity_Filter()

        if(SGLR.isDebugging()) {
            Tools.debug("applyLeftAssociativeFilter() - ", t);
        }

        List<AbstractParseNode> newAmbiguities = new ArrayList<AbstractParseNode>();
        List<AbstractParseNode> kids = t.getKids();
        AbstractParseNode last = kids.get(kids.size() - 1);

        if (last instanceof Amb) {
            List<AbstractParseNode> rest = new ArrayList<AbstractParseNode>();
            rest.addAll(kids);
            rest.remove(rest.size() - 1);

            List<AbstractParseNode> ambs = ((Amb) last).getAlternatives();

            for (int i = 0, max = ambs.size(); i < max; i++) {
                AbstractParseNode amb = ambs.get(i);
                if (amb instanceof Amb
                        || !parseTable.getLabel(((ParseNode) amb).getLabel()).equals(prodLabel)) {
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
                throw new FilterException(parser);
            }
        } else if (last instanceof ParseNode) {
            Label other = parseTable.getLabel(((ParseNode) last).getLabel());
            if (prodLabel.equals(other)) {
                throw new FilterException(parser);
            }
        }

        return t;
    }

    private Label getProductionLabel(AbstractParseNode t) {
        if (t instanceof ParseNode) {
            return parseTable.getLabel(((ParseNode) t).getLabel());
        } else if (t instanceof ParseProductionNode) {
            return parseTable.getLabel(((ParseProductionNode) t).getProduction());
        }
        return null;
    }

    private boolean hasRejectProd(AbstractParseNode t) {
        return t instanceof ParseReject;
    }

    private AbstractParseNode filterAmbiguities(List<AbstractParseNode> ambs) throws FilterException {
        // SG_FilterAmb

        if(SGLR.isDebugging()) {
            Tools.debug("filterAmbiguities() - [", ambs.size(), "]");
        }

        List<AbstractParseNode> newAmbiguities = new ArrayList<AbstractParseNode>();

        for (int i = 0, max = ambs.size(); i < max; i++) {
            AbstractParseNode amb = ambs.get(i);
            AbstractParseNode newAmb = filterTree(amb, true);
            if (newAmb != null) newAmbiguities.add(newAmb);
        }

        if (newAmbiguities.size() > 1) {
            /* Handle ambiguities inside this ambiguity cluster */
            List<AbstractParseNode> oldAmbiguities = new LinkedList<AbstractParseNode>();
            oldAmbiguities.addAll(newAmbiguities);
            for (int i = 0, max = oldAmbiguities.size(); i < max; i++) {
                AbstractParseNode amb = oldAmbiguities.get(i);
                if (newAmbiguities.remove(amb)) {
                    newAmbiguities = filterAmbiguityList(newAmbiguities, amb);
                }
            }
        }

        if (newAmbiguities.isEmpty())
            throw new FilterException(parser);

        if (newAmbiguities.size() == 1)
            return newAmbiguities.get(0);

        return new Amb(newAmbiguities);
    }

    private List<AbstractParseNode> filterAmbiguityList(List<AbstractParseNode> ambs, AbstractParseNode t) {
        // SG_FilterAmbList

        boolean keepT = true;
        List<AbstractParseNode> r = new ArrayList<AbstractParseNode>();

        if (ambs.isEmpty()) {
            r.add(t);
            return r;
        }

        for (int i = 0, max = ambs.size(); i < max; i++) {
            AbstractParseNode amb = ambs.get(i);
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

    private int filter(AbstractParseNode left, AbstractParseNode right) {
        // SG_Filter(t0, t1)

        if(SGLR.isDebugging()) {
            Tools.debug("filter()");
        }

        if (left.equals(right)) {
            return FILTER_LEFT_WINS;
        }

        /* UNDONE: direct eagerness filter seems to be disabled in reference SGLR
        if (filterDirectPreference && parseTable.hasPrefersOrAvoids()) {
            int r = filterOnDirectPrefers(left, right);
            if (r != FILTER_DRAW)
                return r;
        }
        */

        // like C-SGLR, we use indirect preference filtering if the direct one is enabled
        if (filterDirectPreference && parseTable.hasPrefersOrAvoids()) {
            int r = filterOnIndirectPrefers(left, right);
            if (r != FILTER_DRAW)
                return r;
        }

        if (filterPreferenceCount && parseTable.hasPrefersOrAvoids()) {
            int r = filterOnPreferCount(left, right);
            if (r != FILTER_DRAW)
                return r;
        }

        if (filterInjectionCount) {
            int r = filterOnInjectionCount(left, right);
            if (r != FILTER_DRAW)
                return r;
        }

        return filterPermissiveLiterals(left, right);
    }

    private int filterPermissiveLiterals(AbstractParseNode left, AbstractParseNode right) {
        // Work-around for http://bugs.strategoxt.org/browse/SPI-5 (Permissive grammars introduce ambiguities for literals)

        if (left instanceof ParseNode && right instanceof ParseNode) {
            List<AbstractParseNode> leftKids = ((ParseNode) left).getKids();
            List<AbstractParseNode> rightKids = ((ParseNode) right).getKids();
            if (leftKids.size() > 0 && rightKids.size() == 1) {
                if (leftKids.get(0) instanceof ParseProductionNode && rightKids.get(0).equals(left)) {
                    return FILTER_LEFT_WINS;
                }
            }
        }
        return FILTER_DRAW;
    }

    private int filterOnInjectionCount(AbstractParseNode left, AbstractParseNode right) {

        if(SGLR.isDebugging()) {
            Tools.debug("filterOnInjectionCount()");
        }

        ambiguityManager.increaseInjectionCount();

        int leftInjectionCount = countAllInjections(left);
        int rightInjectionCount = countAllInjections(right);

        if (leftInjectionCount != rightInjectionCount) {
            ambiguityManager.increaseInjectionFilterSucceededCount();
        }

        if (leftInjectionCount > rightInjectionCount) {
            return FILTER_RIGHT_WINS;
        } else if (rightInjectionCount > leftInjectionCount) {
            return FILTER_LEFT_WINS;
        }

        return FILTER_DRAW;
    }

    private int countAllInjections(AbstractParseNode t) {
        // SG_CountAllInjectionsInTree
        // - ok
        if (t instanceof Amb) {
            // Trick from forest.c
            return countAllInjections(((Amb) t).getAlternatives().get(0));
        } else if (t instanceof ParseNode) {
            int c = getProductionLabel(t).isInjection() ? 1 : 0;
            return c + countAllInjections(((ParseNode) t).getKids());
        }
        return 0;
    }

    private int countAllInjections(List<AbstractParseNode> ls) {
        // SG_CountAllInjectionsInTree
        // - ok
        int r = 0;
        for (int i = 0, max = ls.size(); i < max; i++) {
            AbstractParseNode n = ls.get(i);
            r += countAllInjections(n);
        }
        return r;
    }

    private int filterOnPreferCount(AbstractParseNode left, AbstractParseNode right) {

        if(SGLR.isDebugging()) {
            Tools.debug("filterOnPreferCount()");
        }

        ambiguityManager.increaseEagernessFilterCalledCount();

        int r = FILTER_DRAW;
        if (parseTable.hasPrefers() || parseTable.hasAvoids()) {
            int leftPreferCount = countPrefers(left);
            int rightPreferCount = countPrefers(right);
            int leftAvoidCount = countAvoids(left);
            int rightAvoidCount = countAvoids(right);

            if ((leftPreferCount > rightPreferCount && leftAvoidCount <= rightAvoidCount)
                    || (leftPreferCount == rightPreferCount && leftAvoidCount < rightAvoidCount)) {
                Tools.logger("Eagerness priority: ", left, " > ", right);
                r = FILTER_LEFT_WINS;
            }

            if ((rightPreferCount > leftPreferCount && rightAvoidCount <= leftAvoidCount)
                    || (rightPreferCount == leftPreferCount && rightAvoidCount < leftAvoidCount)) {
                if (r != FILTER_DRAW) {
                    Tools.logger("Symmetric eagerness priority: ", left, " == ", right);
                    r = FILTER_DRAW;
                } else {
                    Tools.logger("Eagerness priority: ", right, " > ", left);
                    r = FILTER_RIGHT_WINS;
                }
            }
        }

        if (r != FILTER_DRAW) {
            ambiguityManager.increaseEagernessFilterSucceededCount();
        }

        return r;
    }

    private int countPrefers(AbstractParseNode t) {
        // SG_CountPrefersInTree
        // - ok
        if (t instanceof Amb) {
            return countPrefers(((Amb) t).getAlternatives());
        } else if (t instanceof ParseNode) {
            int type = getProductionType(t);
            if (type == ProductionType.PREFER)
                return 1;
            else if (type == ProductionType.AVOID)
                return 0;
            return countPrefers(((ParseNode) t).getKids());
        }
        return 0;
    }

    private int countPrefers(List<AbstractParseNode> ls) {
        // SG_CountPrefersInTree
        // - ok
        int r = 0;
        for (int i = 0, max = ls.size(); i < max; i++) {
            AbstractParseNode n = ls.get(i);
            r += countPrefers(n);
        }
        return r;
    }

    private int countAvoids(AbstractParseNode t) {
        // SG_CountAvoidsInTree
        // - ok
        if (t instanceof Amb) {
            return countAvoids(((Amb) t).getAlternatives());
        } else if (t instanceof ParseNode) {
            int type = getProductionType(t);
            if (type == ProductionType.PREFER)
                return 0;
            else if (type == ProductionType.AVOID)
                return 1;
            return countAvoids(((ParseNode) t).getKids());
        }
        return 0;
    }

    private int countAvoids(List<AbstractParseNode> ls) {
        // SG_CountAvoidsInTree
        // - ok
        int r = 0;
        for (int i = 0, max = ls.size(); i < max; i++) {
            AbstractParseNode n = ls.get(i);
            r += countAvoids(n);
        }
        return r;
    }

    private int filterOnIndirectPrefers(AbstractParseNode left, AbstractParseNode right) {
        // SG_Indirect_Eagerness_Filter

        if(SGLR.isDebugging()) {
            Tools.debug("filterOnIndirectPrefers()");
        }

        if (left instanceof Amb || right instanceof Amb)
            return FILTER_DRAW;

        if (!getLabel(left).equals(getLabel(right)))
            return filterOnDirectPrefers(left, right);

        ParseNode l = (ParseNode) left;
        ParseNode r = (ParseNode) right;

        List<AbstractParseNode> leftArgs = l.getKids();
        List<AbstractParseNode> rightArgs = r.getKids();

        int diffs = computeDistinctArguments(leftArgs, rightArgs);

        if (diffs == 1) {
            for (int i = 0; i < leftArgs.size(); i++) {
                AbstractParseNode leftArg = leftArgs.get(i);
                AbstractParseNode rightArg = rightArgs.get(i);

                if (!leftArg.equals(rightArg)) {
                    return filterOnIndirectPrefers(leftArg, rightArg);
                }
            }

        }
        return FILTER_DRAW;
    }

    private int filterOnDirectPrefers(AbstractParseNode left, AbstractParseNode right) {
        // SG_Direct_Eagerness_Filter

        if(SGLR.isDebugging()) {
            Tools.debug("filterOnDirectPrefers()");
        }

        // TODO: optimize - move up the jumpOverInjectionsModuloEagerness calls
        if (isLeftMoreEager(left, right))
            return FILTER_LEFT_WINS;
        if (isLeftMoreEager(right, left))
            return FILTER_RIGHT_WINS;

        return FILTER_DRAW;
    }

    private boolean isLeftMoreEager(AbstractParseNode left, AbstractParseNode right) {
        assert !(left instanceof Amb || right instanceof Amb);
        if (isMoreEager(left, right))
            return true;

        AbstractParseNode newLeft = jumpOverInjectionsModuloEagerness(left);
        AbstractParseNode newRight = jumpOverInjectionsModuloEagerness(right);

        if (newLeft instanceof ParseNode && newRight instanceof ParseNode)
            return isMoreEager(newLeft, newRight);

        return false;
    }

    private AbstractParseNode jumpOverInjectionsModuloEagerness(AbstractParseNode t) {

        if(SGLR.isDebugging()) {
            Tools.debug("jumpOverInjectionsModuloEagerness()");
        }

        int prodType = getProductionType(t);

        if (t instanceof ParseNode && prodType != ProductionType.PREFER
                && prodType != ProductionType.AVOID) {

            Label prod = getLabel(t);

            while (prod.isInjection()) {
                t = ((ParseNode) t).kids[0];


                if (t instanceof ParseNode) {
                    int prodTypeX = getProductionType(t);

                    if (prodTypeX != ProductionType.PREFER
                        && prodTypeX != ProductionType.AVOID) {
                        prod = getLabel(t);
                        continue;
                    }
                }
                return t;
            }
        }
        return t;
    }

    private Label getLabel(AbstractParseNode t) {
        if (t instanceof ParseNode) {
            ParseNode n = (ParseNode) t;
            return parseTable.getLabel(n.label);
        } else if (t instanceof ParseProductionNode) {
            ParseProductionNode n = (ParseProductionNode) t;
            return parseTable.getLabel(n.prod);
        }
        return null;
    }

    private int getProductionType(AbstractParseNode t) {
        return getLabel(t).getAttributes().getType();
    }

    private boolean isMoreEager(AbstractParseNode left, AbstractParseNode right) {
        int leftLabel = ((ParseNode) left).getLabel();
        int rightLabel = ((ParseNode) right).getLabel();

        Label leftProd = parseTable.getLabel(leftLabel);
        Label rightProd = parseTable.getLabel(rightLabel);

        if (leftProd.isMoreEager(rightProd))
            return true;

        return false;
    }

    private int computeDistinctArguments(List<AbstractParseNode> leftArgs, List<AbstractParseNode> rightArgs) {
        // countDistinctArguments
        int r = 0;
        for (int i = 0; i < leftArgs.size(); i++) {
            if (!leftArgs.get(i).equals(rightArgs.get(i)))
                r++;
        }
        return r;
    }

    private boolean isCyclicTerm(AbstractParseNode t) {

        ambiguityManager.dumpIndexTable();

        List<AbstractParseNode> cycles = computeCyclicTerm(t);

        return cycles != null && cycles.size() > 0;
    }

    private List<AbstractParseNode> computeCyclicTerm(AbstractParseNode t) {
        // FIXME rewrite to use HashMap and object id
        PositionMap visited = new PositionMap(ambiguityManager.getMaxNumberOfAmbiguities());

        ambiguityManager.resetAmbiguityCount();

        return computeCyclicTerm(t, false, visited);
    }

    private List<AbstractParseNode> computeCyclicTerm(AbstractParseNode t, boolean inAmbiguityCluster,
            PositionMap visited) {

        if (SGLR.isDebugging()) {
            Tools.debug("computeCyclicTerm() - ", t);
        }

        if (t instanceof ParseProductionNode) {
            if (SGLR.isDebugging()) {
                Tools.debug(" bumping");
            }
            return null;
        } else if (t instanceof ParseNode) {
            //Amb ambiguities = null;
            List<AbstractParseNode> cycle = null;
            //int clusterIndex;
            ParseNode n = (ParseNode) t;

            if (inAmbiguityCluster) {
                cycle = computeCyclicTerm(n.getKids(), false, visited);
            } else {
                /*
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
                }*/

                throw new NotImplementedException();
/*
                if (ambiguities == null) {
                    cycle = computeCyclicTerm(((ParseNode) t).getKids(), false, visited);
                } else {
                    int length = visited.getValue(clusterIndex);
                    int savePos = parseTreePosition;

                    if (length == -1) {
                        //markMap.mark(clusterIndex);
                        cycle = computeCyclicTermInAmbiguityCluster(ambiguities, visited);
                        visited.put(clusterIndex, parseTreePosition - savePos);
                        //markMap.unmark(clusterIndex);
                    } else {
                        parseTreePosition += length;
                    }
                }
 */
            }
            return cycle;
        } else {
            throw new FatalException();
        }
    }

    /*
    private List<IParseNode> computeCyclicTermInAmbiguityCluster(Amb ambiguities,
            PositionMap visited) {


        List<IParseNode> ambs = ambiguities.getAlternatives();
        for (int i = 0, max = ambs.size(); i < max; i++) {
            IParseNode amb = ambs.get(i);
            List<IParseNode> cycle = computeCyclicTerm(amb, true, visited);
            if (cycle != null)
                return cycle;
        }
        return null;
    }
     */

    private List<AbstractParseNode> computeCyclicTerm(List<AbstractParseNode> kids, boolean b, PositionMap visited) {


        for (int i = 0, max = kids.size(); i < max; i++) {
            AbstractParseNode kid = kids.get(i);
            List<AbstractParseNode> cycle = computeCyclicTerm(kid, false, visited);
            if (cycle != null)
                return cycle;
        }
        return null;
    }

}
