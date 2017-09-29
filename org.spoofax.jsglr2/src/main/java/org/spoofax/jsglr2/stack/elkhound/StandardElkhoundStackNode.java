package org.spoofax.jsglr2.stack.elkhound;

import java.util.ArrayList;
import org.spoofax.jsglr2.parsetable.IState;
import org.spoofax.jsglr2.stack.StackLink;

public class StandardElkhoundStackNode<ParseForest> extends ElkhoundStackNode<ParseForest> {

    // Directed to the initial stack node
    private ArrayList<StackLink<ElkhoundStackNode<ParseForest>, ParseForest>> linksOut = new ArrayList<StackLink<ElkhoundStackNode<ParseForest>, ParseForest>>();
    
    // Directed from the initial stack node
    private ArrayList<StackLink<ElkhoundStackNode<ParseForest>, ParseForest>> linksIn = new ArrayList<StackLink<ElkhoundStackNode<ParseForest>, ParseForest>>();
    
	public StandardElkhoundStackNode(int stackNumber, IState state, int deterministicDepth) {
		super(stackNumber, state, deterministicDepth);
	}
    
    public Iterable<StackLink<ElkhoundStackNode<ParseForest>, ParseForest>> getLinksOut() {
        return linksOut;
    }
    
    public StackLink<ElkhoundStackNode<ParseForest>, ParseForest> getOnlyLinkOut() {
    		return linksOut.get(0);
    }
    
    public Iterable<StackLink<ElkhoundStackNode<ParseForest>, ParseForest>> getLinksIn() {
        return linksIn;
    }
    
    public StackLink<ElkhoundStackNode<ParseForest>, ParseForest> addOutLink(StackLink<ElkhoundStackNode<ParseForest>, ParseForest> link) {
        linksOut.add(link);
        
        link.to.addInLink(link);
        
        if (linksOut.size() == 1) { // This means the first link is just added.
            deterministicDepth = link.to.deterministicDepth + 1;
        } else if (linksOut.size() == 2) { // The second link is added; this means non-determinism
            deterministicDepth = 0;
            
            for (StackLink<ElkhoundStackNode<ParseForest>, ParseForest> linkIn : getLinksIn())
                linkIn.from.resetDeterministicDepth(1);
        } // We do not handle the case > 2, since the case == 2 already adjusted deterministic depths
        
        return link;
    }
    
    protected void addInLink(StackLink<ElkhoundStackNode<ParseForest>, ParseForest> link) {
        linksIn.add(link);
    }
    
    public void resetDeterministicDepth(int deterministicDepth) {
        if (deterministicDepth != 0) {
            this.deterministicDepth = deterministicDepth;
            
            for (StackLink<ElkhoundStackNode<ParseForest>, ParseForest> linkIn : getLinksIn())
                linkIn.from.resetDeterministicDepth(deterministicDepth + 1);
        }
    }
	
    public boolean allOutLinksRejected() {
        if (linksOut.isEmpty())
            return false;
        
        for (StackLink<ElkhoundStackNode<ParseForest>, ParseForest> link : linksOut) {
            if (!link.isRejected())
                return false;
        }
        
        return true;
    }
	
}
