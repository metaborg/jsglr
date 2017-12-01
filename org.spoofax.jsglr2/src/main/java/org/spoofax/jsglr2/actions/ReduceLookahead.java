package org.spoofax.jsglr2.actions;

import java.util.Arrays;

import org.spoofax.jsglr2.characterclasses.ICharacterClass;
import org.spoofax.jsglr2.parser.Parse;
import org.spoofax.jsglr2.parsetable.IProduction;
import org.spoofax.jsglr2.parsetable.ProductionType;

public class ReduceLookahead extends Reduce implements IReduceLookahead {

    private final ICharacterClass[] followRestriction;

    public ReduceLookahead(IProduction production, ProductionType productionType, int arity,
        ICharacterClass[] followRestriction) {
        super(production, productionType, arity);

        this.followRestriction = followRestriction;
    }

    @Override
    public boolean allowsLookahead(Parse parse) {
        String lookahead = parse.getLookahead(followRestriction.length);

        if(lookahead.length() != followRestriction.length)
            return true;

        for(int i = 0; i < followRestriction.length; i++) {
            if(!followRestriction[i].contains(lookahead.charAt(i)))
                return true;
        }

        return false;
    }

    @Override
    public String toString() {
        return "REDUCE_LOOKAHEAD(" + production.id() + "," + Arrays.toString(followRestriction) + ")";
    }

    @Override
    public int hashCode() {
        return super.hashCode() ^ followRestriction.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) {
            return true;
        }
        if(o == null || getClass() != o.getClass()) {
            return false;
        }

        ReduceLookahead that = (ReduceLookahead) o;

        return production.equals(that.production) && productionType.equals(that.productionType) && arity == that.arity
            && Arrays.equals(followRestriction, that.followRestriction);
    }

}
