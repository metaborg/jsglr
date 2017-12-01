package org.spoofax.jsglr2.actions;

import org.spoofax.jsglr2.characterclasses.ICharacterClass;
import org.spoofax.jsglr2.parsetable.IProduction;
import org.spoofax.jsglr2.parsetable.ProductionType;
import org.spoofax.jsglr2.util.Cache;

public class ActionsFactory implements IActionsFactory {

    private boolean cache;

    private Cache<IShift> shiftCache;
    private Cache<IReduce> reduceCache;
    private Cache<IReduceLookahead> reduceLookaheadCache;

    public ActionsFactory(boolean cache) {
        this.cache = cache;

        if(cache) {
            this.shiftCache = new Cache<>();
            this.reduceCache = new Cache<>();
            this.reduceLookaheadCache = new Cache<>();
        }
    }

    public IShift getShift(int shiftStateId) {
        IShift shift = new Shift(shiftStateId);

        return maybeCached(shiftCache, shift);
    }

    @Override public IReduce getReduce(IProduction production, ProductionType productionType, int arity) {
        IReduce reduce = new Reduce(production, productionType, arity);

        return maybeCached(reduceCache, reduce);
    }

    @Override public IReduceLookahead getReduceLookahead(IProduction production, ProductionType productionType,
        int arity, ICharacterClass[] followRestriction) {
        IReduceLookahead reduceLookahead = new ReduceLookahead(production, productionType, arity, followRestriction);

        return maybeCached(reduceLookaheadCache, reduceLookahead);
    }

    @Override public IAccept getAccept() {
        return Accept.SINGLETON;
    }

    private <T> T maybeCached(Cache<T> cacheT, T action) {
        if(cache)
            return cacheT.cached(action);
        else
            return action;
    }

}
