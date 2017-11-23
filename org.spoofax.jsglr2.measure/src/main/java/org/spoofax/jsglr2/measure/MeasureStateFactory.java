package org.spoofax.jsglr2.measure;

import org.spoofax.jsglr2.actions.IAction;
import org.spoofax.jsglr2.actions.IGoto;
import org.spoofax.jsglr2.parsetable.IState;
import org.spoofax.jsglr2.parsetable.StateFactory;

public class MeasureStateFactory extends StateFactory {

    @Override public IState from(int stateNumber, IGoto[] gotos, IAction[] actions) {
        // TODO: do measurements

        return super.from(stateNumber, gotos, actions);
    }

}
