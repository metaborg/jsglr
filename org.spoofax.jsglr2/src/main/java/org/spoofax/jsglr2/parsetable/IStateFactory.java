package org.spoofax.jsglr2.parsetable;

import org.spoofax.jsglr2.actions.ActionsPerCharacterClass;
import org.spoofax.jsglr2.actions.IGoto;

public interface IStateFactory {

    IState from(int stateNumber, IGoto[] gotos, ActionsPerCharacterClass[] actions);

}
