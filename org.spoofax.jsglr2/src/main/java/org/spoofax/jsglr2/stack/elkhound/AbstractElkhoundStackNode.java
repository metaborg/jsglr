package org.spoofax.jsglr2.stack.elkhound;

import org.spoofax.jsglr2.parsetable.IState;
import org.spoofax.jsglr2.stack.AbstractStackNode;
import org.spoofax.jsglr2.stack.EmptyStackPath;
import org.spoofax.jsglr2.stack.NonEmptyStackPath;
import org.spoofax.jsglr2.stack.StackLink;
import org.spoofax.jsglr2.stack.StackPath;

public abstract class AbstractElkhoundStackNode<ParseForest> extends AbstractStackNode<ParseForest> {
    
	public int deterministicDepth;
	
	public AbstractElkhoundStackNode(int stackNumber, IState state, int deterministicDepth) {
		super(stackNumber, state);
        this.deterministicDepth = deterministicDepth;
	}
    
    public abstract Iterable<StackLink<AbstractElkhoundStackNode<ParseForest>, ParseForest>> getLinksOut();
    
    public abstract StackLink<AbstractElkhoundStackNode<ParseForest>, ParseForest> getOnlyLinkOut();
    
    public abstract Iterable<StackLink<AbstractElkhoundStackNode<ParseForest>, ParseForest>> getLinksIn();
    
    public abstract StackLink<AbstractElkhoundStackNode<ParseForest>, ParseForest> addOutLink(StackLink<AbstractElkhoundStackNode<ParseForest>, ParseForest> link);
	
    public StackLink<AbstractElkhoundStackNode<ParseForest>, ParseForest> addOutLink(int linkNumber, AbstractElkhoundStackNode<ParseForest> parent, ParseForest parseNode) {
		StackLink<AbstractElkhoundStackNode<ParseForest>, ParseForest> link = new StackLink<AbstractElkhoundStackNode<ParseForest>, ParseForest>(linkNumber, this, parent, parseNode);
		
		return addOutLink(link);
	}
    
	protected abstract void addInLink(StackLink<AbstractElkhoundStackNode<ParseForest>, ParseForest> link);
	
    public void resetDeterministicDepth(int deterministicDepth) {
        if (deterministicDepth != 0) {
            this.deterministicDepth = deterministicDepth;
            
            for (StackLink<AbstractElkhoundStackNode<ParseForest>, ParseForest> linkIn : getLinksIn())
                linkIn.from.resetDeterministicDepth(deterministicDepth + 1);
        }
    }
    
    public StackPath<AbstractElkhoundStackNode<ParseForest>, ParseForest> findDeterministicPathOfLength(int length) {
        if (length == 0)
            return new EmptyStackPath<AbstractElkhoundStackNode<ParseForest>, ParseForest>(this);
        else {
            StackLink<AbstractElkhoundStackNode<ParseForest>, ParseForest> onlyOwnLink = getOnlyLinkOut();
            
            StackPath<AbstractElkhoundStackNode<ParseForest>, ParseForest> pathAfterOwnLink = onlyOwnLink.to.findDeterministicPathOfLength(length - 1);
            
            return new NonEmptyStackPath<AbstractElkhoundStackNode<ParseForest>, ParseForest>(onlyOwnLink, pathAfterOwnLink);
        }
    }
	
}
