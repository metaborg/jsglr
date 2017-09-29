package org.spoofax.jsglr2.stack.elkhound;

import org.spoofax.jsglr2.parsetable.IState;
import org.spoofax.jsglr2.stack.AbstractStackNode;
import org.spoofax.jsglr2.stack.EmptyStackPath;
import org.spoofax.jsglr2.stack.NonEmptyStackPath;
import org.spoofax.jsglr2.stack.StackLink;
import org.spoofax.jsglr2.stack.StackPath;

public abstract class ElkhoundStackNode<ParseForest> extends AbstractStackNode<ParseForest> {
    
	public int deterministicDepth;
	
	public ElkhoundStackNode(int stackNumber, IState state, int deterministicDepth) {
		super(stackNumber, state);
        this.deterministicDepth = deterministicDepth;
	}
    
    public abstract Iterable<StackLink<ElkhoundStackNode<ParseForest>, ParseForest>> getLinksOut();
    
    public abstract StackLink<ElkhoundStackNode<ParseForest>, ParseForest> getOnlyLinkOut();
    
    public abstract Iterable<StackLink<ElkhoundStackNode<ParseForest>, ParseForest>> getLinksIn();
    
    public abstract StackLink<ElkhoundStackNode<ParseForest>, ParseForest> addOutLink(StackLink<ElkhoundStackNode<ParseForest>, ParseForest> link);
	
	public abstract StackLink<ElkhoundStackNode<ParseForest>, ParseForest> addOutLink(int linkNumber, ElkhoundStackNode<ParseForest> parent, ParseForest parseNode);
    
	protected abstract void addInLink(StackLink<ElkhoundStackNode<ParseForest>, ParseForest> link);
	
    public void resetDeterministicDepth(int deterministicDepth) {
        if (deterministicDepth != 0) {
            this.deterministicDepth = deterministicDepth;
            
            for (StackLink<ElkhoundStackNode<ParseForest>, ParseForest> linkIn : getLinksIn())
                linkIn.from.resetDeterministicDepth(deterministicDepth + 1);
        }
    }
    
    public StackPath<ElkhoundStackNode<ParseForest>, ParseForest> findDeterministicPathOfLength(int length) {
        if (length == 0)
            return new EmptyStackPath<ElkhoundStackNode<ParseForest>, ParseForest>(this);
        else {
            StackLink<ElkhoundStackNode<ParseForest>, ParseForest> onlyOwnLink = getOnlyLinkOut();
            
            StackPath<ElkhoundStackNode<ParseForest>, ParseForest> pathAfterOwnLink = onlyOwnLink.to.findDeterministicPathOfLength(length - 1);
            
            return new NonEmptyStackPath<ElkhoundStackNode<ParseForest>, ParseForest>(onlyOwnLink, pathAfterOwnLink);
        }
    }
	
}
