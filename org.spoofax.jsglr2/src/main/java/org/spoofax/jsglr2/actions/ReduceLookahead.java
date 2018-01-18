package org.spoofax.jsglr2.actions;

import java.util.Arrays;

import org.metaborg.parsetable.IParseInput;
import org.metaborg.parsetable.IProduction;
import org.metaborg.parsetable.ProductionType;
import org.metaborg.parsetable.actions.IReduceLookahead;
import org.metaborg.parsetable.actions.Reduce;
import org.metaborg.parsetable.characterclasses.ICharacterClass;

public class ReduceLookahead extends Reduce implements IReduceLookahead {

    private final ICharacterClass[] followRestriction;

    public ReduceLookahead(IProduction production, ProductionType productionType, int arity,
        ICharacterClass[] followRestriction) {
        super(production, productionType, arity);

        this.followRestriction = followRestriction;
    }

    @Override
    public boolean allowsLookahead(IParseInput parseInput) {
        String lookahead = parseInput.getLookahead(followRestriction.length);

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
