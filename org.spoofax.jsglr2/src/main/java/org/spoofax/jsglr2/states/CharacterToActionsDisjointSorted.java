package org.spoofax.jsglr2.states;

import java.util.ArrayList;
import java.util.List;

import org.spoofax.jsglr2.actions.ActionsPerCharacterClass;
import org.spoofax.jsglr2.actions.IAction;
import org.spoofax.jsglr2.actions.IReduce;
import org.spoofax.jsglr2.characterclasses.ICharacterClass;
import org.spoofax.jsglr2.parser.Parse;
import org.spoofax.jsglr2.util.DisjointRanges;
import org.spoofax.jsglr2.util.Range;

public final class CharacterToActionsDisjointSorted implements ICharacterToActions {

    private final ActionsForRange[] actionsForSortedRanges;

    public CharacterToActionsDisjointSorted(ActionsPerCharacterClass[] actionsPerCharacterClasses) {
        this.actionsForSortedRanges = disjointSort(actionsPerCharacterClasses);
    }

    public static ActionsForRange[] disjointSort(ActionsPerCharacterClass[] actionsPerCharacterClasses) {
        List<Range> disjointRanges = DisjointRanges.get(actionsPerCharacterClasses, actionsPerCharacterClass -> {
            return new Range(actionsPerCharacterClass.characterClass.min(),
                actionsPerCharacterClass.characterClass.max());
        });

        ActionsForRange[] res = new ActionsForRange[disjointRanges.size()];

        for(int i = 0; i < disjointRanges.size(); i++) {
            Range range = disjointRanges.get(i);

            List<ActionsPerCharacterClass> forRange = new ArrayList<>();

            for(ActionsPerCharacterClass actionsPerCharacterClass : actionsPerCharacterClasses) {
                ICharacterClass characterClass = actionsPerCharacterClass.characterClass;

                if(characterClass.min() <= range.from && range.to <= characterClass.max())
                    forRange.add(actionsPerCharacterClass);
            }

            if(forRange.size() == 1)
                res[i] = new SingleActionGroupForRange(range, forRange.get(0));
            else
                res[i] = new MultipleActionGroupsForRange(range,
                    forRange.toArray(new ActionsPerCharacterClass[forRange.size()]));
        }

        return res;
    }

    @Override public IAction[] getActions() {
        List<IAction> res = new ArrayList<>();

        for(ActionsForRange actionsForRange : actionsForSortedRanges) {
            res.addAll(actionsForRange.getActions());
        }

        return res.toArray(new IAction[res.size()]);
    }

    @Override public Iterable<IAction> getActions(int character) {
        return getActionsBinarySearch(character, 0, actionsForSortedRanges.length - 1);
    }

    private Iterable<IAction> getActionsBinarySearch(int character, int low, int high) {
        if(high <= low)
            return actionsForSortedRanges[low].getActions(character);
        else {
            int mid = (low + high) / 2;

            if(character < actionsForSortedRanges[mid].range.from)
                return getActionsBinarySearch(character, low, mid - 1);
            else if(character > actionsForSortedRanges[mid].range.to)
                return getActionsBinarySearch(character, mid + 1, high);
            else
                return actionsForSortedRanges[mid].getActions(character);
        }
    }

    @Override public Iterable<IReduce> getReduceActions(Parse parse) {
        return getReduceActionsBinarySearch(parse, 0, actionsForSortedRanges.length - 1);
    }

    private Iterable<IReduce> getReduceActionsBinarySearch(Parse parse, int low, int high) {
        if(high <= low)
            return actionsForSortedRanges[low].getReduceActions(parse);
        else {
            int mid = (low + high) / 2;

            if(parse.currentChar < actionsForSortedRanges[mid].range.from)
                return getReduceActionsBinarySearch(parse, low, mid - 1);
            else if(parse.currentChar > actionsForSortedRanges[mid].range.to)
                return getReduceActionsBinarySearch(parse, mid + 1, high);
            else
                return actionsForSortedRanges[mid].getReduceActions(parse);
        }
    }

}
