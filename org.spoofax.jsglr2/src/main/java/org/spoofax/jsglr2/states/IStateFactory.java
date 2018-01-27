package org.spoofax.jsglr2.states;

import org.metaborg.parsetable.IState;
import org.metaborg.parsetable.actions.IGoto;
import org.metaborg.sdf2table.parsetable.query.ActionsPerCharacterClass;

public interface IStateFactory {

    IState from(int stateId, IGoto[] gotos, ActionsPerCharacterClass[] actions);

}
