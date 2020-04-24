package org.spoofax.jsglr2.measure.parsetable;

public enum ParseTableMeasurement {
    states, characterClasses, characterClassesUnique, characterClassesOptimizedUnique,
    statesDisjointSortableCharacterClasses, gotos, gotosPerStateMax, actionGroups, actionDisjointSortedRanges, actions,
    actionGroupsPerStateMax, actionDisjointSortedRangesPerStateMax, actionsPerStateMax, actionsPerGroupMax,
    actionsPerDisjointSortedRangeMax
}