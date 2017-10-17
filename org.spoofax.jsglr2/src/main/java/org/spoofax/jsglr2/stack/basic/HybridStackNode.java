package org.spoofax.jsglr2.stack.basic;

import java.util.ArrayList;
import org.spoofax.jsglr2.parsetable.IState;
import org.spoofax.jsglr2.stack.StackLink;
import org.spoofax.jsglr2.util.iterators.SingleElementIterable;
import org.spoofax.jsglr2.util.iterators.SingleElementWithListIterable;

public class HybridStackNode<ParseForest> extends AbstractBasicStackNode<ParseForest> {

    private StackLink<AbstractBasicStackNode<ParseForest>, ParseForest> firstLinkOut;
    private ArrayList<StackLink<AbstractBasicStackNode<ParseForest>, ParseForest>> otherLinksOut;
	
	public HybridStackNode(int stackNumber, IState state, int offset) {
		super(stackNumber, state, offset);
	}
	
	public Iterable<StackLink<AbstractBasicStackNode<ParseForest>, ParseForest>> getLinksOut() {
		if (otherLinksOut == null)
	    		return new SingleElementIterable<StackLink<AbstractBasicStackNode<ParseForest>, ParseForest>>(firstLinkOut);
	    else
	        return new SingleElementWithListIterable<StackLink<AbstractBasicStackNode<ParseForest>, ParseForest>>(firstLinkOut, otherLinksOut);
	}
    
    public StackLink<AbstractBasicStackNode<ParseForest>, ParseForest> addOutLink(StackLink<AbstractBasicStackNode<ParseForest>, ParseForest> link) {
    		if (firstLinkOut == null)
    			firstLinkOut = link;
		else {
			if (otherLinksOut == null)
				otherLinksOut = new ArrayList<StackLink<AbstractBasicStackNode<ParseForest>, ParseForest>>();
			
			otherLinksOut.add(link);
		}
        
        return link;
    }
	
	public boolean allOutLinksRejected() {
        if (firstLinkOut == null || !firstLinkOut.isRejected())
            return false;
        		
    		if (otherLinksOut == null)
    			return true;
    		
        for (StackLink<AbstractBasicStackNode<ParseForest>, ParseForest> link : otherLinksOut) {
            if (!link.isRejected())
                return false;
        }
        
        return true;
    }
	
}
