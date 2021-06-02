package org.spoofax.jsglr2.incremental.parseforest;

import static org.spoofax.jsglr2.parseforest.IParseForest.sumWidth;

import java.util.Collections;

import org.metaborg.parsetable.productions.IProduction;
import org.metaborg.parsetable.states.IState;
import org.spoofax.jsglr2.util.TreePrettyPrinter;

public class IncrementalSkippedNode extends IncrementalParseNode {

    public IncrementalSkippedNode(IProduction production, IncrementalParseForest[] parseForests, IState state) {
        super(sumWidth(parseForests), production, state);
    }

    public IncrementalSkippedNode(IProduction production, IncrementalParseForest[] parseForests) {
        super(sumWidth(parseForests), production, null);
    }

    @Override protected void prettyPrint(TreePrettyPrinter printer) {
        printer.println("s" + production.id() + " : " + production + " (s" + state.id() + ") { ... }");
    }

    @Override public String getYield() {
        throw new UnsupportedOperationException("Cannot get yield of skipped parse node");
    }

    @Override public String getYield(int length) {
        throw new UnsupportedOperationException("Cannot get yield of skipped parse node");
    }

    @Override public void addDerivation(IncrementalDerivation derivation) {
        throw new UnsupportedOperationException("Cannot add derivation to skipped parse node");
    }

    @Override public Iterable<IncrementalDerivation> getDerivations() {
        return Collections.emptyList();
    }

    @Override public IncrementalDerivation getFirstDerivation() {
        throw new UnsupportedOperationException("Cannot get derivation of skipped parse node");
    }

    @Override public boolean isAmbiguous() {
        return false;
    }

}
