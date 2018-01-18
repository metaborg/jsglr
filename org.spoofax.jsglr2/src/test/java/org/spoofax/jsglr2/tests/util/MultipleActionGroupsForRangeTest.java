package org.spoofax.jsglr2.tests.util;

import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.metaborg.characterclasses.CharacterClassFactory;
import org.metaborg.characterclasses.ICharacterClassFactory;
import org.metaborg.parsetable.IParseInput;
import org.metaborg.parsetable.actions.IAction;
import org.metaborg.parsetable.characterclasses.ICharacterClass;
import org.metaborg.sdf2table.parsetable.query.ActionsForCharacterDisjointSorted;
import org.metaborg.sdf2table.parsetable.query.ActionsForCharacterSeparated;
import org.metaborg.sdf2table.parsetable.query.ActionsPerCharacterClass;
import org.metaborg.sdf2table.parsetable.query.IActionsForCharacter;
import org.spoofax.jsglr2.actions.ActionsFactory;
import org.spoofax.jsglr2.actions.IActionsFactory;

public class MultipleActionGroupsForRangeTest {

    ICharacterClassFactory characterClassFactory = new CharacterClassFactory(true, true);

    ICharacterClass AZ = characterClassFactory.fromRange(65, 90);
    ICharacterClass az = characterClassFactory.fromRange(97, 122);

    ICharacterClass ab = characterClassFactory.fromRange(97, 98);
    ICharacterClass bc = characterClassFactory.fromRange(98, 99);

    ICharacterClass a = characterClassFactory.fromSingle(97);
    ICharacterClass b = characterClassFactory.fromSingle(98);
    ICharacterClass ce = characterClassFactory.fromRange(99, 101);
    ICharacterClass f = characterClassFactory.fromSingle(102);
    ICharacterClass gz = characterClassFactory.fromRange(103, 122);

    IActionsFactory actionsFactory = new ActionsFactory(true);

    IAction shift1 = actionsFactory.getShift(1);
    IAction shift2 = actionsFactory.getShift(2);
    IAction shift3 = actionsFactory.getShift(3);

    IAction shift13 = actionsFactory.getShift(13);
    IAction shift14 = actionsFactory.getShift(14);
    IAction shift15 = actionsFactory.getShift(15);

    @Test
    public void test1() {
        // @formatter:off
        ActionsPerCharacterClass[] actionsPerCharacterClasses =
            new ActionsPerCharacterClass[] {
                new ActionsPerCharacterClass(ab, new IAction[] { shift1, shift2 }),
                new ActionsPerCharacterClass(bc, new IAction[] { shift2, shift3 })
            };
        // @formatter:on

        test(actionsPerCharacterClasses);
    }

    @Test
    public void test2() {
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
        IActionsForCharacter separated = new ActionsForCharacterSeparated(actionsPerCharacterClasses);
        IActionsForCharacter disjointSorted = new ActionsForCharacterDisjointSorted(actionsPerCharacterClasses);

        for(int character = 0; character <= CharacterClassFactory.EOF_INT; character++) {
            IParseInput parseInput = new MockParseInput(character);

            Set<IAction> actionForSeparated = iterableToSet(separated.getApplicableActions(parseInput));
            Set<IAction> actionForDisjointSorted = iterableToSet(disjointSorted.getApplicableActions(parseInput));

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
