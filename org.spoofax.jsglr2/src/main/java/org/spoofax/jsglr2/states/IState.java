package org.spoofax.jsglr2.states;

import org.spoofax.jsglr2.actions.IAction;
import org.spoofax.jsglr2.actions.IReduce;
import org.spoofax.jsglr2.parser.IParseInput;

public interface IState {

    int id();

    boolean isRejectable();

    Iterable<IAction> getApplicableActions(IParseInput parseInput);

    Iterable<IReduce> getApplicableReduceActions(IParseInput parseInput);

    int getGotoId(int productionId);

}
