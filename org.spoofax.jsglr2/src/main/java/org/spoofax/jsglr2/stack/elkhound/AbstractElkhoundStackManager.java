package org.spoofax.jsglr2.stack.elkhound;

import org.spoofax.jsglr2.parseforest.AbstractParseForest;
import org.spoofax.jsglr2.parseforest.ParseForestManager;
import org.spoofax.jsglr2.parser.Parse;
import org.spoofax.jsglr2.parsetable.IState;
import org.spoofax.jsglr2.stack.StackLink;
import org.spoofax.jsglr2.stack.StackManager;

public abstract class AbstractElkhoundStackManager<StackNode extends AbstractElkhoundStackNode<ParseForest>, ParseForest extends AbstractParseForest> extends StackManager<AbstractElkhoundStackNode<ParseForest>, ParseForest> {
    
	protected abstract StackNode createStackNode(int stackNumber, IState state, int deterministicDepth);
	
    public AbstractElkhoundStackNode<ParseForest> createInitialStackNode(Parse<AbstractElkhoundStackNode<ParseForest>, ParseForest> parse, IState state) {
        AbstractElkhoundStackNode<ParseForest> newStackNode = createStackNode(parse.stackNodeCount++, state, 1);
        
        parse.notify(observer -> observer.createStackNode(newStackNode));
                
        return newStackNode;
    }
    
    public AbstractElkhoundStackNode<ParseForest> createStackNode(Parse<AbstractElkhoundStackNode<ParseForest>, ParseForest> parse, IState state) {
        AbstractElkhoundStackNode<ParseForest> newStackNode = createStackNode(parse.stackNodeCount++, state, 0);
        
        parse.notify(observer -> observer.createStackNode(newStackNode));
                
        return newStackNode;
    }
    
    public StackLink<AbstractElkhoundStackNode<ParseForest>, ParseForest> createStackLink(Parse<AbstractElkhoundStackNode<ParseForest>, ParseForest> parse, AbstractElkhoundStackNode<ParseForest> from, AbstractElkhoundStackNode<ParseForest> to, ParseForest parseNode) {
        StackLink<AbstractElkhoundStackNode<ParseForest>, ParseForest> link = from.addOutLink(parse.stackLinkCount++, to, parseNode, parse);
        
        parse.notify(observer -> observer.createStackLink(link));
        
        return link;
    }
    
    public DeterministicStackPath<AbstractElkhoundStackNode<ParseForest>, ParseForest> findDeterministicPathOfLength(ParseForestManager<ParseForest, ?, ?> parseForestManager, AbstractElkhoundStackNode<ParseForest> stack, int length) {
		AbstractElkhoundStackNode<ParseForest> lastStackNode = stack;
		AbstractElkhoundStackNode<ParseForest> currentStackNode = stack;
    		
		ParseForest[] parseForests = parseForestManager.parseForestsArray(length);
		
		for (int i = length - 1; i >= 0; i--) {
			StackLink<AbstractElkhoundStackNode<ParseForest>, ParseForest> link = currentStackNode.getOnlyLinkOut();

			if (parseForests != null) {		
				parseForests[i] = link.parseForest;
			}
			
			if (i == 0)
				lastStackNode = link.to;
			else
				currentStackNode = link.to;
		}
    		
    		return new DeterministicStackPath<AbstractElkhoundStackNode<ParseForest>, ParseForest>(parseForests, lastStackNode);
    }
    
    protected Iterable<StackLink<AbstractElkhoundStackNode<ParseForest>, ParseForest>> stackLinksOut(AbstractElkhoundStackNode<ParseForest> stack) {
        return stack.getLinksOut();
    }
    
}
