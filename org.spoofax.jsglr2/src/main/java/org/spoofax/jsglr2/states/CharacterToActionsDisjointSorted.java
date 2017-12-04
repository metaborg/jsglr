package org.spoofax.jsglr2.states;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.spoofax.jsglr2.actions.ActionsPerCharacterClass;
import org.spoofax.jsglr2.actions.IAction;
import org.spoofax.jsglr2.actions.IReduce;
import org.spoofax.jsglr2.characterclasses.ICharacterClass;
import org.spoofax.jsglr2.parser.Parse;

public final class CharacterToActionsDisjointSorted implements ICharacterToActions {

    private final ActionsForRange[] actionsForSortedDisjointRanges;

    public CharacterToActionsDisjointSorted(ActionsPerCharacterClass[] actionsPerCharacterClasses) {
        this.actionsForSortedDisjointRanges = toDisjointSortedRanges(actionsPerCharacterClasses);
    }

    public static ActionsForRange[] toDisjointSortedRanges(ActionsPerCharacterClass[] actionsPerCharacterClasses) {
        List<ActionsForRange> actionsForRanges = new ArrayList<>();

        int newRangeFromCharacter = -1; // Contains the start character for the next range that will be added
        Set<IAction> newRangeActions = null; // Contains the actions for the next range that will be added

        for(int character = 0; character <= ICharacterClass.EOF_INT; character++) {
            Set<IAction> actionsForCharacter = null;

            for(ActionsPerCharacterClass actionsPerCharacterClass : actionsPerCharacterClasses) {
                if(actionsPerCharacterClass.appliesTo(character)) {
                    if(actionsForCharacter == null)
                        actionsForCharacter = new HashSet<>();

                    actionsForCharacter.addAll(actionsPerCharacterClass.actions);
                }
            }

            /*
             * Based on the applicable actions for the current character and if a new range already started before: do
             * nothing, create the range that was already started, start a new range or both of the latter.
             */
            if(actionsForCharacter == null || actionsForCharacter.isEmpty()) {
                if(newRangeFromCharacter != -1) { // A range ended on the previous character
                    actionsForRanges
                        .add(new ActionsForRange(newRangeActions.toArray(new IAction[newRangeActions.size()]),
                            newRangeFromCharacter, character - 1));

                    newRangeFromCharacter = -1;
                    newRangeActions = null;
                }
            } else {
                if(newRangeFromCharacter != -1) {
                    if(!actionsForCharacter.equals(newRangeActions)) { // Different actions, thus a range ended on the
                                                                       // previous character and a new one starts at the
                                                                       // current character
                        actionsForRanges
                            .add(new ActionsForRange(newRangeActions.toArray(new IAction[newRangeActions.size()]),
                                newRangeFromCharacter, character - 1));

                        newRangeFromCharacter = character;
                        newRangeActions = actionsForCharacter;
                    }
                } else { // A new range starts at the current character
                    newRangeFromCharacter = character;
                    newRangeActions = actionsForCharacter;
                }

                if(character == ICharacterClass.EOF_INT && newRangeFromCharacter != -1) {
                    actionsForRanges
                        .add(new ActionsForRange(newRangeActions.toArray(new IAction[newRangeActions.size()]),
                            newRangeFromCharacter, character));
                }
            }
        }

        return actionsForRanges.toArray(new ActionsForRange[actionsForRanges.size()]);
    }

    @Override
    public IAction[] getActions() {
        List<IAction> res = new ArrayList<>();

        for(ActionsForRange actionsForRange : actionsForSortedDisjointRanges) {
            for(IAction action : actionsForRange.getActions())
                res.add(action);
        }

        return res.toArray(new IAction[res.size()]);
    }

    @Override
    public Iterable<IAction> getActions(int character) {
        if(actionsForSortedDisjointRanges.length > 0) {
            int low = 0, high = actionsForSortedDisjointRanges.length - 1;

            while(low <= high) {
                int mid = (low + high) / 2;

                ActionsForRange actionsForMidRange = actionsForSortedDisjointRanges[mid];

                if(actionsForMidRange.from <= character && character <= actionsForMidRange.to)
                    return actionsForMidRange.getActions();
                else if(character < actionsForMidRange.from)
                    high = mid - 1;
                else if(actionsForMidRange.to < character)
                    low = mid + 1;
                else
                    break;
            }
        }

        return Collections.emptyList();
    }

    @Override
    public Iterable<IReduce> getReduceActions(Parse<?, ?> parse) {
        if(actionsForSortedDisjointRanges.length > 0) {
            int low = 0, high = actionsForSortedDisjointRanges.length - 1;

            while(low <= high) {
                int mid = (low + high) / 2;

                ActionsForRange actionsForMidRange = actionsForSortedDisjointRanges[mid];

                if(actionsForMidRange.from <= parse.currentChar && parse.currentChar <= actionsForMidRange.to)
                    return actionsForMidRange.getReduceActions(parse);
                else if(parse.currentChar < actionsForMidRange.from)
                    high = mid - 1;
                else if(actionsForMidRange.to < parse.currentChar)
                    low = mid + 1;
                else
                    break;
            }
        }

        return Collections.emptyList();
    }

}
