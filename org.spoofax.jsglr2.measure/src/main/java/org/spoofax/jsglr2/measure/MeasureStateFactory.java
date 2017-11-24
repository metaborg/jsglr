package org.spoofax.jsglr2.measure;

import org.spoofax.jsglr2.actions.ActionsPerCharacterClass;
import org.spoofax.jsglr2.actions.IGoto;
import org.spoofax.jsglr2.states.IState;
import org.spoofax.jsglr2.states.StateFactory;

public class MeasureStateFactory extends StateFactory {

    public int statesCount = 0;

    public int gotosCount = 0;
    public int actionCharacterClasssCount = 0;
    public int actionsCount = 0;

    public int gotosPerStateMax = 0;
    public int actionCharacterClasssPerStateMax = 0;
    public int actionsPerStateMax = 0;
    public int actionsPerCharacterClassMax = 0;

    @Override public IState from(int stateNumber, IGoto[] gotos,
        ActionsPerCharacterClass[] actionsPerCharacterClasses) {
        statesCount++;

        gotosCount += gotos.length;
        actionCharacterClasssCount += actionsPerCharacterClasses.length;

        int actionsCount = 0;

        for(ActionsPerCharacterClass actionsPerCharacterClass : actionsPerCharacterClasses) {
            actionsCount += actionsPerCharacterClass.actions.size();

            actionsPerCharacterClassMax =
                Math.max(actionsPerCharacterClassMax, actionsPerCharacterClass.actions.size());
        }

        this.actionsCount += actionsCount;

        gotosPerStateMax = Math.max(gotosPerStateMax, gotos.length);
        actionCharacterClasssPerStateMax =
            Math.max(actionCharacterClasssPerStateMax, actionsPerCharacterClasses.length);
        actionsPerStateMax = Math.max(actionsPerStateMax, actionsCount);

        return super.from(stateNumber, gotos, actionsPerCharacterClasses);
    }

}
