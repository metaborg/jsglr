package org.spoofax.jsglr2.measure;

import org.spoofax.jsglr2.actions.IAction;
import org.spoofax.jsglr2.actions.IGoto;
import org.spoofax.jsglr2.parsetable.IState;
import org.spoofax.jsglr2.parsetable.StateFactory;

public class MeasureStateFactory extends StateFactory {

    public int statesCount = 0;

    public int gotosCount = 0;
    public int actionsCount = 0;

    public int gotosMaxPerState = 0;
    public int actionsMaxPerState = 0;

    @Override public IState from(int stateNumber, IGoto[] gotos, IAction[] actions) {
        statesCount++;

        gotosCount += gotos.length;
        actionsCount += actions.length;

        gotosMaxPerState = Math.max(gotosMaxPerState, gotos.length);
        actionsMaxPerState = Math.max(actionsMaxPerState, actions.length);

        return super.from(stateNumber, gotos, actions);
    }

}
