package org.spoofax.jsglr2.stack.elkhound;

import java.util.ArrayList;
import org.spoofax.jsglr2.parsetable.IState;
import org.spoofax.jsglr2.stack.AbstractStackNode;
import org.spoofax.jsglr2.stack.EmptyStackPath;
import org.spoofax.jsglr2.stack.NonEmptyStackPath;
import org.spoofax.jsglr2.stack.StackLink;
import org.spoofax.jsglr2.stack.StackPath;

public class ElkhoundStackNode<ParseForest> extends AbstractStackNode<ParseForest> {

    // Directed to the initial stack node
    private ArrayList<StackLink<ElkhoundStackNode<ParseForest>, ParseForest>> linksOut = new ArrayList<StackLink<ElkhoundStackNode<ParseForest>, ParseForest>>();
    
    // Directed from the initial stack node
    private ArrayList<StackLink<ElkhoundStackNode<ParseForest>, ParseForest>> linksIn = new ArrayList<StackLink<ElkhoundStackNode<ParseForest>, ParseForest>>();;
    
	public int deterministicDepth;
	
	public ElkhoundStackNode(int stackNumber, IState state, int deterministicDepth) {
		super(stackNumber, state);
        this.deterministicDepth = deterministicDepth;
	}
    
    public Iterable<StackLink<ElkhoundStackNode<ParseForest>, ParseForest>> getLinksOut() {
        return linksOut;
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
	
	public StackLink<ElkhoundStackNode<ParseForest>, ParseForest> addLink(int linkNumber, ElkhoundStackNode<ParseForest> parent, ParseForest parseNode) {
		StackLink<ElkhoundStackNode<ParseForest>, ParseForest> link = new StackLink<ElkhoundStackNode<ParseForest>, ParseForest>(linkNumber, this, parent, parseNode);
		
		return addOutLink(link);
	}
    
    private void addInLink(StackLink<ElkhoundStackNode<ParseForest>, ParseForest> link) {
        linksIn.add(link);
    }
    
    public void resetDeterministicDepth(int deterministicDepth) {
        if (deterministicDepth != 0) {
            this.deterministicDepth = deterministicDepth;
            
            for (StackLink<ElkhoundStackNode<ParseForest>, ParseForest> linkIn : getLinksIn())
                linkIn.from.resetDeterministicDepth(deterministicDepth + 1);
        }
    }
	
    public boolean allLinksRejected() {
        if (linksOut.isEmpty())
            return false;
        
        for (StackLink<ElkhoundStackNode<ParseForest>, ParseForest> link : linksOut) {
            if (!link.isRejected())
                return false;
        }
        
        return true;
    }
    
    public StackPath<ElkhoundStackNode<ParseForest>, ParseForest> findDeterministicPathOfLength(int length) {
        if (length == 0)
            return new EmptyStackPath<ElkhoundStackNode<ParseForest>, ParseForest>(this);
        else {
            StackLink<ElkhoundStackNode<ParseForest>, ParseForest> onlyOwnLink = this.linksOut.get(0);
            
            StackPath<ElkhoundStackNode<ParseForest>, ParseForest> pathAfterOwnLink = onlyOwnLink.to.findDeterministicPathOfLength(length - 1);
            
            return new NonEmptyStackPath<ElkhoundStackNode<ParseForest>, ParseForest>(onlyOwnLink, pathAfterOwnLink);
        }
    }
	
}
