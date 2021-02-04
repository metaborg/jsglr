package org.spoofax.jsglr2.measure.incremental;

public enum IncrementalParsingMeasurement {
    version,

    parseNodes, parseNodesAmbiguous, parseNodesNonDeterministic, parseNodesReused, parseNodesRebuilt, //
    characterNodes, characterNodesReused,

    createCharacterNode, createParseNode, shiftCharacterNode, shiftParseNode, //
    breakDowns, breakDownNoActions, breakDownNonDeterministic, breakDownWrongState
}
