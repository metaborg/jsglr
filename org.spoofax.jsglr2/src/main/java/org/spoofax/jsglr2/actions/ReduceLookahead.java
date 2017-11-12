package org.spoofax.jsglr2.actions;

import org.spoofax.jsglr2.characters.ICharacters;
import org.spoofax.jsglr2.parser.Parse;
import org.spoofax.jsglr2.parsetable.IProduction;
import org.spoofax.jsglr2.parsetable.ProductionType;

public class ReduceLookahead extends Reduce implements IReduceLookahead {

    private final ICharacters[] followRestriction;

    public ReduceLookahead(ICharacters characters, IProduction production, ProductionType productionType, int arity,
        ICharacters[] followRestriction) {
        super(characters, production, productionType, arity);

        this.followRestriction = followRestriction;
    }

    @Override public boolean allowsLookahead(String lookahead) {
        if(lookahead.length() != followRestriction.length)
            return true;

        for(int i = 0; i < followRestriction.length; i++) {
            if(!followRestriction[i].containsCharacter(lookahead.charAt(i)))
                return true;
        }

        return false;
    }

    @Override public boolean allowsLookahead(Parse parse) {
        String lookahead = parse.getLookahead(followRestriction.length);

        return allowsLookahead(lookahead);
    }

    @Override public String toString() {
        return characters().toString() + "->REDUCE_LOOKAHEAD(" + production.productionNumber() + "," + followRestriction
            + ")";
    }

}
