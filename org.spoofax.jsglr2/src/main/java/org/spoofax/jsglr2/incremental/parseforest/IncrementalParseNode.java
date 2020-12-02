package org.spoofax.jsglr2.incremental.parseforest;

import static org.spoofax.jsglr2.parseforest.IParseForest.sumWidth;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.metaborg.parsetable.actions.IGoto;
import org.metaborg.parsetable.productions.IProduction;
import org.metaborg.parsetable.query.ActionsForCharacterSeparated;
import org.metaborg.parsetable.query.ActionsPerCharacterClass;
import org.metaborg.parsetable.query.ProductionToGotoForLoop;
import org.metaborg.parsetable.states.IState;
import org.metaborg.parsetable.states.State;
import org.spoofax.jsglr2.parseforest.IParseNode;
import org.spoofax.jsglr2.util.TreePrettyPrinter;
import org.spoofax.jsglr2.util.iterators.SingleElementWithListIterable;

public class IncrementalParseNode extends IncrementalParseForest
    implements IParseNode<IncrementalParseForest, IncrementalDerivation> {

    public static final State NO_STATE =
        new State(-1, new ActionsForCharacterSeparated(new ActionsPerCharacterClass[0], Collections.emptySet()),
            new ProductionToGotoForLoop(new IGoto[0]));

    protected final IProduction production;
    private IncrementalDerivation firstDerivation;
    private List<IncrementalDerivation> otherDerivations;
    private IState state;

    public IncrementalParseNode(IProduction production, IncrementalDerivation firstDerivation, IState state) {
        super(sumWidth(firstDerivation.parseForests()));
        this.production = production;
        this.firstDerivation = firstDerivation;
        this.state = state;
    }

    public IncrementalParseNode(IncrementalParseForest... parseForests) {
        this(null, new IncrementalDerivation(null, null, parseForests), NO_STATE);
    }

    protected IncrementalParseNode(int width, IProduction production) {
        super(width);
        this.production = production;
        this.firstDerivation = null;
        this.state = null;
    }

    @Override public boolean isReusable() {
        return state != NO_STATE;
    }

    @Override public boolean isReusable(IState stackState) {
        // If state == NO_STATE, its ID is -1, and can in that case never equal the ID of stackState
        return stackState.id() == state.id();
    }

    @Override public boolean isTerminal() {
        return false;
    }

    public IProduction production() {
        return production;
    }

    @Override public String descriptor() {
        return production.descriptor();
    }

    @Override public void addDerivation(IncrementalDerivation derivation) {
        if(otherDerivations == null)
            otherDerivations = new ArrayList<>();

        otherDerivations.add(derivation);

        state = NO_STATE; // Parse nodes with multiple derivations are by definition not reusable
    }

    @Override public boolean hasDerivations() {
        return firstDerivation != null;
    }

    @Override public Iterable<IncrementalDerivation> getDerivations() {
        if(firstDerivation == null)
            return Collections.emptyList();
        if(otherDerivations == null)
            return Collections.singleton(firstDerivation);

        return SingleElementWithListIterable.of(firstDerivation, otherDerivations);
    }

    @Override public IncrementalDerivation getFirstDerivation() {
        return firstDerivation;
    }

    @Override public boolean isAmbiguous() {
        return otherDerivations != null;
    }

    @Override public void disambiguate(IncrementalDerivation derivation) {
        firstDerivation = derivation;
        otherDerivations = null;
    }

    @Override protected void prettyPrint(TreePrettyPrinter printer) {
        if(production == null)
            printer.println("p null {");
        else
            printer.println("p" + production.id() + " : " + production.sort() + " { (s" + state.id() + ")");
        if(isAmbiguous()) {
            printer.indent(1);
            printer.println("amb[");
            printer.indent(1);
        } else {
            printer.indent(2);
        }

        firstDerivation.prettyPrint(printer);

        if(otherDerivations != null) {
            for(IncrementalDerivation derivation : otherDerivations) {
                derivation.prettyPrint(printer);
            }
        }

        if(isAmbiguous()) {
            printer.indent(-1);
            printer.println("]");
            printer.indent(-1);
        } else {
            printer.indent(-2);
        }
        printer.println("}");
    }

    @Override public String getYield() {
        StringBuilder sb = new StringBuilder(width());
        for(IncrementalParseForest parseForest : firstDerivation.parseForests) {
            sb.append(parseForest.getYield());
        }
        return sb.toString();
    }

    @Override public String getYield(int length) {
        StringBuilder sb = new StringBuilder(length);
        int offset = 0;
        for(IncrementalParseForest parseForest : firstDerivation.parseForests) {
            int width = parseForest.width();
            if(offset + width <= length) {
                sb.append(parseForest.getYield());
                offset += width;
            } else {
                sb.append(parseForest.getYield(length - offset));
                break;
            }
        }
        return sb.toString();
    }
}
