package org.spoofax.jsglr2.tests.util;

import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.spoofax.jsglr2.actions.ActionsPerCharacterClass;
import org.spoofax.jsglr2.actions.IAction;
import org.spoofax.jsglr2.actions.IActionsFactory;
import org.spoofax.jsglr2.characterclasses.ICharacterClass;
import org.spoofax.jsglr2.characterclasses.ICharacterClassFactory;
import org.spoofax.jsglr2.states.CharacterToActionsDisjointSorted;
import org.spoofax.jsglr2.states.CharacterToActionsSeparated;
import org.spoofax.jsglr2.states.ICharacterToActions;

public class MultipleActionGroupsForRangeTest {

    ICharacterClassFactory characterClassFactory = ICharacterClass.factory();

    ICharacterClass AZ = characterClassFactory.fromRange(65, 90);
    ICharacterClass az = characterClassFactory.fromRange(97, 122);

    ICharacterClass ab = characterClassFactory.fromRange(97, 98);
    ICharacterClass bc = characterClassFactory.fromRange(98, 99);

    ICharacterClass a = characterClassFactory.fromSingle(97);
    ICharacterClass b = characterClassFactory.fromSingle(98);
    ICharacterClass ce = characterClassFactory.fromRange(99, 101);
    ICharacterClass f = characterClassFactory.fromSingle(102);
    ICharacterClass gz = characterClassFactory.fromRange(103, 122);

    IActionsFactory actionsFactory = IAction.factory();

    IAction shift1 = actionsFactory.getShift(1);
    IAction shift2 = actionsFactory.getShift(2);
    IAction shift3 = actionsFactory.getShift(3);

    IAction shift13 = actionsFactory.getShift(13);
    IAction shift14 = actionsFactory.getShift(14);
    IAction shift15 = actionsFactory.getShift(15);

    @Test public void test1() {
        // @formatter:off
        ActionsPerCharacterClass[] actionsPerCharacterClasses =
            new ActionsPerCharacterClass[] {
                new ActionsPerCharacterClass(ab, new IAction[] { shift1, shift2 }),
                new ActionsPerCharacterClass(bc, new IAction[] { shift2, shift3 })
            };
        // @formatter:on

        test(actionsPerCharacterClasses);
    }

    @Test public void test2() {
        // @formatter:off
        ActionsPerCharacterClass[] actionsPerCharacterClasses =
            new ActionsPerCharacterClass[] {
                new ActionsPerCharacterClass(characterClassFactory.union(characterClassFactory.union(a, ce), gz), new IAction[] { shift13 }),
                new ActionsPerCharacterClass(b, new IAction[] { shift15}),
                new ActionsPerCharacterClass(f, new IAction[] { shift14})
            };
        // @formatter:on

        test(actionsPerCharacterClasses);
    }

    public void test(ActionsPerCharacterClass[] actionsPerCharacterClasses) {
        ICharacterToActions separated = new CharacterToActionsSeparated(actionsPerCharacterClasses);
        ICharacterToActions disjointSorted = new CharacterToActionsDisjointSorted(actionsPerCharacterClasses);

        for(int character = 0; character <= ICharacterClass.EOF_INT; character++) {
            Set<IAction> actionForSeparated = iterableToSet(separated.getActions(character));
            Set<IAction> actionForDisjointSorted = iterableToSet(disjointSorted.getActions(character));

            assertEquals("Action sets not equal for character " + character, actionForSeparated,
                actionForDisjointSorted);
        }
    }

    private <T> Set<T> iterableToSet(Iterable<T> iterable) {
        Set<T> set = new HashSet<>();

        for(T t : iterable)
            set.add(t);

        return set;
    }

}
