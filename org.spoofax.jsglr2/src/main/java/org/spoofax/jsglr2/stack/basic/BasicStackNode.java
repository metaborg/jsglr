package org.spoofax.jsglr2.stack.basic;

import java.util.ArrayList;
import java.util.List;

import org.spoofax.jsglr2.parsetable.IState;
import org.spoofax.jsglr2.stack.AbstractStackNode;
import org.spoofax.jsglr2.stack.StackLink;

public class BasicStackNode<ParseForest> extends AbstractStackNode<ParseForest> {

    private final ArrayList<StackLink<BasicStackNode<ParseForest>, ParseForest>> linksOut = new ArrayList<StackLink<BasicStackNode<ParseForest>, ParseForest>>(); // Directed to the initial stack node
	
	public BasicStackNode(int stackNumber, IState state) {
		super(stackNumber, state);
	}
	
	public List<StackLink<BasicStackNode<ParseForest>, ParseForest>> getLinksOut() {
		return linksOut;
	}
    
    public StackLink<BasicStackNode<ParseForest>, ParseForest> addOutLink(StackLink<BasicStackNode<ParseForest>, ParseForest> link) {
        linksOut.add(link);
        
        return link;
    }
	
	public StackLink<BasicStackNode<ParseForest>, ParseForest> addLink(int linkNumber, BasicStackNode<ParseForest> parent, ParseForest parseNode) {
		StackLink<BasicStackNode<ParseForest>, ParseForest> link = new StackLink<BasicStackNode<ParseForest>, ParseForest>(linkNumber, this, parent, parseNode);
		
		return addOutLink(link);
	}
	
	public boolean allOutLinksRejected() {
		if (linksOut.isEmpty())
			return false;
		
		for (StackLink<BasicStackNode<ParseForest>, ParseForest> link : linksOut) {
			if (!link.isRejected())
				return false;
		}
		
		return true;
	}
	
}
