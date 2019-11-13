package org.spoofax.jsglr2.measure.parsing;

public enum ParsingMeasurement {
    name, size, characters, activeStacksAdds, activeStacksMaxSize, activeStacksIsSingleChecks,
    activeStacksIsEmptyChecks, activeStacksFindsWithState, activeStacksForLimitedReductions, activeStacksAddAllTo,
    activeStacksClears, activeStacksIterators, forActorAdds, forActorDelayedAdds, forActorMaxSize,
    forActorDelayedMaxSize, forActorContainsChecks, forActorNonEmptyChecks, stackNodes, stackNodesSingleLink,
    stackLinks, stackLinksRejected, deterministicDepthResets, parseNodes, parseNodesSingleDerivation,
    parseNodesAmbiguous, parseNodesContextFree, parseNodesContextFreeAmbiguous, parseNodesLexical,
    parseNodesLexicalAmbiguous, parseNodesLayout, parseNodesLayoutAmbiguous, characterNodes, actors, doReductions,
    doLimitedReductions, doReductionsLR, doReductionsDeterministicGLR, doReductionsNonDeterministicGLR, reducers,
    reducersElkhound
}