package org.spoofax.jsglr2.actions;

public class Goto implements IGoto {

    private final int[] productions;
	private final int gotoState;
	
	public Goto(int[] productions, int gotoState) {
		this.productions = productions;
		this.gotoState = gotoState;
	}
	
	public int[] productions() {
	    return productions;
	}
	
	public int gotoState() {
        return gotoState;
    }
	
}
