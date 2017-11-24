package org.spoofax.jsglr2.states;

import org.spoofax.jsglr2.actions.ActionsPerCharacterClass;
import org.spoofax.jsglr2.actions.IGoto;

public interface IStateFactory {

    IState from(int stateId, IGoto[] gotos, ActionsPerCharacterClass[] actions);

}
