package org.spoofax.jsglr2.stack.elkhound;

import java.util.ArrayList;
import org.spoofax.jsglr2.parsetable.IState;
import org.spoofax.jsglr2.stack.StackLink;

public class BasicElkhoundStackNode<ParseForest> extends AbstractElkhoundStackNode<ParseForest> {

    // Directed to the initial stack node
    private ArrayList<StackLink<AbstractElkhoundStackNode<ParseForest>, ParseForest>> linksOut = new ArrayList<StackLink<AbstractElkhoundStackNode<ParseForest>, ParseForest>>();
    
    // Directed from the initial stack node
    private ArrayList<StackLink<AbstractElkhoundStackNode<ParseForest>, ParseForest>> linksIn = new ArrayList<StackLink<AbstractElkhoundStackNode<ParseForest>, ParseForest>>();
    
	public BasicElkhoundStackNode(int stackNumber, IState state, int deterministicDepth) {
		super(stackNumber, state, deterministicDepth);
	}
    
    public Iterable<StackLink<AbstractElkhoundStackNode<ParseForest>, ParseForest>> getLinksOut() {
        return linksOut;
    }
    
    public StackLink<AbstractElkhoundStackNode<ParseForest>, ParseForest> getOnlyLinkOut() {
    		return linksOut.get(0);
    }
    
    public Iterable<StackLink<AbstractElkhoundStackNode<ParseForest>, ParseForest>> getLinksIn() {
        return linksIn;
    }
    
    public StackLink<AbstractElkhoundStackNode<ParseForest>, ParseForest> addOutLink(StackLink<AbstractElkhoundStackNode<ParseForest>, ParseForest> link) {
        linksOut.add(link);
        
        link.to.addInLink(link);
        
        if (linksOut.size() == 1) { // This means the first link is just added.
            deterministicDepth = link.to.deterministicDepth + 1;
        } else if (linksOut.size() == 2) { // The second link is added; this means non-determinism
            deterministicDepth = 0;
            
            for (StackLink<AbstractElkhoundStackNode<ParseForest>, ParseForest> linkIn : getLinksIn())
                linkIn.from.resetDeterministicDepth(1);
        } // We do not handle the case > 2, since the case == 2 already adjusted deterministic depths
        
        return link;
    }
    
    protected void addInLink(StackLink<AbstractElkhoundStackNode<ParseForest>, ParseForest> link) {
        linksIn.add(link);
    }
	
    public boolean allOutLinksRejected() {
        if (linksOut.isEmpty())
            return false;
        
        for (StackLink<AbstractElkhoundStackNode<ParseForest>, ParseForest> link : linksOut) {
            if (!link.isRejected())
                return false;
        }
        
        return true;
    }
	
}
