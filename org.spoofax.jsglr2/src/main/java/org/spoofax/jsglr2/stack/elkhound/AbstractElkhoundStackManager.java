package org.spoofax.jsglr2.stack.elkhound;

import org.spoofax.jsglr2.parseforest.AbstractParseForest;
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
        StackLink<AbstractElkhoundStackNode<ParseForest>, ParseForest> link = from.addOutLink(parse.stackLinkCount++, to, parseNode);
        
        parse.notify(observer -> observer.createStackLink(link));
        
        return link;
    }
    
    protected Iterable<StackLink<AbstractElkhoundStackNode<ParseForest>, ParseForest>> stackLinksOut(AbstractElkhoundStackNode<ParseForest> stack) {
        return stack.getLinksOut();
    }
    
}
