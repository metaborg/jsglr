package org.spoofax.jsglr2.measure.incremental;

public enum IncrementalParsingMeasurement {
    version,

    parseNodes, parseNodesAmbiguous, parseNodesIrreusable, parseNodesReused, parseNodesRebuilt, //
    characterNodes, characterNodesReused,

    createCharacterNode, createParseNode, shiftCharacterNode, shiftParseNode, //
    breakDowns, breakDownHasChange, breakDownIrreusable, breakDownNoActions, breakDownTemporary, breakDownWrongState
}
