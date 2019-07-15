package org.spoofax.jsglr2.integration;

import java.util.Objects;

import org.metaborg.parsetable.query.ActionsForCharacterRepresentation;
import org.metaborg.parsetable.query.ProductionToGotoRepresentation;
import org.spoofax.jsglr2.parsetable.ParseTableReader;
import org.spoofax.jsglr2.states.IStateFactory;
import org.spoofax.jsglr2.states.StateFactory;

// TODO move to SDF and use in StateFactory?
public class ParseTableVariant {
    public final ActionsForCharacterRepresentation actionsForCharacterRepresentation;
    public final ProductionToGotoRepresentation productionToGotoRepresentation;

    public ParseTableVariant(ActionsForCharacterRepresentation actionsForCharacterRepresentation,
        ProductionToGotoRepresentation productionToGotoRepresentation) {
        this.actionsForCharacterRepresentation = actionsForCharacterRepresentation;
        this.productionToGotoRepresentation = productionToGotoRepresentation;
    }

    public String name() {
        return "ActionsForCharacterRepresentation:" + actionsForCharacterRepresentation
            + "/ProductionToGotoRepresentation:" + productionToGotoRepresentation;
    }

    public ParseTableReader parseTableReader() {
        IStateFactory stateFactory =
            new StateFactory(actionsForCharacterRepresentation, productionToGotoRepresentation);

        return new ParseTableReader(stateFactory);
    }

    @Override public int hashCode() {
        return Objects.hash(actionsForCharacterRepresentation, productionToGotoRepresentation);
    }

    @Override public boolean equals(Object o) {
        if(this == o) {
            return true;
        }
        if(o == null || getClass() != o.getClass()) {
            return false;
        }

        ParseTableVariant that = (ParseTableVariant) o;

        return actionsForCharacterRepresentation == that.actionsForCharacterRepresentation
            && productionToGotoRepresentation == that.productionToGotoRepresentation;
    }
}
