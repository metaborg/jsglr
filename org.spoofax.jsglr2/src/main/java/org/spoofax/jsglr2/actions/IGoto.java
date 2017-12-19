package org.spoofax.jsglr2.actions;

/*
 * Represents the goto state for a set of productions.
 */
public interface IGoto {

    int[] productionIds();

    int gotoStateId();

}
