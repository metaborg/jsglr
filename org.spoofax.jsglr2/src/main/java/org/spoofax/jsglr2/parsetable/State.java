package org.spoofax.jsglr2.parsetable;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.IntStream;

import org.spoofax.jsglr2.actions.ActionType;
import org.spoofax.jsglr2.actions.IAction;
import org.spoofax.jsglr2.actions.IGoto;
import org.spoofax.jsglr2.actions.IReduce;
import org.spoofax.jsglr2.actions.IReduceLookahead;
import org.spoofax.jsglr2.parser.Parse;

import io.usethesource.capsule.BinaryRelation;

public final class State implements IState {

    private final int stateNumber;
    private final IAction[] actions;
    private boolean rejectable;

    final BinaryRelation.Immutable<Integer, Integer> productionToGoto;

    public State(int stateNumber, IGoto[] gotos, IAction[] actions) {
        this.stateNumber = stateNumber;
        this.actions = actions;
        this.rejectable = false;

        final BinaryRelation.Transient<Integer, Integer> tmpProductionToGoto = BinaryRelation.Transient.of();

        for(IGoto action : gotos) {
            int gotoId = action.gotoState();
            IntStream.of(action.productions()).forEach(productionId -> tmpProductionToGoto.__put(productionId, gotoId));
        }

        this.productionToGoto = tmpProductionToGoto.freeze();
    }

    @Override public int stateNumber() {
        return stateNumber;
    }

    @Override public IAction[] actions() {
        return actions;
    }

    @Override public boolean isRejectable() {
        return rejectable;
    }

    public void markRejectable() {
        this.rejectable = true;
    }

    @Override public Iterable<IAction> applicableActions(byte character) {
        // NOTE: simple code
        // final Iterator<IAction> iterator = Stream.of(actions)
        // .filter(action -> action.appliesTo(character))
        // .iterator();
        //
        // return () -> iterator;

        // NOTE: faster code
        return () -> new Iterator<IAction>() {
            int index = 0;

            @Override public boolean hasNext() {
                // skip non-applicable actions
                while(index < actions.length && !actions[index].appliesTo(character)) {
                    index++;
                }
                return index < actions.length;
            }

            @Override public IAction next() {
                if(!hasNext()) {
                    throw new NoSuchElementException();
                }
                return actions[index++];
            }
        };
    }

    @Override public Iterable<IReduce> applicableReduceActions(Parse parse) {
        // // NOTE: simple code
        // final Iterator<IReduce> iterator = Stream.of(actions)
        // .filter(action -> action.appliesTo(parse.currentChar))
        // .filter(action -> action.actionType() == ActionType.REDUCE ||
        // action.actionType() == ActionType.REDUCE_LOOKAHEAD && ((IReduceLookahead) action).allowsLookahead(parse))
        // .map(action -> (IReduce) action)
        // .iterator();
        //
        // return () -> iterator;

        // NOTE: faster code
        return () -> new Iterator<IReduce>() {
            int index = 0;

            @Override public boolean hasNext() {
                while(index < actions.length && !(actions[index].appliesTo(parse.currentChar)
                    && (actions[index].actionType() == ActionType.REDUCE
                        || actions[index].actionType() == ActionType.REDUCE_LOOKAHEAD
                            && ((IReduceLookahead) actions[index]).allowsLookahead(parse)))) {
                    index++;
                }
                return index < actions.length;
            }

            @Override public IReduce next() {
                if(!hasNext()) {
                    throw new NoSuchElementException();
                }
                return (IReduce) actions[index++];
            }
        };
    }

    @Override public Iterable<IAction> applicableActionsEOF() {
        return () -> new Iterator<IAction>() {
            int index = 0;

            @Override public boolean hasNext() {
                // skip non-applicable actions
                while(index < actions.length && !actions[index].appliesToEOF()) {
                    index++;
                }
                return index < actions.length;
            }

            @Override public IAction next() {
                if(!hasNext()) {
                    throw new NoSuchElementException();
                }
                return actions[index++];
            }
        };
    }

    @Override public Optional<Integer> getGotoId(int productionId) {
        assert productionToGoto.get(productionId).size() <= 1;
        return productionToGoto.get(productionId).findFirst();
    }

    @Override public boolean equals(Object obj) {
        if(!(obj instanceof State))
            return false;

        State otherState = (State) obj;

        return this.stateNumber == otherState.stateNumber;
    }

}
