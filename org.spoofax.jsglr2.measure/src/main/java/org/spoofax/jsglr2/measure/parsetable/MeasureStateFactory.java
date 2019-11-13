package org.spoofax.jsglr2.measure.parsetable;

import java.util.ArrayList;
import java.util.List;

import org.metaborg.parsetable.actions.IGoto;
import org.metaborg.parsetable.characterclasses.ICharacterClass;
import org.metaborg.parsetable.query.ActionsForCharacterDisjointSorted;
import org.metaborg.parsetable.query.ActionsForRange;
import org.metaborg.parsetable.query.ActionsPerCharacterClass;
import org.metaborg.parsetable.states.IState;
import org.metaborg.parsetable.states.StateFactory;

public class MeasureStateFactory extends StateFactory {

    int statesCount = 0;
    int statesDisjointSortableCharacterClassesCount = 0;

    int gotosCount = 0;
    int actionGroupsCount = 0;
    int actionDisjointSortedRangesCount = 0;
    int actionsCount = 0;

    int gotosPerStateMax = 0;
    int actionGroupsPerStateMax = 0;
    int actionDisjointSortedRangesPerStateMax = 0;
    int actionsPerStateMax = 0;
    int actionsPerGroupMax = 0;
    int actionsPerDisjointSortedRangeMax = 0;

    @Override public IState from(int stateNumber, IGoto[] gotos,
        ActionsPerCharacterClass[] actionsPerCharacterClasses) {
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

        return super.from(stateNumber, gotos, actionsPerCharacterClasses);
    }

}
