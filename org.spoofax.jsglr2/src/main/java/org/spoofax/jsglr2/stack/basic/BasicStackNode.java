package org.spoofax.jsglr2.stack.basic;

import java.util.ArrayList;
import java.util.List;

import org.spoofax.jsglr2.parsetable.IState;
import org.spoofax.jsglr2.stack.StackLink;

public class BasicStackNode<ParseForest> extends AbstractBasicStackNode<ParseForest> {

    private final ArrayList<StackLink<AbstractBasicStackNode<ParseForest>, ParseForest>> linksOut = new ArrayList<StackLink<AbstractBasicStackNode<ParseForest>, ParseForest>>(); // Directed to the initial stack node
	
	public BasicStackNode(int stackNumber, IState state) {
		super(stackNumber, state);
	}
	
	public List<StackLink<AbstractBasicStackNode<ParseForest>, ParseForest>> getLinksOut() {
		return linksOut;
	}
    
    public StackLink<AbstractBasicStackNode<ParseForest>, ParseForest> addOutLink(StackLink<AbstractBasicStackNode<ParseForest>, ParseForest> link) {
        linksOut.add(link);
        
        return link;
    }
	
	public boolean allOutLinksRejected() {
		if (linksOut.isEmpty())
			return false;
		
		for (StackLink<AbstractBasicStackNode<ParseForest>, ParseForest> link : linksOut) {
			if (!link.isRejected())
				return false;
		}
		
		return true;
	}
	
}
