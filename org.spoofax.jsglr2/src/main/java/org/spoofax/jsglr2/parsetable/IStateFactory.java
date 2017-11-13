package org.spoofax.jsglr2.parsetable;

import org.spoofax.jsglr2.actions.IAction;
import org.spoofax.jsglr2.actions.IGoto;

public interface IStateFactory {

    IState from(int stateNumber, IGoto[] gotos, IAction[] actions);

}
