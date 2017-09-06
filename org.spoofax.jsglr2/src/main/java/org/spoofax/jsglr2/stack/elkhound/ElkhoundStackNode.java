package org.spoofax.jsglr2.stack.elkhound;

import java.util.ArrayList;
import java.util.Collections;
import org.spoofax.jsglr2.parsetable.IState;
import org.spoofax.jsglr2.stack.AbstractStackNode;
import org.spoofax.jsglr2.stack.EmptyStackPath;
import org.spoofax.jsglr2.stack.NonEmptyStackPath;
import org.spoofax.jsglr2.stack.StackLink;
import org.spoofax.jsglr2.stack.StackPath;
import org.spoofax.jsglr2.util.iterators.SingleElementIterable;
import org.spoofax.jsglr2.util.iterators.SingleElementWithListIterable;

public class ElkhoundStackNode<ParseForest> extends AbstractStackNode<ParseForest> {

    // Directed to the initial stack node
    private StackLink<ElkhoundStackNode<ParseForest>, ParseForest> firstLinkOut;
    private ArrayList<StackLink<ElkhoundStackNode<ParseForest>, ParseForest>> otherLinksOut;
    
    // Directed from the initial stack node
    private StackLink<ElkhoundStackNode<ParseForest>, ParseForest> firstLinkIn;
    private ArrayList<StackLink<ElkhoundStackNode<ParseForest>, ParseForest>> otherLinksIn;
    
	public int deterministicDepth;
	
	public ElkhoundStackNode(int stackNumber, IState state, int deterministicDepth) {
		super(stackNumber, state);
        this.deterministicDepth = deterministicDepth;
	}
    
    public Iterable<StackLink<ElkhoundStackNode<ParseForest>, ParseForest>> getLinksOut() {
        if (firstLinkOut == null)
            return Collections.EMPTY_LIST;
        else {
            if (otherLinksOut == null)
                return new SingleElementIterable<StackLink<ElkhoundStackNode<ParseForest>, ParseForest>>(firstLinkOut);
            else
                return new SingleElementWithListIterable<StackLink<ElkhoundStackNode<ParseForest>, ParseForest>>(firstLinkOut, otherLinksOut);
        }
    }
    
    public Iterable<StackLink<ElkhoundStackNode<ParseForest>, ParseForest>> getLinksIn() {
        if (firstLinkIn == null)
            return Collections.EMPTY_LIST;
        else {
            if (otherLinksIn == null)
                return new SingleElementIterable<StackLink<ElkhoundStackNode<ParseForest>, ParseForest>>(firstLinkIn);
            else
                return new SingleElementWithListIterable<StackLink<ElkhoundStackNode<ParseForest>, ParseForest>>(firstLinkIn, otherLinksIn);
        }
    }
    
    public StackLink<ElkhoundStackNode<ParseForest>, ParseForest> addOutLink(StackLink<ElkhoundStackNode<ParseForest>, ParseForest> link) {
        if (firstLinkOut == null)
            firstLinkOut = link;
        else {
            if (otherLinksOut == null)
                otherLinksOut = new ArrayList<StackLink<ElkhoundStackNode<ParseForest>, ParseForest>>();
            
            otherLinksOut.add(link);
        }
        
        link.to.addInLink(link);
        
        if (otherLinksOut == null) { // This means the first link is just added.
            deterministicDepth = link.to.deterministicDepth + 1;
        } else if (otherLinksOut.size() == 1) { // The second link is added; this means non-determinism
            deterministicDepth = 0;
            
            for (StackLink<ElkhoundStackNode<ParseForest>, ParseForest> linkIn : getLinksIn())
                linkIn.from.resetDeterministicDepth(1);
        } // We do not handle the case > 2, since the case == 2 already adjusted deterministic depths
        
        return link;
    }
	
	public StackLink<ElkhoundStackNode<ParseForest>, ParseForest> addLink(int linkNumber, ElkhoundStackNode<ParseForest> parent, ParseForest parseNode) {
		StackLink<ElkhoundStackNode<ParseForest>, ParseForest> link = new StackLink<ElkhoundStackNode<ParseForest>, ParseForest>(linkNumber, this, parent, parseNode);
		
		return addOutLink(link);
	}
    
    private void addInLink(StackLink<ElkhoundStackNode<ParseForest>, ParseForest> link) {
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
	
	public boolean allLinksRejected() {
	    if (firstLinkOut == null)
            return false;
        
        if (!firstLinkOut.isRejected())
            return false;
        
        if (otherLinksOut != null) {
            for (StackLink<ElkhoundStackNode<ParseForest>, ParseForest> link : otherLinksOut) {
                if (!link.isRejected())
                    return false;
            }
        }
        
        return true;
	}
    
    public StackPath<ElkhoundStackNode<ParseForest>, ParseForest> findDeterministicPathOfLength(int length) {
        if (length == 0)
            return new EmptyStackPath<ElkhoundStackNode<ParseForest>, ParseForest>(this);
        else if (length == 1)
            return new NonEmptyStackPath<ElkhoundStackNode<ParseForest>, ParseForest>(firstLinkOut);
        else {
            StackPath<ElkhoundStackNode<ParseForest>, ParseForest> pathAfterOwnLink = firstLinkOut.to.findDeterministicPathOfLength(length - 1);
            
            return new NonEmptyStackPath<ElkhoundStackNode<ParseForest>, ParseForest>(firstLinkOut, pathAfterOwnLink);
        }
    }
	
}
