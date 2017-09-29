package org.spoofax.jsglr2.stack.standard;

import java.util.ArrayList;
import java.util.List;

import org.spoofax.jsglr2.parsetable.IState;
import org.spoofax.jsglr2.stack.AbstractStackNode;
import org.spoofax.jsglr2.stack.StackLink;

public class StandardStackNode<ParseForest> extends AbstractStackNode<ParseForest> {

    private final ArrayList<StackLink<StandardStackNode<ParseForest>, ParseForest>> linksOut = new ArrayList<StackLink<StandardStackNode<ParseForest>, ParseForest>>(); // Directed to the initial stack node
	
	public StandardStackNode(int stackNumber, IState state) {
		super(stackNumber, state);
	}
	
	public List<StackLink<StandardStackNode<ParseForest>, ParseForest>> getLinksOut() {
		return linksOut;
	}
    
    public StackLink<StandardStackNode<ParseForest>, ParseForest> addOutLink(StackLink<StandardStackNode<ParseForest>, ParseForest> link) {
        linksOut.add(link);
        
        return link;
    }
	
	public StackLink<StandardStackNode<ParseForest>, ParseForest> addLink(int linkNumber, StandardStackNode<ParseForest> parent, ParseForest parseNode) {
		StackLink<StandardStackNode<ParseForest>, ParseForest> link = new StackLink<StandardStackNode<ParseForest>, ParseForest>(linkNumber, this, parent, parseNode);
		
		return addOutLink(link);
	}
	
	public boolean allOutLinksRejected() {
		if (linksOut.isEmpty())
			return false;
		
		for (StackLink<StandardStackNode<ParseForest>, ParseForest> link : linksOut) {
			if (!link.isRejected())
				return false;
		}
		
		return true;
	}
	
}
