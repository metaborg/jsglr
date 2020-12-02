package org.spoofax.jsglr2.measure.parsetable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.metaborg.parsetable.actions.IGoto;
import org.metaborg.parsetable.characterclasses.ICharacterClass;
import org.metaborg.parsetable.query.ActionsForCharacterDisjointSorted;
import org.metaborg.parsetable.query.ActionsForRange;
import org.metaborg.parsetable.query.ActionsPerCharacterClass;
import org.metaborg.parsetable.states.IState;
import org.metaborg.parsetable.states.StateFactory;

public class MeasureStateFactory extends StateFactory {

    long statesCount = 0;
    long statesDisjointSortableCharacterClassesCount = 0;

    long gotosCount = 0;
    long actionGroupsCount = 0;
    long actionDisjointSortedRangesCount = 0;
    long actionsCount = 0;

    long gotosPerStateMax = 0;
    long actionGroupsPerStateMax = 0;
    long actionDisjointSortedRangesPerStateMax = 0;
    long actionsPerStateMax = 0;
    long actionsPerGroupMax = 0;
    long actionsPerDisjointSortedRangeMax = 0;

    @Override public IState from(int stateNumber, IGoto[] gotos, ActionsPerCharacterClass[] actionsPerCharacterClasses,
        Set<Integer> recoveryStateIds) {
        statesCount++;

        gotosCount += gotos.length;
        actionGroupsCount += actionsPerCharacterClasses.length;

        int actionsCount = 0;

        List<ICharacterClass> characterClasses = new ArrayList<>(actionsPerCharacterClasses.length);

        for(ActionsPerCharacterClass actionsPerCharacterClass : actionsPerCharacterClasses) {
            actionsCount += actionsPerCharacterClass.actions.size();

            actionsPerGroupMax = Math.max(actionsPerGroupMax, actionsPerCharacterClass.actions.size());

            characterClasses.add(actionsPerCharacterClass.characterClass);
        }

        statesDisjointSortableCharacterClassesCount += ICharacterClass.disjointSortable(characterClasses) ? 1 : 0;

        ActionsForRange[] actionsForSortedDisjointRanges =
            ActionsForCharacterDisjointSorted.toDisjointSortedRanges(actionsPerCharacterClasses);

        actionDisjointSortedRangesCount += actionsForSortedDisjointRanges.length;

        for(ActionsForRange actionsForSortedDisjointRange : actionsForSortedDisjointRanges) {
            actionsPerDisjointSortedRangeMax =
                Math.max(actionsPerDisjointSortedRangeMax, actionsForSortedDisjointRange.actions.length);
        }

        actionsCount += actionsCount;

        gotosPerStateMax = Math.max(gotosPerStateMax, gotos.length);
        actionGroupsPerStateMax = Math.max(actionGroupsPerStateMax, actionsPerCharacterClasses.length);
        actionDisjointSortedRangesPerStateMax =
            Math.max(actionDisjointSortedRangesPerStateMax, actionsForSortedDisjointRanges.length);
        actionsPerStateMax = Math.max(actionsPerStateMax, actionsCount);

        return super.from(stateNumber, gotos, actionsPerCharacterClasses, recoveryStateIds);
    }

}
