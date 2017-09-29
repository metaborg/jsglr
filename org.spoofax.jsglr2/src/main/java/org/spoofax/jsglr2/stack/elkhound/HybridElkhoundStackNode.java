package org.spoofax.jsglr2.stack.elkhound;

import java.util.ArrayList;
import java.util.Collections;

import org.spoofax.jsglr2.parsetable.IState;
import org.spoofax.jsglr2.stack.StackLink;
import org.spoofax.jsglr2.util.iterators.SingleElementIterable;
import org.spoofax.jsglr2.util.iterators.SingleElementWithListIterable;

public class HybridElkhoundStackNode<ParseForest> extends ElkhoundStackNode<ParseForest> {

    // Directed to the initial stack node (to the origin)
    private StackLink<ElkhoundStackNode<ParseForest>, ParseForest> firstLinkOut;
    private ArrayList<StackLink<ElkhoundStackNode<ParseForest>, ParseForest>> otherLinksOut;
    
    // Directed from the initial stack node (to the future)
    private StackLink<ElkhoundStackNode<ParseForest>, ParseForest> firstLinkIn;
    private ArrayList<StackLink<ElkhoundStackNode<ParseForest>, ParseForest>> otherLinksIn;
    
	public HybridElkhoundStackNode(int stackNumber, IState state, int deterministicDepth) {
		super(stackNumber, state, deterministicDepth);
	}
    
    public Iterable<StackLink<ElkhoundStackNode<ParseForest>, ParseForest>> getLinksOut() {
        if (otherLinksOut == null)
        		return new SingleElementIterable<StackLink<ElkhoundStackNode<ParseForest>, ParseForest>>(firstLinkOut);
	    else
	        return new SingleElementWithListIterable<StackLink<ElkhoundStackNode<ParseForest>, ParseForest>>(firstLinkOut, otherLinksOut);
    }
    
    public StackLink<ElkhoundStackNode<ParseForest>, ParseForest> getOnlyLinkOut() {
    		return firstLinkOut;
    }
    
    public Iterable<StackLink<ElkhoundStackNode<ParseForest>, ParseForest>> getLinksIn() {
	    	if (firstLinkIn == null)
	    		return Collections.emptyList();
	    	else if (otherLinksIn == null)
	    		return new SingleElementIterable<StackLink<ElkhoundStackNode<ParseForest>, ParseForest>>(firstLinkIn);
	    else
	        return new SingleElementWithListIterable<StackLink<ElkhoundStackNode<ParseForest>, ParseForest>>(firstLinkIn, otherLinksIn);
    }
    
    public StackLink<ElkhoundStackNode<ParseForest>, ParseForest> addOutLink(StackLink<ElkhoundStackNode<ParseForest>, ParseForest> link) {
        link.to.addInLink(link);
        
    		if (firstLinkOut == null) { // This means where adding the first link now
    			firstLinkOut = link;
    		
    			deterministicDepth = link.to.deterministicDepth + 1;
    		} else if (otherLinksOut == null) { // The second link is added; at this point we detect non-determinism
    			otherLinksOut = new ArrayList<StackLink<ElkhoundStackNode<ParseForest>, ParseForest>>();
    			
    			otherLinksOut.add(link);
    			
    			deterministicDepth = 0;
                
            for (StackLink<ElkhoundStackNode<ParseForest>, ParseForest> linkIn : getLinksIn())
                linkIn.from.resetDeterministicDepth(1);
    		} else { // We do not handle the case > 2, since the case == 2 already adjusted deterministic depths
    			otherLinksOut.add(link);
    		}
        
        return link;
    }
    
    protected void addInLink(StackLink<ElkhoundStackNode<ParseForest>, ParseForest> link) {
    		if (firstLinkIn == null)
    			firstLinkIn = link;
    		else {
    			if (otherLinksIn == null)
    				otherLinksIn = new ArrayList<StackLink<ElkhoundStackNode<ParseForest>, ParseForest>>();
    			
    			otherLinksIn.add(link);
    		}
    }
    
    public void resetDeterministicDepth(int deterministicDepth) {
        if (deterministicDepth != 0) {
            this.deterministicDepth = deterministicDepth;
            
            for (StackLink<ElkhoundStackNode<ParseForest>, ParseForest> linkIn : getLinksIn())
                linkIn.from.resetDeterministicDepth(deterministicDepth + 1);
        }
    }
	
    public boolean allOutLinksRejected() {
        if (firstLinkOut == null || !firstLinkOut.isRejected())
            return false;
        		
    		if (otherLinksOut == null)
    			return true;
    		
        for (StackLink<ElkhoundStackNode<ParseForest>, ParseForest> link : otherLinksOut) {
            if (!link.isRejected())
                return false;
        }
        
        return true;
    }
	
}
