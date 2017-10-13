package org.spoofax.jsglr2.stack.elkhound;

import org.spoofax.jsglr2.parseforest.AbstractParseForest;
import org.spoofax.jsglr2.parser.Parse;
import org.spoofax.jsglr2.parsetable.IState;
import org.spoofax.jsglr2.stack.AbstractStackNode;
import org.spoofax.jsglr2.stack.StackLink;

public abstract class AbstractElkhoundStackNode<ParseForest extends AbstractParseForest> extends AbstractStackNode<ParseForest> {
    
	public int deterministicDepth;
	
	public AbstractElkhoundStackNode(int stackNumber, IState state, int deterministicDepth) {
		super(stackNumber, state);
        this.deterministicDepth = deterministicDepth;
	}
    
    public abstract Iterable<StackLink<AbstractElkhoundStackNode<ParseForest>, ParseForest>> getLinksOut();
    
    public abstract StackLink<AbstractElkhoundStackNode<ParseForest>, ParseForest> getOnlyLinkOut();
    
    public abstract Iterable<StackLink<AbstractElkhoundStackNode<ParseForest>, ParseForest>> getLinksIn();
    
    public abstract StackLink<AbstractElkhoundStackNode<ParseForest>, ParseForest> addOutLink(StackLink<AbstractElkhoundStackNode<ParseForest>, ParseForest> link, Parse<AbstractElkhoundStackNode<ParseForest>, ParseForest> parse);
	
    public StackLink<AbstractElkhoundStackNode<ParseForest>, ParseForest> addOutLink(int linkNumber, AbstractElkhoundStackNode<ParseForest> parent, ParseForest parseNode, Parse<AbstractElkhoundStackNode<ParseForest>, ParseForest> parse) {
		StackLink<AbstractElkhoundStackNode<ParseForest>, ParseForest> link = new StackLink<AbstractElkhoundStackNode<ParseForest>, ParseForest>(linkNumber, this, parent, parseNode);
		
		return addOutLink(link, parse);
	}
    
	protected abstract void addInLink(StackLink<AbstractElkhoundStackNode<ParseForest>, ParseForest> link);
	
    public void resetDeterministicDepth(int deterministicDepth) {
        if (deterministicDepth != 0) {
            this.deterministicDepth = deterministicDepth;
            
            for (StackLink<AbstractElkhoundStackNode<ParseForest>, ParseForest> linkIn : getLinksIn())
                linkIn.from.resetDeterministicDepth(deterministicDepth + 1);
        }
    }
	
}
