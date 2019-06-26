package org.spoofax.jsglr2.integration;

import org.metaborg.sdf2table.parsetable.query.ActionsForCharacterRepresentation;
import org.metaborg.sdf2table.parsetable.query.ProductionToGotoRepresentation;

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
